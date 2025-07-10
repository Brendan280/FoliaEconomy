package foliaeconomy;
import foliaeconomy.Commands.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

import java.sql.SQLException;

public final class FoliaEconomy extends JavaPlugin {
    private static FoliaEconomy instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        String host = getConfig().getString("database.host");
        String database = getConfig().getString("database.databaseName");
        String user = getConfig().getString("database.user");
        String password = getConfig().getString("database.password");
        String port = getConfig().getString("database.port");

        MySQL.connect(host, database, user, password, port); //Connect to the database
        createTables();
        VaultIntegration economy = new VaultIntegration();
        Bukkit.getServicesManager().register(Economy.class, economy, this, ServicePriority.Normal);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        this.getCommand("bal").setExecutor(new Balance());
        this.getCommand("pay").setExecutor(new Pay());
        this.getCommand("setbal").setExecutor(new SetBal());
        this.getCommand("baltop").setExecutor(new BalTop());
        this.getCommand("history").setExecutor(new History());
        this.getCommand("adminhistory").setExecutor(new AdminHistory());

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders().register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static FoliaEconomy getInstance() {
        return instance;
    }

    private void createTables() { //Create the tables for the database if they do not exist
        String playerSQL = "CREATE TABLE IF NOT EXISTS players (uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(32), balance DOUBLE);";
        String transactionSQL = "CREATE TABLE IF NOT EXISTS transactions (transactionID INT PRIMARY KEY, sender VARCHAR(36), receiver VARCHAR(36), amount DOUBLE, time DATETIME);";
        try (var stmt = MySQL.getConnection().createStatement()) {
            stmt.execute(playerSQL);
            stmt.execute(transactionSQL);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
