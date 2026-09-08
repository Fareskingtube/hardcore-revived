package net.fareskingtube.client.config.helper;

import net.fareskingtube.HardcoreRevived;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ConfigTranslations {

    public static MutableText getGroupName(String id) {
        return Text.translatable("config." + HardcoreRevived.MOD_ID + ".group." + id);
    }

    public static MutableText getGroupDescription(String id) {
        return Text.translatable("config." + HardcoreRevived.MOD_ID + ".group.description." + id);
    }

    public static MutableText getOptionName(String id) {
        return Text.translatable("config." + HardcoreRevived.MOD_ID + ".option." + id);
    }

    public static MutableText getOptionDescription(String id) {
        return Text.translatable("config." + HardcoreRevived.MOD_ID + ".option.description." + id);
    }
}