package org.example.backend.devices;

import org.example.backend.interfaces.Controllable;
import org.example.backend.interfaces.EnergyConsumer;

public class Light extends SmartDevice implements Controllable, EnergyConsumer {

    // simple fixed energy usage per hour
    private double energyUsagePerHour = 0.06;

    public Light(String deviceId, String name) {
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
