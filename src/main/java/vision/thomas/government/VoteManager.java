package vision.thomas.government;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import vision.thomas.government.accounts.Account;

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
                                    distributeRespect(true);
                                    cancelProposal();

                                }
                                else {
                                    executeProposalCommand(20);
                                }
                                cancel();
                            }
                        }.runTaskLater(plugin, 40);

                    }
                    else {

                        if (election) {
                            plugin.announcement.announceElectionResults(false, percentYes, percentNo, votesYes, votesNo, "Not enough votes in the positive.");
                        }
                        else {
                            plugin.announcement.announceProposalResults(false, percentYes, percentNo, votesYes, votesNo, "Not enough votes in the positive.");
                        }
                        distributeRespect(false);
                        cancelProposal();
                    }
                }
                else {
                    if (election) {
                        plugin.announcement.announceElectionResults(false, percentYes, percentNo, votesYes, votesNo, ("Not enough people voted. Minimum required votes is set to " + config.getMinVotesRequired()));
                    }
                    else {
                        plugin.announcement.announceProposalResults(false, percentYes, percentNo, votesYes, votesNo, ("Not enough people voted. Minimum required votes is set to " + config.getMinVotesRequired()));
                    }
                    distributeRespect(false);
                    cancelProposal();
                }
            }
        }.runTaskLater(plugin, 40);

    }

    private void cancelProposal() {

        new BukkitRunnable() {

            public void run() {

                setVoteInProgress(false);
                currentProposal.cancelProposal(currentProposal.getProposer());
            }
        }.runTaskLater(plugin, 40);

    }

    private void distributeRespect(boolean passed) {

        int votesYes = getCurrentProposal().getVotedYes().size();
        int votesNo = getCurrentProposal().getVotedNo().size();
        int totalVotes = votesYes + votesNo;
        float percentYes = (int) Math.round(100.0 / (totalVotes) * votesYes);
        float percentNo = (int) Math.round(100.0 / (totalVotes) * votesNo);

        if (passed) {
            for (Player votedYesPlayer : currentProposal.getVotedYes()) {
                if (votedYesPlayer != currentProposal.getProposer() && votedYesPlayer.isOnline()) {

                    votedYesPlayer.playSound(votedYesPlayer.getLocation(), Sound.BLOCK_ANVIL_USE, 2.0F, 2.0F);

                    Account votedYesAccount = plugin.getAccountManager().getAccount(votedYesPlayer.getUniqueId());
                    plugin.getAccountManager().incrementRespect(votedYesAccount, 1);
                    votedYesPlayer.sendMessage(plugin.prefix + ChatColor.GREEN + "" + ChatColor.BOLD + "+1 Respect Point" + plugin.defaultColor + " for voting in the interest of the majority.");
                }
            }

            if (currentProposal.getProposer().isOnline()) {

                currentProposal.getProposer().playSound(currentProposal.getProposer().getLocation(), Sound.BLOCK_ANVIL_USE, 2.0F, 2.0F);

                Account proposerAccount = plugin.getAccountManager().getAccount(currentProposal.getProposer().getUniqueId());
                plugin.getAccountManager().incrementRespect(proposerAccount, 3);
                currentProposal.getProposer().sendMessage(plugin.prefix + ChatColor.GREEN + "" + ChatColor.BOLD + "+3 Respect Points" + plugin.defaultColor + " for passing a proposal.");
            }

            if (currentProposal.getNominated().isOnline()) {

                currentProposal.getNominated().playSound(currentProposal.getProposer().getLocation(), Sound.BLOCK_ANVIL_USE, 2.0F, 2.0F);

                Account proposerAccount = plugin.getAccountManager().getAccount(currentProposal.getProposer().getUniqueId());
                plugin.getAccountManager().incrementRespect(proposerAccount, 3);
                currentProposal.getNominated().sendMessage(plugin.prefix + ChatColor.GREEN + "" + ChatColor.BOLD + "+5 Respect Points" + plugin.defaultColor + " for being elected by the majority.");
            }

        }
        else {
            if (percentNo > 50) {
                if (currentProposal.getProposer().isOnline()) {
                    currentProposal.getProposer().playSound(currentProposal.getProposer().getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.0F, 2.0F);

                    Account proposerAccount = plugin.getAccountManager().getAccount(currentProposal.getProposer().getUniqueId());
                    plugin.getAccountManager().incrementRespect(proposerAccount, 3);
                    currentProposal.getProposer().sendMessage(plugin.prefix + ChatColor.RED + "" + ChatColor.BOLD + "-3 Respect Points" + plugin.defaultColor + " for proposing an unpopular vote.");
                }

            }
        }
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
                    distributeRespect(true);
                    cancelProposal();
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
