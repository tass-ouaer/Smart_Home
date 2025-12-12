package org.example.backend.controller;

import org.example.backend.home.Home;
import org.example.backend.home.Room;
import org.example.backend.devices.SmartDevice;
import org.example.backend.interfaces.*;

import java.util.ArrayList;
import java.util.List;

public class CentralController {

    private Home home;

    public CentralController(Home home) {
        this.home = home;
    }

    public Home getHome() {
        return home;
    }

    /* ================================================
       ROOM MANAGEMENT
       → Delegated to Home class
    ================================================= */
    public boolean addRoom(String roomName) {
        if (roomName == null || home.getRoom(roomName) != null) return false;
        home.addRoom(new Room(roomName));
        return true;
    }

    public boolean removeRoom(String roomName) {
        return home.removeRoom(roomName);
    }

    public Room getRoom(String roomName) {
        return home.getRoom(roomName);
    }

    public List<Room> getAllRooms() {
        return home.getRooms();
    }

    /* ================================================
       DEVICE MANAGEMENT
       → Delegated to Room class
    ================================================= */
    public boolean addDeviceToRoom(String roomName, SmartDevice device) {
        Room room = getRoom(roomName);
        if (room == null || device == null) return false;
        room.addDevice(device);
        return true;
    }

    public boolean removeDevice(String roomName, String deviceId) {
        Room room = getRoom(roomName);
        if (room == null) return false;
        return room.removeDevice(deviceId);
    }

    public SmartDevice getDevice(String roomName, String deviceId) {
        Room room = getRoom(roomName);
        if (room == null) return null;
        return room.getDevice(deviceId);
    }

    public List<SmartDevice> getDevicesInRoom(String roomName) {
        Room room = getRoom(roomName);
        if (room == null) return new ArrayList<>();
        return room.getDevices();
    }

    /* ================================================
       DEVICE CONTROL (Controllable)
    ================================================= */
    public boolean turnDeviceOn(String roomName, String deviceId) {
        SmartDevice device = getDevice(roomName, deviceId);
        if (device != null) {
            device.turnOn();
            return true;
        }
        return false;
    }


    public boolean turnDeviceOff(String roomName, String deviceId) {
        SmartDevice device = getDevice(roomName, deviceId);
        if (device != null) {
            device.turnOff();
            return true;
        }
        return false;
    }

    /* ================================================
       SCHEDULING (Schedulable)
    ================================================= */
    public boolean scheduleDevice(String roomName, String deviceId, String time) {
        SmartDevice device = getDevice(roomName, deviceId);
        if (device instanceof Schedulable schedulable) {
            schedulable.schedule(time);
            return true;
        }
        return false;
    }

    /* ================================================
       ENERGY USAGE (EnergyConsumer)
    ================================================= */
    public double getDeviceEnergyConsumption(String roomName, String deviceId, double hours) {
        SmartDevice device = getDevice(roomName, deviceId);
        if (device != null && hours >= 0) {
            return device.calculateEnergyUsage(hours);
        }
        return 0;
    }

    /* ================================================
       SENSOR READING (SensorDevice)
    ================================================= */
    public String readSensorValue(String roomName, String deviceId) {
        SmartDevice device = getDevice(roomName, deviceId);
        if (device instanceof SensorDevice sensor) {
            return sensor.readValue();
        }
        return null;
    }

    /* ================================================
       SEARCH HELPERS
       findDeviceById lets you get a device anywhere in the home without knowing the room.
       findDevicesByType lets you get all devices of a certain class (like all lights) easily.
       If your app always knows the room name when controlling devices, you can skip these methods. They’re just there to make searching easier in more complex scenarios.
    ================================================= */
    public SmartDevice findDeviceById(String deviceId) {
        for (Room room : home.getRooms()) {
            SmartDevice device = room.getDevice(deviceId);
            if (device != null) return device;
        }
        return null;
    }

    public List<SmartDevice> findDevicesByType(Class<?> type) {
        List<SmartDevice> list = new ArrayList<>();
        for (Room room : home.getRooms()) {
            for (SmartDevice device : room.getDevices()) {
                if (type.isInstance(device)) {
                    list.add(device);
                }
            }
        }
        return list;
    }
}
