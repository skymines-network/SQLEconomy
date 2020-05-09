package be.renaud11232.plugins.sqleconomy.database;

import be.renaud11232.plugins.sqleconomy.SQLEconomyPlugin;
import org.bukkit.OfflinePlayer;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EconomyDatabase {

    private final SQLEconomyPlugin plugin;

    public EconomyDatabase(SQLEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    private PreparedStatement prepare(String query, OfflinePlayer player) throws SQLException {
        boolean useUUID;
        String customQuery;
        if(query.contains("{{player_name}}")) {
            useUUID = false;
            customQuery = query.replace("{{player_name}}", "?");
        } else {
            useUUID =  true;
            customQuery = query.replace("{{player_uuid}}", "?");
        }
        PreparedStatement statement = plugin.getDatabaseConnection().prepareStatement(customQuery);
        if(useUUID) {
            statement.setString(0, player.getUniqueId().toString());
        } else {
            statement.setString(0, player.getName());
        }
        return statement;
    }
}
