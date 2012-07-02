package org.motekar.project.civics.archieve.mail.sqlapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.mail.objects.DoctorMail;
import org.motekar.project.civics.archieve.mail.objects.DocumentFile;
import org.motekar.project.civics.archieve.mail.objects.Inbox;
import org.motekar.project.civics.archieve.mail.objects.InboxDisposition;
import org.motekar.project.civics.archieve.mail.objects.InboxFile;
import org.motekar.project.civics.archieve.mail.objects.Outbox;
import org.motekar.project.civics.archieve.mail.objects.OutboxDisposition;
import org.motekar.project.civics.archieve.mail.objects.OutboxFile;
import org.motekar.project.civics.archieve.mail.objects.SicknessMail;
import org.motekar.project.civics.archieve.master.objects.Division;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.sqlapi.CommonSQL;

/**
 *
 * @author Muhamad Wibawa
 */
public class MailSQL extends CommonSQL {

    void insertInbox(Connection conn, Inbox inbox) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into inbox(maildate,mailnumber,subject,").
                append("sender,senderaddress,dispositiondate,receiver,unit)").
                append("values(?,?,?,?,?,?,?,?)");

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
        
        if (inbox.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, inbox.getUnit().getIndex());
        }


        pstm.executeUpdate();
        pstm.close();
    }

    void updateInbox(Connection conn, Long oldIndex, Inbox inbox) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update inbox set maildate = ?, mailnumber = ?, subject = ?, ").
                append("sender = ?, senderaddress = ?,dispositiondate = ?, receiver = ?, unit = ? ").
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
        
        if (inbox.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, inbox.getUnit().getIndex());
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

    ArrayList<Inbox> getInbox(Connection conn,String modifier) throws SQLException {
        ArrayList<Inbox> inboxs = new ArrayList<Inbox>();

        StringBuilder query = new StringBuilder();
        query.append("select i.*,d.code divisioncode,d.divisionname divisionname from inbox i ").
                append("left join division d ").
                append("on i.receiver = d.code ").
                append(modifier).
                append(" order by maildate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
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
            inbox.setUnit(unit);

            inbox.setStyled(true);

            inboxs.add(inbox);
        }

        rs.close();
        pstm.close();

        return inboxs;
    }

    ArrayList<Inbox> getInbox(Connection conn, Integer month, Integer year,String modifier) throws SQLException {
        ArrayList<Inbox> inboxs = new ArrayList<Inbox>();

        StringBuilder query = new StringBuilder();

        if (month == 0) {
            query.append("select i.*,d.code divisioncode,d.divisionname from inbox i ").
                    append("left join division d ").
                    append("on i.receiver = d.code ").
                    append("where date_part('year',maildate) = ").
                    append(year).append(modifier).
                    append(" order by maildate");
        } else {
            query.append("select i.*,d.code divisioncode,d.divisionname from inbox i ").
                    append("left join division d ").
                    append("on i.receiver = d.code ").
                    append("where date_part('month',maildate) = ").
                    append(month).
                    append(" and date_part('year',maildate) = ").
                    append(year).append(modifier).
                    append(" order by maildate");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
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
            
            inbox.setUnit(unit);
            
            inbox.setStyled(true);

            inboxs.add(inbox);
        }

        rs.close();
        pstm.close();

        return inboxs;
    }

    ArrayList<Inbox> getInbox(Connection conn, Date date, Date date2,String modifier) throws SQLException {
        ArrayList<Inbox> inboxs = new ArrayList<Inbox>();

        StringBuilder query = new StringBuilder();

        query.append("select i.*,d.code divisioncode,d.divisionname from inbox i ").
                append("left join division d ").
                append("on i.receiver = d.code ").
                append("where maildate between ").
                append("'").append(new java.sql.Date(date.getTime())).append("' ").
                append(" and ").
                append("'").append(new java.sql.Date(date2.getTime())).append("' ").
                append(modifier).
                append(" order by maildate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
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
            inbox.setUnit(unit);

            inbox.setStyled(true);

            inboxs.add(inbox);
        }

        rs.close();
        pstm.close();

        return inboxs;
    }

    void insertInboxDisposition(Connection conn, Inbox inbox,InboxDisposition disposition) throws SQLException {
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

    void updateInboxDisposition(Connection conn, InboxDisposition oldDispo,InboxDisposition newDispo) throws SQLException {
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

    ArrayList<InboxDisposition> getInboxDisposition(Connection conn,Long inboxIndex) throws SQLException {
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
        query.append("insert into outbox(maildate,mailnumber,subject,unit)").
                append("values(?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setDate(++col, new java.sql.Date(outbox.getMailDate().getTime()));
        pstm.setString(++col, outbox.getMailNumber());
        pstm.setString(++col, outbox.getSubject());
        
        if (outbox.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, outbox.getUnit().getIndex());
        }
        
        pstm.executeUpdate();
        pstm.close();
    }

    void updateOutbox(Connection conn, Long oldIndex, Outbox outbox) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update outbox set maildate = ?, mailnumber = ?, subject = ?, unit = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setDate(++col, new java.sql.Date(outbox.getMailDate().getTime()));
        pstm.setString(++col, outbox.getMailNumber());
        pstm.setString(++col, outbox.getSubject());
        
        if (outbox.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, outbox.getUnit().getIndex());
        }
        
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

    ArrayList<Outbox> getOutbox(Connection conn, String modifier) throws SQLException {
        ArrayList<Outbox> outboxs = new ArrayList<Outbox>();

        StringBuilder query = new StringBuilder();
        query.append("select * from outbox ").append(modifier).
                append(" order by maildate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            Outbox outbox = new Outbox();
            outbox.setIndex(rs.getLong("autoindex"));
            outbox.setMailDate(rs.getDate("maildate"));
            outbox.setMailNumber(rs.getString("mailnumber"));
            outbox.setSubject(rs.getString("subject"));
            
            outbox.setUnit(unit);
            
            outbox.setStyled(true);

            outboxs.add(outbox);
        }

        rs.close();
        pstm.close();

        return outboxs;
    }

    ArrayList<Outbox> getOutbox(Connection conn, Integer month, Integer year, String modifier) throws SQLException {
        ArrayList<Outbox> outboxs = new ArrayList<Outbox>();

        StringBuilder query = new StringBuilder();

        if (month == 0) {
            query.append("select * from outbox ").
                    append("where date_part('year',maildate) = ").
                    append(year).append(modifier).
                    append(" order by maildate");
        } else {
            query.append("select * from outbox ").
                    append("where date_part('month',maildate) = ").
                    append(month).
                    append(" and date_part('year',maildate) = ").
                    append(year).append(modifier).
                    append(" order by maildate");
        }



        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            Outbox outbox = new Outbox();
            outbox.setIndex(rs.getLong("autoindex"));
            outbox.setMailDate(rs.getDate("maildate"));
            outbox.setMailNumber(rs.getString("mailnumber"));
            outbox.setSubject(rs.getString("subject"));
            
            outbox.setUnit(unit);

            outbox.setStyled(true);

            outboxs.add(outbox);
        }

        rs.close();
        pstm.close();

        return outboxs;
    }

    ArrayList<Outbox> getOutbox(Connection conn, Date date, Date date2, String modifier) throws SQLException {
        ArrayList<Outbox> outboxs = new ArrayList<Outbox>();

        StringBuilder query = new StringBuilder();
        query.append("select * from outbox ").
                append("where maildate between ").
                append("'").append(new java.sql.Date(date.getTime())).append("' ").
                append(" and ").
                append("'").append(new java.sql.Date(date2.getTime())).append("' ").
                append(modifier).
                append(" order by maildate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");
            
            Unit unit = null;
            
            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            Outbox outbox = new Outbox();
            outbox.setIndex(rs.getLong("autoindex"));
            outbox.setMailDate(rs.getDate("maildate"));
            outbox.setMailNumber(rs.getString("mailnumber"));
            outbox.setSubject(rs.getString("subject"));
            
            outbox.setUnit(unit);

            outbox.setStyled(true);

            outboxs.add(outbox);
        }

        rs.close();
        pstm.close();

        return outboxs;
    }

    void insertOutboxDisposition(Connection conn, Outbox outbox,OutboxDisposition disposition) throws SQLException {
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

    void updateOutboxDisposition(Connection conn, OutboxDisposition oldDispo,OutboxDisposition newDispo) throws SQLException {
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

    ArrayList<OutboxDisposition> getOutboxDisposition(Connection conn,Long outboxIndex) throws SQLException {
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
    
    void insertDocumentFile(Connection conn, DocumentFile file) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into documents(filename,filebyte,description)").
                append("values(?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, file.getFileName());
        pstm.setBytes(++col, file.getFileStream());
        pstm.setString(++col, file.getDescription());

        pstm.executeUpdate();
        pstm.close();
    }
    
    void updateDocumentFile(Connection conn, DocumentFile oldFile,DocumentFile newFile) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("update documents set filename = ?,filebyte = ?, description = ? ").
                append("where autoindex = ?");
        
        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, newFile.getFileName());
        pstm.setBytes(++col, newFile.getFileStream());
        pstm.setString(++col, newFile.getDescription());
        pstm.setLong(++col, oldFile.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteDocumentFile(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("delete from documents ").
                append("where autoindex = ? ");
        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<DocumentFile> getDocumentFile(Connection conn) throws SQLException {
        ArrayList<DocumentFile> documentFiles = new ArrayList<DocumentFile>();

        StringBuilder query = new StringBuilder();
        query.append("select * from documents ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            DocumentFile file = new DocumentFile();

            file.setIndex(rs.getLong("autoindex"));
            file.setFileName(rs.getString("filename"));
            file.setFileStream(rs.getBytes("filebyte"));
            file.setDescription(rs.getString("description"));
            file.setStyled(true);

            documentFiles.add(file);
        }

        rs.close();
        pstm.close();

        return documentFiles;
    }
    
    //
    
    void insertSicknessMail(Connection conn, SicknessMail mail) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into sicknessmail(documentnumber, patiencename, patienceage, jobs, address,").
                append("startdate, enddate, approvalplace, approvaldate, approval)").
                append("values(?, ?, ?, ?, ?, ?,?, ?, ?, ?)");

        
        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, mail.getDocumentNumber());
        pstm.setString(++col, mail.getPatienceName());
        pstm.setInt(++col, mail.getPatienceAge());
        pstm.setString(++col, mail.getJobs());
        pstm.setString(++col, mail.getAddress());

        if (mail.getStartDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getStartDate().getTime()));
        }
        
        if (mail.getEndDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getEndDate().getTime()));
        }

        pstm.setString(++col, mail.getApprovalPlace());
        
        if (mail.getApprovalDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getApprovalDate().getTime()));
        }
        
        if (mail.getApproval() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, mail.getApproval().getIndex());
        }


        pstm.executeUpdate();
        pstm.close();
    }

    void updateSicknessMail(Connection conn, Long oldIndex, SicknessMail mail) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update sicknessmail set documentnumber=?, patiencename=?, patienceage=?, ").
                append("jobs=?, address=?, startdate=?, enddate=?, approvalplace=?, approvaldate=?, ").
                append("approval=? ").
                append("where autoindex = ?");
        
        
        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, mail.getDocumentNumber());
        pstm.setString(++col, mail.getPatienceName());
        pstm.setInt(++col, mail.getPatienceAge());
        pstm.setString(++col, mail.getJobs());
        pstm.setString(++col, mail.getAddress());

        if (mail.getStartDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getStartDate().getTime()));
        }
        
        if (mail.getEndDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getEndDate().getTime()));
        }

        pstm.setString(++col, mail.getApprovalPlace());
        
        if (mail.getApprovalDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getApprovalDate().getTime()));
        }
        
        if (mail.getApproval() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, mail.getApproval().getIndex());
        }
        
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteSicknessMail(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from mail sicknessmail autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<SicknessMail> getSicknessMail(Connection conn,String modifier) throws SQLException {
        ArrayList<SicknessMail> mails = new ArrayList<SicknessMail>();

        StringBuilder query = new StringBuilder();
        query.append("select * from sicknessmail ").
                append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long employeeIndex = rs.getLong("approval");
            
            Employee approval = null;
            
            if (employeeIndex != null && !employeeIndex.equals(Long.valueOf(0))) {
                approval = new Employee(employeeIndex);
            }
            
            SicknessMail mail = new SicknessMail();
            mail.setIndex(rs.getLong("autoindex"));
            mail.setDocumentNumber(rs.getString("documentnumber"));
            mail.setPatienceName(rs.getString("patiencename"));
            mail.setPatienceAge(rs.getInt("patienceage"));
            mail.setJobs(rs.getString("jobs"));
            mail.setAddress(rs.getString("address"));
            mail.setStartDate(rs.getDate("startdate"));
            mail.setEndDate(rs.getDate("enddate"));
            mail.setApprovalPlace(rs.getString("approvalplace"));
            mail.setApprovalDate(rs.getDate("approvaldate"));
            
            mail.setApproval(approval);

            mail.setStyled(true);

            mails.add(mail);
        }

        rs.close();
        pstm.close();

        return mails;
    }

    ArrayList<SicknessMail> getSicknessMail(Connection conn, Integer month, Integer year,String modifier) throws SQLException {
        ArrayList<SicknessMail> mails = new ArrayList<SicknessMail>();

        StringBuilder query = new StringBuilder();

        if (month == 0) {
            query.append("select * from sicknessmail ").
                    append("where date_part('year',startdate) = ").
                    append(year).append(modifier).
                    append(" order by startdate");
        } else {
            query.append("select * from sicknessmail ").
                    append("where date_part('month',startdate) = ").
                    append(month).
                    append(" and date_part('year',startdate) = ").
                    append(year).append(modifier).
                    append(" order by startdate");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long employeeIndex = rs.getLong("approval");
            
            Employee approval = null;
            
            if (employeeIndex != null && !employeeIndex.equals(Long.valueOf(0))) {
                approval = new Employee(employeeIndex);
            }
            
            SicknessMail mail = new SicknessMail();
            mail.setIndex(rs.getLong("autoindex"));
            mail.setDocumentNumber(rs.getString("documentnumber"));
            mail.setPatienceName(rs.getString("patiencename"));
            mail.setPatienceAge(rs.getInt("patienceage"));
            mail.setJobs(rs.getString("jobs"));
            mail.setAddress(rs.getString("address"));
            mail.setStartDate(rs.getDate("startdate"));
            mail.setEndDate(rs.getDate("enddate"));
            mail.setApprovalPlace(rs.getString("approvalplace"));
            mail.setApprovalDate(rs.getDate("approvaldate"));
            
            mail.setApproval(approval);

            mail.setStyled(true);

            mails.add(mail);
        }

        rs.close();
        pstm.close();

        return mails;
    }

    ArrayList<SicknessMail> getSicknessMail(Connection conn, Date date, Date date2,String modifier) throws SQLException {
        ArrayList<SicknessMail> mails = new ArrayList<SicknessMail>();

        StringBuilder query = new StringBuilder();

        query.append("select * from sicknessmail ").
                append("where startdate between ").
                append("'").append(new java.sql.Date(date.getTime())).append("' ").
                append(" and ").
                append("'").append(new java.sql.Date(date2.getTime())).append("' ").
                append(modifier).
                append(" order by startdate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long employeeIndex = rs.getLong("approval");
            
            Employee approval = null;
            
            if (employeeIndex != null && !employeeIndex.equals(Long.valueOf(0))) {
                approval = new Employee(employeeIndex);
            }
            
            SicknessMail mail = new SicknessMail();
            mail.setIndex(rs.getLong("autoindex"));
            mail.setDocumentNumber(rs.getString("documentnumber"));
            mail.setPatienceName(rs.getString("patiencename"));
            mail.setPatienceAge(rs.getInt("patienceage"));
            mail.setJobs(rs.getString("jobs"));
            mail.setAddress(rs.getString("address"));
            mail.setStartDate(rs.getDate("startdate"));
            mail.setEndDate(rs.getDate("enddate"));
            mail.setApprovalPlace(rs.getString("approvalplace"));
            mail.setApprovalDate(rs.getDate("approvaldate"));
            
            mail.setApproval(approval);

            mail.setStyled(true);

            mails.add(mail);
        }

        rs.close();
        pstm.close();

        return mails;
    }
    
    //
    
    void insertDoctorMail(Connection conn, DoctorMail mail) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into doctormail(documentnumber, patiencename,birthplace, birthdate, jobs, address,").
                append("requested, maildate, mailnumber, checked, term, height, weight,").
                append("bloodpreasure,bloodpreasure2, expireddate, approvalplace, approvaldate, approval)").
                append("values(?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?,?)");

        
        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, mail.getDocumentNumber());
        pstm.setString(++col, mail.getPatienceName());
        pstm.setString(++col, mail.getBirthPlace());
        if (mail.getBirthDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getBirthDate().getTime()));
        }
        pstm.setString(++col, mail.getJobs());
        pstm.setString(++col, mail.getAddress());
        pstm.setString(++col, mail.getRequested());

        if (mail.getMailDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getMailDate().getTime()));
        }
        
        pstm.setString(++col, mail.getMailNumber());
        pstm.setString(++col, mail.getChecked());
        pstm.setString(++col, mail.getTerm());
        pstm.setInt(++col, mail.getHeight());
        pstm.setInt(++col, mail.getWeight());
        pstm.setInt(++col, mail.getBloodPreasure());
        pstm.setInt(++col, mail.getBloodPreasure2());

        if (mail.getExpiredDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getExpiredDate().getTime()));
        }
        pstm.setString(++col, mail.getApprovalPlace());
        
        if (mail.getApprovalDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getApprovalDate().getTime()));
        }
        
        if (mail.getApproval() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, mail.getApproval().getIndex());
        }


        pstm.executeUpdate();
        pstm.close();
    }

    void updateDoctorMail(Connection conn, Long oldIndex, DoctorMail mail) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update doctormail set documentnumber=?, patiencename=?,birthplace = ?, birthdate=?, jobs=?, ").
                append("address=?, requested=?, maildate=?, mailnumber=?, checked=?, ").
                append("term=?, height=?, weight=?, bloodpreasure=?, bloodpreasure2=?,expireddate=?, approvalplace=?, ").
                append("approvaldate=?, approval=? ").
                append("where autoindex = ?");
        
        
        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, mail.getDocumentNumber());
        pstm.setString(++col, mail.getPatienceName());
        pstm.setString(++col, mail.getBirthPlace());
        if (mail.getBirthDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getBirthDate().getTime()));
        }
        pstm.setString(++col, mail.getJobs());
        pstm.setString(++col, mail.getAddress());
        pstm.setString(++col, mail.getRequested());

        if (mail.getMailDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getMailDate().getTime()));
        }
        
        pstm.setString(++col, mail.getMailNumber());
        pstm.setString(++col, mail.getChecked());
        pstm.setString(++col, mail.getTerm());
        pstm.setInt(++col, mail.getHeight());
        pstm.setInt(++col, mail.getWeight());
        pstm.setInt(++col, mail.getBloodPreasure());
        pstm.setInt(++col, mail.getBloodPreasure2());

        if (mail.getExpiredDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getExpiredDate().getTime()));
        }
        pstm.setString(++col, mail.getApprovalPlace());
        
        if (mail.getApprovalDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(mail.getApprovalDate().getTime()));
        }
        
        if (mail.getApproval() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, mail.getApproval().getIndex());
        }
        
        pstm.setLong(++col, oldIndex);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteDoctorMail(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from mail doctormail autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<DoctorMail> getDoctorMail(Connection conn,String modifier) throws SQLException {
        ArrayList<DoctorMail> mails = new ArrayList<DoctorMail>();

        StringBuilder query = new StringBuilder();
        query.append("select * from doctormail ").
                append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long employeeIndex = rs.getLong("approval");
            
            Employee approval = null;
            
            if (employeeIndex != null && !employeeIndex.equals(Long.valueOf(0))) {
                approval = new Employee(employeeIndex);
            }
            
            DoctorMail mail = new DoctorMail();
            mail.setIndex(rs.getLong("autoindex"));
            mail.setDocumentNumber(rs.getString("documentnumber"));
            mail.setPatienceName(rs.getString("patiencename"));
            mail.setBirthPlace(rs.getString("birthplace"));
            mail.setBirthDate(rs.getDate("birthdate"));
            mail.setJobs(rs.getString("jobs"));
            mail.setAddress(rs.getString("address"));
            mail.setRequested(rs.getString("requested"));
            mail.setMailDate(rs.getDate("maildate"));
            mail.setMailNumber(rs.getString("mailnumber"));
            mail.setChecked(rs.getString("checked"));
            mail.setTerm(rs.getString("term"));
            mail.setHeight(rs.getInt("height"));
            mail.setWeight(rs.getInt("weight"));
            mail.setBloodPreasure(rs.getInt("bloodpreasure"));
            mail.setBloodPreasure2(rs.getInt("bloodpreasure2"));
            mail.setExpiredDate(rs.getDate("expireddate"));
            mail.setApprovalPlace(rs.getString("approvalplace"));
            mail.setApprovalDate(rs.getDate("approvaldate"));
            
            mail.setApproval(approval);

            mail.setStyled(true);

            mails.add(mail);
        }

        rs.close();
        pstm.close();

        return mails;
    }

    ArrayList<DoctorMail> getDoctorMail(Connection conn, Integer month, Integer year,String modifier) throws SQLException {
        ArrayList<DoctorMail> mails = new ArrayList<DoctorMail>();

        StringBuilder query = new StringBuilder();

        if (month == 0) {
            query.append("select * from doctormail ").
                    append("where date_part('year',maildate) = ").
                    append(year).append(modifier).
                    append(" order by maildate");
        } else {
            query.append("select * from doctormail ").
                    append("where date_part('month',maildate) = ").
                    append(month).
                    append(" and date_part('year',maildate) = ").
                    append(year).append(modifier).
                    append(" order by maildate");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long employeeIndex = rs.getLong("approval");
            
            Employee approval = null;
            
            if (employeeIndex != null && !employeeIndex.equals(Long.valueOf(0))) {
                approval = new Employee(employeeIndex);
            }
            
            DoctorMail mail = new DoctorMail();
            mail.setIndex(rs.getLong("autoindex"));
            mail.setDocumentNumber(rs.getString("documentnumber"));
            mail.setPatienceName(rs.getString("patiencename"));
            mail.setBirthPlace(rs.getString("birthplace"));
            mail.setBirthDate(rs.getDate("birthdate"));
            mail.setJobs(rs.getString("jobs"));
            mail.setAddress(rs.getString("address"));
            mail.setRequested(rs.getString("requested"));
            mail.setMailDate(rs.getDate("maildate"));
            mail.setMailNumber(rs.getString("mailnumber"));
            mail.setChecked(rs.getString("checked"));
            mail.setTerm(rs.getString("term"));
            mail.setHeight(rs.getInt("height"));
            mail.setWeight(rs.getInt("weight"));
            mail.setBloodPreasure(rs.getInt("bloodpreasure"));
            mail.setBloodPreasure2(rs.getInt("bloodpreasure2"));
            mail.setExpiredDate(rs.getDate("expireddate"));
            mail.setApprovalPlace(rs.getString("approvalplace"));
            mail.setApprovalDate(rs.getDate("approvaldate"));
            
            mail.setApproval(approval);

            mail.setStyled(true);

            mails.add(mail);
        }

        rs.close();
        pstm.close();

        return mails;
    }

    ArrayList<DoctorMail> getDoctorMail(Connection conn, Date date, Date date2,String modifier) throws SQLException {
        ArrayList<DoctorMail> mails = new ArrayList<DoctorMail>();

        StringBuilder query = new StringBuilder();

        query.append("select * from doctormail ").
                append("where maildate between ").
                append("'").append(new java.sql.Date(date.getTime())).append("' ").
                append(" and ").
                append("'").append(new java.sql.Date(date2.getTime())).append("' ").
                append(modifier).
                append(" order by maildate");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long employeeIndex = rs.getLong("approval");
            
            Employee approval = null;
            
            if (employeeIndex != null && !employeeIndex.equals(Long.valueOf(0))) {
                approval = new Employee(employeeIndex);
            }
            
            DoctorMail mail = new DoctorMail();
            mail.setIndex(rs.getLong("autoindex"));
            mail.setDocumentNumber(rs.getString("documentnumber"));
            mail.setPatienceName(rs.getString("patiencename"));
            mail.setBirthPlace(rs.getString("birthplace"));
            mail.setBirthDate(rs.getDate("birthdate"));
            mail.setJobs(rs.getString("jobs"));
            mail.setAddress(rs.getString("address"));
            mail.setRequested(rs.getString("requested"));
            mail.setMailDate(rs.getDate("maildate"));
            mail.setMailNumber(rs.getString("mailnumber"));
            mail.setChecked(rs.getString("checked"));
            mail.setTerm(rs.getString("term"));
            mail.setHeight(rs.getInt("height"));
            mail.setWeight(rs.getInt("weight"));
            mail.setBloodPreasure(rs.getInt("bloodpreasure"));
            mail.setBloodPreasure2(rs.getInt("bloodpreasure2"));
            mail.setExpiredDate(rs.getDate("expireddate"));
            mail.setApprovalPlace(rs.getString("approvalplace"));
            mail.setApprovalDate(rs.getDate("approvaldate"));
            
            mail.setApproval(approval);

            mail.setStyled(true);

            mails.add(mail);
        }

        rs.close();
        pstm.close();

        return mails;
    }
}
