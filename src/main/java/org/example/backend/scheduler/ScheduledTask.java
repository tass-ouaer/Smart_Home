package org.example.backend.scheduler;

import org.example.backend.interfaces.Schedulable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single scheduled action for a Schedulable device.
 */
public class ScheduledTask {

    private final Schedulable device;
    private final String timeString;
    private final LocalTime time;
    private final String action; // Example: ON, OFF, CUSTOM

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructor for ScheduledTask
     */
    public ScheduledTask(Schedulable device, String timeString, String action) {
        if (device == null) {
            throw new IllegalArgumentException("Device cannot be null");
        }
        if (timeString == null || timeString.isEmpty()) {
            throw new IllegalArgumentException("Time cannot be empty");
        }

        this.device = device;
        this.timeString = timeString;
        this.time = LocalTime.parse(timeString, FORMATTER);
        this.action = (action == null) ? "DEFAULT" : action;
    }

    /** Returns the device assigned to this task */
    public Schedulable getDevice() {
        return device;
    }

    /** Returns the LocalTime of execution */
    public LocalTime getTime() {
        return time;
    }

    /** Returns the time as a string "HH:mm" */
    public String getTimeString() {
        return timeString;
    }

    /** Returns the action label */
    public String getAction() {
        return action;
    }

    /**
     * Executes the scheduled action.
     * Scheduler calls this automatically when the time matches.
     */
    public void execute() {
        System.out.println("[ScheduledTask] Executing: " + action + " at " + timeString);
        device.schedule(timeString); // triggers the device's scheduling logic
    }

    @Override
    public String toString() {
        return "ScheduledTask{time=" + timeString +
                ", action=" + action +
                ", device=" + device.getClass().getSimpleName() +
                "}";
    }
}
