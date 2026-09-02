package net.fareskingtube.persistent;

import com.mojang.authlib.GameProfile;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record QueuedPlayer(GameProfile player, BlockPos pos, RegistryKey<World> world) {
}
