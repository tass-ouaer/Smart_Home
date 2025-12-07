package org.example.backend.automation;

import org.example.backend.home.Home;

public abstract class AutomationRule {
    protected String ruleId;
    protected String ruleName;
    protected boolean isActive;
    
    public AutomationRule(String ruleName) {
        this.ruleId = java.util.UUID.randomUUID().toString();
        this.ruleName = ruleName;
        this.isActive = true;
    }
    
    // Abstract method - each rule must implement this
    public abstract void apply(Home home);
    
    // Getters and setters
    public String getRuleId() { return ruleId; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public String toString() {
        return ruleName + " [ID: " + ruleId + ", Active: " + isActive + "]";
    }
}