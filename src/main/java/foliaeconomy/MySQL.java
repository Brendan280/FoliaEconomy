package foliaeconomy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private static Connection connection;

    public static void connect(String host, String database, String user, String password, String port) {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true";

        try {
            connection = DriverManager.getConnection(url, user, password);
        }

        catch (SQLException e) {
            e.printStackTrace();
            connection = null;
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}