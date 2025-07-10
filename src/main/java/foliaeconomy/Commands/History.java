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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class History implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;
        List<String> historyList = getPlayerhistory(player);
        boolean isValidInteger = false;
        String prefix = FoliaEconomy.getInstance().getConfig().getString("messages.prefix");
        String defaultColor = FoliaEconomy.getInstance().getConfig().getString("messages.defaultColor");
        if(args.length == 1) {
            try {
                Integer.parseInt(args[0]);
                isValidInteger = true;
            }
            catch(NumberFormatException e) {
                player.sendMessage("Command requires a whole number that is at least 1");
            }

            if(isValidInteger) {
                int page = Integer.parseInt(args[0]);
                if(page >= 1) {
                    int startIndex = (page - 1) * 4; //Start from the page number subtracted by 1, multiplied by the 4 entries per page
                    int endIndex = startIndex + 4; //End 4 entries after the start
                    player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',defaultColor + "History Page " + page));
                    for (int i = startIndex; i < historyList.size() && i < endIndex; i++) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', historyList.get(i)));
                    }
                }

                else {
                    player.sendMessage("The number needs to be at least 1");
                }
            }
        }
        else {
            int iterations = 0;
            player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',defaultColor + "History Page 1"));
            for (String historyItem : historyList) {
                if(iterations < 4) { //Iterate through the list 4 times
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', historyItem));
                }
                iterations++;
            }
        }
        return true;
    }
    public static List<String> getPlayerhistory(Player sender) {
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
                    history.add(historyString(getName(dataReceiver), reformattedTime, amount, true));
                }

                if(dataReceiver.equals(sender.getUniqueId().toString())) {
                    history.add(historyString(getName(dataReceiver), reformattedTime, amount, false));
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
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String historyString(String playerName, String time, double value, boolean fromPlayer) {
        String historyString = "";

        if(fromPlayer) {
            historyString = FoliaEconomy.getInstance().getConfig().getString("messages.history.standardHistory.moneyFromPlayer");
        }

        else {
            historyString = FoliaEconomy.getInstance().getConfig().getString("messages.history.standardHistory.moneyToPlayer");
        }

        assert historyString != null;
        historyString = historyString.replace("{time}", time).replace("{value}", String.valueOf(value)).replace("{player}", playerName);
        return historyString;
    }
}
