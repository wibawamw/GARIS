package org.motekar.project.civics.archieve.assets.procurement.objects;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemCardMap {

    private Date itemsDate = null;
    private String documentNumber = "";
    private Date documentDate = null;
    private String argument = "";
    private Integer amount = Integer.valueOf(0);
    private Integer residual = Integer.valueOf(0);
    private BigDecimal price = BigDecimal.ZERO;
    private String description = "";
    
    public ItemCardMap() {
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
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

    public Date getItemsDate() {
        return itemsDate;
    }

    public void setItemsDate(Date itemsDate) {
        this.itemsDate = itemsDate;
    }

    public Integer getResidual() {
        return residual;
    }

    public void setResidual(Integer residual) {
        this.residual = residual;
    }
    
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        if (itemsDate == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
                new Locale("in", "id", "id"));
        
        return sdf.format(itemsDate);
    }
}
