package vision.thomas.government;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteManager implements Listener {

    private Government plugin;

    private Config config;

    private GovernmentManager govManager;

    private static Proposal currentProposal;

    private static boolean isVoteInProgress = false;

    private static boolean voteCountInProgress = false;

    public static Map<Player, String> creatingProposal = new HashMap<>();

    public VoteManager(Government plugin) {

        this.plugin = plugin;
        config = new Config(plugin);
        govManager = new GovernmentManager(plugin);
    }

    public void castVote(Player voter, Proposal proposal, String vote) {

        if (vote.equalsIgnoreCase("yes")) {

            proposal.getVotedYes().add(voter);
        }
        else {
            proposal.getVotedNo().add(voter);
        }
    }

    public void startTimer(boolean isElection) {

        new BukkitRunnable() {

            boolean firstRun = true;

            int time;

            List<Integer> announceVoteAt = new ArrayList<>();

            public void run() {

                if (firstRun) {
                    time = config.getVoteTimeInSeconds();
                    announceVoteAt = config.getAnnounceVoteAt();
                    firstRun = false;
                }
                if (isVoteInProgress()) {
                    if (time <= 0) {
                        setVoteCountInProgress(true);
                        if (isElection) {
                            plugin.announcement.announceElectionVoteCount();
                        }
                        else {
                            plugin.announcement.announceVoteCount();
                        }

                        cancel();
                    }
                    else if (announceVoteAt.contains(time)) {
                        if (isElection) {
                            plugin.announcement.announceElectionVoteTime(time);
                        }
                        else {
                            plugin.announcement.announceVoteTime(time);
                        }
                    }
                    time--;
                }
                else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void countVotes(boolean election) {

        new BukkitRunnable() {

            public void run() {

                int votesYes = getCurrentProposal().getVotedYes().size();
                int votesNo = getCurrentProposal().getVotedNo().size();
                int totalVotes = votesYes + votesNo;
                float percentYes = (int) Math.round(100.0 / (totalVotes) * votesYes);
                float percentNo = (int) Math.round(100.0 / (totalVotes) * votesNo);

                if (totalVotes >= config.getMinVotesRequired()) {
                    if (percentYes >= config.getPercentNeededToPass()) {

                        if (election) {
                            plugin.announcement.announceElectionResults(true, percentYes, percentNo, votesYes, votesNo, "");
                        }
                        else {
                            plugin.announcement.announceProposalResults(true, percentYes, percentNo, votesYes, votesNo, "");
                        }

                        new BukkitRunnable() {

                            public void run() {

                                if (election) {
                                    if (currentProposal.getCompetitor() != null) {
                                        if (currentProposal.getNominated().isOnline()) {
                                            govManager.addGovLeader(currentProposal.getNominated());
                                        }
                                        else {
                                            govManager.addGovLeader(currentProposal.getNominated().getUniqueId().toString());
                                        }
                                        if (currentProposal.getCompetitor().isOnline()) {
                                            govManager.removeGovLeader(currentProposal.getCompetitor());
                                        }
                                        else {
                                            govManager.removeGovLeader(currentProposal.getCompetitor().getUniqueId().toString());
                                        }
                                    }
                                    else {
                                        if (currentProposal.getNominated().isOnline()) {
                                            govManager.addGovLeader(currentProposal.getNominated());
                                        }
                                        else {
                                            govManager.addGovLeader(currentProposal.getNominated().getUniqueId().toString());
                                        }
                                    }
                                    finishProposal();
                                }
                                else {
                                    executeProposalCommand(20);
                                }
                                cancel();
                            }
                        }.runTaskLater(plugin, 40);

                    }
                    else {
                        finishProposal();
                        if (election) {
                            plugin.announcement.announceElectionResults(false, percentYes, percentNo, votesYes, votesNo, "Not enough votes in the positive.");
                        }
                        else {
                            plugin.announcement.announceProposalResults(false, percentYes, percentNo, votesYes, votesNo, "Not enough votes in the positive.");
                        }
                    }
                }
                else {
                    finishProposal();
                    if (election) {
                        plugin.announcement.announceElectionResults(false, percentYes, percentNo, votesYes, votesNo, ("Not enough people voted. Minimum required votes is set to " + config.getMinVotesRequired()));
                    }
                    else {
                        plugin.announcement.announceProposalResults(false, percentYes, percentNo, votesYes, votesNo, ("Not enough people voted. Minimum required votes is set to " + config.getMinVotesRequired()));
                    }
                }
            }
        }.runTaskLater(plugin, 40);

    }

    private void finishProposal() {

        new BukkitRunnable() {

            public void run() {

                setVoteInProgress(false);
                currentProposal.cancelProposal(currentProposal.getProposer());
            }
        }.runTaskLater(plugin, 40);

    }

    public void executeProposalCommand(int delay) {

        new BukkitRunnable() {

            public void run() {

                Player proposer = currentProposal.getProposer();
                boolean wasAlreadyAnOp = false;

                try {
                    if (!proposer.isOp()) {
                        proposer.setOp(true);
                    }
                    else {
                        wasAlreadyAnOp = true;
                    }
                    plugin.getServer().dispatchCommand(proposer, currentProposal.getCommand().substring(1));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!wasAlreadyAnOp) {
                        proposer.setOp(false);
                    }
                    finishProposal();
                }
            }
        }.runTaskLater(plugin, delay);
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {

        if (isVoteCountInProgress()) {
            event.setCancelled(true);
        }
    }

    public Proposal getCurrentProposal() {

        return currentProposal;
    }

    public void setCurrentProposal(Proposal proposal) {

        currentProposal = proposal;
    }

    public static boolean isVoteInProgress() {

        return isVoteInProgress;
    }

    public static void setVoteInProgress(boolean setVoteInProgress) {

        isVoteInProgress = setVoteInProgress;
    }

    public static boolean isVoteCountInProgress() {

        return voteCountInProgress;
    }

    public static void setVoteCountInProgress(boolean setVoteCountInProgress) {

        voteCountInProgress = setVoteCountInProgress;
    }

}
