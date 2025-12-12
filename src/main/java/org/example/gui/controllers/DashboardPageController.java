package org.example.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import org.example.backend.home.*;
import org.example.backend.devices.*;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardPageController implements Initializable {

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

    private Home home;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        home = HomeFactory.createDefaultHome();
        setupQuickActions();
        renderRooms();
        updateDashboardStats();
    }

    private void renderRooms() {
        roomsContainer.getChildren().clear();
        for (Room room : home.getRooms()) {
            roomsContainer.getChildren().add(createRoomCard(room));
        }
    }

    private VBox createRoomCard(Room room) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color:#1e293b; -fx-background-radius:12;");

        Label title = new Label(room.getRoomName());
        title.setStyle("-fx-text-fill:white; -fx-font-size:18; -fx-font-weight:bold;");

        VBox devicesBox = new VBox(6);
        for (SmartDevice device : room.getDevices()) {
            devicesBox.getChildren().add(createDeviceRow(device));
        }

        card.getChildren().addAll(title, devicesBox);
        return card;
    }

    private HBox createDeviceRow(SmartDevice device) {
        HBox row = new HBox(10);

        Label name = new Label(device.getDeviceName());
        name.setStyle("-fx-text-fill:#cbd5f5;");

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

        row.getChildren().addAll(name, toggle);
        return row;
    }

    private void setupQuickActions() {

        allLightsOnBtn.setOnAction(e -> {
            applyToDevices(Light.class, SmartDevice::turnOn);
            renderRooms();
            updateDashboardStats();
        });

        allLightsOffBtn.setOnAction(e -> {
            applyToDevices(Light.class, SmartDevice::turnOff);
            renderRooms();
            updateDashboardStats();
        });

        lockAllDoorsBtn.setOnAction(e -> {
            applyToDevices(SmartDoorLock.class, SmartDevice::turnOn);
            renderRooms();
            updateDashboardStats();
        });

        awayModeBtn.setOnAction(e -> {
            applyToDevices(Light.class, SmartDevice::turnOff);
            applyToDevices(SmartDoorLock.class, SmartDevice::turnOn);
            renderRooms();
            updateDashboardStats();
        });
    }

    private void updateDashboardStats() {
        int rooms = home.getRooms().size();
        int total = 0, active = 0;

        for (Room room : home.getRooms()) {
            for (SmartDevice d : room.getDevices()) {
                total++;
                if (d.isOn()) active++;
            }
        }

        roomCountLabel.setText(String.valueOf(rooms));
        deviceCountLabel.setText(String.valueOf(total));
        deviceStatusLabel.setText(active + " Active");
        energyUsageLabel.setText(String.format("%.1f kWh", active * 0.15));
        securityStatusLabel.setText(hasActive(SmartDoorLock.class) ? "ARMED" : "DISARMED");
    }

    private void applyToDevices(Class<? extends SmartDevice> type, DeviceAction action) {
        for (Room r : home.getRooms())
            for (SmartDevice d : r.getDevices())
                if (type.isInstance(d)) action.apply(d);
        renderRooms();
        updateDashboardStats();
    }

    private boolean hasActive(Class<? extends SmartDevice> type) {
        for (Room r : home.getRooms())
            for (SmartDevice d : r.getDevices())
                if (type.isInstance(d) && d.isOn()) return true;
        return false;
    }

    @FunctionalInterface
    private interface DeviceAction {
        void apply(SmartDevice device);
    }
}
