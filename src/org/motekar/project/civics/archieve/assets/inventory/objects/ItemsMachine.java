package org.motekar.project.civics.archieve.assets.inventory.objects;

import java.math.BigDecimal;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsMachine extends InventoryBook {
    
    private Long index = Long.valueOf(0);
    
    private String fullItemName = "";
    
    private Unit unit = null;
    
    private String bpkbNumber = "";
    private String machineType = "";
    private Integer volume = Integer.valueOf(0);
    private String factoryNumber = "";
    private String fabricNumber = "";
    private String machineNumber = "";
    private String policeNumber = "";
    private Integer acquisitionYear = Integer.valueOf(0);
    private Integer yearBuilt = Integer.valueOf(0);
    private String location = "";
    private String presentLocation = "";
    private String material = "";
    
    private Condition condition = null;
    
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal adminCost = BigDecimal.ZERO;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private String fundingSource = "";
    private String acquisitionWay = "";
    private String description = "";
    //
    private String userName = "";
    private String lastUserName = "";
    
    public ItemsMachine() {
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

    public String getBpkbNumber() {
        return bpkbNumber;
    }

    public void setBpkbNumber(String bpkbNumber) {
        this.bpkbNumber = bpkbNumber;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFabricNumber() {
        return fabricNumber;
    }

    public void setFabricNumber(String fabricNumber) {
        this.fabricNumber = fabricNumber;
    }

    public String getFactoryNumber() {
        return factoryNumber;
    }

    public void setFactoryNumber(String factoryNumber) {
        this.factoryNumber = factoryNumber;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMachineNumber() {
        return machineNumber;
    }

    public void setMachineNumber(String machineNumber) {
        this.machineNumber = machineNumber;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPoliceNumber() {
        return policeNumber;
    }

    public void setPoliceNumber(String policeNumber) {
        this.policeNumber = policeNumber;
    }

    public String getPresentLocation() {
        return presentLocation;
    }

    public void setPresentLocation(String presentLocation) {
        this.presentLocation = presentLocation;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAdminCost() {
        return adminCost;
    }

    public void setAdminCost(BigDecimal adminCost) {
        this.adminCost = adminCost;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public BigDecimal getTotalPrice() {
        totalPrice = BigDecimal.valueOf(0);
        if (price != null) {
            totalPrice = totalPrice.add(price);
        }
        
        if (adminCost != null) {
            totalPrice = totalPrice.add(adminCost);
        }
        
        return totalPrice;
    }
    
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(Integer yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getLastUserName() {
        return lastUserName;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @Override
    public String toString() {
        return itemName;
    }
}
