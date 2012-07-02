package org.motekar.project.civics.archieve.mail.sqlapi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import org.motekar.project.civics.archieve.mail.objects.*;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.sqlapi.AuthBusinessLogic;

/**
 *
 * @author Muhamad Wibawa
 */
public class MailBusinessLogic {

    private Connection conn;
    private MailSQL sql = new MailSQL();
    private AuthBusinessLogic auth;

    public MailBusinessLogic(Connection conn) {
        this.conn = conn;
        auth = new AuthBusinessLogic(conn);
    }

    public Inbox insertInbox(Long session,Inbox inbox) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.insertInbox(conn, inbox);


            Long index = sql.getMaxIndex(conn, "inbox", "autoindex");
            inbox.setIndex(index);
            inbox.setStyled(true);

            ArrayList<InboxFile> files = inbox.getInboxFiles();
            
            if (!files.isEmpty()) {
                for (InboxFile file : files) {
                    file.setInboxIndex(index);
                    sql.insertInboxFile(conn, file);
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
        return inbox;
    }

    public Inbox updateInbox(Long session,Inbox oldInbox, Inbox newInbox) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateInbox(conn,oldInbox.getIndex(), newInbox);
            newInbox.setIndex(oldInbox.getIndex());
            newInbox.setStyled(true);

            sql.deleteInboxFile(conn, oldInbox.getIndex());

            ArrayList<InboxFile> files = newInbox.getInboxFiles();

            if (!files.isEmpty()) {
                for (InboxFile file : files) {
                    file.setInboxIndex(newInbox.getIndex());
                    sql.insertInboxFile(conn, file);
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
        return newInbox;
    }


    public void deleteInbox(Long session,Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteInbox(conn, index);

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<Inbox> getInbox(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getInbox(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<Inbox> getInbox(Long session,Integer month, Integer year) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            return sql.getInbox(conn, month, year);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<Inbox> getInbox(Long session,Date date, Date date2) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            return sql.getInbox(conn, date, date2);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public Inbox getCompleteInbox(Long session, Inbox inbox) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            ArrayList<InboxFile> inboxFiles = sql.getInboxFile(conn, inbox.getIndex());

            inbox.setInboxFiles(inboxFiles);

        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
        return inbox;
    }

    public void insertInboxDisposition(Long session,Inbox inbox,InboxDisposition disposition) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.insertInboxDisposition(conn, inbox, disposition);

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
    }

    public void updateInboxDisposition(Long session,InboxDisposition oldDisposition,InboxDisposition newDisposition) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateInboxDisposition(conn, oldDisposition, newDisposition);
            
            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
    }

    public void deleteInboxDisposition(Long session,InboxDisposition disposition) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteInboxDisposition(conn, disposition.getIndex());

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<InboxDisposition> getInboxDisposition(Long session,Long index) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getInboxDisposition(conn, index);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public Outbox insertOutbox(Long session,Outbox outbox) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.insertOutbox(conn, outbox);
            Long index = sql.getMaxIndex(conn, "outbox", "autoindex");
            outbox.setIndex(index);
            outbox.setStyled(true);

            ArrayList<OutboxFile> files = outbox.getOutboxFiles();

            if (!files.isEmpty()) {
                for (OutboxFile file : files) {
                    file.setOutboxIndex(index);
                    sql.insertOutboxFile(conn, file);
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
        return outbox;
    }

    public Outbox updateOutbox(Long session,Outbox oldOutbox, Outbox newOutbox) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateOutbox(conn,oldOutbox.getIndex(), newOutbox);
            newOutbox.setIndex(oldOutbox.getIndex());
            newOutbox.setStyled(true);
            
            sql.deleteOutboxFile(conn, oldOutbox.getIndex());

            ArrayList<OutboxFile> files = newOutbox.getOutboxFiles();

            if (!files.isEmpty()) {
                for (OutboxFile file : files) {
                    file.setOutboxIndex(oldOutbox.getIndex());
                    sql.insertOutboxFile(conn, file);
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
        return newOutbox;
    }


    public void deleteOutbox(Long session,Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteOutbox(conn, index);

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<Outbox> getOutbox(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getOutbox(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<Outbox> getOutbox(Long session,Integer month, Integer year) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getOutbox(conn, month, year);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<Outbox> getOutbox(Long session,Date date, Date date2) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getOutbox(conn, date, date2);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public Outbox getCompleteOutbox(Long session, Outbox outbox) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            ArrayList<OutboxFile> outboxFiles = sql.getOutboxFile(conn, outbox.getIndex());

            outbox.setOutboxFiles(outboxFiles);

        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
        return outbox;
    }


    public void insertOutboxDisposition(Long session,Outbox outbox,OutboxDisposition disposition) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.insertOutboxDisposition(conn, outbox, disposition);

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
    }

    public void updateOutboxDisposition(Long session,OutboxDisposition oldDisposition,OutboxDisposition newDisposition) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateOutboxDisposition(conn, oldDisposition, newDisposition);

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
    }

    public void deleteOutboxDisposition(Long session,OutboxDisposition disposition) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteOutboxDisposition(conn, disposition.getIndex());

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<OutboxDisposition> getOutboxDisposition(Long session,Long index) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getOutboxDisposition(conn, index);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public Integer getIterationMailNumber(Long session,String originalCode) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            String maxCode = sql.getMaxCode(conn, "inbox", "mailnumber", originalCode);

            Integer number = Integer.valueOf(0);

            if (!maxCode.equals("")) {
                StringTokenizer tokens = new StringTokenizer(maxCode,"-");
                String it = "";
                while (tokens.hasMoreElements()) {
                    it = tokens.nextToken();
                }

                number = Integer.valueOf(it);
            } 

            return number;
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    //
    
    public ContractMail insertContractMail(Long session,ContractMail contractmail) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.insertContractMail(conn, contractmail);


            Long index = sql.getMaxIndex(conn, "contractmail", "autoindex");
            contractmail.setIndex(index);
            contractmail.setStyled(true);


            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
        return contractmail;
    }

    public ContractMail updateContractMail(Long session,ContractMail oldContractMail, ContractMail newContractMail) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateContractMail(conn,oldContractMail.getIndex(), newContractMail);
            newContractMail.setIndex(oldContractMail.getIndex());
            newContractMail.setStyled(true);


            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
        return newContractMail;
    }


    public void deleteContractMail(Long session,Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteContractMail(conn, index);

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<ContractMail> getContractMail(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getContractMail(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<ContractMail> getContractMail(Long session,Integer month, Integer year) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            return sql.getContractMail(conn, month, year);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<ContractMail> getContractMail(Long session,Date date, Date date2) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            return sql.getContractMail(conn, date, date2);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    //
    
    public SP2D insertSP2D(Long session, SP2D sp2d) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.insertSP2D(conn, sp2d);
            
            Long index = sql.getMaxIndex(conn, "sp2d", "autoindex");
            sp2d.setIndex(index);
            sp2d.setStyled(true);
            
            ArrayList<SP2DFile> files = sp2d.getsP2DFiles();
            
            if (!files.isEmpty()) {
                for (SP2DFile file : files) {
                    file.setSp2dIndex(index);
                    sql.insertSP2DFile(conn, file);
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
        return sp2d;
    }

    public SP2D updateSP2D(Long session, SP2D oldSP2D, SP2D newSP2D) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateSP2D(conn, oldSP2D, newSP2D);
            newSP2D.setStyled(true);
            newSP2D.setIndex(oldSP2D.getIndex());
            
            sql.deleteSP2DFile(conn, oldSP2D.getIndex());
            
            ArrayList<SP2DFile> files = newSP2D.getsP2DFiles();
            
            if (!files.isEmpty()) {
                for (SP2DFile file : files) {
                    file.setSp2dIndex(newSP2D.getIndex());
                    sql.insertSP2DFile(conn, file);
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
        return newSP2D;
    }

    public void deleteSP2D(Long session, SP2D sp2d) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteSP2D(conn, sp2d);

            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<SP2D> getSP2D(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getSP2D(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ArrayList<SP2D> getSP2D(Long session,Integer month, Integer year,String modifier) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getSP2D(conn, month, year,modifier);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ArrayList<SP2D> getSP2D(Long session,Date date, Date date2,String modifier) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getSP2D(conn, date, date2,modifier);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public SP2D getSP2DByIndex(Long session,SP2D sp2d) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getSP2DByIndex(conn,sp2d.getIndex());
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    
    public SP2D getCompleteSP2D(Long session, SP2D sp2d) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            ArrayList<SP2DFile> sP2DFiles = sql.getSP2DFile(conn, sp2d.getIndex());

            sp2d.setsP2DFiles(sP2DFiles);

        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
        return sp2d;
    }
}
