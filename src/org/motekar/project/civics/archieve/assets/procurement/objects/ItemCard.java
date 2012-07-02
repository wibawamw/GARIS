package org.motekar.project.civics.archieve.assets.procurement.objects;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemCard {
    
    private Long index = Long.valueOf(0);
    private String itemCode = "";
    private String itemName = "";
    //
    private String fullItemName = "";
    //
    private String specification = "";
    
    public ItemCard() {
    }

    public String getFullItemName() {
        return fullItemName;
    }

    public void setFullItemName(String fullItemName) {
        this.fullItemName = fullItemName;
    }
    
    public ItemCard(ApprovalBook book) {
        itemCode = book.getItemCode();
        itemName = book.getItemName();
    }
    
    public ItemCard(ReleaseBook book) {
        itemCode = book.getItemCode();
        itemName = book.getItemName();
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

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    @Override
    public String toString() {
        return itemName;
    }
}
