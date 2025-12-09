package org.example.backend.devices;

import org.example.backend.interfaces.Schedulable;

public class SmartTV extends SmartDevice implements Schedulable {

    private int volume;
    private int channel;
    private boolean isMuted;
    private String scheduledTime;

    public SmartTV(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);

        this.volume = 50;
        this.channel = 1;
        this.isMuted = false;
        this.scheduledTime = null;

        // Typical modern LED TV uses 80–120 watts. We'll use 100W.
        this.setPowerRating(100.0);
    }

    // ------------------------------------------------------------
    // POWER / STATE HANDLING
    // ------------------------------------------------------------
    @Override
    public void turnOn() {
        if (!isOn) {
            this.isOn = true;
            startTimeTracking();
        }

        System.out.println(deviceName + " is now ON - Channel " + channel + ", Volume " + volume + "%");
    }

    @Override
    public void turnOff() {
        if (isOn) {
            stopTimeTracking();
        }
        this.isOn = false;

        System.out.println(deviceName + " is now OFF");
    }

    @Override
    public String getDeviceType() {
        return "SmartTV";
    }

    @Override
    public String getStatus() {
        if (!isOn) return "OFF";

        String muteStatus = isMuted ? " (MUTED)" : "";
        return String.format("ON - Channel %d, Volume %d%%%s", channel, volume, muteStatus);
    }

    // ------------------------------------------------------------
    // SCHEDULING
    // ------------------------------------------------------------
    @Override
    public void schedule(String time) {
        this.scheduledTime = time;
        System.out.println(deviceName + " scheduled to turn on at " + time);
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    // ------------------------------------------------------------
    // VOLUME CONTROL
    // ------------------------------------------------------------
    public void setVolume(int level) {
        if (!isOn) {
            System.out.println("TV is off - cannot adjust volume");
            return;
        }

        if (level < 0) level = 0;
        if (level > 100) level = 100;

        this.volume = level;
        this.isMuted = false;

        System.out.println(deviceName + " volume set to " + volume + "%");
    }

    public int getVolume() {
        return volume;
    }

    public void volumeUp(int amount) {
        setVolume(volume + amount);
    }

    public void volumeDown(int amount) {
        setVolume(volume - amount);
    }

    public void toggleMute() {
        if (!isOn) {
            System.out.println("TV is off - cannot mute");
            return;
        }

        this.isMuted = !isMuted;
        System.out.println(deviceName + (isMuted ? " MUTED" : " UNMUTED"));
    }

    public boolean isMuted() {
        return isMuted;
    }

    // ------------------------------------------------------------
    // CHANNEL CONTROL
    // ------------------------------------------------------------
    public void setChannel(int channelNumber) {
        if (!isOn) {
            System.out.println("TV is off - cannot change channel");
            return;
        }

        if (channelNumber < 1) channelNumber = 1;
        if (channelNumber > 999) channelNumber = 999;

        this.channel = channelNumber;
        System.out.println(deviceName + " changed to channel " + channel);
    }

    public int getChannel() {
        return channel;
    }

    public void channelUp() {
        setChannel(channel + 1);
    }

    public void channelDown() {
        setChannel(channel - 1);
    }
}
