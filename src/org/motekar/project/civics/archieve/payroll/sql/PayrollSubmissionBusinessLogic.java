package org.motekar.project.civics.archieve.payroll.sql;

import org.motekar.project.civics.archieve.payroll.objects.PayrollSubmissionFile;
import org.motekar.project.civics.archieve.payroll.objects.PayrollSubmissionRequirement;
import org.motekar.project.civics.archieve.payroll.objects.SubmissionRequirement;
import org.motekar.project.civics.archieve.payroll.objects.SubmissionType;
import org.motekar.project.civics.archieve.payroll.objects.PayrollSubmission;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.motekar.project.civics.archieve.master.sqlapi.MasterSQL;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.sqlapi.AuthBusinessLogic;

/**
 *
 * @author Muhamad Wibawa <wibawa@motekar-it.co.id>
 */
public class PayrollSubmissionBusinessLogic {
    private Connection conn;
    private PayrollSubmissionSQL sql = new PayrollSubmissionSQL();
    private MasterSQL mSql = new MasterSQL();
    private AuthBusinessLogic auth;

    public PayrollSubmissionBusinessLogic(Connection conn) {
        this.conn = conn;
        auth = new AuthBusinessLogic(conn);
    }
    
    public SubmissionType insertSubmissionType(Long session, SubmissionType type) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            sql.insertSubmissionType(conn, type);
            Long index = sql.getMaxIndex(conn, "submissiontype", "autoindex");
            type.setIndex(index);
            type.setStyled(true);



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
        return type;
    }

    public SubmissionType updateSubmissionType(Long session, SubmissionType oldType, SubmissionType newType) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateSubmissionType(conn, oldType.getIndex(), newType);
            newType.setIndex(oldType.getIndex());
            newType.setStyled(true);

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
        return newType;
    }

    public void deleteSubmissionType(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteSubmissionType(conn, index);

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
    
    public ArrayList<SubmissionType> getSubmissionType(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getSubmissionType(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public SubmissionType getSubmissionType(Long session,Long index) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getSubmissionType(conn, index);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public SubmissionRequirement insertSubmissionRequirement(Long session, SubmissionRequirement requirement) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            sql.insertSubmissionRequirement(conn, requirement);
            Long index = sql.getMaxIndex(conn, "submissionrequirement", "autoindex");
            requirement.setIndex(index);
            requirement.setStyled(true);



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
        return requirement;
    }

    public SubmissionRequirement updateSubmissionRequirement(Long session, SubmissionRequirement oldReq, SubmissionRequirement newReq) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateSubmissionRequirement(conn, oldReq.getIndex(), newReq);
            newReq.setIndex(oldReq.getIndex());
            newReq.setStyled(true);

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
        return newReq;
    }

    public void deleteSubmissionRequirement(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteSubmissionRequirement(conn, index);

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
    
    public ArrayList<SubmissionRequirement> getSubmissionRequirement(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getSubmissionRequirement(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public SubmissionRequirement getSubmissionRequirement(Long session,Long index) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getSubmissionRequirement(conn, index);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public PayrollSubmission insertPayrollSubmission(Long session, PayrollSubmission submission) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            sql.insertPayrollSubmission(conn, submission);
            Long index = sql.getMaxIndex(conn, "payrollsubmission", "autoindex");
            submission.setIndex(index);
            submission.setStyled(true);
            
            ArrayList<PayrollSubmissionRequirement> requirements = submission.getRequirements();
            ArrayList<PayrollSubmissionFile> files = submission.getFiles();
            
            if (!requirements.isEmpty()) {
                for (PayrollSubmissionRequirement req : requirements) {
                    req.setSubmissionIndex(index);
                    sql.insertPayrollSubmissionRequirement(conn, req);
                }
            }
            
            if (!files.isEmpty()) {
                for (PayrollSubmissionFile file : files) {
                    file.setSubmissionIndex(index);
                    sql.insertPayrollSubmissionFile(conn, file);
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
        return submission;
    }

    public PayrollSubmission updatePayrollSubmission(Long session, PayrollSubmission oldSubmission, PayrollSubmission newSubmission) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updatePayrollSubmission(conn, oldSubmission.getIndex(), newSubmission);
            newSubmission.setIndex(oldSubmission.getIndex());
            newSubmission.setStyled(true);
            
            sql.deletePayrollSubmissionRequirement(conn, newSubmission.getIndex());
            sql.deletePayrollSubmissionFile(conn, newSubmission.getIndex());
            
            ArrayList<PayrollSubmissionRequirement> requirements = newSubmission.getRequirements();
            ArrayList<PayrollSubmissionFile> files = newSubmission.getFiles();
            
            if (!requirements.isEmpty()) {
                for (PayrollSubmissionRequirement req : requirements) {
                    req.setSubmissionIndex(newSubmission.getIndex());
                    sql.insertPayrollSubmissionRequirement(conn, req);
                }
            }
            
            if (!files.isEmpty()) {
                for (PayrollSubmissionFile file : files) {
                    file.setSubmissionIndex(newSubmission.getIndex());
                    sql.insertPayrollSubmissionFile(conn, file);
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
        return newSubmission;
    }

    public void deletePayrollSubmission(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deletePayrollSubmission(conn, index);

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
    
    public ArrayList<PayrollSubmission> getPayrollSubmission(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getPayrollSubmission(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public PayrollSubmission getPayrollSubmission(Long session,Long index) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getPayrollSubmission(conn, index);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ArrayList<PayrollSubmissionRequirement> getPayrollSubmissionRequirement(Long session,Long index) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getPayrollSubmissionRequirement(conn, index);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ArrayList<PayrollSubmissionFile> getPayrollSubmissionFile(Long session,Long index) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getPayrollSubmissionFile(conn, index);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
//    public PayrollSubmission getCompletePayrollSubmission(Long session,PayrollSubmission submission) throws SQLException {
//        try {
//            if (!auth.isSessionExpired(session)) {
//                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
//            }
//            
//            ArrayList<PayrollSubmissionRequirement> requirements = sql.getPayrollSubmissionRequirement(conn, submission.getIndex());
//            ArrayList<PayrollSubmissionFile> files = sql.getPayrollSubmissionFile(conn, submission.getIndex());
//            
//            if (!requirements.isEmpty()) {
//                for (PayrollSubmissionRequirement requirement : requirements) {
//                    requirement.setRequirement(sql.getSubmissionRequirement(conn, requirement.getRequirement().getIndex()));
//                }
//            }
//            
//            submission.setRequirements(requirements);
//            submission.setFiles(files);
//            
//            
//            return submission;
//        } catch (SQLException sqle) {
//            throw sqle;
//        } catch (Throwable anyOtherException) {
//            throw new RuntimeException(anyOtherException);
//        }
//    }
    
}
