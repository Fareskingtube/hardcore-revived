package net.fareskingtube.client.screen.custom;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class BasicScreen extends Screen {

    // Replace "modid" with your mod id.
    // This points to: assets/modid/textures/gui/basic.png
//    private static final Identifier TEXTURE =
//            Identifier.of("modid", "textures/gui/basic.png");

    private int clickCount = 0;

    public BasicScreen() {
        super(Text.literal("Basic Screen"));
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 200;
        int buttonHeight = 20;
//
        int x = (this.width - buttonWidth) / 2;
        int y = this.height / 2 + 40;
//
//
//        // Button 1: updates its own text
//        this.addDrawableChild(
//                ButtonWidget.builder(Text.literal("Clicked: 0"), button -> {
//                            clickCount++;
//                            button.setMessage(Text.literal("Clicked: " + clickCount));
//                        })
//                        .dimensions(x, y, buttonWidth, buttonHeight)
//                        .build()
//        );
//
//        // Button 2: closes the screen
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Close"), button -> {
                            this.close();
                        })
                        .dimensions(x, y + 24, buttonWidth, buttonHeight)
                        .build()
        );
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        // Draws our custom solid background.
//        this.renderBackground(context, mouseX, mouseY, delta);

        // Draw text.
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("This text is not behind the blur"),
                this.width / 2,
                50,
                0xFFFFFF
        );

        // Draw buttons.
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        // If false, the game does not pause when this screen is open.
        return false;
    }
}
