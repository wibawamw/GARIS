package org.motekar.project.civics.archieve.mail.objects;

import java.util.Date;

/**
 *
 * @author Muhamad Wibawa
 */
public class Decree {

    private Long index = Long.valueOf(0);
    private String decreeNumber = "";
    private Date decreeDate = null;
    private String description = "";

    private boolean styled = false;

    public Decree() {
    }

    public Date getDecreeDate() {
        return decreeDate;
    }

    public void setDecreeDate(Date decreeDate) {
        this.decreeDate = decreeDate;
    }

    public String getDecreeNumber() {
        return decreeNumber;
    }

    public void setDecreeNumber(String decreeNumber) {
        this.decreeNumber = decreeNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
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
            return "<html><b>" + decreeNumber + "</b>";
        }

        return decreeNumber;
    }
}
