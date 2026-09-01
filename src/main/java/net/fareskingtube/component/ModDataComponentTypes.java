package net.fareskingtube.component;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<Boolean> HAS_HEART = register("has_heart",
            booleanBuilder -> booleanBuilder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL));

    public static final ComponentType<GameProfile> SELECTED_PLAYER = register("selected_player",
            gameProfileBuilder -> gameProfileBuilder.codec(Codecs.GAME_PROFILE_WITH_PROPERTIES).packetCodec(PacketCodecs.GAME_PROFILE));

    public static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(HardcoreRevived.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
        HardcoreRevived.LOGGER.info("Registering Data Component Types for " + HardcoreRevived.MOD_ID);
    }
}
