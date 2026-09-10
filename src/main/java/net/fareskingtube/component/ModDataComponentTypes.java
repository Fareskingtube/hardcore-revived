package net.fareskingtube.component;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final DataComponentType<Boolean> HAS_HEART = register("has_heart",
            booleanBuilder -> booleanBuilder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DataComponentType<GameProfile> SELECTED_PLAYER = register("selected_player",
            gameProfileBuilder -> gameProfileBuilder.persistent(ExtraCodecs.GAME_PROFILE).networkSynchronized(ByteBufCodecs.GAME_PROFILE));

    public static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, name),
                builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
        HardcoreRevived.LOGGER.info("Registering Data Component Types for " + HardcoreRevived.MOD_ID);
    }
}
