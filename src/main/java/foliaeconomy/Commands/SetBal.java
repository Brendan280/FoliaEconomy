package foliaeconomy.Commands;
import foliaeconomy.DepositFunds;
import foliaeconomy.FoliaEconomy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static java.lang.Double.parseDouble;

public class SetBal implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 2) {
                OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
                if (target != null) {
                    if(args[1].matches("\\d+(\\.\\d{1,2})?")) { //Ensure that the number only has up to 2 decimal places
                        double funds = parseDouble(args[1]);
                        DepositFunds.depositFunds(target.getUniqueId(), funds, true); //Deposit funds to the player, overwriting previous funds
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',setBalanceString(target.getName(), funds)));
                    }

                    else {
                        player.sendMessage(ChatColor.RED + "Input should be a number and have at most 2 decimal places");
                    }
                }

                else {
                    player.sendMessage(ChatColor.RED + "This player has never played");
                }
            }

            else {
                player.sendMessage(ChatColor.RED + "Command format: /setbal <playername> <amount");
            }
        }

        else {
            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
            assert target != null;
            DepositFunds.depositFunds(target.getUniqueId(), parseDouble(args[1]), true);
        }
        return true;
    }

    public static String setBalanceString(String playerName, double value) {
        String balanceString;
        balanceString = FoliaEconomy.getInstance().getConfig().getString("messages.balance.setBalance");
        assert balanceString != null;
        balanceString = balanceString.replace("{value}", String.valueOf(value)).replace("{player}", playerName);
        return balanceString;
    }
}