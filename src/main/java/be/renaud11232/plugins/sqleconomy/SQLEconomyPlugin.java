package be.renaud11232.plugins.sqleconomy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class SQLEconomyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        SQLEconomy economy = new SQLEconomy();
        Bukkit.getServicesManager().register(Economy.class, economy, this, ServicePriority.High);
    }

    @Override
    public void onDisable() {
        Bukkit.getServicesManager().unregisterAll(this);
    }
}
