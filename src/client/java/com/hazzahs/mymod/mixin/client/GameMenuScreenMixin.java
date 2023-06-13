package com.hazzahs.mymod.mixin.client;

import com.hazzahs.mymod.screens.HazzahScreen;
import com.hazzahs.mymod.widgets.CustomButtonWidget;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// used to add a button to the pause menu, that then shows the HazzahScreen
@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text text) {
        super(text);
    }

    @Inject(at = @At("HEAD"), method = "initWidgets")
    private void initWidgets(CallbackInfo ci) {
        // Add "Hazzahs" button to top left of pause menu
        Text text = Text.of("Hazzah");
        ButtonWidget buttonWidget = new CustomButtonWidget(
                10, 10, 90, 20, text,
                button -> this.client.setScreen(new HazzahScreen(this, this.client.options)));
        this.addDrawableChild(buttonWidget);
    }
}
