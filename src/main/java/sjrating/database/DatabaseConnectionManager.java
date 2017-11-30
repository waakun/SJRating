package sjrating.database;

import sjrating.database.DatabaseConnectionCredentials;
import sjrating.database.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static final String CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://%s:%d/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CET";

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    private Connection connection = null;

    public DatabaseConnectionManager(DatabaseConnectionCredentials credentials) {
        this.host = credentials.getHost();
        this.port = credentials.getPort();
        this.database = credentials.getDatabase();
        this.username = credentials.getUsername();
        this.password = credentials.getPassword();
    }

    public void connect() throws DatabaseException {
        try {
            String url = String.format(URL, host, port, database);
            Class.forName(CLASS_NAME);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseException("Cannot connect do database.\n" + e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws DatabaseException {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot close connection to database.\n" + e.getMessage(), e);
        }
    }
}
