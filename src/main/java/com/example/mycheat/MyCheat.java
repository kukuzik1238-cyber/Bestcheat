package com.example.mycheat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class MyCheat implements ClientModInitializer {
    // Включение и отключение функций (true - включено, false - выключено)
    public static boolean flyEnabled = true;       // Полёт
    public static boolean speedEnabled = true;     // Ускорение
    public static boolean triggerEnabled = true;   // Авто-атака при наведении
    public static boolean espEnabled = true;       // Подсветка игроков и мобов

    @Override
    public void onInitializeClient() {
        // Логика, выполняемая каждый игровой тик
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            // 1. Полёт (Fly)
            if (flyEnabled) {
                client.player.getAbilities().flying = true;
            }

            // 2. Быстрый бег (Speed)
            if (speedEnabled && client.player.isOnGround() && client.player.input.movementForward > 0) {
                client.player.setVelocity(
                    client.player.getVelocity().x * 1.4,
                    client.player.getVelocity().y,
                    client.player.getVelocity().z * 1.4
                );
            }

            // 3. Авто-удар (Triggerbot)
            if (triggerEnabled && client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                EntityHitResult hitResult = (EntityHitResult) client.crosshairTarget;
                Entity target = hitResult.getEntity();

                if (target instanceof LivingEntity && target.isAlive() && client.player.getAttackCooldownProgress(0.5f) >= 1.0f) {
                    if (client.interactionManager != null) {
                        client.interactionManager.attackEntity(client.player, target);
                        client.player.swingHand(Hand.MAIN_HAND);
                    }
                }
            }
        });

        // 4. Подсветка сквозь стены (ESP / Glow)
        WorldRenderEvents.END.register(context -> {
            if (!espEnabled) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world == null || client.player == null) return;

            for (Entity entity : client.world.getEntities()) {
                if (entity instanceof LivingEntity && entity != client.player && entity.isAlive()) {
                    entity.setGlowing(true); // Включает ванильное свечение вокруг сущности
                }
            }
        });
    }
}
