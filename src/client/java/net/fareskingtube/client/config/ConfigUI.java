package net.fareskingtube.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.fareskingtube.client.config.helper.ConfigTranslations;
import net.fareskingtube.config.CommonConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;


public class ConfigUI {
    public static Screen createConfigScreen(Screen parent) {
        ClientConfig instance = ClientConfig.HANDLER.instance();
        CommonConfig commonInstance = CommonConfig.HANDLER.instance();
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("config.hardcore-revived.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.hardcore-revived.category.client"))
                        .tooltip(Text.translatable("config.hardcore-revived.category.description.client"))
                        .group(OptionGroup.createBuilder()
                                .name(ConfigTranslations.getGroupName("player_selection_screen"))
                                .description(OptionDescription.of(ConfigTranslations.getGroupDescription("player_selection_screen")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(ConfigTranslations.getOptionName("apply_blur"))
                                        .description(OptionDescription.of(ConfigTranslations.getOptionDescription("apply_blur")))
                                        .binding(true, () -> instance.isApplyBlur, val -> instance.isApplyBlur = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(ConfigTranslations.getOptionName("apply_darkening"))
                                        .description(OptionDescription.of(ConfigTranslations.getOptionDescription("apply_darkening")))
                                        .binding(true, () -> instance.isApplyDarkening, val -> instance.isApplyDarkening = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.hardcore-revived.category.common"))
                        .tooltip(Text.translatable("config.hardcore-revived.category.description.common"))
                        .group(OptionGroup.createBuilder()
                                .name(ConfigTranslations.getGroupName("items"))
                                .option(Option.<Integer>createBuilder()
                                        .name(ConfigTranslations.getOptionName("heartActivationTime"))
                                        .description(OptionDescription.of(ConfigTranslations.getOptionDescription("heartActivationTime")))
                                        .binding(20, () -> commonInstance.heartActivationTime,
                                                val -> commonInstance.heartActivationTime = val)
                                        .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                                                .min(1)
                                                .formatValue(val -> Text.literal(val + "t")))
                                        .build())
                                .build())
                        .build())
                .save(ClientConfig::save)
                .save(CommonConfig::save)
                .build()
                .generateScreen(parent);
    }


}

