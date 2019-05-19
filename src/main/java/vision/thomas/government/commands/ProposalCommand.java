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

import java.util.HashMap;
import java.util.Map;

public class ProposalCommand extends SubCommand implements Listener {

    private Government plugin;

    private VoteManager voteManager;

    private Announcement announcement;

    private CenteredMessage centeredMessage = new CenteredMessage();

    private Config config;

    private Map<Player, String> creatingProposal = new HashMap<>();

    public ProposalCommand(Government plugin) {

        super(plugin, plugin.getName().toLowerCase(), "proposal", "[create | cancel | list]", "Allows players to create, cancel, and list proposals to be voted on.");

        this.plugin = plugin;
        voteManager = new VoteManager(plugin);
        announcement = new Announcement(plugin);
        config = new Config(plugin);
    }

    public boolean execute(CommandSender sender, String[] args) {

        if (args.length > 0 && sender instanceof Player) {

            Player proposer = (Player) sender;

            if (args[0].equalsIgnoreCase("create")) {

                if (!voteManager.isVoteInProgress()) {

                    creatingProposal.put(proposer, null);
                    proposer.sendMessage(plugin.prefix + ChatColor.BOLD + "Proposal creation started.");
                    proposer.playSound(proposer.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 2.0F, 2F);
                    proposer.sendMessage(plugin.prefix + "In a new chat message, please type exactly what command you are proposing to run but " + ChatColor.RED + "WITHOUT A '/' at the beginning.");
                }
                else {
                    proposer.sendMessage(plugin.prefix + "Please wait until the current vote has ended before proposing a new one.");
                }
            }
            else if (args[0].equalsIgnoreCase("cancel")) {

                if (voteManager.isVoteInProgress()) {

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

                            announcement.announceProposalCancelation(proposer, voteManager.getCurrentProposal(), reason);
                        }
                        else {
                            announcement.announceProposalCancelation(proposer, voteManager.getCurrentProposal());
                        }

                        proposer.sendMessage(plugin.prefix + "Your proposal has been cancelled.");
                        voteManager.getCurrentProposal().cancelProposal(proposer);
                    }
                    else {
                        proposer.sendMessage(plugin.prefix + "Only " + ChatColor.AQUA + "" + voteManager.getCurrentProposal().getProposer().getName() + plugin.defaultColor + " can cancel the current proposal.");
                    }
                }
                else if (creatingProposal.containsKey(proposer)) {

                    creatingProposal.remove(proposer);
                    proposer.sendMessage(plugin.prefix + ChatColor.BOLD + "Proposal creation canceled. ");
                    proposer.playSound(proposer.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 2.0F, 0.5F);
                }
                else {

                    proposer.sendMessage(plugin.prefix + "There is currently no active proposal to cancel");
                }
            }
            else if (args[0].equalsIgnoreCase("list")) {

                sender.sendMessage(plugin.prefix + "Here is a list of the allowed proposal commands:");

                config.reloadConfig();
                for (int i = 0; i < config.allowedCommands.size(); i++) {
                    sender.sendMessage(plugin.mainColor + "/" + config.allowedCommands.get(i));
                }

                sender.sendMessage(plugin.prefix + "To add a new command, use '/gov config addcommand <full command arguments>.");
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

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {

        if (creatingProposal.containsKey(event.getPlayer())) {

            Player proposer = event.getPlayer();

            if (creatingProposal.get(proposer) == null) {

                config.reloadConfig();
                boolean commandExists = false;

                for (int i = 0; i < config.allowedCommands.size(); i++) {
                    if (event.getMessage().toLowerCase().contains(config.allowedCommands.get(i))) {

                        creatingProposal.replace(proposer, event.getMessage());
                        proposer.playSound(proposer.getLocation(), Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 2.0F, 2F);

                        proposer.sendMessage(plugin.prefix + "If your proposal succeeds, the console will run the command " + plugin.highlightColor + "/" + event.getMessage() + ".");
                        proposer.sendMessage(plugin.prefix + "Now, what is the reason for this proposal? In a new chat message, type your appeal to the voters.");
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
            else if (creatingProposal.get(proposer) != null) {

                if (centeredMessage.getMessagePxlSize(event.getMessage()) < centeredMessage.MAX_PX) {
                    voteManager.setVoteInProgress(true);
                    Proposal proposal = new Proposal(plugin);
                    proposal.setCommand(creatingProposal.get(proposer));
                    proposal.setReason(event.getMessage());
                    proposal.setProposer(proposer);
                    voteManager.setCurrentProposal(proposal);

                    proposer.sendMessage(plugin.prefix + ChatColor.BOLD + "Proposal created successfully.");

                    announcement.announceProposal(proposer, proposal);
                    creatingProposal.remove(proposer);

                    //NOT TESTED YET!!!
                    for (Player player : creatingProposal.keySet()) {
                        proposer.sendMessage(plugin.prefix + ChatColor.BOLD + "Proposal creation canceled; " + proposal.getProposer().getName() + " created one before you.");
                    }

                    creatingProposal.clear();
                    voteManager.startTimer();
                }
                else {
                    proposer.sendMessage(plugin.prefix + "Reason must be short enough to fit on one line. Please try again.");
                }
            }

            event.setCancelled(true);
        }
    }

}
