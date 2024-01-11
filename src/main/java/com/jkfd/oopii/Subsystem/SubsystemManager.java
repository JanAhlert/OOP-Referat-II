package com.jkfd.oopii.Subsystem;

import com.jkfd.oopii.Subsystem.Subsystems.EventReminderSubsystem;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class SubsystemManager {
    HashSet<Subsystem> subsystems = new HashSet<>();

    public SubsystemManager() {
        RegisterSubsystem(new EventReminderSubsystem());
    }

    public void Shutdown() {
        for (Subsystem subsystem : subsystems) {
            subsystem.timer.cancel();
        }
    }

    private void RegisterSubsystem(Subsystem subsystem)
    {
        subsystems.add(subsystem);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                subsystem.Fire();
            }
        };

        Timer timer = new Timer("SubsystemTimer_" + subsystem.name);
        subsystem.timer = timer;

        timer.scheduleAtFixedRate(timerTask, 30, subsystem.fireTime);
    }
}
