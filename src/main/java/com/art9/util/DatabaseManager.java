package com.art9.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public final class DatabaseManager {

    private static final String CONFIG_FILE = "db.properties";
    private static volatile HikariDataSource dataSource;

    private DatabaseManager() {
    }

    public static synchronized void init() {
        if (dataSource != null) {
            return;
        }
        Properties props = loadProperties();

        HikariConfig config = new HikariConfig();

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(require(props, "db.jdbcUrl"));
        config.setUsername(require(props, "db.username"));
        config.setPassword(props.getProperty("db.password", ""));
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.maxSize", "10")));
        config.setPoolName("Art9Pool");
        config.setConnectionTimeout(10_000);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            init();
        }
        return dataSource.getConnection();
    }

    public static synchronized void shutdown() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = DatabaseManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException(
                        "File di configurazione '" + CONFIG_FILE + "' non trovato in WEB-INF/classes. "
                                + "Copiare db.properties.example in db.properties e valorizzarlo.");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Impossibile leggere " + CONFIG_FILE, e);
        }
        return props;
    }

    private static String require(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Proprieta' mancante in db.properties: " + key);
        }
        return value;
    }
}
