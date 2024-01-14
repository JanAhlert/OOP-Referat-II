package com.jkfd.oopii.Database.Models;

import java.sql.Date;

public class Event extends Element {
    /**
     * Variable storing if the event is happening for the entire day or not.
     */
    public boolean fullDay;

    /**
     * The date(time) when the event starts.
     */
    private Date startDate;

    /**
     * The date(time) when the event stops.
     */
    private Date endDate;

    /**
     * Returns the start date of the event.
     * @return starting date
     */
    public Date GetStartDate()
    {
        return this.startDate;
    }

    /**
     * Returns the end date of the event.
     * @return ending date
     */
    public Date GetEndDate()
    {
        return this.endDate;
    }

    /**
     * Sets the start- and end date at the same time for this event.
     * @param start starting date
     * @param end ending date
     */
    public void SetDateRange(Date start, Date end)
    {
        this.startDate = start;
        this.endDate = end;
    }
}
