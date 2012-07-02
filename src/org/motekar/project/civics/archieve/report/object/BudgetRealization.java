package org.motekar.project.civics.archieve.report.object;

import java.math.BigDecimal;

/**
 *
 * @author Muhamad Wibawa
 */
public class BudgetRealization {

    private String eselon = "";
    private String activity = "";
    private BigDecimal budget = BigDecimal.ZERO;
    private BigDecimal credit = BigDecimal.ZERO;
    private BigDecimal closing = BigDecimal.ZERO;
    private String description = "";
    
    private Integer counted = Integer.valueOf(0);
    private Integer diff = Integer.valueOf(0);

    public BudgetRealization() {
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getClosing() {
        return closing;
    }

    public void setClosing(BigDecimal closing) {
        this.closing = closing;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEselon() {
        return eselon;
    }

    public void setEselon(String eselon) {
        this.eselon = eselon;
    }

    public Integer getCounted() {
        return counted;
    }

    public void setCounted(Integer counted) {
        this.counted = counted;
    }

    public Integer getDiff() {
        return diff;
    }

    public void setDiff(Integer diff) {
        this.diff = diff;
    }

    public Integer getCountedDiff() {
        return counted - diff;
    }
}
