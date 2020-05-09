package be.renaud11232.plugins.sqleconomy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.Collections;
import java.util.List;

public class SQLEconomy implements Economy {

    private final SQLEconomyPlugin plugin;

    public SQLEconomy(SQLEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return "SQLEconomy";
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double v) {
        //TODO
        return "";
    }

    @Override
    public String currencyNameSingular() {
        //TODO
        return "sing";
    }

    @Override
    public String currencyNamePlural() {
        //TODO
        return "plur";
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        //TODO
        return true;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        //TODO
        return 20;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        //TODO
        return true;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        //TODO
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        //TODO
        return null;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        //TODO
        return true;
    }

    // Other methods of the interface that use the previous methods

    @Override
    @Deprecated
    public boolean hasAccount(String playerName) {
        return hasAccount(plugin.getServer().getOfflinePlayer(playerName));
    }

    @Override
    @Deprecated
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(plugin.getServer().getOfflinePlayer(playerName), worldName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    @Deprecated
    public double getBalance(String playerName) {
        return getBalance(plugin.getServer().getOfflinePlayer(playerName));
    }

    @Override
    @Deprecated
    public double getBalance(String playerName, String worldName) {
        return getBalance(plugin.getServer().getOfflinePlayer(playerName), worldName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String worldName) {
        return getBalance(player);
    }

    @Override
    @Deprecated
    public boolean has(String playerName, double amount) {
        return has(plugin.getServer().getOfflinePlayer(playerName), amount);
    }

    @Override
    @Deprecated
    public boolean has(String playerName, String worldName, double amount) {
        return has(plugin.getServer().getOfflinePlayer(playerName), worldName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(plugin.getServer().getOfflinePlayer(playerName), amount);
    }

    @Override
    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(plugin.getServer().getOfflinePlayer(playerName), worldName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    @Deprecated
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(plugin.getServer().getOfflinePlayer(playerName), amount);
    }

    @Override
    @Deprecated
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(plugin.getServer().getOfflinePlayer(playerName), worldName, amount);
    }

    @Override
    @Deprecated
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    @Deprecated
    public boolean createPlayerAccount(String playerName) {
        return createPlayerAccount(plugin.getServer().getOfflinePlayer(playerName));
    }

    @Override
    @Deprecated
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(plugin.getServer().getOfflinePlayer(playerName), worldName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }

    //Banks

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    @Deprecated
    public EconomyResponse createBank(String name, String playerName) {
        return createBank(name, plugin.getServer().getOfflinePlayer(playerName));
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts");
    }

    @Override
    @Deprecated
    public EconomyResponse isBankOwner(String name, String playerName) {
        return isBankOwner(name, plugin.getServer().getOfflinePlayer(playerName));
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts");
    }

    @Override
    @Deprecated
    public EconomyResponse isBankMember(String name, String playerName) {
        return isBankMember(name, plugin.getServer().getOfflinePlayer(playerName));
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "SQLEconomy does not support bank accounts");
    }

    @Override
    public List<String> getBanks() {
        return Collections.emptyList();
    }
}
