package org.example.backend.interfaces;

public interface EnergyConsumer {

    double getPowerRating();    // watts or kW
    long getOnDurationSeconds(); // how long the device has been on
}
