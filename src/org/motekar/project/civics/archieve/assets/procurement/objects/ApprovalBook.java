package org.motekar.project.civics.archieve.assets.procurement.objects;

import java.math.BigDecimal;
import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class ApprovalBook implements Comparable<ApprovalBook> {
    
    private Long index = Long.valueOf(0);
    //
    private String fullItemName = "";
    //
    private Date receiveDate = null;
    private String receiveFrom = "";
    private String invoiceNumber = "";
    private Date invoiceDate = null;
    private String itemCode = "";
    private String itemName = "";
    private Integer amount = Integer.valueOf(0);
    private BigDecimal price = BigDecimal.ZERO;
    private Date documentDate = null;
    private String documentNumber = "";
    private String description = "";
    private Unit warehouse = null;
    
    private Date contractDate = null;
    private String contractNumber = "";
    
    private boolean selected = false;
    
    public ApprovalBook() {
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
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

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getReceiveFrom() {
        return receiveFrom;
    }

    public void setReceiveFrom(String receiveFrom) {
        this.receiveFrom = receiveFrom;
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

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
    
    @Override
    public String toString() {
        return itemName;
    }

    public int compareTo(ApprovalBook o) {
        return this.getReceiveDate().compareTo(o.getReceiveDate());
    }
}
