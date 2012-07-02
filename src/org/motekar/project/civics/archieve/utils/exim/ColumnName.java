package org.motekar.project.civics.archieve.utils.exim;

/**
 *
 * @author Muhamad Wibawa
 */
public class ColumnName {
    
    private Integer index = Integer.valueOf(0);
    private String name = "";
    private String columnType = "";
    
    public ColumnName(Integer index,String name,String columnType) {
        this.index = index;
        this.name = name;
        this.columnType = columnType;
    }
    
    public ColumnName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColumnName other = (ColumnName) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
    
}
