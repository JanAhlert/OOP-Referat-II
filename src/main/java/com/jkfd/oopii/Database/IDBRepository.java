package com.jkfd.oopii.Database;

import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Database.Models.Todo;

import java.util.ArrayList;

public interface IDBRepository {
    /**
     * Creates (and persists) a new event row in the database from the supplied event object.
     * @param event event object to persist
     */
    void CreateEvent(Event event);

    /**
     * This function fetches a specific event from the database and returns its object instance.
     * @param id event id to fetch
     * @return event object of supplied id
     */
    Event GetEvent(int id);

    /**
     * Returns all events that are currently persisted in the database.
     * @return all events
     */
    ArrayList<Event> GetEvents();

    /**
     * This is similar to the GetEvents function (without any parameters). The only exception is the ability to supply
     * a so-called 'range'. This will then fetch all events from newest to n (range).
     * @param range amount of events to fetch
     * @return all events in range
     */
    ArrayList<Event> GetEvents(int range);

    /**
     * Updates the event with the supplied object in the database and returns the updated event back.
     * @param event new data of the event
     * @return object of the updated event
     */
    Event UpdateEvent(Event event);

    /**
     * Deletes an event from the database.
     * @param id event to delete
     */
    void DeleteEvent(int id);

    /**
     * Creates (and persists) a new todo row in the database from the supplied todo object.
     * @param todo todo object to persist
     */
    void CreateTodo(Todo todo);

    /**
     * This function fetches a specific event from the database and returns its object instance.
     * @param id todo id to fetch
     * @return todo object of supplied id
     */
    Todo GetTodo(int id);

    /**
     * Returns all todos that are currently persisted in the database.
     * @return all todos
     */
    ArrayList<Todo> GetTodos();

    /**
     * This is similar to the GetTodos function (without any parameters). The only exception is the ability to supply
     * a so-called 'range'. This will then fetch all todos from newest to n (range).
     * @param range amount of todos to fetch
     * @return all todos in range
     */
    ArrayList<Todo> GetTodos(int range);

    /**
     * Updates the todo with the supplied object in the database and returns the updated todo back.
     * @param todo new data of the todo
     * @return object of the updated todo
     */
    Todo UpdateTodo(Todo todo);

    /**
     * Deletes an todo from the database.
     * @param id todo to delete
     */
    void DeleteTodo(int id);
}
