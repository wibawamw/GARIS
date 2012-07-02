package org.motekar.project.civics.archieve.mail.objects;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Muhamad Wibawa
 */
public class ReferenceMail {

    private Long index = Long.valueOf(0);
    private String mailNumber = "";
    private Date mailDate = null;
    private String receiver = "";
    private String receiverAddress = "";
    private String subject = "";
    private String description = "";
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    private boolean styled = false;

    public ReferenceMail() {
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
            return "<html><b>" + mailNumber + "</b>"
                    + "<br style=\"font-size:40%\">" + "[" + sdf.format(mailDate) + "] " + subject + "</br>";
        }
        return mailNumber;
    }
}
