package be.renaud11232.plugins.sqleconomy;

import be.renaud11232.plugins.sqleconomy.database.exceptions.DatabaseException;
import be.renaud11232.plugins.sqleconomy.database.exceptions.PlayerNotFoundException;
import be.renaud11232.plugins.sqleconomy.discord.SQLEconomyWebhook;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
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
    public String format(double amount) {
        return String.format("%f %s", amount, plugin.getConfig().getString("currency.symbol"));
    }

    @Override
    public String currencyNameSingular() {
        return plugin.getConfig().getString("currency.name_singular");
    }

    @Override
    public String currencyNamePlural() {
        return plugin.getConfig().getString("currency.name_plural");
    }

    private boolean doHasAccount(OfflinePlayer player) throws DatabaseException {
        return plugin.getDatabase().hasAccount(player);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        synchronized (plugin.getDatabase()) {
            boolean hasAccount;
            try {
                plugin.getDatabase().beginTransaction();
                hasAccount = doHasAccount(player);
                plugin.getDatabase().commitTransaction();
            } catch (DatabaseException e) {
                plugin.getLogger().warning("Transaction failed : " + e.getMessage());
                hasAccount = false;
                try {
                    plugin.getDatabase().rollbackTransaction();
                } catch (DatabaseException rollbackException) {
                    plugin.getLogger().severe("Unable to rollback transaction : " + rollbackException.getMessage());
                }
            }
            return hasAccount;
        }
    }

    private boolean doCreatePlayerAccount(OfflinePlayer player) throws DatabaseException {
        boolean created;
        if(plugin.getDatabase().hasAccount(player)) {
            created = false;
        } else {
            plugin.getDatabase().createPlayerAccount(player);
            created = true;
        }
        return created;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        synchronized (plugin.getDatabase()) {
            boolean created;
            try {
                plugin.getDatabase().beginTransaction();
                created = doCreatePlayerAccount(player);
                plugin.getDatabase().commitTransaction();
            } catch (DatabaseException e) {
                plugin.getLogger().warning("Transaction failed : " + e.getMessage());
                created = false;
                try {
                    plugin.getDatabase().rollbackTransaction();
                } catch (DatabaseException rollbackException) {
                    plugin.getLogger().severe("Unable to rollback transaction : " + rollbackException.getMessage());
                }
            }
            return created;
        }
    }

    private double doGetBalance(OfflinePlayer player) throws DatabaseException {
        double balance;
        try {
            balance = plugin.getDatabase().getPlayerBalance(player);
        } catch (PlayerNotFoundException e) {
            balance = 0;
        }
        return balance;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        synchronized (plugin.getDatabase()) {
            double balance;
            try {
                plugin.getDatabase().beginTransaction();
                balance = doGetBalance(player);
                plugin.getDatabase().commitTransaction();
            } catch (DatabaseException e) {
                plugin.getLogger().warning("Transaction failed : " + e.getMessage());
                balance = 0;
                try {
                    plugin.getDatabase().rollbackTransaction();
                } catch (DatabaseException rollbackException) {
                    plugin.getLogger().severe("Unable to rollback transaction : " + rollbackException.getMessage());
                }
            }
            return balance;
        }
    }

    private boolean doHas(OfflinePlayer player, double amount) throws DatabaseException {
        return doGetBalance(player) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        synchronized (plugin.getDatabase()) {
            boolean has;
            try {
                plugin.getDatabase().beginTransaction();
                has = doHas(player, amount);
                plugin.getDatabase().commitTransaction();
            } catch (DatabaseException e) {
                plugin.getLogger().warning("Transaction failed : " + e.getMessage());
                has = false;
                try{
                    plugin.getDatabase().rollbackTransaction();
                } catch (DatabaseException rollbackException) {
                    plugin.getLogger().severe("Unable to rollback transaction : " + rollbackException.getMessage());
                }
            }
            return has;
        }
    }

    private EconomyResponse doWithdrawPlayer(OfflinePlayer player, double amount) throws DatabaseException {
        if(amount < 0) {
            EconomyResponse response = doDepositPlayer(player, -amount);
            return new EconomyResponse(-response.amount, response.balance, response.type, response.errorMessage);
        }
        try {
            BigDecimal balance = BigDecimal.valueOf(plugin.getDatabase().getPlayerBalance(player));
            BigDecimal newBalance = balance.subtract(BigDecimal.valueOf(amount));
            plugin.getDatabase().withdrawPlayer(player, amount, newBalance.doubleValue());
            SQLEconomyWebhook webhook = new SQLEconomyWebhook();
            webhook.create(player, "-" + amount, newBalance.doubleValue());
            return new EconomyResponse(amount, newBalance.doubleValue(), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (PlayerNotFoundException e) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "The given user does not exists");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        synchronized (plugin.getDatabase()) {
            EconomyResponse response;
            try {
                plugin.getDatabase().beginTransaction();
                response = doWithdrawPlayer(player, amount);
                plugin.getDatabase().commitTransaction();
            } catch (DatabaseException e) {
                plugin.getLogger().warning("Transaction failed : " + e.getMessage());
                response = new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "There was an error with the database : " + e.getMessage());
                try{
                    plugin.getDatabase().rollbackTransaction();
                } catch (DatabaseException rollbackException) {
                    plugin.getLogger().severe("Unable to rollback transaction : " + rollbackException.getMessage());
                }
            }
            return response;
        }
    }

    private EconomyResponse doDepositPlayer(OfflinePlayer player, double amount) throws DatabaseException {
        if(amount < 0) {
            EconomyResponse response = doWithdrawPlayer(player, -amount);
            return new EconomyResponse(-response.amount, response.balance, response.type, response.errorMessage);
        }
        try {
            BigDecimal balance = BigDecimal.valueOf(plugin.getDatabase().getPlayerBalance(player));
            BigDecimal newBalance = balance.add(BigDecimal.valueOf(amount));
            plugin.getDatabase().depositPlayer(player, amount, newBalance.doubleValue());
            SQLEconomyWebhook webhook = new SQLEconomyWebhook();
            webhook.create(player, "+" + amount, newBalance.doubleValue());
            return new EconomyResponse(amount, newBalance.doubleValue(), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (PlayerNotFoundException e) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "The given user does not exists");
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        synchronized (plugin.getDatabase()) {
            EconomyResponse response;
            try {
                plugin.getDatabase().beginTransaction();
                response = doDepositPlayer(player, amount);
                plugin.getDatabase().commitTransaction();
            } catch (DatabaseException e) {
                plugin.getLogger().warning("Transaction failed : " + e.getMessage());
                response = new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "There was an error with the database : " + e.getMessage());
                try{
                    plugin.getDatabase().rollbackTransaction();
                } catch (DatabaseException rollbackException) {
                    plugin.getLogger().severe("Unable to rollback transaction : " + rollbackException.getMessage());
                }
            }
            return response;
        }
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
