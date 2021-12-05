package net.yuukosu;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final File file;
    private final FileConfiguration fileConfiguration;

    public ConfigManager(File file) {
        this.file = file;
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
        this.save();
    }

    public void save() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getFileConfiguration() {
        return this.fileConfiguration;
    }
}
