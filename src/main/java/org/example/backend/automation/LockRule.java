package org.example.backend.automation;

import org.example.backend.home.Home;
import org.example.backend.devices.SmartDoorLock;  // Changed from SmartLock
import java.time.LocalTime;

public class LockRule extends AutomationRule {
    private LocalTime autoLockTime;     // FIXED: variable name was wrong
    private LocalTime autoUnlockTime;   // FIXED: variable name was wrong
    private String lockDeviceName;

    public LockRule(String ruleName, LocalTime lockTime,
                   LocalTime unlockTime, String lockDeviceName) {
        super(ruleName);
        this.autoLockTime = lockTime;     // FIXED: assign to correct variable
        this.autoUnlockTime = unlockTime; // FIXED: assign to correct variable
        this.lockDeviceName = lockDeviceName;
    }
    
    @Override
    public void apply(Home home) {
        if (!isActive) return;
        
        LocalTime now = LocalTime.now();
        
        // Find the lock device by name
        SmartDoorLock lock = findLockDevice(home);
        if (lock == null) {
            System.out.println("LockRule ERROR: Device '" + lockDeviceName + "' not found!");
            return;
        }
        
        // Check if it's time to lock (between lock time and unlock time)
        if (now.isAfter(autoLockTime) && now.isBefore(autoUnlockTime)) {
            if (!lock.isLocked()) {
                lock.lock();
                System.out.println("LockRule → Auto-locked " + lock.getDeviceName() + 
                                 " at " + now);
            }
        } 
        // Check if it's time to unlock (before lock time or after unlock time)
        else if (now.isBefore(autoLockTime) || now.isAfter(autoUnlockTime)) {
            if (lock.isLocked()) {
                lock.unlock();
                System.out.println("LockRule → Auto-unlocked " + lock.getDeviceName() + 
                                 " at " + now);
            }
        }
    }
    
    private SmartDoorLock findLockDevice(Home home) {
        for (var device : home.getAllDevices()) {
            if (device instanceof SmartDoorLock && 
                device.getDeviceName().equals(lockDeviceName)) {
                return (SmartDoorLock) device;
            }
        }
        return null;
    }
    
    // Getters
    public LocalTime getAutoLockTime() { return autoLockTime; }
    public LocalTime getAutoUnlockTime() { return autoUnlockTime; }
    public String getLockDeviceName() { return lockDeviceName; }
}