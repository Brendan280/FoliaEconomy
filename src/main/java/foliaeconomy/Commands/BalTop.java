package foliaeconomy.Commands;

import foliaeconomy.FoliaEconomy;
import foliaeconomy.MySQL;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BalTop implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;
        List<String> balances = getPlayerBalances(); //Get a list of player balances
        boolean isValidInteger = false;
        String prefix = FoliaEconomy.getInstance().getConfig().getString("messages.prefix");
        String defaultColor = FoliaEconomy.getInstance().getConfig().getString("messages.defaultColor");

        if(args.length == 1) {
            try {
                Integer.parseInt(args[0]);
                isValidInteger = true;
            }
            catch(NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Command requires a whole number that is at least 1");
            }

            if(isValidInteger) {
                int page = Integer.parseInt(args[0]);
                if(page >= 1) {
                    int startIndex = (page - 1) * 9; //Start from the page number subtracted by 1, multiplied by the 9 entries per page
                    int endIndex = startIndex + 9; //End 9 entries after the start
                    player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',defaultColor + "Balance Top Page " + page));
                    for (int i = startIndex; i < balances.size() && i < endIndex; i++) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', balances.get(i)));
                    }
                }

                else {
                    player.sendMessage(ChatColor.RED + "The number needs to be at least 1");
                }
            }
        }

        else {
            int iterations = 0;
            player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',defaultColor + "Balance Top Page 1"));
            for (String balance : balances) {
                if(iterations < 9) { //Iterate through the first 9 entries in the list
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',balance));
                }
                iterations++;
            }
        }
        return true;
    }

    public static List<String> getPlayerBalances() {
        String allBalancesSQL = "SELECT balance, name FROM players ORDER BY balance DESC;";

        List<String> balances = new ArrayList<>();

        try (PreparedStatement stmt = MySQL.getConnection().prepareStatement(allBalancesSQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                double balance = rs.getDouble("balance");
                String playerName = rs.getString("name");
                balances.add(Balance.getBalanceString(playerName, balance, false));
            }

        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        return balances;
    }
}