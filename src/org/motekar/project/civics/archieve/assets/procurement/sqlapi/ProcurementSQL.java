package org.motekar.project.civics.archieve.assets.procurement.sqlapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.assets.procurement.objects.ApprovalBook;
import org.motekar.project.civics.archieve.assets.procurement.objects.InventoryCard;
import org.motekar.project.civics.archieve.assets.procurement.objects.ItemCard;
import org.motekar.project.civics.archieve.assets.procurement.objects.ItemCardMap;
import org.motekar.project.civics.archieve.assets.procurement.objects.Procurement;
import org.motekar.project.civics.archieve.assets.procurement.objects.ReleaseBook;
import org.motekar.project.civics.archieve.assets.procurement.objects.Signer;
import org.motekar.project.civics.archieve.assets.procurement.objects.ThirdPartyItems;
import org.motekar.project.civics.archieve.assets.procurement.objects.UnrecycledItems;
import org.motekar.project.civics.archieve.sqlapi.CommonSQL;

/**
 *
 * @author Muhamad Wibawa
 */
public class ProcurementSQL extends CommonSQL {

    void insertProcurement(Connection conn, Procurement procurement) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into procurement(itemscode,itemsname, contractdate, contractnumber,").
                append("documentdate, documentnumber, amount, price, unit, description)").
                append("values(?, ?, ?, ?,?, ?, ?, ?, ?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, procurement.getItemCode());
        pstm.setString(++col, procurement.getItemName());
        
        if (procurement.getContractDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(procurement.getContractDate().getTime()));
        }
        
        pstm.setString(++col, procurement.getContractNumber());
        
        if (procurement.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(procurement.getDocumentDate().getTime()));
        }
        
        
        pstm.setString(++col, procurement.getDocumentNumber());
        pstm.setInt(++col, procurement.getAmount());
        pstm.setBigDecimal(++col, procurement.getPrice());

