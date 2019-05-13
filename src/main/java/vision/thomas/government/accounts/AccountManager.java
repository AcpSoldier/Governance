package vision.thomas.government.accounts;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import vision.thomas.government.Government;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class AccountManager implements Listener {

    private final Government plugin;

    private final AccountRepository repository;

    private final Map<UUID, Account> accounts = new HashMap<>();

    private AtomicBoolean joinable = new AtomicBoolean();

    public AccountManager(Government plugin) {

        this.plugin = plugin;

        repository = new AccountRepository(this);
        repository.createTable().thenAccept(aVoid -> joinable.set(true));

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {

        Player player = event.getPlayer();

        if (!joinable.get()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Please try logging in again. : - ) ");
            return;
        }

        repository.selectAccount(player.getUniqueId()).thenAccept(account -> {
            if (account == null) {
                int id = repository.insertAccount(player.getUniqueId()).join();
                account = new Account(id, player.getUniqueId(), 0);
            }
            accounts.put(player.getUniqueId(), account);
        });
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        accounts.remove(player.getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Account account = getAccount(player.getUniqueId());

        incrementRespect(account, 1);
        player.sendMessage(plugin.prefix + "New respect: " + account.getRespect());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        Account account = getAccount(player.getUniqueId());

        incrementRespect(account, -1);
        player.sendMessage(plugin.prefix + "New respect: " + account.getRespect());
    }

    public void incrementRespect(Account account, int amount) {

        account.incrementRespect(amount);
        repository.updateRespect(account.getUniqueId(), amount);
    }

    public Account getAccount(UUID uuid) {

        return accounts.get(uuid);
    }

    public Government getPlugin() {

        return plugin;
    }

    public AccountRepository getRepository() {

        return repository;
    }

}
