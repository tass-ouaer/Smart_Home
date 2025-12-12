package org.example.backend.automation;

import org.example.backend.controller.CentralController;
import org.example.backend.devices.SmartDevice;
import org.example.backend.interfaces.SensorDevice;

public class MotionRule extends AutomationRule {

    private String motionSensorId;
    private String targetDeviceId;
    private long lastMotionTimestamp;
    private int offDelaySeconds;

    public MotionRule(String ruleName, String motionSensorId, String targetDeviceId, int offDelaySeconds) {
        super(ruleName);
        this.motionSensorId = motionSensorId;
        this.targetDeviceId = targetDeviceId;
        this.offDelaySeconds = offDelaySeconds;
    }

    @Override
    public void apply(CentralController controller) {
        if (!isActive) return;

        SmartDevice sensorDevice = controller.findDeviceById(motionSensorId);
        if (!(sensorDevice instanceof SensorDevice sensor)) return;

        SmartDevice target = controller.findDeviceById(targetDeviceId);
        if (target == null) return;

        String motion = sensor.readValue();
        long now = System.currentTimeMillis();

        // Initialize timestamp on first run
        if (lastMotionTimestamp == 0) {
            lastMotionTimestamp = now;
        }

        if ("motion".equalsIgnoreCase(motion)) {
            lastMotionTimestamp = now;

            // 🔓 MOTION → UNLOCK DOOR
            if (target.isOn()) {   // if currently locked
                target.turnOff();  // unlock
            }

        } else {
            long noMotionSeconds = (now - lastMotionTimestamp) / 1000;

            // 🔒 NO MOTION → LOCK DOOR
            if (noMotionSeconds >= offDelaySeconds && !target.isOn()) {
                target.turnOn();   // lock
            }
        }
    }}

