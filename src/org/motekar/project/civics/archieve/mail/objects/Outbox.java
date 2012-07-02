package org.motekar.project.civics.archieve.mail.objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;

/**
 *
 * @author Muhamad Wibawa
 */
public class Outbox {

    private Long index = Long.valueOf(0);
    private Date mailDate = null;
    private String mailNumber = "";
    private String subject = "";

    private boolean styled = false;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private ArrayList<OutboxFile> outboxFiles = new ArrayList<OutboxFile>();
    
    private Unit unit = null;

    public Outbox() {
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Date getMailDate() {
        return mailDate;
    }

    public void setMailDate(Date mailDate) {
        this.mailDate = mailDate;
    }

    public String getMailNumber() {
        return mailNumber;
    }

    public void setMailNumber(String mailNumber) {
        this.mailNumber = mailNumber;
    }

    public boolean isStyled() {
        return styled;
    }

    public void setStyled(boolean styled) {
        this.styled = styled;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<OutboxFile> getOutboxFiles() {
        return outboxFiles;
    }

    public void setOutboxFiles(ArrayList<OutboxFile> outboxFiles) {
        this.outboxFiles = outboxFiles;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" + mailNumber + "</b>"
                    + "<br style=\"font-size:40%\">" + "[" + sdf.format(mailDate) + "] " + subject + "</br>";
        }
        return mailNumber;
    }
}
