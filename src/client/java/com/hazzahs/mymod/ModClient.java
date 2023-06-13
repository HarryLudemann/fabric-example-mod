package com.hazzahs.mymod;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModClient implements ClientModInitializer {

    public static boolean isAutoFishingEnabled = false;
    public static boolean isFlyingEnabled = false;
    public static boolean isAutoSeedEnabled = false;
    public static boolean isAutoHarvestEnabled = false;
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        this.LOGGER.info("Client: Hazzah's Mod Menu!");

        // register auto farming client tick event handler
        AutoFarmer.register();
        // register flying tick event handler
        Flying.register();
    }
}