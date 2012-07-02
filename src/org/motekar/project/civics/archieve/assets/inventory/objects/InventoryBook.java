package org.motekar.project.civics.archieve.assets.inventory.objects;

import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class InventoryBook {
    
    protected String itemCode = "";
    protected String itemName = "";
    protected String regNumber = "";
    protected boolean selected = false;
    
    public InventoryBook() {
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getShortName() {
        String shortName = "";
        if (itemName != null) {
            StringTokenizer token = new StringTokenizer(itemName,">");
            while(token.hasMoreElements()) {
                shortName = token.nextToken();
            }
        }
        return shortName;
    }

    @Override
    public String toString() {
        return itemName;
    }
}
