package net.fareskingtube.client.bridge;


import net.fareskingtube.bridge.ModMainClientBridges;
import net.fareskingtube.client.screen.custom.PlayerSelectorScreen;
import net.minecraft.client.MinecraftClient;

public class ModClientBridges {
    public static void init() {
        ModMainClientBridges.OPEN_PLAYER_PICKER = (players, self) -> {
            MinecraftClient.getInstance().setScreen(
                    new PlayerSelectorScreen(players, self, chosen -> {
                        /* Output here */
                    })
            );
        };
    }
}
