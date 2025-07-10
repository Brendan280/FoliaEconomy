package foliaeconomy;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static foliaeconomy.GetBalance.getBalance;

public class WithdrawFunds {
    public static void withdrawFunds(UUID uuid, double funds) {
        double currentBalance = getBalance(uuid);
        double newBalance = currentBalance - funds;

        try (PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE players SET balance = ? WHERE uuid = ?")) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
