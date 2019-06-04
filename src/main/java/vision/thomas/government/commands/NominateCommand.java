package vision.thomas.government.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vision.thomas.government.*;
import vision.thomas.government.commands.helpers.SubCommand;

public class NominateCommand extends SubCommand {

    private Government plugin;

    private GovernmentManager govManager;

    private VoteManager voteManager;

    private Config config;

    private Announcements announcements;

    public NominateCommand(Government plugin) {

        super(plugin, plugin.getName().toLowerCase(), "nominate", "<player name>", "Allows players nominate leaders.");
        this.plugin = plugin;
        govManager = plugin.getGovManager();
        voteManager = plugin.getVoteManager();
        config = plugin.getConf();
        announcements = plugin.getAnnouncement();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (sender instanceof Player) {

            if (args.length > 0) {

                Player proposer = (Player) sender;
                String newLeader = args[0];

                if (Bukkit.getOfflinePlayer(newLeader).isOnline()) {

                    Player nominated = (Player) Bukkit.getOfflinePlayer(newLeader);

                    if (config.getGovType() != 0) {
                        if (!govManager.govLeadersContains(newLeader)) {
                            if (!voteManager.isVoteInProgress()) {
                                if (args.length == 1) {

                                    if (config.getGovLeaders().size() <= config.getMaxLeaders()) {
                                        if (config.getGovLeaders().size() < config.getMaxLeaders()) {

                                            createElection(proposer, nominated);
                                        }
                                    }
                                    else {
                                        proposer.sendMessage(plugin.prefix + "The maximum amount of leaders has been met (" + config.getMaxLeaders() + "). In order to elect a new player to office, please specify which current leader you think they should replace.");
                                    }

                                }
                                else if (args.length == 2) {

                                    String competeLeader = args[1];
                                    Player competitor = (Player) Bukkit.getOfflinePlayer(competeLeader);

                                    if (govManager.govLeadersContains(competeLeader)) {
                                        createElection(proposer, nominated, competitor);
                                    }
                                    else {
                                        proposer.sendMessage(plugin.prefix + "'" + competeLeader + "' is not currently a leader and cannot be replaced. Use /gov leaders for a list of current leaders.");
                                    }
                                }
                                else {
                                    proposer.sendMessage(plugin.prefix + "Incorrect arguments.");
                                    proposer.sendMessage(this.getUsage());
                                }
                            }
                            else {
                                proposer.sendMessage(plugin.prefix + "Please wait until the current vote has ended before proposing a new one.");
                            }
                        }
                        else {
                            proposer.sendMessage(plugin.prefix + newLeader + " is already a " + govManager.getTypeOfGovLeader() + ".");
                        }

                    }
                    else {
                        proposer.sendMessage(plugin.prefix + "You can't nominate leaders under a " + govManager.getGovName() + ".");
                    }
                }
                else {
                    proposer.sendMessage(plugin.prefix + "Player '" + newLeader + "' is not online and can't be nominated.");
                }
            }
            else {
                sender.sendMessage(plugin.prefix + "Incorrect arguments.");
                sender.sendMessage(this.getUsage());
            }
        }
        else {
            sender.sendMessage(plugin.prefix + "This command cannot be run by the console.");
        }

        return true;
    }

    private void createElection(Player proposer, Player nominated) {

        for (Player player : voteManager.creatingProposal.keySet()) {
            player.sendMessage(plugin.prefix + ChatColor.BOLD + "Proposal creation canceled; " + proposer.getDisplayName() + " created one before you.");
        }
        voteManager.creatingProposal.clear();

        voteManager.setVoteInProgress(true);
        Proposal proposal = new Proposal(plugin);
        proposal.setProposer(proposer);
        proposal.setNominated(nominated);
        voteManager.setCurrentProposal(proposal);

        announcements.announceElection(proposer, nominated);
        voteManager.startTimer(true);
    }

    private void createElection(Player proposer, Player nominated, Player competitor) {

        for (Player player : voteManager.creatingProposal.keySet()) {
            player.sendMessage(plugin.prefix + ChatColor.BOLD + "Proposal creation canceled; " + proposer.getDisplayName() + " created one before you.");
        }
        voteManager.creatingProposal.clear();

        voteManager.setVoteInProgress(true);
        Proposal proposal = new Proposal(plugin);
        proposal.setProposer(proposer);
        proposal.setNominated(nominated);
        proposal.setCompetitor(competitor);
        voteManager.setCurrentProposal(proposal);

        announcements.announceElectionCompetition(proposer, nominated, competitor);
        voteManager.startTimer(true);
    }

}
