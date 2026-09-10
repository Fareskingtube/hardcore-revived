package net.fareskingtube.client.config.helper;

import net.fareskingtube.HardcoreRevived;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ConfigTranslations {

    public static MutableComponent getGroupName(String id) {
        return Component.translatable("config." + HardcoreRevived.MOD_ID + ".group." + id);
    }

    public static MutableComponent getGroupDescription(String id) {
        return Component.translatable("config." + HardcoreRevived.MOD_ID + ".group.description." + id);
    }

    public static MutableComponent getOptionName(String id) {
        return Component.translatable("config." + HardcoreRevived.MOD_ID + ".option." + id);
    }

    public static MutableComponent getOptionDescription(String id) {
        return Component.translatable("config." + HardcoreRevived.MOD_ID + ".option.description." + id);
    }
}