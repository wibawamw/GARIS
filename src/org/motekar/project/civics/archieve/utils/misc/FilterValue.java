package org.motekar.project.civics.archieve.utils.misc;

import java.util.ArrayList;
import org.motekar.project.civics.archieve.assets.master.objects.AssetObject;

/**
 *
 * @author Muhamad Wibawa
 */
public class FilterValue {
    
    private String filterName = "";
    private String columnName = "";
    private String columnTypes = "";
    
    private ArrayList<String> comboValues = new ArrayList<String>();
    private ArrayList comboObjectValues = new ArrayList();
    
    public FilterValue() {
    }
    
    public FilterValue(String filterName,String columnName,String columnTypes) {
        this.filterName = filterName;
        this.columnName = columnName;
        this.columnTypes = columnTypes;
    }
    
    public FilterValue(String filterName,String columnName,String columnTypes,ArrayList<String> comboValues,ArrayList comboObjectValues) {
        this.filterName = filterName;
        this.columnName = columnName;
        this.columnTypes = columnTypes;
        this.comboValues = comboValues;
        this.comboObjectValues = comboObjectValues;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnTypes() {
        return columnTypes;
    }

    public void setColumnTypes(String columnTypes) {
        this.columnTypes = columnTypes;
    }

    public ArrayList<String> getComboValues() {
        return comboValues;
    }

    public void setComboValues(ArrayList<String> comboValues) {
        this.comboValues = comboValues;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public ArrayList getComboObjectValues() {
        return comboObjectValues;
    }

    public void setComboObjectValues(ArrayList comboObjectValues) {
        this.comboObjectValues = comboObjectValues;
    }

    @Override
    public String toString() {
        return filterName;
    }
}
