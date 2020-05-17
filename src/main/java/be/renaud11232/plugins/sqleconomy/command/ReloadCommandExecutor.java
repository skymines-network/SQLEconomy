package be.renaud11232.plugins.sqleconomy.command;

import be.renaud11232.plugins.sqleconomy.SQLEconomyPlugin;
import be.renaud11232.plugins.sqleconomy.database.exceptions.DatabaseException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommandExecutor implements CommandExecutor {

    private final SQLEconomyPlugin plugin;

    public ReloadCommandExecutor(SQLEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(args.length != 1 || !args[0].equals("reload")) {
            return false;
        }
        try {
            plugin.closeDatabaseConnection();
            commandSender.sendMessage(ChatColor.GREEN + "[1/3] Database connection successfully closed.");
            plugin.getLogger().info("[1/3] Database connection successfully closed.");
            plugin.reloadConfig();
            commandSender.sendMessage(ChatColor.GREEN + "[2/3] Configuration successfully reloaded.");
            plugin.getLogger().info("[2/3] Configuration successfully reloaded.");
            plugin.getDatabaseConnection();
            commandSender.sendMessage(ChatColor.GREEN + "[3/3] Database connection successfully established.");
            plugin.getLogger().info("[3/3] Database connection successfully established.");
        } catch (DatabaseException e) {
            commandSender.sendMessage(ChatColor.RED + "Reloading failed : " + e.getMessage());
            plugin.getLogger().severe("Reloading failed : " + e.getMessage());
        }
        return true;
    }

}
