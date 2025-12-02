package org.example.backend.devices;

import org.example.backend.interfaces.SensorDevice;

public class GasDetector extends SmartDevice implements SensorDevice {

    private boolean gasDetected;

    public GasDetector(String deviceId, String name) {
        super(deviceId, name);
        this.gasDetected = false;
    }

    public void setGasDetected(boolean gasDetected) {
        this.gasDetected = gasDetected;
    }

    @Override
    public String readValue() {
        return gasDetected ? "ALERT" : "SAFE";
    }
}
