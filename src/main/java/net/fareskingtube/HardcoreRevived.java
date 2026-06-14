package net.fareskingtube;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HardcoreRevived implements ModInitializer {
    public static final String MOD_ID = "hardcore-revived";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Loading...");
    }
}