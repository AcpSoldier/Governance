package vision.thomas.government.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vision.thomas.government.Announcements;
import vision.thomas.government.Government;
import vision.thomas.government.GovernmentManager;
import vision.thomas.government.commands.helpers.SubCommand;

public class ResignCommand extends SubCommand {

    private Government plugin;

    private GovernmentManager govManager;

    private Announcements announcements;

    public ResignCommand(Government plugin) {

        super(plugin, plugin.getName().toLowerCase(), "resign", "", "Voluntarily leaves a leader position.");

        this.plugin = plugin;
        govManager = plugin.getGovManager();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (sender instanceof Player) {

            Player resignedPlayer = (Player) sender;

            if (govManager.govLeadersContains(resignedPlayer.getDisplayName())) {
                govManager.removeGovLeader(govManager.getGovLeaderId(resignedPlayer.getDisplayName()));
                announcements.announceResignation(resignedPlayer);
            }
            else {
                resignedPlayer.sendMessage(plugin.prefix + "You can't resign from office unless you are actually IN an office. :-)");
            }
        }
        else {
            sender.sendMessage(plugin.prefix + "Only players can vote on proposals, sorry console : ( ");
        }

        return true;
    }

}