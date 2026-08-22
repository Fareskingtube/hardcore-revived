package net.fareskingtube.client.screen.custom;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Consumer;

public class PlayerSelectorScreen extends Screen {
    private final List<PlayerEntity> listedPlayers;
    private final PlayerEntity self;
    private final Consumer<PlayerEntity> onSelect;
    private TextFieldWidget searchField;

    public PlayerSelectorScreen(List<PlayerEntity> listedPlayers, PlayerEntity self, Consumer<PlayerEntity> onSelect) {
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
        this.searchField = new TextFieldWidget(this.textRenderer, viewportWidth - searchFieldWidth / 2, 20, searchFieldWidth, 20,
                Text.translatable("gui.hardcore-revived.player_selector_screen.search"))
        ;
        this.searchField.setPlaceholder(Text.translatable("gui.hardcore-revived.player_selector_screen.search").formatted(Formatting.DARK_GRAY));
        this.searchField.setChangedListener(query -> {
        }); // fires on every keystroke
        this.searchField.setMaxLength(17);
        this.addDrawableChild(this.searchField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
//        listWidget.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
