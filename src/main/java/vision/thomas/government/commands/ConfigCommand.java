package vision.thomas.government.commands;

import org.bukkit.command.CommandSender;
import vision.thomas.government.Config;
import vision.thomas.government.Government;
import vision.thomas.government.GovernmentManager;
import vision.thomas.government.commands.helpers.SubCommand;

public class ConfigCommand extends SubCommand {

    private final Government plugin;

    private GovernmentManager govManager;

    private final String prefix;

    private Config config;

    public ConfigCommand(Government plugin) {

        super(plugin, plugin.getName().toLowerCase(), "config", "[enable | disable | reload | addLeader | removeLeader | setType]", "Allows the plugin configuration to be modified in-game.");
        this.plugin = plugin;
        config = plugin.config;
        govManager = new GovernmentManager(plugin);
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

                sender.sendMessage(prefix + "Retrieving latest values from config.yml...");
                config.reloadConfig();
                sender.sendMessage(prefix + "Configuration reloaded!");
            }

            else if (args[0].equalsIgnoreCase("addLeader")) {

                if (args.length > 1) {
                    sender.sendMessage(govManager.addGovLeader(args[1]));
                }
                else {
                    sender.sendMessage(prefix + "Please specify what player you would like to become a " + govManager.getTypeOfGovLeader() + ".");
                }
            }

            else if (args[0].equalsIgnoreCase("removeLeader")) {

                if (args.length > 1) {
                    sender.sendMessage(govManager.removeGovLeader(args[1]));
                }
                else {
                    sender.sendMessage(prefix + "Please specify what player you would no longer like to be a " + govManager.getTypeOfGovLeader() + ".");
                }
            }

            else if (args[0].equalsIgnoreCase("setType")) {

                if (args.length > 1) {
                    sender.sendMessage(govManager.setGovType(Integer.parseInt(args[1])));
                }
                else {
                    sender.sendMessage(prefix + "Please specify what what type of government.");
                }
            }

            else if (args[0].equalsIgnoreCase("addcommand")) {

                if (args.length > 1) {

                    String command = "";
                    for (int i = 0; i < args.length; i++) {
                        if (i != 0) {
                            command += args[i] + " ";
                        }
                    }
                    command = command.substring(0, command.length() - 1);

                    sender.sendMessage(govManager.addAllowedCommand(command));
                }
                else {
                    sender.sendMessage(prefix + "Please specify what command you would like to add as a proposal.");
                }
            }
            else if (args[0].equalsIgnoreCase("removecommand")) {

                if (args.length > 1) {

                    String command = "";
                    for (int i = 0; i < args.length; i++) {
                        if (i != 0) {
                            command += args[i] + " ";
                        }
                    }
                    command = command.substring(0, command.length() - 1);

                    sender.sendMessage(govManager.removeAllowedCommand(command));
                }
                else {
                    sender.sendMessage(prefix + "Please specify what command you would like remove.");
                }
            }

            //
            // Everything below this line is just a placeholder.
            //

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
