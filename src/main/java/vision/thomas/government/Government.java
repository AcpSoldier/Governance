package vision.thomas.government;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import vision.thomas.government.commands.ConfigCommand;
import vision.thomas.government.commands.ProposalCommand;
import vision.thomas.government.commands.VoteCommand;
import vision.thomas.government.commands.helpers.CommandExec;

public final class Government extends JavaPlugin {

    public String prefix = ChatColor.GOLD + "" + ChatColor.BOLD + "Government: " + ChatColor.YELLOW;

    public ChatColor defaultColor = ChatColor.YELLOW;

    public Config config;

    @Override
    public void onEnable() {

        config = new Config(this);
        config.setup();

        CommandExec cmdEx = new CommandExec(this);
        cmdEx.register(new ConfigCommand(this));
        cmdEx.register(new VoteCommand(this));
        cmdEx.register(new ProposalCommand(this));
    }

    @Override
    public void onDisable() {
        // Shutdown logic
    }

}