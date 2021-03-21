package siongsng.rpmtwupdatemod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DownloadInfoHelper {
    public static Queue<String> info = new ConcurrentLinkedQueue<>();

    public static void init() {
        new Thread(() -> {
            while (true) {
                if (Minecraft.getInstance().player != null) {
                    while (!info.isEmpty()) {
                        String theInfo = info.remove();
                        Minecraft.getInstance().deferTask(() -> Minecraft.getInstance().player.sendMessage(new StringTextComponent("[RPMTW] " + theInfo), Minecraft.getInstance().player.getUniqueID()));
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "rpmtw-download-info").start();
    }
}
