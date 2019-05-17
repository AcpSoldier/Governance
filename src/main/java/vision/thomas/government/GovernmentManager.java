package vision.thomas.government;

import org.bukkit.Bukkit;

public class GovernmentManager {

    private Government plugin;

    private Config config;

    private Announcement announcement;

    public GovernmentManager(Government plugin) {

        this.plugin = plugin;
        this.config = new Config(plugin);
    }

    public String setGovType(int newType) {

        config.reloadConfig();

        if (newType != getGovType()) {

            config.govType = newType;
            config.cfg.set("Government.Type", newType);
            plugin.saveConfig();

            return plugin.prefix + "Government type changed to " + getGovName() + ".";
        }
        else {
            return plugin.prefix + "The government is already a " + getGovName() + ".";
        }
    }

    public int getGovType() {

        return config.govType;
    }

    public String addGovLeader(String newLeader) {

        config.reloadConfig();

        if (!config.govLeaders.contains(getGovLeaderId(newLeader))) {

            config.govLeaders.add(getGovLeaderId(newLeader));
            config.cfg.set("Government.Leaders", config.govLeaders);
            plugin.saveConfig();

            return plugin.prefix + newLeader + " is now a " + getTypeOfGovLeader() + ".";
        }
        else {
            return plugin.prefix + newLeader + " is already " + getTypeOfGovLeader() + ".";
        }
    }

    public String removeGovLeader(String oldLeader) {

        config.reloadConfig();

        if (config.govLeaders.contains(getGovLeaderId(oldLeader))) {

            config.govLeaders.remove(getGovLeaderId(oldLeader));
            config.cfg.set("Government.Leaders", config.govLeaders);
            plugin.saveConfig();

            return plugin.prefix + oldLeader + " is no longer a " + getTypeOfGovLeader() + ".";
        }
        else {
            return plugin.prefix + oldLeader + " is not a " + getTypeOfGovLeader() + " and can't be removed.";
        }
    }

    public String addAllowedCommand(String command) {

        config.reloadConfig();

        if(!config.allowedCommands.contains(command.toLowerCase())) {

            config.allowedCommands.add(command.toLowerCase());
            config.cfg.set("Commands.Allowed", config.allowedCommands);
            plugin.saveConfig();

            return plugin.prefix + "Proposals can now be created to run " + plugin.mainColor + "/" + command + ".";
        }
        else {
            return plugin.prefix + "/" + command + " is already an allowed command.";
        }
    }

    public String removeAllowedCommand(String command) {

        config.reloadConfig();

        if(config.allowedCommands.contains(command.toLowerCase())) {

            config.allowedCommands.remove(command.toLowerCase());
            config.cfg.set("Commands.Allowed", config.allowedCommands);
            plugin.saveConfig();

            return plugin.prefix + "Proposals can no longer be made for " + plugin.mainColor + "/" + command + ".";
        }
        else {
            return plugin.prefix + plugin.mainColor + "/" + command + plugin.defaultColor + " is not an allowed command and therefore can't be removed.";
        }
    }

    public String getGovLeaderId(String playerName) {

        return Bukkit.getOfflinePlayer(playerName).getUniqueId().toString();
    }

    public String getTypeOfGovLeader() {

        config.reloadConfig();

        switch (config.govType) {
            case 1:
                return "Representative";
            case 2:
                return "Dictator";
            default:
                return "There are no leaders in a Direct Democracy. Gov type is: '" + config.govType + "'.";
        }
    }

    public String getGovName() {

        config.reloadConfig();

        switch (config.govType) {
            case 1:
                return "Republic";
            case 2:
                return "Dictatorship";
            default:
                return "Direct Democracy";
        }
    }

}
