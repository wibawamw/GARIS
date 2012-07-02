package org.motekar.project.civics.archieve.assets.inventory.objects;

import java.math.BigDecimal;
import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsLand extends InventoryBook {

    private Long index = Long.valueOf(0) ;
    //
    private String fullItemName = "";
    //
    private Unit unit;
    private BigDecimal wide = BigDecimal.ZERO;
    private String addressLocation = "";
    private Region urban = null;
    private Region subUrban = null;
    private BigDecimal landPrice = BigDecimal.ZERO;
    private Integer acquisitionYear = Integer.valueOf(0);
    private String fundingSource = "";
    private String acquisitionWay = "";
    private String description = "";
    //
    private String ownerShipStateName = "";
    private String ownerShipNumberState = "";
    private Date ownerShipDate = null;
    private String ownerShipIssuedBy = "";
    private String owner = "";
    private String ownerShipStatus = "";
    private String ownerShipRelation = "";
    private String ownerShipOccupancy = "";
    private String ownerShipOccupying = "";
    //
    private String landCertificateNumber = "";
    private Date landCertificateDate = null;
    private String landTaxNumber = "";
    private Date landTaxDate = null;
    private String buildPermitNumber = "";
    private Date buildPermitDate = null;
    private String advisPlanningNumber = "";
    private Date advisPlanningDate = null;
    //
    private String landCondition = "";
    private String shape = "";
    private String topographCondition = "";
    private String currentUse = "";
    private String closerRoad = "";
    private String roadSurface = "";
    private String heightAbove = "";
    private String heightUnder = "";
    private String elevation = "";
    private String allotment = "";
    private BigDecimal priceBasedChief = BigDecimal.ZERO;
    private BigDecimal priceBasedNJOP = BigDecimal.ZERO;
    private BigDecimal priceBasedExam = BigDecimal.ZERO;
    private String priceDescription = "";
    //
    private String location = "";
    private String position = "";
    private String roadAccess = "";
    private String roadWidth = "";
    private String pavement = "";
    private String traffic = "";
    private String topographLocation = "";
    private String environment = "";
    private String density = "";
    private String socialLevels = "";
    private String facilities = "";
    private String drainage = "";
    private String publicTransportation = "";
    private String security = "";
    //
    private String securityDisturbance = "";
    private String floodDangers = "";
    private String locationEffect = "";
    private String environmentalInfluences = "";
    //
    private String allotmentPoint = "";
    private String utilizationPoint = "";
    private String locationPoint = "";
    private String accessionPoint = "";
    private String nursingPoint = "";
    private String soilConditionsPoint = "";
    private String marketInterestPoint = "";
    //
    private BigDecimal comparativePrice = BigDecimal.ZERO;
    private BigDecimal comparativeWide = BigDecimal.ZERO;
    private String comparativeSource = "";
    private BigDecimal comparativePrice2 = BigDecimal.ZERO;
    private BigDecimal comparativeWide2 = BigDecimal.ZERO;
    private String comparativeSource2 = "";
    private BigDecimal comparativePrice3 = BigDecimal.ZERO;
    private BigDecimal comparativeWide3 = BigDecimal.ZERO;
    private String comparativeSource3 = "";
    private BigDecimal comparativePrice4 = BigDecimal.ZERO;
    private BigDecimal comparativeWide4 = BigDecimal.ZERO;
    private String comparativeSource4 = "";
    private BigDecimal comparativePrice5 = BigDecimal.ZERO;
    private BigDecimal comparativeWide5 = BigDecimal.ZERO;
    private String comparativeSource5 = "";
    private String landAddress = "";
    private Integer njopYear = Integer.valueOf(0);
    private BigDecimal njopLandwide = BigDecimal.ZERO;
    private String njopLandClass = "";
    private BigDecimal njopLandValue = BigDecimal.ZERO;
    
    //
    
    private String userName = "";
    private String lastUserName = "";
    
    
    private String completeName = "";
    
    public ItemsLand() {
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
    }
    
    public String getAccessionPoint() {
        return accessionPoint;
    }

    public void setAccessionPoint(String accessionPoint) {
        this.accessionPoint = accessionPoint;
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

    public String getAllotment() {
        return allotment;
    }

    public void setAllotment(String allotment) {
        this.allotment = allotment;
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

    public BigDecimal getComparativePrice() {
        return comparativePrice;
    }

    public void setComparativePrice(BigDecimal comparativePrice) {
        this.comparativePrice = comparativePrice;
    }

    public BigDecimal getComparativePrice2() {
        return comparativePrice2;
    }

    public void setComparativePrice2(BigDecimal comparativePrice2) {
        this.comparativePrice2 = comparativePrice2;
    }

    public BigDecimal getComparativePrice3() {
        return comparativePrice3;
    }

    public void setComparativePrice3(BigDecimal comparativePrice3) {
        this.comparativePrice3 = comparativePrice3;
    }

    public BigDecimal getComparativePrice4() {
        return comparativePrice4;
    }

    public void setComparativePrice4(BigDecimal comparativePrice4) {
        this.comparativePrice4 = comparativePrice4;
    }

    public BigDecimal getComparativePrice5() {
        return comparativePrice5;
    }

    public void setComparativePrice5(BigDecimal comparativePrice5) {
        this.comparativePrice5 = comparativePrice5;
    }

    public String getComparativeSource() {
        return comparativeSource;
    }

    public void setComparativeSource(String comparativeSource) {
        this.comparativeSource = comparativeSource;
    }

    public String getComparativeSource2() {
        return comparativeSource2;
    }

    public void setComparativeSource2(String comparativeSource2) {
        this.comparativeSource2 = comparativeSource2;
    }

    public String getComparativeSource3() {
        return comparativeSource3;
    }

    public void setComparativeSource3(String comparativeSource3) {
        this.comparativeSource3 = comparativeSource3;
    }

    public String getComparativeSource4() {
        return comparativeSource4;
    }

    public void setComparativeSource4(String comparativeSource4) {
        this.comparativeSource4 = comparativeSource4;
    }

    public String getComparativeSource5() {
        return comparativeSource5;
    }

    public void setComparativeSource5(String comparativeSource5) {
        this.comparativeSource5 = comparativeSource5;
    }

    public BigDecimal getComparativeWide() {
        return comparativeWide;
    }

    public void setComparativeWide(BigDecimal comparativeWide) {
        this.comparativeWide = comparativeWide;
    }

    public BigDecimal getComparativeWide2() {
        return comparativeWide2;
    }

    public void setComparativeWide2(BigDecimal comparativeWide2) {
        this.comparativeWide2 = comparativeWide2;
    }

    public BigDecimal getComparativeWide3() {
        return comparativeWide3;
    }

    public void setComparativeWide3(BigDecimal comparativeWide3) {
        this.comparativeWide3 = comparativeWide3;
    }

    public BigDecimal getComparativeWide4() {
        return comparativeWide4;
    }

    public void setComparativeWide4(BigDecimal comparativeWide4) {
        this.comparativeWide4 = comparativeWide4;
    }

    public BigDecimal getComparativeWide5() {
        return comparativeWide5;
    }

    public void setComparativeWide5(BigDecimal comparativeWide5) {
        this.comparativeWide5 = comparativeWide5;
    }

    public String getCurrentUse() {
        return currentUse;
    }

    public void setCurrentUse(String currentUse) {
        this.currentUse = currentUse;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getDrainage() {
        return drainage;
    }

    public void setDrainage(String drainage) {
        this.drainage = drainage;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getEnvironmentalInfluences() {
        return environmentalInfluences;
    }

    public void setEnvironmentalInfluences(String environmentalInfluences) {
        this.environmentalInfluences = environmentalInfluences;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getFloodDangers() {
        return floodDangers;
    }

    public void setFloodDangers(String floodDangers) {
        this.floodDangers = floodDangers;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }

    public String getHeightAbove() {
        return heightAbove;
    }

    public void setHeightAbove(String heightAbove) {
        this.heightAbove = heightAbove;
    }

    public String getHeightUnder() {
        return heightUnder;
    }

    public void setHeightUnder(String heightUnder) {
        this.heightUnder = heightUnder;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getLandAddress() {
        return landAddress;
    }

    public void setLandAddress(String landAddress) {
        this.landAddress = landAddress;
    }

    public Date getLandCertificateDate() {
        return landCertificateDate;
    }

    public void setLandCertificateDate(Date landCertificateDate) {
        this.landCertificateDate = landCertificateDate;
    }

    public String getLandCertificateNumber() {
        return landCertificateNumber;
    }

    public void setLandCertificateNumber(String landCertificateNumber) {
        this.landCertificateNumber = landCertificateNumber;
    }

    public String getLandCondition() {
        return landCondition;
    }

    public void setLandCondition(String landCondition) {
        this.landCondition = landCondition;
    }

    public BigDecimal getLandPrice() {
        return landPrice;
    }

    public void setLandPrice(BigDecimal landPrice) {
        this.landPrice = landPrice;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationEffect() {
        return locationEffect;
    }

    public void setLocationEffect(String locationEffect) {
        this.locationEffect = locationEffect;
    }

    public String getMarketInterestPoint() {
        return marketInterestPoint;
    }

    public void setMarketInterestPoint(String marketInterestPoint) {
        this.marketInterestPoint = marketInterestPoint;
    }

    public String getNjopLandClass() {
        return njopLandClass;
    }

    public void setNjopLandClass(String njopLandClass) {
        this.njopLandClass = njopLandClass;
    }

    public BigDecimal getNjopLandValue() {
        return njopLandValue;
    }

    public void setNjopLandValue(BigDecimal njopLandValue) {
        this.njopLandValue = njopLandValue;
    }

    public BigDecimal getNjopLandwide() {
        return njopLandwide;
    }

    public void setNjopLandwide(BigDecimal njopLandwide) {
        this.njopLandwide = njopLandwide;
    }

    public Integer getNjopYear() {
        return njopYear;
    }

    public void setNjopYear(Integer njopYear) {
        this.njopYear = njopYear;
    }

    public String getNursingPoint() {
        return nursingPoint;
    }

    public void setNursingPoint(String nursingPoint) {
        this.nursingPoint = nursingPoint;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getOwnerShipDate() {
        return ownerShipDate;
    }

    public void setOwnerShipDate(Date ownerShipDate) {
        this.ownerShipDate = ownerShipDate;
    }

    public String getOwnerShipIssuedBy() {
        return ownerShipIssuedBy;
    }

    public void setOwnerShipIssuedBy(String ownerShipIssuedBy) {
        this.ownerShipIssuedBy = ownerShipIssuedBy;
    }

    public String getOwnerShipNumberState() {
        return ownerShipNumberState;
    }

    public void setOwnerShipNumberState(String ownerShipNumberState) {
        this.ownerShipNumberState = ownerShipNumberState;
    }

    public String getOwnerShipOccupancy() {
        return ownerShipOccupancy;
    }

    public void setOwnerShipOccupancy(String ownerShipOccupancy) {
        this.ownerShipOccupancy = ownerShipOccupancy;
    }

    public String getOwnerShipOccupying() {
        return ownerShipOccupying;
    }

    public void setOwnerShipOccupying(String ownerShipOccupying) {
        this.ownerShipOccupying = ownerShipOccupying;
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

    public String getPavement() {
        return pavement;
    }

    public void setPavement(String pavement) {
        this.pavement = pavement;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getPriceBasedChief() {
        return priceBasedChief;
    }

    public void setPriceBasedChief(BigDecimal priceBasedChief) {
        this.priceBasedChief = priceBasedChief;
    }

    public BigDecimal getPriceBasedExam() {
        return priceBasedExam;
    }

    public void setPriceBasedExam(BigDecimal priceBasedExam) {
        this.priceBasedExam = priceBasedExam;
    }

    public BigDecimal getPriceBasedNJOP() {
        return priceBasedNJOP;
    }

    public void setPriceBasedNJOP(BigDecimal priceBasedNJOP) {
        this.priceBasedNJOP = priceBasedNJOP;
    }

    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }

    public String getPublicTransportation() {
        return publicTransportation;
    }

    public void setPublicTransportation(String publicTransportation) {
        this.publicTransportation = publicTransportation;
    }

    public String getRoadWidth() {
        return roadWidth;
    }

    public void setRoadWidth(String roadWidth) {
        this.roadWidth = roadWidth;
    }

    public String getRoadAccess() {
        return roadAccess;
    }

    public void setRoadAccess(String roadAccess) {
        this.roadAccess = roadAccess;
    }

    public String getRoadSurface() {
        return roadSurface;
    }

    public void setRoadSurface(String roadSurface) {
        this.roadSurface = roadSurface;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getSecurityDisturbance() {
        return securityDisturbance;
    }

    public void setSecurityDisturbance(String securityDisturbance) {
        this.securityDisturbance = securityDisturbance;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getSocialLevels() {
        return socialLevels;
    }

    public void setSocialLevels(String socialLevels) {
        this.socialLevels = socialLevels;
    }

    public String getSoilConditionsPoint() {
        return soilConditionsPoint;
    }

    public void setSoilConditionsPoint(String soilConditionsPoint) {
        this.soilConditionsPoint = soilConditionsPoint;
    }

    public String getTopographCondition() {
        return topographCondition;
    }

    public void setTopographCondition(String topographCondition) {
        this.topographCondition = topographCondition;
    }

    public String getTopographLocation() {
        return topographLocation;
    }

    public void setTopographLocation(String topographLocation) {
        this.topographLocation = topographLocation;
    }

    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getUtilizationPoint() {
        return utilizationPoint;
    }

    public void setUtilizationPoint(String utilizationPoint) {
        this.utilizationPoint = utilizationPoint;
    }

    public BigDecimal getWide() {
        return wide;
    }

    public void setWide(BigDecimal wide) {
        this.wide = wide;
    }

    public String getOwnerShipStatus() {
        return ownerShipStatus;
    }

    public void setOwnerShipStatus(String ownerShipStatus) {
        this.ownerShipStatus = ownerShipStatus;
    }

    public String getCloserRoad() {
        return closerRoad;
    }

    public void setCloserRoad(String closerRoad) {
        this.closerRoad = closerRoad;
    }

    public String getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(String locationPoint) {
        this.locationPoint = locationPoint;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public String getAddressLocation() {
        return addressLocation;
    }

    public void setAddressLocation(String addressLocation) {
        this.addressLocation = addressLocation;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
