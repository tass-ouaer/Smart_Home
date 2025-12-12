package org.example.backend.automation;

import org.example.backend.controller.CentralController;
import org.example.backend.devices.SmartDevice;
import org.example.backend.devices.SmartDoorLock;

import java.time.LocalTime;

public class LockRule extends AutomationRule {

    private final String lockDeviceId;
    private final LocalTime lockTime;
    private final LocalTime unlockTime;

    public LockRule(String ruleName,
                    String lockDeviceId,
                    LocalTime lockTime,
                    LocalTime unlockTime) {
        super(ruleName);
        this.lockDeviceId = lockDeviceId;
        this.lockTime = lockTime;
        this.unlockTime = unlockTime;
    }

    @Override
    public void apply(CentralController controller) {
        if (!isActive) return;

        SmartDevice device = controller.findDeviceById(lockDeviceId);
        if (!(device instanceof SmartDoorLock lock)) return;

        LocalTime now = LocalTime.now();
        boolean shouldBeLocked =
                now.isAfter(lockTime) && now.isBefore(unlockTime);

        if (shouldBeLocked && !lock.isLocked()) {
            lock.lock();
        } else if (!shouldBeLocked && lock.isLocked()) {
            lock.turnOn(); // unlock
        }
    }
}
