package org.motekar.project.civics.archieve.assets.inventory.objects;

import org.motekar.project.civics.archieve.assets.master.objects.Room;

/**
 *
 * @author Muhamad Wibawa
 */
public class RoomInventory {
    
    private Long index = Long.valueOf(0);
    private Room room = null;
    
    private boolean selected = false;
    
    public RoomInventory() {
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
}
