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
public class ItemsNetwork extends InventoryBook{
    
    private Long index = Long.valueOf(0);
    //
    private String fullItemName = "";
    //
    private Unit unit = null;
    private String documentNumber = "";
    private Date documentDate = null;
    private String constructionType = "";
    private Integer length = Integer.valueOf(0);
    private Integer width = Integer.valueOf(0);
    private BigDecimal wide = BigDecimal.ZERO;
    private BigDecimal price = BigDecimal.ZERO;
    private Condition condition = null;
    private String conditionText = "";
    private String addressLocation = "";
    private Region urban = null;
    private Region subUrban = null;
    private String landStatus = "";
    private String landCode = "";
    private String fundingSource = "";
    private String acquisitionWay = "";
    private String description = "";
    //
    private Integer streetYearBuild = Integer.valueOf(0);
    private String streetLocation = "";
    private String streetFlatness = "";
    private Integer streetStartKM = Integer.valueOf(0);
    private Integer streetEndKM = Integer.valueOf(0);
    private String streetWidth = "";
    private String streetSurface = "";
    private String streetSide = "";
    private String streetTrotoire = "";
    private String streetChannel = "";
    private boolean streetSafetyZone = false;
    //
    private String bridgeStandarType = "";
    private Integer bridgeYearBuild = Integer.valueOf(0);
    private Integer bridgeLength = Integer.valueOf(0);
    private Integer bridgeWidth = Integer.valueOf(0);
    private String bridgePurpose = "";
    private String bridgeMainMaterial = "";
    private String bridgeTopShape = "";
    private String bridgeOtherShape = "";
    private String bridgeHeadShape = "";
    private String bridgeHeadMaterial = "";
    private String bridgePillar = "";
    private String bridgePillarMaterial = "";
    //
    private Integer irrigationYearBuild = Integer.valueOf(0);
    private String irrigationBuildingType = "";
    private Integer irrigationLength = Integer.valueOf(0);
    private Integer irrigationHeight = Integer.valueOf(0);
    private Integer irrigationWidth = Integer.valueOf(0);
    private String irrigationBuildingMaterial = "";
    private Condition irrigationCondition = null;
    //
    private String irrigationCarrierType = "";
    private Integer irrigationCarrierLength = Integer.valueOf(0);
    private Integer irrigationCarrierHeight = Integer.valueOf(0);
    private Integer irrigationCarrierWidth = Integer.valueOf(0);
    private String irrigationCarrierMaterial = "";
    private Condition irrigationCarrierCondition = null;
    //
    private Integer irrigationDebitThresholdWidth = Integer.valueOf(0);
    private Integer irrigationDebitCDG = Integer.valueOf(0);
    private Integer irrigationDebitCipolleti = Integer.valueOf(0);
    private Integer irrigationDebitLength = Integer.valueOf(0);
    private Integer irrigationDebitHeight = Integer.valueOf(0);
    private Integer irrigationDebitWidth = Integer.valueOf(0);
    private String irrigationDebitMaterial = "";
    private Condition irrigationDebitCondition = null;
    //
    private Integer irrigationPoolUSBR3 = Integer.valueOf(0);
    private Integer irrigationPoolBlock = Integer.valueOf(0);
    private Integer irrigationPoolUSBR4 = Integer.valueOf(0);
    private Integer irrigationPoolVlogtor = Integer.valueOf(0);
    private Integer irrigationPoolLength = Integer.valueOf(0);
    private Integer irrigationPoolHeight = Integer.valueOf(0);
    private Integer irrigationPoolWidth = Integer.valueOf(0);
    private String irrigationPoolMaterial = "";
    private Condition irrigationPoolCondition = null;
    //
    private Integer irrigationHWFLotBlock = Integer.valueOf(0);
    private Integer irrigationHWFTrapesium = Integer.valueOf(0);
    private Integer irrigationHWFSlide = Integer.valueOf(0);
    private Integer irrigationHWFTreeTop = Integer.valueOf(0);
    private Integer irrigationHWFLength = Integer.valueOf(0);
    private Integer irrigationHWFHeight = Integer.valueOf(0);
    private Integer irrigationHWFWidth = Integer.valueOf(0);
    private String irrigationHWFMaterial = "";
    private Condition irrigationHWFCondition = null;
    //
    private Integer irrigationProtectTransfer = Integer.valueOf(0);
    private Integer irrigationProtectDisposal = Integer.valueOf(0);
    private Integer irrigationProtectDrain = Integer.valueOf(0);
    private Integer irrigationProtectLength = Integer.valueOf(0);
    private Integer irrigationProtectHeight = Integer.valueOf(0);
    private Integer irrigationProtectWidth = Integer.valueOf(0);
    private String irrigationProtectMaterial = "";
    private Condition irrigationProtectCondition = null;
    //
    private Integer irrigationTappedFor = Integer.valueOf(0);
    private Integer irrigationTappedSecond = Integer.valueOf(0);
    private Integer irrigationTappedRegulator = Integer.valueOf(0);
    private Integer irrigationTappedThird = Integer.valueOf(0);
    private Integer irrigationTappedLength = Integer.valueOf(0);
    private Integer irrigationTappedHeight = Integer.valueOf(0);
    private Integer irrigationTappedWidth = Integer.valueOf(0);
    private String irrigationTappedMaterial = "";
    private Condition irrigationTappedCondition = null;
    //
    private Integer irrigationSupportLevee = Integer.valueOf(0);
    private Integer irrigationSupportWash = Integer.valueOf(0);
    private Integer irrigationSupportBridge = Integer.valueOf(0);
    private Integer irrigationSupportKrib = Integer.valueOf(0);
    private Integer irrigationSupportLength = Integer.valueOf(0);
    private Integer irrigationSupportHeight = Integer.valueOf(0);
    private Integer irrigationSupportWidth = Integer.valueOf(0);
    private String irrigationSupportMaterial = "";
    private Condition irrigationSupportCondition = null;
    //
    private String userName = "";
    private String lastUserName = "";
    
