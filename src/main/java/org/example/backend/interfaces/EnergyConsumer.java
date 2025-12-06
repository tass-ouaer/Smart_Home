package org.example.backend.interfaces;

public interface EnergyConsumer {

    /**
     * Base energy consumption for this device in kWh.
     */
    double getEnergyConsumption();

    /**
     * Default helper to compute energy usage for a duration.
     */
    default double calculateEnergyUsage(double hours) {
        return getEnergyConsumption() * hours;
    }
}
