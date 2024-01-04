package com.jkfd.oopii.Database;

import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Database.Models.Todo;

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
    Event[] GetEvents();

    /**
     * This is similar to the GetEvents function (without any parameters). The only exception is the ability to supply
     * a so-called 'range'. This will then fetch all events from newest to n (range).
     * @param range amount of events to fetch
     * @return all events in range
     */
    Event[] GetEvents(int range);

    /**
     *
     * @param event
     * @return
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
     *
     * @param id
     * @return
     */
    Todo GetTodo(int id);

    /**
     *
     * @return
     */
    Todo[] GetTodos();

    /**
     *
     * @param range
     * @return
     */
    Todo[] GetTodos(int range);

    /**
     *
     * @param todo
     * @return
     */
    Todo UpdateTodo(Todo todo);

    /**
     *
     * @param id
     */
    void DeleteTodo(int id);
}
