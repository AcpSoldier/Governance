package vision.thomas.government;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import vision.thomas.government.accounts.Account;
import vision.thomas.government.commands.helpers.CenteredMessage;

public class Announcements {

    private Government plugin;

    private Config config;

    private GovernmentManager govManager;

    private VoteManager voteManager;

    private CenteredMessage centeredMessage = new CenteredMessage();

    public Announcements(Government plugin) {

        this.plugin = plugin;
        config = plugin.getConf();
        govManager = plugin.getGovManager();
        voteManager = plugin.getVoteManager();
    }

    public void announceProposal(Player proposer, Proposal proposal) {

        Account account = plugin.getAccountManager().getAccount(proposer.getUniqueId());

        playSoundIfEnabled(proposer, Sound.BLOCK_BEACON_ACTIVATE, 2.0F, 2.0F);

        Bukkit.broadcastMessage(plugin.fullLine);
        Bukkit.broadcastMessage(centeredMessage.get(plugin.mainColor + proposer.getDisplayName() + plugin.highlightColor + "[" + account.getRespect() + "]" + plugin.mainColor + " has proposed a vote!"));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "Run command: " + plugin.highlightColor + proposal.getCommand()));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "Reason: " + plugin.mainColor + "'" + proposal.getReason() + "'"));

        if (config.getGovType() == 1) {
            Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "You must be a " + govManager.getTypeOfGovLeader() + " to vote."));
        }

        TextComponent voteYesMessage = new TextComponent("      CLICK TO VOTE YES");
        voteYesMessage.setColor(ChatColor.GREEN);
        voteYesMessage.setBold(true);
        voteYesMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/gov vote yes [reason]").create()));
        voteYesMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gov vote yes"));

        TextComponent mediator = new TextComponent("   -   ");
        mediator.setColor(ChatColor.WHITE);
        mediator.setBold(true);

        TextComponent voteNoMessage = new TextComponent("CLICK TO VOTE NO");
        voteNoMessage.setColor(ChatColor.RED);
        voteNoMessage.setBold(true);
        voteNoMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/gov vote no [reason]").create()));
        voteNoMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gov vote no"));

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.spigot().sendMessage(voteYesMessage, mediator, voteNoMessage);
        }

        Bukkit.broadcastMessage(plugin.fullLine);

    }

    public void announceElection(Player proposer, Player nominated) {

        Account proposerAccount = plugin.getAccountManager().getAccount(proposer.getUniqueId());
        Account nominatedAccount = plugin.getAccountManager().getAccount(nominated.getUniqueId());

        playSoundIfEnabled(proposer, Sound.BLOCK_BEACON_ACTIVATE, 2.0F, 2.0F);

        Bukkit.broadcastMessage(plugin.fullLine);
        Bukkit.broadcastMessage(centeredMessage.get(plugin.mainColor + proposer.getDisplayName() + plugin.highlightColor + "[" + proposerAccount.getRespect() + "]" + plugin.mainColor + " has nominated " + plugin.highlightColor + nominated.getDisplayName() + plugin.mainColor + " to be a " + govManager.getTypeOfGovLeader() + "!"));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + govManager.getTypeOfGovLeader() + " count is currently " + config.getGovLeaders().size() + "/" + config.getMaxLeaders() + " (" + (config.getMaxLeaders() - config.getGovLeaders().size()) + " slots available)."));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "If elected, " + nominated.getDisplayName() + " will not replace any current " + govManager.getTypeOfGovLeader() + "s."));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + nominated.getDisplayName() + plugin.highlightColor + "'s Respect Level: " + nominatedAccount.getRespect() + "."));

        TextComponent voteYesMessage = new TextComponent("      CLICK TO VOTE YES");
        voteYesMessage.setColor(ChatColor.GREEN);
        voteYesMessage.setBold(true);
        voteYesMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/gov vote yes [reason]").create()));
        voteYesMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gov vote yes"));

        TextComponent mediator = new TextComponent("   -   ");
        mediator.setColor(ChatColor.WHITE);
        mediator.setBold(true);

        TextComponent voteNoMessage = new TextComponent("CLICK TO VOTE NO");
        voteNoMessage.setColor(ChatColor.RED);
        voteNoMessage.setBold(true);
        voteNoMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/gov vote no [reason]").create()));
        voteNoMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gov vote no"));

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.spigot().sendMessage(voteYesMessage, mediator, voteNoMessage);
        }
        Bukkit.broadcastMessage(plugin.fullLine);
    }

    public void announceElectionCompetition(Player proposer, Player nominated, Player competitor) {

        Account proposerAccount = plugin.getAccountManager().getAccount(proposer.getUniqueId());
        Account nominatedAccount = plugin.getAccountManager().getAccount(nominated.getUniqueId());
        Account competitorAccount = plugin.getAccountManager().getAccount(competitor.getUniqueId());

        playSoundIfEnabled(proposer, Sound.BLOCK_BEACON_ACTIVATE, 2.0F, 2.0F);

        Bukkit.broadcastMessage(plugin.fullLine);
        Bukkit.broadcastMessage(centeredMessage.get(plugin.mainColor + proposer.getDisplayName() + plugin.highlightColor + "[" + proposerAccount.getRespect() + "]" + plugin.mainColor + " has nominated " + plugin.highlightColor + nominated.getDisplayName() + plugin.mainColor + " to be a " + govManager.getTypeOfGovLeader() + "!"));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + govManager.getTypeOfGovLeader() + " count is currently " + config.getGovLeaders().size() + "/" + config.getMaxLeaders() + " (" + (config.getMaxLeaders() - config.getGovLeaders().size()) + " slots available)."));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.mainColor + "If elected, " + nominated.getDisplayName() + " will replace " + competitor.getDisplayName() + "."));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + nominated.getDisplayName() + plugin.highlightColor + "'s Respect Level: " + nominatedAccount.getRespect() + plugin.defaultColor + "."));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + competitor.getDisplayName() + plugin.highlightColor + "'s Respect Level: " + competitorAccount.getRespect() + plugin.defaultColor + "."));

        TextComponent voteYesMessage = new TextComponent("      CLICK TO VOTE YES");
        voteYesMessage.setColor(ChatColor.GREEN);
        voteYesMessage.setBold(true);
        voteYesMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/gov vote yes [reason]").create()));
        voteYesMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gov vote yes"));

        TextComponent mediator = new TextComponent("   -   ");
        mediator.setColor(ChatColor.WHITE);
        mediator.setBold(true);

        TextComponent voteNoMessage = new TextComponent("CLICK TO VOTE NO");
        voteNoMessage.setColor(ChatColor.RED);
        voteNoMessage.setBold(true);
        voteNoMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/gov vote no [reason]").create()));
        voteNoMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gov vote no"));

        for (Player player : Bukkit.getOnlinePlayers()) {

            player.spigot().sendMessage(voteYesMessage, mediator, voteNoMessage);
        }
        Bukkit.broadcastMessage(plugin.fullLine);
    }

    public void announceAppeal(Player p, String appeal) {

        Bukkit.broadcastMessage(plugin.prefix + " [" + govManager.getTypeOfGovLeader() + "] " + p.getName() + "'s public address: " + ChatColor.BLUE + appeal);
        playSoundIfEnabled(p, Sound.BLOCK_BEACON_ACTIVATE, 2.0F, 1.0F);
    }

    public void announceVote(Player voter, Proposal prop, String vote) {

        Account voterAccount = plugin.getAccountManager().getAccount(voter.getUniqueId());

        if (vote.equalsIgnoreCase("yes")) {
            Bukkit.broadcastMessage(plugin.prefix + voter.getName() + plugin.highlightColor + "[" + voterAccount.getRespect() + "]" + plugin.defaultColor + " voted " + ChatColor.GREEN + "" + ChatColor.BOLD + vote + plugin.defaultColor + " on the current proposal.");
            playSoundIfEnabled(voter, Sound.ENTITY_VILLAGER_YES, 2.0F, 1.0F);
        }
        else {
            Bukkit.broadcastMessage(plugin.prefix + voter.getName() + plugin.highlightColor + "[" + voterAccount.getRespect() + "]" + plugin.defaultColor + " voted " + ChatColor.RED + "" + ChatColor.BOLD + vote + plugin.defaultColor + " on the current proposal.");
            playSoundIfEnabled(voter, Sound.ENTITY_VILLAGER_NO, 2.0F, 1.0F);
        }
    }

    public void announceVote(Player voter, Proposal prop, String vote, String reason) {

        Account voterAccount = plugin.getAccountManager().getAccount(voter.getUniqueId());

        if (vote.equalsIgnoreCase("yes")) {

            Bukkit.broadcastMessage(plugin.prefix + voter.getName() + plugin.highlightColor + "[" + voterAccount.getRespect() + "]" + plugin.defaultColor + " voted " + ChatColor.GREEN + "" + ChatColor.BOLD + vote + plugin.defaultColor + " on the current proposal. Reason: " + ChatColor.WHITE + "" + ChatColor.ITALIC + "\"" + reason + "\".");
            playSoundIfEnabled(voter, Sound.ENTITY_VILLAGER_YES, 2.0F, 1.0F);
        }
        else {

            Bukkit.broadcastMessage(plugin.prefix + voter.getName() + plugin.highlightColor + "[" + voterAccount.getRespect() + "]" + plugin.defaultColor + " voted " + ChatColor.RED + "" + ChatColor.BOLD + vote + plugin.defaultColor + " on the current proposal. Reason: " + ChatColor.WHITE + "" + ChatColor.ITALIC + "\"" + reason + "\".");
            playSoundIfEnabled(voter, Sound.ENTITY_VILLAGER_NO, 2.0F, 1.0F);
        }
    }

    public void playSoundIfEnabled(Player player, Sound sound, float volume, float pitch) {

        if (config.isVoteSoundsForAll()) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

                onlinePlayer.playSound(player.getLocation(), sound, volume, pitch);
            }
        }
        else if (config.isVoteSoundsForSelf()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void announceProposalCancelation(Player canceler, Proposal proposal) {

        Account cancelerAccount = plugin.getAccountManager().getAccount(canceler.getUniqueId());

        Bukkit.broadcastMessage(plugin.prefix + canceler.getName() + plugin.highlightColor + "[" + cancelerAccount.getRespect() + "]" + plugin.defaultColor + " has canceled the proposal " + proposal.getCommand() + ".");
        playSoundIfEnabled(canceler, Sound.BLOCK_BEACON_DEACTIVATE, 2.0F, 2.0F);
    }

    public void announceProposalCancelation(Player canceler, Proposal proposal, String reason) {

        Account cancelerAccount = plugin.getAccountManager().getAccount(canceler.getUniqueId());

        Bukkit.broadcastMessage(plugin.prefix + canceler.getName() + plugin.highlightColor + "[" + cancelerAccount.getRespect() + "]" + plugin.defaultColor + " has canceled the proposal " + proposal.getCommand() + ". Reason: " + ChatColor.ITALIC + "" + ChatColor.WHITE + reason + ".");
        playSoundIfEnabled(canceler, Sound.BLOCK_BEACON_DEACTIVATE, 2.0F, 2.0F);
    }

    public void announceVoteTime(int timeLeftInSeconds) {

        String announceTime;

        if (timeLeftInSeconds >= 60) {
            announceTime = (timeLeftInSeconds / 60 + (timeLeftInSeconds / 60 != 1 ? " minutes" : " minute"));
        }
        else {
            announceTime = (timeLeftInSeconds + (timeLeftInSeconds != 1 ? " seconds" : " second"));
        }

        Bukkit.broadcastMessage(plugin.fullLine);
        Bukkit.broadcastMessage(centeredMessage.get(plugin.mainColor + "Only " + plugin.highlightColor + announceTime + plugin.mainColor + " left to vote on the current proposal!"));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "Run command: " + plugin.highlightColor + voteManager.getCurrentProposal().getCommand()));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "Reason: " + plugin.mainColor + "'" + voteManager.getCurrentProposal().getReason() + "'"));
        Bukkit.broadcastMessage(plugin.fullLine);

        for (Player player : Bukkit.getOnlinePlayers()) {
            playSoundIfEnabled(player, Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 2.0F, 2.0F);
        }

    }

    public void announceProposalResults(boolean passed, float percentYes, float percentNo, int votesYes, int votesNo, String reasonFailed) {

        Player proposer = voteManager.getCurrentProposal().getProposer();
        Account proposerAccount = plugin.getAccountManager().getAccount(proposer.getUniqueId());

        Bukkit.broadcastMessage(plugin.fullLine);
        if (passed) {
            Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + proposer.getDisplayName() + "'s" + plugin.highlightColor + "[" + proposerAccount.getRespect() + "]" + plugin.defaultColor + " proposal to run the command"));
            Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + plugin.highlightColor + voteManager.getCurrentProposal().getCommand() + plugin.defaultColor + " has " + ChatColor.GREEN + "" + ChatColor.BOLD + "PASSED!"));
            delayedAnnouncement("" + plugin.prefix + plugin.highlightColor + "Executing proposal...", 40, false);

            for (Player player : Bukkit.getOnlinePlayers()) {
                playSoundIfEnabled(player, Sound.BLOCK_BEACON_POWER_SELECT, 2.0F, 2.0F);
            }
        }
        else {
            Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + proposer.getDisplayName() + "'s" + plugin.highlightColor + "[" + proposerAccount.getRespect() + "]" + plugin.defaultColor + " proposal to run the command"));
            Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + plugin.highlightColor + voteManager.getCurrentProposal().getCommand() + plugin.defaultColor + " has " + ChatColor.RED + "" + ChatColor.BOLD + "FAILED!"));
            for (Player player : Bukkit.getOnlinePlayers()) {
                playSoundIfEnabled(player, Sound.BLOCK_BEACON_DEACTIVATE, 2.0F, 2.0F);
            }
        }
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "Voted yes: " + ChatColor.GREEN + "" + ChatColor.BOLD + percentYes + "%" + plugin.defaultColor + " Voted no: " + ChatColor.RED + "" + ChatColor.BOLD + percentNo + "%" + plugin.defaultColor + ". Total votes: " + ChatColor.BOLD + (votesYes + votesNo + ".")));

        if (reasonFailed.length() > 1) {
            Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + reasonFailed));
        }
        Bukkit.broadcastMessage(plugin.fullLine);
        voteManager.setVoteCountInProgress(false);
    }

    public void announceElectionResults(boolean passed, float percentYes, float percentNo, int votesYes, int votesNo, String reasonFailed) {

        String nominated = voteManager.getCurrentProposal().getNominated().getDisplayName();
        Player competitor = voteManager.getCurrentProposal().getCompetitor();

        Bukkit.broadcastMessage(plugin.fullLine);
        if (passed) {
            Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "Proposal to elect " + plugin.highlightColor + nominated + plugin.defaultColor + " has " + ChatColor.GREEN + "" + ChatColor.BOLD + "PASSED!"));
            if (competitor != null) {
                Bukkit.broadcastMessage(centeredMessage.get(plugin.mainColor + nominated + " won!"));
            }
            delayedAnnouncement("" + plugin.prefix + plugin.highlightColor + "Putting " + nominated + " in office as a " + govManager.getTypeOfGovLeader() + "...", 40, false);
            for (Player player : Bukkit.getOnlinePlayers()) {
                playSoundIfEnabled(player, Sound.BLOCK_BEACON_POWER_SELECT, 2.0F, 2.0F);
            }
        }
        else {
            Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "Proposal to elect " + plugin.highlightColor + nominated + plugin.defaultColor + " to be a " + plugin.highlightColor + govManager.getTypeOfGovLeader() + plugin.defaultColor + " has " + ChatColor.RED + "" + ChatColor.BOLD + "FAILED!"));
            if (competitor != null) {
                Bukkit.broadcastMessage(centeredMessage.get(plugin.mainColor + competitor.getDisplayName() + " won!"));
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                playSoundIfEnabled(player, Sound.BLOCK_BEACON_DEACTIVATE, 2.0F, 2.0F);
            }
        }

        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "Voted yes: " + ChatColor.GREEN + "" + ChatColor.BOLD + percentYes + "%" + plugin.defaultColor + " Voted no: " + ChatColor.RED + "" + ChatColor.BOLD + percentNo + "%" + plugin.defaultColor + ". Total votes: " + ChatColor.BOLD + (votesYes + votesNo + ".")));

        if (reasonFailed.length() > 1) {
            Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + reasonFailed));
        }
        Bukkit.broadcastMessage(plugin.fullLine);
        voteManager.setVoteCountInProgress(false);
    }

    public void announceVoteCount() {

        int votesYes = voteManager.getCurrentProposal().getVotedYes().size();
        int votesNo = voteManager.getCurrentProposal().getVotedNo().size();
        int abstentions = Bukkit.getOnlinePlayers().size() - (votesYes + votesNo);

        if (config.getGovType() == 1) {
            abstentions = config.getGovLeaders().size() - (votesYes + votesNo);
        }

        Bukkit.broadcastMessage(plugin.fullLine);
        Bukkit.broadcastMessage(centeredMessage.get(plugin.prefix + plugin.defaultColor + "Counting votes..."));
        Bukkit.broadcastMessage(plugin.fullLine);

        for (Player player : Bukkit.getOnlinePlayers()) {
            playSoundIfEnabled(player, Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 2.0F, 2.0F);
        }

        delayedAnnouncement("" + ChatColor.GREEN + ChatColor.BOLD + "Number of votes in favor: " + votesYes, 15, false);
        delayedAnnouncement("" + ChatColor.RED + ChatColor.BOLD + "Number of votes against: " + votesNo, 30, false);
        delayedAnnouncement("" + ChatColor.YELLOW + ChatColor.BOLD + "Number of abstentions: " + abstentions, 45, false);
        delayedAnnouncement("" + ChatColor.WHITE + ChatColor.BOLD + "Percentage needed to pass: " + config.getPercentNeededToPass() + "%", 60, false);
        delayedAnnouncement("" + ChatColor.WHITE + ChatColor.BOLD + "Minimum votes required to pass: " + config.getMinVotesRequired(), 75, true);
    }

    private void delayedAnnouncement(String announcement, int delay, boolean lastAnnouncement) {

        new BukkitRunnable() {

            public void run() {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    playSoundIfEnabled(player, Sound.BLOCK_METAL_HIT, 2.0F, 2.0F);
                }

                Bukkit.broadcastMessage(centeredMessage.get(announcement));

                if (lastAnnouncement) {
                    if (voteManager.getCurrentProposal().getNominated() != null) {
                        voteManager.countVotes(true);
                    }
                    else {
                        voteManager.countVotes(false);
                    }
                }
                cancel();
            }
        }.runTaskLater(plugin, delay);
    }

    public void announceDictatedProposal(Proposal proposal) {

        Player dictator = proposal.getProposer();
        Account dictatorAccount = plugin.getAccountManager().getAccount(dictator.getUniqueId());

        Bukkit.broadcastMessage(plugin.fullLine);
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + dictator.getDisplayName() + plugin.highlightColor + "[" + dictatorAccount.getRespect() + "]" + plugin.defaultColor + " (Dictator) has issued a decree"));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "Run command: " + plugin.highlightColor + proposal.getCommand()));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "Reason: " + plugin.mainColor + "'" + proposal.getReason() + "'"));
        delayedAnnouncement("" + plugin.prefix + plugin.highlightColor + "Executing proposal...", 40, false);

        for (Player player : Bukkit.getOnlinePlayers()) {
            playSoundIfEnabled(player, Sound.BLOCK_BEACON_POWER_SELECT, 2.0F, 2.0F);
        }
        Bukkit.broadcastMessage(plugin.fullLine);

    }

    public void announceElectionVoteCount() {

        int votesYes = voteManager.getCurrentProposal().getVotedYes().size();
        int votesNo = voteManager.getCurrentProposal().getVotedNo().size();
        int abstentions = Bukkit.getOnlinePlayers().size() - (votesYes + votesNo);

        Bukkit.broadcastMessage(plugin.fullLine);
        Bukkit.broadcastMessage(centeredMessage.get(plugin.prefix + plugin.defaultColor + "Counting votes..."));
        Bukkit.broadcastMessage(plugin.fullLine);

        for (Player player : Bukkit.getOnlinePlayers()) {
            playSoundIfEnabled(player, Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 2.0F, 2.0F);
        }

        delayedAnnouncement("" + ChatColor.GREEN + ChatColor.BOLD + "Number of votes in favor: " + votesYes, 15, false);
        delayedAnnouncement("" + ChatColor.RED + ChatColor.BOLD + "Number of votes against: " + votesNo, 30, false);
        delayedAnnouncement("" + ChatColor.YELLOW + ChatColor.BOLD + "Number of abstentions: " + abstentions, 45, false);
        delayedAnnouncement("" + ChatColor.WHITE + ChatColor.BOLD + "Percentage needed to pass: " + config.getPercentNeededToPass() + "%", 60, false);
        delayedAnnouncement("" + ChatColor.WHITE + ChatColor.BOLD + "Minimum votes required to pass: " + config.getMinVotesRequired(), 75, true);
    }

    public void announceElectionVoteTime(int timeLeftInSeconds) {

        String announceTime;
        Player nominated = voteManager.getCurrentProposal().getNominated();
        Player competitor = voteManager.getCurrentProposal().getCompetitor();

        Account nominatedAccount = plugin.getAccountManager().getAccount(nominated.getUniqueId());

        if (timeLeftInSeconds >= 60) {
            announceTime = (timeLeftInSeconds / 60 + (timeLeftInSeconds / 60 != 1 ? " minutes" : " minute"));
        }
        else {
            announceTime = (timeLeftInSeconds + (timeLeftInSeconds != 1 ? " seconds" : " second"));
        }

        Bukkit.broadcastMessage(plugin.fullLine);
        Bukkit.broadcastMessage(centeredMessage.get(plugin.mainColor + "Only " + plugin.highlightColor + announceTime + plugin.mainColor + " left to vote in the election!"));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "Elect " + plugin.highlightColor + nominated.getDisplayName() + plugin.defaultColor + " to be a " + plugin.highlightColor + govManager.getTypeOfGovLeader() + plugin.defaultColor + "."));
        if (competitor != null) {
            Account competitorAccount = plugin.getAccountManager().getAccount(competitor.getUniqueId());
            Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + "(" + nominated.getDisplayName() + plugin.highlightColor + "[" + nominatedAccount.getRespect() + "]" + plugin.defaultColor + " would replace " + competitor.getDisplayName() + plugin.highlightColor + "[" + competitorAccount.getRespect() + "]" + plugin.defaultColor + ")"));
        }
        Bukkit.broadcastMessage(plugin.fullLine);

        for (Player player : Bukkit.getOnlinePlayers()) {
            playSoundIfEnabled(player, Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 2.0F, 2.0F);
        }
    }

    public void announceResignation(Player resigned) {

        Account resignedAccount = plugin.getAccountManager().getAccount(resigned.getUniqueId());

        Bukkit.broadcastMessage(plugin.fullLine);
        Bukkit.broadcastMessage(centeredMessage.get(plugin.mainColor + resigned.getDisplayName() + plugin.highlightColor + "[" + resignedAccount.getRespect() + "]" + plugin.mainColor + " has resigned from office!"));
        Bukkit.broadcastMessage(centeredMessage.get(plugin.defaultColor + govManager.getTypeOfGovLeader() + " count is now " + config.getGovLeaders().size() + "/" + config.getMaxLeaders() + " (" + (config.getMaxLeaders() - config.getGovLeaders().size()) + " slots available)."));
        Bukkit.broadcastMessage(plugin.fullLine);

        for (Player player : Bukkit.getOnlinePlayers()) {
            playSoundIfEnabled(player, Sound.BLOCK_BEACON_DEACTIVATE, 2.0F, 2.0F);
        }
    }

}