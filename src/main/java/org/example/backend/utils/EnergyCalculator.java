package org.example.backend.utils;

import org.example.backend.devices.SmartDevice;
import org.example.backend.interfaces.EnergyConsumer;
import org.example.backend.home.Home;
import org.example.backend.home.Room;

import java.util.List;

public class EnergyCalculator {

    /**
     * Compute energy for ONE smart device.
     * Formula: kWh = (W * seconds) / 3,600,000
     */
    public static double computeEnergy(SmartDevice device) {
        if (device == null) return 0.0;

        EnergyConsumer consumer = device;

        double powerWatts = consumer.getPowerRating();
        long durationSeconds = consumer.getOnDurationSeconds();

        return (powerWatts * durationSeconds) / (1000.0 * 3600.0);
    }

    /**
     * Compute total energy for a list of devices.
     */
    public static double computeEnergy(List<SmartDevice> devices) {
        if (devices == null) return 0.0;

        double total = 0.0;
        for (SmartDevice d : devices) {
            total += computeEnergy(d);
        }
        return total;
    }

    /**
     * Compute total energy for one room.
     */
    public static double computeTotalEnergy(Room room) {
        if (room == null) return 0.0;

        return computeEnergy(room.getDevices());
    }

    /**
     * Compute total energy for the entire home.
     */
    public static double computeTotalEnergy(Home home) {
        if (home == null) return 0.0;

        double total = 0.0;
        for (Room room : home.getRooms()) {
            total += computeTotalEnergy(room);
        }
        return total;
    }
}
