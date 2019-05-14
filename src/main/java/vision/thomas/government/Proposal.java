package vision.thomas.government;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Proposal {

    private Government plugin;

    public static String type;

    public static Player proposer;

    public static ArrayList<Player> votedYes;

    public static ArrayList<Player> votedNo;

    public Proposal(Government plugin, String voteType, Player proposer) {

        this.plugin = plugin;
        this.type = voteType;
        this.proposer = proposer;
        votedYes = new ArrayList<>();
        votedNo = new ArrayList<>();

    }

    public void cancelProposal(Player p) {

        if (this.proposer.equals(p)) {

            //Since there is no vote, setting these to null SHOULD be harmless. If I get null pointers, then I know why. : )
            type = null;
            proposer = null;
            votedYes = null;
            votedNo = null;

            p.sendMessage(plugin.prefix + "Your proposal has been cancelled.");
        }
        else {
            p.sendMessage(plugin.prefix + "Only " + ChatColor.AQUA + "" + this.proposer.getName() + " can cancel this proposal.");
        }
    }

}
