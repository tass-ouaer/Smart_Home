package org.example.backend.devices;

import org.example.backend.home.Room;

public class SmartDevice {

    // --------- FIELDS ----------
    private String deviceId;
    private String name;
    private boolean isOn;
    private Room room;

    // --------- CONSTRUCTOR ----------
    public SmartDevice(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
        this.isOn = false;   // device is OFF by default
        this.room = null;    // no room yet
    }

    // --------- COMMON METHODS ----------
    public void turnOn() {
        this.isOn = true;
    }

    public void turnOff() {
        this.isOn = false;
    }

    public String getStatus() {
        return isOn ? "ON" : "OFF";
    }

    public String getName() {
        return name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
