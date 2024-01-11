package com.jkfd.oopii.Subsystem;

import org.slf4j.LoggerFactory;

import java.util.Timer;

public abstract class Subsystem {
    public String name = "unnamed-subsystem";
    public boolean suspended = false;
    public int fireTime = 300000; // 5 Minutes

    public Timer timer; // only for reference

    private int failCount = 0;

    public void Fire() {

    }

    public void HandleException(Exception e) {
        LoggerFactory.getLogger(Subsystem.class).error("Subsystem error (" + name + "): " + e.getMessage());

        failCount++;

        if (failCount >= 5) {
            suspended = true;
        }
    }
}
