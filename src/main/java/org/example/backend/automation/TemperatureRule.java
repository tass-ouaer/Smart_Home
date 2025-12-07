package org.example.backend.automation;

import org.example.backend.home.Home;
import org.example.backend.devices.Thermostat;

public class TemperatureRule extends AutomationRule {
    private double minTemp;
    private double maxTemp;
    private String roomName; // Optional: apply only to specific room
    
    public TemperatureRule(String ruleName, double minTemp, double maxTemp) {
        super(ruleName);
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.roomName = null;
    }
    
    public TemperatureRule(String ruleName, double minTemp, double maxTemp, String roomName) {
        this(ruleName, minTemp, maxTemp);
        this.roomName = roomName;
    }
    
    @Override
    public void apply(Home home) {
        if (!isActive) return;
        
        for (var device : home.getAllDevices()) {
            if (device instanceof Thermostat) {
                Thermostat thermostat = (Thermostat) device;
                
                // Check room filter
                if (roomName != null && !roomName.equals(thermostat.getLocation())) {
                    continue;
                }
                
                double currentTemp = thermostat.getCurrentTemperature();
                
                if (currentTemp < minTemp) {
                    thermostat.turnOn();
                    thermostat.setTargetTemperature(minTemp);
                    thermostat.setMode("HEAT");
                    System.out.println("TemperatureRule → Heating " + thermostat.getDeviceName() + 
                                     " to " + minTemp + "°C (was " + currentTemp + "°C)");
                } 
                else if (currentTemp > maxTemp) {
                    thermostat.turnOn();
                    thermostat.setTargetTemperature(maxTemp);
                    thermostat.setMode("COOL");
                    System.out.println("TemperatureRule → Cooling " + thermostat.getDeviceName() + 
                                     " to " + maxTemp + "°C (was " + currentTemp + "°C)");
                }
            }
        }
    }
    
    // Getters
    public double getMinTemp() { return minTemp; }
    public double getMaxTemp() { return maxTemp; }
    public String getRoomName() { return roomName; }
}