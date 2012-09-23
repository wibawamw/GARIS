package org.motekar.project.civics.archieve.payroll.objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.SKPD;

/**
 *
 * @author Muhamad Wibawa <wibawa@motekar-it.co.id>
 */
public class PayrollSubmission {

    private Long index = Long.valueOf(0);
    private SKPD skpd = null;
    private Employee employee = null;
    private SubmissionType type = null;
    private Date submissionDate = null;
    private boolean proccesed = false;
    private boolean styled = false;
    
    private ArrayList<PayrollSubmissionRequirement> requirements = new ArrayList<PayrollSubmissionRequirement>();
    private ArrayList<PayrollSubmissionFile> files = new ArrayList<PayrollSubmissionFile>();

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public boolean isProccesed() {
        return proccesed;
    }

    public void setProccesed(boolean proccesed) {
        this.proccesed = proccesed;
    }

    public SKPD getSkpd() {
        return skpd;
    }

    public void setSkpd(SKPD skpd) {
        this.skpd = skpd;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public SubmissionType getType() {
        return type;
    }

    public void setType(SubmissionType type) {
        this.type = type;
    }

    public ArrayList<PayrollSubmissionRequirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<PayrollSubmissionRequirement> requirements) {
        this.requirements = requirements;
    }

    public ArrayList<PayrollSubmissionFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<PayrollSubmissionFile> files) {
        this.files = files;
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            String dateStr = sdf.format(this.submissionDate.getTime());

            return "<html><b>" + employee.getName() + "(" + skpd.getName() + ")</b>"
                    + "<br style=\"font-size:40%\"><i>" + type.getTypeName() + "(" + dateStr + ")</i></br>";
        }
        return employee.getName();
    }
}
