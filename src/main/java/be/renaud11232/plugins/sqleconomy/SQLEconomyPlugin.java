package be.renaud11232.plugins.sqleconomy;

import be.renaud11232.plugins.sqleconomy.database.DatabaseException;
import be.renaud11232.plugins.sqleconomy.database.connector.DatabaseType;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLEconomyPlugin extends JavaPlugin {

    private Connection databaseConnection;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        try {
            databaseConnection = DatabaseType.valueOf(getConfig().getString("database_connection.type", "database_type").toUpperCase()).getConnection(getConfig());
            SQLEconomy economy = new SQLEconomy(this);
            Bukkit.getServicesManager().register(Economy.class, economy, this, ServicePriority.High);
        } catch (DatabaseException e) {
            getLogger().severe("Connection failed : " + e.getMessage());
            getPluginLoader().disablePlugin(this);
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
}
