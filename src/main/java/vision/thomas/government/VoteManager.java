package vision.thomas.government;

import org.bukkit.entity.Player;

public class VoteManager {

    private Government plugin;

    private static Proposal currentProposal;

    public static boolean voteInProgress = false;

    public VoteManager(Government plugin) {

        this.plugin = plugin;
    }

    public void castVote(Player voter, Proposal proposal, String vote) {

        if (vote.equalsIgnoreCase("yes")) {

            proposal.votedYes.add(voter);
        }
        else {
            proposal.votedNo.add(voter);
        }
    }

    public Proposal getCurrentProposal() {

        return currentProposal;
    }

    public void setCurrentProposal(Proposal proposal) {

        currentProposal = proposal;
    }

}
