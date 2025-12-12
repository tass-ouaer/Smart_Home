package org.example.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import org.example.backend.home.*;
import org.example.backend.devices.*;
import org.example.backend.controller.CentralController;
import org.example.backend.scheduler.Scheduler;
import org.example.backend.interfaces.Schedulable;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardPageController implements Initializable {

    // ================= UI =================
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
    @FXML private Button listAllDevicesBtn;
    @FXML private Button findDeviceBtn;
    @FXML private Button addRoomBtn;
    @FXML private Button removeRoomBtn;

    // ================= BACKEND =================
    private CentralController controller;
    private Scheduler scheduler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Home home = HomeFactory.createDefaultHome();
        controller = new CentralController(home);
        scheduler = new Scheduler();

        setupActions();
        renderRooms();
        updateDashboardStats();
    }

    // =====================================================
    // ================= ROOMS =============================
    // =====================================================
    private void renderRooms() {
        roomsContainer.getChildren().clear();
        for (Room room : controller.getAllRooms()) {
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
            devicesBox.getChildren().add(createDeviceRow(room, device));
        }

        Button addDeviceBtn = new Button("+ Add Device");
        addDeviceBtn.setOnAction(e -> addDevice(room));

        Button removeDeviceBtn = new Button("Remove Device");
        removeDeviceBtn.setOnAction(e -> removeDevice(room));

        HBox actions = new HBox(10, addDeviceBtn, removeDeviceBtn);

        card.getChildren().addAll(title, devicesBox, actions);
        return card;
    }

    private HBox createDeviceRow(Room room, SmartDevice device) {
        HBox row = new HBox(10);

        Label info = new Label(
                device.getDeviceName() +
                        " | ID: " + device.getDeviceId() +
                        " | " + device.getStatus()
        );
        info.setStyle("-fx-text-fill:#cbd5f5;");

        ToggleButton toggle = new ToggleButton(device.isOn() ? "ON" : "OFF");
        toggle.setSelected(device.isOn());

        toggle.setOnAction(e -> {
            if (toggle.isSelected()) device.turnOn();
            else device.turnOff();
            toggle.setText(device.isOn() ? "ON" : "OFF");
            updateDashboardStats();
        });

        row.getChildren().addAll(info, toggle);

        // Thermostat controls
        if (device instanceof Thermostat thermostat) {
            Button minus = new Button("−");
            Button plus = new Button("+");
            minus.setOnAction(e -> thermostat.decreaseTemperature());
            plus.setOnAction(e -> thermostat.increaseTemperature());

            Button scheduleBtn = new Button("Schedule");
            scheduleBtn.setOnAction(e -> scheduleDevice(thermostat));

            row.getChildren().addAll(minus, plus, scheduleBtn);
        }

        return row;
    }

    // =====================================================
    // ================= ACTIONS ===========================
    // =====================================================
    private void setupActions() {

        allLightsOnBtn.setOnAction(e ->
                applyToDevices(Light.class, SmartDevice::turnOn));

        allLightsOffBtn.setOnAction(e ->
                applyToDevices(Light.class, SmartDevice::turnOff));

        lockAllDoorsBtn.setOnAction(e ->
                applyToDevices(SmartDoorLock.class, SmartDevice::turnOn));

        awayModeBtn.setOnAction(e -> {
            applyToDevices(Light.class, SmartDevice::turnOff);
            applyToDevices(SmartDoorLock.class, SmartDevice::turnOn);
        });

        listAllDevicesBtn.setOnAction(e -> listAllDevices());
        findDeviceBtn.setOnAction(e -> findDeviceById());
        addRoomBtn.setOnAction(e -> addRoom());
        removeRoomBtn.setOnAction(e -> removeRoom());
    }

    private void applyToDevices(Class<? extends SmartDevice> type, DeviceAction action) {
        for (Room r : controller.getAllRooms())
            for (SmartDevice d : r.getDevices())
                if (type.isInstance(d)) action.apply(d);

        renderRooms();
        updateDashboardStats();
    }

    // =====================================================
    // ================= ROOM OPS ==========================
    // =====================================================
    private void addRoom() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter room name");
        dialog.showAndWait().ifPresent(name -> {
            controller.addRoom(name);
            renderRooms();
            updateDashboardStats();
        });
    }

    private void removeRoom() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Room name to remove");
        dialog.showAndWait().ifPresent(name -> {
            controller.removeRoom(name);
            renderRooms();
            updateDashboardStats();
        });
    }

    // =====================================================
    // ================= DEVICE OPS ========================
    // =====================================================
    private void addDevice(Room room) {
        ChoiceDialog<String> dialog =
                new ChoiceDialog<>("Light", "Light", "Thermostat", "SmartDoorLock");

        dialog.setHeaderText("Select device type");
        dialog.showAndWait().ifPresent(type -> {

            String id = room.getRoomName().toLowerCase() + "_" + System.nanoTime();

            SmartDevice device = switch (type) {
                case "Thermostat" -> new Thermostat(id, "Thermostat", room.getRoomName());
                case "SmartDoorLock" -> new SmartDoorLock(id, "Door Lock", room.getRoomName());
                default -> new Light(id, "Light", room.getRoomName());
            };

            controller.addDeviceToRoom(room.getRoomName(), device);
            renderRooms();
            updateDashboardStats();
        });
    }

    private void removeDevice(Room room) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter device ID");
        dialog.showAndWait().ifPresent(id -> {
            controller.removeDevice(room.getRoomName(), id);
            renderRooms();
            updateDashboardStats();
        });
    }

    private void findDeviceById() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter device ID");
        dialog.showAndWait().ifPresent(id -> {
            SmartDevice d = controller.findDeviceById(id);
            showInfo(d == null ? "Not Found" : "Device Found",
                    d == null ? "No device with ID " + id : d.toString());
        });
    }

    private void listAllDevices() {
        StringBuilder sb = new StringBuilder();
        for (Room r : controller.getAllRooms())
            for (SmartDevice d : r.getDevices())
                sb.append(d).append("\n");

        showInfo("All Devices", sb.toString());
    }

    private void scheduleDevice(Schedulable device) {
        TextInputDialog dialog = new TextInputDialog("18:00");
        dialog.setHeaderText("Schedule (HH:mm)");
        dialog.showAndWait().ifPresent(time ->
                scheduler.scheduleDevice(device, time, "GUI"));
    }

    // =====================================================
    // ================= STATS =============================
    // =====================================================
    private void updateDashboardStats() {
        int total = 0, active = 0;

        for (Room r : controller.getAllRooms())
            for (SmartDevice d : r.getDevices()) {
                total++;
                if (d.isOn()) active++;
            }

        roomCountLabel.setText(String.valueOf(controller.getAllRooms().size()));
        deviceCountLabel.setText(String.valueOf(total));
        deviceStatusLabel.setText(active + " Active");
        energyUsageLabel.setText(String.format("%.2f kWh", active * 0.15));
        securityStatusLabel.setText(
                hasActive(SmartDoorLock.class) ? "ARMED" : "DISARMED");
    }

    private boolean hasActive(Class<? extends SmartDevice> type) {
        for (Room r : controller.getAllRooms())
            for (SmartDevice d : r.getDevices())
                if (type.isInstance(d) && d.isOn()) return true;
        return false;
    }

    private void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.setTitle(title);
        a.showAndWait();
    }

    @FunctionalInterface
    private interface DeviceAction {
        void apply(SmartDevice device);
    }
}
