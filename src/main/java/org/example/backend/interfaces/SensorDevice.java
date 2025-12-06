package org.example.backend.interfaces;

import org.example.backend.devices.SmartDevice;

public abstract class SensorDevice extends SmartDevice {

    public SensorDevice(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);
    }

    public abstract String readValue();
}