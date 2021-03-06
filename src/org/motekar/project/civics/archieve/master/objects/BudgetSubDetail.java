package org.motekar.project.civics.archieve.master.objects;

import java.math.BigDecimal;

/**
 *
 * @author Muhamad Wibawa
 */
public class BudgetSubDetail {

    private Long index = Long.valueOf(0);
    private Long parentIndex = Long.valueOf(0);

    private Activity activity = null;
    private BigDecimal amount = BigDecimal.ZERO;

    public BudgetSubDetail() {
    }

    public BudgetSubDetail(BudgetSubDetail subDetail) {
        this.index = subDetail.getIndex();
        this.parentIndex = subDetail.getParentIndex();
        this.activity = subDetail.getActivity();
        this.amount = subDetail.getAmount();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Long parentIndex) {
        this.parentIndex = parentIndex;
    }

    @Override
    public String toString() {
        return activity.toString();
    }
}
