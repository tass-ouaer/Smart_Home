package org.example.backend.automation;

import org.example.backend.home.Home;
import org.example.backend.devices.MotionSensor;
import org.example.backend.devices.Light;
import java.util.List;
import java.util.ArrayList;

public class MotionRule extends AutomationRule {
    private String motionSensorName;
    private List<String> lightDeviceNames;
    private boolean turnOnWhenMotion;
    private int delaySeconds; // For turning off after motion stops
    
    public MotionRule(String ruleName, String motionSensorName, 
                     List<String> lightDeviceNames, boolean turnOnWhenMotion) {
        super(ruleName);
        this.motionSensorName = motionSensorName;
        this.lightDeviceNames = new ArrayList<>(lightDeviceNames);
        this.turnOnWhenMotion = turnOnWhenMotion;
        this.delaySeconds = 60; // Default 60 seconds delay
    }
    
    public MotionRule(String ruleName, String motionSensorName, 
                     String lightDeviceName, boolean turnOnWhenMotion) {
        this(ruleName, motionSensorName, List.of(lightDeviceName), turnOnWhenMotion);
    }
    
    @Override
    public void apply(Home home) {
        if (!isActive) return;
        
        // Find motion sensor
        MotionSensor sensor = findMotionSensor(home);
        if (sensor == null) {
            System.out.println("MotionRule ERROR: Sensor '" + motionSensorName + "' not found!");
            return;
        }
        
        boolean motionDetected = sensor.isMotionDetected();
        
        // Find and control lights
        List<Light> lights = findLights(home);
        
        if (turnOnWhenMotion && motionDetected) {
            turnLights(lights, true);
        } 
        else if (!turnOnWhenMotion && !motionDetected) {
            // In real implementation, you'd use a timer/scheduler
            // For now, just turn off immediately
            turnLights(lights, false);
        }
    }
    
    private MotionSensor findMotionSensor(Home home) {
        for (var device : home.getAllDevices()) {
            if (device instanceof MotionSensor && 
                device.getDeviceName().equals(motionSensorName)) {
                return (MotionSensor) device;
            }
        }
        return null;
    }
    
    private List<Light> findLights(Home home) {
        List<Light> lights = new ArrayList<>();
        for (var device : home.getAllDevices()) {
            if (device instanceof Light && 
                lightDeviceNames.contains(device.getDeviceName())) {
                lights.add((Light) device);
            }
        }
        return lights;
    }
    
    private void turnLights(List<Light> lights, boolean turnOn) {
        for (Light light : lights) {
            if (turnOn) {
                light.turnOn();
                System.out.println("MotionRule → Turned ON " + light.getDeviceName());
            } else {
                light.turnOff();
                System.out.println("MotionRule → Turned OFF " + light.getDeviceName());
            }
        }
    }
    
    // Getters and setters
    public String getMotionSensorName() { return motionSensorName; }
    public List<String> getLightDeviceNames() { return new ArrayList<>(lightDeviceNames); }
    public boolean isTurnOnWhenMotion() { return turnOnWhenMotion; }
    public int getDelaySeconds() { return delaySeconds; }
    public void setDelaySeconds(int delaySeconds) { 
        this.delaySeconds = delaySeconds; 
    }
}