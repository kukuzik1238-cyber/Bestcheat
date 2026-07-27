package com.example.mycheat.module;

import net.minecraft.client.MinecraftClient;

public abstract class Module {
    private final String name;
    private boolean enabled;
    protected final MinecraftClient mc = MinecraftClient.getInstance();

    public Module(String name) {
        this.name = name;
        this.enabled = false;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
}
