package vision.thomas.government.commands.helpers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import vision.thomas.government.Config;
import vision.thomas.government.Government;

import java.util.*;

/**
 * Copyright 2016 Max Lee (https://github.com/Phoenix616/)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Mozilla Public License as published by
 * the Mozilla Foundation, version 2.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License v2.0 for more details.
 * <p/>
 * You should have received a copy of the Mozilla Public License v2.0
 * along with this program. If not, see <http://mozilla.org/MPL/2.0/>.
 */
public class CommandExec implements CommandExecutor {

    private final Government plugin;

    private Config config;

    private Map<String, Map<String, SubCommand>> subCommands = new HashMap<String, Map<String, SubCommand>>();

    private String header;

    public CommandExec(Government plugin) {

        this.plugin = plugin;
        header = ChatColor.WHITE + plugin.getDescription().getAuthors().get(0) + "'s " +
                ChatColor.GOLD + plugin.getName() +
                ChatColor.WHITE + " v" + plugin.getDescription().getVersion();
        plugin.getCommand(plugin.getName().toLowerCase()).setExecutor(this);

        config = plugin.conf;
    }

    public void register(SubCommand sub) {

        if (!subCommands.containsKey(sub.getCommand())) {
            subCommands.put(sub.getCommand(), new LinkedHashMap<String, SubCommand>());
        }
        if (subCommands.get(sub.getCommand()).containsKey(sub.getPath())) {
            throw new IllegalArgumentException("A sub command with the path '" + sub.getPath() + "' is already defined for command '" + sub.getCommand() + "'!");
        }
        subCommands.get(sub.getCommand()).put(sub.getPath(), sub);
        try {
            plugin.getServer().getPluginManager().addPermission(sub.getPermission());
        } catch (IllegalArgumentException ignore) {
            // Permission was already defined correctly in the plugin.yml
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Denies all attempts to run commands while the plugin is disabled EXCEPT for the "enable" command.
        if (!config.isPluginEnabled) {
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("enable")) {

                }
                else {
                    sender.sendMessage(plugin.prefix + "The plugin is currently disabled from the config. An admin may re-enable it with /gov config enable.");
                    return true;
                }
            }
            else {
                sender.sendMessage(plugin.prefix + "The plugin is currently disabled from the config. An admin may re-enable it with /gov config enable.");
                return true;
            }
        }
        if (args.length == 0) {
            List<String> helpText = new ArrayList<String>();
            helpText.add(header);
            if (subCommands.containsKey(cmd.getName())) {
                for (SubCommand sub : subCommands.get(cmd.getName()).values()) {
                    if (!sender.hasPermission(sub.getPermission())) {
                        continue;
                    }
                    helpText.add(ChatColor.GOLD + sub.getUsage(label));
                    helpText.add(ChatColor.WHITE + " " + sub.getHelp());
                }
            }
            else {
                helpText.add("No sub commands found.");
            }
            sender.sendMessage(helpText.toArray(new String[helpText.size()]));
            return true;
        }

        SubCommand sub = null;

        int pathPartCount = 1;
        if (subCommands.containsKey(cmd.getName())) {
            String path = args[0];
            while (!subCommands.get(cmd.getName()).containsKey(path) && pathPartCount < args.length) {
                path += " " + args[pathPartCount].toLowerCase();
                pathPartCount++;
            }
            sub = subCommands.get(cmd.getName()).get(path);
        }

        if (sub == null) {
            if (subCommands.containsKey(cmd.getName())) {
                Set<String> subCmdsStr = subCommands.get(cmd.getName()).keySet();
                sender.sendMessage("Usage: /" + label + " " + Arrays.toString(subCmdsStr.toArray(new String[subCmdsStr.size()])));
                return true;
            }
            else {
                return false;
            }
        }

        if (!sender.hasPermission(sub.getPermission())) {
            sender.sendMessage(plugin.getCommand(sub.getCommand()).getPermissionMessage().replace("<permission>", sub.getPermission().getName()));
            return true;
        }

        String[] subArgs = new String[]{};
        if (args.length > pathPartCount) {
            subArgs = Arrays.copyOfRange(args, pathPartCount, args.length);
        }
        if (!sub.execute(sender, subArgs)) {
            sender.sendMessage("Usage: " + sub.getUsage(label));
        }
        return true;
    }

}