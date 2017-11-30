package sjrating.properties;

import sjrating.database.DatabaseConnectionCredentials;
import sjrating.properties.exception.PropertiesLoaderException;

import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {

    public static DatabaseConnectionCredentials getDatabaseCredentials() throws PropertiesLoaderException {
        Properties prop = new Properties();
        try {
            prop.load(PropertiesLoader.class.getClassLoader().getResourceAsStream("db.properties"));
            String host = prop.getProperty("host", "localhost");
            int port = Integer.parseInt(prop.getProperty("port", "3306"));
            String database = prop.getProperty("database");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            return new DatabaseConnectionCredentials(host, port, database, username, password);
        } catch (IOException | NumberFormatException e) {
            throw new PropertiesLoaderException("Cannot load configuration file", e);
        }
    }

}
