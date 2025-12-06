package org.example.backend.devices;

import org.example.backend.interfaces.Schedulable;

public class Thermostat extends SmartDevice implements Schedulable {

    private double currentTemperature;
    private double targetTemperature;
    private String mode;
    private String scheduledTime;

    public Thermostat(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);
        this.currentTemperature = 22.0;
        this.targetTemperature = 22.0;
        this.mode = "OFF";
        this.scheduledTime = null;
        this.energyConsumption = 1.5;
    }

    @Override
    public void turnOn() {
        this.isOn = true;
        startTimeTracking();
        if (mode.equals("OFF")) {
            this.mode = "AUTO";
        }
        System.out.println(deviceName + " is now ON - Target: " + targetTemperature + "°C, Mode: " + mode);
    }

    @Override
    public void turnOff() {
        stopTimeTracking();
        this.isOn = false;
        this.mode = "OFF";
        System.out.println(deviceName + " is now OFF");
    }

    @Override
    public String getDeviceType() {
        return "Thermostat";
    }

    @Override
    public String getStatus() {
        if (isOn) {
            String action = getHeatingCoolingAction();
            return String.format("%s - Current: %.1f°C, Target: %.1f°C (%s)",
                    mode, currentTemperature, targetTemperature, action);
        } else {
            return String.format("OFF - Current: %.1f°C", currentTemperature);
        }
    }

    private String getHeatingCoolingAction() {
        if (currentTemperature < targetTemperature - 0.5) {
            return "Heating";
        } else if (currentTemperature > targetTemperature + 0.5) {
            return "Cooling";
        } else {
            return "Maintaining";
        }
    }

    @Override
    public void schedule(String time) {
        this.scheduledTime = time;
        System.out.println(deviceName + " scheduled to adjust at " + time);
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setTargetTemperature(double temperature) {
        if (temperature >= 10.0 && temperature <= 35.0) {
            this.targetTemperature = temperature;
            System.out.println(deviceName + " target temperature set to " + targetTemperature + "°C");

            if (isOn) {
                adjustTemperature();
            }
        } else {
            System.out.println("Invalid temperature - Must be between 10°C and 35°C");
        }
    }

    public double getTargetTemperature() {
        return targetTemperature;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public void updateCurrentTemperature(double temperature) {
        this.currentTemperature = temperature;
        System.out.println(deviceName + " current temperature: " + currentTemperature + "°C");
    }

    public void setMode(String mode) {
        String upperMode = mode.toUpperCase();
        if (upperMode.equals("HEAT") || upperMode.equals("COOL") ||
                upperMode.equals("AUTO") || upperMode.equals("OFF")) {

            this.mode = upperMode;
            System.out.println(deviceName + " mode set to " + this.mode);

            if (upperMode.equals("OFF")) {
                turnOff();
            } else if (!isOn) {
                turnOn();
            }
        } else {
            System.out.println("Invalid mode - Use HEAT, COOL, AUTO, or OFF");
        }
    }

    public String getMode() {
        return mode;
    }

    public void increaseTemperature() {
        setTargetTemperature(targetTemperature + 1.0);
    }

    public void decreaseTemperature() {
        setTargetTemperature(targetTemperature - 1.0);
    }

    private void adjustTemperature() {
        if (!isOn) return;

        if (currentTemperature < targetTemperature - 0.5) {
            if (mode.equals("HEAT") || mode.equals("AUTO")) {
                System.out.println(deviceName + " is HEATING");
            }
        } else if (currentTemperature > targetTemperature + 0.5) {
            if (mode.equals("COOL") || mode.equals("AUTO")) {
                System.out.println(deviceName + " is COOLING");
            }
        } else {
            System.out.println(deviceName + " maintaining temperature");
        }
    }

    @Override
    public double calculateEnergyUsage(double hours) {
        if (!isOn) return 0.0;

        double tempDiff = Math.abs(currentTemperature - targetTemperature);
        double usageFactor = 1.0 + (tempDiff / 10.0);

        return energyConsumption * hours * usageFactor;
    }

    @Override
    public double calculateActualEnergyUsage() {
        if (!isOn) return 0.0;

        double hoursOn = getOnDurationHours();
        double tempDiff = Math.abs(currentTemperature - targetTemperature);
        double usageFactor = 1.0 + (tempDiff / 10.0);

        return energyConsumption * hoursOn * usageFactor;
    }
}