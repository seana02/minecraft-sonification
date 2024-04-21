package com.example.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.net.DatagramSocket;
import java.net.InetAddress;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.CustomItem;

@Mixin(PlayerEntity.class)
public class DamageTakenMixin {
    // DatagramSocket sock;
    // InetAddress addr;

	@Inject(at = @At("HEAD"), method = "applyDamage")
	private void applyDamage(CallbackInfo info) {
		// This code is injected into the start of MinecraftClient.run()V
        // try {
        //     sock = new DatagramSocket();
        //     addr = InetAddress.getByName("143.215.84.222");
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // JSONObject data = new JSONObject();
        // data.put("timestamp", startTime - System.currentTimeMillis());
        // data.put("type", "GameEvent");
        // data.put("event", "attacked");
	}
}
