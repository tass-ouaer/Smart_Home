package org.example.backend.automation;

import org.example.backend.home.Home;
import org.example.backend.home.Room;
import org.example.backend.devices.SmartDevice;
import org.example.backend.devices.Thermostat;

public class TemperatureRule extends AutomationRule {

    private double minTemp;
    private double maxTemp;
    private String roomName; // optional: limit to specific room

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

        for (Room room : home.getRooms()) {
            // Skip room if specified
            if (roomName != null && !room.getRoomName().equalsIgnoreCase(roomName)) continue;

            for (SmartDevice device : room.getDevices()) {
                if (device instanceof Thermostat thermostat) {
                    double currentTemp = thermostat.getCurrentTemperature();

                    if (currentTemp < minTemp) {
                        thermostat.turnOn();
                        thermostat.setTargetTemperature(minTemp);
                        System.out.println(ruleName + ": Heating " + thermostat.getDeviceName() + " to " + minTemp + "°C");
                    } else if (currentTemp > maxTemp) {
                        thermostat.turnOn();
                        thermostat.setTargetTemperature(maxTemp);
                        System.out.println(ruleName + ": Cooling " + thermostat.getDeviceName() + " to " + maxTemp + "°C");
                    }
                }
            }
        }
    }
}
