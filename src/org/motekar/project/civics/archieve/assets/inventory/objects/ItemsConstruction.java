package org.motekar.project.civics.archieve.assets.inventory.objects;

import java.math.BigDecimal;
import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsConstruction extends InventoryBook {
    
    private Long index = Long.valueOf(0);
    //
    private String fullItemName = "";
    //
    private Unit unit  = null;
    private String documentNumber = "";
    private Date documentDate = null;
    private String buildingCategory = "";
    private String raised = "";
    private String frameWork = "";
    private BigDecimal buildingWide = BigDecimal.ZERO;
    private String workType = "";
    private BigDecimal price = BigDecimal.ZERO;
    private String addressLocation = "";
    private Region urban = null;
    private Region subUrban = null;
    private Date startDate = null;
    private String landStatus = "";
    private String landCode = "";
    private String fundingSource = "";
    private String description = "";
    //
    private String userName = "";
    private String lastUserName = "";
    
    public ItemsConstruction() {
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
    }
    
    public String getAddressLocation() {
        return addressLocation;
    }

    public void setAddressLocation(String addressLocation) {
        this.addressLocation = addressLocation;
    }

    public String getBuildingCategory() {
        return buildingCategory;
    }

    public void setBuildingCategory(String buildingCategory) {
        this.buildingCategory = buildingCategory;
    }

    public BigDecimal getBuildingWide() {
        return buildingWide;
    }

    public void setBuildingWide(BigDecimal buildingWide) {
        this.buildingWide = buildingWide;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
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

    public String getLandCode() {
        return landCode;
    }

    public void setLandCode(String landCode) {
        this.landCode = landCode;
    }

    public String getLandStatus() {
        return landStatus;
    }

    public void setLandStatus(String landStatus) {
        this.landStatus = landStatus;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getRaised() {
        return raised;
    }

    public void setRaised(String raised) {
        this.raised = raised;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public Region getSubUrban() {
        return subUrban;
    }

    public void setSubUrban(Region subUrban) {
        this.subUrban = subUrban;
    }

    public Region getUrban() {
        return urban;
    }

    public void setUrban(Region urban) {
        this.urban = urban;
    }

    public String getFrameWork() {
        return frameWork;
    }

    public void setFrameWork(String frameWork) {
        this.frameWork = frameWork;
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
