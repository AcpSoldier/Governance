package vision.thomas.government;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Proposal {

    private Government plugin;

    private String command;

    private String croppedReason;

    private String fullReason;

    private Player proposer;

    private Player nominated;

    private Player competitor;

    private ArrayList<Player> votedYes;

    private ArrayList<Player> votedNo;

    public Proposal(Government plugin) {

        this.plugin = plugin;
        votedYes = new ArrayList<>();
        votedNo = new ArrayList<>();
    }

    public void cancelProposal(Player p) {

        //Since there is no vote, setting these to null SHOULD be harmless. If I get null pointers, then I know why. : )
        command = null;
        proposer = null;
        votedYes = null;
        votedNo = null;

        nominated = null;
        competitor = null;
    }

    public String getCommand() {

        return command;
    }

    public Player getProposer() {

        return proposer;
    }

    public ArrayList<Player> getVotedYes() {

        return votedYes;
    }

    public ArrayList<Player> getVotedNo() {

        return votedNo;
    }

    public void setCommand(String command) {

        this.command = command;
    }

    public void setProposer(Player proposer) {

        this.proposer = proposer;
    }

    public String getReason() {

        return croppedReason;
    }

    public void setReason(String croppedReason) {

        this.croppedReason = croppedReason;
    }

    public String getFullReason() {

        return fullReason;
    }

    public void setFullReason(String fullReason) {

        this.fullReason = fullReason;
    }

    public Player getNominated() {

        return nominated;
    }

    public void setNominated(Player nominated) {

        this.nominated = nominated;
    }

    public Player getCompetitor() {

        return competitor;
    }

    public void setCompetitor(Player competitor) {

        this.competitor = competitor;
    }

}
