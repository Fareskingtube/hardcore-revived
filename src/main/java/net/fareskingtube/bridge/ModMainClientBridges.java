package net.fareskingtube.bridge;

import com.mojang.authlib.GameProfile;

import java.util.List;
import java.util.function.BiConsumer;

public class ModMainClientBridges {
    public static BiConsumer<List<GameProfile>, GameProfile> OPEN_PLAYER_PICKER = (players, self) -> {
    };
}
