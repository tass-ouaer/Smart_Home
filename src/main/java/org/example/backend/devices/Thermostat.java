package org.example.backend.devices;

import org.example.backend.interfaces.Controllable;
import org.example.backend.interfaces.EnergyConsumer;

public class Thermostat extends SmartDevice implements Controllable, EnergyConsumer {

    private double targetTemperature;
    private double energyUsagePerHour = 0.20;

    public Thermostat(String deviceId, String name, double targetTemperature) {
        super(deviceId, name);
        this.targetTemperature = targetTemperature;
    }

    public void setTargetTemperature(double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public double getTargetTemperature() {
        return targetTemperature;
    }

    @Override
    public void turnOn() {
        super.turnOn();
    }

    @Override
    public void turnOff() {
        super.turnOff();
    }

    @Override
    public String getStatus() {
        return super.getStatus() + " @ " + targetTemperature + "°C";
    }

    @Override
    public double getEnergyUsage() {
        return energyUsagePerHour;
    }
}
