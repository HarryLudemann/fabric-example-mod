package com.hazzahs.mymod.mixin.client;

import com.hazzahs.mymod.AutoFishing;
import com.hazzahs.mymod.ModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// used in auto fishing, detects caught fish
@Mixin(FishingBobberEntity.class)
public abstract class FisherBobberEntityMixin {
    @Shadow private boolean caughtFish;
    @Inject(at = @At("TAIL"), method = "onTrackedDataSet")
    public void onTrackedDataSet(TrackedData<?> data, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (caughtFish && ModClient.isAutoFishingEnabled) {
            assert MinecraftClient.getInstance().interactionManager != null;
            MinecraftClient.getInstance().interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            AutoFishing.startCounter(); // call the above line interact method with a short delay
        }
    }
}


