package vision.thomas.government;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class VoteManager {

    private Government plugin;

    private Config config;

    private static Proposal currentProposal;

    private static boolean voteInProgress = false;

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

        return voteInProgress;
    }

    public static void setVoteInProgress(boolean voteInProgress) {

        VoteManager.voteInProgress = voteInProgress;
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
                if (voteInProgress) {
                    if (time <= 0) {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            player.sendMessage("Timer has ended.");
                            countVotes();
                        }
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

    private void countVotes() {

        if (currentProposal.getVotedYes().size() > currentProposal.getVotedNo().size()) {
            plugin.announcement.announceProposalPassed();

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
            }
        }
        else {
            plugin.announcement.announceProposalFail();
        }

        voteInProgress = false;
        currentProposal.cancelProposal(currentProposal.getProposer());
    }

}
