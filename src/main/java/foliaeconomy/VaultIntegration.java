package foliaeconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class VaultIntegration implements Economy {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "FoliaEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double v) {
        return "";
    }

    @Override
    public String currencyNamePlural() {
        return FoliaEconomy.getInstance().getConfig().getString("currencyName");
    }

    @Override
    public String currencyNameSingular() {
        return FoliaEconomy.getInstance().getConfig().getString("currencyName");
    }

    @Override
    public boolean hasAccount(String s) {
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(s);
        return target != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {;
        return offlinePlayer != null;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(s);
        return target != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return offlinePlayer != null;
    }

    @Override
    public double getBalance(String s) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(s);
        return GetBalance.getBalance(player.getUniqueId());
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return GetBalance.getBalance(offlinePlayer.getUniqueId());
    }

    @Override
    public double getBalance(String s, String s1) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(s);
        return GetBalance.getBalance(player.getUniqueId());
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return GetBalance.getBalance(offlinePlayer.getUniqueId());
    }

    @Override
    public boolean has(String s, double v) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(s);
        double balance = getBalance(player);
        return balance >= v;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        double balance = getBalance(offlinePlayer);
        return balance >= v;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(s);
        double balance = getBalance(player);
        return balance >= v;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        double balance = getBalance(offlinePlayer);
        return balance >= v;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(s);
        try {
            WithdrawFunds.withdrawFunds(player.getUniqueId(), v);
            return new EconomyResponse(v, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (Exception e) {
            return new EconomyResponse(0, getBalance(player), EconomyResponse.ResponseType.FAILURE, e.getMessage());
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        try {
            WithdrawFunds.withdrawFunds(offlinePlayer.getUniqueId(), v);
            return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (Exception e) {
            return new EconomyResponse(0, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, e.getMessage());
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(s);
        try {
            WithdrawFunds.withdrawFunds(player.getUniqueId(), v);
            return new EconomyResponse(v, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (Exception e) {
            return new EconomyResponse(0, getBalance(player), EconomyResponse.ResponseType.FAILURE, e.getMessage());
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        try {
            WithdrawFunds.withdrawFunds(offlinePlayer.getUniqueId(), v);
            return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (Exception e) {
            return new EconomyResponse(0, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, e.getMessage());
        }
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(s);
        try {
            DepositFunds.depositFunds(player.getUniqueId(), v, false);
            return new EconomyResponse(v, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (Exception e) {
            return new EconomyResponse(0, getBalance(player), EconomyResponse.ResponseType.FAILURE, e.getMessage());
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        try {
            DepositFunds.depositFunds(offlinePlayer.getUniqueId(), v, false);
            return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (Exception e) {
            return new EconomyResponse(0, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, e.getMessage());
        }
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(s);
        try {
            DepositFunds.depositFunds(player.getUniqueId(), v, false);
            return new EconomyResponse(v, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (Exception e) {
            return new EconomyResponse(0, getBalance(player), EconomyResponse.ResponseType.FAILURE, e.getMessage());
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        try {
            DepositFunds.depositFunds(offlinePlayer.getUniqueId(), v, false);
            return new EconomyResponse(v, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
        } catch (Exception e) {
            return new EconomyResponse(0, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, e.getMessage());
        }
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
}
