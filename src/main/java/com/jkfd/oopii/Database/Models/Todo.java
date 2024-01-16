package com.jkfd.oopii.Database.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Todo extends Element {
    /**
     * The date when the todo is due for completion.
     */
    public LocalDate dueDate;

    /**
     * The date(time) when the todo was completed.
     */
    private LocalDateTime completedDate;

    /**
     * Sets the todo completed at the specific date that was supplied.
     * Set this to null to set as uncompleted.
     * @param date completed date
     */
    public void SetCompletedDate(LocalDateTime date)
    {
        this.completedDate = date;
    }

    /**
     * Returns a date if the todo was completed. It will return null otherwise.
     * @return completion date
     */
    public LocalDateTime GetCompletedDate()
    {
        return this.completedDate;
    }
}
