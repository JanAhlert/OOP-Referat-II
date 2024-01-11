package com.jkfd.oopii.Database;

import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Database.Models.Todo;
import com.jkfd.oopii.Database.Repository.SQLiteDB;

import java.util.ArrayList;
import java.util.Objects;

public class DatabaseManager implements IDBRepository {
    /**
     * The instance of the currently initialized database that the manager keeps track of to forward the functions to.
     */
    private final IDBRepository Repository;

    /**
     * The database manager handles all the required handling and forwarding of all the functions to manipulate data in
     * the database. When initializing a new manager (which only one should exist of at any given time), it is possible
     * to pass a 'database type' to initialize.
     * <br><br>
     * Currently, these types are available:<br>
     * - sqlite
     * @param dbType the database kind to initialize (see list above)
     * @throws Exception
     */
    public DatabaseManager(String dbType) throws Exception {
        if (Objects.equals(dbType, "sqlite")) {
            Repository = new SQLiteDB();
            return;
        }

        throw new Exception("Unknown database type '" + dbType + "'");
    }

    @Override
    public void CreateEvent(Event event) {
        Repository.CreateEvent(event);
    }

    @Override
    public Event GetEvent(int id) {
        return Repository.GetEvent(id);
    }

    @Override
    public ArrayList<Event> GetEvents() {
        return Repository.GetEvents();
    }

    @Override
    public ArrayList<Event> GetEvents(int range) {
        return Repository.GetEvents(range);
    }

    @Override
    public Event UpdateEvent(Event event) {
        return Repository.UpdateEvent(event);
    }

    @Override
    public void DeleteEvent(int id) {
        Repository.DeleteEvent(id);
    }

    @Override
    public void CreateTodo(Todo todo) {
        Repository.CreateTodo(todo);
    }

    @Override
    public Todo GetTodo(int id) {
        return Repository.GetTodo(id);
    }

    @Override
    public ArrayList<Todo> GetTodos() {
        return Repository.GetTodos();
    }

    @Override
    public ArrayList<Todo> GetTodos(int range) {
        return Repository.GetTodos(range);
    }

    @Override
    public ArrayList<Todo> GetUnfinishedTodos() {
        return Repository.GetUnfinishedTodos();
    }

    @Override
    public ArrayList<Todo> GetUnfinishedTodos(int range) {
        return Repository.GetUnfinishedTodos(range);
    }

    @Override
    public Todo UpdateTodo(Todo todo) {
        return Repository.UpdateTodo(todo);
    }

    @Override
    public void DeleteTodo(int id) {
        Repository.DeleteTodo(id);
    }
}
