package org.motekar.project.civics.archieve.assets.procurement.objects;

import java.math.BigDecimal;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class ThirdPartyItems {

    private Long index = Long.valueOf(0);
    //
    private String fullItemName = "";
    //
    private String itemCode = "";
    private String itemName = "";
    private String specificationType = "";
    private String specificationNumber = "";
    private Integer acquisitionYear = Integer.valueOf(0);
    private String acquisitionWay = "";
    private String thirdPartyName = "";
    private String itemsUnit = "";
    private String condition = "";
    private Integer amount = Integer.valueOf(0);
    private BigDecimal price = BigDecimal.ZERO;
    private String description = "";
    private Unit unit = null;
    
    private boolean selected = false;
    
    public ThirdPartyItems() {
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSpecificationNumber() {
        return specificationNumber;
    }

    public void setSpecificationNumber(String specificationNumber) {
        this.specificationNumber = specificationNumber;
    }

    public String getSpecificationType() {
        return specificationType;
    }

    public void setSpecificationType(String specificationType) {
        this.specificationType = specificationType;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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
