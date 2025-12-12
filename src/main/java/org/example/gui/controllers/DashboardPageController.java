package org.example.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import org.example.backend.controller.CentralController;
import org.example.backend.scheduler.Scheduler;
import org.example.backend.home.Room;
import org.example.backend.devices.*;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardPageController implements Initializable {

    // ===================== UI =====================
    @FXML private VBox roomsContainer;

    @FXML private Label roomCountLabel;
    @FXML private Label deviceCountLabel;
    @FXML private Label deviceStatusLabel;
    @FXML private Label energyUsageLabel;
    @FXML private Label securityStatusLabel;

    @FXML private Button allLightsOnBtn;
    @FXML private Button allLightsOffBtn;
    @FXML private Button lockAllDoorsBtn;
    @FXML private Button awayModeBtn;

    // ===================== BACKEND =====================
    private CentralController controller;
    private Scheduler scheduler;

    // ===================== INIT =====================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // IMPORTANT:
        // Do NOT create Home or Controller here
        // They are injected by MainApp
    }

    /**
     * Called by MainApp after FXML is loaded
     */
    public void init(CentralController controller, Scheduler scheduler) {
        this.controller = controller;
        this.scheduler = scheduler;

        setupQuickActions();
        renderRooms();
        updateDashboardStats();
    }

    // ===================== ROOMS RENDERING =====================
    private void renderRooms() {
        roomsContainer.getChildren().clear();

        for (Room room : controller.getAllRooms()) {
            roomsContainer.getChildren().add(createRoomCard(room));
        }
    }

    private VBox createRoomCard(Room room) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("""
                -fx-background-color: #1e293b;
                -fx-background-radius: 14;
                """);

        Label title = new Label(room.getRoomName());
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18; -fx-font-weight: bold;");

        VBox devicesBox = new VBox(6);

        for (SmartDevice device : room.getDevices()) {
            devicesBox.getChildren().add(createDeviceRow(room, device));
        }

        card.getChildren().addAll(title, devicesBox);
        return card;
    }

    private HBox createDeviceRow(Room room, SmartDevice device) {
        HBox row = new HBox(12);

        Label name = new Label(device.getDeviceName());
        name.setStyle("-fx-text-fill: #cbd5f5;");

        ToggleButton toggle = new ToggleButton(device.isOn() ? "ON" : "OFF");
        toggle.setSelected(device.isOn());

        toggle.setOnAction(e -> {
            if (toggle.isSelected()) {
                device.turnOn();
                toggle.setText("ON");
            } else {
                device.turnOff();
                toggle.setText("OFF");
            }
            updateDashboardStats();
        });

        Button removeBtn = new Button("✖");
        removeBtn.setOnAction(e -> {
            controller.removeDevice(room.getRoomName(), device.getDeviceId());
            renderRooms();
            updateDashboardStats();
        });

        row.getChildren().addAll(name, toggle, removeBtn);
        return row;
    }

    // ===================== QUICK ACTIONS =====================
    private void setupQuickActions() {

        allLightsOnBtn.setOnAction(e -> {
            applyToDevices(Light.class, SmartDevice::turnOn);
        });

        allLightsOffBtn.setOnAction(e -> {
            applyToDevices(Light.class, SmartDevice::turnOff);
        });

        lockAllDoorsBtn.setOnAction(e -> {
            applyToDevices(SmartDoorLock.class, SmartDevice::turnOn);
        });

        awayModeBtn.setOnAction(e -> {
            applyToDevices(Light.class, SmartDevice::turnOff);
            applyToDevices(SmartDoorLock.class, SmartDevice::turnOn);
        });
    }

    // ===================== DASHBOARD STATS =====================
    private void updateDashboardStats() {

        int rooms = controller.getAllRooms().size();
        int total = 0;
        int active = 0;
        double energy = 0;

        for (Room room : controller.getAllRooms()) {
            for (SmartDevice device : room.getDevices()) {
                total++;
                if (device.isOn()) {
                    active++;
                    energy += device.getEnergyConsumption();
                }
            }
        }

        roomCountLabel.setText(String.valueOf(rooms));
        deviceCountLabel.setText(String.valueOf(total));
        deviceStatusLabel.setText(active + " Active");
        energyUsageLabel.setText(String.format("%.2f kWh", energy));
        securityStatusLabel.setText(hasActive(SmartDoorLock.class) ? "ARMED" : "DISARMED");
    }

    // ===================== HELPERS =====================
    private void applyToDevices(Class<? extends SmartDevice> type, DeviceAction action) {
        for (Room room : controller.getAllRooms()) {
            for (SmartDevice device : room.getDevices()) {
                if (type.isInstance(device)) {
                    action.apply(device);
                }
            }
        }
        renderRooms();
        updateDashboardStats();
    }

    private boolean hasActive(Class<? extends SmartDevice> type) {
        for (Room room : controller.getAllRooms()) {
            for (SmartDevice device : room.getDevices()) {
                if (type.isInstance(device) && device.isOn()) return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    private interface DeviceAction {
        void apply(SmartDevice device);
    }
}
