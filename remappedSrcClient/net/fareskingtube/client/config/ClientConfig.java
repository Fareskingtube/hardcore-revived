package net.fareskingtube.client.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.resources.ResourceLocation;

public class ClientConfig {
    public static ConfigClassHandler<ClientConfig> HANDLER = ConfigClassHandler.createBuilder(ClientConfig.class)
            .id(ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, "client-config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve(HardcoreRevived.MOD_ID + "-client.json5"))
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

    @SerialEntry(value = "applyDarkening", comment = "Whether the player selection screen has a dark background\nDefault: true")
    public boolean isApplyDarkening = true;

    @SerialEntry(value = "applyBlur", comment = "Whether the player selection screen has a blur on the background\nDefault: true")
    public boolean isApplyBlur = true;
}
