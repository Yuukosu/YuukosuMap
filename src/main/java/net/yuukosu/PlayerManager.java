package net.yuukosu;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerManager {

    private final Player player;
    private String trackingLocationName;

    public PlayerManager(Player player) {
        this.player = player;
    }

    public final void tracking(String locationName) {
        this.trackingLocationName = locationName;

        new BukkitRunnable() {
            final String currentTracking = PlayerManager.this.trackingLocationName;

            @Override
            public void run() {
                if (this.currentTracking.equalsIgnoreCase(PlayerManager.this.trackingLocationName) && PlayerManager.this.player != null) {
                    if (YuukosuMap.getLocations().containsKey(this.currentTracking)) {
                        if (PlayerManager.this.player.getWorld() == YuukosuMap.getLocations().get(this.currentTracking).getWorld()) {
                            Location location = YuukosuMap.getLocations().get(this.currentTracking);
                            PlayerManager.this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + this.currentTracking + ChatColor.GREEN + " までの距離 " + ChatColor.YELLOW + String.format("%,d", Math.round(location.distance(PlayerManager.this.player.getLocation()))) + "m"));
                        } else {
                            this.cancel();
                            PlayerManager.this.untracking();
                            PlayerManager.this.player.sendMessage(ChatColor.RED + "トラッキングしていたワールドから移動したため、トラッキングが解除されました。");
                        }
                    } else {
                        this.cancel();
                        PlayerManager.this.untracking();
                        PlayerManager.this.player.sendMessage(ChatColor.RED + "トラッキングしていた場所が地図から消去されているため、トラッキングが解除されました。");
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(YuukosuMap.getPlugin(), 0L, 1L);
    }

    public final void untracking() {
        this.trackingLocationName = null;
        this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final String getTrackingLocationName() {
        return this.trackingLocationName;
    }
}
