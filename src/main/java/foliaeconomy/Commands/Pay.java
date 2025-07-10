package foliaeconomy.Commands;

import foliaeconomy.DepositFunds;
import foliaeconomy.FoliaEconomy;
import foliaeconomy.MySQL;
import foliaeconomy.WithdrawFunds;
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
import java.util.UUID;

import static java.lang.Double.parseDouble;

public class Pay implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;
        if(args.length == 2) {
            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
            if(target != null && !sender.getName().equals(target.getName())) {
                if(args[1].matches("\\d+(\\.\\d{1,2})?")) //Ensure that the number only has up to 2 decimal places
                {
                    double paymentParsed =  parseDouble(args[1]);
                    String prefix = FoliaEconomy.getInstance().getConfig().getString("messages.prefix");

                    if(hasFunds(player.getUniqueId(), paymentParsed))
                    {
                        DepositFunds.depositFunds(target.getUniqueId(), paymentParsed, false); //Deposit money to the receiver
                        WithdrawFunds.withdrawFunds(player.getUniqueId(), paymentParsed); //Withdraw money from the sender
                        player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', getPaymentString(player.getName(), paymentParsed, true))); //Send a message to the sender
                        logPayment(player.getUniqueId(), target.getUniqueId(), paymentParsed);
                        if(target.isOnline()) {
                            Player playerTarget = (Player) target;
                            playerTarget.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', getPaymentString(player.getName(), paymentParsed, false))); //Send a message to the receiver
                        }
                    }

                    else {
                        player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&',getErrorString(target.getName(), paymentParsed)));
                    }
                }

                else {
                    player.sendMessage(ChatColor.RED +"This is not a valid number that is 2 decimal places or less");
                }
            }

            else {
                player.sendMessage(ChatColor.RED + "This player has never played or is yourself");
            }
        }

        else {
            player.sendMessage(ChatColor.RED + "Command format: /pay <playername> <amount>");
        }
        return true;
    }

    public boolean hasFunds(UUID uuid, Double funds) { //Ensure that the sending player has the funds
        try (PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT balance FROM players WHERE uuid = ?")) {
            stmt.setString(1, uuid.toString());
            stmt.executeQuery();

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double balance = rs.getDouble("balance");

                    if(balance >= funds) {
                        return true;
                    }
                }
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void logPayment(UUID sender, UUID receiver, Double funds) { //Log the payment into the transaction log
        int count = -1;
        try (PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT count(transactionID) FROM transactions")) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement stmt = MySQL.getConnection().prepareStatement("INSERT INTO transactions (transactionID, sender, receiver, amount, time) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setInt(1, count + 1);
            stmt.setString(2, sender.toString());
            stmt.setString(3, receiver.toString());
            stmt.setDouble(4, funds);
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getPaymentString(String playerName, double value, boolean forSender) {
        String paymentString;

        if(forSender) {
            paymentString = FoliaEconomy.getInstance().getConfig().getString("messages.payment.paymentSent");
        }

        else {
            paymentString = FoliaEconomy.getInstance().getConfig().getString("messages.payment.paymentReceived");
        }
        assert paymentString != null;
        paymentString = paymentString.replace("{value}", String.valueOf(value)).replace("{player}", playerName);
        return paymentString;
    }

    public static String getErrorString(String playerName, double value) {
        String paymentString = FoliaEconomy.getInstance().getConfig().getString("messages.payment.paymentNoMoney");
        assert paymentString != null;
        paymentString = paymentString.replace("{value}", String.valueOf(value)).replace("{player}", playerName);
        return paymentString;
    }
}