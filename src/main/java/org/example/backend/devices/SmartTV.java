package org.example.backend.devices;

import org.example.backend.interfaces.Controllable;
import org.example.backend.interfaces.EnergyConsumer;

public class SmartTV extends SmartDevice implements Controllable, EnergyConsumer {

    private double energyUsagePerHour = 0.12;

    public SmartTV(String deviceId, String name) {
        super(deviceId, name);
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
        return super.getStatus();
    }

    @Override
    public double getEnergyUsage() {
        return energyUsagePerHour;
    }
}
