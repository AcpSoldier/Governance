package vision.thomas.government.commands;

import org.bukkit.command.CommandSender;
import vision.thomas.government.Config;
import vision.thomas.government.Government;
import vision.thomas.government.commands.helpers.SubCommand;

public class ConfigCommand extends SubCommand {

    private final Government plugin;

    private final String prefix;

    private Config config;

    public ConfigCommand(Government plugin) {

        super(plugin, plugin.getName().toLowerCase(), "config", "[enable | disable | reload | autoUpdate | repeatTime | expireTime | minimumOnlinePlayers | passPercent | maxTermLength | minTermLength | maxinOffice | runDelayTime | addCommand | removeCommand | minRespect]", "Allows the plugin configuration to be modified in-game.");
        this.plugin = plugin;
        config = plugin.conf;
        prefix = plugin.prefix;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("disable")) {
                if (!config.isPluginEnabled) {
                    sender.sendMessage(prefix + "The plugin is already disabled.");
                }
                else {
                    config.setPluginEnabled(false);
                    sender.sendMessage(prefix + "Plugin disabled.");
                }
            }
            else if (args[0].equalsIgnoreCase("enable")) {
                if (config.isPluginEnabled) {
                    sender.sendMessage(prefix + "The plugin is already enabled.");
                }
                else {
                    config.setPluginEnabled(true);
                    sender.sendMessage(prefix + "Plugin has been re-enabled.");
                }
            }
            else if (args[0].equalsIgnoreCase("reload")) {
                sender.sendMessage(prefix + "Getting latest values from the config...");

                sender.sendMessage(prefix + "Config reloaded!");
            }
            else if (args[0].equalsIgnoreCase("autoUpdate")) {
                sender.sendMessage(prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("repeatTime")) {
                sender.sendMessage(prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("expireTime")) {
                sender.sendMessage(prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("minimumOnlinePlayers")) {
                sender.sendMessage(prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("passPercent")) {
                sender.sendMessage(prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("maxTermLength")) {
                sender.sendMessage(prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("minTermLength")) {
                sender.sendMessage(prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("maxInOffice")) {
                sender.sendMessage(prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("runDelayTime")) {
                sender.sendMessage(prefix + "Logic for editing config file.");
            }
            else if (args[0].equalsIgnoreCase("addcommand")) {
                if (args.length > 1) {
                    sender.sendMessage(prefix + "Logic for adding a command to the config.");
                    sender.sendMessage(prefix + "Players can now propose to run the command '" + args[1] + "'.");
                }
                else {
                    sender.sendMessage(prefix + "Please specify what command you would like to add as a proposal.");
                }
            }
            else if (args[0].equalsIgnoreCase("removecommand")) {
                if (args.length > 1) {
                    sender.sendMessage(prefix + "Logic for checking if a command exists in the config. If it does, remove it.");
                    sender.sendMessage(prefix + "Command removed. Players can no longer propose to run the command '" + args[1] + "'.");
                }
                else {
                    sender.sendMessage(prefix + "Please specify what command you would like remove.");
                }
            }
            else if (args[0].equalsIgnoreCase("minrespect")) {
                if (args.length > 1) {
                    if (args.length > 2) {
                        sender.sendMessage(prefix + "Logic for checking if a command exists in the config. If it does, edit it's minimum Respect Level.");
                        sender.sendMessage(prefix + "The minimum Respect Level needed to run the command '" + args[1] + "' has been set to " + args[2] + ".");
                    }
                    else {
                        sender.sendMessage(prefix + "Logic for checking if a command exists in the config. If it does, edit it's minimum Respect Level.");
                        sender.sendMessage(prefix + "Please specify what you like the minimum Respect Level to be for the command: '" + args[1] + "'.");
                        return true; // To make sure that the message below doesn't run? I'll have to test that out later.
                    }
                }
                else {
                    sender.sendMessage(prefix + "Please specify what command you would like to configure Respect Level for.");
                }
            }
            else {
                sender.sendMessage(prefix + "'" + args[0] + "' is not a valid configuration setting.");
                sender.sendMessage(this.getUsage());
            }
        }
        else {
            sender.sendMessage(prefix + "Please choose a setting to configure.");
            sender.sendMessage(this.getUsage());
        }
        return true;
    }

}
