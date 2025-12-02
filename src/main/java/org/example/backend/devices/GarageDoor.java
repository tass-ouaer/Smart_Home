package org.example.backend.devices;

public class GarageDoor extends SmartDevice {

    private boolean open;

    public GarageDoor(String deviceId, String name) {
        super(deviceId, name);
        this.open = false;
    }

    public void openDoor() {
        open = true;
    }

    public void closeDoor() {
        open = false;
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public String getStatus() {
        return open ? "OPEN" : "CLOSED";
    }
}
