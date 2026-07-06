package com.art9.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Collections;
import java.util.List;


@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseManager.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseManager.shutdown();
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (LinkageError | Exception ignored) {
        }
        deregistraDriverJdbc();
    }

   
    private void deregistraDriverJdbc() {
        List<Driver> daRimuovere = Collections.list(DriverManager.getDrivers()).stream()
                .filter(driver -> driver.getClass().getClassLoader() == getClass().getClassLoader())
                .toList();
        for (Driver driver : daRimuovere) {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (java.sql.SQLException ignored) {
            }
        }
    }
}
