package org.example.backend.automation;

import org.example.backend.controller.CentralController;
import org.example.backend.devices.Thermostat;
import org.example.backend.devices.SmartDevice;

public class TemperatureRule extends AutomationRule {

    private double minTemp;
    private double maxTemp;

    public TemperatureRule(String ruleName, double minTemp, double maxTemp) {
        super(ruleName);
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    @Override
    public void apply(CentralController controller) {
        if (!isActive) return;

        for (SmartDevice device : controller.findDevicesByType(Thermostat.class)) {
            Thermostat thermostat = (Thermostat) device;
            double current = thermostat.getCurrentTemperature();

            if (current < minTemp) {
                thermostat.turnOn();
                thermostat.setTargetTemperature(minTemp);
                thermostat.setMode("HEAT");
            } else if (current > maxTemp) {
                thermostat.turnOn();
                thermostat.setTargetTemperature(maxTemp);
                thermostat.setMode("COOL");
            }
        }
    }
}
