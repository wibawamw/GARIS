package org.motekar.project.civics.archieve.expedition.objects;

import org.motekar.project.civics.archieve.master.objects.Employee;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionFollower  {

    private Employee follower = null;
    private String notes = "";

    public ExpeditionFollower() {
    }

    public Employee getFollower() {
        return follower;
    }

    public void setFollower(Employee follower) {
        this.follower = follower;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;

        if (obj == this) return true;

        if (obj instanceof ExpeditionFollower) {
            ExpeditionFollower foll = (ExpeditionFollower) obj;
            if (this.getFollower().equals(foll.getFollower())){
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.follower != null ? this.follower.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return follower.getName();
    }
}
