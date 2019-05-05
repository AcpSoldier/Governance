package vision.thomas.government;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Announcement {

    private Government plugin;

    public Announcement(Government plugin) {

        this.plugin = plugin;
    }

    public void announceProposal(Player proposer, String proposal, String appeal) {
        proposal = "[Logic for getting the proposal]";

        String announcement = plugin.prefix + proposer.getName() + "[getRespectLevel logic] " + " has proposed to " + proposal + "!";

        TextComponent voteYesMessage = new TextComponent("CLICK TO VOTE YES");
        voteYesMessage.setColor(ChatColor.GREEN);
        voteYesMessage.setBold(true);
        voteYesMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Vote for the proposal" + proposal + ".").create()));
        voteYesMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/government vote yes"));

        TextComponent voteNoMessage = new TextComponent("CLICK TO VOTE NO");
        voteNoMessage.setColor(ChatColor.RED);
        voteNoMessage.setBold(true);
        voteNoMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Vote against the proposal" + proposal + ".").create()));
        voteNoMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/government vote no"));

        Bukkit.broadcastMessage(announcement);
        Bukkit.broadcastMessage(plugin.prefix + proposer.getName() + "'s appeal: " + appeal);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(voteYesMessage);
            p.spigot().sendMessage(voteNoMessage);
        }
    }

    public void annouceElection(Player runner, boolean competition) {

        String positionType = "(Logic for what type of government Position)";
        String announcement = plugin.prefix + runner.getName() + "[getRespectLevel logic] " + " is running to be a " + positionType + "!";

        TextComponent voteYesMessage = new TextComponent("CLICK TO VOTE YES");
        voteYesMessage.setColor(ChatColor.GREEN);
        voteYesMessage.setBold(true);
        voteYesMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Vote to make " + runner.getName() + " the new " + positionType + ".").create()));
        voteYesMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/government vote yes"));

        TextComponent voteNoMessage = new TextComponent("CLICK TO VOTE NO");
        voteNoMessage.setColor(ChatColor.RED);
        voteNoMessage.setBold(true);
        voteNoMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Vote to make " + runner.getName()).create()));
        voteNoMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/government vote no"));

        if (competition) {

            String officeHolder = "[Logic for getting whoever is currently in office]";
            voteNoMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Vote to keep " + officeHolder + " as the " + positionType + ".").create()));
            announcement = plugin.prefix + runner.getName() + "[getRespectLevel logic] " + " is running to replace " + officeHolder + " as a " + positionType + "!";
        }

        Bukkit.broadcastMessage(announcement);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.spigot().sendMessage(voteYesMessage);
            p.spigot().sendMessage(voteNoMessage);
        }
    }

    public void announceAppeal(Player p, String appeal) {

        Bukkit.broadcastMessage(plugin.prefix + p.getName() + " (Logic for getting the player's government position)" + "'s public address: " + ChatColor.BLUE + appeal);
    }

}
