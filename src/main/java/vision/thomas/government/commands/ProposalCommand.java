package vision.thomas.government.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vision.thomas.government.Announcement;
import vision.thomas.government.Government;
import vision.thomas.government.Proposal;
import vision.thomas.government.VoteManager;
import vision.thomas.government.commands.helpers.SubCommand;

public class ProposalCommand extends SubCommand {

    private Government plugin;

    private VoteManager voteManager;

    private Announcement announcement;

    public ProposalCommand(Government plugin) {

        super(plugin, plugin.getName().toLowerCase(), "proposal", "[create <proposal type> <reason>| cancel]", "Allows players to create proposals to be voted on.");

        this.plugin = plugin;
        voteManager = new VoteManager(plugin);
        announcement = new Announcement(plugin);
    }

    public boolean execute(CommandSender sender, String[] args) {

        if (args.length > 0 && sender instanceof Player) {

            Player proposer = (Player) sender;

            if (args[0].equalsIgnoreCase("create")) {

                if (!voteManager.voteInProgress) {
                    if (args.length > 2) {

                        voteManager.voteInProgress = true;
                        Proposal proposal = new Proposal(plugin, args[1], proposer);
                        voteManager.setCurrentProposal(proposal);
                        announcement.announceProposal(proposer, proposal, args[2]);
                    }
                    else {
                        proposer.sendMessage(plugin.prefix + "Please specify what you are proposing and why.");
                    }
                }
                else {
                    proposer.sendMessage(plugin.prefix + "Please wait until the current vote is complete before proposing a new one.");
                }
            }
            else if (args[0].equalsIgnoreCase("cancel")) {

                if (voteManager.voteInProgress) {

                    if (voteManager.getCurrentProposal().proposer.getName().equalsIgnoreCase(proposer.getName()) || proposer.isOp()) {

                        voteManager.voteInProgress = false;
                        if (args.length > 1) {

                            String reason = "";
                            for (int i = 0; i < args.length; i++) {
                                if (i != 0) {
                                    reason += args[i] + " ";
                                }
                            }
                            reason = reason.substring(0, reason.length() - 1);

                            announcement.announceProposalCancellation(proposer, voteManager.getCurrentProposal(), reason);
                        }
                        else {
                            announcement.announceProposalCancellation(proposer, voteManager.getCurrentProposal());
                        }
                        voteManager.getCurrentProposal().cancelProposal(proposer);
                    }
                    else {
                        proposer.sendMessage(plugin.prefix + "Only " + ChatColor.AQUA + "" + voteManager.getCurrentProposal().proposer.getName() + plugin.defaultColor + " can cancel this proposal.");
                    }
                }
                else {
                    proposer.sendMessage(plugin.prefix + "There is currently no active proposal to cancel");
                }
            }
            else {
                sender.sendMessage(plugin.prefix + "Incorrect arguments.");
                sender.sendMessage(this.getUsage());
            }
        }
        else {
            sender.sendMessage(plugin.prefix + "Incorrect arguments.");
            sender.sendMessage(this.getUsage());
        }
        return true;
    }

}