        if (procurement.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, procurement.getUnit().getIndex());
        }

        pstm.setString(++col, procurement.getDescription());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateProcurement(Connection conn, Long oldIndex, Procurement procurement) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update procurement set itemscode=?,itemsname=?, contractdate=?, contractnumber=?,").
                append("documentdate=?, documentnumber=?, amount=?, price=?, unit=?, description=? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;
        
        pstm.setString(++col, procurement.getItemCode());
        pstm.setString(++col, procurement.getItemName());
        
        if (procurement.getContractDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(procurement.getContractDate().getTime()));
        }
        
        pstm.setString(++col, procurement.getContractNumber());
        
        if (procurement.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(procurement.getDocumentDate().getTime()));
        }
        
        
        pstm.setString(++col, procurement.getDocumentNumber());
        pstm.setInt(++col, procurement.getAmount());
        pstm.setBigDecimal(++col, procurement.getPrice());

        if (procurement.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, procurement.getUnit().getIndex());
        }

        pstm.setString(++col, procurement.getDescription());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteProcurement(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from procurement where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Procurement> getProcurement(Connection conn,String modifier) throws SQLException {
        ArrayList<Procurement> procurements = new ArrayList<Procurement>();

        StringBuilder query = new StringBuilder();
        query.append("select * from procurement ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Procurement procurement = new Procurement();
            procurement.setIndex(rs.getLong("autoindex"));
            procurement.setItemCode(rs.getString("itemscode"));
            procurement.setItemName(rs.getString("itemsname"));
            procurement.setContractDate(rs.getDate("contractdate"));
            procurement.setContractNumber(rs.getString("contractnumber"));
            procurement.setDocumentDate(rs.getDate("documentdate"));
            procurement.setDocumentNumber(rs.getString("documentnumber"));
            procurement.setAmount(rs.getInt("amount"));
            procurement.setPrice(rs.getBigDecimal("price"));

            Unit unit = null;
            Long unitIndex = rs.getLong("unit");

            if (unitIndex != null && unitIndex.compareTo(Long.valueOf(0)) > 0) {
                unit = new Unit(unitIndex);
            }

            procurement.setUnit(unit);
            procurement.setDescription(rs.getString("description"));

            procurements.add(procurement);
        }

        rs.close();
        pstm.close();

        return procurements;
    }

    void insertSigner(Connection conn, Signer signer) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into signer(signername, signernip, signertype)").
                append("values(?, ?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, signer.getSignerName());
        pstm.setString(++col, signer.getSignerNIP());
        pstm.setInt(++col, signer.getSignerType());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateSigner(Connection conn, Long oldIndex, Signer signer) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update signer set signername=?, signernip=?, signertype=? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, signer.getSignerName());
        pstm.setString(++col, signer.getSignerNIP());
        pstm.setInt(++col, signer.getSignerType());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteSigner(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from signer where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Signer> getSigner(Connection conn) throws SQLException {
        ArrayList<Signer> signers = new ArrayList<Signer>();

        StringBuilder query = new StringBuilder();
        query.append("select * from signer ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Signer signer = new Signer();
            signer.setIndex(rs.getLong("autoindex"));
            signer.setSignerName(rs.getString("signername"));
            signer.setSignerNIP(rs.getString("signernip"));
            signer.setSignerType(rs.getInt("signertype"));

            signers.add(signer);
        }

        rs.close();
        pstm.close();

        return signers;
    }
    
    ArrayList<Signer> getSigner(Connection conn,Integer signerType) throws SQLException {
        ArrayList<Signer> signers = new ArrayList<Signer>();

        StringBuilder query = new StringBuilder();
        query.append("select * from signer ").
                append(" where signertype = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, signerType);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Signer signer = new Signer();
            signer.setIndex(rs.getLong("autoindex"));
            signer.setSignerName(rs.getString("signername"));
            signer.setSignerNIP(rs.getString("signernip"));
            signer.setSignerType(rs.getInt("signertype"));

            signers.add(signer);
        }

        rs.close();
        pstm.close();

        return signers;
    }
    
    Signer getSigner(Connection conn,String name) throws SQLException {
        Signer signer = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from signer ").
                append(" where signername = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setString(1, name);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            signer = new Signer();
            signer.setIndex(rs.getLong("autoindex"));
            signer.setSignerName(rs.getString("signername"));
            signer.setSignerNIP(rs.getString("signernip"));
            signer.setSignerType(rs.getInt("signertype"));

        }

        rs.close();
        pstm.close();

        return signer;
    }

    void insertApprovalBook(Connection conn, ApprovalBook approvalbook) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into approvalbook(receivedate, receivefrom, invoicenumber, invoicedate, ").
                append("itemscode, itemsname, amount, price, documentnumber, documentdate, description,warehouse,").
                append("contractdate,contractnumber)").
                append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setDate(++col, new java.sql.Date(approvalbook.getReceiveDate().getTime()));
        pstm.setString(++col, approvalbook.getReceiveFrom());
        pstm.setString(++col, approvalbook.getInvoiceNumber());
        
        
        if (approvalbook.getInvoiceDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(approvalbook.getInvoiceDate().getTime()));
        }
        
        pstm.setString(++col, approvalbook.getItemCode());
        pstm.setString(++col, approvalbook.getItemName());
        pstm.setInt(++col, approvalbook.getAmount());
        pstm.setBigDecimal(++col, approvalbook.getPrice());
        pstm.setString(++col, approvalbook.getDocumentNumber());
        
        
        if (approvalbook.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(approvalbook.getDocumentDate().getTime()));
        }
        
        pstm.setString(++col, approvalbook.getDescription());
        
        if (approvalbook.getWarehouse() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, approvalbook.getWarehouse().getIndex());
        }
        
        if (approvalbook.getContractDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(approvalbook.getContractDate().getTime()));
        }
        
        pstm.setString(++col, approvalbook.getContractNumber());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateApprovalBook(Connection conn, Long oldIndex, ApprovalBook approvalbook) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update approvalbook set receivedate=?, receivefrom=?, invoicenumber=?, invoicedate=?, ").
                append("itemscode=?, itemsname=?, amount=?, price=?, documentnumber=?, ").
                append("documentdate=?, description=?, warehouse = ?,contractdate = ?,contractnumber = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setDate(++col, new java.sql.Date(approvalbook.getReceiveDate().getTime()));
        pstm.setString(++col, approvalbook.getReceiveFrom());
        pstm.setString(++col, approvalbook.getInvoiceNumber());
        
        
        if (approvalbook.getInvoiceDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(approvalbook.getInvoiceDate().getTime()));
        }
        
        pstm.setString(++col, approvalbook.getItemCode());
        pstm.setString(++col, approvalbook.getItemName());
        pstm.setInt(++col, approvalbook.getAmount());
        pstm.setBigDecimal(++col, approvalbook.getPrice());
        pstm.setString(++col, approvalbook.getDocumentNumber());
        
        
        if (approvalbook.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(approvalbook.getDocumentDate().getTime()));
        }
        
        pstm.setString(++col, approvalbook.getDescription());
        
        if (approvalbook.getWarehouse() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, approvalbook.getWarehouse().getIndex());
        }
        
        if (approvalbook.getContractDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(approvalbook.getContractDate().getTime()));
        }
        
        pstm.setString(++col, approvalbook.getContractNumber());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteApprovalBook(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from approvalbook where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ApprovalBook> getApprovalBook(Connection conn,String modifier) throws SQLException {
        ArrayList<ApprovalBook> approvalbooks = new ArrayList<ApprovalBook>();

        StringBuilder query = new StringBuilder();
        query.append("select * from approvalbook ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ApprovalBook approvalbook = new ApprovalBook();
            approvalbook.setIndex(rs.getLong("autoindex"));
            approvalbook.setReceiveDate(rs.getDate("receivedate"));
            approvalbook.setReceiveFrom(rs.getString("receivefrom"));
            approvalbook.setInvoiceNumber(rs.getString("invoicenumber"));
            approvalbook.setInvoiceDate(rs.getDate("invoicedate"));
            approvalbook.setItemCode(rs.getString("itemscode"));
            approvalbook.setItemName(rs.getString("itemsname"));
            approvalbook.setDocumentNumber(rs.getString("documentnumber"));
            approvalbook.setDocumentDate(rs.getDate("documentdate"));
            approvalbook.setAmount(rs.getInt("amount"));
            approvalbook.setPrice(rs.getBigDecimal("price"));
            approvalbook.setDescription(rs.getString("description"));
            
            approvalbook.setContractNumber(rs.getString("contractnumber"));
            approvalbook.setContractDate(rs.getDate("contractdate"));
            
            Unit warehouse = null;
            Long unitIndex = rs.getLong("warehouse");
            
            if (unitIndex != null) {
                if (!unitIndex.equals(Long.valueOf(0))) {
                    warehouse = new Unit(unitIndex);
                }
            }
            
            approvalbook.setWarehouse(warehouse);
            
            approvalbooks.add(approvalbook);
        }

        rs.close();
        pstm.close();

        return approvalbooks;
    }

    void insertReleaseBook(Connection conn, ReleaseBook releasebook) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into releasebook(releasedate, sortingnumber, itemscode, itemsname, ").
                append("amount, price, allotment, delegatedate, description,warehouse,billnumber,billdate)").
                append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setDate(++col, new java.sql.Date(releasebook.getReleaseDate().getTime()));
        pstm.setString(++col, releasebook.getSortingNumber());
        pstm.setString(++col, releasebook.getItemCode());
        pstm.setString(++col, releasebook.getItemName());
        pstm.setInt(++col, releasebook.getAmount());
        pstm.setBigDecimal(++col, releasebook.getPrice());
        pstm.setString(++col, releasebook.getAllotment());
        
                
        if (releasebook.getDelegateDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(releasebook.getDelegateDate().getTime()));
        }

        
        pstm.setString(++col, releasebook.getDescription());
        
        if (releasebook.getWarehouse() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, releasebook.getWarehouse().getIndex());
        }
        
        pstm.setString(++col, releasebook.getBillNumber());
        pstm.setDate(++col, new java.sql.Date(releasebook.getBillDate().getTime()));

        pstm.executeUpdate();
        pstm.close();
    }

    void updateReleaseBook(Connection conn, Long oldIndex, ReleaseBook releasebook) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update releasebook set releasedate=?, sortingnumber=?, itemscode=?, itemsname=?, ").
                append("amount=?, price=?, allotment=?, delegatedate=?, description=?, warehouse = ?, ").
                append("billnumber = ?,billdate = ?").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setDate(++col, new java.sql.Date(releasebook.getReleaseDate().getTime()));
        pstm.setString(++col, releasebook.getSortingNumber());
        pstm.setString(++col, releasebook.getItemCode());
        pstm.setString(++col, releasebook.getItemName());
        pstm.setInt(++col, releasebook.getAmount());
        pstm.setBigDecimal(++col, releasebook.getPrice());
        pstm.setString(++col, releasebook.getAllotment());
        pstm.setDate(++col, new java.sql.Date(releasebook.getDelegateDate().getTime()));
        pstm.setString(++col, releasebook.getDescription());
        
        if (releasebook.getWarehouse() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, releasebook.getWarehouse().getIndex());
        }
        
        pstm.setString(++col, releasebook.getBillNumber());
        pstm.setDate(++col, new java.sql.Date(releasebook.getBillDate().getTime()));

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteReleaseBook(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from releasebook where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ReleaseBook> getReleaseBook(Connection conn) throws SQLException {
        ArrayList<ReleaseBook> releasebooks = new ArrayList<ReleaseBook>();

        StringBuilder query = new StringBuilder();
        query.append("select * from releasebook ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ReleaseBook releasebook = new ReleaseBook();
            releasebook.setIndex(rs.getLong("autoindex"));
            releasebook.setReleaseDate(rs.getDate("releasedate"));
            releasebook.setSortingNumber(rs.getString("sortingnumber"));
            releasebook.setItemCode(rs.getString("itemscode"));
            releasebook.setItemName(rs.getString("itemsname"));
            releasebook.setAmount(rs.getInt("amount"));
            releasebook.setPrice(rs.getBigDecimal("price"));
            releasebook.setAllotment(rs.getString("allotment"));
            releasebook.setDelegateDate(rs.getDate("delegatedate"));
            releasebook.setDescription(rs.getString("description"));
            
            releasebook.setBillDate(rs.getDate("billdate"));
            releasebook.setBillNumber(rs.getString("billnumber"));
            
            Unit warehouse = null;
            Long unitIndex = rs.getLong("warehouse");
            
            if (unitIndex != null) {
                if (!unitIndex.equals(Long.valueOf(0))) {
                    warehouse = new Unit(unitIndex);
                }
            }
            
            releasebook.setWarehouse(warehouse);
            
            releasebooks.add(releasebook);
        }

        rs.close();
        pstm.close();

        return releasebooks;
    }
    
    ArrayList<ReleaseBook> getReleaseBook(Connection conn,String modifier) throws SQLException {
        ArrayList<ReleaseBook> releasebooks = new ArrayList<ReleaseBook>();

        StringBuilder query = new StringBuilder();
        query.append("select * from releasebook ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ReleaseBook releasebook = new ReleaseBook();
            releasebook.setIndex(rs.getLong("autoindex"));
            releasebook.setReleaseDate(rs.getDate("releasedate"));
            releasebook.setSortingNumber(rs.getString("sortingnumber"));
            releasebook.setItemCode(rs.getString("itemscode"));
            releasebook.setItemName(rs.getString("itemsname"));
            releasebook.setAmount(rs.getInt("amount"));
            releasebook.setPrice(rs.getBigDecimal("price"));
            releasebook.setAllotment(rs.getString("allotment"));
            releasebook.setDelegateDate(rs.getDate("delegatedate"));
            releasebook.setDescription(rs.getString("description"));
            
            releasebook.setBillDate(rs.getDate("billdate"));
            releasebook.setBillNumber(rs.getString("billnumber"));
            
            Unit warehouse = null;
            Long unitIndex = rs.getLong("warehouse");
            
            if (unitIndex != null) {
                if (!unitIndex.equals(Long.valueOf(0))) {
                    warehouse = new Unit(unitIndex);
                }
            }
            
            releasebook.setWarehouse(warehouse);
            
            releasebooks.add(releasebook);
        }

        rs.close();
        pstm.close();

        return releasebooks;
    }

    void insertItemCard(Connection conn, ItemCard itemcard) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into itemcard(itemcode, itemname, specification)").
                append("values(?, ?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, itemcard.getItemCode());
        pstm.setString(++col, itemcard.getItemName());
        pstm.setString(++col, itemcard.getSpecification());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateItemCard(Connection conn, Long oldIndex, ItemCard itemcard) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update itemcard set itemcode=?, itemname=?, specification=? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, itemcard.getItemCode());
        pstm.setString(++col, itemcard.getItemName());
        pstm.setString(++col, itemcard.getSpecification());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemCard(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemcard where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ItemCard> getItemCard(Connection conn) throws SQLException {
        ArrayList<ItemCard> itemcards = new ArrayList<ItemCard>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemcard ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ItemCard itemcard = new ItemCard();
            itemcard.setIndex(rs.getLong("autoindex"));
            itemcard.setItemCode(rs.getString("itemcode"));
            itemcard.setItemName(rs.getString("itemname"));
            itemcard.setSpecification(rs.getString("specification"));

            itemcards.add(itemcard);
        }

        rs.close();
        pstm.close();

        return itemcards;
    }
    
    ItemCard getItemCard(Connection conn,String code) throws SQLException {
        ItemCard itemcard = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from itemcard ").
                append("where itemcode = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setString(1, code);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            itemcard = new ItemCard();
            itemcard.setIndex(rs.getLong("autoindex"));
            itemcard.setItemCode(rs.getString("itemcode"));
            itemcard.setItemName(rs.getString("itemname"));
            itemcard.setSpecification(rs.getString("specification"));

        }

        rs.close();
        pstm.close();

        return itemcard;
    }
    
    
    ArrayList<ItemCardMap> getItemCardMap(Connection conn,String modifier) throws SQLException {
        ArrayList<ItemCardMap> maps = new ArrayList<ItemCardMap>();

        StringBuilder query = new StringBuilder();
        query.append("select * from ").
                append("(select receivedate as idate,itemscode,warehouse,documentnumber,documentdate,receivefrom as args,  ").
                append("amount,price,description from approvalbook ").
                append("union ").
                append("select releasedate as idate,itemscode,warehouse,billnumber as documentnumber, billdate as documentdate,  ").
                append("allotment as args,-amount,price,description from releasebook) as card ").
                append(modifier).
                append(" order by idate,amount desc ");
        
        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ItemCardMap icMap = new ItemCardMap();
            icMap.setItemsDate(rs.getDate("idate"));
            icMap.setDocumentNumber(rs.getString("documentnumber"));
            icMap.setDocumentDate(rs.getDate("documentdate"));
            icMap.setArgument(rs.getString("args"));
            icMap.setAmount(rs.getInt("amount"));
            icMap.setPrice(rs.getBigDecimal("price"));
            icMap.setDescription(rs.getString("description"));

            maps.add(icMap);
        }

        rs.close();
        pstm.close();

        return maps;
    }
    
    ArrayList<ItemCardMap> getItemCardMap(Connection conn) throws SQLException {
        ArrayList<ItemCardMap> maps = new ArrayList<ItemCardMap>();

        StringBuilder query = new StringBuilder();
        query.append("select * from ").
                append("(select receivedate as idate,itemscode,documentnumber,documentdate,receivefrom as args,  ").
                append("amount,price,description from approvalbook ").
                append("union ").
                append("select releasedate as idate,itemscode,billnumber as documentnumber, billdate as documentdate,  ").
                append("allotment as args,-amount,price,description from releasebook) as card ").
                append("order by idate,amount desc ");
        
        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ItemCardMap icMap = new ItemCardMap();
            icMap.setItemsDate(rs.getDate("idate"));
            icMap.setDocumentNumber(rs.getString("documentnumber"));
            icMap.setDocumentDate(rs.getDate("documentdate"));
            icMap.setArgument(rs.getString("args"));
            icMap.setAmount(rs.getInt("amount"));
            icMap.setPrice(rs.getBigDecimal("price"));
            icMap.setDescription(rs.getString("description"));

            maps.add(icMap);
        }

        rs.close();
        pstm.close();

        return maps;
    }

    void insertInventoryCard(Connection conn, InventoryCard inventorycard) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into inventorycard(warehouse, itemcode, itemname, ").
                append("units, cardnumber, spesification)").
                append("values(?, ?, ?, ?, ?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        if (inventorycard.getWarehouse() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, inventorycard.getWarehouse().getIndex());
        }

        pstm.setString(++col, inventorycard.getItemCode());
        pstm.setString(++col, inventorycard.getItemName());
        pstm.setString(++col, inventorycard.getItemsUnit());
        pstm.setString(++col, inventorycard.getCardNumber());
        pstm.setString(++col, inventorycard.getSpecification());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateInventoryCard(Connection conn, Long oldIndex, InventoryCard inventorycard) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update inventorycard set warehouse=?, itemcode=?, itemname=?, ").
                append("units=?, cardnumber=?, spesification=? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        if (inventorycard.getWarehouse() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, inventorycard.getWarehouse().getIndex());
        }

        pstm.setString(++col, inventorycard.getItemCode());
        pstm.setString(++col, inventorycard.getItemName());
        pstm.setString(++col, inventorycard.getItemsUnit());
        pstm.setString(++col, inventorycard.getCardNumber());
        pstm.setString(++col, inventorycard.getSpecification());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteInventoryCard(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from inventorycard where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<InventoryCard> getInventoryCard(Connection conn) throws SQLException {
        ArrayList<InventoryCard> inventorycards = new ArrayList<InventoryCard>();

        StringBuilder query = new StringBuilder();
        query.append("select * from inventorycard ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            InventoryCard inventorycard = new InventoryCard();
            inventorycard.setIndex(rs.getLong("autoindex"));

            Unit warehouse = null;
            Long unitIndex = rs.getLong("warehouse");

            if (unitIndex != null && unitIndex.compareTo(Long.valueOf(0)) > 0) {
                warehouse = new Unit(unitIndex);
            }

            inventorycard.setWarehouse(warehouse);

            inventorycard.setItemCode(rs.getString("itemcode"));
            inventorycard.setItemName(rs.getString("itemname"));
            inventorycard.setItemsUnit(rs.getString("units"));
            inventorycard.setCardNumber(rs.getString("cardnumber"));
            inventorycard.setSpecification(rs.getString("specification"));

            inventorycards.add(inventorycard);
        }

        rs.close();
        pstm.close();

        return inventorycards;
    }
    
    InventoryCard getInventoryCard(Connection conn,Unit warehouse,String itemCode) throws SQLException {
        InventoryCard inventorycard = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from inventorycard ").
                append("where warehouse = ? ").
                append("and itemcode = ? ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, warehouse.getIndex());
        pstm.setString(2, itemCode);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            inventorycard = new InventoryCard();
            inventorycard.setIndex(rs.getLong("autoindex"));

            inventorycard.setWarehouse(warehouse);

            inventorycard.setItemCode(itemCode);
            inventorycard.setItemName(rs.getString("itemname"));
            inventorycard.setItemsUnit(rs.getString("units"));
            inventorycard.setCardNumber(rs.getString("cardnumber"));
            inventorycard.setSpecification(rs.getString("spesification"));

        }

        rs.close();
        pstm.close();

        return inventorycard;
    }
    

    void insertThirdPartyItems(Connection conn, ThirdPartyItems thirdpartyitems) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into thirdpartyitems(itemscode, itemsname, specificationtype, specificationnumber, ").
                append("acquisitionyear, acquisitionway, thirdpartyname, itemsunit, condition, ").
                append("amount, price, description, unit)").
                append("values(?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, thirdpartyitems.getItemCode());
        pstm.setString(++col, thirdpartyitems.getItemName());
        pstm.setString(++col, thirdpartyitems.getSpecificationType());
        pstm.setString(++col, thirdpartyitems.getSpecificationNumber());
        pstm.setInt(++col, thirdpartyitems.getAcquisitionYear());
        pstm.setString(++col, thirdpartyitems.getAcquisitionWay());
        pstm.setString(++col, thirdpartyitems.getThirdPartyName());
        pstm.setString(++col, thirdpartyitems.getItemsUnit());
        pstm.setString(++col, thirdpartyitems.getCondition());
        pstm.setInt(++col, thirdpartyitems.getAmount());
        pstm.setBigDecimal(++col, thirdpartyitems.getPrice());
        pstm.setString(++col, thirdpartyitems.getDescription());

        if (thirdpartyitems.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, thirdpartyitems.getUnit().getIndex());
        }

        pstm.executeUpdate();
        pstm.close();
    }

    void updateThirdPartyItems(Connection conn, Long oldIndex, ThirdPartyItems thirdpartyitems) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update thirdpartyitems set itemscode=?, itemsname=?, specificationtype=?, specificationnumber=?,").
                append("acquisitionyear=?, acquisitionway=?, thirdpartyname=?, itemsunit=?,").
                append("condition=?, amount=?, price=?, description=?, unit=? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, thirdpartyitems.getItemCode());
        pstm.setString(++col, thirdpartyitems.getItemName());
        pstm.setString(++col, thirdpartyitems.getSpecificationType());
        pstm.setString(++col, thirdpartyitems.getSpecificationNumber());
        pstm.setInt(++col, thirdpartyitems.getAcquisitionYear());
        pstm.setString(++col, thirdpartyitems.getAcquisitionWay());
        pstm.setString(++col, thirdpartyitems.getThirdPartyName());
        pstm.setString(++col, thirdpartyitems.getItemsUnit());
        pstm.setString(++col, thirdpartyitems.getCondition());
        pstm.setInt(++col, thirdpartyitems.getAmount());
        pstm.setBigDecimal(++col, thirdpartyitems.getPrice());
        pstm.setString(++col, thirdpartyitems.getDescription());

        if (thirdpartyitems.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, thirdpartyitems.getUnit().getIndex());
        }

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteThirdPartyItems(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from thirdpartyitems where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ThirdPartyItems> getThirdPartyItems(Connection conn) throws SQLException {
        ArrayList<ThirdPartyItems> thirdpartyitemss = new ArrayList<ThirdPartyItems>();

        StringBuilder query = new StringBuilder();
        query.append("select * from thirdpartyitems ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ThirdPartyItems thirdpartyitems = new ThirdPartyItems();
            thirdpartyitems.setIndex(rs.getLong("autoindex"));
            thirdpartyitems.setItemCode(rs.getString("itemscode"));
            thirdpartyitems.setItemName(rs.getString("itemsname"));
            thirdpartyitems.setSpecificationType(rs.getString("specificationtype"));
            thirdpartyitems.setSpecificationNumber(rs.getString("specificationnumber"));
            thirdpartyitems.setAcquisitionYear(rs.getInt("acquisitionyear"));
            thirdpartyitems.setAcquisitionWay(rs.getString("acquisitionway"));
            thirdpartyitems.setThirdPartyName(rs.getString("thirdpartyname"));
            thirdpartyitems.setItemsUnit(rs.getString("itemsunit"));
            thirdpartyitems.setCondition(rs.getString("condition"));
            thirdpartyitems.setAmount(rs.getInt("amount"));
            thirdpartyitems.setPrice(rs.getBigDecimal("price"));
            thirdpartyitems.setDescription(rs.getString("description"));
            
            Unit unit = null;
            Long unitIndex = rs.getLong("unit");

            if (unitIndex != null && unitIndex.compareTo(Long.valueOf(0)) > 0) {
                unit = new Unit(unitIndex);
            }

            thirdpartyitems.setUnit(unit);

            thirdpartyitemss.add(thirdpartyitems);
        }

        rs.close();
        pstm.close();

        return thirdpartyitemss;
    }
    
    ArrayList<ThirdPartyItems> getThirdPartyItems(Connection conn,String modifier) throws SQLException {
        ArrayList<ThirdPartyItems> thirdpartyitemss = new ArrayList<ThirdPartyItems>();

        StringBuilder query = new StringBuilder();
        query.append("select * from thirdpartyitems ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ThirdPartyItems thirdpartyitems = new ThirdPartyItems();
            thirdpartyitems.setIndex(rs.getLong("autoindex"));
            thirdpartyitems.setItemCode(rs.getString("itemscode"));
            thirdpartyitems.setItemName(rs.getString("itemsname"));
            thirdpartyitems.setSpecificationType(rs.getString("specificationtype"));
            thirdpartyitems.setSpecificationNumber(rs.getString("specificationnumber"));
            thirdpartyitems.setAcquisitionYear(rs.getInt("acquisitionyear"));
            thirdpartyitems.setAcquisitionWay(rs.getString("acquisitionway"));
            thirdpartyitems.setThirdPartyName(rs.getString("thirdpartyname"));
            thirdpartyitems.setItemsUnit(rs.getString("itemsunit"));
            thirdpartyitems.setCondition(rs.getString("condition"));
            thirdpartyitems.setAmount(rs.getInt("amount"));
            thirdpartyitems.setPrice(rs.getBigDecimal("price"));
            thirdpartyitems.setDescription(rs.getString("description"));
            
            Unit unit = null;
            Long unitIndex = rs.getLong("unit");

            if (unitIndex != null && unitIndex.compareTo(Long.valueOf(0)) > 0) {
                unit = new Unit(unitIndex);
            }

            thirdpartyitems.setUnit(unit);

            thirdpartyitemss.add(thirdpartyitems);
        }

        rs.close();
        pstm.close();

        return thirdpartyitemss;
    }
    
    //
    
    void insertUnrecycledItems(Connection conn, UnrecycledItems unrecycledItems) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into unrecycledItems(receivedate, itemscode, itemsname, specification,").
                append("productionyear, receiveamount, contractdate, contractnumber,").
                append("documentnumber, documentdate, releasedate, submitted, releaseamount,").
                append("releasedocumentnumber, releasedocumentdate, description,unit)").
                append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;
        
        pstm.setDate(++col, new java.sql.Date(unrecycledItems.getReceiveDate().getTime()));

        pstm.setString(++col, unrecycledItems.getItemCode());
        pstm.setString(++col, unrecycledItems.getItemName());

        pstm.setString(++col, unrecycledItems.getSpecification());
        pstm.setInt(++col, unrecycledItems.getProductionYear());
        pstm.setInt(++col, unrecycledItems.getReceiveAmount());
        
        if (unrecycledItems.getContractDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(unrecycledItems.getContractDate().getTime()));
        }
        
        pstm.setString(++col, unrecycledItems.getContractNumber());
        pstm.setString(++col, unrecycledItems.getDocumentNumber());
        
        if (unrecycledItems.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(unrecycledItems.getDocumentDate().getTime()));
        }
        
        if (unrecycledItems.getReceiveDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(unrecycledItems.getReceiveDate().getTime()));
        }
        
        pstm.setString(++col, unrecycledItems.getSubmitted());
        pstm.setInt(++col, unrecycledItems.getReleaseAmount());
        
        pstm.setString(++col, unrecycledItems.getReleaseDocumentNumber());
        
        if (unrecycledItems.getReleaseDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(unrecycledItems.getReleaseDocumentDate().getTime()));
        }
        
        pstm.setString(++col, unrecycledItems.getDescription());
        
        if (unrecycledItems.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, unrecycledItems.getUnit().getIndex());
        }

        pstm.executeUpdate();
        pstm.close();
    }

    void updateUnrecycledItems(Connection conn, Long oldIndex, UnrecycledItems unrecycledItems) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update unrecycledItems set receivedate=?, itemscode=?, itemsname=?, specification=?,").
                append("productionyear=?, receiveamount=?, contractdate=?, contractnumber=?,  ").
                append("documentnumber=?, documentdate=?, releasedate=?, submitted=?,  ").
                append("releaseamount=?, releasedocumentnumber=?, releasedocumentdate=?,description=?, unit = ?  ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;
        
        pstm.setDate(++col, new java.sql.Date(unrecycledItems.getReceiveDate().getTime()));

        pstm.setString(++col, unrecycledItems.getItemCode());
        pstm.setString(++col, unrecycledItems.getItemName());

        pstm.setString(++col, unrecycledItems.getSpecification());
        pstm.setInt(++col, unrecycledItems.getProductionYear());
        pstm.setInt(++col, unrecycledItems.getReceiveAmount());
        
        if (unrecycledItems.getContractDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(unrecycledItems.getContractDate().getTime()));
        }
        
        pstm.setString(++col, unrecycledItems.getContractNumber());
        pstm.setString(++col, unrecycledItems.getDocumentNumber());
        
        if (unrecycledItems.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(unrecycledItems.getDocumentDate().getTime()));
        }
        
        if (unrecycledItems.getReceiveDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(unrecycledItems.getReceiveDate().getTime()));
        }
        
        pstm.setString(++col, unrecycledItems.getSubmitted());
        pstm.setInt(++col, unrecycledItems.getReleaseAmount());
        
        pstm.setString(++col, unrecycledItems.getReleaseDocumentNumber());
        
        if (unrecycledItems.getReleaseDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(unrecycledItems.getReleaseDocumentDate().getTime()));
        }
        
        pstm.setString(++col, unrecycledItems.getDescription());
        
        if (unrecycledItems.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, unrecycledItems.getUnit().getIndex());
        }

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteUnrecycledItems(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from unrecycledItems where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<UnrecycledItems> getUnrecycledItems(Connection conn,String modifier) throws SQLException {
        ArrayList<UnrecycledItems> unrecycledItemss = new ArrayList<UnrecycledItems>();

        StringBuilder query = new StringBuilder();
        query.append("select * from unrecycledItems ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Long unitIndex = rs.getLong("unit");

            Unit unit = null;

            if (unitIndex != null && !unitIndex.equals(Long.valueOf(0))) {
                unit = new Unit(unitIndex);
            }
            
            UnrecycledItems unrecycledItems = new UnrecycledItems();
            unrecycledItems.setIndex(rs.getLong("autoindex"));
            unrecycledItems.setReceiveDate(rs.getDate("receivedate"));
            unrecycledItems.setItemCode(rs.getString("itemscode"));
            unrecycledItems.setItemName(rs.getString("itemsname"));
            unrecycledItems.setSpecification(rs.getString("specification"));
            unrecycledItems.setProductionYear(rs.getInt("productionyear"));
            unrecycledItems.setReceiveAmount(rs.getInt("receiveamount"));
            unrecycledItems.setContractDate(rs.getDate("contractdate"));
            unrecycledItems.setContractNumber(rs.getString("contractnumber"));
            unrecycledItems.setDocumentDate(rs.getDate("documentdate"));
            unrecycledItems.setDocumentNumber(rs.getString("documentnumber"));
            unrecycledItems.setReleaseDate(rs.getDate("releasedate"));
            unrecycledItems.setSubmitted(rs.getString("submitted"));
            unrecycledItems.setReleaseAmount(rs.getInt("releaseamount"));
            unrecycledItems.setReleaseDocumentDate(rs.getDate("releasedocumentdate"));
            unrecycledItems.setReleaseDocumentNumber(rs.getString("releasedocumentnumber"));

            unrecycledItems.setDescription(rs.getString("description"));
            
            unrecycledItems.setUnit(unit);

            unrecycledItemss.add(unrecycledItems);
        }

        rs.close();
        pstm.close();

        return unrecycledItemss;
    }
}
