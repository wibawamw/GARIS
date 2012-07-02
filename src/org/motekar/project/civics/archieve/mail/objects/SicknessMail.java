package org.motekar.project.civics.archieve.mail.objects;

import java.util.Date;
import org.motekar.project.civics.archieve.master.objects.Employee;

/**
 *
 * @author Muhamad Wibawa
 */
public class SicknessMail {

    private Long index = Long.valueOf(0);
    private String documentNumber = "";
    private String patienceName = "";
    private Integer patienceAge = Integer.valueOf(0);
    private String jobs = "";
    private String address = "";
    private Date startDate = null;
    private Date endDate = null;
    private String approvalPlace = "";
    private Date approvalDate = null;
    
    private Employee approval = null;
    
    private boolean styled = false;
    
    public SicknessMail() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Employee getApproval() {
        return approval;
    }

    public void setApproval(Employee approval) {
        this.approval = approval;
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

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getJobs() {
        return jobs;
    }

    public void setJobs(String jobs) {
        this.jobs = jobs;
    }

    public Integer getPatienceAge() {
        return patienceAge;
    }

    public void setPatienceAge(Integer patienceAge) {
        this.patienceAge = patienceAge;
    }

    public String getPatienceName() {
        return patienceName;
    }

    public void setPatienceName(String patienceName) {
        this.patienceName = patienceName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }
    
    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" + documentNumber + "</b>";
        }

        return documentNumber;
    }
    
}
