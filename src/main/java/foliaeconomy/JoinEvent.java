package foliaeconomy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JoinEvent implements Listener {
    @EventHandler()
    public void joinListener(PlayerJoinEvent event) {
        var player = event.getPlayer();

        try {
            PreparedStatement stmt = MySQL.getConnection().prepareStatement("INSERT INTO players (uuid, name, balance) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE name=?"); //Create an account for a player on join
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, player.getName());
            stmt.setInt(3, 0);
            stmt.setString(4, player.getName());
            stmt.executeUpdate();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
