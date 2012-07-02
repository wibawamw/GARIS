package org.motekar.project.civics.archieve.assets.inventory.objects;

import java.math.BigDecimal;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsFixedAsset extends InventoryBook {
    
    private Long index = Long.valueOf(0);
    //
    private String fullItemName = "";
    //
    private Unit unit  = null;
    //
    private String bookAuthor = "";
    private String bookSpec = "";
    //
    private String artRegion = "";
    private String artAuthor = "";
    private String artMaterial = "";
    //
    private String cattleType = "";
    private String cattleSize = "";
    //
    private BigDecimal price = BigDecimal.ZERO;
    private Integer acquisitionYear = Integer.valueOf(0);
    private Condition condition = null;
    private String fundingSource = "";
    private String acquisitionWay = "";
    private String description = "";
    //
    private String userName = "";
    private String lastUserName = "";
    
    public ItemsFixedAsset() {
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

    public String getArtAuthor() {
        return artAuthor;
    }

    public void setArtAuthor(String artAuthor) {
        this.artAuthor = artAuthor;
    }

    public String getArtMaterial() {
        return artMaterial;
    }

    public void setArtMaterial(String artMaterial) {
        this.artMaterial = artMaterial;
    }

    public String getArtRegion() {
        return artRegion;
    }

    public void setArtRegion(String artRegion) {
        this.artRegion = artRegion;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookSpec() {
        return bookSpec;
    }

    public void setBookSpec(String bookSpec) {
        this.bookSpec = bookSpec;
    }

    public String getCattleSize() {
        return cattleSize;
    }

    public void setCattleSize(String cattleSize) {
        this.cattleSize = cattleSize;
    }

    public String getCattleType() {
        return cattleType;
    }

    public void setCattleType(String cattleType) {
        this.cattleType = cattleType;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Integer getAcquisitionYear() {
        return acquisitionYear;
    }

    public void setAcquisitionYear(Integer acquisitionYear) {
        this.acquisitionYear = acquisitionYear;
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
