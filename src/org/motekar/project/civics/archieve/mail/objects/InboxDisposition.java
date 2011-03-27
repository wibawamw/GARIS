package org.motekar.project.civics.archieve.mail.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.motekar.project.civics.archieve.master.objects.Division;

/**
 *
 * @author Muhamad Wibawa
 */
public class InboxDisposition {
    public static final String[] MAIL_STATUS = new String[]{"","Diterima","Diajukan","Disahkan","Ditolak"};

    public static final Integer RECEIVED = 1;
    public static final Integer PROPOSE = 2;
    public static final Integer VERIFIED = 3;
    public static final Integer DENIED = 4;

    private Long index = Long.valueOf(0);

    private Division division = null;
    private Date receiveDate = null;
    private Integer status = Integer.valueOf(0);

    private Long inboxIndex = Long.valueOf(0);

    public InboxDisposition() {
    }

    public Long getInboxIndex() {
        return inboxIndex;
    }

    public void setInboxIndex(Long inboxIndex) {
        this.inboxIndex = inboxIndex;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getStatusAsString() {
        return MAIL_STATUS[status];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InboxDisposition other = (InboxDisposition) obj;
        if (this.index != other.index && (this.index == null || !this.index.equals(other.index))) {
            return false;
        }
        if (this.division != other.division && (this.division == null || !this.division.equals(other.division))) {
            return false;
        }
        if (this.receiveDate != other.receiveDate && (this.receiveDate == null || !this.receiveDate.equals(other.receiveDate))) {
            return false;
        }
        if (this.status != other.status && (this.status == null || !this.status.equals(other.status))) {
            return false;
        }
        if (this.inboxIndex != other.inboxIndex && (this.inboxIndex == null || !this.inboxIndex.equals(other.inboxIndex))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.index != null ? this.index.hashCode() : 0);
        hash = 83 * hash + (this.division != null ? this.division.hashCode() : 0);
        hash = 83 * hash + (this.receiveDate != null ? this.receiveDate.hashCode() : 0);
        hash = 83 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 83 * hash + (this.inboxIndex != null ? this.inboxIndex.hashCode() : 0);
        return hash;
    }

    public static ArrayList<String> mailStatusAsList() {
        ArrayList<String> state = new ArrayList<String>();
        state.addAll(Arrays.asList(MAIL_STATUS));
        return state;
    }

    @Override
    public String toString() {
        return division.getName();
    }
}
