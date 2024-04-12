package com.example;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class ExampleModClient implements ClientModInitializer {
    
    public static final Logger LOGGER = LoggerFactory.getLogger("sonification");
    CommThread t;
    String lastHeld;

    @Override
    public void onInitializeClient() {
        t = new CommThread();
        t.start();
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                if (client.world != null) {
                    String held = client.player.getHandItems().iterator().next().getItem().toString();
                    if (held != lastHeld) {
                        lastHeld = held;
                        LOGGER.info("Held item switched to: " + held);
                    }
                    if (client.player.age % 20 == 0) { // Print every second (20 ticks)
                        //t.send(client);
                        client.player.getStatusEffects().forEach(new Consumer<StatusEffectInstance>() {
                            @Override
                            public void accept(StatusEffectInstance s) {
                                LOGGER.info(s.getEffectType().getName().getString() + " " + s.getAmplifier() + " " + s.getDuration());
                            }
                        });
                        LOGGER.info("" + client.player.getAttackCooldownProgress(100));
                    }
                }
            }
        });
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            LOGGER.info("Attack callback");
            return ActionResult.PASS;
        });
    }



    private void printPlayerCoordinates(MinecraftClient client) {
        BlockPos playerPos = client.player.getBlockPos();
        int x = playerPos.getX();
        int y = playerPos.getY();
        int z = playerPos.getZ();
        String out = String.format("Player coordinates: X=%d, Y=%d, Z=%d%n", x, y, z);
        LOGGER.info(out);
    }
}
