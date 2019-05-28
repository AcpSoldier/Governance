package vision.thomas.government;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public FileConfiguration cfg;

    private Government plugin;

    private static boolean isPluginEnabled;

    private static boolean isVoteSoundsForAll;

    private static boolean isVoteSoundsForSelf;

    private static int voteTimeInSeconds;

    private static int minVotesRequired;

    private static int percentNeededToPass;

    private static boolean chatWhileCountingVotes;

    private static List<Integer> announceVoteAt = new ArrayList<>();

    private static int govType;

    private static int maxLeaders;

    private static List<String> govLeaders = new ArrayList<>();

    private static int minTermLengthInMinutes;

    private static int maxTermLengthInMinutes;

    private static List<String> allowedCommands = new ArrayList<>();

    public Config(Government plugin) {

        this.plugin = plugin;
    }

    public void reloadConfig() {

        plugin.reloadConfig();
        cfg = plugin.getConfig(); // 100% needed
        announceVoteAt = cfg.getIntegerList("Settings.AnnounceVoteAt");
        isPluginEnabled = cfg.getBoolean("Settings.Enabled");
        isVoteSoundsForAll = cfg.getBoolean("Settings.VoteSoundsForAll");
        isVoteSoundsForSelf = cfg.getBoolean("Settings.VoteSoundsForSelf");
        voteTimeInSeconds = cfg.getInt("Settings.VoteTimeInSeconds");
        minVotesRequired = cfg.getInt("Settings.MinVotesRequired");
        percentNeededToPass = cfg.getInt("Settings.PercentRequiredToPass");
        chatWhileCountingVotes = cfg.getBoolean("Settings.ChatWhileCountingVotes");

        govType = cfg.getInt("Government.Type");
        maxLeaders = cfg.getInt("Government.MaxLeaders");
        govLeaders = cfg.getStringList("Government.Leaders");
        minTermLengthInMinutes = cfg.getInt("Government.MinTermLengthInMinutes");
        maxTermLengthInMinutes = cfg.getInt("Government.MaxTermLengthInMinutes");

        allowedCommands = cfg.getStringList("Commands.Allowed");

        plugin.saveConfig();
    }

    public void setPluginEnabled(boolean pluginEnabled) {

        isPluginEnabled = pluginEnabled;
        cfg = plugin.getConfig();
        cfg.set("Settings.Enabled", isPluginEnabled);
        plugin.saveConfig();
    }

    public void setup() {

        plugin.saveDefaultConfig();
        cfg = plugin.getConfig();
        reloadConfig();
    }

    public String getElegantTermLength(int timeInMinutes) {

        int days = timeInMinutes / 24 / 60;
        int hours = timeInMinutes / 60 % 24;
        int minutes = timeInMinutes % 60;

        String minuteString = minutes != 1 ? " minutes" : " minute";
        String hourString = hours != 1 ? " hours" : " hour";
        String dayString = days != 1 ? " days" : " day";

        if (days > 0) {
            if (hours > 0) {
                if (minutes > 0) {
                    return "" + days + dayString + ", " + hours + hourString + ", and " + minutes + minuteString;
                }
                else {
                    return "" + days + dayString + " and " + hours + hourString;
                }
            }
            else if (minutes > 0) {
                return "" + days + dayString + " and " + minutes + minuteString;
            }
            else {
                return "" + days + dayString;
            }
        }
        else if (hours > 0) {
            if (minutes > 0) {
                return "" + hours + hourString + " and " + minutes + minuteString;
            }
            else {
                return "" + hours + hourString;
            }
        }
        else {
            return "" + minutes + minuteString;
        }
    }

    public boolean isPluginEnabled() {

        return isPluginEnabled;
    }

    public int getVoteTimeInSeconds() {

        return voteTimeInSeconds;
    }

    public int getMinVotesRequired() {

        return minVotesRequired;
    }

    public int getPercentNeededToPass() {

        return percentNeededToPass;
    }

    public List<Integer> getAnnounceVoteAt() {

        return announceVoteAt;
    }

    public int getGovType() {

        return govType;
    }

    public void setGovType(int newType) {

        this.govType = newType;
        cfg = plugin.getConfig();
        cfg.set("Government.Type", newType);
        plugin.saveConfig();
    }

    public int getMaxLeaders() {

        return maxLeaders;
    }

    public List<String> getGovLeaders() {

        return govLeaders;
    }

    public void setGovLeaders(List<String> newGovLeaders) {

        govLeaders = newGovLeaders;
        cfg = plugin.getConfig(); // 100% needed
        cfg.set("Government.Leaders", newGovLeaders);
        plugin.saveConfig();
    }

    public int getMinTermLengthInMinutes() {

        return minTermLengthInMinutes;
    }

    public int getMaxTermLengthInMinutes() {

        return maxTermLengthInMinutes;
    }

    public List<String> getAllowedCommands() {

        return allowedCommands;
    }

    public boolean isVoteSoundsForAll() {

        return isVoteSoundsForAll;
    }

    public boolean isVoteSoundsForSelf() {

        return isVoteSoundsForSelf;
    }

    public void setAllowedCommands(List<String> newAllowedCommands) {

        allowedCommands = newAllowedCommands;
        cfg = plugin.getConfig(); // 100% needed
        cfg.set("Commands.Allowed", newAllowedCommands);
        plugin.saveConfig();
    }

}