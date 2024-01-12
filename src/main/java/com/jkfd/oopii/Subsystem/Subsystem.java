package com.jkfd.oopii.Subsystem;

import com.jkfd.oopii.Subsystem.Subsystems.EventReminderSubsystem;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * Base subsystem class all subsystems are built upon.
 */
public abstract class Subsystem {
    /**
     * The name of the subsystem.
     */
    public String name = "unnamed-subsystem";

    /**
     * Variable that is set if the subsystem is suspended.
     * Suspended subsystems will not fire when their timer ticks.
     */
    public boolean suspended = false;

    /**
     * The fireTime sets how often/quick the subsystem should be fired.
     * Currently, the default is every 5 Minutes (300000 ms).
     */
    public long fireTime = TimeUnit.MINUTES.toMillis(5);

    /**
     * Timer variable for reference on shutdown of the application.
     */
    public Timer timer;

    /**
     * Bookkeeping variable to keep track how often the subsystem failed.
     */
    private int failCount = 0;

    /**
     * The fire function will be called every time the fire time has been reached.
     * The default Fire() action is just logging that the subsystem has fired.
     */
    public void Fire() {
        LoggerFactory.getLogger(EventReminderSubsystem.class).info(this.name + " subsystem fired.");
    }

    /**
     * Exception handling.
     * Subsystems should utilize "super.HandleException(exceptionVar);".
     * @param e exception to handle
     */
    public void HandleException(Exception e) {
        LoggerFactory.getLogger(Subsystem.class).error("Subsystem error (" + name + "): " + e.getMessage());

        failCount++;

        if (failCount >= 5) {
            suspended = true;
            LoggerFactory.getLogger(Subsystem.class).error("Subsystem suspended (" + name + ") for throwing too many errors.");
        }
    }
}
