package com.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.util.HashMap;

import org.json.JSONObject;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class CommThread extends Thread {
    private DatagramSocket sock;
    private InetAddress addr;
    private byte[] buf;

    private static long startTime;

    private HashMap<String, Integer> maxDurability;

    public CommThread() {
        buf = new byte[4096];
        startTime = System.currentTimeMillis();
        try {
            sock = new DatagramSocket();
    addr = InetAddress.getByName("143.215.83.161");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        maxDurability = new HashMap<>();
        maxDurability.put("gold", 32);
        maxDurability.put("wood", 59);
        maxDurability.put("ston", 131);
        maxDurability.put("iron", 250);
        maxDurability.put("diam", 1561);
        maxDurability.put("neth", 2031);
    }
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                // ExampleModClient.LOGGER.info(" run: " + this.sendTest("" + 2));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String sendPlayerData(MinecraftClient client) {
        JSONObject data = new JSONObject();
        data.put("timestamp", System.currentTimeMillis() - startTime);
        data.put("type", "PlayerData");
        data.put("health", Math.round(client.player.getHealth()));
        data.put("hunger", client.player.getHungerManager().getFoodLevel());
        if (client.player.getStatusEffects().size() > 1) {
            StatusEffectInstance[] statusArray = client.player.getStatusEffects().toArray(new StatusEffectInstance[0]);
            ExampleModClient.LOGGER.info(client.player.getStatusEffects().toString());
            JSONObject[] effects = new JSONObject[client.player.getStatusEffects().size() - 1];
            int NVPassed = 0;
            for (int i = 0; i < effects.length; i++) {
                if (statusArray[i].getEffectType().getName().getString().equals("Night Vision")) { NVPassed = 1; continue; }
                effects[i - NVPassed] = new JSONObject();
                effects[i - NVPassed].put("name", statusArray[i].getEffectType().getName().getString());
                effects[i - NVPassed].put("time", statusArray[i].getDuration() / 20.0f);
            }
            data.put("effects", effects);
        }
        ExampleModClient.LOGGER.info(data.toString());
        send(data.toString());
        return "done";
    }

    public String sendSwitchEvent(MinecraftClient client) {
        JSONObject data = new JSONObject();
        data.put("timestamp", System.currentTimeMillis() - startTime);
        data.put("type", "GameEvent");
        data.put("event", "itemSwitch");
        String mainhand = client.player.getHandItems().iterator().next().getItem().toString();
        if (mainhand.endsWith("_sword")
                || mainhand.endsWith("_axe") || mainhand.endsWith("_pickaxe")
                || mainhand.endsWith("_shovel") || mainhand.endsWith("_hoe")) {
            data.put("item", mainhand.substring(mainhand.indexOf("_") + 1));
        } else if (mainhand.equals("cooked_beef")) {
            data.put("item", "food");
        } else if (mainhand.equals("potion")) {
            data.put("item", "potion");
        } else {
            data.put("item", "other");
        }
        ExampleModClient.LOGGER.info(data.toString());
        send(data.toString());
        return "done";
    }

    public String sendTest(MinecraftClient client) {
        JSONObject playerData = new JSONObject();
        playerData.put("time", System.currentTimeMillis() - startTime);
        playerData.put("health", Math.round(client.player.getHealth()));
        playerData.put("hunger", client.player.getHungerManager().getFoodLevel());
        playerData.put("pitch", client.player.getPitch());
        playerData.put("yaw", client.player.getYaw());
        playerData.put("light", client.world.getLightLevel(new BlockPos((int) client.player.getX(), (int) client.player.getY(), (int) client.player.getZ())));

        ItemStack held = client.player.getHandItems().iterator().next();
        String mainhand = held.getItem().toString();
        int count = held.getCount();
        if (mainhand.endsWith("_sword")
                || mainhand.endsWith("_axe") || mainhand.endsWith("_pickaxe")
                || mainhand.endsWith("_shovel") || mainhand.endsWith("_hoe")) {
            count = maxDurability.get(mainhand.substring(0,4)) - held.getDamage();
        }
        playerData.put("heldType", mainhand);
        playerData.put("heldCount", count);
        StatusEffectInstance[] statusArray = (StatusEffectInstance[]) client.player.getStatusEffects().toArray();
        JSONObject[] effects = new JSONObject[client.player.getStatusEffects().size()];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = new JSONObject();
            effects[i].put("name", statusArray[i].getEffectType().getName().getString());
            effects[i].put("level", statusArray[i].getAmplifier());
            effects[i].put("time", statusArray[i].getDuration());
        }
        playerData.put("effects", effects);
        playerData.put("attackCD", client.player.getAttackCooldownProgress(0));

        JSONObject data = new JSONObject();
        data.put("playerData", data);
        return "done";
    }

    private void send(String msg) {
        try {
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, addr, 5001);
            sock.send(packet);
        //     packet = new DatagramPacket(buf, buf.length);
        //     sock.receive(packet);
        //     // response starts with 4-byte message length
        //     byte[] lenBytes = packet.getData();
        //     int len = ((0xFF & lenBytes[0]) << 24)
        //             | ((0xFF & lenBytes[1]) << 16)
        //             | ((0xFF & lenBytes[2]) << 8)
        //             | (0xFF & lenBytes[3]);
        //     ExampleModClient.LOGGER.info("send: response length=" + len);
        //     sock.receive(packet);
        //     received = new String(packet.getData(), 0, len);
        // } catch(PortUnreachableException pe) {
        //     ExampleModClient.LOGGER.info("Unreachable port!");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        sock.close();
    }

}
