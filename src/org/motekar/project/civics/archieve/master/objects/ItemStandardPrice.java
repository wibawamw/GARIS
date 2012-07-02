package org.motekar.project.civics.archieve.master.objects;

import java.math.BigDecimal;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemStandardPrice {

    private Long index = Long.valueOf(0);
    private String itemName = "";
    private String specification = "";
    private String itemsUnit = "";
    private BigDecimal price = BigDecimal.ZERO;
    private String description = "";

    private boolean styled = false;
    
    public ItemStandardPrice() {
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemsUnit() {
        return itemsUnit;
    }

    public void setItemsUnit(String itemsUnit) {
        this.itemsUnit = itemsUnit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" + itemName+ "</b>";
        }
        
        return itemName;
    }
}
