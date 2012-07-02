package org.motekar.project.civics.archieve.expedition.sqlapi;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.motekar.project.civics.archieve.expedition.objects.AssignmentLetter;
import org.motekar.project.civics.archieve.expedition.objects.Expedition;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionCheque;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionFollower;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionJournal;
import org.motekar.project.civics.archieve.expedition.objects.ExpeditionResult;
import org.motekar.project.civics.archieve.master.objects.Account;
import org.motekar.project.civics.archieve.master.objects.Activity;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.master.objects.Program;
import org.motekar.project.civics.archieve.master.objects.StandardPrice;
import org.motekar.project.civics.archieve.master.sqlapi.MasterSQL;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.sqlapi.AuthBusinessLogic;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ExpeditionBusinessLogic {

    private Connection conn;
    private ExpeditionSQL sql = new ExpeditionSQL();
    private MasterSQL mSql = new MasterSQL();
    private AuthBusinessLogic auth;

    public ExpeditionBusinessLogic(Connection conn) {
        this.conn = conn;
        auth = new AuthBusinessLogic(conn);
    }

    public Expedition insertExpedition(Long session, Expedition expedition) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.insertExpedition(conn, expedition);

            Long index = sql.getMaxIndex(conn, "expedition", "autoindex");

            if (index.longValue() != -1) {
                expedition.setIndex(index);
                if (!expedition.getFollower().isEmpty()) {
                    for (ExpeditionFollower ef : expedition.getFollower()) {
                        sql.insertExpeditionFollower(conn, index, ef);
                    }
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
        return expedition;
    }

    public Expedition updateExpedition(Long session, Expedition oldExp, Expedition newExp) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteExpeditionFollower(conn, oldExp.getIndex());

            sql.updateExpedition(conn, oldExp.getIndex(), newExp);
            newExp.setIndex(oldExp.getIndex());

            if (!newExp.getFollower().isEmpty()) {
                for (ExpeditionFollower ef : newExp.getFollower()) {
                    sql.insertExpeditionFollower(conn, newExp.getIndex(), ef);
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
        return newExp;
    }

    public void deleteExpedition(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteExpedition(conn, index);

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

    public ArrayList<Expedition> getExpedition(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getExpedition(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<Expedition> getExpedition(Long session, Date startDate, Date endDate, String modifier) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            return sql.getExpedition(conn, startDate, endDate,modifier);
        } catch (SQLException sqle) {
            Exceptions.printStackTrace(sqle);
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<Expedition> getExpedition(Long session, Integer month, Integer year, String modifier) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getExpedition(conn, month, year,modifier);
        } catch (SQLException sqle) {
            Exceptions.printStackTrace(sqle);
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public boolean getExpeditionRangeCheck(Long session, Date startdate,Date enddate, Long indexEmployee) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getExpeditionRangeCheck(conn, startdate, enddate, indexEmployee);
        } catch (SQLException sqle) {
            Exceptions.printStackTrace(sqle);
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public Expedition getCompleteExpedition(Long session, Expedition expedition) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            Employee assignedEmployee = mSql.getEmployeeByIndex(conn, expedition.getAssignedEmployee().getIndex());
            AssignmentLetter assignmentLetter = sql.getAssignmentLetterByIndex(conn, expedition.getLetter().getIndex());
            assignmentLetter = getCompleteAssignmentLetter(session, assignmentLetter);
            assignmentLetter.setStyled(false);

            Account account = null;

            if (expedition.getAccount() != null) {
                account = mSql.getAccountByIndex(conn, expedition.getAccount().getIndex());
            }
            
            Program program = null;

            if (expedition.getProgram() != null) {
                program = mSql.getProgramByIndex(conn, expedition.getProgram().getIndex());
            }

            Activity activity = null;

            if (expedition.getActivity() != null) {
                activity = mSql.getActivityByIndex(conn, expedition.getActivity().getIndex());
            }

            ArrayList<ExpeditionFollower> followers = sql.getExpeditionFollower(conn, expedition.getIndex());

            expedition.setAssignedEmployee(assignedEmployee);
            expedition.setLetter(assignmentLetter);
            expedition.setAccount(account);
            expedition.setProgram(program);
            expedition.setActivity(activity);

            expedition.setFollower(followers);

            expedition.setStyled(true);

        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            Exceptions.printStackTrace(anyOtherException);
            throw new RuntimeException(anyOtherException);
        }
        return expedition;
    }

    public ExpeditionJournal insertExpeditionJournal(Long session, ExpeditionJournal journal) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.insertExpeditionJournal(conn, journal);

            Long index = sql.getMaxIndex(conn, "expeditionjournal", "journalindex");

            if (index.longValue() != -1) {
                journal.setJournalIndex(index);
                if (!journal.getResult().isEmpty()) {
                    for (ExpeditionResult er : journal.getResult()) {
                        sql.insertExpeditionResult(conn, index, er);
                    }
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
        return journal;
    }

    public ExpeditionJournal updateExpeditionJournal(Long session, ExpeditionJournal oldJou, ExpeditionJournal newJou) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteExpeditionResult(conn, oldJou.getJournalIndex());

            sql.updateExpeditionJournal(conn, oldJou.getJournalIndex(), newJou);
            newJou.setJournalIndex(oldJou.getJournalIndex());

            if (!newJou.getResult().isEmpty()) {
                for (ExpeditionResult er : newJou.getResult()) {
                    sql.insertExpeditionResult(conn, newJou.getJournalIndex(), er);
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
        return newJou;
    }

    public void deleteExpeditionJournal(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteExpeditionJournal(conn, index);

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

    public ArrayList<ExpeditionJournal> getExpeditionJournal(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getExpeditionJournal(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<ExpeditionJournal> getExpeditionJournal(Long session, Integer month, Integer year) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getExpeditionJournal(conn, month, year);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ExpeditionJournal getCompleteExpeditionJournal(Long session, ExpeditionJournal journal) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            AssignmentLetter letter = sql.getAssignmentLetterByIndex(conn, journal.getLetter().getIndex());

            letter = getCompleteAssignmentLetter(session, letter);

            ArrayList<Expedition> expeditions = sql.getExpeditionByLetter(conn, letter.getIndex());

            ArrayList<ExpeditionResult> result = sql.getExpeditionResult(conn, journal.getJournalIndex());

            journal.setLetter(letter);
            journal.setResult(result);
            journal.setExpeditions(expeditions);

            journal.setStyled(true);

        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
        return journal;
    }

    public AssignmentLetter insertAssignmentLetter(Long session, AssignmentLetter letter) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.insertAssignmentLetter(conn, letter);

            Long index = sql.getMaxIndex(conn, "assignmentletter", "autoindex");

            if (index.longValue() != -1) {
                letter.setIndex(index);
                if (!letter.getAssignedEmployee().isEmpty()) {
                    for (Employee ae : letter.getAssignedEmployee()) {
                        sql.insertAssignedEmployee(conn, index, ae);
                    }
                }

                if (!letter.getCarbonCopy().isEmpty()) {
                    for (String cc : letter.getCarbonCopy()) {
                        sql.insertCarbonCopy(conn, index, cc);
                    }
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
        return letter;
    }

    public AssignmentLetter updateAssignmentLetter(Long session, AssignmentLetter oldLetter, AssignmentLetter newLetter) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteAssignedEmployee(conn, oldLetter.getIndex());
            sql.deleteCarbonCopy(conn, oldLetter.getIndex());

            sql.updateAssignmentLetter(conn, oldLetter.getIndex(), newLetter);
            newLetter.setIndex(oldLetter.getIndex());

            if (!newLetter.getAssignedEmployee().isEmpty()) {
                for (Employee ae : newLetter.getAssignedEmployee()) {
                    sql.insertAssignedEmployee(conn, newLetter.getIndex(), ae);
                }
            }

            if (!newLetter.getCarbonCopy().isEmpty()) {
                for (String cc : newLetter.getCarbonCopy()) {
                    sql.insertCarbonCopy(conn, newLetter.getIndex(), cc);
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
        return newLetter;
    }

    public void deleteAssignmentLetter(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteAssignmentLetter(conn, index);

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

    public ArrayList<AssignmentLetter> getAssignmentLetter(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getAssigmentLetter(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<AssignmentLetter> getAssignmentLetterInExpedition(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getAssigmentLetterInExpedition(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<AssignmentLetter> getAssignmentLetter(Long session, Integer month, Integer year) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getAssignmentLetter(conn, month, year);
        } catch (SQLException sqle) {
            Exceptions.printStackTrace(sqle);
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public boolean getAssignmentLetterInExpedition(Long session, Long index) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getAssignmentLetterInExpedition(conn, index);
        } catch (SQLException sqle) {
            Exceptions.printStackTrace(sqle);
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public AssignmentLetter getCompleteAssignmentLetter(Long session, AssignmentLetter letter) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            Employee commander = mSql.getEmployeeByIndex(conn, letter.getCommander().getIndex());

            ArrayList<Employee> assignedEmployee = sql.getAssignedEmployee(conn, letter.getIndex());
            ArrayList<String> carbonCopy = sql.getCarbonCopy(conn, letter.getIndex());

            letter.setCommander(commander);
            letter.setAssignedEmployee(assignedEmployee);
            letter.setCarbonCopy(carbonCopy);

            letter.setStyled(true);

        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
        return letter;
    }


    public ExpeditionCheque insertExpeditionCheque(Long session, ExpeditionCheque cheque) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.insertExpeditionCheque(conn, cheque);

            Long index = sql.getMaxIndex(conn, "expeditioncheque", "autoindex");
            cheque.setIndex(index);

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
        return cheque;
    }

    public ExpeditionCheque updateExpeditionCheque(Long session, ExpeditionCheque oldCheque, ExpeditionCheque newCheque) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateExpeditionCheque(conn, oldCheque.getIndex(), newCheque);
            newCheque.setIndex(oldCheque.getIndex());

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
            Exceptions.printStackTrace(anyOtherException);
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
        return newCheque;
    }

    public void deleteExpeditionCheque(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteExpeditionCheque(conn, index);

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

    public ArrayList<ExpeditionCheque> getExpeditionCheque(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getExpeditionCheque(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ExpeditionCheque getCompleteExpeditionCheque(Long session,ExpeditionCheque cheque) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            Expedition expedition = sql.getExpeditionByIndex(conn, cheque.getExpedition().getIndex());
            expedition = getCompleteExpedition(session, expedition);
            expedition.setStyled(false);

            Employee headEmployee = mSql.getEmployeeByIndex(conn, cheque.getHeadEmployee().getIndex());
            Employee pptk = mSql.getEmployeeByIndex(conn, cheque.getPptk().getIndex());
            Employee clerk = mSql.getEmployeeByIndex(conn, cheque.getClerk().getIndex());
            Employee paidTo = mSql.getEmployeeByIndex(conn, cheque.getPaidTo().getIndex());
            StandardPrice amount = mSql.getStandardPriceByIndex(conn, cheque.getAmount().getIndex());

            headEmployee.setStyled(false);
            pptk.setStyled(false);
            clerk.setStyled(false);
            paidTo.setStyled(false);

            cheque.setExpedition(expedition);
            cheque.setHeadEmployee(headEmployee);
            cheque.setPptk(pptk);
            cheque.setClerk(clerk);
            cheque.setPaidTo(paidTo);
            cheque.setAmount(amount);

            return cheque;
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public BigDecimal getBudgetUsed(Long session,Integer years, Integer budgetType) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getBudgetUsed(conn, years, budgetType);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public BigDecimal getBudgetUsed(Long session,Long indexCheque,Integer years, Integer budgetType) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getBudgetUsed(conn, indexCheque, years, budgetType);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
}
