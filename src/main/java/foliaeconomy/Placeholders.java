package foliaeconomy;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import static foliaeconomy.GetBalance.getBalance;

public class Placeholders extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "foliaeconomy";
    }

    @Override
    public @NotNull String getAuthor() {
        return "foliaeconomy";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if(identifier.equals("balance")) {
            double balance = getBalance(player.getUniqueId());
            return String.valueOf(balance);
        }

        if(identifier.equals("balance_formatted")) {
            double balance = getBalance(player.getUniqueId());
            if (balance >= 1000000) {
                return String.format("%.2fM", balance / 1_000_000);
            }

            else if (balance >= 1000) {
                return String.format("%.2fK", balance / 1_000);
            }

            else {
                return String.format("%.2f", balance);
            }
        }
        return null;
    }
}
