package org.motekar.project.civics.archieve.expedition.objects;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionCost {

    private String transactionName = "";
    private Integer count = Integer.valueOf(0);
    private BigDecimal price = BigDecimal.ZERO;
    private String notes = "";

    private BigDecimal total = BigDecimal.ZERO;

    public ExpeditionCost() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public BigDecimal getTotal() {
        total = BigDecimal.ZERO;
        total = total.add(price);
        total = total.multiply(BigDecimal.valueOf(count.longValue()));
        total = total.setScale(2, RoundingMode.UNNECESSARY);

        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj == this) return true;

        if (obj instanceof ExpeditionCost) {
            ExpeditionCost cost = (ExpeditionCost) obj;
            if (this.getTransactionName().equals(cost.getTransactionName()) &&
                    this.getPrice().equals(cost.getPrice())){
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.transactionName != null ? this.transactionName.hashCode() : 0);
        hash = 17 * hash + (this.price != null ? this.price.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return transactionName;
    }
}
