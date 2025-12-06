package org.example.backend.devices;

import org.example.backend.interfaces.Schedulable;

public class SmartDoorLock extends SmartDevice implements Schedulable {

    private boolean isLocked;
    private String accessCode;
    private String scheduledTime;
    private int autoLockDelay;

    public SmartDoorLock(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);
        this.isLocked = true;
        this.accessCode = "1234";
        this.scheduledTime = null;
        this.autoLockDelay = 5;
        this.energyConsumption = 0.003;
    }

    @Override
    public void turnOn() {
        this.isOn = true;
        this.isLocked = false;
        startTimeTracking();
        System.out.println(deviceName + " is now UNLOCKED");
    }

    @Override
    public void turnOff() {
        stopTimeTracking();
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
        if (isLocked) {
            return "LOCKED - Door secured";
        } else {
            return "UNLOCKED - Door accessible";
        }
    }

    @Override
    public void schedule(String time) {
        this.scheduledTime = time;
        System.out.println(deviceName + " scheduled to lock at " + time);
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void lock() {
        turnOff();
    }

    public boolean unlock(String code) {
        if (verifyAccessCode(code)) {
            turnOn();
            return true;
        } else {
            System.out.println("Access denied - Incorrect code for " + deviceName);
            return false;
        }
    }

    private boolean verifyAccessCode(String code) {
        return this.accessCode.equals(code);
    }

    public boolean setAccessCode(String oldCode, String newCode) {
        if (verifyAccessCode(oldCode)) {
            this.accessCode = newCode;
            System.out.println("Access code updated for " + deviceName);
            return true;
        } else {
            System.out.println("Cannot change code - Incorrect current code");
            return false;
        }
    }

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

    public void emergencyUnlock() {
        this.isLocked = false;
        this.isOn = true;
        startTimeTracking();
        System.out.println("EMERGENCY UNLOCK - " + deviceName + " unlocked!");
    }
}