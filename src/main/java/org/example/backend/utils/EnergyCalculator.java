package org.example.backend.utils;

import org.example.backend.devices.SmartDevice;
import org.example.backend.interfaces.EnergyConsumer;
import org.example.backend.home.Room;
import org.example.backend.home.Home;

import java.util.List;

public class EnergyCalculator {

    // Compute energy for a single SmartDevice
    public static double computeEnergy(SmartDevice device) {
        if (device == null) return 0.0;

        // Direct cast because SmartDevice implements EnergyConsumer
        EnergyConsumer consumer = (EnergyConsumer) device;

        double powerWatts = consumer.getPowerRating();           // e.g. 60 W
        long timeSeconds = consumer.getOnDurationSeconds();      // time ON in seconds

        // Convert W * seconds → kWh
        return (powerWatts * timeSeconds) / (1000.0 * 3600.0);
    }

    // Compute total energy from a list of devices
    public static double computeEnergy(List<SmartDevice> devices) {
        if (devices == null) return 0.0;

        double total = 0.0;
        for (SmartDevice d : devices) {
            total += computeEnergy(d);
        }
        return total;
    }

    // Compute energy for a room
    public static double computeTotalEnergy(Room room) {
        if (room == null) return 0.0;
        return computeEnergy(room.getDevices());
    }

    // Compute energy for the whole home
    public static double computeTotalEnergy(Home home) {
        if (home == null) return 0.0;

        double total = 0.0;
        for (Room r : home.getRooms()) {
            total += computeTotalEnergy(r);
        }
        return total;
    }
}
