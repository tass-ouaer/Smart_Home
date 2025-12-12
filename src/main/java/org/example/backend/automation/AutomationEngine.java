package org.example.backend.automation;

import org.example.backend.controller.CentralController;
import java.util.ArrayList;
import java.util.List;

public class AutomationEngine {

    private final CentralController controller;
    private final List<AutomationRule> rules = new ArrayList<>();

    public AutomationEngine(CentralController controller) {
        this.controller = controller;
    }

    public void addRule(AutomationRule rule) {
        rules.add(rule);
    }

    public void run() {
        for (AutomationRule rule : rules) {
            rule.apply(controller);
        }
    }
}
