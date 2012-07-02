package org.motekar.project.civics.archieve.assets.procurement.objects;

import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class InventoryCard {
    
    private Long index = Long.valueOf(0);
    private Unit warehouse = null;
    private String itemCode = "";
    private String itemName = "";
    //
    private String fullItemName = "";
    //
    private String itemsUnit = "";
    private String cardNumber = "";
    private String specification = "";
    
    public InventoryCard() {
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
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

    public String getItemsUnit() {
        return itemsUnit;
    }

    public void setItemsUnit(String itemsUnit) {
        this.itemsUnit = itemsUnit;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Unit getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Unit warehouse) {
        this.warehouse = warehouse;
    }
    
    @Override
    public String toString() {
        return itemName;
    }
}
