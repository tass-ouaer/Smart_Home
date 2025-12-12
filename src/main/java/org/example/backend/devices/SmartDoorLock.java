package org.example.backend.devices;

import org.example.backend.interfaces.Schedulable;

public class SmartDoorLock extends SmartDevice implements Schedulable {

    private boolean isLocked;
    private String accessCode;
    private String scheduledTime;
    private int autoLockDelay; // in minutes

    public SmartDoorLock(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);

        this.isLocked = true;          // Door starts locked
        this.accessCode = "1234";      // Default code
        this.scheduledTime = null;
        this.autoLockDelay = 5;

        // Most smart locks use around 3 watts when active
        this.setPowerRating(3.0);

        // Device is ON (listening, motor enabled)
        this.isOn = true;
        startTimeTracking();
    }

    // --------------------------------------------------------------------
    // POWER-INFLUENCED LOCK BEHAVIOR
    // --------------------------------------------------------------------
    @Override
    public void turnOn() {
        if (!isOn) {
            this.isOn = true;
            startTimeTracking();
        }
        this.isLocked = false;
        System.out.println(deviceName + " is now UNLOCKED");
    }

    @Override
    public void turnOff() {
        if (isOn) stopTimeTracking();

        this.isOn = false;
        this.isLocked = true;

        System.out.println(deviceName + " is now LOCKED");
    }

    @Override
    public String getDeviceType() {
        return "SmartDoorLock";
    }

    @Override
    public String getStatus() {
        return isLocked ? "LOCKED - Door secured" : "UNLOCKED - Door accessible";
    }

    // --------------------------------------------------------------------
    // SCHEDULING
    // --------------------------------------------------------------------
    @Override
    public void schedule(String time) {
        this.scheduledTime = time;
        System.out.println(deviceName + " scheduled to lock at " + time);
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    // --------------------------------------------------------------------
    // LOCKING / UNLOCKING LOGIC
    // --------------------------------------------------------------------
    public void lock() {
        turnOff();
    }

    public boolean unlock() {
        if (verifyAccessCode(code)) {
            turnOn();
            return true;
        }

        System.out.println("Access denied - Incorrect code for " + deviceName);
        return false;
    }

    private boolean verifyAccessCode(String code) {
        return this.accessCode.equals(code);
    }

    public boolean setAccessCode(String oldCode, String newCode) {
        if (verifyAccessCode(oldCode)) {
            this.accessCode = newCode;
            System.out.println("Access code updated for " + deviceName);
            return true;
        }

        System.out.println("Cannot change code - Incorrect current code");
        return false;
    }

    // --------------------------------------------------------------------
    // GETTERS / SETTERS
    // --------------------------------------------------------------------
    public String getAccessCode() {
        return accessCode;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setAutoLockDelay(int minutes) {
        if (minutes >= 1 && minutes <= 60) {
            this.autoLockDelay = minutes;
            System.out.println(deviceName + " will auto-lock after " + minutes + " minutes");
        }
    }

    public int getAutoLockDelay() {
        return autoLockDelay;
    }

    // --------------------------------------------------------------------
    // EMERGENCY MODE
    // --------------------------------------------------------------------
    public void emergencyUnlock() {
        this.isLocked = false;

        if (!isOn) {
            this.isOn = true;
            startTimeTracking();
        }

        System.out.println("EMERGENCY UNLOCK - " + deviceName + " unlocked!");
    }
}
