// จุด เรียกใช้ livraries เช่น spigot และ paper ใน code จะเป็นของ spigot
package io.papermc.testplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// ประกาสเป็น publice class ของไฟล์ plugin
public class ExamplePlugin extends JavaPlugin implements Listener {

    private final Map<UUID, Long> lastMovement = new HashMap<>();
    private static final long AFK_THRESHOLD = 10000; // 10 วินาที

    // ส่วนที่ประกาสใน console ว่า plugin เปิดใช้งาน เช่น public void onEnable คือเมื่อ plugin ทำงาน
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        new BukkitRunnable() {
            @Override
            public void run() {
                checkAFK();
            }
        }.runTaskTimer(this, 0L, 100L); // ทำงานทุก 5 วินาที (100 ticks)
    }
    // กับหนด Event ของเกมว่าให้เช็นอะไร ในที่นี้ให้เช็คจากตัว player ว่าขยับไหม onPlayerMove
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        lastMovement.put(player.getUniqueId(), System.currentTimeMillis());

        // ให้แสดงข้อความเมื่อผู้เล่นเคลื่อนไหว You are now active
        player.sendMessage("You are now active.");
    }
    // แล้วก็เช็คค่า Event ว่าผู้เล่นหยุดขยับไหมถ้าหยุดให้แสดงข้อความ You are AFK!
    private void checkAFK() {
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<UUID, Long> entry : lastMovement.entrySet()) {
            long lastMoveTime = entry.getValue();
            if (currentTime - lastMoveTime > AFK_THRESHOLD) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player != null && player.isOnline()) {
                    player.sendMessage("You are AFK!");
                }
            }
        }
    }
}
