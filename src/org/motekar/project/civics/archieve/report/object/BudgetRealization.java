package org.motekar.project.civics.archieve.report.object;

import java.math.BigDecimal;

/**
 *
 * @author Muhamad Wibawa
 */
public class BudgetRealization {

    private String code = "";
    private String description = "";
    private BigDecimal opening = BigDecimal.ZERO;
    private BigDecimal debet = BigDecimal.ZERO;
    private BigDecimal credit = BigDecimal.ZERO;
    private BigDecimal closing = BigDecimal.ZERO;

    private String marker = "";

    public BudgetRealization() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public BigDecimal getDebet() {
        return debet;
    }

    public void setDebet(BigDecimal debet) {
        this.debet = debet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getOpening() {
        return opening;
    }

    public void setOpening(BigDecimal opening) {
        this.opening = opening;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }
}