    public ItemsNetwork() {
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
    }
    
    public String getBridgeHeadMaterial() {
        return bridgeHeadMaterial;
    }

    public void setBridgeHeadMaterial(String bridgeHeadMaterial) {
        this.bridgeHeadMaterial = bridgeHeadMaterial;
    }

    public String getBridgeHeadShape() {
        return bridgeHeadShape;
    }

    public void setBridgeHeadShape(String bridgeHeadShape) {
        this.bridgeHeadShape = bridgeHeadShape;
    }

    public Integer getBridgeLength() {
        return bridgeLength;
    }

    public void setBridgeLength(Integer bridgeLength) {
        this.bridgeLength = bridgeLength;
    }

    public String getBridgeMainMaterial() {
        return bridgeMainMaterial;
    }

    public void setBridgeMainMaterial(String bridgeMainMaterial) {
        this.bridgeMainMaterial = bridgeMainMaterial;
    }

    public String getBridgeOtherShape() {
        return bridgeOtherShape;
    }

    public void setBridgeOtherShape(String bridgeOtherShape) {
        this.bridgeOtherShape = bridgeOtherShape;
    }

    public String getBridgePillar() {
        return bridgePillar;
    }

    public void setBridgePillar(String bridgePillar) {
        this.bridgePillar = bridgePillar;
    }

    public String getBridgePillarMaterial() {
        return bridgePillarMaterial;
    }

    public void setBridgePillarMaterial(String bridgePillarMaterial) {
        this.bridgePillarMaterial = bridgePillarMaterial;
    }

    public String getBridgePurpose() {
        return bridgePurpose;
    }

    public void setBridgePurpose(String bridgePurpose) {
        this.bridgePurpose = bridgePurpose;
    }

