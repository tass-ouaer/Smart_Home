package org.example.backend.devices;

import org.example.backend.interfaces.Schedulable;

public class Light extends SmartDevice implements Schedulable {

    private int brightness;       // 0–100%
    private String scheduledTime;

    public Light(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);

        this.brightness = 100;
        this.scheduledTime = null;

        // Typical LED light bulb uses ~10 watts at 100% brightness
        this.setPowerRating(10.0);
    }

    @Override
    public void turnOn() {
        if (!isOn) {
            this.isOn = true;
            startTimeTracking();
        }
        System.out.println(deviceName + " is now ON (Brightness: " + brightness + "%)");
    }

    @Override
    public void turnOff() {
        if (isOn) {
            stopTimeTracking();
            this.isOn = false;
        }
        System.out.println(deviceName + " is now OFF");
    }

    @Override
    public String getDeviceType() {
        return "Light";
    }

    @Override
    public String getStatus() {
        return isOn ? "ON - Brightness: " + brightness + "%" : "OFF";
    }

    @Override
    public void schedule(String time) {
        this.scheduledTime = time;
        System.out.println(deviceName + " scheduled to turn on at " + time);
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    // -----------------------------------------------------------
    // BRIGHTNESS CONTROL
    // -----------------------------------------------------------
    public void setBrightness(int level) {
        if (level < 0) level = 0;
        if (level > 100) level = 100;

        this.brightness = level;

        if (brightness == 0) {
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

    // -----------------------------------------------------------
    // ENERGY MODEL → brightness reduces energy usage
    // -----------------------------------------------------------
    @Override
    public double getEnergyConsumption() {
        // Compute kWh based on brightness
        double brightnessFactor = brightness / 100.0;
        double effectivePower = getPowerRating() * brightnessFactor;

        // Convert W × seconds → kWh
        return (effectivePower * getOnDurationSeconds()) / (1000.0 * 3600.0);
    }

    @Override
    public double calculateEnergyUsage(double hours) {
        double brightnessFactor = brightness / 100.0;
        double effectivePower = getPowerRating() * brightnessFactor;
        return (effectivePower / 1000.0) * hours;
    }
}
