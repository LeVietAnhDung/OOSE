package com.codeTeam_3.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
//import org.apache.commons.dbcp2.BasicDataSource;
//import java.sql.DriverManager;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class dbMysql {
    private static final HikariDataSource DS;

    static {
        HikariConfig cfg = new HikariConfig();

        cfg.setDriverClassName("com.mysql.cj.jdbc.Driver");
        cfg.setJdbcUrl(System.getenv().getOrDefault(
                "DB_URL",
                "jdbc:mysql://localhost:3306/team4"
        ));
        cfg.setUsername(System.getenv().getOrDefault("DB_USER", "root"));
        cfg.setPassword(System.getenv().getOrDefault("DB_PASS", ""));

        // --- Pool sizing & timeouts (ví dụ an toàn) ---
        int cores = Runtime.getRuntime().availableProcessors();
        cfg.setMaximumPoolSize(Math.max(10, cores * 2));  // gợi ý: 2×CPU (tối thiểu 10)
        cfg.setMinimumIdle(2);
        cfg.setConnectionTimeout(30_000); // 30s
        cfg.setIdleTimeout(600_000);      // 10m
        cfg.setMaxLifetime(1_800_000);    // 30m
        cfg.setPoolName("DemoHikariPool");

        // --- Optional: health check / validation ---
        cfg.setConnectionTestQuery("SELECT 1");

        // --- Driver properties (MySQL) ---
        cfg.addDataSourceProperty("cachePrepStmts", "true");
        cfg.addDataSourceProperty("prepStmtCacheSize", "250");
        cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        DS = new HikariDataSource(cfg);
    }

    private dbMysql() {}

    public static DataSource getDataSource() {
        return DS;
    }

    public static Connection getConnection() throws SQLException {
        return DS.getConnection();
    }

    public static void close() {
        DS.close();
    }
}

//public class dbMysql {
//    public static Connection getConnection() throws Exception {
//        String url = "jdbc:mysql://localhost:3306/team3";
//        // String name = "tiendungv03";
//        String name = "root";
//        String password = "daibeo";
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        return DriverManager.getConnection(url, name, password);
//    }
//}
