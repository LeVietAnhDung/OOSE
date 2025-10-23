package com.codeTeam_3.config;

import com.codeTeam_3.util.dbMysql;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.sql.Connection;

public class AppLifecycleListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        try (Connection connection = dbMysql.getConnection()) {
            event.getServletContext().log("dbMysql pool ready: " + connection.getMetaData().getURL());
        } catch (Exception e) {
            throw new RuntimeException("Cannot connect DB at startup", e);
        }
    }
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        dbMysql.close();
    }
}
