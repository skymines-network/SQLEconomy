package be.renaud11232.plugins.sqleconomy;

import be.renaud11232.plugins.sqleconomy.command.ReloadCommandExecutor;
import be.renaud11232.plugins.sqleconomy.command.ReloadTabCompleter;
import be.renaud11232.plugins.sqleconomy.database.exceptions.DatabaseException;
import be.renaud11232.plugins.sqleconomy.database.EconomyDatabase;
import be.renaud11232.plugins.sqleconomy.database.connector.DatabaseType;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLEconomyPlugin extends JavaPlugin {

    private Connection databaseConnection;
    private EconomyDatabase economyDatabase;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("sqleconomy").setExecutor(new ReloadCommandExecutor(this));
        getCommand("sqleconomy").setTabCompleter(new ReloadTabCompleter());
        economyDatabase = new EconomyDatabase(this);
        SQLEconomy economy = new SQLEconomy(this);
        Bukkit.getServicesManager().register(Economy.class, economy, this, ServicePriority.High);
        try {
            getDatabaseConnection();
        } catch (DatabaseException e) {
            getLogger().severe("Connection failed : " + e.getMessage());
            getLogger().severe("If you haven't configured the plugin yet, this is normal. Configure it and run /sqleconomy reload");
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getServicesManager().unregisterAll(this);
        try {
            if(databaseConnection != null && !databaseConnection.isClosed()) {
                databaseConnection.close();
                databaseConnection = null;
            }
        } catch (SQLException e) {
            getLogger().severe("Unable to properly close database connection : " + e.getMessage());
        }
    }

    public EconomyDatabase getDatabase() {
        return economyDatabase;
    }

    public void closeDatabaseConnection() throws DatabaseException {
        try {
            if (databaseConnection != null && !databaseConnection.isClosed()) {
                databaseConnection.close();
                databaseConnection = null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Unable to close database connection.", e);
        }
    }

    public Connection getDatabaseConnection() throws DatabaseException {
        try {
            if (databaseConnection == null || databaseConnection.isClosed()) {
                databaseConnection = DatabaseType.valueOf(getConfig().getString("database_connection.type", "MYSQL").toUpperCase()).getConnection(getConfig());
            }
            return databaseConnection;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to open database connection", e);
        }
    }

}
