package org.motekar.project.civics.archieve.expedition.objects;

import java.util.ArrayList;
import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.master.objects.Employee;

/**
 *
 * @author Muhamad Wibawa
 */
public class AssignmentLetter {

    private Long index = Long.valueOf(0);
    private String documentNumber = "";
    private Employee commander = null;
    private String purpose = "";
    private String goals = "";
    private Date startDate = null;
    private String notes = "";
    private Date endDate = null;
    private boolean styled = false;
    private String approvalPlace = "";
    private Date approvalDate = null;

    private ArrayList<Employee> assignedEmployee = new ArrayList<Employee>();
    private ArrayList<String>  carbonCopy = new ArrayList<String>();
    
    private Unit unit = null;

    public AssignmentLetter() {
    }

    public AssignmentLetter(Long index) {
        this.index = index;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovalPlace() {
        return approvalPlace;
    }

    public void setApprovalPlace(String approvalPlace) {
        this.approvalPlace = approvalPlace;
    }

    public Employee getCommander() {
        return commander;
    }

    public void setCommander(Employee commander) {
        this.commander = commander;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    public ArrayList<String> getCarbonCopy() {
        return carbonCopy;
    }

    public void setCarbonCopy(ArrayList<String> carbonCopy) {
        this.carbonCopy = carbonCopy;
    }

    public ArrayList<Employee> getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(ArrayList<Employee> assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj == this) return true;

        if (obj instanceof AssignmentLetter) {
            AssignmentLetter letter = (AssignmentLetter) obj;
            if (this.getIndex() == letter.getIndex() &&
                    this.getDocumentNumber().equals(letter.getDocumentNumber()) &&
                    this.getPurpose().equals(letter.getPurpose()) &&
                    this.getCommander().equals(letter.getCommander()) &&
                    this.getApprovalPlace().equals(letter.getApprovalPlace()) &&
                    this.getApprovalDate().equals(letter.getApprovalDate())) {
                return true;
            } else {
                return false;
            }
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.index != null ? this.index.hashCode() : 0);
        hash = 31 * hash + (this.documentNumber != null ? this.documentNumber.hashCode() : 0);
        hash = 31 * hash + (this.commander != null ? this.commander.hashCode() : 0);
        hash = 31 * hash + (this.purpose != null ? this.purpose.hashCode() : 0);
        hash = 31 * hash + (this.approvalPlace != null ? this.approvalPlace.hashCode() : 0);
        hash = 31 * hash + (this.approvalDate != null ? this.approvalDate.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" + documentNumber + "</b>";
        }

        return documentNumber;
    }
}
