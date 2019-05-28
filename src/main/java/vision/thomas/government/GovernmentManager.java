package vision.thomas.government;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class GovernmentManager {

    private Government plugin;

    private Config config;

    public GovernmentManager(Government plugin) {

        this.plugin = plugin;
        this.config = new Config(plugin);
    }

    public String setGovType(int newType) {

        if (newType != config.getGovType()) {

            config.setGovType(newType);

            return plugin.prefix + "Government type changed to " + getGovName() + ".";
        }
        else {
            return plugin.prefix + "The government is already a " + getGovName() + ".";
        }
    }

    public void addGovLeader(String newLeader) {

        List<String> newLeaders = config.getGovLeaders();
        newLeaders.add(newLeader);

        config.setGovLeaders(newLeaders);
    }

    public void addGovLeader(Player newLeader) {

        List<String> newLeaders = config.getGovLeaders();
        newLeaders.add(newLeader.getUniqueId().toString());

        config.setGovLeaders(newLeaders);

        newLeader.sendMessage(plugin.prefix + "You are now a " + getTypeOfGovLeader() + ".");
    }

    public void removeGovLeader(String oldLeader) {

        List<String> newLeaders = config.getGovLeaders();
        newLeaders.remove(oldLeader);

        config.setGovLeaders(newLeaders);
    }

    public void removeGovLeader(Player oldLeader) {

        List<String> newLeaders = config.getGovLeaders();
        newLeaders.remove(oldLeader.getUniqueId().toString());

        config.setGovLeaders(newLeaders);

        oldLeader.sendMessage(plugin.prefix + "You are no longer a " + getTypeOfGovLeader() + ".");
    }

    public boolean govLeadersContains(String leaderDisplayName) {

        if (config.getGovLeaders().contains(getGovLeaderId(leaderDisplayName))) {
            return true;
        }
        else {
            return false;
        }
    }

    public void addAllowedCommand(String command) {

        List<String> newAllowedCommands = config.getAllowedCommands();
        newAllowedCommands.add(command);

        config.setAllowedCommands(newAllowedCommands);
    }

    public void removeAllowedCommand(String command) {

        List<String> newAllowedCommands = config.getAllowedCommands();
        newAllowedCommands.remove(command);

        config.setAllowedCommands(newAllowedCommands);
    }

    public String getGovLeaderId(String displayName) {

        return Bukkit.getOfflinePlayer(displayName).getUniqueId().toString();
    }

    public String getTypeOfGovLeader() {

        switch (config.getGovType()) {
            case 1:
                return "Representative";
            case 2:
                return "Dictator";
            default:
                return "There are no leaders in a Direct Democracy. Gov type is: '" + config.getGovType() + "'.";
        }
    }

    public String getGovName() {

        switch (config.getGovType()) {
            case 1:
                return "Republic";
            case 2:
                return "Dictatorship";
            default:
                return "Direct Democracy";
        }
    }

}
