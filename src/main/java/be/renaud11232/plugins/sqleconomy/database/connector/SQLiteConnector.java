package be.renaud11232.plugins.sqleconomy.database.connector;

import be.renaud11232.plugins.sqleconomy.database.exceptions.DatabaseException;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnector implements DatabaseConnector {
    @Override
    public Connection getConnection(FileConfiguration configuration) throws DatabaseException {
        try {
            Class.forName("jdbc:sqlite:sample.db");
            String database = configuration.getString("database_connection.database");
            return DriverManager.getConnection(String.format("jdbc:sqlite:%s", database));
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseException("Unable to create a database connection : " + e.getMessage(), e);
        }
    }
}
