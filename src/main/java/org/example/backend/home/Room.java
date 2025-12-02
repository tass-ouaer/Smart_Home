package org.example.backend.home;

import org.example.backend.devices.SmartDevice;
import java.util.ArrayList;
import java.util.List;

/**
 * The Room class represents a single room in the home.

 * PURPOSE FOR GUI:
 * - RoomPageController displays the room name + its devices.
 * - DevicePageController needs access to devices inside the room.

 * LOGIC:
 * - Avoid duplicate devices (same ID).
 * - Provide clean helper methods for controllers.
 */
public class Room {

    private String roomName;
    private List<SmartDevice> devices;


    public Room(String roomName) {
        this.roomName = roomName;
        this.devices = new ArrayList<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public List<SmartDevice> getDevices() {
        return devices;
    }


    /**
     * Adds a device if it does not already exist in the room.
     */
    public void addDevice(SmartDevice device) {
        if (device == null) return;

        for (SmartDevice d : devices) {
            if (d.getId().equals(device.getId())) {
                return; // duplicate → ignore
            }
        }

        devices.add(device);
    }

    /**
     * Removes a device by ID.
     */
    public boolean removeDevice(String deviceId) {
        if (deviceId == null) return false;

        return devices.removeIf(d -> d.getId().equals(deviceId));
    }

    /**
     * Returns a device by ID.
     */
    public SmartDevice getDevice(String deviceId) {
        if (deviceId == null) return null;

        for (SmartDevice d : devices) {
            if (d.getId().equals(deviceId)) {
                return d;
            }
        }

        return null;
    }

    /**
     * Counts how many devices of the same type (same subclass)
     * exist in this room.
     */
    public int countDevices(Class<?> deviceClass) {
        int count = 0;

        for (SmartDevice d : devices) {
            if (d.getClass() == deviceClass) {
                count++;
            }
        }

        return count;
    }

}
