package org.motekar.project.civics.archieve.assets.inventory.objects;

import java.math.BigDecimal;

/**
 *
 * @author Muhamad Wibawa
 */
public class DeleteDraftItems {
    
    private Long index = Long.valueOf(0);
    private String itemCode = "";
    private String itemName = "";
    private Long itemIndex = Long.valueOf(0);
    private Integer itemType = Integer.valueOf(0);
    private String locationCode = "";
    private String specificationType = "";
    private String ownedDocument = "";
    private Integer acquisitionYear = Integer.valueOf(0);
    private BigDecimal price = BigDecimal.ZERO;
    private String condition = "";
    private String description = "";
    
    private boolean selected = false;
    
    public DeleteDraftItems() {
    }

    public Integer getAcquisitionYear() {
        return acquisitionYear;
    }

    public void setAcquisitionYear(Integer acquisitionYear) {
        this.acquisitionYear = acquisitionYear;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(Long itemIndex) {
        this.itemIndex = itemIndex;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getOwnedDocument() {
        return ownedDocument;
    }

    public void setOwnedDocument(String ownedDocument) {
        this.ownedDocument = ownedDocument;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSpecificationType() {
        return specificationType;
    }

    public void setSpecificationType(String specificationType) {
        this.specificationType = specificationType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    @Override
    public String toString() {
        return itemName;
    }
}
