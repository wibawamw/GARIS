package org.motekar.project.civics.archieve.payroll.objects;

/**
 *
 * @author Muhamad Wibawa
 */
public class SubmissionType {

    private Long index = Long.valueOf(0);
    private String typeName = "";
    private boolean styled = false;

    public SubmissionType() {
    }
    
    public SubmissionType(Long index) {
        this.index = index;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubmissionType other = (SubmissionType) obj;
        if ((this.typeName == null) ? (other.typeName != null) : !this.typeName.equals(other.typeName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.typeName != null ? this.typeName.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        if (styled) {
            return "<html><b>" + typeName + "</b>";
        }
        return typeName;
    }
}
