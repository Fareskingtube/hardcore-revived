package net.fareskingtube.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.resources.ResourceLocation;

public class CommonConfig {
    public static ConfigClassHandler<CommonConfig> HANDLER = ConfigClassHandler.createBuilder(CommonConfig.class)
            .id(ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, "common-config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(HardcoreRevived.MOD_ID + "-common.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    public static void save() {
        HANDLER.save();
    }

    public static void load() {
        HANDLER.load();
    }

    @SerialEntry(comment = "Hardcore Heart activation time in ticks\nDefault: 20 (1 Second)")
    public int heartActivationTime = 20;

    @SerialEntry(comment = "The amount of hp a player loses when killing another player\nDefault: 4 (2 Hearts)")
    public int killPenalty = 4;
}
