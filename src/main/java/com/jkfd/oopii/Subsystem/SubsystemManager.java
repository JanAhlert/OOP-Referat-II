package com.jkfd.oopii.Subsystem;

import com.jkfd.oopii.Subsystem.Subsystems.EventReminderSubsystem;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Subsystems are used to run specific logic & code at specific intervals (asynchronously) from everything else.
 * Examples include looking up if any events are upcoming, fetching/updating iCal files, etc.
 */
public class SubsystemManager {
    /**
     * Variable to keep track of currently registered subsystems.
     */
    private final HashSet<Subsystem> subsystems = new HashSet<>();

    /**
     * Constructor of the SubsystemManager.
     * Subsystems get registered here.
     */
    public SubsystemManager() {
        // Register Subsystems
        RegisterSubsystem(new EventReminderSubsystem());
    }

    /**
     * The shutdown function will stop all running timers.
     */
    public void Shutdown() {
        for (Subsystem subsystem : subsystems) {
            subsystem.timer.cancel();
        }
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
    }
}
