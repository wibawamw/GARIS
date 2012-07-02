package org.motekar.project.civics.archieve.assets.master.objects;

import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class Unit {

    public static final Integer TYPE_UPB = Integer.valueOf(1);
    public static final Integer TYPE_SUB_UPB = Integer.valueOf(2);
    private Long index = Long.valueOf(0);
    private String unitCode = "";
    private String unitName = "";
    private Integer unitType = Integer.valueOf(0);
    private String streetName = "";
    private String rt = "";
    private String rw = "";
    private Region urban = null;
    private Region subUrban = null;
    private Long parentIndex = Long.valueOf(0);
    private Unit parent = null;
    private boolean group = false;
    private boolean styled = false;
    private boolean hirarchiecal = false;

    public Unit() {
    }

    public Unit(Long index) {
        this.index = index;
    }

    public Unit(String unitCode) {
        this.unitCode = unitCode;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRw() {
        return rw;
    }

    public void setRw(String rw) {
        this.rw = rw;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Region getSubUrban() {
        return subUrban;
    }

    public void setSubUrban(Region subUrban) {
        this.subUrban = subUrban;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Region getUrban() {
        return urban;
    }

    public void setUrban(Region urban) {
        this.urban = urban;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
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

    public Integer getUnitType() {
        return unitType;
    }

    public void setUnitType(Integer unitType) {
        this.unitType = unitType;
    }

    public String getChildCode() {
        StringTokenizer token = new StringTokenizer(unitCode, ".");
        String childCode = "";
        while (token.hasMoreElements()) {
            childCode = token.nextToken();
        }

        return childCode;
    }

    public Unit getParent() {
        return parent;
    }

    public void setParent(Unit parent) {
        this.parent = parent;
    }

    public boolean isHirarchiecal() {
        return hirarchiecal;
    }

    public void setHirarchiecal(boolean hirarchiecal) {
        this.hirarchiecal = hirarchiecal;
    }

    private String getSeparator() {
        StringBuilder builder = new StringBuilder();

        int codeLength = 0;
        
        StringTokenizer token = new StringTokenizer(unitCode,".");
        while (token.hasMoreElements()) {
            token.nextToken();
            codeLength++;
        }
        if (parentIndex.compareTo(Long.valueOf(0)) > 0) {
            for (int i = 0; i < codeLength - 2; i++) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Unit other = (Unit) obj;
        if ((this.unitCode == null) ? (other.unitCode != null) : !this.unitCode.equals(other.unitCode)) {
            return false;
        }
        if ((this.unitName == null) ? (other.unitName != null) : !this.unitName.equals(other.unitName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.unitCode != null ? this.unitCode.hashCode() : 0);
        hash = 19 * hash + (this.unitName != null ? this.unitName.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        if (isStyled()) {
            return unitCode + " " + unitName;
        } else if (isHirarchiecal()) {
            return getSeparator()+unitCode + " " + unitName;
        }
        return unitName;
    }
}
