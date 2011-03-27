package org.motekar.project.civics.archieve.mail.objects;

import java.util.Date;

/**
 *
 * @author Muhamad Wibawa
 */
public class Regulation {

    private Long index = Long.valueOf(0);
    private String regulationNumber = "";
    private Date regulationDate = null;
    private String description = "";

    private boolean styled = false;

    public Regulation() {
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

    public Date getRegulationDate() {
        return regulationDate;
    }

    public void setRegulationDate(Date regulationDate) {
        this.regulationDate = regulationDate;
    }

    public String getRegulationNumber() {
        return regulationNumber;
    }

    public void setRegulationNumber(String regulationNumber) {
        this.regulationNumber = regulationNumber;
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
            return "<html><b>" + regulationNumber + "</b>";
        }

        return regulationNumber;
    }
}
