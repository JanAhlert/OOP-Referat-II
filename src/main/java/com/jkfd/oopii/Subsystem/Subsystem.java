package com.jkfd.oopii.Subsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * Base subsystem class all subsystems are built upon.
 */
public abstract class Subsystem {
    /**
     * Logger instance of the subsystem.
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

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
     * The amount of times a subsystem is allowed to error before getting suspended.
     * Default: 5 times.
     */
    public int maxFailCount = 5;

    /**
     * Timer variable for reference on shutdown of the application.
     */
    private Timer timer;

    /**
     * Bookkeeping variable to keep track how often the subsystem failed.
     */
    private int failCount = 0;

    /**
     * The fire function will be called every time the fire time has been reached.
     * The default Fire() action is just logging that the subsystem has fired.
     */
    public void Fire() {
        LoggerFactory.getLogger(this.getClass()).info(this.name + " subsystem fired.");
    }

    /**
     * Exception handling.
     * Subsystems should utilize "super.HandleException(exceptionVar);".
     * @param e exception to handle
     */
    public void HandleException(Exception e) {
        logger.atError().setMessage("Subsystem error ({}): {}").addArgument(name).addArgument(e.getMessage()).log();

        failCount++;

        if (failCount >= maxFailCount) {
            suspended = true;
            logger.atError().setMessage("Subsystem suspended ({}) for throwing too many errors.").addArgument(name).log();
        }
    }

    /**
     * This function will be called when the SubsystemManager is shutting down.
     * Some subsystems might implement custom logic to clean up before fully shutting down.
     * Subsystems should utilize "super.Shutdown();" when using custom shutdown logic.
     */
    public void Shutdown() {
        logger.atInfo().setMessage("{} subsystem shutdown").addArgument(this.name).log();
    }

    /**
     * Sets the timer reference for the subsystem for later use (e.g.: shutdown operations).
     * The call to set the timer should usually only be made from the {@link com.jkfd.oopii.Subsystem.SubsystemManager}.
     * @param timer timer reference
     */
    public void SetTimer(Timer timer)
    {
        if (this.timer == null) {
            this.timer = timer;
        } else {
            logger.atError().setMessage("Tried to set timer for {} even though timer was already set").addArgument(this.name).log();
        }
    }

    /**
     * Returns the timer reference firing this subsystem.
     * @return timer reference
     */
    public Timer GetTimer() {
        return this.timer;
    }
}
