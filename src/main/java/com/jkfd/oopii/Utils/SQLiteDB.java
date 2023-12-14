package com.jkfd.oopii.Utils;

import java.io.File;
import java.sql.*;

public class SQLiteDB {
    /**
     * The constructor of the SQLiteDB class
     */
    public SQLiteDB() throws Exception {
        // Check if the database exists and create it when it doesn't
        File f = new File("./data/app.db");
        if(!f.exists()) {
            try (Connection conn = connect()) {
                if (conn != null) {
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("[sqlite] The driver name is " + meta.getDriverName());
                    System.out.println("[sqlite] Database created.");
                } else {
                    throw new Exception("[sqlite] conn has returned null in constructor.");
                }
            }
        }

        // Migrate the database schema
        migrate();
    }

    /**
     * Migrates the database if there is a newer schema version
     */
    private void migrate() {
        String sql = "EXISTS(SELECT * FROM migrate)";

        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Check if migration table exists and migrate everything as a result
            if (rs.wasNull()) {
                // TODO
            } else {
                while (rs.next()) {
                    System.out.println(rs.getInt("version"));
                    // TODO
                }
            }
        } catch (SQLException e) {
            System.out.println("[sqlite] " + e.getMessage());
        }
    }

    /**
     * Establishes a new connection to the local sqlite database
     * Database location: ./data/app.db
     * @return sqlite connection
     */
    private Connection connect() {
        Connection conn = null;

        try {
            String url = "jdbc:sqlite:./data/app.db";

            conn = DriverManager.getConnection(url);

            System.out.println("[sqlite] Connection established");

            if (conn != null) {
                return conn;
            }
        } catch (SQLException e) {
            System.out.println("[sqlite] " + e.getMessage());
        }

        return null;
    }
}
