package org.motekar.project.civics.archieve.assets.inventory.objects;

import java.math.BigDecimal;
import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class InventoryRekap {
    
    private String itemCode = "";
    private String itemName = "";
    private Integer total = Integer.valueOf(0);
    private BigDecimal price = BigDecimal.ZERO;
    
    public InventoryRekap() {
    }
    
    public InventoryRekap(String itemCode,Integer total,BigDecimal price) {
        this.itemCode = itemCode;
        this.total = total;
        this.price = price;
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
    
    public String getChildCode() {
        String childCode = "";
        
        if (!itemCode.equals("")) {
            StringTokenizer token = new StringTokenizer(itemCode,".");
            while (token.hasMoreTokens()) {
                childCode = token.nextToken();
            }
        } 
        
        return childCode;
    }
    
    public String getParentCode() {
        String childCode = "";
        
        if (!itemCode.equals("") && itemCode.length() > 2) {
            StringTokenizer token = new StringTokenizer(itemCode,".");
            while (token.hasMoreTokens()) {
                childCode = token.nextToken();
                break;
            }
        } 
        
        return childCode;
    }

    @Override
    public String toString() {
        return "InventoryRekap{" + "itemCode=" + itemCode + '}';
    }
}
