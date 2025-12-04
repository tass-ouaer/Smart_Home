package org.example.backend.devices;

import org.example.backend.home.Room;
import org.example.backend.interfaces.Controllable;
import org.example.backend.interfaces.EnergyConsumer;

public abstract class SmartDevice implements Controllable, EnergyConsumer {
    // Basic device attributes
    protected String deviceId;
    protected String deviceName;
    protected String location;        // Room name or description
    protected boolean isOn;
    protected double energyConsumption; // base consumption in kWh
    protected Room room;              // optional link to Room object

    // Constructor
    public SmartDevice(String deviceId, String deviceName, String location) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.location = location;
        this.isOn = false;
        this.energyConsumption = 0.0;
        this.room = null;
    }

    // ----- Abstract methods to be implemented by subclasses -----
    @Override
    public abstract void turnOn();

    @Override
    public abstract void turnOff();

    public abstract String getDeviceType();

    // ----- Common behaviour -----
    @Override
    public String getStatus() {
        return isOn ? "ON" : "OFF";
    }

    @Override
    public double getEnergyConsumption() {
        return energyConsumption;
    }

    /**
     * Calculate energy usage for a given number of hours.
     */
    public double calculateEnergyUsage(double hours) {
        return isOn ? energyConsumption * hours : 0.0;
    }

    public void toggle() {
        if (isOn) {
            turnOff();
        } else {
            turnOn();
        }
    }

    // ----- Getters / setters -----
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return String.format(
                "%s [%s] - Location: %s, Status: %s, Energy: %.2f kWh",
                deviceName,
                getDeviceType(),
                location,
                isOn ? "ON" : "OFF",
                energyConsumption
        );
    }
}
