package vision.thomas.government;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import vision.thomas.government.commands.ConfigCommand;
import vision.thomas.government.commands.helpers.CommandExec;

public final class Government extends JavaPlugin {

    public String prefix = ChatColor.GOLD + "" + ChatColor.BOLD + "Government: " + ChatColor.WHITE;

    @Override
    public void onEnable() {

        CommandExec cmdEx = new CommandExec(this);
        cmdEx.register(new ConfigCommand(this));
    }

    @Override
    public void onDisable() {
        // Shutdown logic
    }
}
