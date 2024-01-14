package com.jkfd.oopii.Database.Models;

import java.sql.Date;

public class Todo extends Element {
    /**
     * The date(time) when the todo was completed.
     */
    private Date completedDate;

    /**
     * Sets the todo completed at the specific date that was supplied.
     * Set this to null to set as uncompleted.
     * @param date completed date
     */
    public void SetCompletedDate(Date date)
    {
        this.completedDate = date;
    }

    /**
     * Returns a date if the todo was completed. It will return null otherwise.
     * @return completion date
     */
    public Date GetCompletedDate()
    {
        return this.completedDate;
    }
}
