package org.example.backend.automation;

import org.example.backend.home.Home;
import org.example.backend.home.Room;
import org.example.backend.devices.SmartDoorLock;
import java.time.LocalTime;

public class LockRule extends AutomationRule {

    private final LocalTime lockTime;
    private final LocalTime unlockTime;
    private final String deviceName;

    public LockRule(String name, LocalTime lockTime,
                    LocalTime unlockTime, String deviceName) {
        super(name);
        this.lockTime = lockTime;
        this.unlockTime = unlockTime;
        this.deviceName = deviceName;
    }

    @Override
    public void apply(Home home) {
        if (!isActive) return;

        SmartDoorLock lock = findLock(home);
        if (lock == null) return;

        LocalTime now = LocalTime.now();
        boolean withinInterval;

        if (lockTime.isBefore(unlockTime)) {
            // Normal case: 22:00 → 06:00
            withinInterval = now.isAfter(lockTime) && now.isBefore(unlockTime);
        } else {
            // Midnight-crossing case
            withinInterval = now.isAfter(lockTime) || now.isBefore(unlockTime);
        }

        if (withinInterval && !lock.isLocked()) {
            lock.lock();
        } else if (!withinInterval && lock.isLocked()) {
            lock.turnOn(); // unlock (no code required)
        }
    }

    private SmartDoorLock findLock(Home home) {
        for (Room room : home.getRooms()) {
            for (var d : room.getDevices()) {
                if (d instanceof SmartDoorLock &&
                        d.getDeviceName().equalsIgnoreCase(deviceName)) {
                    return (SmartDoorLock) d;
                }
            }
        }
        return null;
    }
}
