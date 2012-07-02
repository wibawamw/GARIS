package org.motekar.project.civics.archieve.mail.sqlapi;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import org.motekar.project.civics.archieve.mail.objects.*;
import org.motekar.project.civics.archieve.master.objects.Division;
import org.motekar.project.civics.archieve.master.objects.SKPD;
import org.motekar.project.civics.archieve.sqlapi.CommonSQL;

/**
 *
 * @author Muhamad Wibawa
 */
public class MailSQL extends CommonSQL {

    void insertInbox(Connection conn, Inbox inbox) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into inbox(maildate,mailnumber,subject,").
                append("sender,senderaddress,dispositiondate,receiver)").
                append("values(?,?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setDate(++col, new java.sql.Date(inbox.getMailDate().getTime()));
        pstm.setString(++col, inbox.getMailNumber());
        pstm.setString(++col, inbox.getSubject());
        pstm.setString(++col, inbox.getSender());
        pstm.setString(++col, inbox.getSenderAddress());

        if (inbox.getDispositionDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(inbox.getDispositionDate().getTime()));
        }

        if (inbox.getReceipient() == null) {
            pstm.setNull(++col, Types.VARCHAR);
        } else {
            pstm.setString(++col, inbox.getReceipient().getCode());
        }


        pstm.executeUpdate();
        pstm.close();
    }

    void updateInbox(Connection conn, Long oldIndex, Inbox inbox) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update inbox set maildate = ?, mailnumber = ?, subject = ?, ").
                append("sender = ?, senderaddress = ?,dispositiondate = ?, receiver = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setDate(++col, new java.sql.Date(inbox.getMailDate().getTime()));
        pstm.setString(++col, inbox.getMailNumber());
        pstm.setString(++col, inbox.getSubject());
        pstm.setString(++col, inbox.getSender());
        pstm.setString(++col, inbox.getSenderAddress());

        if (inbox.getDispositionDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(inbox.getDispositionDate().getTime()));
        }

