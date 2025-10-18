package com.gshelgaas.messageserver;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageMod implements ModInitializer {
    public static final String MOD_ID = "message-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("MessageMod initialized");
    }
}