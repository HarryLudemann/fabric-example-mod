package com.hazzahs.mymod.mixin.client;

import com.hazzahs.mymod.AutoFishing;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// used to access the tick method, this is used to add delays
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow static MinecraftClient instance;

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        AutoFishing.tick(instance);
    }
}

