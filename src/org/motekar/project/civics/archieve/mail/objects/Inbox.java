package org.motekar.project.civics.archieve.mail.objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.motekar.project.civics.archieve.master.objects.Division;

/**
 *
 * @author Muhamad Wibawa
 */
public class Inbox {

    private Long index = Long.valueOf(0);
    private Date mailDate = null;
    private String mailNumber = "";
    private String subject = "";
    private String sender = "";
    private String senderAddress = "";
    private Date dispositionDate = null;
    private Division receipient = null;

    private boolean styled = false;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private ArrayList<InboxFile> inboxFiles = new ArrayList<InboxFile>();

    public Inbox() {
    }

    public Date getDispositionDate() {
        return dispositionDate;
    }

    public void setDispositionDate(Date dispositionDate) {
        this.dispositionDate = dispositionDate;
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

    public Division getReceipient() {
        return receipient;
    }

    public void setReceipient(Division receipient) {
        this.receipient = receipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
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

    public ArrayList<InboxFile> getInboxFiles() {
        return inboxFiles;
    }

    public void setInboxFiles(ArrayList<InboxFile> inboxFiles) {
        this.inboxFiles = inboxFiles;
    }

    @Override
    public String toString() {
        if (isStyled()) {
            return "<html><b>" + mailNumber + "</b>"
                    + "<br style=\"font-size:40%\">"+"["+ sdf.format(mailDate) +"] "+subject+ "</br>";
        }
        return mailNumber;
    }


}
