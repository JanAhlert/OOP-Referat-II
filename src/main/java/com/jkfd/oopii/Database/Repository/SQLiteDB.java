package com.jkfd.oopii.Database.Repository;



import com.jkfd.oopii.Database.IDBRepository;
import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Database.Models.Todo;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;


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
        createTables();
        insertTestData();

        String sql = "SELECT EXISTS(SELECT * FROM migrate)";

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

            System.out.println("[sqlite] Migrated tables to newest version");
        } catch (SQLException e) {
            System.out.println("[sqlite] " + e.getMessage());
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


    /**
     * Creates the events table in the database if it doesn't exist yet.
     */
    public void createTables() { //TODO: [SQLITE_ERROR] SQL error or missing database (near "EXISTS": syntax error)
        String eventsTableSQL = """
                CREATE TABLE IF NOT EXISTS events (
                ID integer PRIMARY KEY AUTOINCREMENT NOT NULL,
                Titel text NOT NULL,
                Description text,
                Full_day boolean NOT NULL,
                Start_date text,
                End_date text);
                """;
        //Start_date is a text because SQLite safes Dates as String in de format YYYY-MM-DD

        String TODOTableSQL = """
                CREATE TABLE IF NOT EXISTS todos (
                ID integer PRIMARY KEY AUTOINCREMENT NOT NULL,
                Titel text NOT NULL,
                Description text,
                List integer,
                CompletedDate text,
                TimeRequired text);
                """;

        try {Connection conn = connect();Statement stmt = conn.createStatement();
            stmt.execute(eventsTableSQL);
            stmt.execute(TODOTableSQL);
            System.out.println("[sqlite] Events table created.");
        } catch (SQLException e) {
            System.out.println("[sqlite] " + e.getMessage());
        }
    }

    /**
     * Populates the events table with test data.
     * @author ChatGPT
     */
    public void insertTestData() {
        String[] testData = {
                "INSERT INTO events (Titel, Description, Full_day, Start_date, End_date, Created_at) VALUES ('Meeting', 'Project meeting with team', 0, '2023-01-15', '2023-01-15', '2023-01-01')",
                "INSERT INTO events (Titel, Description, Full_day, Start_date, End_date, Created_at) VALUES ('Conference', 'Annual tech conference', 1, '2023-03-20', '2023-03-22', '2023-02-15')",
                "INSERT INTO events (Titel, Description, Full_day, Start_date, End_date, Created_at) VALUES ('Workshop', 'Workshop on Java Programming', 0, '2023-05-05', '2023-05-05', '2023-04-10')"
                // Weitere Testdaten können hier hinzugefügt werden
        };

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {

            // Durchlaufen jedes Testdatensatzes und Ausführen der Insert-Anweisungen
            for (String sql : testData) {
                stmt.executeUpdate(sql);
            }

            System.out.println("[sqlite] Test data inserted into the database.");

        } catch (SQLException e) {
            System.out.println("[sqlite] Error while inserting test data: " + e.getMessage());
        }
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
        return new ArrayList<>();
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
        String query = "SELECT * FROM events";

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
            System.out.println("[sqlite] Error while retrieving events: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public ArrayList<Todo> GetTodos(int range) {
        return new ArrayList<>();
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
