package org.example.backend.devices;

import org.example.backend.interfaces.Schedulable;

public class Sprinkler extends SmartDevice implements Schedulable {

    private int waterFlowRate;
    private int duration;
    private String scheduledTime;
    private boolean isWatering;

    public Sprinkler(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);
        this.waterFlowRate = 10;
        this.duration = 30;
        this.scheduledTime = null;
        this.isWatering = false;
        this.energyConsumption = 0.75;
    }

    @Override
    public void turnOn() {
        this.isOn = true;
        this.isWatering = true;
        startTimeTracking();
        System.out.println(deviceName + " started watering - Duration: " + duration + " minutes");
    }

    @Override
    public void turnOff() {
        stopTimeTracking();
        this.isOn = false;
        this.isWatering = false;
        System.out.println(deviceName + " stopped watering");
    }

    @Override
    public String getDeviceType() {
        return "Sprinkler";
    }

    @Override
    public String getStatus() {
        if (isWatering) {
            return String.format("WATERING - %d minutes, %d L/min", duration, waterFlowRate);
        } else {
            return "OFF - Not watering";
        }
    }

    @Override
    public void schedule(String time) {
        this.scheduledTime = time;
        System.out.println(deviceName + " scheduled to water at " + time + " for " + duration + " minutes");
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void startWatering(int minutes) {
        if (minutes > 0) {
            this.duration = minutes;
        }
        turnOn();
    }

    public void stopWatering() {
        turnOff();
    }

    public void setDuration(int minutes) {
        if (minutes >= 1 && minutes <= 120) {
            this.duration = minutes;
            System.out.println(deviceName + " watering duration set to " + minutes + " minutes");
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setWaterFlowRate(int litersPerMinute) {
        if (litersPerMinute >= 1 && litersPerMinute <= 50) {
            this.waterFlowRate = litersPerMinute;
            System.out.println(deviceName + " flow rate set to " + litersPerMinute + " L/min");
        }
    }

    public int getWaterFlowRate() {
        return waterFlowRate;
    }

    public double calculateWaterUsage() {
        return waterFlowRate * duration;
    }

    public boolean isWatering() {
        return isWatering;
    }

    public void testSpray() {
        System.out.println("Testing " + deviceName + " - 1 minute test spray");
        int originalDuration = this.duration;
        this.duration = 1;
        turnOn();
        this.duration = originalDuration;
    }
}