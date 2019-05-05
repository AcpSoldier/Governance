package vision.thomas.government;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import vision.thomas.government.commands.ConfigCommand;
import vision.thomas.government.commands.helpers.CommandExec;

public final class Government extends JavaPlugin {

    public String prefix = ChatColor.GOLD + "" + ChatColor.BOLD + "Government: " + ChatColor.WHITE;

    public Config conf;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        conf = new Config(this);
        conf.config = this.getConfig();
        conf.reloadConfig(this);

        CommandExec cmdEx = new CommandExec(this);
        cmdEx.register(new ConfigCommand(this));
    }

    @Override
    public void onDisable() {
        // Shutdown logic
    }

}