package org.example.backend.devices;

import org.example.backend.interfaces.Schedulable;

public class Sprinkler extends SmartDevice implements Schedulable {

    private int waterFlowRate;  // L/min
    private int duration;       // minutes
    private String scheduledTime;
    private boolean isWatering;

    public Sprinkler(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);

        this.waterFlowRate = 10;
        this.duration = 30;
        this.scheduledTime = null;
        this.isWatering = false;

        // Typical garden sprinkler motor consumes 20–50 watts
        this.setPowerRating(40.0);  // 40W is a realistic default
    }

    // -----------------------------------------------------------
    // POWER / WATERING CONTROL
    // -----------------------------------------------------------
    @Override
    public void turnOn() {
        if (!isOn) {
            this.isOn = true;
            startTimeTracking();
        }

        this.isWatering = true;
        System.out.println(deviceName + " started watering - Duration: " + duration + " minutes");
    }

    @Override
    public void turnOff() {
        if (isOn) stopTimeTracking();
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
        }
        return "OFF - Not watering";
    }

    // -----------------------------------------------------------
    // SCHEDULING
    // -----------------------------------------------------------
    @Override
    public void schedule(String time) {
        this.scheduledTime = time;
        System.out.println(deviceName + " scheduled to water at " + time + " for " + duration + " minutes");
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    // -----------------------------------------------------------
    // WATERING LOGIC
    // -----------------------------------------------------------
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

    // -----------------------------------------------------------
    // WATER USAGE
    // -----------------------------------------------------------
    public double calculateWaterUsage() {
        return waterFlowRate * duration; // Liters used
    }

    public boolean isWatering() {
        return isWatering;
    }

    // -----------------------------------------------------------
    // TEST MODE
    // -----------------------------------------------------------
    public void testSpray() {
        System.out.println("Testing " + deviceName + " - 1 minute test spray");

        int originalDuration = this.duration;

        this.duration = 1;
        turnOn();

        // Restore original default duration
        this.duration = originalDuration;
    }
}
