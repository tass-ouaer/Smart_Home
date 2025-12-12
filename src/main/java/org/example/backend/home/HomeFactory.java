package org.example.backend.home;

import org.example.backend.devices.*;
import org.example.backend.home.Home;
import org.example.backend.home.Room;


public class HomeFactory {

    public static Home createDefaultHome() {

        Home home = new Home("My Smart Home");

        // Kitchen
        Room kitchen = new Room("Kitchen");
        kitchen.addDevice(new Light("k_light", "Kitchen Light", "Kitchen"));
        kitchen.addDevice(new GasDetector("k_gas", "Gas Detector", "Kitchen"));
        home.addRoom(kitchen);

        // Living Room
        Room living = new Room("Living Room");
        living.addDevice(new Light("l_light", "Living Room Light", "Living Room"));
        living.addDevice(new SmartTV("tv", "Smart TV", "Living Room"));
        home.addRoom(living);

        // Bedroom
        Room bedroom = new Room("Bedroom");
        bedroom.addDevice(new Light("b_light", "Bedroom Light", "Bedroom"));
        bedroom.addDevice(new Thermostat("thermo", "Thermostat", "Bedroom"));
        home.addRoom(bedroom);

        // Bathroom
        Room bathroom = new Room("Bathroom");
        bathroom.addDevice(new Light("bath_light", "Bathroom Light", "Bathroom"));
        home.addRoom(bathroom);

        // Garden
        Room garden = new Room("Garden");
        garden.addDevice(new Sprinkler("sprinkler", "Garden Sprinkler", "Garden"));
        garden.addDevice(new MotionSensor("motion_garden", "Garden Motion Sensor", "Garden"));
        home.addRoom(garden);

        // Garage
        Room garage = new Room("Garage");
        garage.addDevice(new GarageDoor("garage_door", "Garage Door", "Garage"));
        garage.addDevice(new IntrusionSensor("intrusion_garage", "Garage Intrusion Sensor", "Garage"));
        home.addRoom(garage);

        // Entry Hall
        Room entryHall = new Room("Entry Hall");
        entryHall.addDevice(new SmartDoorLock("front_door", "Front Door Lock", "Entry Hall"));
        entryHall.addDevice(new IntrusionSensor("intrusion_entry", "Entry Intrusion Sensor", "Entry Hall"));
        entryHall.addDevice(new MotionSensor("motion_entry", "Entry Motion Sensor", "Entry Hall"));
        home.addRoom(entryHall);

        return home;
    }
}
