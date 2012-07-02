package org.motekar.project.civics.archieve.assets.master.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class Region {

    public static final String[] REGION_TYPE = new String[]{"","Kecamatan","Kelurahan","Desa"};
    
    public static final Integer TYPE_URBAN = Integer.valueOf(1);
    public static final Integer TYPE_SUB_URBAN = Integer.valueOf(2);
    public static final Integer TYPE_SUB_URBAN_2 = Integer.valueOf(3);
    
    private Long index = Long.valueOf(0);
    private String regionCode = "";
    private String regionName = "";
    private Integer regionType = Integer.valueOf(0);
    private boolean group = false;
    
    private Long parentIndex = Long.valueOf(0);
    
    private boolean styled = false;
    
    public Region() {
    }
    
    public Region(Long index) {
        this.index = index;
    }
    
    public Region(String regionName) {
        this.regionName = regionName;
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

    public Long getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Long parentIndex) {
        this.parentIndex = parentIndex;
    }
    
    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Integer getRegionType() {
        return regionType;
    }

    public void setRegionType(Integer regionType) {
        this.regionType = regionType;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }
    
    public String getRegionTypeAsString() {
        return REGION_TYPE[regionType];
    }
    
    public static ArrayList<String> regionTypeAsList() {
        ArrayList<String> regionTypes = new ArrayList<String>();
        regionTypes.addAll(Arrays.asList(REGION_TYPE));

        return regionTypes;
    }

    public String getChildCode() {
        StringTokenizer token = new StringTokenizer(regionCode,".");
        String childCode = "";
        while (token.hasMoreElements()) {
            childCode = token.nextToken();
        }

        return childCode;
    }

    @Override
    public String toString() {
        if (isStyled()) {
            return regionCode.toUpperCase()+" "+
                    getRegionTypeAsString().toUpperCase()+" "+
                    regionName.toUpperCase();
        }
        return regionName.toUpperCase();
    }
}
