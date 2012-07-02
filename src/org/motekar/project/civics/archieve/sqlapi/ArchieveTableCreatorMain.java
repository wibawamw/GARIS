package org.motekar.project.civics.archieve.sqlapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import net.n3.nanoxml.XMLException;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.misc.ArchievePropertiesReader;
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
            sql.installEmployeeFacilityTable(stm);
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
            
            // Asset Table
            
            sql.installItemsCategoryTable(stm);
            sql.installItemsCategoryStructureTable(stm);
            sql.installRoomTable(stm);
            sql.installConditionTable(stm);
            sql.installItemsLandTable(stm);
            sql.installItemsMachineTable(stm);
            sql.installItemsBuildingTable(stm);
            sql.installItemsNetworkTable(stm);
            sql.installItemsFixedAssetTable(stm);
            sql.installItemsConstructionTable(stm);
            sql.installItemsMachineRoomTable(stm);
            sql.installItemsFixedAssetRoomTable(stm);
            sql.installConfigTable(stm);
            sql.installDocumentsTable(stm);
            // Baru 05-01-2012
            sql.installProcurementTable(stm);
            sql.installApprovalBookTable(stm);
            sql.installSignerTable(stm);
            sql.installReleaseBookTable(stm);
            sql.installItemsCardTable(stm);
            sql.installInventoryCardTable(stm);
            sql.installThirdPartyItemsTable(stm);
            sql.installUnrecycledItemsTable(stm);
            sql.installDeleteDraftItemsTable(stm);
            sql.installUsedItemsTable(stm);
            
            //
            sql.installItemsLandPictureTable(stm);
            sql.installItemsMachinePictureTable(stm);
            sql.installItemsBuildingPictureTable(stm);
            sql.installItemsNetworkPictureTable(stm);
            sql.installItemsFixedAssetPictureTable(stm);
            sql.installItemsConstructionPictureTable(stm);
            //
            
            sql.installSicknessMailTable(stm);
            sql.installDoctorMailTable(stm);
            
            

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
            
            // Asset Table
            
            sql.uninstallItemsCategoryTable(stm);
            sql.uninstallItemsCategoryStructureTable(stm);
            sql.uninstallRoomTable(stm);
            sql.uninstallItemsMachineRoomTable(stm);
            sql.uninstallItemsFixedAssetRoomTable(stm);
            sql.uninstallConditionTable(stm);
            sql.uninstallItemsLandTable(stm);
            sql.uninstallItemsMachineTable(stm);
            sql.uninstallItemsBuildingTable(stm);
            sql.uninstallItemsNetworkTable(stm);
            sql.uninstallItemsFixedAssetTable(stm);
            sql.uninstallItemsConstructionTable(stm);
            sql.uninstallConfigTable(stm);
            sql.uninstallDocumentsTable(stm);
            // Baru 05-01-2012
            sql.uninstallProcurementTable(stm);
            sql.uninstallApprovalBookTable(stm);
            sql.uninstallSignerTable(stm);
            sql.uninstallReleasebookTable(stm);
            sql.uninstallItemsCardTable(stm);
            sql.uninstallInventoryCardTable(stm);
            sql.uninstallThirdPartyItemsTable(stm);
            sql.uninstallUnrecycledItemsTable(stm);
            sql.uninstallDeleteDraftItemsTable(stm);
            sql.uninstallUsedItemsTable(stm);
            //
            sql.uninstallItemsLandPictureTable(stm);
            sql.uninstallItemsMachinePictureTable(stm);
            sql.uninstallItemsBuildingPictureTable(stm);
            sql.uninstallItemsNetworkPictureTable(stm);
            sql.uninstallItemsFixedAssetPictureTable(stm);
            sql.uninstallItemsConstructionPictureTable(stm);
            
            //
            sql.uninstallSicknessMailTable(stm);
            sql.uninstallDoctorMailTable(stm);
            

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
    
    public static ArchieveProperties reloadArchieveProperties(String fileName) {
        ArchieveProperties config = null;
        try {
            config = new ArchieveProperties(fileName);
            ArchievePropertiesReader reader = new ArchievePropertiesReader(config.getXMLFile());
            reader.loadXML();
            config = reader.getProperties();

        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (XMLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvalidKeyException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalBlockSizeException ex) {
            Exceptions.printStackTrace(ex);
        } catch (BadPaddingException ex) {
            Exceptions.printStackTrace(ex);
        }
        return config;
    }

    public static void main(String[] args) {
        try {

            ArchieveProperties prop = reloadArchieveProperties("app.xml");
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
