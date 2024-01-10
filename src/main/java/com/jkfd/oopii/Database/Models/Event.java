package com.jkfd.oopii.Database.Models;

import java.sql.Date;

public class Event extends Element {
    /**
     * Variable storing if the event is happening for the entire day or not.
     */
    public boolean fullDay;

    /**
     * The date(time) when then event starts.
     */
    private Date startDate;

    /**
     * The date(time) when the event stops.
     */
    private Date endDate;

    public Date GetStartDate()
    {
        return this.startDate;
    }

    public Date GetEndDate()
    {
        return this.endDate;
    }

    public void SetDateRange(Date start, Date end)
    {
        this.startDate = start;
        this.endDate = end;
    }
}
