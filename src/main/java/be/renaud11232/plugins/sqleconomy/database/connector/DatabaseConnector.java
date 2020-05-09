package be.renaud11232.plugins.sqleconomy.database.connector;

import be.renaud11232.plugins.sqleconomy.database.exceptions.DatabaseException;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;

public interface DatabaseConnector {

    Connection getConnection(FileConfiguration configuration) throws DatabaseException;

}
