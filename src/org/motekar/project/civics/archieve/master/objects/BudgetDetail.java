package org.motekar.project.civics.archieve.master.objects;

import java.util.ArrayList;

/**
 *
 * @author Muhamad Wibawa
 */
public class BudgetDetail {

    private Long index = Long.valueOf(0);
    private Long parentIndex = Long.valueOf(0);

    private Program program = null;

    private ArrayList<BudgetSubDetail> subDetails = new ArrayList<BudgetSubDetail>();

    public BudgetDetail() {
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Long parentIndex) {
        this.parentIndex = parentIndex;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public ArrayList<BudgetSubDetail> getSubDetails() {
        return subDetails;
    }

    public void setSubDetails(ArrayList<BudgetSubDetail> subDetails) {
        this.subDetails = subDetails;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BudgetDetail other = (BudgetDetail) obj;
        if (this.program != other.program && (this.program == null || !this.program.equals(other.program))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.program != null ? this.program.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return program.toString();
    }
}
