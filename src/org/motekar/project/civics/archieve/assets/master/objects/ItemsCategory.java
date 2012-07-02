package org.motekar.project.civics.archieve.assets.master.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class ItemsCategory {

    private static final String[] TYPES = {"", "Bidang Barang Tanah",
        "Bidang Barang Peralatan dan Mesin",
        "Bidang Barang Gedung dan Bangunan",
        "Bidang Barang Jalan, Irigasi dan Jaringan",
        "Bidang Barang Aset Tetap Lainnya",
        "Bidang Barang Konstruksi dalam Pengerjaan"};
    //
    public static final Long ROOT_PARENT_INDEX = Long.valueOf(0);
    //
    public static final Integer ITEMS_LAND = Integer.valueOf(1);
    public static final Integer ITEMS_MACHINE = Integer.valueOf(2);
    public static final Integer ITEMS_BUILDING = Integer.valueOf(3);
    public static final Integer ITEMS_NETWORK = Integer.valueOf(4);
    public static final Integer ITEMS_FIXED_ASSET = Integer.valueOf(5);
    public static final Integer ITEMS_CONSTRUCTION = Integer.valueOf(6);
    
    public static final String TYPE_ITEMS_LAND = "Bidang Barang Tanah";
    public static final String TYPE_ITEMS_MACHINE = "Bidang Barang Peralatan dan Mesin";
    public static final String TYPE_ITEMS_BUILDING = "Bidang Barang Gedung dan Bangunan";
    public static final String TYPE_ITEMS_NETWORK = "Bidang Barang Jalan, Irigasi dan Jaringan";
    public static final String TYPE_ITEMS_FIXED_ASSET = "Bidang Barang Aset Tetap Lainnya";
    
    private Long index = Long.valueOf(0);
    private String categoryCode = "";
    private String categoryName = "";
    private boolean group = false;
    private Integer types = Integer.valueOf(0);
    private boolean styled = false;
    private Long parentIndex = Long.valueOf(0);

    public ItemsCategory() {
    }
    
    public ItemsCategory(String categoryCode) {
        this.categoryCode = categoryCode;
    }
    
    public ItemsCategory(ItemsCategory category) {
        this.index = category.getIndex();
        this.categoryCode = category.getCategoryCode();
        this.categoryName = category.getCategoryName();
        this.group = category.isGroup();
        this.types = category.getTypes();
        this.styled = category.isStyled();
        this.parentIndex = category.getParentIndex();
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    public Long getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Long parentIndex) {
        this.parentIndex = parentIndex;
    }

    public String getChildCode() {
        StringTokenizer token = new StringTokenizer(categoryCode, ".");
        String childCode = "";
        while (token.hasMoreElements()) {
            childCode = token.nextToken();
        }

        return childCode;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }
    
   public String getTypesAsString() {
       return TYPES[types];
   }
   
   public static ArrayList<String> typeAsList() {
        ArrayList<String> types = new ArrayList<String>();
        types.addAll(Arrays.asList(TYPES));
        return types;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ItemsCategory other = (ItemsCategory) obj;
        if ((this.categoryCode == null) ? (other.categoryCode != null) : !this.categoryCode.equals(other.categoryCode)) {
            return false;
        }
        if ((this.categoryName == null) ? (other.categoryName != null) : !this.categoryName.equals(other.categoryName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.categoryCode != null ? this.categoryCode.hashCode() : 0);
        hash = 11 * hash + (this.categoryName != null ? this.categoryName.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        if (isStyled()) {
            return categoryCode + " " + categoryName;
        }
        return categoryName;
    }
}
