package org.example.backend.interfaces;

public interface EnergyConsumer {
    double getPowerRating();
    long getOnDurationSeconds();
    double getEnergyConsumption();
    double calculateEnergyUsage(double hours);
}
