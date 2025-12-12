package org.example.backend.devices;

import org.example.backend.interfaces.SensorDevice;

public class IntrusionSensor extends SensorDevice {

    private boolean intrusionDetected;
    private boolean isArmed;
    private int sensitivityLevel;

    public IntrusionSensor(String deviceId, String deviceName, String location) {
        super(deviceId, deviceName, location);

        this.intrusionDetected = false;
        this.isArmed = false;
        this.sensitivityLevel = 5;

        // Typical PIR intrusion sensor consumes ~1 watt
        this.setPowerRating(1.0);

        // Sensors are normally always ON
        this.isOn = true;
        startTimeTracking();
    }

    @Override
    public void turnOn() {
        if (!isOn) {
            this.isOn = true;
            startTimeTracking();
            System.out.println(deviceName + " is now ACTIVE");
        }
    }

    @Override
    public void turnOff() {
        if (isOn) {
            stopTimeTracking();
            this.isOn = false;
            this.isArmed = false;
            this.intrusionDetected = false;
            System.out.println(deviceName + " is now INACTIVE");
        }
    }

    @Override
    public String getDeviceType() {
        return "IntrusionSensor";
    }

    @Override
    public String getStatus() {
        if (!isOn) {
            return "INACTIVE - Sensor powered off";
        }

        if (intrusionDetected) {
            return "INTRUSION DETECTED! - Security breach at " + location;
        }

        if (isArmed) {
            return "ARMED - Monitoring for intrusions";
        }

        return "DISARMED - Not monitoring";
    }

    @Override
    public String readValue() {
        if (!isOn) return "INACTIVE";
        if (intrusionDetected) return "INTRUSION";
        if (isArmed) return "ARMED";
        return "DISARMED";
    }

    // -----------------------------------------------------------
    // SECURITY LOGIC
    // -----------------------------------------------------------

    public void arm() {
        if (!isOn) {
            System.out.println("Cannot arm - Sensor is powered off");
            return;
        }

        this.isArmed = true;
        this.intrusionDetected = false;

        System.out.println(deviceName + " is now ARMED - Monitoring active");
    }

    public void disarm() {
        this.isArmed = false;
        this.intrusionDetected = false;
        System.out.println(deviceName + " is now DISARMED");
    }

    public void detectIntrusion() {
        if (!isOn || !isArmed) return;

        this.intrusionDetected = true;

        System.out.println("INTRUSION ALERT - " + deviceName);
        System.out.println("Location: " + location);
        System.out.println("Time: " + java.time.LocalTime.now());
    }

    public boolean isIntrusionDetected() {
        return intrusionDetected;
    }

    public boolean isArmed() {
        return isArmed;
    }

    public void setSensitivity(int level) {
        if (level >= 1 && level <= 10) {
            this.sensitivityLevel = level;
            System.out.println(deviceName + " sensitivity set to: " + level);
        }
    }

    public int getSensitivity() {
        return sensitivityLevel;
    }

    public void resetAlert() {
        this.intrusionDetected = false;
        System.out.println(deviceName + " alert has been reset");
    }
}
