package org.example.backend.automation;

import org.example.backend.home.Home;
import org.example.backend.home.Room;
import org.example.backend.devices.SmartDevice;
import org.example.backend.interfaces.SensorDevice;

public class MotionRule extends AutomationRule {

    private final String sensorName;
    private final String targetName;

    private long lastMotionTimestamp = 0;
    private final int offDelaySeconds;

    public MotionRule(String name, String sensorName,
                      String targetName, int offDelaySeconds) {
        super(name);
        this.sensorName = sensorName;
        this.targetName = targetName;
        this.offDelaySeconds = offDelaySeconds;
    }

    @Override
    public void apply(Home home) {
        if (!isActive) return;

        SensorDevice sensor = findSensor(home);
        SmartDevice target = findTarget(home);
        if (sensor == null || target == null) return;

        boolean detected = sensor.readValue().equalsIgnoreCase("motion");

        if (detected) {
            lastMotionTimestamp = System.currentTimeMillis();
            if (!target.isOn()) target.turnOn();
        } else {
            long idle = (System.currentTimeMillis() - lastMotionTimestamp) / 1000;
            if (idle >= offDelaySeconds && target.isOn()) {
                target.turnOff();
            }
        }
    }

    private SensorDevice findSensor(Home home) {
        for (Room room : home.getRooms()) {
            for (SmartDevice d : room.getDevices()) {
                if (d instanceof SensorDevice &&
                        d.getDeviceName().equalsIgnoreCase(sensorName)) {
                    return (SensorDevice) d;
                }
            }
        }
        return null;
    }

    private SmartDevice findTarget(Home home) {
        for (Room room : home.getRooms()) {
            for (SmartDevice d : room.getDevices()) {
                if (d.getDeviceName().equalsIgnoreCase(targetName)) {
                    return d;
                }
            }
        }
        return null;
    }
}
