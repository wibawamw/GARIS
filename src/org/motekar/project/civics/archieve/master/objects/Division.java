package org.motekar.project.civics.archieve.master.objects;

import java.util.StringTokenizer;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class Division {

    
    private String code = "";
    private String name = "";
    
    private String parentCode = "";
    private Division parent = null;
    
    private boolean styled = false;
    private boolean selected = false;
    
    private Unit unit = null;

    public Division() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Division getParent() {
        return parent;
    }

    public void setParent(Division parent) {
        this.parent = parent;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getChildCode() {
        StringTokenizer token = new StringTokenizer(code, ".");
        String childCode = "";
        while (token.hasMoreElements()) {
            childCode = token.nextToken();
        }

        return childCode;
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
    public boolean equals(Object obj) {

        if (obj == null) return false;

        if (obj == this) return true;

        if (obj instanceof Division) {
            Division division = (Division) obj;
            if (this.getCode().equals(division.getCode()) &&
                    this.getName().equals(division.getName())) {
               return true;
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.code != null ? this.code.hashCode() : 0);
        hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
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
            if (parentCode.equals("")) {
                return "<html><b>" + code + " " + name + "</b>";
            } else {
                return code + " " + name;
            }
            
        }
        if (code.equals("") || (name.equals(""))) {
            return code + " " + name;
        }
        return name;
    }
}
