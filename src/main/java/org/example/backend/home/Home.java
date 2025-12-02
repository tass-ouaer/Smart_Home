package backend.home;

import java.util.ArrayList;
import java.util.List;

/**
 * The Home class represents the entire smart home system.
 * It contains a name and a list of Room objects.
 *
 * LOGIC:
 * - The GUI needs to display all rooms → so we keep an internal list.
 * - We provide add/remove/find methods to keep the HomePageController clean.
 * - The controller will NOT modify the list directly; it uses these methods.
 */
public class Home {

    private String homeName;
    private List<Room> rooms;

    // -----------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------
    public Home(String homeName) {
        this.homeName = homeName;
        this.rooms = new ArrayList<>();
    }

    // -----------------------------------------------------------
    // Basic Getters
    // -----------------------------------------------------------
    public String getHomeName() {
        return homeName;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    // -----------------------------------------------------------
    // Room Management
    // -----------------------------------------------------------

    /**
     * Adds a new room into the home, only if it doesn’t already exist.
     *
     * REASONING:
     * - Avoid duplicated room names because controllers and later logic
     *   will use room name as an identifier.
     */
    public void addRoom(Room room) {
        if (room == null) return;


        for (Room r : rooms) {
            if (r.getRoomName().equalsIgnoreCase(room.getRoomName())) {
                return; // Do nothing (you can throw an exception if you prefer)
            }
        }

        rooms.add(room);
    }

    /**
     * Removes a room by name.
     *
     * REASONING:
     * - GUI (RoomPageController) may need to delete a room dynamically.
     */
    public boolean removeRoom(String roomName) {
        if (roomName == null) return false;

        return rooms.removeIf(r -> r.getRoomName().equalsIgnoreCase(roomName));
    }

    /**
     * Returns a room by name.
     *
     * REASONING:
     * - GUI navigates to a room page when user clicks a room card in the interface.
     * - PageManager will call this to open "room.fxml".
     */
    public Room getRoom(String roomName) {
        if (roomName == null) return null;

        for (Room r : rooms) {
            if (r.getRoomName().equalsIgnoreCase(roomName)) {
                return r;
            }
        }
        return null;
    }
}
