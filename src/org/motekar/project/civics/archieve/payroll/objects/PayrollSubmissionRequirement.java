package org.motekar.project.civics.archieve.payroll.objects;

import java.util.ArrayList;

/**
 *
 * @author Muhamad Wibawa <wibawa@motekar-it.co.id>
 */
public class PayrollSubmissionRequirement {
    
    private Long submissionIndex = Long.valueOf(0);
    private SubmissionRequirement requirement = null;
    private boolean checked = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public SubmissionRequirement getRequirement() {
        return requirement;
    }

    public void setRequirement(SubmissionRequirement requirement) {
        this.requirement = requirement;
    }

    public Long getSubmissionIndex() {
        return submissionIndex;
    }

    public void setSubmissionIndex(Long submissionIndex) {
        this.submissionIndex = submissionIndex;
    }
    
    public static ArrayList<PayrollSubmissionRequirement> createRequirement(ArrayList<SubmissionRequirement> subRequirements) {
        ArrayList<PayrollSubmissionRequirement> psrs = new ArrayList<PayrollSubmissionRequirement>();
        
        if (!subRequirements.isEmpty()) {
            for (SubmissionRequirement sr : subRequirements) {
                PayrollSubmissionRequirement psr = new PayrollSubmissionRequirement();
                psr.setRequirement(sr);
                psrs.add(psr);
            }
        }
        
        return psrs;
    }

    @Override
    public String toString() {
        if (requirement != null) {
            return requirement.getRequirementName();
        }
        return "";
    }
}
