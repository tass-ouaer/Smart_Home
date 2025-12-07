package org.example.backend.devices;

import org.example.backend.interfaces.Controllable;
import org.example.backend.interfaces.EnergyConsumer;

public abstract class SmartDevice implements Controllable, EnergyConsumer {

    protected String deviceId;
    protected String deviceName;
    protected String location;
    protected boolean isOn;
    protected double energyConsumption;

    // NEW FIELD REQUIRED BY THE INTERFACE
    protected double powerRating; // in watts

    private long lastOnTimestamp;
    private long totalOnDuration;

    public SmartDevice(String deviceId, String deviceName, String location) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.location = location;
        this.isOn = false;
        this.energyConsumption = 0.0;
        this.powerRating = 0.0;   // default value
        this.lastOnTimestamp = 0;
        this.totalOnDuration = 0;
    }

    // ABSTRACT METHODS
    public abstract void turnOn();
    public abstract void turnOff();
    public abstract String getDeviceType();
    public abstract String getStatus();

    // TIME TRACKING
    protected void startTimeTracking() {
        if (!isOn) {
            lastOnTimestamp = System.currentTimeMillis();
        }
    }

    protected void stopTimeTracking() {
        if (isOn && lastOnTimestamp > 0) {
            long now = System.currentTimeMillis();
            totalOnDuration += now - lastOnTimestamp;
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

    public double getOnDurationHours() {
        return getOnDurationSeconds() / 3600.0;
    }

    public void resetTimeTracking() {
        totalOnDuration = 0;
        if (isOn) {
            lastOnTimestamp = System.currentTimeMillis();
        } else {
            lastOnTimestamp = 0;
        }
    }

    public void toggle() {
        if (isOn) {
            turnOff();
        } else {
            turnOn();
        }
    }

    // ENERGY-CONSUMER INTERFACE IMPLEMENTATION
    @Override
    public double getPowerRating() {
        return powerRating;
    }

    public void setPowerRating(double powerRating) {
        this.powerRating = powerRating;
    }

    @Override
    public double getEnergyConsumption() {
        return energyConsumption;
    }

    @Override
    public double calculateEnergyUsage(double hours) {
        return isOn ? energyConsumption * hours : 0.0;
    }

    public double calculateActualEnergyUsage() {
        double hoursOn = getOnDurationHours();
        return energyConsumption * hoursOn;
    }

    // GETTERS / SETTERS
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    @Override
    public String toString() {
        return String.format(
                "%s [%s] - Location: %s, Status: %s, Energy: %.2f kWh, Power: %.2f W",
                deviceName, getDeviceType(), location, isOn ? "ON" : "OFF",
                energyConsumption, powerRating
        );
    }
}
