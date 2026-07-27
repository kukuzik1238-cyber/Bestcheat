package com.example.mycheat;

import com.example.mycheat.module.Module;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.ArrayList;
import java.util.List;

public class MyCheat implements ClientModInitializer {
    public static final List<Module> modules = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        // Сюда добавляешь вызовы твоих сурсов:
        // modules.add(new MyModule());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            for (Module module : modules) {
                if (module.isEnabled()) {
                    module.onTick();
                }
            }
        });
    }
}
