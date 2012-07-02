package org.motekar.project.civics.archieve.assets.procurement.objects;

import java.math.BigDecimal;
import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class ReleaseBook implements Comparable<ReleaseBook> {
    
    private Long index = Long.valueOf(0);
    //
    private String fullItemName = "";
    //
    private Date releaseDate = null;
    private String sortingNumber = "";
    private String itemCode = "";
    private String itemName = "";
    private Integer amount = Integer.valueOf(0);
    private BigDecimal price = BigDecimal.ZERO;
    private String allotment = "";
    private Date delegateDate = null;
    private String description = "";
    
    private String billNumber = "";
    private Date billDate = null;
    
    private Unit warehouse = null;
    
    private boolean selected = false;
    
    public ReleaseBook() {
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
    }
    
    public String getAllotment() {
        return allotment;
    }

    public void setAllotment(String allotment) {
        this.allotment = allotment;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getDelegateDate() {
        return delegateDate;
    }

    public void setDelegateDate(Date delegateDate) {
        this.delegateDate = delegateDate;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSortingNumber() {
        return sortingNumber;
    }

    public void setSortingNumber(String sortingNumber) {
        this.sortingNumber = sortingNumber;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Unit getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Unit warehouse) {
        this.warehouse = warehouse;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }
    
    @Override
    public String toString() {
        return itemName;
    }

    public int compareTo(ReleaseBook o) {
        return this.getReleaseDate().compareTo(o.getReleaseDate());
    }
}
