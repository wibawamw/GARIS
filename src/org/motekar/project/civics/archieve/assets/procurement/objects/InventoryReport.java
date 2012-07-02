package org.motekar.project.civics.archieve.assets.procurement.objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Muhamad Wibawa
 */
public class InventoryReport implements Comparable<InventoryReport> {
    
    private Date inventoryDate = null;
    
    private ApprovalBook approvalBook = null;
    private ReleaseBook releaseBook = null;
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("in", "id", "id"));
    
    public InventoryReport() {
    }

    public ApprovalBook getApprovalBook() {
        return approvalBook;
    }

    public void setApprovalBook(ApprovalBook approvalBook) {
        this.approvalBook = approvalBook;
    }

    public ReleaseBook getReleaseBook() {
        return releaseBook;
    }

    public void setReleaseBook(ReleaseBook releaseBook) {
        this.releaseBook = releaseBook;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }
    
    public static ArrayList<InventoryReport> createInventoryReport(ArrayList<ApprovalBook> approvalBooks,
            ArrayList<ReleaseBook> releaseBooks) {
        ArrayList<InventoryReport> reports = new ArrayList<InventoryReport>();
        
        if (!approvalBooks.isEmpty() && !releaseBooks.isEmpty()) {
            Collections.sort(approvalBooks);
            Collections.sort(releaseBooks);
            
            
            for (ApprovalBook ab : approvalBooks) {
                InventoryReport ir = new InventoryReport();
                ir.setApprovalBook(ab);
                ir.setReleaseBook(new ReleaseBook());
                ir.setInventoryDate(ab.getReceiveDate());
                reports.add(ir);
            }
            
            for (ReleaseBook rb : releaseBooks) {
                InventoryReport ir = new InventoryReport();
                ir.setApprovalBook(new ApprovalBook());
                ir.setReleaseBook(rb);
                ir.setInventoryDate(rb.getReleaseDate());
                reports.add(ir);
            }
            
        }
        
        
        if (!reports.isEmpty()) {
            Collections.sort(reports);
        }
        
        
        return reports;
    }

    public int compareTo(InventoryReport o) {
        return this.getInventoryDate().compareTo(o.getInventoryDate());
    }

    @Override
    public String toString() {
        
        if (inventoryDate == null) {
            return "";
        }
        
        return sdf.format(inventoryDate);
    }
}
