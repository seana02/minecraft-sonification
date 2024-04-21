file://<WORKSPACE>/src/client/java/com/example/CommThread.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.1
Classpath:
<HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.1/scala3-library_3-3.3.1.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.10/scala-library-2.13.10.jar [exists ]
Options:



action parameters:
uri: file://<WORKSPACE>/src/client/java/com/example/CommThread.java
text:
```scala
package com.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

    private long startTime;

    private HashMap<String, Integer> maxDurability;

    public CommThread() {
        buf = new byte[4096];
        startTime = System.currentTimeMillis();
        try {
            sock = new DatagramSocket();
            addr = InetAddress.getByName("localhost");
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

    public String send(MinecraftClient client) {
        JSONObject playerData = new JSONObject();
        playerData.put("time", System.currentTimeMillis() - startTime);
        playerData.put("health", client.player.getHealth());
        playerData.put("hunger", client.player.getHungerManager().getFoodLevel());
        playerData.put("pitch", client.player.getPitch());
        playerData.put("yaw", client.player.getYaw());
        playerData.put("light", client.world.getLightLevel(new BlockPos((int) client.player.getX(), (int) client.player.getY(), (int) client.player.getZ())));

        ItemStack held = client.player.getHandItems().iterator().next();
        String mainhand = held.getItem().toString();
        int count = held.getCount();
        if (mainhand.endsWith("_sword")
 mainhand.endsWith("_axe") || mainhand.endsWith("_pickaxe")
 mainhand.endsWith("_shovel") || mainhand.endsWith("_hoe")) {
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
        return "hi";
    }

    private String sendTest(String msg) {
        String received = "DatagramPacketError";
        try {
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, addr, 5001);
            sock.send(packet);
            packet = new DatagramPacket(buf, buf.length);
            sock.receive(packet);
            // response starts with 4-byte message length
            byte[] lenBytes = packet.getData();
            int len = ((0xFF & lenBytes[0]) << 24)
 ((0xFF & lenBytes[1]) << 16)
 ((0xFF & lenBytes[2]) << 8)
 (0xFF & lenBytes[3]);
            ExampleModClient.LOGGER.info("send: response length=" + len);
            sock.receive(packet);
            received = new String(packet.getData(), 0, len);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return received;
    }

    public void close() {
        sock.close();
    }

}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:933)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:168)
	scala.meta.internal.pc.MetalsDriver.run(MetalsDriver.scala:45)
	scala.meta.internal.pc.PcCollector.<init>(PcCollector.scala:44)
	scala.meta.internal.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:90)
	scala.meta.internal.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:109)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator