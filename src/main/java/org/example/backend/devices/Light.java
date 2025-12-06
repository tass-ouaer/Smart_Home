package org.example.backend.devices;

import org.example.backend.interfaces.Schedulable;

public class Light extends SmartDevice implements Schedulable {

    private int brightness;
    private String scheduledTime;

    public Light(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);
        this.brightness = 100;
        this.energyConsumption = 0.06;
        this.scheduledTime = null;
    }

    @Override
    public void turnOn() {
        this.isOn = true;
        startTimeTracking();
        System.out.println(deviceName + " is now ON (Brightness: " + brightness + "%)");
    }

    @Override
    public void turnOff() {
        stopTimeTracking();
        this.isOn = false;
        System.out.println(deviceName + " is now OFF");
    }

    @Override
    public String getDeviceType() {
        return "Light";
    }

    @Override
    public String getStatus() {
        if (isOn) {
            return "ON - Brightness: " + brightness + "%";
        } else {
            return "OFF";
        }
    }

    @Override
    public void schedule(String time) {
        this.scheduledTime = time;
        System.out.println(deviceName + " scheduled to turn on at " + time);
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setBrightness(int level) {
        if (level < 0) level = 0;
        if (level > 100) level = 100;

        this.brightness = level;

        if (level == 0) {
            turnOff();
        } else if (!isOn) {
            turnOn();
        } else {
            System.out.println(deviceName + " brightness set to " + brightness + "%");
        }
    }

    public int getBrightness() {
        return brightness;
    }

    public void dim(int amount) {
        setBrightness(brightness - amount);
    }

    public void brighten(int amount) {
        setBrightness(brightness + amount);
    }

    @Override
    public double calculateEnergyUsage(double hours) {
        if (!isOn) return 0.0;
        return energyConsumption * hours * (brightness / 100.0);
    }

    @Override
    public double calculateActualEnergyUsage() {
        double hoursOn = getOnDurationHours();
        return energyConsumption * hoursOn * (brightness / 100.0);
    }
}