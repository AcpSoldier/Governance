package vision.thomas.government.commands;

import org.bukkit.command.CommandSender;
import vision.thomas.government.Government;
import vision.thomas.government.commands.helpers.SubCommand;

public class ConfigCommand extends SubCommand {

    private final Government plugin;

    public ConfigCommand(Government plugin) {

        super(plugin, plugin.getName().toLowerCase(), "config", "[enable | disable | autoUpdate | repeatTime | expireTime | minimumOnlinePlayers | passPercent | maxTermLength | minTermLength | maxinOffice | runDelayTime | addCommand | removeCommand | minRespect]", "Allows the plugin configuration to be modified in-game.");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("disable")) {
                sender.sendMessage(plugin.prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("enable")) {
                sender.sendMessage(plugin.prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("autoUpdate")) {
                sender.sendMessage(plugin.prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("repeatTime")) {
                sender.sendMessage(plugin.prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("expireTime")) {
                sender.sendMessage(plugin.prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("minimumOnlinePlayers")) {
                sender.sendMessage(plugin.prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("passPercent")) {
                sender.sendMessage(plugin.prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("maxTermLength")) {
                sender.sendMessage(plugin.prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("minTermLength")) {
                sender.sendMessage(plugin.prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("maxInOffice")) {
                sender.sendMessage(plugin.prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("runDelayTime")) {
                sender.sendMessage(plugin.prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("addcommand")) {
                if (args.length > 1) {
                    sender.sendMessage(plugin.prefix + "Logic for adding a command to the config.");
                    sender.sendMessage(plugin.prefix + "Players can now propose to run the command '" + args[1] + "'.");
                }
                else {
                    sender.sendMessage(plugin.prefix + "Please specify what command you would like to add as a proposal.");
                }
            }
            else if (args[0].equalsIgnoreCase("removecommand")) {
                if (args.length > 1) {
                    sender.sendMessage(plugin.prefix + "Logic for checking if a command exists in the config. If it does, remove it.");
                    sender.sendMessage(plugin.prefix + "Command removed. Players can no longer propose to run the command '" + args[1] + "'.");
                }
                else {
                    sender.sendMessage(plugin.prefix + "Please specify what command you would like remove.");
                }
            }
            else if (args[0].equalsIgnoreCase("minrespect")) {
                if (args.length > 1) {
                    if (args.length > 2) {
                        sender.sendMessage(plugin.prefix + "Logic for checking if a command exists in the config. If it does, edit it's minimum Respect Level.");
                        sender.sendMessage(plugin.prefix + "The minimum Respect Level needed to run the command '" + args[1] + "' has been set to " + args[2] + ".");
                    }
                    else {
                        sender.sendMessage(plugin.prefix + "Logic for checking if a command exists in the config. If it does, edit it's minimum Respect Level.");
                        sender.sendMessage(plugin.prefix + "Please specify what you like the minimum Respect Level to be for the command: '" + args[1] + "'.");
                        return true; // To make sure that the message below doesn't run? I'll have to test that out later.
                    }
                }
                else {
                    sender.sendMessage(plugin.prefix + "Please specify what command you would like to configure Respect Level for.");
                }
            }
            else {
                sender.sendMessage(plugin.prefix + "'" + args[0] + "' is not a valid configuration setting.");
                sender.sendMessage(this.getUsage());
            }
        }
        else {
            sender.sendMessage(plugin.prefix + "Please choose a setting to configure.");
            sender.sendMessage(this.getUsage());
        }
        return true;
    }

}
