package net.fareskingtube.client.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;

import java.util.*;
import java.util.concurrent.CompletableFuture;

// SCGF (Small Claude Generated Function)
public final class PlayerProfileTextureCache {
    private static final Map<UUID, GameProfile> FILLED = new HashMap<>();
    private static final Set<UUID> PENDING = new HashSet<>();

    /**
     * Call every frame from renderWidget(). Never blocks.
     */
    public static GameProfile resolve(GameProfile bareProfile) {
        UUID id = bareProfile.getId();

        GameProfile filled = FILLED.get(id);
        if (filled != null) {
            return filled; // has textures - draw the real skin this frame
        }

        if (PENDING.add(id)) { // returns false if already in-flight
            CompletableFuture
                    .supplyAsync(() -> MinecraftClient.getInstance()
                                    .getSessionService() // verify exact accessor via autocomplete
                                    .fetchProfile(id, false), // verify exact signature - may differ in your build
                            Util.getMainWorkerExecutor()) // network work off the render thread
                    .thenAcceptAsync(result -> {
                        if (result != null && result.profile() != null) {
                            FILLED.put(id, result.profile());
                        }
                        PENDING.remove(id);
                    }, MinecraftClient.getInstance()) // hop back to client thread to touch the cache
                    .exceptionallyAsync(ex -> {
                        PENDING.remove(id);
                        return null;
                    }, MinecraftClient.getInstance());
        }

        return bareProfile; // this frame: unfilled, draws default - next frame picks up the fix
    }
}