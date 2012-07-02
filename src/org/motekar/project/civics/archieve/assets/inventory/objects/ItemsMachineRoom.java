package org.motekar.project.civics.archieve.assets.inventory.objects;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsMachineRoom extends RoomInventory{
    
    private ItemsMachine machine = null;
    
    public ItemsMachineRoom() {
    }

    public ItemsMachine getMachine() {
        return machine;
    }

    public void setMachine(ItemsMachine machine) {
        this.machine = machine;
    }

    @Override
    public String toString() {
        if (machine != null) {
            return machine.itemName;
        }
        return "";
    }
}
