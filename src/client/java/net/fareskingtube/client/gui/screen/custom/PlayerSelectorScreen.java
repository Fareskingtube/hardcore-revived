package net.fareskingtube.client.gui.screen.custom;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    @Override
    protected void renderDarkening(DrawContext context) {

    }

    @Override
    protected void applyBlur(float delta) {
        super.applyBlur(delta);
    }

    private void refreshList(String query) {
        listWidget.clear();
        for (GameProfile p : listedPlayers) {
            String name = p.getName();
            if (query.isEmpty() || name.toLowerCase().contains(query.toLowerCase())) {
                boolean isSelf = p.getId().equals(self.getId());
                listWidget.addPlayerEntry(new PlayerListWidget.Entry(p, isSelf, selected -> {
                    onSelect.accept(selected);   // <-- output fires here
                    if (client == null) return;
                    this.client.setScreen(null);
                }));
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
        public void addPlayerEntry(Entry entry) {
            super.addEntry(entry);
        }

        /* The entry the button is in the list */
        public static class Entry extends ElementListWidget.Entry<Entry> {
            private final ButtonWidget selectButton;

            public Entry(GameProfile player, boolean isSelf, Consumer<GameProfile> onPick) {
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
                private static final Map<UUID, Identifier> SKIN_CACHE = new HashMap<>();
                private final boolean isSelf;
                private final GameProfile player;

                protected PlayerButtonWidget(int x, int y, int width, int height, GameProfile player, boolean isSelf, PressAction onPress) {
                    super(x, y, width, height, Text.literal(""), onPress, DEFAULT_NARRATION_SUPPLIER);
                    this.player = player;

                    this.isSelf = isSelf;
                }

                /* Gets Player skin anc caches it */
                private static Identifier resolveSkin(GameProfile profile) {
                    UUID uuid = profile.getId();

                    Identifier cached = SKIN_CACHE.get(uuid);
                    if (cached != null) return cached;

                    var networkHandler = MinecraftClient.getInstance().getNetworkHandler();
                    if (networkHandler != null) {
                        PlayerListEntry entry = networkHandler.getPlayerListEntry(uuid);
                        if (entry != null) {
                            Identifier texture = entry.getSkinTextures().texture();
                            SKIN_CACHE.put(uuid, texture);
                            return texture;
                        }
                    }

                    Identifier fallback = DefaultSkinHelper.getSkinTextures(uuid).texture();
                    SKIN_CACHE.put(uuid, fallback);

                    MinecraftClient.getInstance().getSkinProvider().fetchSkinTextures(profile)
                            .thenAcceptAsync(skinTextures -> SKIN_CACHE.put(uuid, skinTextures.texture()),
                                    MinecraftClient.getInstance())
                            .exceptionally(throwable -> null);

                    return fallback;
                }

                int padding = 8;

                /* Called every frame, Also where the text and head texture are actually rendered */
                @Override
                protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                    drawCustomBackground(context);
                    /* Center a div and draw the head */
                    int headSize = this.getHeight() - padding;
                    int headX = this.getX() + padding / 2;
                    int headY = this.getY() + padding / 2;

                    Identifier skinTexture = resolveSkin(player);
                    PlayerSkinDrawer.draw(context, skinTexture, headX, headY, headSize);

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
                            ENTRY_WIDTH, 18, 32, 32);
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
