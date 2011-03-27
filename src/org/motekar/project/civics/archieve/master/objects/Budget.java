package org.motekar.project.civics.archieve.master.objects;

import java.util.ArrayList;

/**
 *
 * @author Muhamad Wibawa
 */
public class Budget {

    public static final Integer MURNI = Integer.valueOf(1);
    public static final Integer PERUBAHAN = Integer.valueOf(2);

    private Long index = Long.valueOf(0);
    private Integer years = Integer.valueOf(0);
    private Integer budgetType = Integer.valueOf(0);

    private ArrayList<BudgetDetail> details = new ArrayList<BudgetDetail>();

    public Budget() {
    }

    public Integer getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(Integer budgetType) {
        this.budgetType = budgetType;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }

    public ArrayList<BudgetDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<BudgetDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        if (budgetType.equals(MURNI)) {
            return "Anggaran Tahun "+years;
        } else {
            return "Anggaran Perubahan Tahun "+years;
        }
        
    }
}
