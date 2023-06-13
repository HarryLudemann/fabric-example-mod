package com.hazzahs.mymod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;

// used to create a short delay between to reel in and the recast of the fishing rod
// auto fishing uses the fisher bobber screen mixin and the minecraft client mixin.
public class AutoFishing {
    public static int counter = -1;

    public static void tick(MinecraftClient client) {
        if (counter > 0) {
            counter--;
        } else if (counter == 0) {
            ModClient.LOGGER.info("Caught Fish");
            assert client.getInstance().interactionManager != null;
            client.getInstance().interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            counter = -1;
        }
    }

    public static void startCounter() {
        counter = 20;
    }
}
