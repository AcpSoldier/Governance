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

    public ChatColor mainColor = ChatColor.GOLD;

    public String highlightColor = ChatColor.AQUA + "" + ChatColor.BOLD;

    public String fullLine = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.STRIKETHROUGH + "---------------------------------------------";

    public Config config;

    public Announcement announcement = new Announcement(this);

    @Override
    public void onEnable() {

        config = new Config(this);
        config.setup();

        ProposalCommand proposalCommand = new ProposalCommand(this);
        VoteManager voteManager = new VoteManager(this);

        CommandExec cmdEx = new CommandExec(this);
        cmdEx.register(new ConfigCommand(this));
        cmdEx.register(new VoteCommand(this));
        cmdEx.register(proposalCommand);

        getServer().getPluginManager().registerEvents(proposalCommand, this);
        getServer().getPluginManager().registerEvents(voteManager, this);

    }

    @Override
    public void onDisable() {
        // Shutdown logic
    }

}