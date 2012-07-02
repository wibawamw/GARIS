package org.motekar.project.civics.archieve.assets.procurement.objects;

import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class UnrecycledItems {
    
    private Long index = Long.valueOf(0);
    private Date receiveDate = null;
    private String itemCode = "";
    private String itemName = "";
    //
    private String fullItemName = "";
    //
    private String specification = "";
    private Integer productionYear = Integer.valueOf(0);
    private Integer receiveAmount = Integer.valueOf(0);
    private String contractNumber = "";
    private Date contractDate = null;
    private String documentNumber = "";
    private Date documentDate = null;
    private Date releaseDate = null;
    private String submitted = "";
    private Integer releaseAmount = Integer.valueOf(0);
    private String releaseDocumentNumber = "";
    private Date releaseDocumentDate = null;
    private String description = "";
    
    private Unit unit = null;
    
    private boolean selected = false;
    
    public UnrecycledItems() {
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
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

    public Integer getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public Integer getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(Integer receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Integer getReleaseAmount() {
        return releaseAmount;
    }

    public void setReleaseAmount(Integer releaseAmount) {
        this.releaseAmount = releaseAmount;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getReleaseDocumentDate() {
        return releaseDocumentDate;
    }

    public void setReleaseDocumentDate(Date releaseDocumentDate) {
        this.releaseDocumentDate = releaseDocumentDate;
    }

    public String getReleaseDocumentNumber() {
        return releaseDocumentNumber;
    }

    public void setReleaseDocumentNumber(String releaseDocumentNumber) {
        this.releaseDocumentNumber = releaseDocumentNumber;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public String toString() {
        return itemName;
    }
}
