package org.example.backend.interfaces;

public interface Controllable {

    void turnOn();

    void turnOff();

    /**
     * Human‑readable status, usually "ON" or "OFF".
     */
    String getStatus();
}
