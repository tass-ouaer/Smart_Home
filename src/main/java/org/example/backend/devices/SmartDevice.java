package org.example.backend.devices;

import org.example.backend.interfaces.Controllable;
import org.example.backend.interfaces.EnergyConsumer;

public abstract class SmartDevice implements Controllable, EnergyConsumer {

    protected String deviceId;
    protected String deviceName;
    protected String location;
    protected boolean isOn;

    protected double powerRating; // watts
    private long lastOnTimestamp;
    private long totalOnDuration;

    public SmartDevice(String deviceId, String deviceName, String location) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.location = location;

        this.isOn = false;
        this.powerRating = 0.0;

        this.lastOnTimestamp = 0;
        this.totalOnDuration = 0;
    }

    // -----------------------------------------------------------
    // ABSTRACT METHODS
    // -----------------------------------------------------------
    public abstract void turnOn();
    public abstract void turnOff();
    public abstract String getDeviceType();
    public abstract String getStatus();

    // -----------------------------------------------------------
    // TIME TRACKING
    // -----------------------------------------------------------
    protected void startTimeTracking() {
        if (!isOn) {
            lastOnTimestamp = System.currentTimeMillis();
        }
    }

    protected void stopTimeTracking() {
        if (isOn && lastOnTimestamp > 0) {
            totalOnDuration += System.currentTimeMillis() - lastOnTimestamp;
        }
    }

    @Override
    public long getOnDurationSeconds() {
        long duration = totalOnDuration;

        if (isOn && lastOnTimestamp > 0) {
            duration += System.currentTimeMillis() - lastOnTimestamp;
        }

        return duration / 1000;
    }

    // -----------------------------------------------------------
    // EnergyConsumer Interface
    // -----------------------------------------------------------
    @Override
    public double getPowerRating() {
        return powerRating;
    }

    public void setPowerRating(double rating) {
        this.powerRating = rating;
    }

    @Override
    public double getEnergyConsumption() {
        // energy already consumed (kWh)
        return (powerRating * getOnDurationSeconds()) / (1000.0 * 3600.0);
    }

    @Override
    public double calculateEnergyUsage(double hours) {
        return (powerRating / 1000.0) * hours;
    }

    // -----------------------------------------------------------
    // GETTERS / SETTERS
    // -----------------------------------------------------------
    public String getDeviceId() { return deviceId; }
    public String getDeviceName() { return deviceName; }
    public String getLocation() { return location; }
    public boolean isOn() { return isOn; }

    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString() {
        return deviceName + " (" + getDeviceType() + ") - " + (isOn ? "ON" : "OFF");
    }
}
