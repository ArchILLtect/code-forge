package me.nickhanson.codeforge.testutil;

import me.nickhanson.codeforge.config.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Provides access to the database
 * Created on 8/31/16.
 * @author pwaite
 * @author Alex M - Fall 2019 - added multi-line sql capabilitygit
*/
public class Database implements PropertiesLoader {

    private static final Logger logger = LogManager.getLogger(Database.class);

    // singleton instance
    private static final Database instance = new Database();

    private final Properties properties;
    private Connection connection;

    /** private constructor prevents instantiating this class anywhere else */
    private Database() {
        properties = loadProperties("/test-db.properties");
    }

    /** get the single database object */
    public static Database getInstance() {
        return instance;
    }

    /** get the database connection */
    public Connection getConnection() {
        return connection;
    }

    /** attempt to connect to the database */
    public void connect() throws Exception {
        if (connection != null) return;

        try {
            Class.forName(properties.getProperty("hibernate.connection.driver_class"));
        } catch (ClassNotFoundException e) {
            throw new Exception("Database Driver not found", e);
        }

        String url = properties.getProperty("hibernate.connection.url");
        connection = DriverManager.getConnection(
                url,
                properties.getProperty("hibernate.connection.username"),
                properties.getProperty("hibernate.connection.password")
        );
    }

    /** close and clean up the database connection */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("Cannot close database connection", e);
            } finally {
                connection = null;
            }
        }
    }

    /**
     * Run the SQL file.
     *
     * @param sqlFile the SQL file to be read and executed line by line
     */
    public void runSQL(String sqlFile) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(classloader.getResourceAsStream(sqlFile)))) {

            connect();
            try (Statement stmt = connection.createStatement()) {
                String sql = "";

                while (br.ready()) {
                    char inputValue = (char) br.read();

                    if (inputValue == ';') {
                        stmt.executeUpdate(sql.trim());
                        sql = "";
                    } else {
                        sql += inputValue;
                    }
                }
            }

        } catch (SQLException se) {
            logger.error("SQL Exception while executing file: " + sqlFile, se);
        } catch (Exception e) {
            logger.error("Exception while reading file: " + sqlFile, e);
        } finally {
            disconnect();
        }
    }
}