package foliaeconomy.Commands;

import foliaeconomy.FoliaEconomy;
import foliaeconomy.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminHistory implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;
        boolean isValidInteger = false;

        if(args.length == 1 || args.length == 2) { //Ensure that 1 or 2 arguments were used
            String prefix = FoliaEconomy.getInstance().getConfig().getString("messages.prefix");
            String defaultColor = FoliaEconomy.getInstance().getConfig().getString("messages.defaultColor");
            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (target != null) {
                List<String> historylist = getPlayerhistory(target);
                if(args.length == 1) {
                    int iterations = 0;
                    player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',defaultColor + "History Page 1"));
                    for (String history : historylist) {
                        if (iterations < 4) { //Run through the list 4 times
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', history));
                        }
                        iterations++;
                    }
                }

                else {
                    try {
                        Integer.parseInt(args[1]);
                        isValidInteger = true;
                    }
                    catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Command requires a whole number that is at least 1");
                    }

                    if (isValidInteger) { //Ensure that the page number is a valid integer
                        int page = Integer.parseInt(args[1]);
                        if (page >= 1) {
                            int startIndex = (page - 1) * 4; //Start from the page number subtracted by 1, multiplied by the 4 entries per page
                            int endIndex = startIndex + 4; //End 4 entries after the start
                            player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',defaultColor + "History Page " + page));

                            for (int i = startIndex; i < historylist.size() && i < endIndex; i++) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', historylist.get(i)));
                            }
                        }

                        else {
                            player.sendMessage(ChatColor.RED + "The number needs to be at least 1");
                        }
                    }
                }
            }

            else {
                player.sendMessage(ChatColor.RED + "This player has never played");
            }
        }

        else {
            player.sendMessage(ChatColor.RED + "Command format: /adminhitory <playername> <page>");
        }
        return true;
    }
    public static List<String> getPlayerhistory(OfflinePlayer sender) {
        String allBalancesSQL = "SELECT * FROM transactions ORDER BY time DESC;";

        List<String> history = new ArrayList<>();

        try (PreparedStatement stmt = MySQL.getConnection().prepareStatement(allBalancesSQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                double amount = rs.getDouble("amount");
                String dataSender = rs.getString("sender");
                String dataReceiver = rs.getString("receiver");
                String time = rs.getTimestamp("time").toString();
                String reformattedTime = time.substring(0, time.length() - 2); //Remove the trailing .0 from time
                boolean usTime = FoliaEconomy.getInstance().getConfig().getBoolean("USTime");
                if(usTime) {
                    Timestamp timestamp = rs.getTimestamp("time");
                    LocalDateTime dateTime = timestamp.toLocalDateTime();
                    DateTimeFormatter usFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
                    reformattedTime = dateTime.format(usFormatter);
                }

                if(dataSender.equals(sender.getUniqueId().toString())) {
                    history.add(historyString(sender.getName(), getName(dataReceiver), reformattedTime, amount,true));
                }

                if(dataReceiver.equals(sender.getUniqueId().toString())) {
                    history.add(historyString(sender.getName(), getName(dataSender), reformattedTime, amount,false));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public static String getName(String player) { //Get the name of a player based on their UUID
        String query = "SELECT uuid, name FROM players WHERE uuid = ?";
        String name = "";
        try (PreparedStatement stmt = MySQL.getConnection().prepareStatement(query)) {
            stmt.setString(1, player);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    name = rs.getString("name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String historyString(String selectedPlayer, String otherPlayerName, String time, double value, boolean fromPlayer) {
        String historyString = "";
        if(fromPlayer) {
            historyString = FoliaEconomy.getInstance().getConfig().getString("messages.history.adminHistory.moneyFromPlayer");
        }

        else {
            historyString = FoliaEconomy.getInstance().getConfig().getString("messages.history.adminHistory.moneyToPlayer");
        }
        assert historyString != null;
        historyString = historyString.replace("{time}", time).replace("{selectedPlayer}", selectedPlayer).replace("{value}", String.valueOf(value)).replace("{player}", otherPlayerName);
        return historyString;
    }
}
