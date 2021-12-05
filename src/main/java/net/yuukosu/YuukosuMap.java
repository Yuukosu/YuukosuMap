package net.yuukosu;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_13_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class YuukosuMap extends JavaPlugin {

    public static Plugin plugin;
    public static final HashMap<String, Location> locations = new HashMap<>();
    public static final Set<PlayerManager> playerManager = new HashSet<>();
    public static final ConfigManager locationsData = new ConfigManager(new File("plugins/YuukosuMap/Locations.yml"));

    @Override
    public void onEnable() {
        YuukosuMap.plugin = this;

        this.loadLocations();

        CraftServer server = (CraftServer) Bukkit.getServer();
        server.getCommandMap().register("YuukosuMap", new YMapCommand());

        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    private void loadLocations() {
        if (YuukosuMap.locationsData.getFileConfiguration().contains("LOCATIONS")) {
            for (String key : YuukosuMap.locationsData.getFileConfiguration().getConfigurationSection("LOCATIONS").getKeys(false)) {
                Location location = new Location(
                        Bukkit.getWorld(YuukosuMap.locationsData.getFileConfiguration().getString("LOCATIONS." + key + ".WORLD")),
                        YuukosuMap.locationsData.getFileConfiguration().getDouble("LOCATIONS." + key + ".X"),
                        YuukosuMap.locationsData.getFileConfiguration().getDouble("LOCATIONS." + key + ".Y"),
                        YuukosuMap.locationsData.getFileConfiguration().getDouble("LOCATIONS." + key + ".Z")
                );

                YuukosuMap.locations.put(key, location);
            }
        }

        Bukkit.getLogger().info("Loaded " + YuukosuMap.locations.size() + " locations!");
    }

    public static void saveLocations() {
        FileConfiguration config = YuukosuMap.locationsData.getFileConfiguration();

        config.set("LOCATIONS", null);

        for (Map.Entry<String, Location> entry : YuukosuMap.locations.entrySet()) {
            config.set("LOCATIONS." + entry.getKey() + ".WORLD", entry.getValue().getWorld().getName());
            config.set("LOCATIONS." + entry.getKey() + ".X", entry.getValue().getX());
            config.set("LOCATIONS." + entry.getKey() + ".Y", entry.getValue().getY());
            config.set("LOCATIONS." + entry.getKey() + ".Z", entry.getValue().getZ());
        }

        YuukosuMap.locationsData.save();
    }

    public static boolean addLocation(String name, Location location) {
        if (!YuukosuMap.locations.containsKey(name)) {
            YuukosuMap.locations.put(name, location);
            return true;
        }

        return false;
    }

    public static boolean deleteLocation(String name) {
        if (YuukosuMap.locations.containsKey(name)) {
            YuukosuMap.locations.remove(name);
            return true;
        }

        return false;
    }

    public static HashMap<String, Location> getLocations() {
        return YuukosuMap.locations;
    }

    public static PlayerManager getPlayerManager(Player player) {
        for (PlayerManager playerManager : YuukosuMap.playerManager) {
            if (playerManager.getPlayer().equals(player)) {
                return playerManager;
            }
        }

        return null;
    }

    public static Plugin getPlugin() {
        return YuukosuMap.plugin;
    }
}
