package vision.thomas.government;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import vision.thomas.government.accounts.AccountManager;
import vision.thomas.government.commands.ConfigCommand;
import vision.thomas.government.commands.ProposalCommand;
import vision.thomas.government.commands.VoteCommand;
import vision.thomas.government.commands.helpers.CommandExec;
import vision.thomas.government.database.SQLiteDatabase;

import java.io.File;

public final class Government extends JavaPlugin {

    public String prefix = ChatColor.GOLD + "" + ChatColor.BOLD + "Government: " + ChatColor.YELLOW;

    public ChatColor defaultColor = ChatColor.YELLOW;

    public Config config;

    private SQLiteDatabase database;



    private AccountManager accountManager;

    @Override
    public void onEnable() {

        config = new Config(this);
        config.setup();

        database = new SQLiteDatabase(getDataFolder() + File.separator + "database.sql");
        accountManager = new AccountManager(this);

        CommandExec cmdEx = new CommandExec(this);
        cmdEx.register(new ConfigCommand(this));
        cmdEx.register(new VoteCommand(this));
        cmdEx.register(new ProposalCommand(this));
    }

    @Override
    public void onDisable() {
        // Shutdown logic
    }

    public SQLiteDatabase getDatabase() {

        return database;
    }

    public AccountManager getAccountManager() {

        return accountManager;
    }
}