package org.motekar.project.civics.archieve.expedition.objects;

import java.util.Date;

/**
 *
 * @author Muhamad Wibawa
 */
public class Approval {

    private String place = "";
    private Date date = null;

    public Approval() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

}
