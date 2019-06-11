package vision.thomas.government.commands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import vision.thomas.government.*;
import vision.thomas.government.commands.helpers.CenteredMessage;
import vision.thomas.government.commands.helpers.SubCommand;

public class ProposalCommand extends SubCommand implements Listener {

    private Government plugin;

    private VoteManager voteManager;

    private GovernmentManager governmentManager;

    private Announcements announcements;

    private CenteredMessage centeredMessage = new CenteredMessage();

    private Config config;

    public ProposalCommand(Government plugin) {

        super(plugin, plugin.getName().toLowerCase(), "proposal", "[create | cancel | list]", "Allows players to create, cancel, and list proposals to be voted on.");

        this.plugin = plugin;
        voteManager = plugin.getVoteManager();
        governmentManager = plugin.getGovManager();
        announcements = plugin.getAnnouncement();
        config = plugin.getConf();
    }

    public boolean execute(CommandSender sender, String[] args) {

        if (args.length > 0 && sender instanceof Player) {

            Player proposer = (Player) sender;

            if (args[0].equalsIgnoreCase("create")) {

                switch (config.getGovType()) {

                    default: // Direct Democracy
                        createProposal(proposer);
                        break;

                    case 1: // Republic
                        if (governmentManager.govLeadersContains(proposer.getDisplayName())) {
                            createProposal(proposer);
                        }
                        else {
                            proposer.sendMessage(plugin.prefix + "You must be a " + governmentManager.getTypeOfGovLeader() + " to create proposals under a " + governmentManager.getGovName() + ".");
                        }
                        break;
                    case 2: // Dictatorship
                        if (governmentManager.govLeadersContains(proposer.getDisplayName())) {
                            createProposal(proposer);
                        }
                        else {
                            proposer.sendMessage(plugin.prefix + "You must be a " + governmentManager.getTypeOfGovLeader() + " to use this command.");
                        }
                        break;
                }
            }
            else if (args[0].equalsIgnoreCase("cancel")) {

                switch (config.getGovType()) {

                    default: // Direct Democracy
                        cancelProposal(proposer, args);
                        break;

                    case 1: // Republic
                        if (governmentManager.govLeadersContains(proposer.getDisplayName()) || proposer.isOp()) {
                            cancelProposal(proposer, args);
                        }
                        else {
                            proposer.sendMessage(plugin.prefix + "You must be a " + governmentManager.getTypeOfGovLeader() + " to use this command.");
                        }
                        break;
                    case 2: // Dictatorship
                        if (governmentManager.govLeadersContains(proposer.getDisplayName()) || proposer.isOp()) {
                            cancelProposal(proposer, args);
                        }
                        else {
                            proposer.sendMessage(plugin.prefix + "You must be a " + governmentManager.getTypeOfGovLeader() + " to use this command.");
                        }
                        break;
                }
            }
            else if (args[0].equalsIgnoreCase("list")) {

                listProposals(proposer);
            }
            else {
                sender.sendMessage(plugin.prefix + "Incorrect arguments.");
                sender.sendMessage(this.getUsage());
            }

        }
        else {
            sender.sendMessage(plugin.prefix + "Only players can vote on proposals, sorry console : ( ");
        }
        return true;
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {

        if (voteManager.creatingProposal.containsKey(event.getPlayer())) {

            event.setCancelled(true);

            // Un-tested. The idea is that if for some reason, if a player is stuck in proposal creation mode (or election?), this will clear them if a vote is already in progress.
            if (voteManager.isVoteInProgress()) {
                voteManager.creatingProposal.clear();
                event.setCancelled(false);
            }

            Player proposer = event.getPlayer();

            if (voteManager.creatingProposal.get(proposer) == null) {

                boolean commandExists = false;

                for (int i = 0; i < config.getAllowedCommands().size(); i++) {
                    if (event.getMessage().toLowerCase().contains(config.getAllowedCommands().get(i))) {

                        voteManager.creatingProposal.replace(proposer, event.getMessage());
                        proposer.playSound(proposer.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 2.0F, 2F);

                        proposer.sendMessage(plugin.prefix + "If your proposal succeeds, the console will run the command " + plugin.highlightColor + "/" + event.getMessage() + ".");
                        proposer.sendMessage(plugin.prefix + "Now, what is the reason for this proposal? In a new chat message, type your appeal to the players.");
                        commandExists = true;
                        break;
                    }
                }
                if (!commandExists) {

                    proposer.playSound(proposer.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 2.0F, 0.5F);
                    proposer.sendMessage(plugin.prefix + "/" + event.getMessage().toLowerCase() + " is not on the list of approved commands.");
                    proposer.sendMessage(plugin.prefix + "Type '/gov proposal list' for a list of commands, or perhaps propose to add /" + event.getMessage().toLowerCase() + " to the list of approved commands.");
                    proposer.sendMessage(plugin.prefix + "Type '/gov proposal cancel' to cancel this proposal");
                }
            }

            else if (voteManager.creatingProposal.get(proposer) != null) {

                String reason = "" + plugin.defaultColor + "Reason: " + plugin.mainColor;
                reason = reason + event.getMessage();

                if (centeredMessage.getMessagePxlSize(reason) > centeredMessage.MAX_PX) {

                    String hoverMessage = "" + ChatColor.GRAY + ChatColor.BOLD + "...[Hover]" + plugin.mainColor;
                    reason = reason + hoverMessage;

                    for (int i = centeredMessage.getMessagePxlSize(reason); i > centeredMessage.MAX_PX; ) {
                        i = centeredMessage.getMessagePxlSize(reason);
                        reason = reason.substring(0, reason.length() - hoverMessage.length() - 1);
                        reason = reason + hoverMessage;
                    }
                }

                voteManager.setVoteInProgress(true);
                Proposal proposal = new Proposal(plugin);
                proposal.setCommand("/" + voteManager.creatingProposal.get(proposer));
                proposal.setReason(reason);
                proposal.setFullReason(event.getMessage());
                proposal.setProposer(proposer);
                voteManager.setCurrentProposal(proposal);

                if (config.getGovType() != 2) {

                    proposer.sendMessage(plugin.prefix + ChatColor.BOLD + "Proposal created successfully.");

                    announcements.announceProposal(proposer, proposal);
                    voteManager.creatingProposal.remove(proposer);

                    for (Player player : voteManager.creatingProposal.keySet()) {
                        player.sendMessage(plugin.prefix + ChatColor.BOLD + "Proposal creation canceled; " + proposal.getProposer().getName() + " created one before you.");
                    }

                    voteManager.creatingProposal.clear();
                    voteManager.startTimer(proposal, false);
                }
                else {
                    announcements.announceDictatedProposal(proposal);
                    voteManager.executeProposalCommand(60);
                    voteManager.creatingProposal.remove(proposer);

                    for (Player player : voteManager.creatingProposal.keySet()) {
                        player.sendMessage(plugin.prefix + ChatColor.BOLD + "Proposal creation canceled; " + proposal.getProposer().getName() + " created one before you.");
                    }

                    voteManager.creatingProposal.clear();
                }

            }
        }

    }

    private void createProposal(Player proposer) {

        if (!voteManager.isVoteInProgress()) {

            voteManager.creatingProposal.put(proposer, null);
            proposer.sendMessage(plugin.prefix + ChatColor.BOLD + "Proposal creation started.");
            proposer.playSound(proposer.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 2.0F, 2F);
            proposer.sendMessage(plugin.prefix + "In a new chat message, please type exactly what command you are proposing to run but " + ChatColor.RED + "WITHOUT A '/' at the beginning.");
        }
        else {
            proposer.sendMessage(plugin.prefix + "Please wait until the current vote has ended before proposing a new one.");
        }

    }

    private void cancelProposal(Player proposer, String[] args) {

        if (voteManager.isVoteInProgress()) {

            if (!voteManager.isVoteCountInProgress()) {

                if (voteManager.getCurrentProposal().getProposer().getName().equalsIgnoreCase(proposer.getName()) || proposer.isOp()) {

                    voteManager.setVoteInProgress(false);
                    if (args.length > 1) {

                        String reason = "";
                        for (int i = 0; i < args.length; i++) {
                            if (i != 0) {
                                reason += args[i] + " ";
                            }
                        }
                        reason = reason.substring(0, reason.length() - 1);

                        announcements.announceProposalCancelation(proposer, voteManager.getCurrentProposal(), reason);
                    }
                    else {
                        announcements.announceProposalCancelation(proposer, voteManager.getCurrentProposal());
                    }

                    proposer.sendMessage(plugin.prefix + "Your proposal has been cancelled.");
                    voteManager.getCurrentProposal().cancelProposal(proposer);
                }
                else {
                    proposer.sendMessage(plugin.prefix + "Only " + ChatColor.AQUA + "" + voteManager.getCurrentProposal().getProposer().getName() + plugin.defaultColor + " can cancel the current proposal.");
                }
            }
            else {
                proposer.sendMessage(plugin.prefix + ChatColor.BOLD + "It's too late to cancel this proposal!");
            }
        }
        else if (voteManager.creatingProposal.containsKey(proposer)) {

            voteManager.creatingProposal.remove(proposer);
            proposer.sendMessage(plugin.prefix + ChatColor.BOLD + "Proposal creation canceled. ");
            proposer.playSound(proposer.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 2.0F, 0.5F);
        }
        else {

            proposer.sendMessage(plugin.prefix + "There is currently no active proposal to cancel");
        }
    }

    private void listProposals(Player proposer) {

        proposer.sendMessage(plugin.prefix + "Here is a list of the allowed proposal commands:");

        for (int i = 0; i < config.getAllowedCommands().size(); i++) {
            proposer.sendMessage(plugin.mainColor + "/" + config.getAllowedCommands().get(i));
        }

        proposer.sendMessage(plugin.prefix + "To add a new command, use '/gov config addcommand <full command arguments>.");
    }

}
