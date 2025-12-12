package org.example.backend.devices;

import org.example.backend.interfaces.SensorDevice;

public class MotionSensor extends SensorDevice {

    private boolean motionDetected;
    private long lastMotionTime;
    private int detectionRange;     // meters
    private boolean isActive;       // monitoring enabled or not

    public MotionSensor(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);

        this.motionDetected = false;
        this.lastMotionTime = 0;
        this.detectionRange = 5;
        this.isActive = true;

        // Typical PIR motion sensor: 0.8 watts
        this.setPowerRating(0.8);

        // Motion sensor is ON by default
        this.isOn = true;
        startTimeTracking();
    }

    @Override
    public void turnOn() {
        if (!isOn) {
            this.isOn = true;
            startTimeTracking();
        }
        this.isActive = true;

        System.out.println(deviceName + " is now ACTIVE - Motion monitoring enabled");
    }

    @Override
    public void turnOff() {
        if (isOn) {
            stopTimeTracking();
        }
        this.isOn = false;
        this.isActive = false;
        this.motionDetected = false;

        System.out.println(deviceName + " is now INACTIVE - Motion monitoring disabled");
    }

    @Override
    public String getDeviceType() {
        return "MotionSensor";
    }

    @Override
    public String getStatus() {
        if (!isOn || !isActive) {
            return "INACTIVE - Not monitoring";
        }

        if (motionDetected) {
            return "MOTION DETECTED - Activity in " + location;
        }

        if (lastMotionTime == 0) {
            return "NO MOTION - Monitoring active";
        }

        long secondsAgo = (System.currentTimeMillis() - lastMotionTime) / 1000;
        return "NO MOTION - Last detected " + secondsAgo + "s ago";
    }

    @Override
    public String readValue() {
        if (!isOn || !isActive) return "INACTIVE";
        return motionDetected ? "MOTION" : "NO_MOTION";
    }

    // -----------------------------------------------------------
    // MOTION DETECTION LOGIC
    // -----------------------------------------------------------

    public void detectMotion() {
        if (!isOn || !isActive) return;

        this.motionDetected = true;
        this.lastMotionTime = System.currentTimeMillis();

        System.out.println("MOTION DETECTED - " + deviceName + " at " + location);
    }

    public void clearMotion() {
        this.motionDetected = false;
        System.out.println("Motion cleared - " + deviceName);
    }

    // -----------------------------------------------------------
    // GETTERS / SETTERS
    // -----------------------------------------------------------

    public boolean isMotionDetected() {
        return motionDetected;
    }

    public long getLastMotionTime() {
        return lastMotionTime;
    }

    public long getSecondsSinceLastMotion() {
        if (lastMotionTime == 0) return -1;
        if (motionDetected) return 0;
        return (System.currentTimeMillis() - lastMotionTime) / 1000;
    }

    public void setDetectionRange(int range) {
        if (range >= 1 && range <= 10) {
            this.detectionRange = range;
            System.out.println(deviceName + " detection range set to " + range + " meters");
        }
    }

    public int getDetectionRange() {
        return detectionRange;
    }

    public void activate() {
        if (!isOn) {
            System.out.println("Cannot activate - Sensor is powered off");
            return;
        }
        this.isActive = true;
        System.out.println(deviceName + " monitoring activated");
    }

    public void deactivate() {
        this.isActive = false;
        this.motionDetected = false;
        System.out.println(deviceName + " monitoring deactivated");
    }

    public boolean isActive() {
        return isActive;
    }
}
