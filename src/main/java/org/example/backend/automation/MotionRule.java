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

        String motion = controller.readSensorValue(null, motionSensorId);
        SmartDevice target = controller.findDeviceById(targetDeviceId);
        if (motion == null || target == null) return;

        if (motion.equalsIgnoreCase("motion")) {
            lastMotionTimestamp = System.currentTimeMillis();
            if (!target.isOn()) target.turnOn();
        } else {
            long noMotionTime = (System.currentTimeMillis() - lastMotionTimestamp) / 1000;
            if (noMotionTime >= offDelaySeconds && target.isOn()) {
                target.turnOff();
            }
        }
    }
}
