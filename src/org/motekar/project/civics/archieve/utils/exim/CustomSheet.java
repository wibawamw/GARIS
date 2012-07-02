package org.motekar.project.civics.archieve.utils.exim;

/**
 *
 * @author Muhamad Wibawa
 */
public class CustomSheet {
    
    private int cellType = -1;
    private int column = -1;
    private String columnName = "";
    
    public CustomSheet() {
        
    }

    public int getCellType() {
        return cellType;
    }

    public void setCellType(int cellType) {
        this.cellType = cellType;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
}
