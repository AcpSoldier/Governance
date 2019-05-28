package vision.thomas.government;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import vision.thomas.government.accounts.Account;
import vision.thomas.government.accounts.AccountManager;
import vision.thomas.government.commands.ConfigCommand;
import vision.thomas.government.commands.NominateCommand;
import vision.thomas.government.commands.ProposalCommand;
import vision.thomas.government.commands.VoteCommand;
import vision.thomas.government.commands.*;
import vision.thomas.government.commands.helpers.CommandExec;
import vision.thomas.government.database.SQLiteDatabase;

import java.io.File;
import java.sql.SQLException;

public final class Government extends JavaPlugin {

    public String prefix = ChatColor.GOLD + "" + ChatColor.BOLD + "Governance: " + ChatColor.YELLOW;

    public ChatColor defaultColor = ChatColor.YELLOW;

    public ChatColor mainColor = ChatColor.GOLD;

    public String highlightColor = ChatColor.AQUA + "" + ChatColor.BOLD;

    public String fullLine = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.STRIKETHROUGH + "---------------------------------------------";

    public Config config;

    public Announcement announcement = new Announcement(this);

    private SQLiteDatabase database;

    private AccountManager accountManager;

    @Override
    public void onEnable() {

        config = new Config(this);
        config.setup();

        database = new SQLiteDatabase(getDataFolder() + File.separator + "database.sql");
        accountManager = new AccountManager(this);

        ProposalCommand proposalCommand = new ProposalCommand(this);
        VoteManager voteManager = new VoteManager(this);

        CommandExec cmdEx = new CommandExec(this);
        cmdEx.register(new ConfigCommand(this));
        cmdEx.register(new VoteCommand(this));
        cmdEx.register(new NominateCommand(this));
        cmdEx.register(new LeadersCommand(this));
        cmdEx.register(new ResignCommand(this));
        cmdEx.register(proposalCommand);

        getServer().getPluginManager().registerEvents(proposalCommand, this);
        getServer().getPluginManager().registerEvents(voteManager, this);

        // If the server was reloaded, add all of the online players back into the 'accounts' HashMap.
        if (Bukkit.getOnlinePlayers().size() > 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                accountManager.getRepository().selectAccount(player.getUniqueId()).thenAccept(account -> {
                    if (account == null) {
                        int id = accountManager.getRepository().insertAccount(player.getUniqueId()).join();
                        account = new Account(id, player.getUniqueId(), 0);
                    }
                    accountManager.accounts.put(player.getUniqueId(), account);
                }).join();
            }
        }
    }

    @Override
    public void onDisable() {

        try {
            if (database.getConnection() != null && !database.getConnection().isClosed()) {
                try {
                    Bukkit.getLogger().info("[Governance] Closing player database connection...");
                    database.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SQLiteDatabase getDatabase() {

        return database;
    }

    public AccountManager getAccountManager() {

        return accountManager;
    }

}