package org.motekar.project.civics.archieve.report.sqlapi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.motekar.project.civics.archieve.master.objects.Employee;
import org.motekar.project.civics.archieve.report.object.BudgetRealization;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.sqlapi.AuthBusinessLogic;

/**
 *
 * @author Muhamad Wibawa
 */
public class ReportBusinessLogic {

    private Connection conn;
    private AuthBusinessLogic auth;

    private ReportSQL sql = new ReportSQL();

    public ReportBusinessLogic(Connection conn) {
        this.conn = conn;
        auth = new AuthBusinessLogic(conn);
    }

    public ArrayList<BudgetRealization> getBudgetRealization(Long session, Integer years, Integer budgetType) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getBudgetRealization(conn, years, budgetType);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

    public ArrayList<Employee> getEmployeeReport(Long session,String modifier) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getEmployeeReport(conn,modifier);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }

}
