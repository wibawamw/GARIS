package org.motekar.project.civics.archieve.assets.inventory.objects;

import java.math.BigDecimal;
import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsBuilding extends InventoryBook {
    
    private Long index = Long.valueOf(0);
    //
    private String fullItemName = "";
    //
    private Unit unit = null;
    private BigDecimal wide = BigDecimal.ZERO;
    private String raised =  "";
    
    private Condition condition = null;
    private String conditionText = "";
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal adminCost = BigDecimal.ZERO;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private Integer acquisitionYear = Integer.valueOf(0);
    private String address = "";
    private Region urban = null;
    private Region subUrban = null;
    private Date documentDate = null;
    private String documentNumber = null;
    private BigDecimal landWide = BigDecimal.ZERO;
    private String landCode = "";
    private String landState = "";
    private String fundingSource = "";
    private String acquitionWay = "";
    private String description = "";
    private String eastLongitudeCoord = "";
    private String southLatitudeCoord = "";
    //
    private String ownerShipStateName = "";
    private String ownerShipRelation = "";
    private String ownerShipOccupancy = "";
    private String ownerShipOccupaying = "";
    //
    private String landTaxNumber = "";
    private Date landTaxDate = null;
    private String buildPermitNumber = "";
    private Date buildPermitDate = null;
    private String advisPlanningNumber = "";
    private Date advisPlanningDate = null;
    //
    private String type = "";
    private String shape = "";
    private String utilization = "";
    private String ages = "";
    private String levels = "";
    private String frameworks = "";
    private String foundation = "";
    private String floorType = "";
    private String wallType = "";
    private String ceillingType = "";
    private String roofType = "";
    private String frameType = "";
    private String doorType = "";
    private String windowType = "";
    private String roomType = "";
    private String materialQuality = "";
    private String facilities = "";
    private Condition buildingCondition = null;
    private String nursing = "";
    private String buildingClass = "";
    //
    private String allotmentPoint = "";
    private String utilizationPoint = "";
    private String locationPoint = "";
    private String accesionPoint = "";
    private String qualityPoint = "";
    private String conditionPoint = "";
    private String layoutPoint = "";
    private String nursingPoint = "";
    private String marketInterestPoint = "";
    //
    private String userName = "";
    private String lastUserName = "";
    
    public ItemsBuilding() {
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
    }
    
    public String getAccesionPoint() {
        return accesionPoint;
    }

    public void setAccesionPoint(String accesionPoint) {
        this.accesionPoint = accesionPoint;
    }

    public Integer getAcquisitionYear() {
        return acquisitionYear;
    }

    public void setAcquisitionYear(Integer acquisitionYear) {
        this.acquisitionYear = acquisitionYear;
    }

    public String getAcquitionWay() {
        return acquitionWay;
    }

    public void setAcquitionWay(String acquitionWay) {
        this.acquitionWay = acquitionWay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getAdvisPlanningDate() {
        return advisPlanningDate;
    }

    public void setAdvisPlanningDate(Date advisPlanningDate) {
        this.advisPlanningDate = advisPlanningDate;
    }

    public String getAdvisPlanningNumber() {
        return advisPlanningNumber;
    }

    public void setAdvisPlanningNumber(String advisPlanningNumber) {
        this.advisPlanningNumber = advisPlanningNumber;
    }

    public String getAges() {
        return ages;
    }

    public void setAges(String ages) {
        this.ages = ages;
    }

    public String getAllotmentPoint() {
        return allotmentPoint;
    }

    public void setAllotmentPoint(String allotmentPoint) {
        this.allotmentPoint = allotmentPoint;
    }

    public Date getBuildPermitDate() {
        return buildPermitDate;
    }

    public void setBuildPermitDate(Date buildPermitDate) {
        this.buildPermitDate = buildPermitDate;
    }

    public String getBuildPermitNumber() {
        return buildPermitNumber;
    }

    public void setBuildPermitNumber(String buildPermitNumber) {
        this.buildPermitNumber = buildPermitNumber;
    }

    public String getBuildingClass() {
        return buildingClass;
    }

    public void setBuildingClass(String buildingClass) {
        this.buildingClass = buildingClass;
    }

    public Condition getBuildingCondition() {
        return buildingCondition;
    }

    public void setBuildingCondition(Condition buildingCondition) {
        this.buildingCondition = buildingCondition;
    }

    public String getCeillingType() {
        return ceillingType;
    }

    public void setCeillingType(String ceillingType) {
        this.ceillingType = ceillingType;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getConditionPoint() {
        return conditionPoint;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }
    
    public void setConditionPoint(String conditionPoint) {
        this.conditionPoint = conditionPoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDoorType() {
        return doorType;
    }

    public void setDoorType(String doorType) {
        this.doorType = doorType;
    }

    public String getEastLongitudeCoord() {
        return eastLongitudeCoord;
    }

    public void setEastLongitudeCoord(String eastLongitudeCoord) {
        this.eastLongitudeCoord = eastLongitudeCoord;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getFloorType() {
        return floorType;
    }

    public void setFloorType(String floorType) {
        this.floorType = floorType;
    }

    public String getFoundation() {
        return foundation;
    }

    public void setFoundation(String foundation) {
        this.foundation = foundation;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }

    public String getFrameType() {
        return frameType;
    }

    public void setFrameType(String frameType) {
        this.frameType = frameType;
    }

    public String getFrameworks() {
        return frameworks;
    }

    public void setFrameworks(String frameworks) {
        this.frameworks = frameworks;
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

    public String getLandState() {
        return landState;
    }

    public void setLandState(String landState) {
        this.landState = landState;
    }

    public Date getLandTaxDate() {
        return landTaxDate;
    }

    public void setLandTaxDate(Date landTaxDate) {
        this.landTaxDate = landTaxDate;
    }

    public String getLandTaxNumber() {
        return landTaxNumber;
    }

    public void setLandTaxNumber(String landTaxNumber) {
        this.landTaxNumber = landTaxNumber;
    }

    public BigDecimal getLandWide() {
        return landWide;
    }

    public void setLandWide(BigDecimal landWide) {
        this.landWide = landWide;
    }

    public String getLayoutPoint() {
        return layoutPoint;
    }

    public void setLayoutPoint(String layoutPoint) {
        this.layoutPoint = layoutPoint;
    }

    public String getLevels() {
        return levels;
    }

    public void setLevels(String levels) {
        this.levels = levels;
    }

    public String getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(String locationPoint) {
        this.locationPoint = locationPoint;
    }

    public String getMarketInterestPoint() {
        return marketInterestPoint;
    }

    public void setMarketInterestPoint(String marketInterestPoint) {
        this.marketInterestPoint = marketInterestPoint;
    }

    public String getMaterialQuality() {
        return materialQuality;
    }

    public void setMaterialQuality(String materialQuality) {
        this.materialQuality = materialQuality;
    }

    public String getNursing() {
        return nursing;
    }

    public void setNursing(String nursing) {
        this.nursing = nursing;
    }

    public String getNursingPoint() {
        return nursingPoint;
    }

    public void setNursingPoint(String nursingPoint) {
        this.nursingPoint = nursingPoint;
    }

    public String getOwnerShipOccupancy() {
        return ownerShipOccupancy;
    }

    public void setOwnerShipOccupancy(String ownerShipOccupancy) {
        this.ownerShipOccupancy = ownerShipOccupancy;
    }

    public String getOwnerShipOccupaying() {
        return ownerShipOccupaying;
    }

    public void setOwnerShipOccupaying(String ownerShipOccupaying) {
        this.ownerShipOccupaying = ownerShipOccupaying;
    }

    public String getOwnerShipRelation() {
        return ownerShipRelation;
    }

    public void setOwnerShipRelation(String ownerShipRelation) {
        this.ownerShipRelation = ownerShipRelation;
    }

    public String getOwnerShipStateName() {
        return ownerShipStateName;
    }

    public void setOwnerShipStateName(String ownerShipStateName) {
        this.ownerShipStateName = ownerShipStateName;
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
    
    public String getQualityPoint() {
        return qualityPoint;
    }

    public void setQualityPoint(String qualityPoint) {
        this.qualityPoint = qualityPoint;
    }

    public String getRaised() {
        return raised;
    }

    public void setRaised(String raised) {
        this.raised = raised;
    }

    public String getRoofType() {
        return roofType;
    }

    public void setRoofType(String roofType) {
        this.roofType = roofType;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getSouthLatitudeCoord() {
        return southLatitudeCoord;
    }

    public void setSouthLatitudeCoord(String southLatitudeCoord) {
        this.southLatitudeCoord = southLatitudeCoord;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getUtilization() {
        return utilization;
    }

    public void setUtilization(String utilization) {
        this.utilization = utilization;
    }

    public String getUtilizationPoint() {
        return utilizationPoint;
    }

    public void setUtilizationPoint(String utilizationPoint) {
        this.utilizationPoint = utilizationPoint;
    }

    public String getWallType() {
        return wallType;
    }

    public void setWallType(String wallType) {
        this.wallType = wallType;
    }

    public BigDecimal getWide() {
        return wide;
    }

    public void setWide(BigDecimal wide) {
        this.wide = wide;
    }

    public String getWindowType() {
        return windowType;
    }

    public void setWindowType(String windowType) {
        this.windowType = windowType;
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
