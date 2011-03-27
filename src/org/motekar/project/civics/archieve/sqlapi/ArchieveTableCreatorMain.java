package org.motekar.project.civics.archieve.sqlapi;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.motekar.util.user.misc.MotekarProperties;
import org.motekar.util.user.sqlapi.ConnectionManager;
import org.motekar.util.user.sqlapi.TableCreatorMain;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ArchieveTableCreatorMain extends TableCreatorMain {

    private ArchieveTableCreatorSQL sql = new ArchieveTableCreatorSQL();
    
    public ArchieveTableCreatorMain(Connection conn) {
        super(conn);
    }

    public void installArsipTable() {
        try {
            conn.setAutoCommit(false);

            Statement stm = conn.createStatement();

            sql.installAccountTable(stm);
            sql.installAccountStructureTable(stm);
            sql.installEmployeeTable(stm);
            sql.installCurriculumVitaeTable(stm);
            sql.installEmployeeTable(stm);
            sql.installEmployeeCoursesTable(stm);
            sql.installDivisionTable(stm);
            sql.installAssignmentLetterTable(stm);
            sql.installAssignedEmployeeTable(stm);
            sql.installCarbonCopyTable(stm);
            sql.installInboxTable(stm);
            sql.installInboxDispositionTable(stm);
            sql.installInboxFileTable(stm);
            sql.installOutboxTable(stm);
            sql.installOutboxDispositionTable(stm);
            sql.installOutboxFileTable(stm);
            sql.installProgramTable(stm);
            sql.installActivityTable(stm);
            sql.installExpeditionTable(stm);
            sql.installExpeditionFollowerTable(stm);
            sql.installExpeditionJournalTable(stm);
            sql.installExpeditionResultTable(stm);
            sql.installStandarPriceTable(stm);
            sql.installExpeditionChequeTable(stm);
            sql.installBudgetTable(stm);
            sql.installBudgetDetailTable(stm);
            sql.installBudgetSubDetailTable(stm);

            stm.close();
            conn.commit();
            closeConnection();
            System.out.println("Ok");

        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(TableCreatorMain.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(TableCreatorMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(TableCreatorMain.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(TableCreatorMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void uninstallArsipTable() {
        try {
            conn.setAutoCommit(false);
            Statement stm = conn.createStatement();

            sql.uninstallInboxFileTable(stm);
            sql.uninstallInboxTable(stm);
            sql.uninstallOutboxFileTable(stm);
            sql.uninstallOutboxTable(stm);
            sql.uninstallEmployeeFacilitysTable(stm);
            sql.uninstallEmployeeCoursesTable(stm);
            sql.uninstallCurriculumVitaeTable(stm);
            sql.uninstallEmployeeTable(stm);
            sql.uninstallExpeditionTable(stm);
            sql.uninstallExpeditionFollowerTable(stm);
            sql.uninstallExpeditionJournalTable(stm);
            sql.uninstallExpeditionResultTable(stm);
            sql.uninstallActivityTable(stm);
            sql.uninstallProgramTable(stm);
            sql.uninstallCarbonCopyTable(stm);
            sql.uninstallAssignedEmployeeTable(stm);
            sql.uninstallAssignmentLetterTable(stm);
            sql.uninstallStandarPriceTable(stm);
            sql.uninstallDivisionTable(stm);
            sql.uninstallAccountStructureTable(stm);
            sql.uninstallAccountTable(stm);
            sql.uninstallExpeditionChequeTable(stm);
            sql.uninstallBudgetTable(stm);
            sql.uninstallBudgetDetailTable(stm);
            sql.uninstallBudgetSubDetailTable(stm);

            stm.close();
            conn.commit();
            closeConnection();
            System.out.println("Ok");
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(TableCreatorMain.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(TableCreatorMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        try {

            MotekarProperties prop = ArchieveTableCreatorMain.reloadProperties("app.xml");
            if (prop != null) {
                ArchieveTableCreatorMain.createDatabase(prop.getDatabase());
                ConnectionManager connectionManager = new ConnectionManager(new File("app.xml"));
                Connection conn = connectionManager.connect();

                ArchieveTableCreatorMain arsipCreator = new ArchieveTableCreatorMain(conn);
//                creator.uninstallTable();
//                ArchieveTableCreatorMain.dropDatabase(prop.getDatabase());

                arsipCreator.installLoginTable();
                arsipCreator.installArsipTable();
            } else {
                System.out.println("Tidak ditemukan properties dengan nama app.xml");
            }


        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

}
