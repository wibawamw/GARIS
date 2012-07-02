package org.motekar.project.civics.archieve.mail.objects;

import java.util.Date;
import org.motekar.project.civics.archieve.master.objects.Employee;

/**
 *
 * @author Muhamad Wibawa
 */
public class DoctorMail {
    
    private Long index = Long.valueOf(0);
    private String documentNumber = "";
    private String patienceName = "";
    private String birthPlace = "";
    private Date birthDate = null;
    private String jobs = "";
    private String address = "";
    private String requested = "";
    private Date mailDate = null;
    private String mailNumber = "";
    private String checked = "";
    private String term = "";
    private Integer height = Integer.valueOf(0);
    private Integer weight = Integer.valueOf(0);
    private Integer bloodPreasure = Integer.valueOf(0);
    private Integer bloodPreasure2 = Integer.valueOf(0);
    private String approvalPlace = "";
    private Date approvalDate = null;
    
    private Employee approval = null;
    
    private Date expiredDate = null;
    
    private boolean styled = false;
    
    public DoctorMail() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getBloodPreasure() {
        return bloodPreasure;
    }

    public Integer getBloodPreasure2() {
        return bloodPreasure2;
    }

    public void setBloodPreasure2(Integer bloodPreasure2) {
        this.bloodPreasure2 = bloodPreasure2;
    }
    
    public void setBloodPreasure(Integer bloodPreasure) {
        this.bloodPreasure = bloodPreasure;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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

    public Date getMailDate() {
        return mailDate;
    }

    public void setMailDate(Date mailDate) {
        this.mailDate = mailDate;
    }

    public String getMailNumber() {
        return mailNumber;
    }

    public void setMailNumber(String mailNumber) {
        this.mailNumber = mailNumber;
    }

    public String getPatienceName() {
        return patienceName;
    }

    public void setPatienceName(String patienceName) {
        this.patienceName = patienceName;
    }

    public String getRequested() {
        return requested;
    }

    public void setRequested(String requested) {
        this.requested = requested;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }
    
    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" + documentNumber + "</b>";
        }

        return documentNumber;
    }
    
}
