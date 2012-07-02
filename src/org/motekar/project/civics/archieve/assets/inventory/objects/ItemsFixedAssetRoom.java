package org.motekar.project.civics.archieve.assets.inventory.objects;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsFixedAssetRoom extends RoomInventory {
    
    private ItemsFixedAsset fixedAsset = null;
    
    public ItemsFixedAssetRoom() {
    }

    public ItemsFixedAsset getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(ItemsFixedAsset fixedAsset) {
        this.fixedAsset = fixedAsset;
    }
    
    @Override
    public String toString() {
        if (fixedAsset != null) {
            return fixedAsset.itemName;
        }
        return "";
    }
}
