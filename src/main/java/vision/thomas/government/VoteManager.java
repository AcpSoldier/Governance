package vision.thomas.government;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class VoteManager implements Listener {

    private Government plugin;

    private Config config;

    private static Proposal currentProposal;

    private static boolean isVoteInProgress = false;

    private static boolean voteCountInProgress = false;

    public VoteManager(Government plugin) {

        this.plugin = plugin;
        config = new Config(plugin);
    }

    public void castVote(Player voter, Proposal proposal, String vote) {

        if (vote.equalsIgnoreCase("yes")) {

            proposal.getVotedYes().add(voter);
        }
        else {
            proposal.getVotedNo().add(voter);
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

    public static void setVoteInProgress(boolean voteInProgress) {

        VoteManager.isVoteInProgress = voteInProgress;
    }

    public static boolean isVoteCountInProgress() {

        return voteCountInProgress;
    }

    public static void setVoteCountInProgress(boolean voteCountInProgress) {

        VoteManager.voteCountInProgress = voteCountInProgress;
    }

    public void startTimer() {

        new BukkitRunnable() {

            boolean firstRun = true;

            int time;

            List<Integer> announceVoteAt = new ArrayList<>();

            public void run() {

                if (firstRun) {
                    config.reloadConfig();
                    time = config.voteTimeInSeconds;
                    announceVoteAt = config.announceVoteAt;
                    firstRun = false;
                }
                if (isVoteInProgress()) {
                    if (time <= 0) {
                        setVoteCountInProgress(true);
                        plugin.announcement.announceVoteCount();

                        cancel();
                    }
                    else if (announceVoteAt.contains(time)) {
                        plugin.announcement.announceVoteTime(time);
                    }
                    time--;
                }
                else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void countVotes() {

        new BukkitRunnable() {

            public void run() {

                int votesYes = getCurrentProposal().getVotedYes().size();
                int votesNo = getCurrentProposal().getVotedNo().size();
                int totalVotes = votesYes + votesNo;
                float percentYes = (int) Math.round(100.0 / (totalVotes) * votesYes);
                float percentNo = (int) Math.round(100.0 / (totalVotes) * votesNo);

                config.reloadConfig();
                if (totalVotes >= config.minVotesRequired) {
                    if (percentYes >= config.percentNeededToPass) {
                        plugin.announcement.announceProposalResults(true, percentYes, percentNo, votesYes, votesNo, "");

                        new BukkitRunnable() {

                            public void run() {

                                executeProposalCommand();
                                cancel();
                            }
                        }.runTaskLater(plugin, 60);

                    }
                    else {
                        finishProposal();
                        plugin.announcement.announceProposalResults(false, percentYes, percentNo, votesYes, votesNo, "Not enough votes in the positive.");
                    }
                }
                else {
                    finishProposal();
                    plugin.announcement.announceProposalResults(false, percentYes, percentNo, votesYes, votesNo, ("Not enough people voted. Minimum required votes is set to " + config.minVotesRequired));
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

    private void executeProposalCommand() {

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
                    plugin.getServer().dispatchCommand(proposer, currentProposal.getCommand());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!wasAlreadyAnOp) {
                        proposer.setOp(false);
                    }
                    finishProposal();
                }
            }
        }.runTaskLater(plugin, 20);
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {

        if (isVoteCountInProgress()) {
            event.setCancelled(true);
        }
    }

}
