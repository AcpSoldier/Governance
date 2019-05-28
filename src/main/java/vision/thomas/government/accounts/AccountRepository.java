package vision.thomas.government.accounts;

import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class AccountRepository {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS accounts(id INTEGER PRIMARY KEY AUTOINCREMENT, uuid VARCHAR(36) NOT NULL, respect INTEGER NOT NULL); CREATE UNIQUE INDEX uuidIndex ON accounts(uuid);";

    private static final String INSERT_ACCOUNT = "INSERT INTO accounts (uuid, respect) VALUES (?, ?);";

    private static final String SELECT_ACCOUNT = "SELECT id, respect FROM accounts WHERE uuid = ?;";

    private static final String UPDATE_RESPECT = "UPDATE accounts SET respect = respect + ? WHERE uuid = ?;";

    private final AccountManager manager;

    public AccountRepository(AccountManager manager) {

        this.manager = manager;
    }

    protected CompletableFuture<Void> createTable() {

        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement statement = manager.getPlugin().getDatabase().getConnection().prepareStatement(CREATE_TABLE)) {

                statement.executeUpdate();
            } catch (SQLException exception) {
                throw new CompletionException(exception);
            }
        });
    }

    public CompletableFuture<Integer> insertAccount(UUID uuid) {

        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = manager.getPlugin().getDatabase().getConnection().prepareStatement(INSERT_ACCOUNT, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, uuid.toString());
                statement.setInt(2, 0);
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                    else {
                        Bukkit.getLogger().warning("[Governance]: Error while inserting account. This should not happen.");
                        return null;
                    }
                }
            } catch (SQLException exception) {
                throw new CompletionException(exception);
            }
        });
    }

    public CompletableFuture<Account> selectAccount(UUID uuid) {

        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = manager.getPlugin().getDatabase().getConnection().prepareStatement(SELECT_ACCOUNT)) {

                statement.setString(1, uuid.toString());

                try (ResultSet rs = statement.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    int id = rs.getInt("id");
                    int respect = rs.getInt("respect");

                    return new Account(id, uuid, respect);
                }
            } catch (SQLException exception) {
                throw new CompletionException(exception);
            }
        });
    }

    protected CompletableFuture<Void> updateRespect(UUID uuid, int respect) {

        return CompletableFuture.runAsync(() -> {
            try (PreparedStatement statement = manager.getPlugin().getDatabase().getConnection().prepareStatement(UPDATE_RESPECT)) {

                statement.setInt(1, respect);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();

            } catch (SQLException exception) {
                throw new CompletionException(exception);
            }
        });
    }

}
