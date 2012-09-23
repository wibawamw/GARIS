package org.motekar.project.civics.archieve.payroll.objects;

/**
 *
 * @author Muhamad Wibawa <wibawa@motekar-it.co.id>
 */
public class SubmissionRequirement {

    private Long index = Long.valueOf(0);
    private String requirementName = "";
    private boolean styled = false;

    public SubmissionRequirement() {
    }

    public SubmissionRequirement(Long index) {
        this.index = index;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getRequirementName() {
        return requirementName;
    }

    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    @Override
    public String toString() {
        if (styled) {
            return "<html><b>" + requirementName + "</b>";
        }
        return requirementName;
    }
}