    public String getBridgeStandarType() {
        return bridgeStandarType;
    }

    public void setBridgeStandarType(String bridgeStandarType) {
        this.bridgeStandarType = bridgeStandarType;
    }

    public String getBridgeTopShape() {
        return bridgeTopShape;
    }

    public void setBridgeTopShape(String bridgeTopShape) {
        this.bridgeTopShape = bridgeTopShape;
    }

    public Integer getBridgeWidth() {
        return bridgeWidth;
    }

    public void setBridgeWidth(Integer bridgeWidth) {
        this.bridgeWidth = bridgeWidth;
    }

    public Integer getBridgeYearBuild() {
        return bridgeYearBuild;
    }

    public void setBridgeYearBuild(Integer bridgeYearBuild) {
        this.bridgeYearBuild = bridgeYearBuild;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getConditionText() {
        return conditionText;
    }

    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
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

    public String getIrrigationBuildingMaterial() {
        return irrigationBuildingMaterial;
    }

    public void setIrrigationBuildingMaterial(String irrigationBuildingMaterial) {
        this.irrigationBuildingMaterial = irrigationBuildingMaterial;
    }

    public String getIrrigationBuildingType() {
        return irrigationBuildingType;
    }

    public void setIrrigationBuildingType(String irrigationBuildingType) {
        this.irrigationBuildingType = irrigationBuildingType;
    }

    public Condition getIrrigationCarrierCondition() {
        return irrigationCarrierCondition;
    }

    public void setIrrigationCarrierCondition(Condition irrigationCarrierCondition) {
        this.irrigationCarrierCondition = irrigationCarrierCondition;
    }

    public Integer getIrrigationCarrierHeight() {
        return irrigationCarrierHeight;
    }

    public void setIrrigationCarrierHeight(Integer irrigationCarrierHeight) {
        this.irrigationCarrierHeight = irrigationCarrierHeight;
    }

    public Integer getIrrigationCarrierLength() {
        return irrigationCarrierLength;
    }

    public void setIrrigationCarrierLength(Integer irrigationCarrierLength) {
        this.irrigationCarrierLength = irrigationCarrierLength;
    }

    public String getIrrigationCarrierMaterial() {
        return irrigationCarrierMaterial;
    }

    public void setIrrigationCarrierMaterial(String irrigationCarrierMaterial) {
        this.irrigationCarrierMaterial = irrigationCarrierMaterial;
    }

    public String getIrrigationCarrierType() {
        return irrigationCarrierType;
    }

    public void setIrrigationCarrierType(String irrigationCarrierType) {
        this.irrigationCarrierType = irrigationCarrierType;
    }

    public Integer getIrrigationCarrierWidth() {
        return irrigationCarrierWidth;
    }

    public void setIrrigationCarrierWidth(Integer irrigationCarrierWidth) {
        this.irrigationCarrierWidth = irrigationCarrierWidth;
    }

    public Condition getIrrigationCondition() {
        return irrigationCondition;
    }

    public void setIrrigationCondition(Condition irrigationCondition) {
        this.irrigationCondition = irrigationCondition;
    }

    public Integer getIrrigationDebitCDG() {
        return irrigationDebitCDG;
    }

    public void setIrrigationDebitCDG(Integer irrigationDebitCDG) {
        this.irrigationDebitCDG = irrigationDebitCDG;
    }

    public Integer getIrrigationDebitCipolleti() {
        return irrigationDebitCipolleti;
    }

    public void setIrrigationDebitCipolleti(Integer irrigationDebitCipolleti) {
        this.irrigationDebitCipolleti = irrigationDebitCipolleti;
    }

    public Condition getIrrigationDebitCondition() {
        return irrigationDebitCondition;
    }

    public void setIrrigationDebitCondition(Condition irrigationDebitCondition) {
        this.irrigationDebitCondition = irrigationDebitCondition;
    }

    public Integer getIrrigationDebitHeight() {
        return irrigationDebitHeight;
    }

    public void setIrrigationDebitHeight(Integer irrigationDebitHeight) {
        this.irrigationDebitHeight = irrigationDebitHeight;
    }

    public Integer getIrrigationDebitLength() {
        return irrigationDebitLength;
    }

    public void setIrrigationDebitLength(Integer irrigationDebitLength) {
        this.irrigationDebitLength = irrigationDebitLength;
    }

    public String getIrrigationDebitMaterial() {
        return irrigationDebitMaterial;
    }

    public void setIrrigationDebitMaterial(String irrigationDebitMaterial) {
        this.irrigationDebitMaterial = irrigationDebitMaterial;
    }

    public Integer getIrrigationDebitThresholdWidth() {
        return irrigationDebitThresholdWidth;
    }

    public void setIrrigationDebitThresholdWidth(Integer irrigationDebitThresholdWidth) {
        this.irrigationDebitThresholdWidth = irrigationDebitThresholdWidth;
    }

    public Integer getIrrigationDebitWidth() {
        return irrigationDebitWidth;
    }

    public void setIrrigationDebitWidth(Integer irrigationDebitWidth) {
        this.irrigationDebitWidth = irrigationDebitWidth;
    }

    public Condition getIrrigationHWFCondition() {
        return irrigationHWFCondition;
    }

    public void setIrrigationHWFCondition(Condition irrigationHWFCondition) {
        this.irrigationHWFCondition = irrigationHWFCondition;
    }

    public Integer getIrrigationHWFHeight() {
        return irrigationHWFHeight;
    }

    public void setIrrigationHWFHeight(Integer irrigationHWFHeight) {
        this.irrigationHWFHeight = irrigationHWFHeight;
    }

    public Integer getIrrigationHWFLength() {
        return irrigationHWFLength;
    }

    public void setIrrigationHWFLength(Integer irrigationHWFLength) {
        this.irrigationHWFLength = irrigationHWFLength;
    }

    public Integer getIrrigationHWFLotBlock() {
        return irrigationHWFLotBlock;
    }

    public void setIrrigationHWFLotBlock(Integer irrigationHWFLotBlock) {
        this.irrigationHWFLotBlock = irrigationHWFLotBlock;
    }

    public String getIrrigationHWFMaterial() {
        return irrigationHWFMaterial;
    }

    public void setIrrigationHWFMaterial(String irrigationHWFMaterial) {
        this.irrigationHWFMaterial = irrigationHWFMaterial;
    }

    public Integer getIrrigationHWFSlide() {
        return irrigationHWFSlide;
    }

    public void setIrrigationHWFSlide(Integer irrigationHWFSlide) {
        this.irrigationHWFSlide = irrigationHWFSlide;
    }

    public Integer getIrrigationHWFTrapesium() {
        return irrigationHWFTrapesium;
    }

    public void setIrrigationHWFTrapesium(Integer irrigationHWFTrapesium) {
        this.irrigationHWFTrapesium = irrigationHWFTrapesium;
    }

    public Integer getIrrigationHWFTreeTop() {
        return irrigationHWFTreeTop;
    }

    public void setIrrigationHWFTreeTop(Integer irrigationHWFTreeTop) {
        this.irrigationHWFTreeTop = irrigationHWFTreeTop;
    }

    public Integer getIrrigationHWFWidth() {
        return irrigationHWFWidth;
    }

    public void setIrrigationHWFWidth(Integer irrigationHWFWidth) {
        this.irrigationHWFWidth = irrigationHWFWidth;
    }

    public Integer getIrrigationHeight() {
        return irrigationHeight;
    }

    public void setIrrigationHeight(Integer irrigationHeight) {
        this.irrigationHeight = irrigationHeight;
    }

    public Integer getIrrigationLength() {
        return irrigationLength;
    }

    public void setIrrigationLength(Integer irrigationLength) {
        this.irrigationLength = irrigationLength;
    }

    public Integer getIrrigationPoolBlock() {
        return irrigationPoolBlock;
    }

    public void setIrrigationPoolBlock(Integer irrigationPoolBlock) {
        this.irrigationPoolBlock = irrigationPoolBlock;
    }

    public Condition getIrrigationPoolCondition() {
        return irrigationPoolCondition;
    }

    public void setIrrigationPoolCondition(Condition irrigationPoolCondition) {
        this.irrigationPoolCondition = irrigationPoolCondition;
    }

    public Integer getIrrigationPoolHeight() {
        return irrigationPoolHeight;
    }

    public void setIrrigationPoolHeight(Integer irrigationPoolHeight) {
        this.irrigationPoolHeight = irrigationPoolHeight;
    }

    public Integer getIrrigationPoolLength() {
        return irrigationPoolLength;
    }

    public void setIrrigationPoolLength(Integer irrigationPoolLength) {
        this.irrigationPoolLength = irrigationPoolLength;
    }

    public String getIrrigationPoolMaterial() {
        return irrigationPoolMaterial;
    }

    public void setIrrigationPoolMaterial(String irrigationPoolMaterial) {
        this.irrigationPoolMaterial = irrigationPoolMaterial;
    }

    public Integer getIrrigationPoolUSBR3() {
        return irrigationPoolUSBR3;
    }

    public void setIrrigationPoolUSBR3(Integer irrigationPoolUSBR3) {
        this.irrigationPoolUSBR3 = irrigationPoolUSBR3;
    }

    public Integer getIrrigationPoolUSBR4() {
        return irrigationPoolUSBR4;
    }

    public void setIrrigationPoolUSBR4(Integer irrigationPoolUSBR4) {
        this.irrigationPoolUSBR4 = irrigationPoolUSBR4;
    }

    public Integer getIrrigationPoolVlogtor() {
        return irrigationPoolVlogtor;
    }

    public void setIrrigationPoolVlogtor(Integer irrigationPoolVlogtor) {
        this.irrigationPoolVlogtor = irrigationPoolVlogtor;
    }

    public Integer getIrrigationPoolWidth() {
        return irrigationPoolWidth;
    }

    public void setIrrigationPoolWidth(Integer irrigationPoolWidth) {
        this.irrigationPoolWidth = irrigationPoolWidth;
    }

    public Condition getIrrigationProtectCondition() {
        return irrigationProtectCondition;
    }

    public void setIrrigationProtectCondition(Condition irrigationProtectCondition) {
        this.irrigationProtectCondition = irrigationProtectCondition;
    }

    public Integer getIrrigationProtectDisposal() {
        return irrigationProtectDisposal;
    }

    public void setIrrigationProtectDisposal(Integer irrigationProtectDisposal) {
        this.irrigationProtectDisposal = irrigationProtectDisposal;
    }

    public Integer getIrrigationProtectDrain() {
        return irrigationProtectDrain;
    }

    public void setIrrigationProtectDrain(Integer irrigationProtectDrain) {
        this.irrigationProtectDrain = irrigationProtectDrain;
    }

    public Integer getIrrigationProtectHeight() {
        return irrigationProtectHeight;
    }

    public void setIrrigationProtectHeight(Integer irrigationProtectHeight) {
        this.irrigationProtectHeight = irrigationProtectHeight;
    }

    public Integer getIrrigationProtectLength() {
        return irrigationProtectLength;
    }

    public void setIrrigationProtectLength(Integer irrigationProtectLength) {
        this.irrigationProtectLength = irrigationProtectLength;
    }

    public String getIrrigationProtectMaterial() {
        return irrigationProtectMaterial;
    }

    public void setIrrigationProtectMaterial(String irrigationProtectMaterial) {
        this.irrigationProtectMaterial = irrigationProtectMaterial;
    }

    public Integer getIrrigationProtectTransfer() {
        return irrigationProtectTransfer;
    }

    public void setIrrigationProtectTransfer(Integer irrigationProtectTransfer) {
        this.irrigationProtectTransfer = irrigationProtectTransfer;
    }

    public Integer getIrrigationProtectWidth() {
        return irrigationProtectWidth;
    }

    public void setIrrigationProtectWidth(Integer irrigationProtectWidth) {
        this.irrigationProtectWidth = irrigationProtectWidth;
    }

    public Integer getIrrigationSupportBridge() {
        return irrigationSupportBridge;
    }

    public void setIrrigationSupportBridge(Integer irrigationSupportBridge) {
        this.irrigationSupportBridge = irrigationSupportBridge;
    }

    public Condition getIrrigationSupportCondition() {
        return irrigationSupportCondition;
    }

    public void setIrrigationSupportCondition(Condition irrigationSupportCondition) {
        this.irrigationSupportCondition = irrigationSupportCondition;
    }

    public Integer getIrrigationSupportHeight() {
        return irrigationSupportHeight;
    }

    public void setIrrigationSupportHeight(Integer irrigationSupportHeight) {
        this.irrigationSupportHeight = irrigationSupportHeight;
    }

    public Integer getIrrigationSupportKrib() {
        return irrigationSupportKrib;
    }

    public void setIrrigationSupportKrib(Integer irrigationSupportKrib) {
        this.irrigationSupportKrib = irrigationSupportKrib;
    }

    public Integer getIrrigationSupportLength() {
        return irrigationSupportLength;
    }

    public void setIrrigationSupportLength(Integer irrigationSupportLength) {
        this.irrigationSupportLength = irrigationSupportLength;
    }

    public Integer getIrrigationSupportLevee() {
        return irrigationSupportLevee;
    }

    public void setIrrigationSupportLevee(Integer irrigationSupportLevee) {
        this.irrigationSupportLevee = irrigationSupportLevee;
    }

    public String getIrrigationSupportMaterial() {
        return irrigationSupportMaterial;
    }

    public void setIrrigationSupportMaterial(String irrigationSupportMaterial) {
        this.irrigationSupportMaterial = irrigationSupportMaterial;
    }

    public Integer getIrrigationSupportWash() {
        return irrigationSupportWash;
    }

    public void setIrrigationSupportWash(Integer irrigationSupportWash) {
        this.irrigationSupportWash = irrigationSupportWash;
    }

    public Integer getIrrigationSupportWidth() {
        return irrigationSupportWidth;
    }

    public void setIrrigationSupportWidth(Integer irrigationSupportWidth) {
        this.irrigationSupportWidth = irrigationSupportWidth;
    }

    public Condition getIrrigationTappedCondition() {
        return irrigationTappedCondition;
    }

    public void setIrrigationTappedCondition(Condition irrigationTappedCondition) {
        this.irrigationTappedCondition = irrigationTappedCondition;
    }

    public Integer getIrrigationTappedFor() {
        return irrigationTappedFor;
    }

    public void setIrrigationTappedFor(Integer irrigationTappedFor) {
        this.irrigationTappedFor = irrigationTappedFor;
    }

    public Integer getIrrigationTappedHeight() {
        return irrigationTappedHeight;
    }

    public void setIrrigationTappedHeight(Integer irrigationTappedHeight) {
        this.irrigationTappedHeight = irrigationTappedHeight;
    }

    public Integer getIrrigationTappedLength() {
        return irrigationTappedLength;
    }

    public void setIrrigationTappedLength(Integer irrigationTappedLength) {
        this.irrigationTappedLength = irrigationTappedLength;
    }

    public String getIrrigationTappedMaterial() {
        return irrigationTappedMaterial;
    }

    public void setIrrigationTappedMaterial(String irrigationTappedMaterial) {
        this.irrigationTappedMaterial = irrigationTappedMaterial;
    }

    public Integer getIrrigationTappedRegulator() {
        return irrigationTappedRegulator;
    }

    public void setIrrigationTappedRegulator(Integer irrigationTappedRegulator) {
        this.irrigationTappedRegulator = irrigationTappedRegulator;
    }

    public Integer getIrrigationTappedSecond() {
        return irrigationTappedSecond;
    }

    public void setIrrigationTappedSecond(Integer irrigationTappedSecond) {
        this.irrigationTappedSecond = irrigationTappedSecond;
    }

    public Integer getIrrigationTappedThird() {
        return irrigationTappedThird;
    }

    public void setIrrigationTappedThird(Integer irrigationTappedThird) {
        this.irrigationTappedThird = irrigationTappedThird;
    }

    public Integer getIrrigationTappedWidth() {
        return irrigationTappedWidth;
    }

    public void setIrrigationTappedWidth(Integer irrigationTappedWidth) {
        this.irrigationTappedWidth = irrigationTappedWidth;
    }

    public Integer getIrrigationWidth() {
        return irrigationWidth;
    }

    public void setIrrigationWidth(Integer irrigationWidth) {
        this.irrigationWidth = irrigationWidth;
    }

    public Integer getIrrigationYearBuild() {
        return irrigationYearBuild;
    }

    public void setIrrigationYearBuild(Integer irrigationYearBuild) {
        this.irrigationYearBuild = irrigationYearBuild;
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

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStreetChannel() {
        return streetChannel;
    }

    public void setStreetChannel(String streetChannel) {
        this.streetChannel = streetChannel;
    }

    public Integer getStreetEndKM() {
        return streetEndKM;
    }

    public void setStreetEndKM(Integer streetEndKM) {
        this.streetEndKM = streetEndKM;
    }

    public String getStreetFlatness() {
        return streetFlatness;
    }

    public void setStreetFlatness(String streetFlatness) {
        this.streetFlatness = streetFlatness;
    }

    public String getStreetLocation() {
        return streetLocation;
    }

    public void setStreetLocation(String streetLocation) {
        this.streetLocation = streetLocation;
    }

    public boolean isStreetSafetyZone() {
        return streetSafetyZone;
    }

    public void setStreetSafetyZone(boolean streetSafetyZone) {
        this.streetSafetyZone = streetSafetyZone;
    }

    public Integer getStreetStartKM() {
        return streetStartKM;
    }

    public void setStreetStartKM(Integer streetStartKM) {
        this.streetStartKM = streetStartKM;
    }

    public String getStreetWidth() {
        return streetWidth;
    }

    public void setStreetWidth(String streetWidth) {
        this.streetWidth = streetWidth;
    }

    public Integer getStreetYearBuild() {
        return streetYearBuild;
    }

    public void setStreetYearBuild(Integer streetYearBuild) {
        this.streetYearBuild = streetYearBuild;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getConstructionType() {
        return constructionType;
    }

    public void setConstructionType(String constructionType) {
        this.constructionType = constructionType;
    }

    public String getAcquisitionWay() {
        return acquisitionWay;
    }

    public void setAcquisitionWay(String acquisitionWay) {
        this.acquisitionWay = acquisitionWay;
    }

    public String getStreetSide() {
        return streetSide;
    }

    public void setStreetSide(String streetSide) {
        this.streetSide = streetSide;
    }

    public String getStreetSurface() {
        return streetSurface;
    }

    public void setStreetSurface(String streetSurface) {
        this.streetSurface = streetSurface;
    }

    public String getStreetTrotoire() {
        return streetTrotoire;
    }

    public void setStreetTrotoire(String streetTrotoire) {
        this.streetTrotoire = streetTrotoire;
    }

    public BigDecimal getWide() {
        return wide;
    }

    public void setWide(BigDecimal wide) {
        this.wide = wide;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
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
