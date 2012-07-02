package org.motekar.project.civics.archieve.assets.inventory.objects;

import java.math.BigDecimal;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemStatus {

    private String itemName = "";
    private String itemCode = "";
    private String machineType = "";
    private String machineNumber = "";
    private Integer measure = Integer.valueOf(0);
    private String material = "";
    private Integer acquisitionYear = Integer.valueOf(0);
    private Integer amount = Integer.valueOf(0);
    private BigDecimal price = BigDecimal.ZERO;
    private String good = "";
    private String notGood = "";
    private String description = "";
    
    public ItemStatus() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
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

    public String getMachineNumber() {
        return machineNumber;
    }

    public void setMachineNumber(String machineNumber) {
        this.machineNumber = machineNumber;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Integer getMeasure() {
        return measure;
    }

    public void setMeasure(Integer measure) {
        this.measure = measure;
    }

    public String getNotGood() {
        return notGood;
    }

    public void setNotGood(String notGood) {
        this.notGood = notGood;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
}
