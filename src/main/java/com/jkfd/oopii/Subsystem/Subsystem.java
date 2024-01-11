package com.jkfd.oopii.Subsystem;

import org.slf4j.LoggerFactory;

import java.util.Timer;

/**
 * Base subsystem class all subsystems are built upon.
 */
public abstract class Subsystem {
    public String name = "unnamed-subsystem";
    public boolean suspended = false;
    public int fireTime = 300000; // 5 Minutes

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
     */
    public void Fire() {

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
        }
    }
}
