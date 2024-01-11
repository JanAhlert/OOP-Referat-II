package com.jkfd.oopii.Subsystem.Subsystems;

import com.jkfd.oopii.Subsystem.Subsystem;
import org.slf4j.LoggerFactory;

public class EventReminderSubsystem extends Subsystem {
    public EventReminderSubsystem() {
        this.name = "event-reminders";
    }

    @Override
    public void Fire() {
        try {
            LoggerFactory.getLogger(EventReminderSubsystem.class).info(this.name + " subsystem fired.");
        } catch(Exception e) {
            super.HandleException(e);
        }
    }
}
