package be.renaud11232.plugins.sqleconomy.database;

import be.renaud11232.plugins.sqleconomy.SQLEconomyPlugin;
import be.renaud11232.plugins.sqleconomy.database.exceptions.DatabaseException;
import be.renaud11232.plugins.sqleconomy.database.exceptions.PlayerNotFoundException;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EconomyDatabase {

    private final SQLEconomyPlugin plugin;

    public EconomyDatabase(SQLEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    public void beginTransaction() throws DatabaseException {
        try {
            plugin.getDatabaseConnection().setAutoCommit(false);
        } catch(SQLException e) {
            throw new DatabaseException("Could not begin a transaction : " + e.getMessage(), e);
        }
    }

    public void commitTransaction() throws DatabaseException {
        try {
            plugin.getDatabaseConnection().commit();
            plugin.getDatabaseConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throw new DatabaseException("Could not commit a transaction : " + e.getMessage(), e);
        }
    }

    public void rollbackTransaction() throws DatabaseException {
        try {
            plugin.getDatabaseConnection().rollback();
            plugin.getDatabaseConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throw new DatabaseException("Could not rollback a transaction : " + e.getMessage(), e);
        }
    }

    public boolean hasAccount(OfflinePlayer player) throws DatabaseException {
        try {
            getPlayerBalance(player);
            return true;
        } catch (PlayerNotFoundException e) {
            return false;
        }
    }


    public BigDecimal getPlayerBalance(OfflinePlayer player) throws DatabaseException, PlayerNotFoundException {
        QueryPreparer preparer = new QueryPreparer();
        preparer.setString("player_name", player.getName());
        preparer.setString("player_uuid", player.getUniqueId().toString());
        try (
                PreparedStatement statement = preparer.prepare(plugin.getDatabaseConnection(), plugin.getConfig().getString("queries.get_balance"));
                ResultSet resultSet = statement.executeQuery()
        ) {
            if(resultSet.next()) {
                return resultSet.getBigDecimal(1);
            }
            throw new PlayerNotFoundException("Unable to get the balance for the player " + player.getName() + ". The database returned an empty set.");
        } catch (SQLException e) {
            throw new DatabaseException("Unable to get the balance for the player : " + e.getMessage(), e);
        }
    }

    public void createPlayerAccount(OfflinePlayer player) throws DatabaseException {
        QueryPreparer preparer = new QueryPreparer();
        preparer.setString("player_name", player.getName());
        preparer.setString("player_uuid", player.getUniqueId().toString());
        List<String> queries = plugin.getConfig().getStringList("queries.create_account");
        for(String query : queries) {
            try (
                    PreparedStatement statement = preparer.prepare(plugin.getDatabaseConnection(), query)
            ){
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseException("Unable to create an account for the player : " + e.getMessage(), e);
            }
        }
    }

}
