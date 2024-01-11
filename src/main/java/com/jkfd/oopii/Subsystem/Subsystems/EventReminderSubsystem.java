package com.jkfd.oopii.Subsystem.Subsystems;

import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Subsystem.Subsystem;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.jkfd.oopii.HelloApplication.databaseManager;

/**
 * The event reminder subsystem periodically checks if there is an upcoming event, and notifies the user accordingly.
 */
public class EventReminderSubsystem extends Subsystem {
    public EventReminderSubsystem() {
        this.name = "event-reminders";
    }

    @Override
    public void Fire() {
        try {
            ArrayList<Event> events = databaseManager.GetEvents();

            // TODO: Check if event is happening soon

            LoggerFactory.getLogger(EventReminderSubsystem.class).info(this.name + " subsystem fired.");
        } catch(Exception e) {
            super.HandleException(e);
        }
    }
}
