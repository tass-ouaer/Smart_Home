package org.example.backend.devices;

import org.example.backend.interfaces.SensorDevice;

public class GasDetector extends SensorDevice {

    private double gasLevel;
    private double alertThreshold;
    private boolean alertTriggered;

    public GasDetector(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);
        this.gasLevel = 0.0;
        this.alertThreshold = 50.0;
        this.alertTriggered = false;
        this.energyConsumption = 0.002;
        this.isOn = true;
    }

    @Override
    public void turnOn() {
        this.isOn = true;
        startTimeTracking();
        System.out.println(deviceName + " is now ACTIVE - Monitoring air quality");
    }

    @Override
    public void turnOff() {
        stopTimeTracking();
        this.isOn = false;
        this.alertTriggered = false;
        System.out.println(deviceName + " is now INACTIVE - Monitoring stopped");
    }

    @Override
    public String getDeviceType() {
        return "GasDetector";
    }

    @Override
    public String getStatus() {
        if (!isOn) {
            return "INACTIVE - Not monitoring";
        }

        if (alertTriggered) {
            return String.format("ALERT! Dangerous gas level detected: %.1f%%", gasLevel);
        } else if (gasLevel > alertThreshold * 0.7) {
            return String.format("WARNING - Elevated gas level: %.1f%%", gasLevel);
        } else {
            return String.format("SAFE - Gas level normal: %.1f%%", gasLevel);
        }
    }

    @Override
    public String readValue() {
        if (!isOn) return "INACTIVE";

        if (alertTriggered) {
            return "ALERT";
        } else if (gasLevel > alertThreshold * 0.7) {
            return "WARNING";
        } else {
            return "SAFE";
        }
    }

    public void updateGasLevel(double level) {
        if (level < 0) level = 0;
        if (level > 100) level = 100;

        this.gasLevel = level;

        if (level >= alertThreshold) {
            triggerAlert();
        } else {
            this.alertTriggered = false;
        }
    }

    private void triggerAlert() {
        if (!alertTriggered) {
            this.alertTriggered = true;
            System.out.println("GAS ALERT - " + deviceName + " detected dangerous levels!");
            System.out.println("Gas level: " + gasLevel + "% (Threshold: " + alertThreshold + "%)");
        }
    }

    public double getGasLevel() {
        return gasLevel;
    }

    public void setAlertThreshold(double threshold) {
        if (threshold >= 0 && threshold <= 100) {
            this.alertThreshold = threshold;
            System.out.println(deviceName + " alert threshold set to: " + threshold + "%");
        }
    }

    public double getAlertThreshold() {
        return alertThreshold;
    }

    public boolean isAlertTriggered() {
        return alertTriggered;
    }

    public void resetAlert() {
        this.alertTriggered = false;
        System.out.println(deviceName + " alert has been reset");
    }

    public boolean performSelfTest() {
        if (!isOn) {
            System.out.println("Self-test failed - Detector is OFF");
            return false;
        }

        System.out.println("Running self-test on " + deviceName + "...");
        System.out.println("Self-test passed - Sensor is operational");
        return true;
    }
}