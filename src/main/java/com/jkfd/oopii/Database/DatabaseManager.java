package com.jkfd.oopii.Database;

import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Database.Models.Todo;
import com.jkfd.oopii.Database.Repository.SQLiteDB;

import java.util.Objects;

public class DatabaseManager implements IDBRepository {
    private final IDBRepository Repository;

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
    public Event[] GetEvents() {
        return Repository.GetEvents();
    }

    @Override
    public Event[] GetEvents(int range) {
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
    public Todo[] GetTodos() {
        return Repository.GetTodos();
    }

    @Override
    public Todo[] GetTodos(int range) {
        return Repository.GetTodos(range);
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
