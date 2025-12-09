package org.example.backend.automation;

import org.example.backend.home.Home;

public abstract class AutomationRule {

    protected final String ruleName;
    protected boolean isActive = true;

    public AutomationRule(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleName() { return ruleName; }
    public boolean isActive() { return isActive; }

    public void activate() { isActive = true; }
    public void deactivate() { isActive = false; }

    public abstract void apply(Home home);
}
