package com.jkfd.oopii.Subsystem.Subsystems;

import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Subsystem.Subsystem;
import org.slf4j.LoggerFactory;

/**
 * TODO: Change this default comment accordingly.
 */
public class ${NAME} extends Subsystem {
    public EventReminderSubsystem() {
        this.name = "new-subsystem"; // TODO: Choose a proper name for the subsystem
    }

    @Override
    public void Fire() {
        try {
            super.Fire(); // TODO: Replace super.Fire() with subsystem implementation
        } catch(Exception e) {
            super.HandleException(e);
        }
    }
}
