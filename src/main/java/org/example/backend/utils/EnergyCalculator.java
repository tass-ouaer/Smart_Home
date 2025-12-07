package org.example.backend.utils;

import org.example.backend.devices.SmartDevice;
import org.example.backend.interfaces.EnergyConsumer;
import java.util.List;

public class EnergyCalculator {

    public static double computeEnergy(SmartDevice device) {
        if (device instanceof EnergyConsumer consumer) {
            double powerWatts = consumer.getPowerRating();
            long timeSeconds = consumer.getOnDurationSeconds();
            return (powerWatts * timeSeconds) / (1000.0 * 3600.0);
        }
        return 0.0;
    }

    public static double computeEnergy(List<SmartDevice> devices) {
        double total = 0;
        for (SmartDevice d : devices) {
            total += computeEnergy(d);
        }
        return total;
    }

    public static double computeTotalEnergy(Room room) {
        return computeEnergy(room.getDevices());
    }

    public static double computeTotalEnergy(Home home) {
        double total = 0;
        for (Room r : home.getRooms()) {
            total += computeTotalEnergy(r);
        }
        return total;
    }
}
