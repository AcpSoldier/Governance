package vision.thomas.government.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vision.thomas.government.Announcement;
import vision.thomas.government.Government;
import vision.thomas.government.GovernmentManager;
import vision.thomas.government.VoteManager;
import vision.thomas.government.commands.helpers.SubCommand;

public class VoteCommand extends SubCommand {

    private Government plugin;

    private VoteManager voteManager;

    private Announcement announcement;

    private GovernmentManager governmentManager;

    public VoteCommand(Government plugin) {

        super(plugin, plugin.getName().toLowerCase(), "vote", "[yes | no]", "Allows players to vote on an active proposal or election.");
        this.plugin = plugin;
        voteManager = new VoteManager(plugin);
        governmentManager = new GovernmentManager(plugin);
        announcement = new Announcement(plugin);
    }

    public boolean execute(CommandSender sender, String[] args) {

        if (sender instanceof Player) {
            if (args.length > 0) {

                Player voter = (Player) sender;

                if (voteManager.isVoteInProgress()) {

                    if (!voteManager.getCurrentProposal().getVotedNo().contains(voter) && !voteManager.getCurrentProposal().getVotedYes().contains(voter)) {
                        if (args[0].equalsIgnoreCase("yes")) {

                            switch (governmentManager.getGovType()) {

                                default: // Direct Democracy
                                    castVote(voter, args);
                                    break;

                                case 1: // Republic
                                    if (governmentManager.govLeadersContains(voter.getDisplayName())) {
                                        castVote(voter, args);
                                    }
                                    else {
                                        voter.sendMessage(plugin.prefix + "In a " + governmentManager.getGovName() + ", you must be a " + governmentManager.getTypeOfGovLeader() + " to vote.");
                                    }
                                    break;
                            }
                        }
                        else if (args[0].equalsIgnoreCase("no")) {

                            voteManager.castVote(voter, voteManager.getCurrentProposal(), args[0]);

                            if (args.length > 1) {

                                String reason = "";
                                for (int i = 0; i < args.length; i++) {
                                    if (i != 0) {
                                        reason += args[i] + " ";
                                    }
                                }
                                reason = reason.substring(0, reason.length() - 1);

                                announcement.announceVote(voter, voteManager.getCurrentProposal(), args[0], reason);
                            }
                            else {
                                announcement.announceVote(voter, voteManager.getCurrentProposal(), args[0]);
                            }
                        }
                        else {
                            sender.sendMessage(plugin.prefix + "Please cast your vote as 'yes' or 'no'.");
                            sender.sendMessage(this.getUsage());
                        }
                    }
                    else {
                        voter.sendMessage(plugin.prefix + "You have already voted on this proposal.");
                    }
                }
                else {
                    voter.sendMessage(plugin.prefix + "There is currently no active proposal to vote on.");
                }
            }
            else {
                sender.sendMessage(plugin.prefix + "Please cast your vote as 'yes' or 'no'.");
                sender.sendMessage(this.getUsage());
            }
        }
        else {
            sender.sendMessage(plugin.prefix + "Only players can vote on proposals, sorry console : ( ");
        }
        return true;
    }

    private void castVote(Player voter, String[] args) {

        voteManager.castVote(voter, voteManager.getCurrentProposal(), args[0]);

        if (args.length > 1) {

            String reason = "";
            for (int i = 0; i < args.length; i++) {
                if (i != 0) {
                    reason += args[i] + " ";
                }
            }
            reason = reason.substring(0, reason.length() - 1);

            announcement.announceVote(voter, voteManager.getCurrentProposal(), args[0], reason);
        }
        else {
            announcement.announceVote(voter, voteManager.getCurrentProposal(), args[0]);
        }
    }

}
