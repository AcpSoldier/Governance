package vision.thomas.government.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import vision.thomas.government.Config;
import vision.thomas.government.Government;
import vision.thomas.government.GovernmentManager;
import vision.thomas.government.commands.helpers.SubCommand;

import java.util.UUID;

public class LeadersCommand extends SubCommand {

    private Government plugin;

    private GovernmentManager govManager;

    private Config config;

    public LeadersCommand(Government plugin) {

        super(plugin, plugin.getName().toLowerCase(), "leaders", "", "Shows a list of the current government's leaders.");

        this.plugin = plugin;
        config = new Config(plugin);
        this.govManager = new GovernmentManager(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        String leaders = "";

        for (String leaderId : config.getGovLeaders()) {
            String leaderUsername = Bukkit.getOfflinePlayer(UUID.fromString(leaderId)).getName();
            leaders = leaders + ", " + ChatColor.BOLD + leaderUsername;
        }

        sender.sendMessage(plugin.prefix + "Here is a list of the current " + govManager.getTypeOfGovLeader() + "s:");
        sender.sendMessage(plugin.mainColor + leaders.substring(1));
        return true;
    }

}
