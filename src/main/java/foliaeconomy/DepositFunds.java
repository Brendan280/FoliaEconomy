package foliaeconomy;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static foliaeconomy.GetBalance.getBalance;

public class DepositFunds {
    public static void depositFunds(UUID uuid, double funds, boolean overwrite) {
        double currentBalance = getBalance(uuid);
        double newBalance = currentBalance + funds;

        if(overwrite) {
            newBalance = funds;
        }

        try (PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE players SET balance = ? WHERE uuid = ?")) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, uuid.toString());
            stmt.executeUpdate();
        }

        catch (SQLException e) {
            FoliaEconomy.getInstance().getLogger().info("Failure to deposit funds");
        }
    }
}
