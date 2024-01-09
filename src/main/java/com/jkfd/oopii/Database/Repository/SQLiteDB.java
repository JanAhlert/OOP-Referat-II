package com.jkfd.oopii.Database.Repository;



import com.jkfd.oopii.Database.IDBRepository;
import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Database.Models.Todo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class SQLiteDB implements IDBRepository {
    /**
     * The constructor of the SQLiteDB class
     */
    public SQLiteDB() throws Exception {
        // Create the data directory if it doesn't exist
        File directory = new File("./data");
        if (!directory.exists()) {
            directory.mkdirs();
        }
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
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='migrations'";
        int currentMigrateVersion = -1;
        boolean didMigrate = false;

        try (Connection conn = connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Check if migration table exists and migrate everything as a result
            while(rs.next()) {
                sql = "SELECT version FROM migrations";
                rs = stmt.executeQuery(sql);

                while(rs.next()) {
                    currentMigrateVersion = rs.getInt("version");
                }
            }

            System.out.println("[sqlite] Migrate version: " + currentMigrateVersion);

            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource("sql");

            assert resource != null;
            List<File> result = Files.walk(Paths.get(resource.toURI())).filter(Files::isRegularFile).map(Path::toFile).toList();
            int migrateTarget = (result.size() - 1);

            while (currentMigrateVersion < migrateTarget) {
                try {
                    sql = Files.readString(result.get(currentMigrateVersion + 1).toPath());

                    stmt.executeLargeUpdate(sql);

                    currentMigrateVersion++;
                    System.out.println("[sqlite] Migrated sql file version " + currentMigrateVersion);
                } catch (Exception e) {
                    System.out.println("[sqlite] Error while migrating sql file version " + (currentMigrateVersion + 1) + ": " + e.getMessage());
                }

                didMigrate = true;
            }

            if (didMigrate) {
                System.out.println(MessageFormat.format("[sqlite] Migrated tables to newest version ({0})", migrateTarget));
            }
        } catch (SQLException e) {
            System.out.println("[sqlite] " + e.getMessage());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Establishes a new connection to the local sqlite database.
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

    @Override
    public void CreateEvent(Event event) {
        String query = "INSERT INTO events(title,description,full_day) VALUES (?,?,?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, event.title);
            pstmt.setString(2, event.description);
            pstmt.setBoolean(3, event.fullDay);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[sqlite] Error while creating event: " + e.getMessage());
        }
    }

    @Override
    public Event GetEvent(int id) {
        Event result = new Event();
        String query = "SELECT * FROM events WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.SetID(rs.getInt("id"));
                result.title = rs.getString("title");
                result.description = rs.getString("description");
                result.fullDay = rs.getBoolean("full_day");
            }

        } catch (SQLException e) {
            System.out.println("[sqlite] Error while retrieving event: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (result.GetID() == 0) {
            System.err.println("[sqlite] Warning, no event with id '" + id + "' found");
        }

        return result;
    }

    @Override
    public ArrayList<Event> GetEvents() {
        ArrayList<Event> result = new ArrayList<>();
        String query = "SELECT * FROM events";

        try (Connection conn = connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterate through the result set and print each record
            while (rs.next()) {
                Event tmp = new Event();
                tmp.SetID(rs.getInt("id"));
                tmp.title = rs.getString("title");
                tmp.description = rs.getString("description");
                tmp.fullDay = rs.getBoolean("full_day");

                result.add(tmp);
            }

        } catch (SQLException e) {
            System.out.println("[sqlite] Error while retrieving events: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public ArrayList<Event> GetEvents(int range) {
        ArrayList<Event> result = new ArrayList<>();
        String query = "SELECT * FROM events LIMIT ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, range);
            ResultSet rs = pstmt.executeQuery();

            // Iterate through the result set and print each record
            while (rs.next()) {
                Event tmp = new Event();
                tmp.SetID(rs.getInt("id"));
                tmp.title = rs.getString("title");
                tmp.description = rs.getString("description");
                tmp.fullDay = rs.getBoolean("full_day");

                result.add(tmp);
            }

        } catch (SQLException e) {
            System.out.println("[sqlite] Error while retrieving events: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Event UpdateEvent(Event event) {
        return null;
    }

    @Override
    public void DeleteEvent(int id) {
        String query = "DELETE FROM events WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[sqlite] Error while deleting event: " + e.getMessage());
        }
    }

    @Override
    public void CreateTodo(Todo todo) {
        String query = "INSERT INTO todos(title,description) VALUES (?,?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, todo.title);
            pstmt.setString(2, todo.description);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[sqlite] Error while creating todo: " + e.getMessage());
        }
    }

    @Override
    public Todo GetTodo(int id) {
        Todo result = new Todo();
        String query = "SELECT * FROM todos WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.SetID(rs.getInt("id"));
                result.title = rs.getString("title");
                result.description = rs.getString("description");
            }

        } catch (SQLException e) {
            System.out.println("[sqlite] Error while retrieving todo: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (result.GetID() == 0) {
            System.err.println("[sqlite] Warning, no todo with id '" + id + "' found");
        }

        return result;
    }

    @Override
    public ArrayList<Todo> GetTodos() {
        ArrayList<Todo> result = new ArrayList<>();
        String query = "SELECT * FROM todos";

        try (Connection conn = connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterate through the result set and print each record
            while (rs.next()) {
                Todo tmp = new Todo();
                tmp.SetID(rs.getInt("id"));
                tmp.title = rs.getString("title");
                tmp.description = rs.getString("description");

                result.add(tmp);
            }

        } catch (SQLException e) {
            System.out.println("[sqlite] Error while retrieving todos: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public ArrayList<Todo> GetTodos(int range) {
        ArrayList<Todo> result = new ArrayList<>();
        String query = "SELECT * FROM todos LIMIT ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, range);
            ResultSet rs = pstmt.executeQuery(query);

            // Iterate through the result set and print each record
            while (rs.next()) {
                Todo tmp = new Todo();
                tmp.SetID(rs.getInt("id"));
                tmp.title = rs.getString("title");
                tmp.description = rs.getString("description");

                result.add(tmp);
            }

        } catch (SQLException e) {
            System.out.println("[sqlite] Error while retrieving todos: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Todo UpdateTodo(Todo todo) {
        return null;
    }

    @Override
    public void DeleteTodo(int id) {
        String query = "DELETE FROM todos WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[sqlite] Error while deleting todo: " + e.getMessage());
        }
    }
}
