package org.motekar.project.civics.archieve.master.objects;

import java.util.StringTokenizer;

/**
 *
 * @author Muhamad Wibawa
 */
public class Activity {

    private Long index = Long.valueOf(0);
    private String activityCode = "";
    private String activityName = "";
    private Long programIndex = Long.valueOf(0);

    public Activity() {
    }

    public Activity(Long index) {
        this.index = index;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getProgramIndex() {
        return programIndex;
    }

    public void setProgramIndex(Long programIndex) {
        this.programIndex = programIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Activity other = (Activity) obj;
        if (this.index != other.index && (this.index == null || !this.index.equals(other.index))) {
            return false;
        }
        if ((this.activityCode == null) ? (other.activityCode != null) : !this.activityCode.equals(other.activityCode)) {
            return false;
        }
        if ((this.activityName == null) ? (other.activityName != null) : !this.activityName.equals(other.activityName)) {
            return false;
        }
        return true;
    }

    public String getChildCode() {
        StringTokenizer token = new StringTokenizer(activityCode, ".");
        String childCode = "";
        while (token.hasMoreElements()) {
            childCode = token.nextToken();
        }

        return childCode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.index != null ? this.index.hashCode() : 0);
        hash = 79 * hash + (this.activityCode != null ? this.activityCode.hashCode() : 0);
        hash = 79 * hash + (this.activityName != null ? this.activityName.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return activityCode + " " + activityName;
    }
}
