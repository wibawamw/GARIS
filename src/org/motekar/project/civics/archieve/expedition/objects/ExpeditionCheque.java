package org.motekar.project.civics.archieve.expedition.objects;

import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.StandardPrice;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionCheque {

    private Long index = Long.valueOf(0);
    private Expedition expedition = null;
    private String payer = "";
    private String bankAccount = "";
    private String taxNumber = "";
    private String receivedFrom = "";
    private String notes = "";
    private String receivedPlace = "";
    private Employee headEmployee = null;
    private Employee pptk = null;
    private Employee clerk = null;
    private Employee paidTo = null;

    private Integer budgetType = Integer.valueOf(0);

    private StandardPrice amount = null;
    
    private Unit unit = null;

    private boolean styled = false;

    public ExpeditionCheque() {
    }

    public StandardPrice getAmount() {
        return amount;
    }

    public void setAmount(StandardPrice amount) {
        this.amount = amount;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Employee getClerk() {
        return clerk;
    }

    public void setClerk(Employee clerk) {
        this.clerk = clerk;
    }

    public Expedition getExpedition() {
        return expedition;
    }

    public void setExpedition(Expedition expedition) {
        this.expedition = expedition;
    }

    public Employee getHeadEmployee() {
        return headEmployee;
    }

    public void setHeadEmployee(Employee headEmployee) {
        this.headEmployee = headEmployee;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Employee getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(Employee paidTo) {
        this.paidTo = paidTo;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public Employee getPptk() {
        return pptk;
    }

    public void setPptk(Employee pptk) {
        this.pptk = pptk;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getReceivedPlace() {
        return receivedPlace;
    }

    public void setReceivedPlace(String receivedPlace) {
        this.receivedPlace = receivedPlace;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public Integer getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(Integer budgetType) {
        this.budgetType = budgetType;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    
    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>Kwitansi Atas</b>"
                    + "<br style=\"font-size:40%\"><i>SPPD No. " + expedition.getDocumentNumber() + "</i></br>"
                    +"<br style=\"font-size:40%\">Pegawai " + paidTo.getName().toUpperCase() + "</br>";
        }

        return "Kwitansi Atas SPPD No. "+expedition.getDocumentNumber();
    }
    
}
