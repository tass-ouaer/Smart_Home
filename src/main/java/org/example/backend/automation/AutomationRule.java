package org.example.backend.automation;

import org.example.backend.controller.CentralController;

public abstract class AutomationRule {

    protected String ruleName;
    protected boolean isActive;

    public AutomationRule(String ruleName) {
        this.ruleName = ruleName;
        this.isActive = true;
    }

    public String getRuleName() {
        return ruleName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Apply the automation rule using the CentralController.
     * Each rule must implement its own logic here.
     */
    public abstract void apply(CentralController controller);
}