        if (inbox.getReceipient() == null) {
            pstm.setNull(++col, Types.VARCHAR);
        } else {
            pstm.setString(++col, inbox.getReceipient().getCode());
        }
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteInbox(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from inbox where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Inbox> getInbox(Connection conn) throws SQLException {
        ArrayList<Inbox> inboxs = new ArrayList<Inbox>();

        StringBuilder query = new StringBuilder();
        query.append("select i.*,d.code divisioncode,d.divisionname divisionname from inbox i ").
                append("left join division d ").
                append("on i.receiver = d.code ").
                append("order by maildate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Inbox inbox = new Inbox();
            inbox.setIndex(rs.getLong("autoindex"));
            inbox.setMailDate(rs.getDate("maildate"));
            inbox.setMailNumber(rs.getString("mailnumber"));
            inbox.setSubject(rs.getString("subject"));
            inbox.setSender(rs.getString("sender"));
            inbox.setSenderAddress(rs.getString("senderaddress"));
            inbox.setDispositionDate(rs.getDate("dispositiondate"));

            Division receiver = null;

            String divisionCode = rs.getString("divisioncode");
            String divisionName = rs.getString("divisionname");

            if (divisionCode != null) {
                receiver = new Division();
                receiver.setCode(divisionCode);
                receiver.setName(divisionName);
            }

            inbox.setReceipient(receiver);

            inbox.setStyled(true);

            inboxs.add(inbox);
        }

        rs.close();
        pstm.close();

        return inboxs;
    }

    ArrayList<Inbox> getInbox(Connection conn, Integer month, Integer year) throws SQLException {
        ArrayList<Inbox> inboxs = new ArrayList<Inbox>();

        StringBuilder query = new StringBuilder();

        if (month == 0) {
            query.append("select i.*,d.code divisioncode,d.divisionname from inbox i ").
                    append("left join division d ").
                    append("on i.receiver = d.code ").
                    append("where date_part('year',maildate) = ").
                    append(year).
                    append(" order by maildate");
        } else {
            query.append("select i.*,d.code divisioncode,d.divisionname from inbox i ").
                    append("left join division d ").
                    append("on i.receiver = d.code ").
                    append("where date_part('month',maildate) = ").
                    append(month).
                    append(" and date_part('year',maildate) = ").
                    append(year).
                    append(" order by maildate");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Inbox inbox = new Inbox();
            inbox.setIndex(rs.getLong("autoindex"));
            inbox.setMailDate(rs.getDate("maildate"));
            inbox.setMailNumber(rs.getString("mailnumber"));
            inbox.setSubject(rs.getString("subject"));
            inbox.setSender(rs.getString("sender"));
            inbox.setSenderAddress(rs.getString("senderaddress"));
            inbox.setDispositionDate(rs.getDate("dispositiondate"));

            Division receiver = null;

            String divisionCode = rs.getString("divisioncode");
            String divisionName = rs.getString("divisionname");

            if (divisionCode != null) {
                receiver = new Division();
                receiver.setCode(divisionCode);
                receiver.setName(divisionName);
            }

            inbox.setReceipient(receiver);

            inbox.setStyled(true);

            inboxs.add(inbox);
        }

        rs.close();
        pstm.close();

        return inboxs;
    }

    ArrayList<Inbox> getInbox(Connection conn, Date date, Date date2) throws SQLException {
        ArrayList<Inbox> inboxs = new ArrayList<Inbox>();

        StringBuilder query = new StringBuilder();

        query.append("select i.*,d.code divisioncode,d.divisionname from inbox i ").
                append("left join division d ").
                append("on i.receiver = d.code ").
                append("where maildate between ").
                append("'").append(new java.sql.Date(date.getTime())).append("' ").
                append(" and ").
                append("'").append(new java.sql.Date(date2.getTime())).append("' ").
                append("order by maildate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Inbox inbox = new Inbox();
            inbox.setIndex(rs.getLong("autoindex"));
            inbox.setMailDate(rs.getDate("maildate"));
            inbox.setMailNumber(rs.getString("mailnumber"));
            inbox.setSubject(rs.getString("subject"));
            inbox.setSender(rs.getString("sender"));
            inbox.setSenderAddress(rs.getString("senderaddress"));
            inbox.setDispositionDate(rs.getDate("dispositiondate"));

            Division receiver = null;

            String divisionCode = rs.getString("divisioncode");
            String divisionName = rs.getString("divisionname");

            if (divisionCode != null) {
                receiver = new Division();
                receiver.setCode(divisionCode);
                receiver.setName(divisionName);
            }

            inbox.setReceipient(receiver);

            inbox.setStyled(true);

            inboxs.add(inbox);
        }

        rs.close();
        pstm.close();

        return inboxs;
    }

    void insertInboxDisposition(Connection conn, Inbox inbox, InboxDisposition disposition) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into inboxdisposition(inboxindex,division,receivedate,status)").
                append("values(?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, inbox.getIndex());
        pstm.setString(++col, disposition.getDivision().getCode());
        pstm.setDate(++col, new java.sql.Date(disposition.getReceiveDate().getTime()));
        pstm.setInt(++col, disposition.getStatus());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateInboxDisposition(Connection conn, InboxDisposition oldDispo, InboxDisposition newDispo) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update inboxdisposition set inboxindex = ? , ").
                append("division = ?,receivedate = ?,status = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, newDispo.getInboxIndex());
        pstm.setString(++col, newDispo.getDivision().getCode());
        pstm.setDate(++col, new java.sql.Date(newDispo.getReceiveDate().getTime()));
        pstm.setInt(++col, newDispo.getStatus());

        pstm.setLong(++col, oldDispo.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteInboxDisposition(Connection conn, Long index) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("delete from inboxdisposition where autoindex = ? ");
        PreparedStatement pstm = conn.prepareStatement(builder.toString());
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<InboxDisposition> getInboxDisposition(Connection conn, Long inboxIndex) throws SQLException {
        ArrayList<InboxDisposition> dispositions = new ArrayList<InboxDisposition>();

        StringBuilder query = new StringBuilder();
        query.append("select id.*,d.code divisioncode,d.divisionname from inboxdisposition id ").
                append("left join division d ").
                append("on id.division = d.code ").
                append("where inboxindex = ? ").
                append("order by receivedate");


        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, inboxIndex);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            InboxDisposition disposition = new InboxDisposition();

            disposition.setIndex(rs.getLong("autoindex"));
            disposition.setInboxIndex(rs.getLong("inboxindex"));
            disposition.setReceiveDate(rs.getDate("receivedate"));
            disposition.setStatus(rs.getInt("status"));

            Division division = null;

            String divisionCode = rs.getString("divisioncode");
            String divisionName = rs.getString("divisionname");

            if (divisionCode != null) {
                division = new Division();
                division.setCode(divisionCode);
                division.setName(divisionName);
            }

            disposition.setDivision(division);

            dispositions.add(disposition);
        }

        rs.close();
        pstm.close();

        return dispositions;
    }

    void insertInboxFile(Connection conn, InboxFile file) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into inboxfile(inboxindex,filename,filebyte)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, file.getInboxIndex());
        pstm.setString(++col, file.getFileName());
        pstm.setBytes(++col, file.getFileStream());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteInboxFile(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("delete from inboxfile ").
                append("where inboxindex = ? ");
        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<InboxFile> getInboxFile(Connection conn, Long index) throws SQLException {
        ArrayList<InboxFile> inboxFiles = new ArrayList<InboxFile>();

        StringBuilder query = new StringBuilder();
        query.append("select * from inboxfile ").
                append("where inboxindex = ? ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            InboxFile file = new InboxFile();

            file.setInboxIndex(rs.getLong("inboxindex"));
            file.setFileName(rs.getString("filename"));
            file.setFileStream(rs.getBytes("filebyte"));

            inboxFiles.add(file);
        }

        rs.close();
        pstm.close();

        return inboxFiles;
    }

    void insertOutbox(Connection conn, Outbox outbox) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into outbox(maildate,mailnumber,subject)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setDate(++col, new java.sql.Date(outbox.getMailDate().getTime()));
        pstm.setString(++col, outbox.getMailNumber());
        pstm.setString(++col, outbox.getSubject());
        pstm.executeUpdate();
        pstm.close();
    }

    void updateOutbox(Connection conn, Long oldIndex, Outbox outbox) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update outbox set maildate = ?, mailnumber = ?, subject = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setDate(++col, new java.sql.Date(outbox.getMailDate().getTime()));
        pstm.setString(++col, outbox.getMailNumber());
        pstm.setString(++col, outbox.getSubject());
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteOutbox(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from outbox where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Outbox> getOutbox(Connection conn) throws SQLException {
        ArrayList<Outbox> outboxs = new ArrayList<Outbox>();

        StringBuilder query = new StringBuilder();
        query.append("select * from outbox ").
                append("order by maildate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Outbox outbox = new Outbox();
            outbox.setIndex(rs.getLong("autoindex"));
            outbox.setMailDate(rs.getDate("maildate"));
            outbox.setMailNumber(rs.getString("mailnumber"));
            outbox.setSubject(rs.getString("subject"));

            outbox.setStyled(true);

            outboxs.add(outbox);
        }

        rs.close();
        pstm.close();

        return outboxs;
    }

    ArrayList<Outbox> getOutbox(Connection conn, Integer month, Integer year) throws SQLException {
        ArrayList<Outbox> outboxs = new ArrayList<Outbox>();

        StringBuilder query = new StringBuilder();

        if (month == 0) {
            query.append("select * from outbox ").
                    append("where date_part('year',maildate) = ").
                    append(year).
                    append(" order by maildate");
        } else {
            query.append("select * from outbox ").
                    append("where date_part('month',maildate) = ").
                    append(month).
                    append(" and date_part('year',maildate) = ").
                    append(year).
                    append(" order by maildate");
        }



        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Outbox outbox = new Outbox();
            outbox.setIndex(rs.getLong("autoindex"));
            outbox.setMailDate(rs.getDate("maildate"));
            outbox.setMailNumber(rs.getString("mailnumber"));
            outbox.setSubject(rs.getString("subject"));

            outbox.setStyled(true);

            outboxs.add(outbox);
        }

        rs.close();
        pstm.close();

        return outboxs;
    }

    ArrayList<Outbox> getOutbox(Connection conn, Date date, Date date2) throws SQLException {
        ArrayList<Outbox> outboxs = new ArrayList<Outbox>();

        StringBuilder query = new StringBuilder();
        query.append("select * from outbox ").
                append("where maildate between ").
                append("'").append(new java.sql.Date(date.getTime())).append("' ").
                append(" and ").
                append("'").append(new java.sql.Date(date2.getTime())).append("' ").
                append("order by maildate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Outbox outbox = new Outbox();
            outbox.setIndex(rs.getLong("autoindex"));
            outbox.setMailDate(rs.getDate("maildate"));
            outbox.setMailNumber(rs.getString("mailnumber"));
            outbox.setSubject(rs.getString("subject"));

            outbox.setStyled(true);

            outboxs.add(outbox);
        }

        rs.close();
        pstm.close();

        return outboxs;
    }

    void insertOutboxDisposition(Connection conn, Outbox outbox, OutboxDisposition disposition) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into outboxdisposition(outboxindex,receiver,receiveraddress,").
                append("receivedate,status)").
                append("values(?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, outbox.getIndex());
        pstm.setString(++col, disposition.getReceipient());
        pstm.setString(++col, disposition.getReceipientAddress());
        pstm.setDate(++col, new java.sql.Date(disposition.getReceiveDate().getTime()));
        pstm.setInt(++col, disposition.getStatus());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateOutboxDisposition(Connection conn, OutboxDisposition oldDispo, OutboxDisposition newDispo) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update outboxdisposition set outboxindex = ? , receiver = ?, ").
                append("receiveraddress = ?,receivedate = ?,status = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, newDispo.getOutboxIndex());
        pstm.setString(++col, newDispo.getReceipient());
        pstm.setString(++col, newDispo.getReceipientAddress());
        pstm.setDate(++col, new java.sql.Date(newDispo.getReceiveDate().getTime()));
        pstm.setInt(++col, newDispo.getStatus());

        pstm.setLong(++col, oldDispo.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteOutboxDisposition(Connection conn, Long index) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("delete from outboxdisposition where autoindex = ? ");
        PreparedStatement pstm = conn.prepareStatement(builder.toString());
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<OutboxDisposition> getOutboxDisposition(Connection conn, Long outboxIndex) throws SQLException {
        ArrayList<OutboxDisposition> dispositions = new ArrayList<OutboxDisposition>();

        StringBuilder query = new StringBuilder();
        query.append("select * from outboxdisposition ").
                append("where outboxindex = ? ").
                append("order by receivedate");


        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, outboxIndex);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            OutboxDisposition disposition = new OutboxDisposition();

            disposition.setIndex(rs.getLong("autoindex"));
            disposition.setOutboxIndex(rs.getLong("outboxindex"));
            disposition.setReceipient(rs.getString("receiver"));
            disposition.setReceipientAddress(rs.getString("receiveraddress"));
            disposition.setReceiveDate(rs.getDate("receivedate"));
            disposition.setStatus(rs.getInt("status"));
            dispositions.add(disposition);
        }

        rs.close();
        pstm.close();

        return dispositions;
    }

    void insertOutboxFile(Connection conn, OutboxFile file) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into outboxfile(outboxindex,filename,filebyte)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, file.getOutboxIndex());
        pstm.setString(++col, file.getFileName());
        pstm.setBytes(++col, file.getFileStream());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteOutboxFile(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("delete from outboxfile ").
                append("where outboxindex = ? ");
        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<OutboxFile> getOutboxFile(Connection conn, Long index) throws SQLException {
        ArrayList<OutboxFile> outboxFiles = new ArrayList<OutboxFile>();

        StringBuilder query = new StringBuilder();
        query.append("select * from outboxfile ").
                append("where outboxindex = ? ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            OutboxFile file = new OutboxFile();

            file.setOutboxIndex(rs.getLong("outboxindex"));
            file.setFileName(rs.getString("filename"));
            file.setFileStream(rs.getBytes("filebyte"));

            outboxFiles.add(file);
        }

        rs.close();
        pstm.close();

        return outboxFiles;
    }
    //

    void insertContractMail(Connection conn, ContractMail mail) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into contractmail(mailnumber,maildate,receiver,").
                append("receiveraddress,subject,description)").
                append("values(?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, mail.getMailNumber());
        if (mail.getMailDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getMailDate().getTime()));
        }
        pstm.setString(++col, mail.getReceiver());
        pstm.setString(++col, mail.getReceiverAddress());

        pstm.setString(++col, mail.getSubject());
        pstm.setString(++col, mail.getDescription());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateContractMail(Connection conn, Long oldIndex, ContractMail mail) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update contractmail set mailnumber = ?,maildate = ?,receiver = ?,").
                append("receiveraddress = ?,subject = ?,description = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, mail.getMailNumber());
        if (mail.getMailDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getMailDate().getTime()));
        }
        pstm.setString(++col, mail.getReceiver());
        pstm.setString(++col, mail.getReceiverAddress());

        pstm.setString(++col, mail.getSubject());
        pstm.setString(++col, mail.getDescription());

        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteContractMail(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from contractmail where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ContractMail> getContractMail(Connection conn) throws SQLException {
        ArrayList<ContractMail> mails = new ArrayList<ContractMail>();

        StringBuilder query = new StringBuilder();
        query.append("select * from contractmail ").
                append("order by maildate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ContractMail mail = new ContractMail();
            mail.setIndex(rs.getLong("autoindex"));
            mail.setMailDate(rs.getDate("maildate"));
            mail.setMailNumber(rs.getString("mailnumber"));
            mail.setReceiver(rs.getString("receiver"));
            mail.setReceiverAddress(rs.getString("receiveraddress"));
            mail.setSubject(rs.getString("subject"));
            mail.setDescription(rs.getString("description"));

            mail.setStyled(true);

            mails.add(mail);
        }

        rs.close();
        pstm.close();

        return mails;
    }

    ArrayList<ContractMail> getContractMail(Connection conn, Integer month, Integer year) throws SQLException {
        ArrayList<ContractMail> mails = new ArrayList<ContractMail>();

        StringBuilder query = new StringBuilder();

        if (month == 0) {
            query.append("select * from contractmail ").
                    append("where date_part('year',maildate) = ").
                    append(year).
                    append(" order by maildate");
        } else {
            query.append("select * from contractmail ").
                    append("where date_part('month',maildate) = ").
                    append(month).
                    append(" and date_part('year',maildate) = ").
                    append(year).
                    append(" order by maildate");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ContractMail mail = new ContractMail();
            mail.setIndex(rs.getLong("autoindex"));
            mail.setMailDate(rs.getDate("maildate"));
            mail.setMailNumber(rs.getString("mailnumber"));
            mail.setReceiver(rs.getString("receiver"));
            mail.setReceiverAddress(rs.getString("receiveraddress"));
            mail.setSubject(rs.getString("subject"));
            mail.setDescription(rs.getString("description"));

            mail.setStyled(true);

            mails.add(mail);
        }

        rs.close();
        pstm.close();

        return mails;
    }

    ArrayList<ContractMail> getContractMail(Connection conn, Date date, Date date2) throws SQLException {
        ArrayList<ContractMail> mails = new ArrayList<ContractMail>();

        StringBuilder query = new StringBuilder();

        query.append("select * from contractmail ").
                append("where maildate between ").
                append("'").append(new java.sql.Date(date.getTime())).append("' ").
                append(" and ").
                append("'").append(new java.sql.Date(date2.getTime())).append("' ").
                append("order by maildate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ContractMail mail = new ContractMail();
            mail.setIndex(rs.getLong("autoindex"));
            mail.setMailDate(rs.getDate("maildate"));
            mail.setMailNumber(rs.getString("mailnumber"));
            mail.setReceiver(rs.getString("receiver"));
            mail.setReceiverAddress(rs.getString("receiveraddress"));
            mail.setSubject(rs.getString("subject"));
            mail.setDescription(rs.getString("description"));

            mail.setStyled(true);

            mails.add(mail);
        }

        rs.close();
        pstm.close();

        return mails;
    }

    //
    void insertSP2D(Connection conn, SP2D sp2d) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into sp2d(skpdindex, sp2dnumber, sp2ddate, receiver, purpose,").
                append("amount, description)").
                append("values(?, ?, ?, ?, ?, ?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, sp2d.getSkpd().getIndex());
        pstm.setString(++col, sp2d.getSp2dNumber());
        pstm.setDate(++col, new java.sql.Date(sp2d.getSp2dDate().getTime()));
        pstm.setString(++col, sp2d.getReceiver());
        pstm.setString(++col, sp2d.getPurpose());
        pstm.setBigDecimal(++col, sp2d.getAmount());
        pstm.setString(++col, sp2d.getDescription());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateSP2D(Connection conn, SP2D oldSp2d, SP2D newSp2d) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update sp2d set skpdindex=?, sp2dnumber=?, sp2ddate=?, receiver=?,").
                append("purpose=?, amount=?, description=? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setLong(++col, newSp2d.getSkpd().getIndex());
        pstm.setString(++col, newSp2d.getSp2dNumber());
        pstm.setDate(++col, new java.sql.Date(newSp2d.getSp2dDate().getTime()));
        pstm.setString(++col, newSp2d.getReceiver());
        pstm.setString(++col, newSp2d.getPurpose());
        pstm.setBigDecimal(++col, newSp2d.getAmount());
        pstm.setString(++col, newSp2d.getDescription());
        pstm.setLong(++col, oldSp2d.getIndex());
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteSP2D(Connection conn, SP2D sp2d) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from sp2d where autoindex = ?");
        pstm.setLong(1, sp2d.getIndex());
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<SP2D> getSP2D(Connection conn) throws SQLException {
        ArrayList<SP2D> sp2ds = new ArrayList<SP2D>();

        StringBuilder query = new StringBuilder();
        query.append("select * from sp2d ").
                append("order by sp2ddate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            Long skpdIndex = rs.getLong("skpdindex");
            SKPD skpd = null;

            if (skpdIndex != null) {
                if (!skpdIndex.equals(Long.valueOf(0))) {
                    skpd = new SKPD(skpdIndex);
                }
            }

            SP2D sp2d = new SP2D();
            sp2d.setIndex(rs.getLong("autoindex"));
            sp2d.setSp2dNumber(rs.getString("sp2dnumber"));
            sp2d.setSp2dDate(rs.getDate("sp2ddate"));
            sp2d.setReceiver(rs.getString("receiver"));
            sp2d.setPurpose(rs.getString("purpose"));
            sp2d.setAmount(rs.getBigDecimal("amount"));
            sp2d.setDescription(rs.getString("description"));

            sp2d.setSkpd(skpd);

            sp2ds.add(sp2d);
        }

        rs.close();
        pstm.close();

        return sp2ds;
    }

    ArrayList<SP2D> getSP2D(Connection conn, Integer month, Integer year,String modifier) throws SQLException {
        ArrayList<SP2D> sp2ds = new ArrayList<SP2D>();

        StringBuilder query = new StringBuilder();

        if (month == 0) {
            query.append("select sp.*,s.skpdcode,s.skpdname from sp2d sp ").
                    append("left join skpd s ").
                    append("on sp.skpdindex = s.autoindex ").
                    append("where date_part('year',sp2ddate) = ").
                    append(year).append(" ").
                    append(modifier).
                    append(" order by sp2ddate");
        } else {
            query.append("select sp.*,s.skpdcode,s.skpdname from sp2d sp ").
                    append("left join skpd s ").
                    append("on sp.skpdindex = s.autoindex ").
                    append("where date_part('month',sp2ddate) = ").
                    append(month).
                    append(" and date_part('year',sp2ddate) = ").
                    append(year).append(" ").
                    append(modifier).
                    append(" order by sp2ddate");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            Long skpdIndex = rs.getLong("skpdindex");
            SKPD skpd = null;

            if (skpdIndex != null) {
                if (!skpdIndex.equals(Long.valueOf(0))) {
                    skpd = new SKPD(skpdIndex);
                    skpd.setCode(rs.getString("skpdcode"));
                    skpd.setName(rs.getString("skpdname"));
                }
            }

            SP2D sp2d = new SP2D();
            sp2d.setIndex(rs.getLong("autoindex"));
            sp2d.setSp2dNumber(rs.getString("sp2dnumber"));
            sp2d.setSp2dDate(rs.getDate("sp2ddate"));
            sp2d.setReceiver(rs.getString("receiver"));
            sp2d.setPurpose(rs.getString("purpose"));
            sp2d.setAmount(rs.getBigDecimal("amount"));
            sp2d.setDescription(rs.getString("description"));

            sp2d.setSkpd(skpd);

            sp2ds.add(sp2d);
        }

        rs.close();
        pstm.close();

        return sp2ds;
    }

    ArrayList<SP2D> getSP2D(Connection conn, Date date, Date date2,String modifier) throws SQLException {
        ArrayList<SP2D> sp2ds = new ArrayList<SP2D>();

        StringBuilder query = new StringBuilder();
        query.append("select sp.*,s.skpdcode,s.skpdname from sp2d sp ").
                append("left join skpd s ").
                append("on sp.skpdindex = s.autoindex ").
                append("where sp2ddate between ").
                append("'").append(new java.sql.Date(date.getTime())).append("' ").
                append(" and ").
                append("'").append(new java.sql.Date(date2.getTime())).append("' ").
                append(modifier).
                append(" order by sp2ddate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            Long skpdIndex = rs.getLong("skpdindex");
            SKPD skpd = null;

            if (skpdIndex != null) {
                if (!skpdIndex.equals(Long.valueOf(0))) {
                    skpd = new SKPD(skpdIndex);
                    skpd.setCode(rs.getString("skpdcode"));
                    skpd.setName(rs.getString("skpdname"));
                }
            }

            SP2D sp2d = new SP2D();
            sp2d.setIndex(rs.getLong("autoindex"));
            sp2d.setSp2dNumber(rs.getString("sp2dnumber"));
            sp2d.setSp2dDate(rs.getDate("sp2ddate"));
            sp2d.setReceiver(rs.getString("receiver"));
            sp2d.setPurpose(rs.getString("purpose"));
            sp2d.setAmount(rs.getBigDecimal("amount"));
            sp2d.setDescription(rs.getString("description"));

            sp2d.setSkpd(skpd);

            sp2ds.add(sp2d);
        }

        rs.close();
        pstm.close();

        return sp2ds;
    }

    SP2D getSP2DByIndex(Connection conn, Long index) throws SQLException {
        SP2D sp2d = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from sp2d ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            Long skpdIndex = rs.getLong("skpdindex");
            SKPD skpd = null;

            if (skpdIndex != null) {
                if (!skpdIndex.equals(Long.valueOf(0))) {
                    skpd = new SKPD(skpdIndex);
                }
            }

            sp2d = new SP2D();
            sp2d.setIndex(rs.getLong("autoindex"));
            sp2d.setSp2dNumber(rs.getString("sp2dnumber"));
            sp2d.setSp2dDate(rs.getDate("sp2ddate"));
            sp2d.setReceiver(rs.getString("reciever"));
            sp2d.setPurpose(rs.getString("purpose"));
            sp2d.setAmount(rs.getBigDecimal("amount"));
            sp2d.setDescription(rs.getString("description"));

            sp2d.setSkpd(skpd);

        }

        rs.close();
        pstm.close();

        return sp2d;
    }

    void insertSP2DFile(Connection conn, SP2DFile file) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into sp2dfile(sp2dindex,filename,filebyte)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, file.getSp2dIndex());
        pstm.setString(++col, file.getFileName());
        pstm.setBytes(++col, file.getFileStream());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteSP2DFile(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("delete from sp2dfile ").
                append("where sp2dindex = ? ");
        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<SP2DFile> getSP2DFile(Connection conn, Long index) throws SQLException {
        ArrayList<SP2DFile> sP2DFiles = new ArrayList<SP2DFile>();

        StringBuilder query = new StringBuilder();
        query.append("select * from sp2dfile ").
                append("where sp2dindex = ? ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            SP2DFile file = new SP2DFile();

            file.setSp2dIndex(rs.getLong("sp2dindex"));
            file.setFileName(rs.getString("filename"));
            file.setFileStream(rs.getBytes("filebyte"));

            sP2DFiles.add(file);
        }

        rs.close();
        pstm.close();

        return sP2DFiles;
    }
}
