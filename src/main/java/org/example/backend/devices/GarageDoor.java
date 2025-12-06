package org.example.backend.devices;

import org.example.backend.interfaces.Schedulable;

public class GarageDoor extends SmartDevice implements Schedulable {

    private boolean isOpen;
    private String scheduledTime;

    public GarageDoor(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);
        this.isOpen = false;
        this.energyConsumption = 0.5;
        this.scheduledTime = null;
    }

    @Override
    public void turnOn() {
        this.isOn = true;
        this.isOpen = true;
        startTimeTracking();
        System.out.println(deviceName + " is now OPEN");
    }

    @Override
    public void turnOff() {
        stopTimeTracking();
        this.isOn = false;
        this.isOpen = false;
        System.out.println(deviceName + " is now CLOSED");
    }

    @Override
    public String getDeviceType() {
        return "GarageDoor";
    }

    @Override
    public String getStatus() {
        if (isOpen) {
            return "Door is OPEN - Ready to close";
        } else {
            return "Door is CLOSED - Secured";
        }
    }

    @Override
    public void schedule(String time) {
        this.scheduledTime = time;
        System.out.println(deviceName + " scheduled to open at " + time);
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void emergencyStop() {
        System.out.println("EMERGENCY STOP - " + deviceName + " halted!");
    }

    public int getDoorPosition() {
        return isOpen ? 100 : 0;
    }
}