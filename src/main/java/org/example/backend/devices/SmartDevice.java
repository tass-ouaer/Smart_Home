package org.example.backend.devices;

import org.example.backend.home.Room;

public abstract class SmartDevice {
    // Basic device attributes
    protected String deviceId;
    protected String deviceName;
    protected String location; // Room where the device is located
    protected boolean isOn;
    protected double energyConsumption; // in kWh

    // Constructor
    public SmartDevice(String deviceId, String deviceName, String location) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.location = location;
        this.isOn = false;
        this.energyConsumption = 0.0;
    }

    // Abstract methods that must be implemented by subclasses
    public abstract void turnOn();
    public abstract void turnOff();
    public abstract String getDeviceType();
    public abstract String getStatus();

    // Concrete methods
    public void toggle() {
        if (isOn) {
            turnOff();
        } else {
            turnOn();
        }
    }

    public double calculateEnergyUsage(double hours) {
        return isOn ? energyConsumption * hours : 0.0;
    }

    // Getters and Setters
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

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - Location: %s, Status: %s, Energy: %.2f kWh",
                deviceName, getDeviceType(), location, isOn ? "ON" : "OFF", energyConsumption);
    }
}

