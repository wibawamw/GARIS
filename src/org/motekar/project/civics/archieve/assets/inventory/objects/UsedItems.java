package org.motekar.project.civics.archieve.assets.inventory.objects;

import java.math.BigDecimal;

/**
 *
 * @author Muhamad Wibawa
 */
public class UsedItems {
    
    private Long index = Long.valueOf(0);
    private String itemCode = "";
    private String itemName = "";
    private Long itemIndex = Long.valueOf(0);
    private Integer itemType = Integer.valueOf(0);
    private String locationCode = "";
    private String registerNumber = "";
    private String documentNumber = "";
    private String itemAddress = "";
    private String acquisitionWay = "";
    private Integer acquisitionYear = Integer.valueOf(0);
    private String construction = "";
    private String condition = "";
    private BigDecimal wide = BigDecimal.ZERO;
    private BigDecimal price = BigDecimal.ZERO;
    private String decreeNumber = "";
    private String cooperationPeriod = "";
    private String thirdPartyAddress = "";
    private String description = "";
    
    private boolean selected = false;
    
    public UsedItems() {
    }

    public String getAcquisitionWay() {
        return acquisitionWay;
    }

    public void setAcquisitionWay(String acquisitionWay) {
        this.acquisitionWay = acquisitionWay;
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

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }

    public String getCooperationPeriod() {
        return cooperationPeriod;
    }

    public void setCooperationPeriod(String cooperationPeriod) {
        this.cooperationPeriod = cooperationPeriod;
    }

    public String getDecreeNumber() {
        return decreeNumber;
    }

    public void setDecreeNumber(String decreeNumber) {
        this.decreeNumber = decreeNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getItemAddress() {
        return itemAddress;
    }

    public void setItemAddress(String itemAddress) {
        this.itemAddress = itemAddress;
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

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getThirdPartyAddress() {
        return thirdPartyAddress;
    }

    public void setThirdPartyAddress(String thirdPartyAddress) {
        this.thirdPartyAddress = thirdPartyAddress;
    }

    public BigDecimal getWide() {
        return wide;
    }

    public void setWide(BigDecimal wide) {
        this.wide = wide;
    }

    public Long getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(Long itemIndex) {
        this.itemIndex = itemIndex;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }
    
    @Override
    public String toString() {
        return itemName;
    }
}
