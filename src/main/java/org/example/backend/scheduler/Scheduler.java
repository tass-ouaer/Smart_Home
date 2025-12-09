package org.example.backend.scheduler;

import org.example.backend.interfaces.Schedulable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Central scheduler that manages Schedulable devices based on simple "HH:mm" times.
 */
public class Scheduler {

    private static class ScheduledEntry {
        private final Schedulable target;
        private final String timeString;
        private final LocalTime time;
        private final String action; // e.g. "ON", "OFF", "CUSTOM"

        ScheduledEntry(Schedulable target, String timeString, String action) {
            this.target = target;
            this.timeString = timeString;
            this.time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
            this.action = action;
        }

        public Schedulable getTarget() {
            return target;
        }

        public LocalTime getTime() {
            return time;
        }

        public String getTimeString() {
            return timeString;
        }

        public String getAction() {
            return action;
        }
    }

    private final List<ScheduledEntry> tasks;
    private final DateTimeFormatter timeFormatter;

    public Scheduler() {
        this.tasks = new ArrayList<>();
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }

    /**
     * Register a Schedulable device to be triggered at a specific time.
     * The device's own schedule(time) method is called immediately so it can store the time.
     */
    public void scheduleDevice(Schedulable device, String time) {
        scheduleDevice(device, time, "DEFAULT");
    }

    /**
     * Register a Schedulable device with a custom action label.
     * The scheduler does not interpret the action; it is for logging or future use.
     */
    public void scheduleDevice(Schedulable device, String time, String action) {
        Objects.requireNonNull(device, "Schedulable device cannot be null");
        Objects.requireNonNull(time, "Time cannot be null");

        LocalTime.parse(time, timeFormatter); // validate format, will throw if invalid

        device.schedule(time);
        ScheduledEntry entry = new ScheduledEntry(device, time, action);
        tasks.add(entry);

        System.out.println("[Scheduler] Task added at " + time + " (" + action + ")");
    }

    /**
     * Remove all scheduled entries for a given device.
     */
    public void cancelAllForDevice(Schedulable device) {
        if (device == null) return;
        tasks.removeIf(entry -> entry.getTarget() == device);
        System.out.println("[Scheduler] All tasks cancelled for device");
    }

    /**
     * Returns an immutable view of all scheduled entries' times as strings.
     */
    public List<String> listScheduledTimes() {
        List<String> times = new ArrayList<>();
        for (ScheduledEntry entry : tasks) {
            times.add(entry.getTimeString());
        }
        return Collections.unmodifiableList(times);
    }

    /**
     * Check all tasks and trigger those whose time is equal to the current time (HH:mm).
     * In a real system this would be called periodically, e.g. every minute.
     */
    public void runDueTasks() {
        LocalTime now = LocalTime.now();
        String nowString = now.format(timeFormatter);

        for (ScheduledEntry entry : tasks) {
            if (entry.getTime().getHour() == now.getHour()
                    && entry.getTime().getMinute() == now.getMinute()) {
                System.out.println("[Scheduler] Executing task scheduled at "
                        + entry.getTimeString() + " (" + entry.getAction() + ")");
                entry.getTarget().schedule(nowString);
            }
        }
    }

    /**
     * Simple debug helper to print all scheduled tasks.
     */
    public void printAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("[Scheduler] No tasks scheduled");
            return;
        }
        System.out.println("[Scheduler] Scheduled tasks:");
        for (ScheduledEntry entry : tasks) {
            System.out.println(" - Time: " + entry.getTimeString()
                    + ", Action: " + entry.getAction()
                    + ", Target: " + entry.getTarget().getClass().getSimpleName());
        }
    }
}
