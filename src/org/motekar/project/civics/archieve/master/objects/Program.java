package org.motekar.project.civics.archieve.master.objects;

import java.util.ArrayList;

/**
 *
 * @author Muhamad Wibawa
 */
public class Program {

    private Long index = Long.valueOf(0);
    private String programCode = "";
    private String programName = "";

    private ArrayList<Activity> activitiys = new ArrayList<Activity>();

    public Program() {
    }

    public Program(Long index) {
        this.index = index;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public ArrayList<Activity> getActivitiys() {
        return activitiys;
    }

    public void setActivitiys(ArrayList<Activity> activitiys) {
        this.activitiys = activitiys;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Program other = (Program) obj;
        if (this.index != other.index && (this.index == null || !this.index.equals(other.index))) {
            return false;
        }
        if ((this.programCode == null) ? (other.programCode != null) : !this.programCode.equals(other.programCode)) {
            return false;
        }
        if ((this.programName == null) ? (other.programName != null) : !this.programName.equals(other.programName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.index != null ? this.index.hashCode() : 0);
        hash = 31 * hash + (this.programCode != null ? this.programCode.hashCode() : 0);
        hash = 31 * hash + (this.programName != null ? this.programName.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return programCode+" "+programName;
    }
}
