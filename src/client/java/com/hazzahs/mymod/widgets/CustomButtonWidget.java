package com.hazzahs.mymod.widgets;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

// Used to create buttons within screens
public class CustomButtonWidget extends ButtonWidget {
    public CustomButtonWidget(int x, int y, int width, int height, Text text, PressAction onPress) {
        super(x, y, width, height, text, onPress, button -> (MutableText) text);
    }
}
