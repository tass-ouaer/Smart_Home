package smarthome.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.*;
import javafx.beans.property.*;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    
    // =========== DYNAMIC NAVIGATION ===========
    @FXML private VBox roomNavBox;
    @FXML private VBox deviceNavBox;
    @FXML private VBox automationNavBox;
    
    @FXML private Label roomCountLabel;
    @FXML private Label deviceCountLabel;
    @FXML private Label deviceStatusLabel;
    @FXML private Label automationCountLabel;
    @FXML private Label automationStatusLabel;
    
    @FXML private Button addRoomButton;
    @FXML private Button addDeviceButton;
    @FXML private Button addAutomationButton;
    
    // =========== STATUS CARDS ===========
    @FXML private Label securityStatusLabel;
    @FXML private Label energyUsageLabel;
    @FXML private Label activeDevicesLabel;
    
    // =========== QUICK ACTIONS ===========
    @FXML private Button allLightsOnBtn;
    @FXML private Button allLightsOffBtn;
    @FXML private Button lockAllDoorsBtn;
    @FXML private Button awayModeBtn;
    
    // =========== ROOM SECTIONS ===========
    @FXML private VBox room1Container;
    @FXML private VBox room2Container;
    
    @FXML private Label room1SecurityStatus;
    @FXML private ToggleButton room1DoorLock;
    @FXML private Label room1CameraStatus;
    
    // =========== DATA MODELS ===========
    private ObservableList<Room> rooms = FXCollections.observableArrayList();
    private ObservableList<Device> devices = FXCollections.observableArrayList();
    private ObservableList<Automation> automations = FXCollections.observableArrayList();
    
    // Home state
    private boolean allLightsOn = false;
    private boolean securityArmed = true;
    private double energyUsage = 2.3;
    private int activeDeviceCount = 8;
    private int totalDeviceCount = 12;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupNavigation();
        initializeSampleData();
        setupEventHandlers();
        updateUI();
    }
    
    private void setupNavigation() {
        // Make navigation boxes clickable
        roomNavBox.setOnMouseClicked(e -> showRoomDetails());
        roomNavBox.setStyle("-fx-cursor: hand;");
        
        deviceNavBox.setOnMouseClicked(e -> showDeviceList());
        deviceNavBox.setStyle("-fx-cursor: hand;");
        
        automationNavBox.setOnMouseClicked(e -> showAutomationList());
        automationNavBox.setStyle("-fx-cursor: hand;");
        
        // Add button actions
        addRoomButton.setOnAction(e -> addNewRoom());
        addDeviceButton.setOnAction(e -> addNewDevice());
        addAutomationButton.setOnAction(e -> addNewAutomation());
    }
    
    private void initializeSampleData() {
        // Create sample rooms
        Room room1 = new Room("Room 1");
        room1.addDevice(new Device("Entry Door Lock", "Security", true));
        room1.addDevice(new Device("Security Camera", "Camera", true));
        rooms.add(room1);
        
        Room room2 = new Room("Room 2");
        rooms.add(room2);
        
        // Add more devices
        devices.addAll(
            new Device("Living Room Light", "Light", false),
            new Device("Kitchen Light", "Light", true),
            new Device("Bedroom Light", "Light", false),
            new Device("Hallway Light", "Light", true),
            new Device("Thermostat", "Climate", true)
        );
        
        // Create sample automations
        automations.addAll(
            new Automation("Night Mode", "Turn off lights at 11 PM", true),
            new Automation("Away Mode", "Activate security when away", true),
            new Automation("Morning Routine", "Lights on at 7 AM", false)
        );
    }
    
    private void setupEventHandlers() {
        // Quick Actions
        allLightsOnBtn.setOnAction(e -> turnAllLightsOn());
        allLightsOffBtn.setOnAction(e -> turnAllLightsOff());
        lockAllDoorsBtn.setOnAction(e -> lockAllDoors());
        awayModeBtn.setOnAction(e -> activateAwayMode());
        
        // Room 1 controls
        room1DoorLock.setOnAction(e -> toggleRoom1DoorLock());
    }
    
    private void updateUI() {
        // Update navigation counts
        roomCountLabel.setText(String.valueOf(rooms.size()));
        deviceCountLabel.setText(String.valueOf(totalDeviceCount));
        
        // Calculate active devices
        activeDeviceCount = 0;
        for (Device device : devices) {
            if (device.isActive()) activeDeviceCount++;
        }
        for (Room room : rooms) {
            for (Device device : room.getDevices()) {
                if (device.isActive()) activeDeviceCount++;
            }
        }
        
        deviceStatusLabel.setText(activeDeviceCount + " Active");
        activeDevicesLabel.setText(activeDeviceCount + "/" + totalDeviceCount);
        
        // Update automation counts
        int runningAutomations = 0;
        for (Automation automation : automations) {
            if (automation.isActive()) runningAutomations++;
        }
        automationCountLabel.setText(String.valueOf(automations.size()));
        automationStatusLabel.setText(runningAutomations + " Running");
        
        // Update security status
        securityStatusLabel.setText(securityArmed ? "ARMED" : "DISARMED");
        securityStatusLabel.setStyle(securityArmed ? 
            "-fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold; -fx-background-color: #dc2626; -fx-background-radius: 10; -fx-padding: 4 8;" :
            "-fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold; -fx-background-color: #059669; -fx-background-radius: 10; -fx-padding: 4 8;"
        );
        
        // Update energy usage based on active devices
        energyUsage = 1.5 + (activeDeviceCount * 0.1);
        energyUsageLabel.setText(String.format("%.1f kWh", energyUsage));
    }
    
    // =========== NAVIGATION METHODS ===========
    @FXML
    private void showRoomDetails() {
        highlightNavigation(roomNavBox);
        showAlert("Rooms", "Total Rooms: " + rooms.size());
    }
    
    @FXML
    private void showDeviceList() {
        highlightNavigation(deviceNavBox);
        showAlert("Devices", "Total Devices: " + totalDeviceCount + "\nActive: " + activeDeviceCount);
    }
    
    @FXML
    private void showAutomationList() {
        highlightNavigation(automationNavBox);
        showAlert("Automations", "Total Automations: " + automations.size());
    }
    
    private void highlightNavigation(VBox navBox) {
        // Reset all
        roomNavBox.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        deviceNavBox.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        automationNavBox.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        
        // Highlight selected
        navBox.setStyle("-fx-background-color: rgba(59, 130, 246, 0.1); -fx-background-radius: 8; -fx-padding: 5; -fx-cursor: hand;");
    }
    
    // =========== QUICK ACTION METHODS ===========
    @FXML
    private void turnAllLightsOn() {
        allLightsOn = true;
        for (Device device : devices) {
            if (device.getType().equals("Light")) {
                device.setActive(true);
            }
        }
        updateUI();
        showAlert("Lights", "All lights turned ON");
    }
    
    @FXML
    private void turnAllLightsOff() {
        allLightsOn = false;
        for (Device device : devices) {
            if (device.getType().equals("Light")) {
                device.setActive(false);
            }
        }
        updateUI();
        showAlert("Lights", "All lights turned OFF");
    }
    
    @FXML
    private void lockAllDoors() {
        securityArmed = true;
        for (Device device : devices) {
            if (device.getType().equals("Security")) {
                device.setActive(true);
            }
        }
        updateUI();
        showAlert("Security", "All doors locked and security armed");
    }
    
    @FXML
    private void activateAwayMode() {
        securityArmed = true;
        // Turn off most lights
        for (Device device : devices) {
            if (device.getType().equals("Light")) {
                device.setActive(false);
            }
            if (device.getType().equals("Security")) {
                device.setActive(true);
            }
        }
        updateUI();
        showAlert("Away Mode", "Away mode activated. Security armed, lights off.");
    }
    
    // =========== ROOM CONTROL METHODS ===========
    @FXML
    private void toggleRoom1DoorLock() {
        boolean isLocked = room1DoorLock.isSelected();
        room1DoorLock.setText(isLocked ? "UNLOCKED" : "LOCKED");
        room1DoorLock.setStyle(isLocked ? 
            "-fx-background-color: #059669; -fx-text-fill: white;" :
            "-fx-background-color: #dc2626; -fx-text-fill: white;"
        );
        
        // Update the device in room
        for (Room room : rooms) {
            if (room.getName().equals("Room 1")) {
                for (Device device : room.getDevices()) {
                    if (device.getName().contains("Door")) {
                        device.setActive(!isLocked); // Inverted because selected means unlocked
                    }
                }
            }
        }
        updateUI();
    }
    
    // =========== ADD METHODS ===========
    @FXML
    private void addNewRoom() {
        TextInputDialog dialog = new TextInputDialog("New Room");
        dialog.setTitle("Add Room");
        dialog.setHeaderText("Enter room name:");
        dialog.setContentText("Name:");
        
        dialog.showAndWait().ifPresent(name -> {
            Room newRoom = new Room(name);
            rooms.add(newRoom);
            updateUI();
            showAlert("Room Added", "New room '" + name + "' added successfully");
        });
    }
    
    @FXML
    private void addNewDevice() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Light", "Light", "Security", "Camera", "Climate", "Entertainment");
        dialog.setTitle("Add Device");
        dialog.setHeaderText("Select device type:");
        dialog.setContentText("Type:");
        
        dialog.showAndWait().ifPresent(type -> {
            TextInputDialog nameDialog = new TextInputDialog("New " + type);
            nameDialog.setTitle("Device Name");
            nameDialog.setHeaderText("Enter device name:");
            nameDialog.setContentText("Name:");
            
            nameDialog.showAndWait().ifPresent(name -> {
                Device newDevice = new Device(name, type, false);
                devices.add(newDevice);
                totalDeviceCount++;
                updateUI();
                showAlert("Device Added", "New " + type + " device added: " + name);
            });
        });
    }
    
    @FXML
    private void addNewAutomation() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Automation");
        dialog.setHeaderText("Create new automation rule");
        dialog.setContentText("Rule name:");
        
        dialog.showAndWait().ifPresent(name -> {
            Automation newAuto = new Automation(name, "New automation rule", true);
            automations.add(newAuto);
            updateUI();
            showAlert("Automation Added", "New automation rule created: " + name);
        });
    }
    
    // =========== HELPER METHODS ===========
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

// =========== DATA MODEL CLASSES ===========
class Room {
    private String name;
    private List<Device> devices = new ArrayList<>();
    
    public Room(String name) {
        this.name = name;
    }
    
    public String getName() { return name; }
    public List<Device> getDevices() { return devices; }
    public void addDevice(Device device) { devices.add(device); }
}

class Device {
    private String name;
    private String type;
    private boolean active;
    
    public Device(String name, String type, boolean active) {
        this.name = name;
        this.type = type;
        this.active = active;
    }
    
    public String getName() { return name; }
    public String getType() { return type; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

class Automation {
    private String name;
    private String description;
    private boolean active;
    
    public Automation(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }
    
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}