package org.example.backend.interfaces;

public interface Schedulable {

    /**
     * Set the time when this device should be activated, e.g. "06:30".
     */
    void schedule(String time);
}
