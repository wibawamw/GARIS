package org.motekar.project.civics.archieve.mail.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Muhamad Wibawa
 */
public class OutboxDisposition {
    public static final String[] MAIL_STATUS = new String[]{"","Diterima","Diajukan","Disahkan","Ditolak"};

    public static final Integer RECEIVED = 1;
    public static final Integer PROPOSE = 2;
    public static final Integer VERIFIED = 3;
    public static final Integer DENIED = 4;

    private Long index = Long.valueOf(0);

    private String receipient = "";
    private String receipientAddress = "";
    private Date receiveDate = null;
    private Integer status = Integer.valueOf(0);

    private Long outboxIndex = Long.valueOf(0);
    private String mailNumber = "";

    public OutboxDisposition() {
    }

    public Long getOutboxIndex() {
        return outboxIndex;
    }

    public void setOutboxIndex(Long outboxIndex) {
        this.outboxIndex = outboxIndex;
    }

    public String getReceipient() {
        return receipient;
    }

    public void setReceipient(String receipient) {
        this.receipient = receipient;
    }

    public String getReceipientAddress() {
        return receipientAddress;
    }

    public void setReceipientAddress(String receipientAddress) {
        this.receipientAddress = receipientAddress;
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

    public String getMailNumber() {
        return mailNumber;
    }

    public void setMailNumber(String mailNumber) {
        this.mailNumber = mailNumber;
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
        final OutboxDisposition other = (OutboxDisposition) obj;
        if (this.index != other.index && (this.index == null || !this.index.equals(other.index))) {
            return false;
        }
        if ((this.receipient == null) ? (other.receipient != null) : !this.receipient.equals(other.receipient)) {
            return false;
        }
        if ((this.receipientAddress == null) ? (other.receipientAddress != null) : !this.receipientAddress.equals(other.receipientAddress)) {
            return false;
        }
        if (this.receiveDate != other.receiveDate && (this.receiveDate == null || !this.receiveDate.equals(other.receiveDate))) {
            return false;
        }
        if (this.status != other.status && (this.status == null || !this.status.equals(other.status))) {
            return false;
        }
        if (this.outboxIndex != other.outboxIndex && (this.outboxIndex == null || !this.outboxIndex.equals(other.outboxIndex))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.index != null ? this.index.hashCode() : 0);
        hash = 31 * hash + (this.receipient != null ? this.receipient.hashCode() : 0);
        hash = 31 * hash + (this.receipientAddress != null ? this.receipientAddress.hashCode() : 0);
        hash = 31 * hash + (this.receiveDate != null ? this.receiveDate.hashCode() : 0);
        hash = 31 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 31 * hash + (this.outboxIndex != null ? this.outboxIndex.hashCode() : 0);
        return hash;
    }

    public static ArrayList<String> mailStatusAsList() {
        ArrayList<String> state = new ArrayList<String>();
        state.addAll(Arrays.asList(MAIL_STATUS));
        return state;
    }

    @Override
    public String toString() {
        return receipient;
    }
}
