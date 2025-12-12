package org.example.backend.devices;

import org.example.backend.interfaces.Schedulable;

public class Thermostat extends SmartDevice implements Schedulable {

    private double currentTemperature;
    private double targetTemperature;
    private String mode;             // HEAT | COOL | AUTO | OFF
    private String scheduledTime;

    public Thermostat(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);

        this.currentTemperature = 22.0;
        this.targetTemperature = 22.0;
        this.mode = "OFF";
        this.scheduledTime = null;

        // Thermostat control circuitry consumes ~5W
        this.setPowerRating(5.0);
    }

    // ------------------------------------------------------------------
    // POWER STATE
    // ------------------------------------------------------------------
    @Override
    public void turnOn() {
        if (!isOn) {
            this.isOn = true;
            startTimeTracking();
        }

        if (mode.equals("OFF")) {
            this.mode = "AUTO";
        }

        System.out.println(deviceName + " is now ON - Target: " + targetTemperature + "°C, Mode: " + mode);
    }

    @Override
    public void turnOff() {
        if (isOn) stopTimeTracking();
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
        if (!isOn) {
            return String.format("OFF - Current: %.1f°C", currentTemperature);
        }

        String action = getHeatingCoolingAction();
        return String.format("%s - Current: %.1f°C, Target: %.1f°C (%s)",
                mode, currentTemperature, targetTemperature, action);
    }

    private String getHeatingCoolingAction() {
        if (currentTemperature < targetTemperature - 0.5) return "Heating";
        if (currentTemperature > targetTemperature + 0.5) return "Cooling";
        return "Maintaining";
    }

    // ------------------------------------------------------------------
    // SCHEDULING
    // ------------------------------------------------------------------
    @Override
    public void schedule(String time) {
        this.scheduledTime = time;
        System.out.println(deviceName + " scheduled to adjust at " + time);
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    // ------------------------------------------------------------------
    // TEMPERATURE LOGIC
    // ------------------------------------------------------------------
    public void setTargetTemperature(double temperature) {
        if (temperature < 10.0 || temperature > 35.0) {
            System.out.println("Invalid temperature - Must be between 10°C and 35°C");
            return;
        }

        this.targetTemperature = temperature;
        System.out.println(deviceName + " target temperature set to " + targetTemperature + "°C");

        if (isOn) adjustTemperature();
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

        if (isOn) adjustTemperature();
    }

    public void setMode(String mode) {
        String upper = mode.toUpperCase();

        if (!upper.equals("HEAT") && !upper.equals("COOL") &&
                !upper.equals("AUTO") && !upper.equals("OFF")) {
            System.out.println("Invalid mode - Use HEAT, COOL, AUTO, or OFF");
            return;
        }

        this.mode = upper;
        System.out.println(deviceName + " mode set to " + this.mode);

        if (upper.equals("OFF")) {
            turnOff();
        } else if (!isOn) {
            turnOn();
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

        String action = getHeatingCoolingAction();

        if (action.equals("Heating") && (mode.equals("HEAT") || mode.equals("AUTO"))) {
            System.out.println(deviceName + " is HEATING");
        } else if (action.equals("Cooling") && (mode.equals("COOL") || mode.equals("AUTO"))) {
            System.out.println(deviceName + " is COOLING");
        } else {
            System.out.println(deviceName + " maintaining temperature");
        }
    }

    // ------------------------------------------------------------------
    // ENERGY CONSUMPTION
    // ------------------------------------------------------------------
    @Override
    public double getEnergyConsumption() {
        if (!isOn) return 0.0;

        double basePower = getPowerRating(); // thermostat electronics

        // Additional power depends on mode
        double hvacPower = 0.0;

        String action = getHeatingCoolingAction();

        if (action.equals("Heating") && (mode.equals("HEAT") || mode.equals("AUTO"))) {
            hvacPower = 2000.0;        // Heater ~2 kW
        } else if (action.equals("Cooling") && (mode.equals("COOL") || mode.equals("AUTO"))) {
            hvacPower = 1500.0;        // AC ~1.5 kW
        }

        double effectivePower = basePower + hvacPower;

        return (effectivePower * getOnDurationSeconds()) / (1000.0 * 3600.0);
    }

    @Override
    public double calculateEnergyUsage(double hours) {
        double basePower = getPowerRating();
        double hvacPower = 0.0;

        String action = getHeatingCoolingAction();

        if (action.equals("Heating") && (mode.equals("HEAT") || mode.equals("AUTO"))) {
            hvacPower = 2000.0;
        } else if (action.equals("Cooling") && (mode.equals("COOL") || mode.equals("AUTO"))) {
            hvacPower = 1500.0;
        }

        return ((basePower + hvacPower) / 1000.0) * hours;
    }
}
