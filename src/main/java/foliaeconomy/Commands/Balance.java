package foliaeconomy.Commands;

import foliaeconomy.FoliaEconomy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import foliaeconomy.GetBalance;


public class Balance implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;
        String prefix = FoliaEconomy.getInstance().getConfig().getString("messages.prefix");
        if(args.length == 0) {
            player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', getBalanceString(player.getName(), GetBalance.getBalance(player.getUniqueId()),true)));
        }

        else if(args.length == 1) {
            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);

            if(target != null) {
                player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', getBalanceString(target.getName(), GetBalance.getBalance(target.getUniqueId()), false)));
            }

            else {
                player.sendMessage(ChatColor.RED + "This player has never played");
            }
        }
        return true;
    }

    public static String getBalanceString(String playerName, double value, boolean justSelf) {
        String balanceString;

        if (justSelf) {
            balanceString = FoliaEconomy.getInstance().getConfig().getString("messages.balance.selfBalance");
            assert balanceString != null;
            balanceString = balanceString.replace("{value}", String.valueOf(value));
        }

        else {
            balanceString = FoliaEconomy.getInstance().getConfig().getString("messages.balance.otherBalance");
            assert balanceString != null;
            balanceString = balanceString.replace("{value}", String.valueOf(value)).replace("{player}", playerName);
        }
        return balanceString;
    }
}
