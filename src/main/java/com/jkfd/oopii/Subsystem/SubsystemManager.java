package com.jkfd.oopii.Subsystem;

import com.jkfd.oopii.Subsystem.Subsystems.BackupSubsystem;
import com.jkfd.oopii.Subsystem.Subsystems.EventReminderSubsystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Subsystems are used to run specific logic/code at specific intervals (asynchronously) from everything else.
 * Examples include looking up if any events are upcoming, fetching/updating iCal files, etc.
 */
public class SubsystemManager {
    /**
     * Logger instance of the subsystem manager.
     */
    protected final Logger logger = LoggerFactory.getLogger(SubsystemManager.class);

    /**
     * Variable to keep track of currently registered subsystems.
     */
    private final HashSet<Subsystem> subsystems = new HashSet<>();

    /**
     * Constructor of the SubsystemManager.
     * Subsystems get registered here.
     */
    public SubsystemManager() {
        logger.atInfo().setMessage("Initializing subsystems").log();

        // Register Subsystems
        RegisterSubsystem(new BackupSubsystem());
        RegisterSubsystem(new EventReminderSubsystem());
    }

    /**
     * The shutdown function will stop all running timers.
     */
    public void Shutdown() {
        logger.atInfo().setMessage("Shutting down subsystems").log();

        for (Subsystem subsystem : subsystems) {
            subsystem.Shutdown();
            subsystem.timer.cancel();
        }

        logger.atInfo().setMessage("Subsystems shutdown complete").log();
    }

    /**
     * Registering a new subsystem will start its fire timer.
     * This function is only used in the SubsystemManager class so far.
     * @param subsystem subsystem instance to register
     */
    private void RegisterSubsystem(Subsystem subsystem)
    {
        // Add the newly registered subsystem to the hashset
        subsystems.add(subsystem);

        // Create a new timer and tell it to fire the subsystem when the subsystems defined fire time is reached
        // Suspended subsystems will not fire!
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!subsystem.suspended) {
                    subsystem.Fire();
                }
            }
        };

        Timer timer = new Timer("SubsystemTimer_" + subsystem.name);
        timer.scheduleAtFixedRate(timerTask, 30, subsystem.fireTime);

        // Finally give the subsystem a reference to the timer
        subsystem.timer = timer;

        logger.atInfo().setMessage("Subsystem registered: " + subsystem.name).log();
    }
}
