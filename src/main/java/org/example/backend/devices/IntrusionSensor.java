package org.example.backend.devices;

import org.example.backend.interfaces.SensorDevice;

public class IntrusionSensor extends SmartDevice implements SensorDevice {

    private boolean intrusion;

    public IntrusionSensor(String deviceId, String name) {
        super(deviceId, name);
        this.intrusion = false;
    }

    public void setIntrusion(boolean intrusion) {
        this.intrusion = intrusion;
    }

    @Override
    public String readValue() {
        return intrusion ? "INTRUSION" : "NORMAL";
    }
}
