package org.example.backend.devices;

import org.example.backend.interfaces.Controllable;
import org.example.backend.interfaces.EnergyConsumer;
import org.example.backend.interfaces.Schedulable;

public class Sprinkler extends SmartDevice implements Controllable, EnergyConsumer, Schedulable {

    private double energyUsagePerHour = 0.08;
    private String scheduleTime;

    public Sprinkler(String deviceId, String name) {
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

    @Override
    public void schedule(String time) {
        this.scheduleTime = time;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }
}
