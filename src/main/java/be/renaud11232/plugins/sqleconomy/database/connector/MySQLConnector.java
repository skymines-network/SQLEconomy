package be.renaud11232.plugins.sqleconomy.database.connector;

import be.renaud11232.plugins.sqleconomy.database.exceptions.DatabaseException;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector implements DatabaseConnector {

    @Override
    public Connection getConnection(FileConfiguration configuration) throws DatabaseException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String host = configuration.getString("database_connection.host", "localhost");
            int port = configuration.getInt("database_connection.port", 3306);
            String database = configuration.getString("database_connection.database");
            String user = configuration.getString("database_connection.user");
            String password = configuration.getString("database_connection.password", "");
            return DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s?useSSL=false", host, port, database),user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseException("Unable to create a database connection : " + e.getMessage(), e);
        }
    }

}
