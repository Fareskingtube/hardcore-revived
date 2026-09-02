package net.fareskingtube.client.gui.screen.custom;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fareskingtube.client.util.PlayerProfileTextureCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PlayerSelectorScreen extends Screen {
    private final List<GameProfile> listedPlayers;
    private final GameProfile self;
    private final Consumer<GameProfile> onSelect;
    private PlayerListWidget listWidget;
    private int panelX, panelY, panelWidth, panelHeight;

    public PlayerSelectorScreen(List<GameProfile> listedPlayers, GameProfile self, Consumer<GameProfile> onSelect) {
        super(Text.translatable("gui.hardcore-revived.player_selector_screen.title"));
        this.listedPlayers = listedPlayers;
        this.self = self;
        this.onSelect = onSelect;
    }

    @Override
    protected void init() {
        super.init();

        int viewportWidth = this.width / 2;

        int searchFieldWidth = 175;
        TextFieldWidget searchField = new TextFieldWidget(this.textRenderer, (viewportWidth - searchFieldWidth / 2), 20, searchFieldWidth + 10, 20,
                Text.translatable("gui.hardcore-revived.player_selector_screen.search")) {
            @Override
            public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                RenderSystem.enableBlend();

                context.setShaderColor(0.8f, 0.8f, 0.8f, 0.8f);
                context.drawTexture(
                        Identifier.of("minecraft", "textures/gui/menu_list_background.png"),
                        this.getX(), this.getY(),
                        0f, 0f,
                        this.getWidth(), this.getHeight(), 32, 32);
                context.setShaderColor(1.0f, 1.0f, 1.0f, 1f);

                if (this.isHovered()) {
                    context.fill(this.getX(), this.getY(),
                            this.getX() + this.getWidth(), this.getY() + this.getHeight(),
                            0x15FFFFFF);
                }


                context.getMatrices().push();
                context.getMatrices().translate(5, 6.5f, 0);

                super.renderWidget(context, mouseX, mouseY, delta);
                RenderSystem.disableBlend();
                context.setShaderColor(1, 1f, 1f, 1f);
                context.getMatrices().pop();
            }
        };
        searchField.setDrawsBackground(false);
        searchField.setPlaceholder(Text.translatable("gui.hardcore-revived.player_selector_screen.search").formatted(Formatting.DARK_GRAY));
        searchField.setChangedListener(this::refreshList);
        this.addDrawableChild(searchField);


        this.listWidget = new PlayerListWidget(this.client, this.width, this.height - 85, 42, 20);
        this.addSelectableChild(this.listWidget);
        refreshList("");

//        SCGF
        int padding = 5;
        this.panelX = Math.min(searchField.getX(), this.listWidget.getRowLeft()) - padding;
        this.panelY = searchField.getY() - padding;
        int right = Math.max(searchField.getX() + searchField.getWidth(),
                this.listWidget.getRowLeft() + this.listWidget.getRowWidth()) + padding;
        int bottom = this.listWidget.getY() + this.listWidget.getHeight() + padding;
        this.panelWidth = right - this.panelX;
        this.panelHeight = bottom - this.panelY;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        listWidget.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // TODO: Add Blur and Darkening to the config
        this.renderDarkening(context);
        this.applyBlur(delta);
        RenderSystem.enableBlend();
        context.setShaderColor(1.0f, 1.0f, 1.0f, 0.8f);
        context.drawTexture(
                Identifier.of("minecraft", "textures/gui/menu_list_background.png"),
                panelX, panelY,
                0f, 0f,
                panelWidth, panelHeight, 32, 32);
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
        context.drawBorder(panelX, panelY, panelWidth, panelHeight, 0x6000000);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void refreshList(String query) {
        listWidget.clear();
        for (GameProfile p : listedPlayers) {
            String name = p.getName();
            if (query.isEmpty() || name.toLowerCase().contains(query.toLowerCase())) {
                boolean isSelf = p.getId().equals(self.getId());
                // if (isSelf) continue;
                listWidget.addPlayerEntry(p, isSelf, selected -> {
                    onSelect.accept(selected);
                    if (client == null) return;
                    this.client.setScreen(null);
                });
            }
        }
    }

    /* The player list */
    public static class PlayerListWidget extends ElementListWidget<PlayerListWidget.Entry> {
        private static final int ENTRY_WIDTH = 175;
        private static final int PADDING = 10;

        public PlayerListWidget(MinecraftClient client, int width, int height, int top, int itemHeight) {
            super(client, width, height, top, itemHeight);
        }

        /* Disables the black background when opening the menu */
        @Override
        protected void drawMenuListBackground(DrawContext context) {
//            super.drawMenuListBackground(context);
        }

        /* Disables the header and footer borders */
        @Override
        protected void drawHeaderAndFooterSeparators(DrawContext context) {

        }

        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            if (this.children().isEmpty()) {
                int centerX = this.getRowLeft() + this.getRowWidth() / 2;
                int centerY = this.getY() + this.getHeight() / 2 - PADDING * 2 + PADDING / 2;

                context.drawCenteredTextWithShadow(
                        this.client.textRenderer,
                        Text.translatable("gui.hardcore-revived.player_selector_screen.no_players"),
                        centerX,
                        centerY,
                        0xAAAAAA
                );
            }
            super.renderWidget(context, mouseX, mouseY, delta);
        }

        /* Centering a div */
        @Override
        public int getRowWidth() {
            return ENTRY_WIDTH + PADDING;
        }

        @Override
        public int getRowLeft() {
            return (this.getX() + this.getWidth() / 2 - this.getRowWidth() / 2) + PADDING / 2;
        }

        /* Add scrollbar */
        @Override
        protected int getScrollbarX() {
            return (getRowLeft() + getRowWidth() + 4 - PADDING);
        }

        /* Clears listed entries */
        public void clear() {
            super.clearEntries();
        }

        /* Adds a new entry */
        public void addPlayerEntry(GameProfile player, boolean isSelf, Consumer<GameProfile> onPick) {
            super.addEntry(new Entry(this, player, isSelf, onPick));
        }

        private boolean hasScrollbar() {
            return this.getMaxScroll() > 0;
        }

        /* The entry the button is in the list */
        public static class Entry extends ElementListWidget.Entry<Entry> {
            private final ButtonWidget selectButton;
            private final PlayerListWidget parent;

            public Entry(PlayerListWidget parent, GameProfile player, boolean isSelf, Consumer<GameProfile> onPick) {
                this.parent = parent;
                this.selectButton = new PlayerButtonWidget(
                        0,
                        0,
                        ENTRY_WIDTH,
                        18,
                        player,
                        isSelf,
                        button -> onPick.accept(player));
            }


            /* Renders the entry */
            @Override
            public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                int width = parent.hasScrollbar() ? entryWidth - PADDING : entryWidth;
                selectButton.setWidth(width);
                selectButton.setPosition(x, y);
                selectButton.render(context, mouseX, mouseY, tickDelta);
            }

            /* I'm going to be honest IDK what these two do */
            @Override
            public List<? extends Selectable> selectableChildren() {
                return List.of(selectButton);
            }

            @Override
            public List<? extends Element> children() {
                return List.of(selectButton);
            }

            /* Renders the button that has a player head and their username */

            public static class PlayerButtonWidget extends ButtonWidget {
                private final boolean isSelf;
                private final GameProfile player;

                protected PlayerButtonWidget(int x, int y, int width, int height, GameProfile player, boolean isSelf, PressAction onPress) {
                    super(x, y, width, height, Text.literal(""), onPress, DEFAULT_NARRATION_SUPPLIER);
                    this.player = player;

                    this.isSelf = isSelf;
                }


                int padding = 8;

                private static final Map<UUID, Identifier> SKIN_CACHE = new HashMap<>();
                private static final Set<UUID> PENDING_FETCHES = new HashSet<>();

                private static Identifier resolveSkin(GameProfile profile) {
                    UUID id = profile.getId();

                    if (SKIN_CACHE.containsKey(id)) {
                        return SKIN_CACHE.get(id); // cache hit - render immediately
                    }

                    if (!PENDING_FETCHES.contains(id)) {
                        PENDING_FETCHES.add(id);

                        MinecraftClient.getInstance()
                                .getSkinProvider() // verify exact accessor via autocomplete
                                .fetchSkinTextures(profile)
                                .thenAcceptAsync(skinTextures -> {
                                    SKIN_CACHE.put(id, skinTextures.texture());
                                    PENDING_FETCHES.remove(id);
                                    // no explicit "refresh" call needed - next render() picks it up
                                }, MinecraftClient.getInstance()); // <- executor param is the key bit
                    }

                    // this frame: fall back while the fetch is in flight
                    return DefaultSkinHelper.getSkinTextures(id).texture();
                }

                /* Called every frame, Also where the text and head texture are actually rendered */
                @Override
                protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                    drawCustomBackground(context);
                    /* Center a div and draw the head */
                    int headSize = this.getHeight() - padding;
                    int headX = this.getX() + padding / 2;
                    int headY = this.getY() + padding / 2;

                    GameProfile profileToRender = PlayerProfileTextureCache.resolve(this.player);

                    CompletableFuture<SkinTextures> skinFuture = MinecraftClient.getInstance()
                            .getSkinProvider()
                            .fetchSkinTextures(profileToRender);

                    SkinTextures textures = skinFuture.getNow(
                            MinecraftClient.getInstance().getSkinProvider().getSkinTextures(profileToRender)
                    );

                    PlayerSkinDrawer.draw(context, textures, headX, headY, headSize);

                    /* Draw text */
                    int textX = headX + headSize + 4;
                    int textY = this.getY() + (this.getHeight() - 8) / 2;
                    context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer,
                            isSelf ? this.player.getName() + " (You)" : this.player.getName(), textX, textY, 0xFFFFFF);
                }

                /* Pretty self-explanatory. It draws the background.. Also handles the hover */
                private void drawCustomBackground(DrawContext context) {
                    RenderSystem.enableBlend();

                    context.setShaderColor(0.8f, 0.8f, 0.8f, 0.8f);
                    context.drawTexture(
                            Identifier.of("minecraft", "textures/gui/menu_list_background.png"),
                            this.getX(), this.getY(),
                            0f, 0f,
                            this.getWidth(), 18, 32, 32);
                    context.setShaderColor(1.0f, 1.0f, 1.0f, 1f);

                    if (this.isHovered()) {
                        context.fill(this.getX(), this.getY(),
                                this.getX() + this.getWidth(), this.getY() + this.getHeight(),
                                0x15FFFFFF);
                    }

                    RenderSystem.disableBlend();
                }
            }

        }
    }
}
