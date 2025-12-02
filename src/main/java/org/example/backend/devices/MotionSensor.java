package org.example.backend.devices;

import org.example.backend.interfaces.SensorDevice;

public class MotionSensor extends SmartDevice implements SensorDevice {

    private boolean motionDetected;

    public MotionSensor(String deviceId, String name) {
        super(deviceId, name);
        this.motionDetected = false;
    }

    public void setMotionDetected(boolean motionDetected) {
        this.motionDetected = motionDetected;
    }

    @Override
    public String readValue() {
        return motionDetected ? "MOTION" : "NO_MOTION";
    }
}
