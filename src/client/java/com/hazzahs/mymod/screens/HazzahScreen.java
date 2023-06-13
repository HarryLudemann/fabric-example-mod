package com.hazzahs.mymod.screens;

import com.hazzahs.mymod.ModClient;
import com.hazzahs.mymod.widgets.CustomButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;



// to add button create class the extends ButtonAbstract, and add to init function.
public class HazzahScreen extends Screen {
    private final Screen parent;
    private final GameOptions settings;

    public HazzahScreen(Screen parent, GameOptions gameOptions) {
        super(Text.literal("Hazzah"));
        this.parent = parent;
        this.settings = gameOptions;
    }

    protected void init() {
        List<ButtonAbstract> buttonList = new ArrayList<>(2);
        buttonList.add(new AutoFishingButton());
        buttonList.add(new AutoSeedButton());
        buttonList.add(new AutoHarvestButton());;
        buttonList.add(new FlyingButton());

        for (ButtonAbstract button : generateCords(buttonList)) {
            this.addDrawableChild(button.getButton());
        }
    }

    private List<ButtonAbstract> generateCords(List<ButtonAbstract> buttonList) {
        int minButtonWidth = 150;
        int minButtonHeight = 25;
        int margin = 5;
        int numButtons = buttonList.size();

        int totalHeight = (numButtons * minButtonHeight) + ((numButtons - 1) * margin);
        int startY = (this.height - totalHeight) / 2;

        for (int i = 0; i < numButtons; i++) {
            int x = (this.width - minButtonWidth) / 2;
            int y = startY + (i * (minButtonHeight + margin));
            buttonList.get(i).setCord(new Cord(x, y, minButtonWidth, minButtonHeight));
        }

        return buttonList;
    }
}

class FlyingButton extends ButtonAbstract {
    @Override
    public void action() {
        ModClient.isFlyingEnabled = !ModClient.isFlyingEnabled;
        updateButtonText();
    }
    @Override
    public String getText() {
        if (ModClient.isFlyingEnabled) {
            return "Flying: Enabled";
        }
        return "Flying: Disabled";
    }
}

class AutoFishingButton extends ButtonAbstract {
    @Override
    public void action() {
        ModClient.isAutoFishingEnabled = !ModClient.isAutoFishingEnabled;
        updateButtonText();
    }
    @Override
    public String getText() {
        if (ModClient.isAutoFishingEnabled) {
            return "Auto Fishing: Enabled";
        }
        return "Auto Fishing: Disabled";
    }
}

class AutoSeedButton extends ButtonAbstract  {
    @Override
    public void action() {
        ModClient.isAutoSeedEnabled = !ModClient.isAutoSeedEnabled;
        updateButtonText();
    }
    @Override
    public String getText() {
        if (ModClient.isAutoSeedEnabled) {
            return "Auto Seed: Enabled";
        }
        return "Auto Seed: Disabled";
    }
}


class AutoHarvestButton extends ButtonAbstract  {
    @Override
    public void action() {
        ModClient.isAutoHarvestEnabled = !ModClient.isAutoHarvestEnabled;
        updateButtonText();
    }
    @Override
    public String getText() {
        if (ModClient.isAutoHarvestEnabled) {
            return "Auto Harvest: Enabled";
        }
        return "Auto Harvest: Disabled";
    }
}


class Cord {
    int x;
    int y;
    int width;
    int height;
    Cord(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}


abstract class ButtonAbstract {
    Cord cord;
    CustomButtonWidget button;

    void setCord(Cord cord) {
        this.cord = cord;
    }

    public CustomButtonWidget getButton() {
        if (button == null) {
            button = new CustomButtonWidget(
                    this.cord.x, this.cord.y, this.cord.width, this.cord.height, Text.of(getText()),
                    button -> action());
        }
        return button;
    }

    abstract void action();

    abstract String getText();

    protected void updateButtonText() {
        if (button != null) {
            button.setMessage(Text.of(getText()));
        }
    }
}