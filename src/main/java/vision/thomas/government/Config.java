package vision.thomas.government;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public FileConfiguration cfg;

    private Government plugin;

    public boolean isPluginEnabled;

    public static boolean isVoteSoundsForAll;

    public static boolean isVoteSoundsForSelf;

    public int govType;

    public List<String> govLeaders = new ArrayList<String>();

    public List<String> allowedCommands = new ArrayList<String>();

    public Config(Government plugin) {

        this.plugin = plugin;
    }

    public void reloadConfig() {

        plugin.reloadConfig();
        cfg = plugin.getConfig(); // 100% needed

        isPluginEnabled = cfg.getBoolean("Settings.Enabled");
        isVoteSoundsForAll = cfg.getBoolean("Settings.VoteSoundsForAll");
        isVoteSoundsForSelf = cfg.getBoolean("Settings.VoteSoundsForSelf");
        govType = cfg.getInt("Government.Type");
        govLeaders = cfg.getStringList("Government.Leaders");
        allowedCommands = cfg.getStringList("Commands.Allowed");

        plugin.saveConfig();
    }

    public void setPluginEnabled(boolean pluginEnabled) {

        isPluginEnabled = pluginEnabled;
        cfg.set("Settings.Enabled", isPluginEnabled);
        plugin.saveConfig();
    }

    public void setup() {

        plugin.saveDefaultConfig();
        cfg = plugin.getConfig();
        reloadConfig();
    }

}