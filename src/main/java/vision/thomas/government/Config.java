package vision.thomas.government;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public FileConfiguration config;

    private Government plugin;

    public boolean isPluginEnabled;

    public Config(Government passedPlugin) {

        this.plugin = passedPlugin;
    }

    public void reloadConfig(Government plugin) {

        isPluginEnabled = config.getBoolean("Settings.Enabled");
        plugin.saveConfig();
    }

    public void setPluginEnabled(boolean pluginEnabled) {

        isPluginEnabled = pluginEnabled;
        config.set("Settings.Enabled", isPluginEnabled);
        plugin.saveConfig();
    }

}