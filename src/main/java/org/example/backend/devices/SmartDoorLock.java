package org.example.backend.devices;

public class SmartDoorLock extends SmartDevice {

    private boolean locked;

    public SmartDoorLock(String deviceId, String name) {
        super(deviceId, name);
        this.locked = true;  // locked by default
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public String getStatus() {
        return locked ? "LOCKED" : "UNLOCKED";
    }
}
