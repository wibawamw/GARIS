package org.motekar.project.civics.archieve.master.objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Muhamad Wibawa
 */
public class BudgetSubDetailChild {

    public static final String[] ESELON = new String[]{"", "I", "II", "III", "IV"};
    private Long parentIndex = Long.valueOf(0);
    private String description = "";
    private Integer eselon = Integer.valueOf(0);
    private Integer counted = Integer.valueOf(0);
    private String units = "";
    private BigDecimal amount = BigDecimal.ZERO;
    private boolean selected = false;

    public BudgetSubDetailChild() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCounted() {
        return counted;
    }

    public void setCounted(Integer counted) {
        this.counted = counted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Long parentIndex) {
        this.parentIndex = parentIndex;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getEselon() {
        return eselon;
    }

    public void setEselon(Integer eselon) {
        this.eselon = eselon;
    }

    public String getEselonAsString() {
        if (this.eselon.compareTo(Integer.valueOf(0)) > 0) {
            return ESELON[eselon];
        }
        return "";
    }

    public static ArrayList<String> eselonAsList() {
        ArrayList<String> str = new ArrayList<String>();
        str.addAll(Arrays.asList(ESELON));

        return str;
    }

    @Override
    public String toString() {
        return description;
    }
}
