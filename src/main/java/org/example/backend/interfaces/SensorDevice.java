package org.example.backend.interfaces;

import org.example.backend.devices.SmartDevice;

public abstract class SensorDevice extends SmartDevice {

    public SensorDevice(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);
        // Sensors usually do not consume much energy; keep default or set later.
    }

    /**
     * Read the current sensor value as a String
     * (e.g. "MOTION", "NO_MOTION", "SAFE", "ALERT").
     */
    public abstract String readValue();
}
