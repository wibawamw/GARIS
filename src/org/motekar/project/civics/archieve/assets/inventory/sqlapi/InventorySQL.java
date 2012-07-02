package org.motekar.project.civics.archieve.assets.inventory.sqlapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.motekar.project.civics.archieve.assets.inventory.objects.DeleteDraftItems;
import org.motekar.project.civics.archieve.assets.inventory.objects.InventoryRekap;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsBuilding;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsConstruction;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAsset;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsFixedAssetRoom;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsLand;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachine;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsNetwork;
import org.motekar.project.civics.archieve.assets.inventory.objects.ItemsMachineRoom;
import org.motekar.project.civics.archieve.assets.inventory.objects.UsedItems;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Room;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.sqlapi.CommonSQL;

/**
 *
 * @author Muhamad Wibawa
 */
public class InventorySQL extends CommonSQL {

    void insertItemsMachine(Connection conn, ItemsMachine machine) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into itemsmachine(unit,itemcode,itemname,regnumber,bpkbnumber,").
                append("machinetype,volume,factorynumber,fabricnumber,machinenumber,policenumber,").
                append("acquisitionyear,yearbuilt,location,presentlocation,material,").
                append("condition,price,admincost,fundingsources,acquisitionway,").
                append("description,username,lastusername)").
                append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        if (machine.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, machine.getUnit().getIndex());
        }

        pstm.setString(++col, machine.getItemCode());
        pstm.setString(++col, machine.getItemName());
        pstm.setString(++col, machine.getRegNumber());
        pstm.setString(++col, machine.getBpkbNumber());
        pstm.setString(++col, machine.getMachineType());
        pstm.setInt(++col, machine.getVolume());
        pstm.setString(++col, machine.getFactoryNumber());
        pstm.setString(++col, machine.getFabricNumber());
        pstm.setString(++col, machine.getMachineNumber());
        pstm.setString(++col, machine.getPoliceNumber());
        if (machine.getAcquisitionYear() == null) {
            pstm.setNull(++col, Types.INTEGER);
        } else {
            pstm.setInt(++col, machine.getAcquisitionYear());
        }

        if (machine.getYearBuilt() == null) {
            pstm.setNull(++col, Types.INTEGER);
        } else {
            pstm.setInt(++col, machine.getYearBuilt());
        }
        
        pstm.setString(++col, machine.getLocation());
        pstm.setString(++col, machine.getPresentLocation());
        pstm.setString(++col, machine.getMaterial());

        if (machine.getCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, machine.getCondition().getIndex());
        }

        pstm.setBigDecimal(++col, machine.getPrice());
        pstm.setBigDecimal(++col, machine.getAdminCost());
        pstm.setString(++col, machine.getFundingSource());
        pstm.setString(++col, machine.getAcquisitionWay());
        pstm.setString(++col, machine.getDescription());
        pstm.setString(++col, machine.getUserName());
        pstm.setString(++col, machine.getLastUserName());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateItemsMachine(Connection conn, Long oldIndex, ItemsMachine machine) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update itemsmachine set unit = ?,itemcode = ?,itemname = ?,regnumber = ?,bpkbnumber = ?, ").
                append("machinetype = ?,volume = ?,factorynumber = ?,fabricnumber = ?,machinenumber = ?,policenumber = ?,").
                append("acquisitionyear = ?,yearbuilt = ?,location = ?,presentlocation = ?,material = ?,").
                append("condition = ?,price = ?, admincost = ?,fundingsources = ?,acquisitionway = ?,").
                append("description = ?,lastusername = ?").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        if (machine.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, machine.getUnit().getIndex());
        }

        pstm.setString(++col, machine.getItemCode());
        pstm.setString(++col, machine.getItemName());
        pstm.setString(++col, machine.getRegNumber());
        pstm.setString(++col, machine.getBpkbNumber());
        pstm.setString(++col, machine.getMachineType());
        pstm.setInt(++col, machine.getVolume());
        pstm.setString(++col, machine.getFactoryNumber());
        pstm.setString(++col, machine.getFabricNumber());
        pstm.setString(++col, machine.getMachineNumber());
        pstm.setString(++col, machine.getPoliceNumber());
        if (machine.getAcquisitionYear() == null) {
            pstm.setNull(++col, Types.INTEGER);
        } else {
            pstm.setInt(++col, machine.getAcquisitionYear());
        }
        if (machine.getYearBuilt() == null) {
            pstm.setNull(++col, Types.INTEGER);
        } else {
            pstm.setInt(++col, machine.getYearBuilt());
        }
        pstm.setString(++col, machine.getLocation());
        pstm.setString(++col, machine.getPresentLocation());
        pstm.setString(++col, machine.getMaterial());

        if (machine.getCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, machine.getCondition().getIndex());
        }

        pstm.setBigDecimal(++col, machine.getPrice());
        pstm.setBigDecimal(++col, machine.getAdminCost());
        pstm.setString(++col, machine.getFundingSource());
        pstm.setString(++col, machine.getAcquisitionWay());
        pstm.setString(++col, machine.getDescription());
        pstm.setString(++col, machine.getLastUserName());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsMachine(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsmachine where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }
    
    void deleteItemsMachine(Connection conn, Unit unit) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsmachine where unit = ?");
        pstm.setLong(1, unit.getIndex());
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsMachine(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsmachine where itemcode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ItemsMachine> getItemsMachine(Connection conn) throws SQLException {
        ArrayList<ItemsMachine> machines = new ArrayList<ItemsMachine>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsmachine");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            ItemsMachine machine = new ItemsMachine();
            machine.setIndex(rs.getLong("autoindex"));
            machine.setUnit(unit);
            machine.setCondition(condition);
            machine.setItemCode(rs.getString("itemcode"));
            machine.setItemName(rs.getString("itemname"));
            machine.setRegNumber(rs.getString("regnumber"));
            machine.setBpkbNumber(rs.getString("bpkbnumber"));
            machine.setMachineType(rs.getString("machinetype"));
            machine.setVolume(rs.getInt("volume"));
            machine.setFactoryNumber(rs.getString("factorynumber"));
            machine.setFabricNumber(rs.getString("fabricnumber"));
            machine.setMachineNumber(rs.getString("machinenumber"));
            machine.setPoliceNumber(rs.getString("policenumber"));

            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }

            machine.setAcquisitionYear(acquisitionYear);

            Integer yearBuilt = rs.getInt("yearbuilt");

            if (yearBuilt != null) {
                if (yearBuilt.equals(Integer.valueOf(0))) {
                    yearBuilt = null;
                }
            }

            machine.setYearBuilt(yearBuilt);
            
            machine.setLocation(rs.getString("location"));
            machine.setPresentLocation(rs.getString("presentlocation"));
            machine.setMaterial(rs.getString("material"));
            machine.setPrice(rs.getBigDecimal("price"));
            machine.setAdminCost(rs.getBigDecimal("admincost"));
            machine.setFundingSource(rs.getString("fundingsources"));
            machine.setAcquisitionWay(rs.getString("acquisitionway"));
            machine.setDescription(rs.getString("description"));
            machine.setUserName(rs.getString("username"));
            machine.setLastUserName(rs.getString("lastusername"));

            machines.add(machine);
        }

        rs.close();
        pstm.close();

        return machines;
    }

    ArrayList<ItemsMachine> getItemsMachine(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsMachine> machines = new ArrayList<ItemsMachine>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsmachine ").append(modifier).
                append(" order by autoindex");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            ItemsMachine machine = new ItemsMachine();
            machine.setIndex(rs.getLong("autoindex"));
            machine.setUnit(unit);
            machine.setCondition(condition);
            machine.setItemCode(rs.getString("itemcode"));
            machine.setItemName(rs.getString("itemname"));
            machine.setRegNumber(rs.getString("regnumber"));
            machine.setBpkbNumber(rs.getString("bpkbnumber"));
            machine.setMachineType(rs.getString("machinetype"));
            machine.setVolume(rs.getInt("volume"));
            machine.setFactoryNumber(rs.getString("factorynumber"));
            machine.setFabricNumber(rs.getString("fabricnumber"));
            machine.setMachineNumber(rs.getString("machinenumber"));
            machine.setPoliceNumber(rs.getString("policenumber"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }

            machine.setAcquisitionYear(acquisitionYear);
            Integer yearBuilt = rs.getInt("yearbuilt");

            if (yearBuilt != null) {
                if (yearBuilt.equals(Integer.valueOf(0))) {
                    yearBuilt = null;
                }
            }

            machine.setYearBuilt(yearBuilt);
            machine.setLocation(rs.getString("location"));
            machine.setPresentLocation(rs.getString("presentlocation"));
            machine.setMaterial(rs.getString("material"));
            machine.setPrice(rs.getBigDecimal("price"));
            machine.setAdminCost(rs.getBigDecimal("admincost"));
            machine.setFundingSource(rs.getString("fundingsources"));
            machine.setAcquisitionWay(rs.getString("acquisitionway"));
            machine.setDescription(rs.getString("description"));
            machine.setUserName(rs.getString("username"));
            machine.setLastUserName(rs.getString("lastusername"));

            machines.add(machine);
        }

        rs.close();
        pstm.close();

        return machines;
    }

    ArrayList<ItemsMachine> getItemsMachineNotInRoomInventory(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsMachine> machines = new ArrayList<ItemsMachine>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsmachine ").append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select items from itemsmachineroom) ");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select items from itemsmachineroom) ");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            ItemsMachine machine = new ItemsMachine();
            machine.setIndex(rs.getLong("autoindex"));
            machine.setUnit(unit);
            machine.setCondition(condition);
            machine.setItemCode(rs.getString("itemcode"));
            machine.setItemName(rs.getString("itemname"));
            machine.setRegNumber(rs.getString("regnumber"));
            machine.setBpkbNumber(rs.getString("bpkbnumber"));
            machine.setMachineType(rs.getString("machinetype"));
            machine.setVolume(rs.getInt("volume"));
            machine.setFactoryNumber(rs.getString("factorynumber"));
            machine.setFabricNumber(rs.getString("fabricnumber"));
            machine.setMachineNumber(rs.getString("machinenumber"));
            machine.setPoliceNumber(rs.getString("policenumber"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            machine.setAcquisitionYear(acquisitionYear);
            Integer yearBuilt = rs.getInt("yearbuilt");

            if (yearBuilt != null) {
                if (yearBuilt.equals(Integer.valueOf(0))) {
                    yearBuilt = null;
                }
            }

            machine.setYearBuilt(yearBuilt);
            machine.setLocation(rs.getString("location"));
            machine.setPresentLocation(rs.getString("presentlocation"));
            machine.setMaterial(rs.getString("material"));
            machine.setPrice(rs.getBigDecimal("price"));
            machine.setAdminCost(rs.getBigDecimal("admincost"));
            machine.setFundingSource(rs.getString("fundingsources"));
            machine.setAcquisitionWay(rs.getString("acquisitionway"));
            machine.setDescription(rs.getString("description"));
            machine.setUserName(rs.getString("username"));
            machine.setLastUserName(rs.getString("lastusername"));

            machines.add(machine);
        }

        rs.close();
        pstm.close();

        return machines;
    }

    ArrayList<ItemsMachine> getItemsMachineNotInDeleteDraft(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsMachine> machines = new ArrayList<ItemsMachine>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsmachine ").append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select itemsindex from deletedraftitems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_MACHINE).
                    append(")");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select itemsindex from deletedraftitems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_MACHINE).
                    append(")");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            ItemsMachine machine = new ItemsMachine();
            machine.setIndex(rs.getLong("autoindex"));
            machine.setUnit(unit);
            machine.setCondition(condition);
            machine.setItemCode(rs.getString("itemcode"));
            machine.setItemName(rs.getString("itemname"));
            machine.setRegNumber(rs.getString("regnumber"));
            machine.setBpkbNumber(rs.getString("bpkbnumber"));
            machine.setMachineType(rs.getString("machinetype"));
            machine.setVolume(rs.getInt("volume"));
            machine.setFactoryNumber(rs.getString("factorynumber"));
            machine.setFabricNumber(rs.getString("fabricnumber"));
            machine.setMachineNumber(rs.getString("machinenumber"));
            machine.setPoliceNumber(rs.getString("policenumber"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            machine.setAcquisitionYear(acquisitionYear);
            Integer yearBuilt = rs.getInt("yearbuilt");

            if (yearBuilt != null) {
                if (yearBuilt.equals(Integer.valueOf(0))) {
                    yearBuilt = null;
                }
            }

            machine.setYearBuilt(yearBuilt);
            machine.setLocation(rs.getString("location"));
            machine.setPresentLocation(rs.getString("presentlocation"));
            machine.setMaterial(rs.getString("material"));
            machine.setPrice(rs.getBigDecimal("price"));
            machine.setAdminCost(rs.getBigDecimal("admincost"));
            machine.setFundingSource(rs.getString("fundingsources"));
            machine.setAcquisitionWay(rs.getString("acquisitionway"));
            machine.setDescription(rs.getString("description"));
            machine.setUserName(rs.getString("username"));
            machine.setLastUserName(rs.getString("lastusername"));

            machines.add(machine);
        }

        rs.close();
        pstm.close();

        return machines;
    }

    ArrayList<ItemsMachine> getItemsMachineNotInUsedItems(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsMachine> machines = new ArrayList<ItemsMachine>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsmachine ").append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select itemsindex from useditems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_MACHINE).
                    append(")");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select itemsindex from useditems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_MACHINE).
                    append(")");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            ItemsMachine machine = new ItemsMachine();
            machine.setIndex(rs.getLong("autoindex"));
            machine.setUnit(unit);
            machine.setCondition(condition);
            machine.setItemCode(rs.getString("itemcode"));
            machine.setItemName(rs.getString("itemname"));
            machine.setRegNumber(rs.getString("regnumber"));
            machine.setBpkbNumber(rs.getString("bpkbnumber"));
            machine.setMachineType(rs.getString("machinetype"));
            machine.setVolume(rs.getInt("volume"));
            machine.setFactoryNumber(rs.getString("factorynumber"));
            machine.setFabricNumber(rs.getString("fabricnumber"));
            machine.setMachineNumber(rs.getString("machinenumber"));
            machine.setPoliceNumber(rs.getString("policenumber"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            machine.setAcquisitionYear(acquisitionYear);
            Integer yearBuilt = rs.getInt("yearbuilt");

            if (yearBuilt != null) {
                if (yearBuilt.equals(Integer.valueOf(0))) {
                    yearBuilt = null;
                }
            }

            machine.setYearBuilt(yearBuilt);
            machine.setLocation(rs.getString("location"));
            machine.setPresentLocation(rs.getString("presentlocation"));
            machine.setMaterial(rs.getString("material"));
            machine.setPrice(rs.getBigDecimal("price"));
            machine.setAdminCost(rs.getBigDecimal("admincost"));
            machine.setFundingSource(rs.getString("fundingsources"));
            machine.setAcquisitionWay(rs.getString("acquisitionway"));
            machine.setDescription(rs.getString("description"));
            machine.setUserName(rs.getString("username"));
            machine.setLastUserName(rs.getString("lastusername"));

            machines.add(machine);
        }

        rs.close();
        pstm.close();

        return machines;
    }

    ItemsMachine getItemsMachine(Connection conn, Long index) throws SQLException {
        ItemsMachine machine = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsmachine ").
                append(" where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            machine = new ItemsMachine();
            machine.setIndex(rs.getLong("autoindex"));
            machine.setUnit(unit);
            machine.setCondition(condition);
            machine.setItemCode(rs.getString("itemcode"));
            machine.setItemName(rs.getString("itemname"));
            machine.setRegNumber(rs.getString("regnumber"));
            machine.setBpkbNumber(rs.getString("bpkbnumber"));
            machine.setMachineType(rs.getString("machinetype"));
            machine.setVolume(rs.getInt("volume"));
            machine.setFactoryNumber(rs.getString("factorynumber"));
            machine.setFabricNumber(rs.getString("fabricnumber"));
            machine.setMachineNumber(rs.getString("machinenumber"));
            machine.setPoliceNumber(rs.getString("policenumber"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            machine.setAcquisitionYear(acquisitionYear);
            Integer yearBuilt = rs.getInt("yearbuilt");

            if (yearBuilt != null) {
                if (yearBuilt.equals(Integer.valueOf(0))) {
                    yearBuilt = null;
                }
            }

            machine.setYearBuilt(yearBuilt);
            machine.setLocation(rs.getString("location"));
            machine.setPresentLocation(rs.getString("presentlocation"));
            machine.setMaterial(rs.getString("material"));
            machine.setPrice(rs.getBigDecimal("price"));
            machine.setAdminCost(rs.getBigDecimal("admincost"));
            machine.setFundingSource(rs.getString("fundingsources"));
            machine.setAcquisitionWay(rs.getString("acquisitionway"));
            machine.setDescription(rs.getString("description"));
            machine.setUserName(rs.getString("username"));
            machine.setLastUserName(rs.getString("lastusername"));

        }

        rs.close();
        pstm.close();

        return machine;
    }

    void insertItemsFixedAsset(Connection conn, ItemsFixedAsset fixedAsset) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into itemsfixedasset(unit,itemcode,itemname,regnumber,bookauthor,").
                append("bookspec,artregion,artauthor,artmaterial,cattletype,cattlesize,").
                append("price,acquisitionyear,condition,fundingsources,acquisitionway,").
                append("description,username,lastusername)").
                append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        if (fixedAsset.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, fixedAsset.getUnit().getIndex());
        }

        pstm.setString(++col, fixedAsset.getItemCode());
        pstm.setString(++col, fixedAsset.getItemName());
        pstm.setString(++col, fixedAsset.getRegNumber());
        pstm.setString(++col, fixedAsset.getBookAuthor());
        pstm.setString(++col, fixedAsset.getBookSpec());
        pstm.setString(++col, fixedAsset.getArtRegion());
        pstm.setString(++col, fixedAsset.getArtAuthor());
        pstm.setString(++col, fixedAsset.getArtMaterial());
        pstm.setString(++col, fixedAsset.getCattleType());
        pstm.setString(++col, fixedAsset.getCattleSize());

        pstm.setBigDecimal(++col, fixedAsset.getPrice());
        if (fixedAsset.getAcquisitionYear() == null) {
            pstm.setNull(++col, Types.INTEGER);
        } else {
            pstm.setInt(++col, fixedAsset.getAcquisitionYear());
        }

        if (fixedAsset.getCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, fixedAsset.getCondition().getIndex());
        }

        pstm.setString(++col, fixedAsset.getFundingSource());
        pstm.setString(++col, fixedAsset.getAcquisitionWay());
        pstm.setString(++col, fixedAsset.getDescription());
        pstm.setString(++col, fixedAsset.getUserName());
        pstm.setString(++col, fixedAsset.getLastUserName());


        pstm.executeUpdate();
        pstm.close();
    }

    void updateItemsFixedAsset(Connection conn, Long oldIndex, ItemsFixedAsset fixedAsset) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update itemsfixedasset set unit= ?,itemcode = ?,itemname = ?,regnumber = ?,bookauthor = ?,").
                append("bookspec = ? ,artregion = ?,artauthor = ?,artmaterial = ?,cattletype = ?,cattlesize = ?,").
                append("price = ?,acquisitionyear = ?,condition = ?,fundingsources = ?,acquisitionway = ?,").
                append("description = ?,lastusername = ?").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        if (fixedAsset.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, fixedAsset.getUnit().getIndex());
        }

        pstm.setString(++col, fixedAsset.getItemCode());
        pstm.setString(++col, fixedAsset.getItemName());
        pstm.setString(++col, fixedAsset.getRegNumber());
        pstm.setString(++col, fixedAsset.getBookAuthor());
        pstm.setString(++col, fixedAsset.getBookSpec());
        pstm.setString(++col, fixedAsset.getArtRegion());
        pstm.setString(++col, fixedAsset.getArtAuthor());
        pstm.setString(++col, fixedAsset.getArtMaterial());
        pstm.setString(++col, fixedAsset.getCattleType());
        pstm.setString(++col, fixedAsset.getCattleSize());

        pstm.setBigDecimal(++col, fixedAsset.getPrice());
        if (fixedAsset.getAcquisitionYear() == null) {
            pstm.setNull(++col, Types.INTEGER);
        } else {
            pstm.setInt(++col, fixedAsset.getAcquisitionYear());
        }

        if (fixedAsset.getCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, fixedAsset.getCondition().getIndex());
        }

        pstm.setString(++col, fixedAsset.getFundingSource());
        pstm.setString(++col, fixedAsset.getAcquisitionWay());
        pstm.setString(++col, fixedAsset.getDescription());
        pstm.setString(++col, fixedAsset.getLastUserName());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsFixedAsset(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsfixedasset where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }
    
    void deleteItemsFixedAsset(Connection conn, Unit unit) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsfixedasset where unit = ?");
        pstm.setLong(1, unit.getIndex());
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsFixedAsset(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsfixedasset where itemcode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ItemsFixedAsset> getItemsFixedAsset(Connection conn) throws SQLException {
        ArrayList<ItemsFixedAsset> fixedAssets = new ArrayList<ItemsFixedAsset>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsfixedasset");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            ItemsFixedAsset fixedAsset = new ItemsFixedAsset();
            fixedAsset.setIndex(rs.getLong("autoindex"));
            fixedAsset.setUnit(unit);
            fixedAsset.setCondition(condition);
            fixedAsset.setItemCode(rs.getString("itemcode"));
            fixedAsset.setItemName(rs.getString("itemname"));
            fixedAsset.setRegNumber(rs.getString("regnumber"));
            fixedAsset.setBookAuthor(rs.getString("bookauthor"));
            fixedAsset.setBookSpec(rs.getString("bookspec"));
            fixedAsset.setArtRegion(rs.getString("artregion"));
            fixedAsset.setArtAuthor(rs.getString("artauthor"));
            fixedAsset.setArtMaterial(rs.getString("artmaterial"));
            fixedAsset.setCattleType(rs.getString("cattletype"));
            fixedAsset.setCattleSize(rs.getString("cattlesize"));

            fixedAsset.setPrice(rs.getBigDecimal("price"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            fixedAsset.setAcquisitionYear(acquisitionYear);
            fixedAsset.setFundingSource(rs.getString("fundingsources"));
            fixedAsset.setAcquisitionWay(rs.getString("acquisitionway"));
            fixedAsset.setDescription(rs.getString("description"));
            fixedAsset.setUserName(rs.getString("username"));
            fixedAsset.setLastUserName(rs.getString("lastusername"));

            fixedAssets.add(fixedAsset);
        }

        rs.close();
        pstm.close();

        return fixedAssets;
    }

    ArrayList<ItemsFixedAsset> getItemsFixedAsset(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsFixedAsset> fixedAssets = new ArrayList<ItemsFixedAsset>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsfixedasset ").
                append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            ItemsFixedAsset fixedAsset = new ItemsFixedAsset();
            fixedAsset.setIndex(rs.getLong("autoindex"));
            fixedAsset.setUnit(unit);
            fixedAsset.setCondition(condition);
            fixedAsset.setItemCode(rs.getString("itemcode"));
            fixedAsset.setItemName(rs.getString("itemname"));
            fixedAsset.setRegNumber(rs.getString("regnumber"));
            fixedAsset.setBookAuthor(rs.getString("bookauthor"));
            fixedAsset.setBookSpec(rs.getString("bookspec"));
            fixedAsset.setArtRegion(rs.getString("artregion"));
            fixedAsset.setArtAuthor(rs.getString("artauthor"));
            fixedAsset.setArtMaterial(rs.getString("artmaterial"));
            fixedAsset.setCattleType(rs.getString("cattletype"));
            fixedAsset.setCattleSize(rs.getString("cattlesize"));

            fixedAsset.setPrice(rs.getBigDecimal("price"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            fixedAsset.setAcquisitionYear(acquisitionYear);
            fixedAsset.setFundingSource(rs.getString("fundingsources"));
            fixedAsset.setAcquisitionWay(rs.getString("acquisitionway"));
            fixedAsset.setDescription(rs.getString("description"));
            fixedAsset.setUserName(rs.getString("username"));
            fixedAsset.setLastUserName(rs.getString("lastusername"));

            fixedAssets.add(fixedAsset);
        }

        rs.close();
        pstm.close();

        return fixedAssets;
    }

    ArrayList<ItemsFixedAsset> getItemsFixedAssetNotInRoomInventory(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsFixedAsset> fixedAssets = new ArrayList<ItemsFixedAsset>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsfixedasset ").
                append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select items from itemsfixedassetroom) ");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select items from itemsfixedassetroom) ");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            ItemsFixedAsset fixedAsset = new ItemsFixedAsset();
            fixedAsset.setIndex(rs.getLong("autoindex"));
            fixedAsset.setUnit(unit);
            fixedAsset.setCondition(condition);
            fixedAsset.setItemCode(rs.getString("itemcode"));
            fixedAsset.setItemName(rs.getString("itemname"));
            fixedAsset.setRegNumber(rs.getString("regnumber"));
            fixedAsset.setBookAuthor(rs.getString("bookauthor"));
            fixedAsset.setBookSpec(rs.getString("bookspec"));
            fixedAsset.setArtRegion(rs.getString("artregion"));
            fixedAsset.setArtAuthor(rs.getString("artauthor"));
            fixedAsset.setArtMaterial(rs.getString("artmaterial"));
            fixedAsset.setCattleType(rs.getString("cattletype"));
            fixedAsset.setCattleSize(rs.getString("cattlesize"));

            fixedAsset.setPrice(rs.getBigDecimal("price"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            fixedAsset.setAcquisitionYear(acquisitionYear);
            fixedAsset.setFundingSource(rs.getString("fundingsources"));
            fixedAsset.setAcquisitionWay(rs.getString("acquisitionway"));
            fixedAsset.setDescription(rs.getString("description"));
            fixedAsset.setUserName(rs.getString("username"));
            fixedAsset.setLastUserName(rs.getString("lastusername"));

            fixedAssets.add(fixedAsset);
        }

        rs.close();
        pstm.close();

        return fixedAssets;
    }

    ArrayList<ItemsFixedAsset> getItemsFixedAssetNotInDeleteDraft(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsFixedAsset> fixedAssets = new ArrayList<ItemsFixedAsset>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsfixedasset ").
                append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select itemsindex from deletedraftitems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_FIXED_ASSET).
                    append(")");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select itemsindex from deletedraftitems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_FIXED_ASSET).
                    append(")");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            ItemsFixedAsset fixedAsset = new ItemsFixedAsset();
            fixedAsset.setIndex(rs.getLong("autoindex"));
            fixedAsset.setUnit(unit);
            fixedAsset.setCondition(condition);
            fixedAsset.setItemCode(rs.getString("itemcode"));
            fixedAsset.setItemName(rs.getString("itemname"));
            fixedAsset.setRegNumber(rs.getString("regnumber"));
            fixedAsset.setBookAuthor(rs.getString("bookauthor"));
            fixedAsset.setBookSpec(rs.getString("bookspec"));
            fixedAsset.setArtRegion(rs.getString("artregion"));
            fixedAsset.setArtAuthor(rs.getString("artauthor"));
            fixedAsset.setArtMaterial(rs.getString("artmaterial"));
            fixedAsset.setCattleType(rs.getString("cattletype"));
            fixedAsset.setCattleSize(rs.getString("cattlesize"));

            fixedAsset.setPrice(rs.getBigDecimal("price"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            fixedAsset.setAcquisitionYear(acquisitionYear);
            fixedAsset.setFundingSource(rs.getString("fundingsources"));
            fixedAsset.setAcquisitionWay(rs.getString("acquisitionway"));
            fixedAsset.setDescription(rs.getString("description"));
            fixedAsset.setUserName(rs.getString("username"));
            fixedAsset.setLastUserName(rs.getString("lastusername"));

            fixedAssets.add(fixedAsset);
        }

        rs.close();
        pstm.close();

        return fixedAssets;
    }

    ArrayList<ItemsFixedAsset> getItemsFixedAssetNotInUsedItems(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsFixedAsset> fixedAssets = new ArrayList<ItemsFixedAsset>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsfixedasset ").
                append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select itemsindex from useditems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_FIXED_ASSET).
                    append(")");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select itemsindex from useditems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_FIXED_ASSET).
                    append(")");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            ItemsFixedAsset fixedAsset = new ItemsFixedAsset();
            fixedAsset.setIndex(rs.getLong("autoindex"));
            fixedAsset.setUnit(unit);
            fixedAsset.setCondition(condition);
            fixedAsset.setItemCode(rs.getString("itemcode"));
            fixedAsset.setItemName(rs.getString("itemname"));
            fixedAsset.setRegNumber(rs.getString("regnumber"));
            fixedAsset.setBookAuthor(rs.getString("bookauthor"));
            fixedAsset.setBookSpec(rs.getString("bookspec"));
            fixedAsset.setArtRegion(rs.getString("artregion"));
            fixedAsset.setArtAuthor(rs.getString("artauthor"));
            fixedAsset.setArtMaterial(rs.getString("artmaterial"));
            fixedAsset.setCattleType(rs.getString("cattletype"));
            fixedAsset.setCattleSize(rs.getString("cattlesize"));

            fixedAsset.setPrice(rs.getBigDecimal("price"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            fixedAsset.setAcquisitionYear(acquisitionYear);
            fixedAsset.setFundingSource(rs.getString("fundingsources"));
            fixedAsset.setAcquisitionWay(rs.getString("acquisitionway"));
            fixedAsset.setDescription(rs.getString("description"));
            fixedAsset.setUserName(rs.getString("username"));
            fixedAsset.setLastUserName(rs.getString("lastusername"));

            fixedAssets.add(fixedAsset);
        }

        rs.close();
        pstm.close();

        return fixedAssets;
    }

    ItemsFixedAsset getItemsFixedAsset(Connection conn, Long index) throws SQLException {
        ItemsFixedAsset fixedAsset = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsfixedasset ").
                append(" where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");

            Unit unit = null;
            Condition condition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            fixedAsset = new ItemsFixedAsset();
            fixedAsset.setIndex(rs.getLong("autoindex"));
            fixedAsset.setUnit(unit);
            fixedAsset.setCondition(condition);
            fixedAsset.setItemCode(rs.getString("itemcode"));
            fixedAsset.setItemName(rs.getString("itemname"));
            fixedAsset.setRegNumber(rs.getString("regnumber"));
            fixedAsset.setBookAuthor(rs.getString("bookauthor"));
            fixedAsset.setBookSpec(rs.getString("bookspec"));
            fixedAsset.setArtRegion(rs.getString("artregion"));
            fixedAsset.setArtAuthor(rs.getString("artauthor"));
            fixedAsset.setArtMaterial(rs.getString("artmaterial"));
            fixedAsset.setCattleType(rs.getString("cattletype"));
            fixedAsset.setCattleSize(rs.getString("cattlesize"));

            fixedAsset.setPrice(rs.getBigDecimal("price"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            fixedAsset.setAcquisitionYear(acquisitionYear);
            fixedAsset.setFundingSource(rs.getString("fundingsources"));
            fixedAsset.setAcquisitionWay(rs.getString("acquisitionway"));
            fixedAsset.setDescription(rs.getString("description"));
            fixedAsset.setUserName(rs.getString("username"));
            fixedAsset.setLastUserName(rs.getString("lastusername"));

        }

        rs.close();
        pstm.close();

        return fixedAsset;
    }

    void insertItemsConstruction(Connection conn, ItemsConstruction itemsConstruction) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into itemsconstruction(unit,itemcode,itemname,regnumber,documentnumber,").
                append("documentdate,buildingcategory,israised,framework,buildingwide,worktype,price,").
                append("addresslocation,urban,suburban,startdate,landstatus,landcode,fundingsources,").
                append("description,username,lastusername)").
                append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        if (itemsConstruction.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsConstruction.getUnit().getIndex());
        }

        pstm.setString(++col, itemsConstruction.getItemCode());
        pstm.setString(++col, itemsConstruction.getItemName());
        pstm.setString(++col, itemsConstruction.getRegNumber());
        pstm.setString(++col, itemsConstruction.getDocumentNumber());

        if (itemsConstruction.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsConstruction.getDocumentDate().getTime()));
        }

        pstm.setString(++col, itemsConstruction.getBuildingCategory());
        pstm.setString(++col, itemsConstruction.getRaised());
        pstm.setString(++col, itemsConstruction.getFrameWork());
        pstm.setBigDecimal(++col, itemsConstruction.getBuildingWide());
        pstm.setString(++col, itemsConstruction.getWorkType());
        pstm.setBigDecimal(++col, itemsConstruction.getPrice());
        pstm.setString(++col, itemsConstruction.getAddressLocation());

        if (itemsConstruction.getUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsConstruction.getUrban().getIndex());
        }

        if (itemsConstruction.getSubUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsConstruction.getSubUrban().getIndex());
        }

        if (itemsConstruction.getStartDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsConstruction.getStartDate().getTime()));
        }

        pstm.setString(++col, itemsConstruction.getLandStatus());
        pstm.setString(++col, itemsConstruction.getLandCode());
        pstm.setString(++col, itemsConstruction.getFundingSource());
        pstm.setString(++col, itemsConstruction.getDescription());
        pstm.setString(++col, itemsConstruction.getUserName());
        pstm.setString(++col, itemsConstruction.getLastUserName());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateItemsConstruction(Connection conn, Long oldIndex, ItemsConstruction itemsConstruction) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update itemsconstruction set unit = ?,itemcode = ?,itemname = ?,regnumber = ?,documentnumber = ?,").
                append("documentdate = ?,buildingcategory = ?,israised = ?,framework = ?,buildingwide = ?,worktype = ?,price = ?,").
                append("addresslocation = ?,urban = ?,suburban = ?,startdate = ?,landstatus = ?,landcode = ?,fundingsources = ?,").
                append("description = ?,lastusername = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        if (itemsConstruction.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsConstruction.getUnit().getIndex());
        }

        pstm.setString(++col, itemsConstruction.getItemCode());
        pstm.setString(++col, itemsConstruction.getItemName());
        pstm.setString(++col, itemsConstruction.getRegNumber());
        pstm.setString(++col, itemsConstruction.getDocumentNumber());

        if (itemsConstruction.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsConstruction.getDocumentDate().getTime()));
        }

        pstm.setString(++col, itemsConstruction.getBuildingCategory());
        pstm.setString(++col, itemsConstruction.getRaised());
        pstm.setString(++col, itemsConstruction.getFrameWork());
        pstm.setBigDecimal(++col, itemsConstruction.getBuildingWide());
        pstm.setString(++col, itemsConstruction.getWorkType());
        pstm.setBigDecimal(++col, itemsConstruction.getPrice());
        pstm.setString(++col, itemsConstruction.getAddressLocation());

        if (itemsConstruction.getUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsConstruction.getUrban().getIndex());
        }

        if (itemsConstruction.getSubUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsConstruction.getSubUrban().getIndex());
        }

        if (itemsConstruction.getStartDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsConstruction.getStartDate().getTime()));
        }

        pstm.setString(++col, itemsConstruction.getLandStatus());
        pstm.setString(++col, itemsConstruction.getLandCode());
        pstm.setString(++col, itemsConstruction.getFundingSource());
        pstm.setString(++col, itemsConstruction.getDescription());
        pstm.setString(++col, itemsConstruction.getLastUserName());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsConstruction(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsconstruction where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }
    
    void deleteItemsConstruction(Connection conn, Unit unit) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsconstruction where unit = ?");
        pstm.setLong(1, unit.getIndex());
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsConstruction(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsconstruction where itemcode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ItemsConstruction> getItemsConstruction(Connection conn) throws SQLException {
        ArrayList<ItemsConstruction> itemsConstructions = new ArrayList<ItemsConstruction>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsconstruction");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Unit unit = null;

            if (unitIndex != null) {
                if (!unitIndex.equals(Long.valueOf(0))) {
                    unit = new Unit(unitIndex);
                }
            }

            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsConstruction itemsConstruction = new ItemsConstruction();
            itemsConstruction.setIndex(rs.getLong("autoindex"));
            itemsConstruction.setUnit(unit);
            itemsConstruction.setUrban(urban);
            itemsConstruction.setSubUrban(subUrban);
            itemsConstruction.setItemCode(rs.getString("itemcode"));
            itemsConstruction.setItemName(rs.getString("itemname"));
            itemsConstruction.setRegNumber(rs.getString("regnumber"));
            itemsConstruction.setDocumentNumber(rs.getString("documentnumber"));
            itemsConstruction.setDocumentDate(rs.getDate("documentdate"));
            itemsConstruction.setBuildingCategory(rs.getString("buildingcategory"));
            itemsConstruction.setRaised(rs.getString("israised"));
            itemsConstruction.setFrameWork(rs.getString("framework"));
            itemsConstruction.setBuildingWide(rs.getBigDecimal("buildingwide"));
            itemsConstruction.setWorkType(rs.getString("worktype"));
            itemsConstruction.setPrice(rs.getBigDecimal("price"));
            itemsConstruction.setAddressLocation(rs.getString("addresslocation"));
            itemsConstruction.setStartDate(rs.getDate("startdate"));
            itemsConstruction.setLandStatus(rs.getString("landstatus"));
            itemsConstruction.setLandCode(rs.getString("landcode"));
            itemsConstruction.setFundingSource(rs.getString("fundingsources"));
            itemsConstruction.setDescription(rs.getString("description"));
            itemsConstruction.setUserName(rs.getString("username"));
            itemsConstruction.setLastUserName(rs.getString("lastusername"));

            itemsConstructions.add(itemsConstruction);
        }

        rs.close();
        pstm.close();

        return itemsConstructions;
    }

    ArrayList<ItemsConstruction> getItemsConstruction(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsConstruction> itemsConstructions = new ArrayList<ItemsConstruction>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsconstruction ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Unit unit = null;

            if (unitIndex != null) {
                if (!unitIndex.equals(Long.valueOf(0))) {
                    unit = new Unit(unitIndex);
                }
            }

            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsConstruction itemsConstruction = new ItemsConstruction();
            itemsConstruction.setIndex(rs.getLong("autoindex"));
            itemsConstruction.setUnit(unit);
            itemsConstruction.setUrban(urban);
            itemsConstruction.setSubUrban(subUrban);
            itemsConstruction.setItemCode(rs.getString("itemcode"));
            itemsConstruction.setItemName(rs.getString("itemname"));
            itemsConstruction.setRegNumber(rs.getString("regnumber"));
            itemsConstruction.setDocumentNumber(rs.getString("documentnumber"));
            itemsConstruction.setDocumentDate(rs.getDate("documentdate"));
            itemsConstruction.setBuildingCategory(rs.getString("buildingcategory"));
            itemsConstruction.setRaised(rs.getString("israised"));
            itemsConstruction.setFrameWork(rs.getString("framework"));
            itemsConstruction.setBuildingWide(rs.getBigDecimal("buildingwide"));
            itemsConstruction.setWorkType(rs.getString("worktype"));
            itemsConstruction.setPrice(rs.getBigDecimal("price"));
            itemsConstruction.setAddressLocation(rs.getString("addresslocation"));
            itemsConstruction.setStartDate(rs.getDate("startdate"));
            itemsConstruction.setLandStatus(rs.getString("landstatus"));
            itemsConstruction.setLandCode(rs.getString("landcode"));
            itemsConstruction.setFundingSource(rs.getString("fundingsources"));
            itemsConstruction.setDescription(rs.getString("description"));
            itemsConstruction.setUserName(rs.getString("username"));
            itemsConstruction.setLastUserName(rs.getString("lastusername"));

            itemsConstructions.add(itemsConstruction);
        }

        rs.close();
        pstm.close();

        return itemsConstructions;
    }

    void insertItemsBuilding(Connection conn, ItemsBuilding itemsBuilding) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO itemsbuilding(").
                append("unit, itemcode, itemname, regnumber, wide, ").
                append("israised, condition, buildingprice,admincost, acquisitionyear, address,urban,suburban,").
                append("documentdate, documentnumber,landwide, landcode, landstate, fundingsources, acquisitionway,").
                append("description, eastlongitudecoord, southlatitudecoord, ownershipstatename,").
                append("ownershiprelation, ownershipoccupancy, ownershipoccupying, landtaxnumber,").
                append("landtaxdate, buildpermitnumber, buildpermitdate, advisplanningnumber,").
                append("advisplanningdate, types, shape, utilization, ages, levels,").
                append("framework, foundation, floortype, walltype, ceilingtype, rooftype,").
                append("frametype, doortype, windowtype, roomtype, materialquality, facilities,").
                append("buildingcondition, nursing, buildingclass, allotmentpoint, utilizationpoint,").
                append("locationpoint, accessionpoint, qualitypoint, conditionpoint,").
                append("layoutpoint, nursingpoint, marketinterestpoint,username,lastusername)").
                append("VALUES (?, ?, ?, ?, ?, ?, ?,").append("?, ?, ?, ?, ?,").
                append("?, ?, ?, ?, ?,").append("?, ?, ?, ?,").
                append("?, ?, ?, ?,").append("?, ?, ?, ?, ").
                append("?, ?, ?, ?, ?, ?,").append("?, ?, ?, ?, ?, ?, ").
                append("?, ?, ?, ?, ?, ?, ").append("?, ?, ?, ?, ?,").
                append("?, ?, ?, ?,").append("?, ?, ?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        if (itemsBuilding.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsBuilding.getUnit().getIndex());
        }

        pstm.setString(++col, itemsBuilding.getItemCode());
        pstm.setString(++col, itemsBuilding.getItemName());
        pstm.setString(++col, itemsBuilding.getRegNumber());
        pstm.setBigDecimal(++col, itemsBuilding.getWide());
        pstm.setString(++col, itemsBuilding.getRaised());

        if (itemsBuilding.getCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsBuilding.getCondition().getIndex());
        }

        pstm.setBigDecimal(++col, itemsBuilding.getPrice());
        pstm.setBigDecimal(++col, itemsBuilding.getAdminCost());
        if (itemsBuilding.getAcquisitionYear() == null) {
            pstm.setNull(++col, Types.INTEGER);
        } else {
            pstm.setInt(++col, itemsBuilding.getAcquisitionYear());
        }
        pstm.setString(++col, itemsBuilding.getAddress());

        if (itemsBuilding.getUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsBuilding.getUrban().getIndex());
        }

        if (itemsBuilding.getSubUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsBuilding.getSubUrban().getIndex());
        }

        if (itemsBuilding.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsBuilding.getDocumentDate().getTime()));
        }

        pstm.setString(++col, itemsBuilding.getDocumentNumber());

        pstm.setBigDecimal(++col, itemsBuilding.getLandWide());
        pstm.setString(++col, itemsBuilding.getLandCode());
        pstm.setString(++col, itemsBuilding.getLandState());
        pstm.setString(++col, itemsBuilding.getFundingSource());
        pstm.setString(++col, itemsBuilding.getAcquitionWay());
        pstm.setString(++col, itemsBuilding.getDescription());
        pstm.setString(++col, itemsBuilding.getEastLongitudeCoord());
        pstm.setString(++col, itemsBuilding.getSouthLatitudeCoord());
        pstm.setString(++col, itemsBuilding.getOwnerShipStateName());
        pstm.setString(++col, itemsBuilding.getOwnerShipRelation());
        pstm.setString(++col, itemsBuilding.getOwnerShipOccupancy());
        pstm.setString(++col, itemsBuilding.getOwnerShipOccupaying());
        pstm.setString(++col, itemsBuilding.getLandTaxNumber());

        if (itemsBuilding.getLandTaxDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsBuilding.getLandTaxDate().getTime()));
        }

        pstm.setString(++col, itemsBuilding.getBuildPermitNumber());

        if (itemsBuilding.getBuildPermitDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsBuilding.getBuildPermitDate().getTime()));
        }

        pstm.setString(++col, itemsBuilding.getAdvisPlanningNumber());

        if (itemsBuilding.getAdvisPlanningDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsBuilding.getAdvisPlanningDate().getTime()));
        }

        pstm.setString(++col, itemsBuilding.getType());
        pstm.setString(++col, itemsBuilding.getShape());
        pstm.setString(++col, itemsBuilding.getUtilization());
        pstm.setString(++col, itemsBuilding.getAges());
        pstm.setString(++col, itemsBuilding.getLevels());
        pstm.setString(++col, itemsBuilding.getFrameworks());
        pstm.setString(++col, itemsBuilding.getFoundation());
        pstm.setString(++col, itemsBuilding.getFloorType());
        pstm.setString(++col, itemsBuilding.getWallType());
        pstm.setString(++col, itemsBuilding.getCeillingType());
        pstm.setString(++col, itemsBuilding.getRoofType());
        pstm.setString(++col, itemsBuilding.getFrameType());
        pstm.setString(++col, itemsBuilding.getDoorType());
        pstm.setString(++col, itemsBuilding.getWindowType());
        pstm.setString(++col, itemsBuilding.getRoomType());
        pstm.setString(++col, itemsBuilding.getMaterialQuality());
        pstm.setString(++col, itemsBuilding.getFacilities());

        if (itemsBuilding.getBuildingCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsBuilding.getBuildingCondition().getIndex());
        }

        pstm.setString(++col, itemsBuilding.getNursing());
        pstm.setString(++col, itemsBuilding.getBuildingClass());
        pstm.setString(++col, itemsBuilding.getAllotmentPoint());
        pstm.setString(++col, itemsBuilding.getUtilizationPoint());
        pstm.setString(++col, itemsBuilding.getLocationPoint());
        pstm.setString(++col, itemsBuilding.getAccesionPoint());
        pstm.setString(++col, itemsBuilding.getQualityPoint());
        pstm.setString(++col, itemsBuilding.getConditionPoint());
        pstm.setString(++col, itemsBuilding.getLayoutPoint());
        pstm.setString(++col, itemsBuilding.getNursingPoint());
        pstm.setString(++col, itemsBuilding.getMarketInterestPoint());
        pstm.setString(++col, itemsBuilding.getUserName());
        pstm.setString(++col, itemsBuilding.getLastUserName());


        pstm.executeUpdate();
        pstm.close();
    }

    void updateItemsBuilding(Connection conn, Long oldIndex, ItemsBuilding itemsBuilding) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE itemsbuilding ").
                append("SET unit=?, itemcode=?, itemname=?, regnumber=?, wide=?,").
                append("israised=?, condition=?, buildingprice=?,admincost =?, acquisitionyear=?,").
                append("address=?, urban = ?, suburban = ?,documentdate = ?, documentnumber = ?, landwide=?, landcode=?, landstate=?, fundingsources=?,").
                append("acquisitionway=?, description=?, eastlongitudecoord=?, southlatitudecoord=?,").
                append("ownershipstatename=?, ownershiprelation=?, ownershipoccupancy=?,").
                append("ownershipoccupying=?, landtaxnumber=?, landtaxdate=?, buildpermitnumber=?,").
                append("buildpermitdate=?, advisplanningnumber=?, advisplanningdate=?,").
                append("types=?, shape=?, utilization=?, ages=?, levels=?, framework=?,").
                append("foundation=?, floortype=?, walltype=?, ceilingtype=?, rooftype=?,").
                append("frametype=?, doortype=?, windowtype=?, roomtype=?, materialquality=?,").
                append("facilities=?, buildingcondition=?, nursing=?, buildingclass=?,").
                append("allotmentpoint=?, utilizationpoint=?, locationpoint=?, accessionpoint=?,").
                append("qualitypoint=?, conditionpoint=?, layoutpoint=?, nursingpoint=?,").
                append("marketinterestpoint=?, lastusername = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        if (itemsBuilding.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsBuilding.getUnit().getIndex());
        }

        pstm.setString(++col, itemsBuilding.getItemCode());
        pstm.setString(++col, itemsBuilding.getItemName());
        pstm.setString(++col, itemsBuilding.getRegNumber());
        pstm.setBigDecimal(++col, itemsBuilding.getWide());
        pstm.setString(++col, itemsBuilding.getRaised());

        if (itemsBuilding.getCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsBuilding.getCondition().getIndex());
        }

        pstm.setBigDecimal(++col, itemsBuilding.getPrice());
        pstm.setBigDecimal(++col, itemsBuilding.getAdminCost());
        if (itemsBuilding.getAcquisitionYear() == null) {
            pstm.setNull(++col, Types.INTEGER);
        } else {
            pstm.setInt(++col, itemsBuilding.getAcquisitionYear());
        }
        pstm.setString(++col, itemsBuilding.getAddress());

        if (itemsBuilding.getUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsBuilding.getUrban().getIndex());
        }

        if (itemsBuilding.getSubUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsBuilding.getSubUrban().getIndex());
        }


        if (itemsBuilding.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsBuilding.getDocumentDate().getTime()));
        }

        pstm.setString(++col, itemsBuilding.getDocumentNumber());

        pstm.setBigDecimal(++col, itemsBuilding.getLandWide());
        pstm.setString(++col, itemsBuilding.getLandCode());
        pstm.setString(++col, itemsBuilding.getLandState());
        pstm.setString(++col, itemsBuilding.getFundingSource());
        pstm.setString(++col, itemsBuilding.getAcquitionWay());
        pstm.setString(++col, itemsBuilding.getDescription());
        pstm.setString(++col, itemsBuilding.getEastLongitudeCoord());
        pstm.setString(++col, itemsBuilding.getSouthLatitudeCoord());
        pstm.setString(++col, itemsBuilding.getOwnerShipStateName());
        pstm.setString(++col, itemsBuilding.getOwnerShipRelation());
        pstm.setString(++col, itemsBuilding.getOwnerShipOccupancy());
        pstm.setString(++col, itemsBuilding.getOwnerShipOccupaying());
        pstm.setString(++col, itemsBuilding.getLandTaxNumber());

        if (itemsBuilding.getLandTaxDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsBuilding.getLandTaxDate().getTime()));
        }

        pstm.setString(++col, itemsBuilding.getBuildPermitNumber());

        if (itemsBuilding.getBuildPermitDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsBuilding.getBuildPermitDate().getTime()));
        }

        pstm.setString(++col, itemsBuilding.getAdvisPlanningNumber());

        if (itemsBuilding.getAdvisPlanningDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(itemsBuilding.getAdvisPlanningDate().getTime()));
        }

        pstm.setString(++col, itemsBuilding.getType());
        pstm.setString(++col, itemsBuilding.getShape());
        pstm.setString(++col, itemsBuilding.getUtilization());
        pstm.setString(++col, itemsBuilding.getAges());
        pstm.setString(++col, itemsBuilding.getLevels());
        pstm.setString(++col, itemsBuilding.getFrameworks());
        pstm.setString(++col, itemsBuilding.getFoundation());
        pstm.setString(++col, itemsBuilding.getFloorType());
        pstm.setString(++col, itemsBuilding.getWallType());
        pstm.setString(++col, itemsBuilding.getCeillingType());
        pstm.setString(++col, itemsBuilding.getRoofType());
        pstm.setString(++col, itemsBuilding.getFrameType());
        pstm.setString(++col, itemsBuilding.getDoorType());
        pstm.setString(++col, itemsBuilding.getWindowType());
        pstm.setString(++col, itemsBuilding.getRoomType());
        pstm.setString(++col, itemsBuilding.getMaterialQuality());
        pstm.setString(++col, itemsBuilding.getFacilities());

        if (itemsBuilding.getBuildingCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, itemsBuilding.getBuildingCondition().getIndex());
        }

        pstm.setString(++col, itemsBuilding.getNursing());
        pstm.setString(++col, itemsBuilding.getBuildingClass());
        pstm.setString(++col, itemsBuilding.getAllotmentPoint());
        pstm.setString(++col, itemsBuilding.getUtilizationPoint());
        pstm.setString(++col, itemsBuilding.getLocationPoint());
        pstm.setString(++col, itemsBuilding.getAccesionPoint());
        pstm.setString(++col, itemsBuilding.getQualityPoint());
        pstm.setString(++col, itemsBuilding.getConditionPoint());
        pstm.setString(++col, itemsBuilding.getLayoutPoint());
        pstm.setString(++col, itemsBuilding.getNursingPoint());
        pstm.setString(++col, itemsBuilding.getMarketInterestPoint());
        pstm.setString(++col, itemsBuilding.getLastUserName());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsBuilding(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsbuilding where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }
    
    void deleteItemsBuilding(Connection conn, Unit unit) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsbuilding where unit = ?");
        pstm.setLong(1, unit.getIndex());
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsBuilding(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsbuilding where itemcode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ItemsBuilding> getItemsBuilding(Connection conn) throws SQLException {
        ArrayList<ItemsBuilding> itemsBuildings = new ArrayList<ItemsBuilding>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsbuilding");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");
            Long bconditionIndex = rs.getLong("buildingcondition");

            Unit unit = null;
            Condition condition = null;
            Condition buildingCondition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            if (bconditionIndex != null) {
                buildingCondition = new Condition(bconditionIndex);
            }


            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsBuilding itemsBuilding = new ItemsBuilding();
            itemsBuilding.setIndex(rs.getLong("autoindex"));
            itemsBuilding.setUnit(unit);
            itemsBuilding.setCondition(condition);
            itemsBuilding.setBuildingCondition(buildingCondition);
            //
            itemsBuilding.setItemCode(rs.getString("itemcode"));
            itemsBuilding.setItemName(rs.getString("itemname"));
            itemsBuilding.setRegNumber(rs.getString("regnumber"));
            itemsBuilding.setWide(rs.getBigDecimal("wide"));
            itemsBuilding.setPrice(rs.getBigDecimal("buildingprice"));
            itemsBuilding.setAdminCost(rs.getBigDecimal("admincost"));
            itemsBuilding.setRaised(rs.getString("israised"));
            itemsBuilding.setLandCode(rs.getString("landcode"));
            itemsBuilding.setLandState(rs.getString("landstate"));
            itemsBuilding.setLandWide(rs.getBigDecimal("landwide"));
            itemsBuilding.setAddress(rs.getString("address"));
            itemsBuilding.setUrban(urban);
            itemsBuilding.setSubUrban(subUrban);
            itemsBuilding.setDocumentDate(rs.getDate("documentdate"));
            itemsBuilding.setDocumentNumber(rs.getString("documentnumber"));
            itemsBuilding.setFundingSource(rs.getString("fundingsources"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            itemsBuilding.setAcquisitionYear(acquisitionYear);
            itemsBuilding.setAcquitionWay(rs.getString("acquisitionway"));
            itemsBuilding.setDescription(rs.getString("description"));
            itemsBuilding.setEastLongitudeCoord(rs.getString("eastlongitudecoord"));
            itemsBuilding.setSouthLatitudeCoord(rs.getString("southlatitudecoord"));
            itemsBuilding.setOwnerShipStateName(rs.getString("ownershipstatename"));
            itemsBuilding.setOwnerShipRelation(rs.getString("ownershiprelation"));
            itemsBuilding.setOwnerShipOccupancy(rs.getString("ownershipoccupancy"));
            itemsBuilding.setOwnerShipOccupaying(rs.getString("ownershipoccupying"));
            itemsBuilding.setLandTaxNumber(rs.getString("landtaxnumber"));
            itemsBuilding.setLandTaxDate(rs.getDate("landtaxdate"));
            itemsBuilding.setBuildPermitNumber(rs.getString("buildpermitnumber"));
            itemsBuilding.setBuildPermitDate(rs.getDate("buildpermitdate"));
            itemsBuilding.setAdvisPlanningNumber(rs.getString("advisplanningnumber"));
            itemsBuilding.setAdvisPlanningDate(rs.getDate("advisplanningdate"));
            itemsBuilding.setType(rs.getString("types"));
            itemsBuilding.setShape(rs.getString("shape"));
            itemsBuilding.setUtilization(rs.getString("utilization"));
            itemsBuilding.setAges(rs.getString("ages"));
            itemsBuilding.setLevels(rs.getString("levels"));
            itemsBuilding.setFrameworks(rs.getString("framework"));
            itemsBuilding.setFoundation(rs.getString("foundation"));
            itemsBuilding.setFloorType(rs.getString("floortype"));
            itemsBuilding.setWallType(rs.getString("walltype"));
            itemsBuilding.setCeillingType(rs.getString("ceilingtype"));
            itemsBuilding.setRoofType(rs.getString("rooftype"));
            itemsBuilding.setFrameType(rs.getString("frametype"));
            itemsBuilding.setDoorType(rs.getString("doortype"));
            itemsBuilding.setWindowType(rs.getString("windowtype"));
            itemsBuilding.setRoomType(rs.getString("roomtype"));
            itemsBuilding.setMaterialQuality(rs.getString("materialquality"));
            itemsBuilding.setFacilities(rs.getString("facilities"));
            itemsBuilding.setNursing(rs.getString("nursing"));
            itemsBuilding.setBuildingClass(rs.getString("buildingclass"));
            itemsBuilding.setAllotmentPoint(rs.getString("allotmentpoint"));
            itemsBuilding.setUtilizationPoint(rs.getString("utilizationpoint"));
            itemsBuilding.setLocationPoint(rs.getString("locationpoint"));
            itemsBuilding.setAccesionPoint(rs.getString("accessionpoint"));
            itemsBuilding.setQualityPoint(rs.getString("qualitypoint"));
            itemsBuilding.setConditionPoint(rs.getString("conditionpoint"));
            itemsBuilding.setLayoutPoint(rs.getString("layoutpoint"));
            itemsBuilding.setNursingPoint(rs.getString("nursingpoint"));
            itemsBuilding.setMarketInterestPoint(rs.getString("marketinterestpoint"));

            itemsBuilding.setUserName(rs.getString("username"));
            itemsBuilding.setLastUserName(rs.getString("lastusername"));

            itemsBuildings.add(itemsBuilding);
        }

        rs.close();
        pstm.close();

        return itemsBuildings;
    }

    ArrayList<ItemsBuilding> getItemsBuilding(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsBuilding> itemsBuildings = new ArrayList<ItemsBuilding>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsbuilding ").append(modifier).
                append(" order by autoindex");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");
            Long bconditionIndex = rs.getLong("buildingcondition");

            Unit unit = null;
            Condition condition = null;
            Condition buildingCondition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            if (bconditionIndex != null) {
                buildingCondition = new Condition(bconditionIndex);
            }


            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsBuilding itemsBuilding = new ItemsBuilding();
            itemsBuilding.setIndex(rs.getLong("autoindex"));
            itemsBuilding.setUnit(unit);
            itemsBuilding.setCondition(condition);
            itemsBuilding.setBuildingCondition(buildingCondition);
            //
            itemsBuilding.setItemCode(rs.getString("itemcode"));
            itemsBuilding.setItemName(rs.getString("itemname"));
            itemsBuilding.setRegNumber(rs.getString("regnumber"));
            itemsBuilding.setWide(rs.getBigDecimal("wide"));
            itemsBuilding.setPrice(rs.getBigDecimal("buildingprice"));
            itemsBuilding.setAdminCost(rs.getBigDecimal("admincost"));
            itemsBuilding.setRaised(rs.getString("israised"));
            itemsBuilding.setLandCode(rs.getString("landcode"));
            itemsBuilding.setLandState(rs.getString("landstate"));
            itemsBuilding.setLandWide(rs.getBigDecimal("landwide"));
            itemsBuilding.setAddress(rs.getString("address"));
            itemsBuilding.setUrban(urban);
            itemsBuilding.setSubUrban(subUrban);
            itemsBuilding.setDocumentDate(rs.getDate("documentdate"));
            itemsBuilding.setDocumentNumber(rs.getString("documentnumber"));
            itemsBuilding.setFundingSource(rs.getString("fundingsources"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            itemsBuilding.setAcquisitionYear(acquisitionYear);
            itemsBuilding.setAcquitionWay(rs.getString("acquisitionway"));
            itemsBuilding.setDescription(rs.getString("description"));
            itemsBuilding.setEastLongitudeCoord(rs.getString("eastlongitudecoord"));
            itemsBuilding.setSouthLatitudeCoord(rs.getString("southlatitudecoord"));
            itemsBuilding.setOwnerShipStateName(rs.getString("ownershipstatename"));
            itemsBuilding.setOwnerShipRelation(rs.getString("ownershiprelation"));
            itemsBuilding.setOwnerShipOccupancy(rs.getString("ownershipoccupancy"));
            itemsBuilding.setOwnerShipOccupaying(rs.getString("ownershipoccupying"));
            itemsBuilding.setLandTaxNumber(rs.getString("landtaxnumber"));
            itemsBuilding.setLandTaxDate(rs.getDate("landtaxdate"));
            itemsBuilding.setBuildPermitNumber(rs.getString("buildpermitnumber"));
            itemsBuilding.setBuildPermitDate(rs.getDate("buildpermitdate"));
            itemsBuilding.setAdvisPlanningNumber(rs.getString("advisplanningnumber"));
            itemsBuilding.setAdvisPlanningDate(rs.getDate("advisplanningdate"));
            itemsBuilding.setType(rs.getString("types"));
            itemsBuilding.setShape(rs.getString("shape"));
            itemsBuilding.setUtilization(rs.getString("utilization"));
            itemsBuilding.setAges(rs.getString("ages"));
            itemsBuilding.setLevels(rs.getString("levels"));
            itemsBuilding.setFrameworks(rs.getString("framework"));
            itemsBuilding.setFoundation(rs.getString("foundation"));
            itemsBuilding.setFloorType(rs.getString("floortype"));
            itemsBuilding.setWallType(rs.getString("walltype"));
            itemsBuilding.setCeillingType(rs.getString("ceilingtype"));
            itemsBuilding.setRoofType(rs.getString("rooftype"));
            itemsBuilding.setFrameType(rs.getString("frametype"));
            itemsBuilding.setDoorType(rs.getString("doortype"));
            itemsBuilding.setWindowType(rs.getString("windowtype"));
            itemsBuilding.setRoomType(rs.getString("roomtype"));
            itemsBuilding.setMaterialQuality(rs.getString("materialquality"));
            itemsBuilding.setFacilities(rs.getString("facilities"));
            itemsBuilding.setNursing(rs.getString("nursing"));
            itemsBuilding.setBuildingClass(rs.getString("buildingclass"));
            itemsBuilding.setAllotmentPoint(rs.getString("allotmentpoint"));
            itemsBuilding.setUtilizationPoint(rs.getString("utilizationpoint"));
            itemsBuilding.setLocationPoint(rs.getString("locationpoint"));
            itemsBuilding.setAccesionPoint(rs.getString("accessionpoint"));
            itemsBuilding.setQualityPoint(rs.getString("qualitypoint"));
            itemsBuilding.setConditionPoint(rs.getString("conditionpoint"));
            itemsBuilding.setLayoutPoint(rs.getString("layoutpoint"));
            itemsBuilding.setNursingPoint(rs.getString("nursingpoint"));
            itemsBuilding.setMarketInterestPoint(rs.getString("marketinterestpoint"));
            itemsBuilding.setUserName(rs.getString("username"));
            itemsBuilding.setLastUserName(rs.getString("lastusername"));

            itemsBuildings.add(itemsBuilding);
        }

        rs.close();
        pstm.close();

        return itemsBuildings;
    }

    ArrayList<ItemsBuilding> getItemsBuildingNotInDeleteDraft(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsBuilding> itemsBuildings = new ArrayList<ItemsBuilding>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsbuilding ").append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select itemsindex from deletedraftitems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_BUILDING).
                    append(")");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select itemsindex from deletedraftitems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_BUILDING).
                    append(")");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");
            Long bconditionIndex = rs.getLong("buildingcondition");

            Unit unit = null;
            Condition condition = null;
            Condition buildingCondition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            if (bconditionIndex != null) {
                buildingCondition = new Condition(bconditionIndex);
            }


            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsBuilding itemsBuilding = new ItemsBuilding();
            itemsBuilding.setIndex(rs.getLong("autoindex"));
            itemsBuilding.setUnit(unit);
            itemsBuilding.setCondition(condition);
            itemsBuilding.setBuildingCondition(buildingCondition);
            //
            itemsBuilding.setItemCode(rs.getString("itemcode"));
            itemsBuilding.setItemName(rs.getString("itemname"));
            itemsBuilding.setRegNumber(rs.getString("regnumber"));
            itemsBuilding.setWide(rs.getBigDecimal("wide"));
            itemsBuilding.setPrice(rs.getBigDecimal("buildingprice"));
            itemsBuilding.setAdminCost(rs.getBigDecimal("admincost"));
            itemsBuilding.setRaised(rs.getString("israised"));
            itemsBuilding.setLandCode(rs.getString("landcode"));
            itemsBuilding.setLandState(rs.getString("landstate"));
            itemsBuilding.setLandWide(rs.getBigDecimal("landwide"));
            itemsBuilding.setAddress(rs.getString("address"));
            itemsBuilding.setUrban(urban);
            itemsBuilding.setSubUrban(subUrban);
            itemsBuilding.setDocumentDate(rs.getDate("documentdate"));
            itemsBuilding.setDocumentNumber(rs.getString("documentnumber"));
            itemsBuilding.setFundingSource(rs.getString("fundingsources"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            itemsBuilding.setAcquisitionYear(acquisitionYear);
            itemsBuilding.setAcquitionWay(rs.getString("acquisitionway"));
            itemsBuilding.setDescription(rs.getString("description"));
            itemsBuilding.setEastLongitudeCoord(rs.getString("eastlongitudecoord"));
            itemsBuilding.setSouthLatitudeCoord(rs.getString("southlatitudecoord"));
            itemsBuilding.setOwnerShipStateName(rs.getString("ownershipstatename"));
            itemsBuilding.setOwnerShipRelation(rs.getString("ownershiprelation"));
            itemsBuilding.setOwnerShipOccupancy(rs.getString("ownershipoccupancy"));
            itemsBuilding.setOwnerShipOccupaying(rs.getString("ownershipoccupying"));
            itemsBuilding.setLandTaxNumber(rs.getString("landtaxnumber"));
            itemsBuilding.setLandTaxDate(rs.getDate("landtaxdate"));
            itemsBuilding.setBuildPermitNumber(rs.getString("buildpermitnumber"));
            itemsBuilding.setBuildPermitDate(rs.getDate("buildpermitdate"));
            itemsBuilding.setAdvisPlanningNumber(rs.getString("advisplanningnumber"));
            itemsBuilding.setAdvisPlanningDate(rs.getDate("advisplanningdate"));
            itemsBuilding.setType(rs.getString("types"));
            itemsBuilding.setShape(rs.getString("shape"));
            itemsBuilding.setUtilization(rs.getString("utilization"));
            itemsBuilding.setAges(rs.getString("ages"));
            itemsBuilding.setLevels(rs.getString("levels"));
            itemsBuilding.setFrameworks(rs.getString("framework"));
            itemsBuilding.setFoundation(rs.getString("foundation"));
            itemsBuilding.setFloorType(rs.getString("floortype"));
            itemsBuilding.setWallType(rs.getString("walltype"));
            itemsBuilding.setCeillingType(rs.getString("ceilingtype"));
            itemsBuilding.setRoofType(rs.getString("rooftype"));
            itemsBuilding.setFrameType(rs.getString("frametype"));
            itemsBuilding.setDoorType(rs.getString("doortype"));
            itemsBuilding.setWindowType(rs.getString("windowtype"));
            itemsBuilding.setRoomType(rs.getString("roomtype"));
            itemsBuilding.setMaterialQuality(rs.getString("materialquality"));
            itemsBuilding.setFacilities(rs.getString("facilities"));
            itemsBuilding.setNursing(rs.getString("nursing"));
            itemsBuilding.setBuildingClass(rs.getString("buildingclass"));
            itemsBuilding.setAllotmentPoint(rs.getString("allotmentpoint"));
            itemsBuilding.setUtilizationPoint(rs.getString("utilizationpoint"));
            itemsBuilding.setLocationPoint(rs.getString("locationpoint"));
            itemsBuilding.setAccesionPoint(rs.getString("accessionpoint"));
            itemsBuilding.setQualityPoint(rs.getString("qualitypoint"));
            itemsBuilding.setConditionPoint(rs.getString("conditionpoint"));
            itemsBuilding.setLayoutPoint(rs.getString("layoutpoint"));
            itemsBuilding.setNursingPoint(rs.getString("nursingpoint"));
            itemsBuilding.setMarketInterestPoint(rs.getString("marketinterestpoint"));
            itemsBuilding.setUserName(rs.getString("username"));
            itemsBuilding.setLastUserName(rs.getString("lastusername"));

            itemsBuildings.add(itemsBuilding);
        }

        rs.close();
        pstm.close();

        return itemsBuildings;
    }

    ArrayList<ItemsBuilding> getItemsBuildingNotInUsedItems(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsBuilding> itemsBuildings = new ArrayList<ItemsBuilding>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsbuilding ").append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select itemsindex from useditems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_BUILDING).
                    append(")");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select itemsindex from useditems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_BUILDING).
                    append(")");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");
            Long bconditionIndex = rs.getLong("buildingcondition");

            Unit unit = null;
            Condition condition = null;
            Condition buildingCondition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            if (bconditionIndex != null) {
                buildingCondition = new Condition(bconditionIndex);
            }


            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsBuilding itemsBuilding = new ItemsBuilding();
            itemsBuilding.setIndex(rs.getLong("autoindex"));
            itemsBuilding.setUnit(unit);
            itemsBuilding.setCondition(condition);
            itemsBuilding.setBuildingCondition(buildingCondition);
            //
            itemsBuilding.setItemCode(rs.getString("itemcode"));
            itemsBuilding.setItemName(rs.getString("itemname"));
            itemsBuilding.setRegNumber(rs.getString("regnumber"));
            itemsBuilding.setWide(rs.getBigDecimal("wide"));
            itemsBuilding.setPrice(rs.getBigDecimal("buildingprice"));
            itemsBuilding.setAdminCost(rs.getBigDecimal("admincost"));
            itemsBuilding.setRaised(rs.getString("israised"));
            itemsBuilding.setLandCode(rs.getString("landcode"));
            itemsBuilding.setLandState(rs.getString("landstate"));
            itemsBuilding.setLandWide(rs.getBigDecimal("landwide"));
            itemsBuilding.setAddress(rs.getString("address"));
            itemsBuilding.setUrban(urban);
            itemsBuilding.setSubUrban(subUrban);
            itemsBuilding.setDocumentDate(rs.getDate("documentdate"));
            itemsBuilding.setDocumentNumber(rs.getString("documentnumber"));
            itemsBuilding.setFundingSource(rs.getString("fundingsources"));
            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }
            
            itemsBuilding.setAcquisitionYear(acquisitionYear);
            itemsBuilding.setAcquitionWay(rs.getString("acquisitionway"));
            itemsBuilding.setDescription(rs.getString("description"));
            itemsBuilding.setEastLongitudeCoord(rs.getString("eastlongitudecoord"));
            itemsBuilding.setSouthLatitudeCoord(rs.getString("southlatitudecoord"));
            itemsBuilding.setOwnerShipStateName(rs.getString("ownershipstatename"));
            itemsBuilding.setOwnerShipRelation(rs.getString("ownershiprelation"));
            itemsBuilding.setOwnerShipOccupancy(rs.getString("ownershipoccupancy"));
            itemsBuilding.setOwnerShipOccupaying(rs.getString("ownershipoccupying"));
            itemsBuilding.setLandTaxNumber(rs.getString("landtaxnumber"));
            itemsBuilding.setLandTaxDate(rs.getDate("landtaxdate"));
            itemsBuilding.setBuildPermitNumber(rs.getString("buildpermitnumber"));
            itemsBuilding.setBuildPermitDate(rs.getDate("buildpermitdate"));
            itemsBuilding.setAdvisPlanningNumber(rs.getString("advisplanningnumber"));
            itemsBuilding.setAdvisPlanningDate(rs.getDate("advisplanningdate"));
            itemsBuilding.setType(rs.getString("types"));
            itemsBuilding.setShape(rs.getString("shape"));
            itemsBuilding.setUtilization(rs.getString("utilization"));
            itemsBuilding.setAges(rs.getString("ages"));
            itemsBuilding.setLevels(rs.getString("levels"));
            itemsBuilding.setFrameworks(rs.getString("framework"));
            itemsBuilding.setFoundation(rs.getString("foundation"));
            itemsBuilding.setFloorType(rs.getString("floortype"));
            itemsBuilding.setWallType(rs.getString("walltype"));
            itemsBuilding.setCeillingType(rs.getString("ceilingtype"));
            itemsBuilding.setRoofType(rs.getString("rooftype"));
            itemsBuilding.setFrameType(rs.getString("frametype"));
            itemsBuilding.setDoorType(rs.getString("doortype"));
            itemsBuilding.setWindowType(rs.getString("windowtype"));
            itemsBuilding.setRoomType(rs.getString("roomtype"));
            itemsBuilding.setMaterialQuality(rs.getString("materialquality"));
            itemsBuilding.setFacilities(rs.getString("facilities"));
            itemsBuilding.setNursing(rs.getString("nursing"));
            itemsBuilding.setBuildingClass(rs.getString("buildingclass"));
            itemsBuilding.setAllotmentPoint(rs.getString("allotmentpoint"));
            itemsBuilding.setUtilizationPoint(rs.getString("utilizationpoint"));
            itemsBuilding.setLocationPoint(rs.getString("locationpoint"));
            itemsBuilding.setAccesionPoint(rs.getString("accessionpoint"));
            itemsBuilding.setQualityPoint(rs.getString("qualitypoint"));
            itemsBuilding.setConditionPoint(rs.getString("conditionpoint"));
            itemsBuilding.setLayoutPoint(rs.getString("layoutpoint"));
            itemsBuilding.setNursingPoint(rs.getString("nursingpoint"));
            itemsBuilding.setMarketInterestPoint(rs.getString("marketinterestpoint"));
            itemsBuilding.setUserName(rs.getString("username"));
            itemsBuilding.setLastUserName(rs.getString("lastusername"));

            itemsBuildings.add(itemsBuilding);
        }

        rs.close();
        pstm.close();

        return itemsBuildings;
    }

    void insertItemsLand(Connection conn, ItemsLand land) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO itemsland(").
                append("unit, itemcode, itemname, regnumber, wide,addresslocation,urban,suburban, landprice,").
                append("acquisitionyear, fundingsources, acquisitionway,description, ownershipstatename, ownershipnumberstate,").
                append("ownershipdate, ownershipissuedby, owners, ownershipstatus,ownershiprelation,").
                append("ownershipoccupancy, ownershipoccupying, landcertificatenumber,").
                append("landcertificatedate, landtaxnumber, landtaxdate, buildpermitnumber,").
                append("buildpermitdate, advisplanningnumber, advisplanningdate, condition,").
                append("shape, topographcondition, currentuse, closerroad, roadsurface, heightabove,").
                append("heightunder, elevation, allotment, pricebasedchief, pricebasednjop,").
                append("pricebasedexamination, pricedescription, locations, positions,").
                append("roadaccess, roadwidth, pavement, traffic, topographlocation,").
                append("environment, density, sociallevels, facilities, drainage, publictransportation,").
                append("securitys, securitydisturbance, flooddangers, locationeffect,").
                append("environmentalinfluences, allotmentpoint, utilizationpoint,locationpoint, accessionpoint,").
                append("nursingpoint, soilconditionspoint, marketinterestpoint, comparativeprice1,").
                append("comparativewide1, comparativesource1, comparativeprice2, comparativewide2,").
                append("comparativesource2, comparativeprice3, comparativewide3, comparativesource3,").
                append("comparativeprice4, comparativewide4, comparativesource4, comparativeprice5,").
                append("comparativewide5, comparativesource5, landaddress, njopyear,").
                append("njoplandwide, njoplandclass, njoplandvalue,username,lastusername) ").
                append("VALUES (?, ?, ?, ?, ?, ?,").append("?, ?, ?, ?,").
                append("?, ?, ?,").append("?, ?, ?, ?,").append("?, ?, ?,").
                append("?, ?, ?, ?,").append("?, ?, ?, ?,").append("?, ?, ?, ?, ?,").
                append("?, ?, ?, ?, ?,").append("?, ?, ?, ?,").append("?, ?, ?, ?, ?,").
                append("?, ?, ?, ?, ?, ?,").append("?, ?, ?, ?,").append("?, ?, ?, ?,").
                append("?, ?, ?, ?,").append("?, ?, ?, ?,").append("?, ?, ?, ?,?,").
                append("?, ?, ?, ?,").append("?, ?, ?, ?,").append("?, ?, ?,?, ?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        if (land.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, land.getUnit().getIndex());
        }

        pstm.setString(++col, land.getItemCode());
        pstm.setString(++col, land.getItemName());

        if (land.getRegNumber() == null) {
            pstm.setString(++col, "");
        } else {
            pstm.setString(++col, land.getRegNumber());
        }

        if (land.getWide() == null) {
            pstm.setNull(++col, Types.DECIMAL);
        } else {
            pstm.setBigDecimal(++col, land.getWide());
        }

        if (land.getAddressLocation() == null) {
            pstm.setString(++col, "");
        } else {
            pstm.setString(++col, land.getAddressLocation());
        }

        if (land.getUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, land.getUrban().getIndex());
        }

        if (land.getSubUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, land.getSubUrban().getIndex());
        }

        if (land.getLandPrice() == null) {
            pstm.setNull(++col, Types.DECIMAL);
        } else {
            pstm.setBigDecimal(++col, land.getLandPrice());
        }

        if (land.getAcquisitionYear() == null) {
            pstm.setNull(++col, Types.INTEGER);
        } else {
            pstm.setInt(++col, land.getAcquisitionYear());
        }

        pstm.setString(++col, land.getFundingSource());
        if (land.getAcquisitionWay() == null) {
            pstm.setString(++col, "");
        } else {
            pstm.setString(++col, land.getAcquisitionWay());
        }

        if (land.getDescription() == null) {
            pstm.setString(++col, "");
        } else {
            pstm.setString(++col, land.getDescription());
        }

        //
        if (land.getOwnerShipStateName() == null) {
            pstm.setString(++col, "");
        } else {
            pstm.setString(++col, land.getOwnerShipStateName());
        }


        pstm.setString(++col, land.getOwnerShipNumberState());

        if (land.getOwnerShipDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(land.getOwnerShipDate().getTime()));
        }

        pstm.setString(++col, land.getOwnerShipIssuedBy());
        pstm.setString(++col, land.getOwner());
        pstm.setString(++col, land.getOwnerShipStatus());
        pstm.setString(++col, land.getOwnerShipRelation());
        pstm.setString(++col, land.getOwnerShipOccupancy());
        pstm.setString(++col, land.getOwnerShipOccupying());
        //
        if (land.getLandCertificateNumber() == null) {
            pstm.setString(++col, "");
        } else {
            pstm.setString(++col, land.getLandCertificateNumber());
        }

        if (land.getLandCertificateDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(land.getLandCertificateDate().getTime()));
        }
        pstm.setString(++col, land.getLandTaxNumber());
        if (land.getLandTaxDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(land.getLandTaxDate().getTime()));
        }
        pstm.setString(++col, land.getBuildPermitNumber());
        if (land.getBuildPermitDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(land.getBuildPermitDate().getTime()));
        }
        pstm.setString(++col, land.getAdvisPlanningNumber());
        if (land.getAdvisPlanningDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(land.getAdvisPlanningDate().getTime()));
        }
        pstm.setString(++col, land.getLandCondition());
        pstm.setString(++col, land.getShape());
        pstm.setString(++col, land.getTopographCondition());
        pstm.setString(++col, land.getCurrentUse());
        pstm.setString(++col, land.getCloserRoad());
        pstm.setString(++col, land.getRoadSurface());
        pstm.setString(++col, land.getHeightAbove());
        pstm.setString(++col, land.getHeightUnder());
        pstm.setString(++col, land.getElevation());

        if (land.getAllotment() == null) {
            pstm.setString(++col, "");
        } else {
            pstm.setString(++col, land.getAllotment());
        }

        pstm.setBigDecimal(++col, land.getPriceBasedChief());
        pstm.setBigDecimal(++col, land.getPriceBasedNJOP());
        pstm.setBigDecimal(++col, land.getPriceBasedExam());
        pstm.setString(++col, land.getPriceDescription());
        //
        pstm.setString(++col, land.getLocation());
        pstm.setString(++col, land.getPosition());
        pstm.setString(++col, land.getRoadAccess());
        pstm.setString(++col, land.getRoadWidth());
        pstm.setString(++col, land.getPavement());
        pstm.setString(++col, land.getTraffic());
        pstm.setString(++col, land.getTopographLocation());
        pstm.setString(++col, land.getEnvironment());
        pstm.setString(++col, land.getDensity());
        pstm.setString(++col, land.getSocialLevels());
        pstm.setString(++col, land.getFacilities());
        pstm.setString(++col, land.getDrainage());
        pstm.setString(++col, land.getPublicTransportation());
        pstm.setString(++col, land.getSecurity());
        //
        pstm.setString(++col, land.getSecurityDisturbance());
        pstm.setString(++col, land.getFloodDangers());
        pstm.setString(++col, land.getLocationEffect());
        pstm.setString(++col, land.getEnvironmentalInfluences());
        //
        pstm.setString(++col, land.getAllotmentPoint());
        pstm.setString(++col, land.getUtilizationPoint());
        pstm.setString(++col, land.getLocationPoint());
        pstm.setString(++col, land.getAccessionPoint());
        pstm.setString(++col, land.getNursingPoint());
        pstm.setString(++col, land.getSoilConditionsPoint());
        pstm.setString(++col, land.getMarketInterestPoint());
        //
        pstm.setBigDecimal(++col, land.getComparativePrice());
        pstm.setBigDecimal(++col, land.getComparativeWide());
        pstm.setString(++col, land.getComparativeSource());

        pstm.setBigDecimal(++col, land.getComparativePrice2());
        pstm.setBigDecimal(++col, land.getComparativeWide2());
        pstm.setString(++col, land.getComparativeSource2());

        pstm.setBigDecimal(++col, land.getComparativePrice3());
        pstm.setBigDecimal(++col, land.getComparativeWide3());
        pstm.setString(++col, land.getComparativeSource3());

        pstm.setBigDecimal(++col, land.getComparativePrice4());
        pstm.setBigDecimal(++col, land.getComparativeWide4());
        pstm.setString(++col, land.getComparativeSource4());

        pstm.setBigDecimal(++col, land.getComparativePrice5());
        pstm.setBigDecimal(++col, land.getComparativeWide5());
        pstm.setString(++col, land.getComparativeSource5());

        pstm.setString(++col, land.getLandAddress());
        pstm.setInt(++col, land.getNjopYear());
        pstm.setBigDecimal(++col, land.getNjopLandwide());
        pstm.setString(++col, land.getNjopLandClass());
        pstm.setBigDecimal(++col, land.getNjopLandValue());
        pstm.setString(++col, land.getUserName());
        pstm.setString(++col, land.getLastUserName());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateItemsLand(Connection conn, Long oldIndex, ItemsLand land) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE itemsland ").
                append("SET unit=?, itemcode=?, itemname=?, regnumber=?, wide=?, addresslocation = ?, urban = ?, suburban = ?,").
                append("landprice=?, acquisitionyear=?, fundingsources=?, acquisitionway=?,description = ?,").
                append("ownershipstatename=?,ownershipnumberstate=?, ownershipdate=?, ownershipissuedby=?,").
                append("owners=?, ownershipstatus=?,ownershiprelation=?, ownershipoccupancy=?, ownershipoccupying=?,").
                append("landcertificatenumber=?, landcertificatedate=?, landtaxnumber=?,").
                append("landtaxdate=?, buildpermitnumber=?, buildpermitdate=?, advisplanningnumber=?,").
                append("advisplanningdate=?, condition=?, shape=?, topographcondition=?,").
                append("currentuse=?, closerroad= ?,roadsurface=?, heightabove=?, heightunder=?, elevation=?,").
                append("allotment=?, pricebasedchief=?, pricebasednjop=?, pricebasedexamination=?,").
                append("pricedescription=?, locations=?, positions=?, roadaccess=?, roadwidth=?,").
                append("pavement=?, traffic=?, topographlocation=?, environment=?, density=?,").
                append("sociallevels=?, facilities=?, drainage=?, publictransportation=?,").
                append("securitys=?, securitydisturbance=?, flooddangers=?, locationeffect=?,").
                append("environmentalinfluences=?, allotmentpoint=?, utilizationpoint=?, locationpoint = ?,").
                append("accessionpoint=?, nursingpoint=?, soilconditionspoint=?, marketinterestpoint=?,").
                append("comparativeprice1=?, comparativewide1=?, comparativesource1=?,").
                append("comparativeprice2=?, comparativewide2=?, comparativesource2=?,").
                append("comparativeprice3=?, comparativewide3=?, comparativesource3=?,").
                append("comparativeprice4=?, comparativewide4=?, comparativesource4=?,").
                append("comparativeprice5=?, comparativewide5=?, comparativesource5=?,").
                append("landaddress=?, njopyear=?, njoplandwide=?, njoplandclass=?, njoplandvalue=?, lastusername = ? ").
                append("where autoindex = ?");


        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        if (land.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, land.getUnit().getIndex());
        }

        pstm.setString(++col, land.getItemCode());
        pstm.setString(++col, land.getItemName());
        pstm.setString(++col, land.getRegNumber());
        pstm.setBigDecimal(++col, land.getWide());
        pstm.setString(++col, land.getAddressLocation());

        if (land.getUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, land.getUrban().getIndex());
        }

        if (land.getSubUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, land.getSubUrban().getIndex());
        }
        pstm.setBigDecimal(++col, land.getLandPrice());
        if (land.getAcquisitionYear() == null) {
            pstm.setNull(++col, Types.INTEGER);
        } else {
            pstm.setInt(++col, land.getAcquisitionYear());
        }
        pstm.setString(++col, land.getFundingSource());
        pstm.setString(++col, land.getAcquisitionWay());
        pstm.setString(++col, land.getDescription());
        //
        pstm.setString(++col, land.getOwnerShipStateName());
        pstm.setString(++col, land.getOwnerShipNumberState());

        if (land.getOwnerShipDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(land.getOwnerShipDate().getTime()));
        }

        pstm.setString(++col, land.getOwnerShipIssuedBy());
        pstm.setString(++col, land.getOwner());
        pstm.setString(++col, land.getOwnerShipStatus());
        pstm.setString(++col, land.getOwnerShipRelation());
        pstm.setString(++col, land.getOwnerShipOccupancy());
        pstm.setString(++col, land.getOwnerShipOccupying());
        //
        pstm.setString(++col, land.getLandCertificateNumber());
        if (land.getLandCertificateDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(land.getLandCertificateDate().getTime()));
        }
        pstm.setString(++col, land.getLandTaxNumber());
        if (land.getLandTaxDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(land.getLandTaxDate().getTime()));
        }
        pstm.setString(++col, land.getBuildPermitNumber());
        if (land.getBuildPermitDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(land.getBuildPermitDate().getTime()));
        }
        pstm.setString(++col, land.getAdvisPlanningNumber());
        if (land.getAdvisPlanningDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(land.getAdvisPlanningDate().getTime()));
        }
        pstm.setString(++col, land.getLandCondition());
        pstm.setString(++col, land.getShape());
        pstm.setString(++col, land.getTopographCondition());
        pstm.setString(++col, land.getCurrentUse());
        pstm.setString(++col, land.getCloserRoad());
        pstm.setString(++col, land.getRoadSurface());
        pstm.setString(++col, land.getHeightAbove());
        pstm.setString(++col, land.getHeightUnder());
        pstm.setString(++col, land.getElevation());
        pstm.setString(++col, land.getAllotment());
        pstm.setBigDecimal(++col, land.getPriceBasedChief());
        pstm.setBigDecimal(++col, land.getPriceBasedNJOP());
        pstm.setBigDecimal(++col, land.getPriceBasedExam());
        pstm.setString(++col, land.getPriceDescription());
        //
        pstm.setString(++col, land.getLocation());
        pstm.setString(++col, land.getPosition());
        pstm.setString(++col, land.getRoadAccess());
        pstm.setString(++col, land.getRoadWidth());
        pstm.setString(++col, land.getPavement());
        pstm.setString(++col, land.getTraffic());
        pstm.setString(++col, land.getTopographLocation());
        pstm.setString(++col, land.getEnvironment());
        pstm.setString(++col, land.getDensity());
        pstm.setString(++col, land.getSocialLevels());
        pstm.setString(++col, land.getFacilities());
        pstm.setString(++col, land.getDrainage());
        pstm.setString(++col, land.getPublicTransportation());
        pstm.setString(++col, land.getSecurity());
        //
        pstm.setString(++col, land.getSecurityDisturbance());
        pstm.setString(++col, land.getFloodDangers());
        pstm.setString(++col, land.getLocationEffect());
        pstm.setString(++col, land.getEnvironmentalInfluences());
        //
        pstm.setString(++col, land.getAllotmentPoint());
        pstm.setString(++col, land.getUtilizationPoint());
        pstm.setString(++col, land.getLocationPoint());
        pstm.setString(++col, land.getAccessionPoint());
        pstm.setString(++col, land.getNursingPoint());
        pstm.setString(++col, land.getSoilConditionsPoint());
        pstm.setString(++col, land.getMarketInterestPoint());
        //
        pstm.setBigDecimal(++col, land.getComparativePrice());
        pstm.setBigDecimal(++col, land.getComparativeWide());
        pstm.setString(++col, land.getComparativeSource());
        pstm.setBigDecimal(++col, land.getComparativePrice2());
        pstm.setBigDecimal(++col, land.getComparativeWide2());
        pstm.setString(++col, land.getComparativeSource2());
        pstm.setBigDecimal(++col, land.getComparativePrice3());
        pstm.setBigDecimal(++col, land.getComparativeWide3());
        pstm.setString(++col, land.getComparativeSource3());
        pstm.setBigDecimal(++col, land.getComparativePrice4());
        pstm.setBigDecimal(++col, land.getComparativeWide4());
        pstm.setString(++col, land.getComparativeSource4());
        pstm.setBigDecimal(++col, land.getComparativePrice5());
        pstm.setBigDecimal(++col, land.getComparativeWide5());
        pstm.setString(++col, land.getComparativeSource5());
        pstm.setString(++col, land.getLandAddress());
        pstm.setInt(++col, land.getNjopYear());
        pstm.setBigDecimal(++col, land.getNjopLandwide());
        pstm.setString(++col, land.getNjopLandClass());
        pstm.setBigDecimal(++col, land.getNjopLandValue());
        pstm.setString(++col, land.getLastUserName());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsLand(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsland where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsLand(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsland where itemcode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }
    
    void deleteItemsLand(Connection conn, Unit unit) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsland where unit = ?");
        pstm.setLong(1, unit.getIndex());
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ItemsLand> getItemsLand(Connection conn) throws SQLException {
        ArrayList<ItemsLand> lands = new ArrayList<ItemsLand>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsland");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");

            Unit unit = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }


            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsLand land = new ItemsLand();
            land.setIndex(rs.getLong("autoindex"));
            land.setUnit(unit);
            land.setUrban(urban);
            land.setSubUrban(subUrban);
            land.setItemCode(rs.getString("itemcode"));
            land.setItemName(rs.getString("itemname"));
            land.setRegNumber(rs.getString("regnumber"));
            land.setWide(rs.getBigDecimal("wide"));
            land.setAddressLocation(rs.getString("addresslocation"));
            land.setLandPrice(rs.getBigDecimal("landprice"));

            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }

            land.setAcquisitionYear(acquisitionYear);
            land.setFundingSource(rs.getString("fundingsources"));
            land.setAcquisitionWay(rs.getString("acquisitionway"));
            land.setDescription(rs.getString("description"));
            //
            land.setOwnerShipStateName(rs.getString("ownershipstatename"));
            land.setOwnerShipNumberState(rs.getString("ownershipnumberstate"));
            land.setOwnerShipDate(rs.getDate("ownershipdate"));
            land.setOwnerShipIssuedBy(rs.getString("ownershipissuedby"));
            land.setOwner(rs.getString("owners"));
            land.setOwnerShipStatus(rs.getString("ownershipstatus"));
            land.setOwnerShipRelation(rs.getString("ownershiprelation"));
            land.setOwnerShipOccupancy(rs.getString("ownershipoccupancy"));
            land.setOwnerShipOccupying(rs.getString("ownershipoccupying"));
            //
            land.setLandCertificateNumber(rs.getString("landcertificatenumber"));
            land.setLandCertificateDate(rs.getDate("landcertificatedate"));
            land.setLandTaxNumber(rs.getString("landtaxnumber"));
            land.setLandTaxDate(rs.getDate("landtaxdate"));
            land.setBuildPermitNumber(rs.getString("buildpermitnumber"));
            land.setBuildPermitDate(rs.getDate("buildpermitdate"));
            land.setAdvisPlanningNumber(rs.getString("advisplanningnumber"));
            land.setAdvisPlanningDate(rs.getDate("advisplanningdate"));
            //
            land.setLandCondition(rs.getString("condition"));
            land.setShape(rs.getString("shape"));
            land.setTopographCondition(rs.getString("topographcondition"));
            land.setCurrentUse(rs.getString("currentuse"));
            land.setCloserRoad(rs.getString("closerroad"));
            land.setRoadSurface(rs.getString("roadsurface"));
            land.setHeightAbove(rs.getString("heightabove"));
            land.setHeightUnder(rs.getString("heightunder"));
            land.setElevation(rs.getString("elevation"));
            land.setAllotment(rs.getString("allotment"));
            land.setPriceBasedChief(rs.getBigDecimal("pricebasedchief"));
            land.setPriceBasedNJOP(rs.getBigDecimal("pricebasednjop"));
            land.setPriceBasedExam(rs.getBigDecimal("pricebasedexamination"));
            land.setPriceDescription(rs.getString("pricedescription"));
            land.setLocation(rs.getString("locations"));
            land.setPosition(rs.getString("positions"));
            land.setRoadAccess(rs.getString("roadaccess"));
            land.setRoadWidth(rs.getString("roadwidth"));
            land.setPavement(rs.getString("pavement"));
            land.setTraffic(rs.getString("traffic"));
            land.setTopographLocation(rs.getString("topographlocation"));
            land.setEnvironment(rs.getString("environment"));
            land.setDensity(rs.getString("density"));
            land.setSocialLevels(rs.getString("sociallevels"));
            land.setFacilities(rs.getString("facilities"));
            land.setDrainage(rs.getString("drainage"));
            land.setPublicTransportation(rs.getString("publictransportation"));
            land.setSecurity(rs.getString("securitys"));
            land.setSecurityDisturbance(rs.getString("securitydisturbance"));
            land.setFloodDangers(rs.getString("flooddangers"));
            land.setLocationEffect(rs.getString("locationeffect"));
            land.setEnvironmentalInfluences(rs.getString("environmentalinfluences"));
            land.setAllotmentPoint(rs.getString("allotmentpoint"));
            land.setUtilizationPoint(rs.getString("utilizationpoint"));
            land.setLocationPoint(rs.getString("locationpoint"));
            land.setAccessionPoint(rs.getString("accessionpoint"));
            land.setNursingPoint(rs.getString("nursingpoint"));
            land.setSoilConditionsPoint(rs.getString("soilconditionspoint"));
            land.setMarketInterestPoint(rs.getString("marketinterestpoint"));
            //
            land.setComparativePrice(rs.getBigDecimal("comparativeprice1"));
            land.setComparativeWide(rs.getBigDecimal("comparativewide1"));
            land.setComparativeSource(rs.getString("comparativesource1"));
            land.setComparativePrice2(rs.getBigDecimal("comparativeprice2"));
            land.setComparativeWide2(rs.getBigDecimal("comparativewide2"));
            land.setComparativeSource2(rs.getString("comparativesource2"));
            land.setComparativePrice3(rs.getBigDecimal("comparativeprice3"));
            land.setComparativeWide3(rs.getBigDecimal("comparativewide3"));
            land.setComparativeSource3(rs.getString("comparativesource3"));
            land.setComparativePrice4(rs.getBigDecimal("comparativeprice4"));
            land.setComparativeWide4(rs.getBigDecimal("comparativewide4"));
            land.setComparativeSource4(rs.getString("comparativesource4"));
            land.setComparativePrice5(rs.getBigDecimal("comparativeprice5"));
            land.setComparativeWide5(rs.getBigDecimal("comparativewide5"));
            land.setComparativeSource5(rs.getString("comparativesource5"));
            land.setLandAddress(rs.getString("landaddress"));
            land.setNjopYear(rs.getInt("njopyear"));
            land.setNjopLandwide(rs.getBigDecimal("njoplandwide"));
            land.setNjopLandClass(rs.getString("njoplandclass"));
            land.setNjopLandValue(rs.getBigDecimal("njoplandvalue"));
            land.setUserName(rs.getString("username"));
            land.setLastUserName(rs.getString("lastusername"));

            lands.add(land);
        }

        rs.close();
        pstm.close();

        return lands;
    }

    ArrayList<ItemsLand> getItemsLand(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsLand> lands = new ArrayList<ItemsLand>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsland ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");

            Unit unit = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }


            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsLand land = new ItemsLand();
            land.setIndex(rs.getLong("autoindex"));
            land.setUnit(unit);
            land.setUrban(urban);
            land.setSubUrban(subUrban);
            land.setItemCode(rs.getString("itemcode"));
            land.setItemName(rs.getString("itemname"));
            land.setRegNumber(rs.getString("regnumber"));
            land.setWide(rs.getBigDecimal("wide"));
            land.setAddressLocation(rs.getString("addresslocation"));
            land.setLandPrice(rs.getBigDecimal("landprice"));

            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }

            land.setAcquisitionYear(acquisitionYear);
            land.setFundingSource(rs.getString("fundingsources"));
            land.setAcquisitionWay(rs.getString("acquisitionway"));
            land.setDescription(rs.getString("description"));
            //
            land.setOwnerShipStateName(rs.getString("ownershipstatename"));
            land.setOwnerShipNumberState(rs.getString("ownershipnumberstate"));
            land.setOwnerShipDate(rs.getDate("ownershipdate"));
            land.setOwnerShipIssuedBy(rs.getString("ownershipissuedby"));
            land.setOwner(rs.getString("owners"));
            land.setOwnerShipStatus(rs.getString("ownershipstatus"));
            land.setOwnerShipRelation(rs.getString("ownershiprelation"));
            land.setOwnerShipOccupancy(rs.getString("ownershipoccupancy"));
            land.setOwnerShipOccupying(rs.getString("ownershipoccupying"));
            //
            land.setLandCertificateNumber(rs.getString("landcertificatenumber"));
            land.setLandCertificateDate(rs.getDate("landcertificatedate"));
            land.setLandTaxNumber(rs.getString("landtaxnumber"));
            land.setLandTaxDate(rs.getDate("landtaxdate"));
            land.setBuildPermitNumber(rs.getString("buildpermitnumber"));
            land.setBuildPermitDate(rs.getDate("buildpermitdate"));
            land.setAdvisPlanningNumber(rs.getString("advisplanningnumber"));
            land.setAdvisPlanningDate(rs.getDate("advisplanningdate"));
            //
            land.setLandCondition(rs.getString("condition"));
            land.setShape(rs.getString("shape"));
            land.setTopographCondition(rs.getString("topographcondition"));
            land.setCurrentUse(rs.getString("currentuse"));
            land.setCloserRoad(rs.getString("closerroad"));
            land.setRoadSurface(rs.getString("roadsurface"));
            land.setHeightAbove(rs.getString("heightabove"));
            land.setHeightUnder(rs.getString("heightunder"));
            land.setElevation(rs.getString("elevation"));
            land.setAllotment(rs.getString("allotment"));
            land.setPriceBasedChief(rs.getBigDecimal("pricebasedchief"));
            land.setPriceBasedNJOP(rs.getBigDecimal("pricebasednjop"));
            land.setPriceBasedExam(rs.getBigDecimal("pricebasedexamination"));
            land.setPriceDescription(rs.getString("pricedescription"));
            land.setLocation(rs.getString("locations"));
            land.setPosition(rs.getString("positions"));
            land.setRoadAccess(rs.getString("roadaccess"));
            land.setRoadWidth(rs.getString("roadwidth"));
            land.setPavement(rs.getString("pavement"));
            land.setTraffic(rs.getString("traffic"));
            land.setTopographLocation(rs.getString("topographlocation"));
            land.setEnvironment(rs.getString("environment"));
            land.setDensity(rs.getString("density"));
            land.setSocialLevels(rs.getString("sociallevels"));
            land.setFacilities(rs.getString("facilities"));
            land.setDrainage(rs.getString("drainage"));
            land.setPublicTransportation(rs.getString("publictransportation"));
            land.setSecurity(rs.getString("securitys"));
            land.setSecurityDisturbance(rs.getString("securitydisturbance"));
            land.setFloodDangers(rs.getString("flooddangers"));
            land.setLocationEffect(rs.getString("locationeffect"));
            land.setEnvironmentalInfluences(rs.getString("environmentalinfluences"));
            land.setAllotmentPoint(rs.getString("allotmentpoint"));
            land.setUtilizationPoint(rs.getString("utilizationpoint"));
            land.setLocationPoint(rs.getString("locationpoint"));
            land.setAccessionPoint(rs.getString("accessionpoint"));
            land.setNursingPoint(rs.getString("nursingpoint"));
            land.setSoilConditionsPoint(rs.getString("soilconditionspoint"));
            land.setMarketInterestPoint(rs.getString("marketinterestpoint"));
            //
            land.setComparativePrice(rs.getBigDecimal("comparativeprice1"));
            land.setComparativeWide(rs.getBigDecimal("comparativewide1"));
            land.setComparativeSource(rs.getString("comparativesource1"));
            land.setComparativePrice2(rs.getBigDecimal("comparativeprice2"));
            land.setComparativeWide2(rs.getBigDecimal("comparativewide2"));
            land.setComparativeSource2(rs.getString("comparativesource2"));
            land.setComparativePrice3(rs.getBigDecimal("comparativeprice3"));
            land.setComparativeWide3(rs.getBigDecimal("comparativewide3"));
            land.setComparativeSource3(rs.getString("comparativesource3"));
            land.setComparativePrice4(rs.getBigDecimal("comparativeprice4"));
            land.setComparativeWide4(rs.getBigDecimal("comparativewide4"));
            land.setComparativeSource4(rs.getString("comparativesource4"));
            land.setComparativePrice5(rs.getBigDecimal("comparativeprice5"));
            land.setComparativeWide5(rs.getBigDecimal("comparativewide5"));
            land.setComparativeSource5(rs.getString("comparativesource5"));
            land.setLandAddress(rs.getString("landaddress"));
            land.setNjopYear(rs.getInt("njopyear"));
            land.setNjopLandwide(rs.getBigDecimal("njoplandwide"));
            land.setNjopLandClass(rs.getString("njoplandclass"));
            land.setNjopLandValue(rs.getBigDecimal("njoplandvalue"));
            land.setUserName(rs.getString("username"));
            land.setLastUserName(rs.getString("lastusername"));

            lands.add(land);
        }

        rs.close();
        pstm.close();

        return lands;
    }

    ArrayList<ItemsLand> getItemsLandNotInDeleteDraft(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsLand> lands = new ArrayList<ItemsLand>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsland ").append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select itemsindex from deletedraftitems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_LAND).
                    append(")");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select itemsindex from deletedraftitems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_LAND).
                    append(")");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");

            Unit unit = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }


            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsLand land = new ItemsLand();
            land.setIndex(rs.getLong("autoindex"));
            land.setUnit(unit);
            land.setUrban(urban);
            land.setSubUrban(subUrban);
            land.setItemCode(rs.getString("itemcode"));
            land.setItemName(rs.getString("itemname"));
            land.setRegNumber(rs.getString("regnumber"));
            land.setWide(rs.getBigDecimal("wide"));
            land.setAddressLocation(rs.getString("addresslocation"));
            land.setLandPrice(rs.getBigDecimal("landprice"));

            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }

            land.setAcquisitionYear(acquisitionYear);
            land.setFundingSource(rs.getString("fundingsources"));
            land.setAcquisitionWay(rs.getString("acquisitionway"));
            land.setDescription(rs.getString("description"));
            //
            land.setOwnerShipStateName(rs.getString("ownershipstatename"));
            land.setOwnerShipNumberState(rs.getString("ownershipnumberstate"));
            land.setOwnerShipDate(rs.getDate("ownershipdate"));
            land.setOwnerShipIssuedBy(rs.getString("ownershipissuedby"));
            land.setOwner(rs.getString("owners"));
            land.setOwnerShipStatus(rs.getString("ownershipstatus"));
            land.setOwnerShipRelation(rs.getString("ownershiprelation"));
            land.setOwnerShipOccupancy(rs.getString("ownershipoccupancy"));
            land.setOwnerShipOccupying(rs.getString("ownershipoccupying"));
            //
            land.setLandCertificateNumber(rs.getString("landcertificatenumber"));
            land.setLandCertificateDate(rs.getDate("landcertificatedate"));
            land.setLandTaxNumber(rs.getString("landtaxnumber"));
            land.setLandTaxDate(rs.getDate("landtaxdate"));
            land.setBuildPermitNumber(rs.getString("buildpermitnumber"));
            land.setBuildPermitDate(rs.getDate("buildpermitdate"));
            land.setAdvisPlanningNumber(rs.getString("advisplanningnumber"));
            land.setAdvisPlanningDate(rs.getDate("advisplanningdate"));
            //
            land.setLandCondition(rs.getString("condition"));
            land.setShape(rs.getString("shape"));
            land.setTopographCondition(rs.getString("topographcondition"));
            land.setCurrentUse(rs.getString("currentuse"));
            land.setCloserRoad(rs.getString("closerroad"));
            land.setRoadSurface(rs.getString("roadsurface"));
            land.setHeightAbove(rs.getString("heightabove"));
            land.setHeightUnder(rs.getString("heightunder"));
            land.setElevation(rs.getString("elevation"));
            land.setAllotment(rs.getString("allotment"));
            land.setPriceBasedChief(rs.getBigDecimal("pricebasedchief"));
            land.setPriceBasedNJOP(rs.getBigDecimal("pricebasednjop"));
            land.setPriceBasedExam(rs.getBigDecimal("pricebasedexamination"));
            land.setPriceDescription(rs.getString("pricedescription"));
            land.setLocation(rs.getString("locations"));
            land.setPosition(rs.getString("positions"));
            land.setRoadAccess(rs.getString("roadaccess"));
            land.setRoadWidth(rs.getString("roadwidth"));
            land.setPavement(rs.getString("pavement"));
            land.setTraffic(rs.getString("traffic"));
            land.setTopographLocation(rs.getString("topographlocation"));
            land.setEnvironment(rs.getString("environment"));
            land.setDensity(rs.getString("density"));
            land.setSocialLevels(rs.getString("sociallevels"));
            land.setFacilities(rs.getString("facilities"));
            land.setDrainage(rs.getString("drainage"));
            land.setPublicTransportation(rs.getString("publictransportation"));
            land.setSecurity(rs.getString("securitys"));
            land.setSecurityDisturbance(rs.getString("securitydisturbance"));
            land.setFloodDangers(rs.getString("flooddangers"));
            land.setLocationEffect(rs.getString("locationeffect"));
            land.setEnvironmentalInfluences(rs.getString("environmentalinfluences"));
            land.setAllotmentPoint(rs.getString("allotmentpoint"));
            land.setUtilizationPoint(rs.getString("utilizationpoint"));
            land.setLocationPoint(rs.getString("locationpoint"));
            land.setAccessionPoint(rs.getString("accessionpoint"));
            land.setNursingPoint(rs.getString("nursingpoint"));
            land.setSoilConditionsPoint(rs.getString("soilconditionspoint"));
            land.setMarketInterestPoint(rs.getString("marketinterestpoint"));
            //
            land.setComparativePrice(rs.getBigDecimal("comparativeprice1"));
            land.setComparativeWide(rs.getBigDecimal("comparativewide1"));
            land.setComparativeSource(rs.getString("comparativesource1"));
            land.setComparativePrice2(rs.getBigDecimal("comparativeprice2"));
            land.setComparativeWide2(rs.getBigDecimal("comparativewide2"));
            land.setComparativeSource2(rs.getString("comparativesource2"));
            land.setComparativePrice3(rs.getBigDecimal("comparativeprice3"));
            land.setComparativeWide3(rs.getBigDecimal("comparativewide3"));
            land.setComparativeSource3(rs.getString("comparativesource3"));
            land.setComparativePrice4(rs.getBigDecimal("comparativeprice4"));
            land.setComparativeWide4(rs.getBigDecimal("comparativewide4"));
            land.setComparativeSource4(rs.getString("comparativesource4"));
            land.setComparativePrice5(rs.getBigDecimal("comparativeprice5"));
            land.setComparativeWide5(rs.getBigDecimal("comparativewide5"));
            land.setComparativeSource5(rs.getString("comparativesource5"));
            land.setLandAddress(rs.getString("landaddress"));
            land.setNjopYear(rs.getInt("njopyear"));
            land.setNjopLandwide(rs.getBigDecimal("njoplandwide"));
            land.setNjopLandClass(rs.getString("njoplandclass"));
            land.setNjopLandValue(rs.getBigDecimal("njoplandvalue"));
            land.setUserName(rs.getString("username"));
            land.setLastUserName(rs.getString("lastusername"));

            lands.add(land);
        }

        rs.close();
        pstm.close();

        return lands;
    }

    ArrayList<ItemsLand> getItemsLandNotInUsedItems(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsLand> lands = new ArrayList<ItemsLand>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsland ").append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select itemsindex from useditems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_LAND).
                    append(")");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select itemsindex from useditems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_LAND).
                    append(")");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");

            Unit unit = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }


            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsLand land = new ItemsLand();
            land.setIndex(rs.getLong("autoindex"));
            land.setUnit(unit);
            land.setUrban(urban);
            land.setSubUrban(subUrban);
            land.setItemCode(rs.getString("itemcode"));
            land.setItemName(rs.getString("itemname"));
            land.setRegNumber(rs.getString("regnumber"));
            land.setWide(rs.getBigDecimal("wide"));
            land.setAddressLocation(rs.getString("addresslocation"));
            land.setLandPrice(rs.getBigDecimal("landprice"));

            Integer acquisitionYear = rs.getInt("acquisitionyear");

            if (acquisitionYear != null) {
                if (acquisitionYear.equals(Integer.valueOf(0))) {
                    acquisitionYear = null;
                }
            }

            land.setAcquisitionYear(acquisitionYear);
            land.setFundingSource(rs.getString("fundingsources"));
            land.setAcquisitionWay(rs.getString("acquisitionway"));
            land.setDescription(rs.getString("description"));
            //
            land.setOwnerShipStateName(rs.getString("ownershipstatename"));
            land.setOwnerShipNumberState(rs.getString("ownershipnumberstate"));
            land.setOwnerShipDate(rs.getDate("ownershipdate"));
            land.setOwnerShipIssuedBy(rs.getString("ownershipissuedby"));
            land.setOwner(rs.getString("owners"));
            land.setOwnerShipStatus(rs.getString("ownershipstatus"));
            land.setOwnerShipRelation(rs.getString("ownershiprelation"));
            land.setOwnerShipOccupancy(rs.getString("ownershipoccupancy"));
            land.setOwnerShipOccupying(rs.getString("ownershipoccupying"));
            //
            land.setLandCertificateNumber(rs.getString("landcertificatenumber"));
            land.setLandCertificateDate(rs.getDate("landcertificatedate"));
            land.setLandTaxNumber(rs.getString("landtaxnumber"));
            land.setLandTaxDate(rs.getDate("landtaxdate"));
            land.setBuildPermitNumber(rs.getString("buildpermitnumber"));
            land.setBuildPermitDate(rs.getDate("buildpermitdate"));
            land.setAdvisPlanningNumber(rs.getString("advisplanningnumber"));
            land.setAdvisPlanningDate(rs.getDate("advisplanningdate"));
            //
            land.setLandCondition(rs.getString("condition"));
            land.setShape(rs.getString("shape"));
            land.setTopographCondition(rs.getString("topographcondition"));
            land.setCurrentUse(rs.getString("currentuse"));
            land.setCloserRoad(rs.getString("closerroad"));
            land.setRoadSurface(rs.getString("roadsurface"));
            land.setHeightAbove(rs.getString("heightabove"));
            land.setHeightUnder(rs.getString("heightunder"));
            land.setElevation(rs.getString("elevation"));
            land.setAllotment(rs.getString("allotment"));
            land.setPriceBasedChief(rs.getBigDecimal("pricebasedchief"));
            land.setPriceBasedNJOP(rs.getBigDecimal("pricebasednjop"));
            land.setPriceBasedExam(rs.getBigDecimal("pricebasedexamination"));
            land.setPriceDescription(rs.getString("pricedescription"));
            land.setLocation(rs.getString("locations"));
            land.setPosition(rs.getString("positions"));
            land.setRoadAccess(rs.getString("roadaccess"));
            land.setRoadWidth(rs.getString("roadwidth"));
            land.setPavement(rs.getString("pavement"));
            land.setTraffic(rs.getString("traffic"));
            land.setTopographLocation(rs.getString("topographlocation"));
            land.setEnvironment(rs.getString("environment"));
            land.setDensity(rs.getString("density"));
            land.setSocialLevels(rs.getString("sociallevels"));
            land.setFacilities(rs.getString("facilities"));
            land.setDrainage(rs.getString("drainage"));
            land.setPublicTransportation(rs.getString("publictransportation"));
            land.setSecurity(rs.getString("securitys"));
            land.setSecurityDisturbance(rs.getString("securitydisturbance"));
            land.setFloodDangers(rs.getString("flooddangers"));
            land.setLocationEffect(rs.getString("locationeffect"));
            land.setEnvironmentalInfluences(rs.getString("environmentalinfluences"));
            land.setAllotmentPoint(rs.getString("allotmentpoint"));
            land.setUtilizationPoint(rs.getString("utilizationpoint"));
            land.setLocationPoint(rs.getString("locationpoint"));
            land.setAccessionPoint(rs.getString("accessionpoint"));
            land.setNursingPoint(rs.getString("nursingpoint"));
            land.setSoilConditionsPoint(rs.getString("soilconditionspoint"));
            land.setMarketInterestPoint(rs.getString("marketinterestpoint"));
            //
            land.setComparativePrice(rs.getBigDecimal("comparativeprice1"));
            land.setComparativeWide(rs.getBigDecimal("comparativewide1"));
            land.setComparativeSource(rs.getString("comparativesource1"));
            land.setComparativePrice2(rs.getBigDecimal("comparativeprice2"));
            land.setComparativeWide2(rs.getBigDecimal("comparativewide2"));
            land.setComparativeSource2(rs.getString("comparativesource2"));
            land.setComparativePrice3(rs.getBigDecimal("comparativeprice3"));
            land.setComparativeWide3(rs.getBigDecimal("comparativewide3"));
            land.setComparativeSource3(rs.getString("comparativesource3"));
            land.setComparativePrice4(rs.getBigDecimal("comparativeprice4"));
            land.setComparativeWide4(rs.getBigDecimal("comparativewide4"));
            land.setComparativeSource4(rs.getString("comparativesource4"));
            land.setComparativePrice5(rs.getBigDecimal("comparativeprice5"));
            land.setComparativeWide5(rs.getBigDecimal("comparativewide5"));
            land.setComparativeSource5(rs.getString("comparativesource5"));
            land.setLandAddress(rs.getString("landaddress"));
            land.setNjopYear(rs.getInt("njopyear"));
            land.setNjopLandwide(rs.getBigDecimal("njoplandwide"));
            land.setNjopLandClass(rs.getString("njoplandclass"));
            land.setNjopLandValue(rs.getBigDecimal("njoplandvalue"));
            land.setUserName(rs.getString("username"));
            land.setLastUserName(rs.getString("lastusername"));

            lands.add(land);
        }

        rs.close();
        pstm.close();

        return lands;
    }

    void insertItemsNetwork(Connection conn, ItemsNetwork network) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO itemsnetwork( ").
                append("unit, itemcode, itemname, regnumber, documentnumber, ").
                append("documentdate, constructiontype,lengths,width,wide, price, condition,addresslocation,urban,suburban, landstatus, landcode, ").
                append("fundingsources, acquisitionway, description, styearbuilt, stlocation, stflatness, ").
                append("ststartkm, stendkm, stwidth, stsurface, stside,sttrotoire,stchannel, stsafetyzone, brdgstandardtype,").
                append("brdgyearbuild, brdglengths, brdgwidth, brdgpurpose, brdgmainmaterial,").
                append("brdgtopshape, brdgothershape, brdgheadshape, brdgheadmaterial,").
                append("brdgpillar, brdgpillarmaterial, irryearbuilt, irrbuildingtype, ").
                append("irrlengths, irrheight, irrwidth, irrbuildingmaterial, irrcondition, ").
                append("irrcarriertype, irrcarrierlengths, irrcarrierheight, irrcarrierwidth, ").
                append("irrcarriermaterial, irrcarriercondition, irrdebitthresholdwidth, ").
                append("irrdebitcdg, irrdebitcipolleti, irrdebitlengths, irrdebitheight,").
                append("irrdebitwidth, irrdebitbuildingmaterial, irrdebitcondition, irrpoolusbr3, ").
                append("irrpoolblock, irrpoolusbr4, irrpoolvlogtor, irrpoollengths, irrpoolheight, ").
                append("irrpoolwidth, irrpoolbuildingmaterial, irrpoolcondition, irrhwfslotblock, ").
                append("irrhwftrapesium, irrhwfslide, irrhwftreetop, irrhwflengths, irrhwfheight, ").
                append("irrhwfwidth, irrhwfbuildingmaterial, irrhwfcondition, irrprottransfer, ").
                append("irrprotdisposal, irrprotdrain, irrprotlengths, irrprotheight, ").
                append("irrprotwidth, irrprotbuildingmaterial, irrprotcondition, irrtapfor,").
                append("irrtapsecond, irrtapregulator, irrtapthird, irrtaplength, irrtapheight,").
                append("irrtapwidth, irrtapbuildingmaterial, irrtapcondition, irrsupplevee,").
                append("irrsuppwash, irrsuppbridge, irrsuppkrib, irrsupplength, irrsuppheight,").
                append("irrsuppwidth, irrsuppbuildingmaterial, irrsuppcondition,username,lastusername) ").
                append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,").
                append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ").
                append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ").
                append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ").
                append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ").
                append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ").
                append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ").
                append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ").
                append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ").
                append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ").
                append("?, ?, ?, ?, ?, ?, ?, ?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        if (network.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getUnit().getIndex());
        }

        pstm.setString(++col, network.getItemCode());
        pstm.setString(++col, network.getItemName());
        pstm.setString(++col, network.getRegNumber());
        pstm.setString(++col, network.getDocumentNumber());

        if (network.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(network.getDocumentDate().getTime()));
        }

        pstm.setString(++col, network.getConstructionType());
        pstm.setInt(++col, network.getLength());
        pstm.setInt(++col, network.getWidth());
        pstm.setBigDecimal(++col, network.getWide());
        pstm.setBigDecimal(++col, network.getPrice());

        if (network.getCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getCondition().getIndex());
        }

        pstm.setString(++col, network.getAddressLocation());

        if (network.getUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getUrban().getIndex());
        }

        if (network.getSubUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getSubUrban().getIndex());
        }

        pstm.setString(++col, network.getLandStatus());
        pstm.setString(++col, network.getLandCode());
        pstm.setString(++col, network.getFundingSource());
        pstm.setString(++col, network.getAcquisitionWay());
        pstm.setString(++col, network.getDescription());
        //
        pstm.setInt(++col, network.getStreetYearBuild());
        pstm.setString(++col, network.getStreetLocation());
        pstm.setString(++col, network.getStreetFlatness());
        pstm.setInt(++col, network.getStreetStartKM());
        pstm.setInt(++col, network.getStreetEndKM());
        pstm.setString(++col, network.getStreetWidth());
        pstm.setString(++col, network.getStreetSurface());
        pstm.setString(++col, network.getStreetSide());
        pstm.setString(++col, network.getStreetTrotoire());
        pstm.setString(++col, network.getStreetChannel());
        pstm.setBoolean(++col, network.isStreetSafetyZone());
        //
        pstm.setString(++col, network.getBridgeStandarType());
        pstm.setInt(++col, network.getBridgeYearBuild());
        pstm.setInt(++col, network.getBridgeLength());
        pstm.setInt(++col, network.getBridgeWidth());
        pstm.setString(++col, network.getBridgePurpose());
        pstm.setString(++col, network.getBridgeMainMaterial());
        pstm.setString(++col, network.getBridgeTopShape());
        pstm.setString(++col, network.getBridgeOtherShape());
        pstm.setString(++col, network.getBridgeHeadShape());
        pstm.setString(++col, network.getBridgeHeadMaterial());
        pstm.setString(++col, network.getBridgePillar());
        pstm.setString(++col, network.getBridgePillarMaterial());
        //
        pstm.setInt(++col, network.getIrrigationYearBuild());
        pstm.setString(++col, network.getIrrigationBuildingType());
        pstm.setInt(++col, network.getIrrigationLength());
        pstm.setInt(++col, network.getIrrigationHeight());
        pstm.setInt(++col, network.getIrrigationWidth());
        pstm.setString(++col, network.getIrrigationBuildingMaterial());

        if (network.getIrrigationCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationCondition().getIndex());
        }
        //
        pstm.setString(++col, network.getIrrigationCarrierType());
        pstm.setInt(++col, network.getIrrigationCarrierLength());
        pstm.setInt(++col, network.getIrrigationCarrierHeight());
        pstm.setInt(++col, network.getIrrigationCarrierWidth());
        pstm.setString(++col, network.getIrrigationCarrierMaterial());
        if (network.getIrrigationCarrierCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationCarrierCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationDebitThresholdWidth());
        pstm.setInt(++col, network.getIrrigationDebitCDG());
        pstm.setInt(++col, network.getIrrigationDebitCipolleti());
        pstm.setInt(++col, network.getIrrigationDebitLength());
        pstm.setInt(++col, network.getIrrigationDebitHeight());
        pstm.setInt(++col, network.getIrrigationDebitWidth());
        pstm.setString(++col, network.getIrrigationDebitMaterial());
        if (network.getIrrigationDebitCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationDebitCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationPoolUSBR3());
        pstm.setInt(++col, network.getIrrigationPoolBlock());
        pstm.setInt(++col, network.getIrrigationPoolUSBR4());
        pstm.setInt(++col, network.getIrrigationPoolVlogtor());
        pstm.setInt(++col, network.getIrrigationPoolLength());
        pstm.setInt(++col, network.getIrrigationPoolHeight());
        pstm.setInt(++col, network.getIrrigationPoolWidth());
        pstm.setString(++col, network.getIrrigationPoolMaterial());
        if (network.getIrrigationPoolCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationPoolCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationHWFLotBlock());
        pstm.setInt(++col, network.getIrrigationHWFTrapesium());
        pstm.setInt(++col, network.getIrrigationHWFSlide());
        pstm.setInt(++col, network.getIrrigationHWFTreeTop());
        pstm.setInt(++col, network.getIrrigationHWFLength());
        pstm.setInt(++col, network.getIrrigationHWFHeight());
        pstm.setInt(++col, network.getIrrigationHWFWidth());
        pstm.setString(++col, network.getIrrigationHWFMaterial());
        if (network.getIrrigationHWFCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationHWFCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationProtectTransfer());
        pstm.setInt(++col, network.getIrrigationProtectDisposal());
        pstm.setInt(++col, network.getIrrigationProtectDrain());
        pstm.setInt(++col, network.getIrrigationProtectLength());
        pstm.setInt(++col, network.getIrrigationProtectHeight());
        pstm.setInt(++col, network.getIrrigationProtectWidth());
        pstm.setString(++col, network.getIrrigationProtectMaterial());
        if (network.getIrrigationProtectCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationProtectCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationTappedFor());
        pstm.setInt(++col, network.getIrrigationTappedSecond());
        pstm.setInt(++col, network.getIrrigationTappedRegulator());
        pstm.setInt(++col, network.getIrrigationTappedThird());
        pstm.setInt(++col, network.getIrrigationTappedLength());
        pstm.setInt(++col, network.getIrrigationTappedHeight());
        pstm.setInt(++col, network.getIrrigationTappedWidth());
        pstm.setString(++col, network.getIrrigationTappedMaterial());
        if (network.getIrrigationTappedCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationTappedCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationSupportLevee());
        pstm.setInt(++col, network.getIrrigationSupportWash());
        pstm.setInt(++col, network.getIrrigationSupportBridge());
        pstm.setInt(++col, network.getIrrigationSupportKrib());
        pstm.setInt(++col, network.getIrrigationSupportLength());
        pstm.setInt(++col, network.getIrrigationSupportHeight());
        pstm.setInt(++col, network.getIrrigationSupportWidth());
        pstm.setString(++col, network.getIrrigationSupportMaterial());
        if (network.getIrrigationSupportCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationSupportCondition().getIndex());
        }
        pstm.setString(++col, network.getUserName());
        pstm.setString(++col, network.getLastUserName());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateItemsNetwork(Connection conn, Long oldIndex, ItemsNetwork network) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE itemsnetwork ").
                append("SET unit=?, itemcode=?, itemname=?, regnumber=?, documentnumber=?, ").
                append("documentdate=?, constructiontype=?, lengths=?,width=?,wide=?, price=?, condition=?,").
                append("addresslocation=?,urban=?,suburban=?,landstatus=?, landcode=?, fundingsources=?, acquisitionway=?,description=?, styearbuilt=?,").
                append("stlocation=?, stflatness=?, ststartkm=?, stendkm=?, stwidth=?, ").
                append("stsurface=?, stside=?,sttrotoire=?,stchannel=?, stsafetyzone=?, brdgstandardtype=?, brdgyearbuild=?, ").
                append("brdglengths=?, brdgwidth=?, brdgpurpose=?, brdgmainmaterial=?, ").
                append("brdgtopshape=?, brdgothershape=?, brdgheadshape=?, brdgheadmaterial=?, ").
                append("brdgpillar=?, brdgpillarmaterial=?, irryearbuilt=?, irrbuildingtype=?, ").
                append("irrlengths=?, irrheight=?, irrwidth=?, irrbuildingmaterial=?, ").
                append("irrcondition=?, irrcarriertype=?, irrcarrierlengths=?, irrcarrierheight=?, ").
                append("irrcarrierwidth=?, irrcarriermaterial=?, irrcarriercondition=?, ").
                append("irrdebitthresholdwidth=?, irrdebitcdg=?, irrdebitcipolleti=?, ").
                append("irrdebitlengths=?, irrdebitheight=?, irrdebitwidth=?, irrdebitbuildingmaterial=?, ").
                append("irrdebitcondition=?, irrpoolusbr3=?, irrpoolblock=?, irrpoolusbr4=?, ").
                append("irrpoolvlogtor=?, irrpoollengths=?, irrpoolheight=?, irrpoolwidth=?, ").
                append("irrpoolbuildingmaterial=?, irrpoolcondition=?, irrhwfslotblock=?, ").
                append("irrhwftrapesium=?, irrhwfslide=?, irrhwftreetop=?, irrhwflengths=?, ").
                append("irrhwfheight=?, irrhwfwidth=?, irrhwfbuildingmaterial=?, irrhwfcondition=?, ").
                append("irrprottransfer=?, irrprotdisposal=?, irrprotdrain=?, irrprotlengths=?, ").
                append("irrprotheight=?, irrprotwidth=?, irrprotbuildingmaterial=?, irrprotcondition=?, ").
                append("irrtapfor=?, irrtapsecond=?, irrtapregulator=?, irrtapthird=?, ").
                append("irrtaplength=?, irrtapheight=?, irrtapwidth=?, irrtapbuildingmaterial=?, ").
                append("irrtapcondition=?, irrsupplevee=?, irrsuppwash=?, irrsuppbridge=?, ").
                append("irrsuppkrib=?, irrsupplength=?, irrsuppheight=?, irrsuppwidth=?,").
                append("irrsuppbuildingmaterial=?, irrsuppcondition=?,lastusername = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        if (network.getUnit() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getUnit().getIndex());
        }

        pstm.setString(++col, network.getItemCode());
        pstm.setString(++col, network.getItemName());
        pstm.setString(++col, network.getRegNumber());
        pstm.setString(++col, network.getDocumentNumber());

        if (network.getDocumentDate() == null) {
            pstm.setNull(++col, Types.DATE);
        } else {
            pstm.setDate(++col, new java.sql.Date(network.getDocumentDate().getTime()));
        }

        pstm.setString(++col, network.getConstructionType());
        pstm.setInt(++col, network.getLength());
        pstm.setInt(++col, network.getWidth());
        pstm.setBigDecimal(++col, network.getWide());
        pstm.setBigDecimal(++col, network.getPrice());

        if (network.getCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getCondition().getIndex());
        }

        pstm.setString(++col, network.getAddressLocation());

        if (network.getUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getUrban().getIndex());
        }

        if (network.getSubUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getSubUrban().getIndex());
        }

        pstm.setString(++col, network.getLandStatus());
        pstm.setString(++col, network.getLandCode());
        pstm.setString(++col, network.getFundingSource());
        pstm.setString(++col, network.getAcquisitionWay());
        pstm.setString(++col, network.getDescription());
        //
        pstm.setInt(++col, network.getStreetYearBuild());
        pstm.setString(++col, network.getStreetLocation());
        pstm.setString(++col, network.getStreetFlatness());
        pstm.setInt(++col, network.getStreetStartKM());
        pstm.setInt(++col, network.getStreetEndKM());
        pstm.setString(++col, network.getStreetWidth());
        pstm.setString(++col, network.getStreetSurface());
        pstm.setString(++col, network.getStreetSide());
        pstm.setString(++col, network.getStreetTrotoire());
        pstm.setString(++col, network.getStreetChannel());
        pstm.setBoolean(++col, network.isStreetSafetyZone());
        //
        pstm.setString(++col, network.getBridgeStandarType());
        pstm.setInt(++col, network.getBridgeYearBuild());
        pstm.setInt(++col, network.getBridgeLength());
        pstm.setInt(++col, network.getBridgeWidth());
        pstm.setString(++col, network.getBridgePurpose());
        pstm.setString(++col, network.getBridgeMainMaterial());
        pstm.setString(++col, network.getBridgeTopShape());
        pstm.setString(++col, network.getBridgeOtherShape());
        pstm.setString(++col, network.getBridgeHeadShape());
        pstm.setString(++col, network.getBridgeHeadMaterial());
        pstm.setString(++col, network.getBridgePillar());
        pstm.setString(++col, network.getBridgePillarMaterial());
        //
        pstm.setInt(++col, network.getIrrigationYearBuild());
        pstm.setString(++col, network.getIrrigationBuildingType());
        pstm.setInt(++col, network.getIrrigationLength());
        pstm.setInt(++col, network.getIrrigationHeight());
        pstm.setInt(++col, network.getIrrigationWidth());
        pstm.setString(++col, network.getIrrigationBuildingMaterial());

        if (network.getIrrigationCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationCondition().getIndex());
        }
        //
        pstm.setString(++col, network.getIrrigationCarrierType());
        pstm.setInt(++col, network.getIrrigationCarrierLength());
        pstm.setInt(++col, network.getIrrigationCarrierHeight());
        pstm.setInt(++col, network.getIrrigationCarrierWidth());
        pstm.setString(++col, network.getIrrigationCarrierMaterial());
        if (network.getIrrigationCarrierCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationCarrierCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationDebitThresholdWidth());
        pstm.setInt(++col, network.getIrrigationDebitCDG());
        pstm.setInt(++col, network.getIrrigationDebitCipolleti());
        pstm.setInt(++col, network.getIrrigationDebitLength());
        pstm.setInt(++col, network.getIrrigationDebitHeight());
        pstm.setInt(++col, network.getIrrigationDebitWidth());
        pstm.setString(++col, network.getIrrigationDebitMaterial());
        if (network.getIrrigationDebitCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationDebitCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationPoolUSBR3());
        pstm.setInt(++col, network.getIrrigationPoolBlock());
        pstm.setInt(++col, network.getIrrigationPoolUSBR4());
        pstm.setInt(++col, network.getIrrigationPoolVlogtor());
        pstm.setInt(++col, network.getIrrigationPoolLength());
        pstm.setInt(++col, network.getIrrigationPoolHeight());
        pstm.setInt(++col, network.getIrrigationPoolWidth());
        pstm.setString(++col, network.getIrrigationPoolMaterial());
        if (network.getIrrigationPoolCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationPoolCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationHWFLotBlock());
        pstm.setInt(++col, network.getIrrigationHWFTrapesium());
        pstm.setInt(++col, network.getIrrigationHWFSlide());
        pstm.setInt(++col, network.getIrrigationHWFTreeTop());
        pstm.setInt(++col, network.getIrrigationHWFLength());
        pstm.setInt(++col, network.getIrrigationHWFHeight());
        pstm.setInt(++col, network.getIrrigationHWFWidth());
        pstm.setString(++col, network.getIrrigationHWFMaterial());
        if (network.getIrrigationHWFCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationHWFCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationProtectTransfer());
        pstm.setInt(++col, network.getIrrigationProtectDisposal());
        pstm.setInt(++col, network.getIrrigationProtectDrain());
        pstm.setInt(++col, network.getIrrigationProtectLength());
        pstm.setInt(++col, network.getIrrigationProtectHeight());
        pstm.setInt(++col, network.getIrrigationProtectWidth());
        pstm.setString(++col, network.getIrrigationProtectMaterial());
        if (network.getIrrigationProtectCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationProtectCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationTappedFor());
        pstm.setInt(++col, network.getIrrigationTappedSecond());
        pstm.setInt(++col, network.getIrrigationTappedRegulator());
        pstm.setInt(++col, network.getIrrigationTappedThird());
        pstm.setInt(++col, network.getIrrigationTappedLength());
        pstm.setInt(++col, network.getIrrigationTappedHeight());
        pstm.setInt(++col, network.getIrrigationTappedWidth());
        pstm.setString(++col, network.getIrrigationTappedMaterial());
        if (network.getIrrigationTappedCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationTappedCondition().getIndex());
        }
        //
        pstm.setInt(++col, network.getIrrigationSupportLevee());
        pstm.setInt(++col, network.getIrrigationSupportWash());
        pstm.setInt(++col, network.getIrrigationSupportBridge());
        pstm.setInt(++col, network.getIrrigationSupportKrib());
        pstm.setInt(++col, network.getIrrigationSupportLength());
        pstm.setInt(++col, network.getIrrigationSupportHeight());
        pstm.setInt(++col, network.getIrrigationSupportWidth());
        pstm.setString(++col, network.getIrrigationSupportMaterial());
        if (network.getIrrigationSupportCondition() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, network.getIrrigationSupportCondition().getIndex());
        }
        pstm.setString(++col, network.getLastUserName());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsNetwork(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsnetwork where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }
    
    void deleteItemsNetwork(Connection conn, Unit unit) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsnetwork where unit = ?");
        pstm.setLong(1, unit.getIndex());
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsNetwork(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsnetwork where itemcode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ItemsNetwork> getItemsNetwork(Connection conn) throws SQLException {
        ArrayList<ItemsNetwork> networks = new ArrayList<ItemsNetwork>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsnetwork");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");
            Long irrConditionIndex = rs.getLong("irrcondition");
            Long irrcarrierconIndex = rs.getLong("irrcarriercondition");
            Long irrDebitConditionIndex = rs.getLong("irrdebitcondition");
            Long irrPoolConditionIndex = rs.getLong("irrpoolcondition");
            Long irrHWFConditionIndex = rs.getLong("irrhwfcondition");
            Long irrProtectConditionIndex = rs.getLong("irrprotcondition");
            Long irrTappedConditionIndex = rs.getLong("irrtapcondition");
            Long irrSupportConditionIndex = rs.getLong("irrsuppcondition");

            Unit unit = null;
            Condition condition = null;
            Condition irrCondition = null;
            Condition irrCarrierCondition = null;
            Condition irrDebitCondition = null;
            Condition irrPoolCondition = null;
            Condition irrHWFCondition = null;
            Condition irrProtectCondition = null;
            Condition irrTappedCondition = null;
            Condition irrSupportCondition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            if (irrConditionIndex != null) {
                irrCondition = new Condition(irrConditionIndex);
            }

            if (irrcarrierconIndex != null) {
                irrCarrierCondition = new Condition(irrcarrierconIndex);
            }

            if (irrDebitConditionIndex != null) {
                irrDebitCondition = new Condition(irrDebitConditionIndex);
            }

            if (irrPoolConditionIndex != null) {
                irrPoolCondition = new Condition(irrPoolConditionIndex);
            }

            if (irrHWFConditionIndex != null) {
                irrHWFCondition = new Condition(irrHWFConditionIndex);
            }

            if (irrProtectConditionIndex != null) {
                irrProtectCondition = new Condition(irrProtectConditionIndex);
            }

            if (irrTappedConditionIndex != null) {
                irrTappedCondition = new Condition(irrTappedConditionIndex);
            }

            if (irrSupportConditionIndex != null) {
                irrSupportCondition = new Condition(irrSupportConditionIndex);
            }

            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsNetwork network = new ItemsNetwork();
            network.setIndex(rs.getLong("autoindex"));
            network.setUnit(unit);
            network.setItemCode(rs.getString("itemcode"));
            network.setItemName(rs.getString("itemname"));
            network.setRegNumber(rs.getString("regnumber"));
            network.setDocumentNumber(rs.getString("documentnumber"));
            network.setDocumentDate(rs.getDate("documentdate"));
            network.setConstructionType(rs.getString("constructiontype"));
            network.setLength(rs.getInt("lengths"));
            network.setWidth(rs.getInt("width"));
            network.setWide(rs.getBigDecimal("wide"));
            network.setPrice(rs.getBigDecimal("price"));
            network.setCondition(condition);
            network.setAddressLocation(rs.getString("addresslocation"));
            network.setUrban(urban);
            network.setSubUrban(subUrban);
            network.setLandStatus(rs.getString("landstatus"));
            network.setLandCode(rs.getString("landcode"));
            network.setFundingSource(rs.getString("fundingsources"));
            network.setAcquisitionWay(rs.getString("acquisitionway"));
            network.setDescription(rs.getString("description"));
            //
            network.setStreetYearBuild(rs.getInt("styearbuilt"));
            network.setStreetLocation(rs.getString("stlocation"));
            network.setStreetFlatness(rs.getString("stflatness"));
            network.setStreetStartKM(rs.getInt("ststartkm"));
            network.setStreetEndKM(rs.getInt("stendkm"));
            network.setStreetWidth(rs.getString("stwidth"));
            network.setStreetSurface(rs.getString("stsurface"));
            network.setStreetSide(rs.getString("stside"));
            network.setStreetTrotoire(rs.getString("sttrotoire"));
            network.setStreetChannel(rs.getString("stchannel"));
            network.setStreetSafetyZone(rs.getBoolean("stsafetyzone"));
            //
            network.setBridgeStandarType(rs.getString("brdgstandardtype"));
            network.setBridgeYearBuild(rs.getInt("brdgyearbuild"));
            network.setBridgeLength(rs.getInt("brdglengths"));
            network.setBridgeWidth(rs.getInt("brdgwidth"));
            network.setBridgePurpose(rs.getString("brdgpurpose"));
            network.setBridgeMainMaterial(rs.getString("brdgmainmaterial"));
            network.setBridgeTopShape(rs.getString("brdgtopshape"));
            network.setBridgeOtherShape(rs.getString("brdgothershape"));
            network.setBridgeHeadShape(rs.getString("brdgheadshape"));
            network.setBridgeHeadMaterial(rs.getString("brdgheadmaterial"));
            network.setBridgePillar(rs.getString("brdgpillar"));
            network.setBridgePillarMaterial(rs.getString("brdgpillarmaterial"));
            //
            network.setIrrigationYearBuild(rs.getInt("irryearbuilt"));
            network.setIrrigationBuildingType(rs.getString("irrbuildingtype"));
            network.setIrrigationLength(rs.getInt("irrlengths"));
            network.setIrrigationHeight(rs.getInt("irrheight"));
            network.setIrrigationWidth(rs.getInt("irrwidth"));
            network.setIrrigationBuildingMaterial(rs.getString("irrbuildingmaterial"));
            network.setIrrigationCondition(irrCondition);
            //
            network.setIrrigationCarrierType(rs.getString("irrcarriertype"));
            network.setIrrigationCarrierLength(rs.getInt("irrcarrierlengths"));
            network.setIrrigationCarrierHeight(rs.getInt("irrcarrierheight"));
            network.setIrrigationCarrierWidth(rs.getInt("irrcarrierwidth"));
            network.setIrrigationCarrierMaterial(rs.getString("irrcarriermaterial"));
            network.setIrrigationCarrierCondition(irrCarrierCondition);
            //
            network.setIrrigationDebitThresholdWidth(rs.getInt("irrdebitthresholdwidth"));
            network.setIrrigationDebitCDG(rs.getInt("irrdebitcdg"));
            network.setIrrigationDebitCipolleti(rs.getInt("irrdebitcipolleti"));
            network.setIrrigationDebitLength(rs.getInt("irrdebitlengths"));
            network.setIrrigationDebitHeight(rs.getInt("irrdebitheight"));
            network.setIrrigationDebitWidth(rs.getInt("irrdebitwidth"));
            network.setIrrigationDebitMaterial(rs.getString("irrdebitbuildingmaterial"));
            network.setIrrigationDebitCondition(irrDebitCondition);
            //
            network.setIrrigationPoolUSBR3(rs.getInt("irrpoolusbr3"));
            network.setIrrigationPoolBlock(rs.getInt("irrpoolblock"));
            network.setIrrigationPoolUSBR4(rs.getInt("irrpoolusbr4"));
            network.setIrrigationPoolVlogtor(rs.getInt("irrpoolvlogtor"));
            network.setIrrigationPoolLength(rs.getInt("irrpoollengths"));
            network.setIrrigationPoolHeight(rs.getInt("irrpoolheight"));
            network.setIrrigationPoolWidth(rs.getInt("irrpoolwidth"));
            network.setIrrigationPoolMaterial(rs.getString("irrpoolbuildingmaterial"));
            network.setIrrigationPoolCondition(irrPoolCondition);
            //
            network.setIrrigationHWFLotBlock(rs.getInt("irrhwfslotblock"));
            network.setIrrigationHWFTrapesium(rs.getInt("irrhwftrapesium"));
            network.setIrrigationHWFSlide(rs.getInt("irrhwfslide"));
            network.setIrrigationHWFTreeTop(rs.getInt("irrhwftreetop"));
            network.setIrrigationHWFLength(rs.getInt("irrhwflengths"));
            network.setIrrigationHWFHeight(rs.getInt("irrhwfheight"));
            network.setIrrigationHWFWidth(rs.getInt("irrhwfwidth"));
            network.setIrrigationHWFMaterial(rs.getString("irrhwfbuildingmaterial"));
            network.setIrrigationHWFCondition(irrHWFCondition);
            //
            network.setIrrigationProtectTransfer(rs.getInt("irrprottransfer"));
            network.setIrrigationProtectDisposal(rs.getInt("irrprotdisposal"));
            network.setIrrigationProtectDrain(rs.getInt("irrprotdrain"));
            network.setIrrigationProtectLength(rs.getInt("irrprotlengths"));
            network.setIrrigationProtectHeight(rs.getInt("irrprotheight"));
            network.setIrrigationProtectWidth(rs.getInt("irrprotwidth"));
            network.setIrrigationProtectMaterial(rs.getString("irrprotbuildingmaterial"));
            network.setIrrigationProtectCondition(irrProtectCondition);
            //
            network.setIrrigationTappedFor(rs.getInt("irrtapfor"));
            network.setIrrigationTappedSecond(rs.getInt("irrtapsecond"));
            network.setIrrigationTappedRegulator(rs.getInt("irrtapregulator"));
            network.setIrrigationTappedThird(rs.getInt("irrtapthird"));
            network.setIrrigationTappedLength(rs.getInt("irrtaplength"));
            network.setIrrigationTappedHeight(rs.getInt("irrtapheight"));
            network.setIrrigationTappedWidth(rs.getInt("irrtapwidth"));
            network.setIrrigationTappedMaterial(rs.getString("irrtapbuildingmaterial"));
            network.setIrrigationTappedCondition(irrTappedCondition);
            //
            network.setIrrigationSupportLevee(rs.getInt("irrsupplevee"));
            network.setIrrigationSupportWash(rs.getInt("irrsuppwash"));
            network.setIrrigationSupportBridge(rs.getInt("irrsuppbridge"));
            network.setIrrigationSupportKrib(rs.getInt("irrsuppkrib"));
            network.setIrrigationSupportLength(rs.getInt("irrsupplength"));
            network.setIrrigationSupportHeight(rs.getInt("irrsuppheight"));
            network.setIrrigationSupportWidth(rs.getInt("irrsuppwidth"));
            network.setIrrigationSupportMaterial(rs.getString("irrsuppbuildingmaterial"));
            network.setIrrigationSupportCondition(irrSupportCondition);

            network.setUserName(rs.getString("username"));
            network.setLastUserName(rs.getString("lastusername"));

            networks.add(network);
        }

        rs.close();
        pstm.close();

        return networks;
    }

    ArrayList<ItemsNetwork> getItemsNetwork(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsNetwork> networks = new ArrayList<ItemsNetwork>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsnetwork ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");
            Long irrConditionIndex = rs.getLong("irrcondition");
            Long irrcarrierconIndex = rs.getLong("irrcarriercondition");
            Long irrDebitConditionIndex = rs.getLong("irrdebitcondition");
            Long irrPoolConditionIndex = rs.getLong("irrpoolcondition");
            Long irrHWFConditionIndex = rs.getLong("irrhwfcondition");
            Long irrProtectConditionIndex = rs.getLong("irrprotcondition");
            Long irrTappedConditionIndex = rs.getLong("irrtapcondition");
            Long irrSupportConditionIndex = rs.getLong("irrsuppcondition");

            Unit unit = null;
            Condition condition = null;
            Condition irrCondition = null;
            Condition irrCarrierCondition = null;
            Condition irrDebitCondition = null;
            Condition irrPoolCondition = null;
            Condition irrHWFCondition = null;
            Condition irrProtectCondition = null;
            Condition irrTappedCondition = null;
            Condition irrSupportCondition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            if (irrConditionIndex != null) {
                irrCondition = new Condition(irrConditionIndex);
            }

            if (irrcarrierconIndex != null) {
                irrCarrierCondition = new Condition(irrcarrierconIndex);
            }

            if (irrDebitConditionIndex != null) {
                irrDebitCondition = new Condition(irrDebitConditionIndex);
            }

            if (irrPoolConditionIndex != null) {
                irrPoolCondition = new Condition(irrPoolConditionIndex);
            }

            if (irrHWFConditionIndex != null) {
                irrHWFCondition = new Condition(irrHWFConditionIndex);
            }

            if (irrProtectConditionIndex != null) {
                irrProtectCondition = new Condition(irrProtectConditionIndex);
            }

            if (irrTappedConditionIndex != null) {
                irrTappedCondition = new Condition(irrTappedConditionIndex);
            }

            if (irrSupportConditionIndex != null) {
                irrSupportCondition = new Condition(irrSupportConditionIndex);
            }

            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsNetwork network = new ItemsNetwork();
            network.setIndex(rs.getLong("autoindex"));
            network.setUnit(unit);
            network.setItemCode(rs.getString("itemcode"));
            network.setItemName(rs.getString("itemname"));
            network.setRegNumber(rs.getString("regnumber"));
            network.setDocumentNumber(rs.getString("documentnumber"));
            network.setDocumentDate(rs.getDate("documentdate"));
            network.setConstructionType(rs.getString("constructiontype"));
            network.setLength(rs.getInt("lengths"));
            network.setWidth(rs.getInt("width"));
            network.setWide(rs.getBigDecimal("wide"));
            network.setPrice(rs.getBigDecimal("price"));
            network.setCondition(condition);
            network.setAddressLocation(rs.getString("addresslocation"));
            network.setUrban(urban);
            network.setSubUrban(subUrban);
            network.setLandStatus(rs.getString("landstatus"));
            network.setLandCode(rs.getString("landcode"));
            network.setFundingSource(rs.getString("fundingsources"));
            network.setAcquisitionWay(rs.getString("acquisitionway"));
            network.setDescription(rs.getString("description"));
            //
            network.setStreetYearBuild(rs.getInt("styearbuilt"));
            network.setStreetLocation(rs.getString("stlocation"));
            network.setStreetFlatness(rs.getString("stflatness"));
            network.setStreetStartKM(rs.getInt("ststartkm"));
            network.setStreetEndKM(rs.getInt("stendkm"));
            network.setStreetWidth(rs.getString("stwidth"));
            network.setStreetSurface(rs.getString("stsurface"));
            network.setStreetSide(rs.getString("stside"));
            network.setStreetTrotoire(rs.getString("sttrotoire"));
            network.setStreetChannel(rs.getString("stchannel"));
            network.setStreetSafetyZone(rs.getBoolean("stsafetyzone"));
            //
            network.setBridgeStandarType(rs.getString("brdgstandardtype"));
            network.setBridgeYearBuild(rs.getInt("brdgyearbuild"));
            network.setBridgeLength(rs.getInt("brdglengths"));
            network.setBridgeWidth(rs.getInt("brdgwidth"));
            network.setBridgePurpose(rs.getString("brdgpurpose"));
            network.setBridgeMainMaterial(rs.getString("brdgmainmaterial"));
            network.setBridgeTopShape(rs.getString("brdgtopshape"));
            network.setBridgeOtherShape(rs.getString("brdgothershape"));
            network.setBridgeHeadShape(rs.getString("brdgheadshape"));
            network.setBridgeHeadMaterial(rs.getString("brdgheadmaterial"));
            network.setBridgePillar(rs.getString("brdgpillar"));
            network.setBridgePillarMaterial(rs.getString("brdgpillarmaterial"));
            //
            network.setIrrigationYearBuild(rs.getInt("irryearbuilt"));
            network.setIrrigationBuildingType(rs.getString("irrbuildingtype"));
            network.setIrrigationLength(rs.getInt("irrlengths"));
            network.setIrrigationHeight(rs.getInt("irrheight"));
            network.setIrrigationWidth(rs.getInt("irrwidth"));
            network.setIrrigationBuildingMaterial(rs.getString("irrbuildingmaterial"));
            network.setIrrigationCondition(irrCondition);
            //
            network.setIrrigationCarrierType(rs.getString("irrcarriertype"));
            network.setIrrigationCarrierLength(rs.getInt("irrcarrierlengths"));
            network.setIrrigationCarrierHeight(rs.getInt("irrcarrierheight"));
            network.setIrrigationCarrierWidth(rs.getInt("irrcarrierwidth"));
            network.setIrrigationCarrierMaterial(rs.getString("irrcarriermaterial"));
            network.setIrrigationCarrierCondition(irrCarrierCondition);
            //
            network.setIrrigationDebitThresholdWidth(rs.getInt("irrdebitthresholdwidth"));
            network.setIrrigationDebitCDG(rs.getInt("irrdebitcdg"));
            network.setIrrigationDebitCipolleti(rs.getInt("irrdebitcipolleti"));
            network.setIrrigationDebitLength(rs.getInt("irrdebitlengths"));
            network.setIrrigationDebitHeight(rs.getInt("irrdebitheight"));
            network.setIrrigationDebitWidth(rs.getInt("irrdebitwidth"));
            network.setIrrigationDebitMaterial(rs.getString("irrdebitbuildingmaterial"));
            network.setIrrigationDebitCondition(irrDebitCondition);
            //
            network.setIrrigationPoolUSBR3(rs.getInt("irrpoolusbr3"));
            network.setIrrigationPoolBlock(rs.getInt("irrpoolblock"));
            network.setIrrigationPoolUSBR4(rs.getInt("irrpoolusbr4"));
            network.setIrrigationPoolVlogtor(rs.getInt("irrpoolvlogtor"));
            network.setIrrigationPoolLength(rs.getInt("irrpoollengths"));
            network.setIrrigationPoolHeight(rs.getInt("irrpoolheight"));
            network.setIrrigationPoolWidth(rs.getInt("irrpoolwidth"));
            network.setIrrigationPoolMaterial(rs.getString("irrpoolbuildingmaterial"));
            network.setIrrigationPoolCondition(irrPoolCondition);
            //
            network.setIrrigationHWFLotBlock(rs.getInt("irrhwfslotblock"));
            network.setIrrigationHWFTrapesium(rs.getInt("irrhwftrapesium"));
            network.setIrrigationHWFSlide(rs.getInt("irrhwfslide"));
            network.setIrrigationHWFTreeTop(rs.getInt("irrhwftreetop"));
            network.setIrrigationHWFLength(rs.getInt("irrhwflengths"));
            network.setIrrigationHWFHeight(rs.getInt("irrhwfheight"));
            network.setIrrigationHWFWidth(rs.getInt("irrhwfwidth"));
            network.setIrrigationHWFMaterial(rs.getString("irrhwfbuildingmaterial"));
            network.setIrrigationHWFCondition(irrHWFCondition);
            //
            network.setIrrigationProtectTransfer(rs.getInt("irrprottransfer"));
            network.setIrrigationProtectDisposal(rs.getInt("irrprotdisposal"));
            network.setIrrigationProtectDrain(rs.getInt("irrprotdrain"));
            network.setIrrigationProtectLength(rs.getInt("irrprotlengths"));
            network.setIrrigationProtectHeight(rs.getInt("irrprotheight"));
            network.setIrrigationProtectWidth(rs.getInt("irrprotwidth"));
            network.setIrrigationProtectMaterial(rs.getString("irrprotbuildingmaterial"));
            network.setIrrigationProtectCondition(irrProtectCondition);
            //
            network.setIrrigationTappedFor(rs.getInt("irrtapfor"));
            network.setIrrigationTappedSecond(rs.getInt("irrtapsecond"));
            network.setIrrigationTappedRegulator(rs.getInt("irrtapregulator"));
            network.setIrrigationTappedThird(rs.getInt("irrtapthird"));
            network.setIrrigationTappedLength(rs.getInt("irrtaplength"));
            network.setIrrigationTappedHeight(rs.getInt("irrtapheight"));
            network.setIrrigationTappedWidth(rs.getInt("irrtapwidth"));
            network.setIrrigationTappedMaterial(rs.getString("irrtapbuildingmaterial"));
            network.setIrrigationTappedCondition(irrTappedCondition);
            //
            network.setIrrigationSupportLevee(rs.getInt("irrsupplevee"));
            network.setIrrigationSupportWash(rs.getInt("irrsuppwash"));
            network.setIrrigationSupportBridge(rs.getInt("irrsuppbridge"));
            network.setIrrigationSupportKrib(rs.getInt("irrsuppkrib"));
            network.setIrrigationSupportLength(rs.getInt("irrsupplength"));
            network.setIrrigationSupportHeight(rs.getInt("irrsuppheight"));
            network.setIrrigationSupportWidth(rs.getInt("irrsuppwidth"));
            network.setIrrigationSupportMaterial(rs.getString("irrsuppbuildingmaterial"));
            network.setIrrigationSupportCondition(irrSupportCondition);

            network.setUserName(rs.getString("username"));
            network.setLastUserName(rs.getString("lastusername"));

            networks.add(network);
        }

        rs.close();
        pstm.close();

        return networks;
    }

    ArrayList<ItemsNetwork> getItemsNetworkNotInDeleteDraft(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsNetwork> networks = new ArrayList<ItemsNetwork>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsnetwork ").append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select itemsindex from deletedraftitems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_NETWORK).
                    append(")");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select itemsindex from deletedraftitems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_NETWORK).
                    append(")");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");
            Long irrConditionIndex = rs.getLong("irrcondition");
            Long irrcarrierconIndex = rs.getLong("irrcarriercondition");
            Long irrDebitConditionIndex = rs.getLong("irrdebitcondition");
            Long irrPoolConditionIndex = rs.getLong("irrpoolcondition");
            Long irrHWFConditionIndex = rs.getLong("irrhwfcondition");
            Long irrProtectConditionIndex = rs.getLong("irrprotcondition");
            Long irrTappedConditionIndex = rs.getLong("irrtapcondition");
            Long irrSupportConditionIndex = rs.getLong("irrsuppcondition");

            Unit unit = null;
            Condition condition = null;
            Condition irrCondition = null;
            Condition irrCarrierCondition = null;
            Condition irrDebitCondition = null;
            Condition irrPoolCondition = null;
            Condition irrHWFCondition = null;
            Condition irrProtectCondition = null;
            Condition irrTappedCondition = null;
            Condition irrSupportCondition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            if (irrConditionIndex != null) {
                irrCondition = new Condition(irrConditionIndex);
            }

            if (irrcarrierconIndex != null) {
                irrCarrierCondition = new Condition(irrcarrierconIndex);
            }

            if (irrDebitConditionIndex != null) {
                irrDebitCondition = new Condition(irrDebitConditionIndex);
            }

            if (irrPoolConditionIndex != null) {
                irrPoolCondition = new Condition(irrPoolConditionIndex);
            }

            if (irrHWFConditionIndex != null) {
                irrHWFCondition = new Condition(irrHWFConditionIndex);
            }

            if (irrProtectConditionIndex != null) {
                irrProtectCondition = new Condition(irrProtectConditionIndex);
            }

            if (irrTappedConditionIndex != null) {
                irrTappedCondition = new Condition(irrTappedConditionIndex);
            }

            if (irrSupportConditionIndex != null) {
                irrSupportCondition = new Condition(irrSupportConditionIndex);
            }

            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsNetwork network = new ItemsNetwork();
            network.setIndex(rs.getLong("autoindex"));
            network.setUnit(unit);
            network.setItemCode(rs.getString("itemcode"));
            network.setItemName(rs.getString("itemname"));
            network.setRegNumber(rs.getString("regnumber"));
            network.setDocumentNumber(rs.getString("documentnumber"));
            network.setDocumentDate(rs.getDate("documentdate"));
            network.setConstructionType(rs.getString("constructiontype"));
            network.setLength(rs.getInt("lengths"));
            network.setWidth(rs.getInt("width"));
            network.setWide(rs.getBigDecimal("wide"));
            network.setPrice(rs.getBigDecimal("price"));
            network.setCondition(condition);
            network.setAddressLocation(rs.getString("addresslocation"));
            network.setUrban(urban);
            network.setSubUrban(subUrban);
            network.setLandStatus(rs.getString("landstatus"));
            network.setLandCode(rs.getString("landcode"));
            network.setFundingSource(rs.getString("fundingsources"));
            network.setAcquisitionWay(rs.getString("acquisitionway"));
            network.setDescription(rs.getString("description"));
            //
            network.setStreetYearBuild(rs.getInt("styearbuilt"));
            network.setStreetLocation(rs.getString("stlocation"));
            network.setStreetFlatness(rs.getString("stflatness"));
            network.setStreetStartKM(rs.getInt("ststartkm"));
            network.setStreetEndKM(rs.getInt("stendkm"));
            network.setStreetWidth(rs.getString("stwidth"));
            network.setStreetSurface(rs.getString("stsurface"));
            network.setStreetSide(rs.getString("stside"));
            network.setStreetTrotoire(rs.getString("sttrotoire"));
            network.setStreetChannel(rs.getString("stchannel"));
            network.setStreetSafetyZone(rs.getBoolean("stsafetyzone"));
            //
            network.setBridgeStandarType(rs.getString("brdgstandardtype"));
            network.setBridgeYearBuild(rs.getInt("brdgyearbuild"));
            network.setBridgeLength(rs.getInt("brdglengths"));
            network.setBridgeWidth(rs.getInt("brdgwidth"));
            network.setBridgePurpose(rs.getString("brdgpurpose"));
            network.setBridgeMainMaterial(rs.getString("brdgmainmaterial"));
            network.setBridgeTopShape(rs.getString("brdgtopshape"));
            network.setBridgeOtherShape(rs.getString("brdgothershape"));
            network.setBridgeHeadShape(rs.getString("brdgheadshape"));
            network.setBridgeHeadMaterial(rs.getString("brdgheadmaterial"));
            network.setBridgePillar(rs.getString("brdgpillar"));
            network.setBridgePillarMaterial(rs.getString("brdgpillarmaterial"));
            //
            network.setIrrigationYearBuild(rs.getInt("irryearbuilt"));
            network.setIrrigationBuildingType(rs.getString("irrbuildingtype"));
            network.setIrrigationLength(rs.getInt("irrlengths"));
            network.setIrrigationHeight(rs.getInt("irrheight"));
            network.setIrrigationWidth(rs.getInt("irrwidth"));
            network.setIrrigationBuildingMaterial(rs.getString("irrbuildingmaterial"));
            network.setIrrigationCondition(irrCondition);
            //
            network.setIrrigationCarrierType(rs.getString("irrcarriertype"));
            network.setIrrigationCarrierLength(rs.getInt("irrcarrierlengths"));
            network.setIrrigationCarrierHeight(rs.getInt("irrcarrierheight"));
            network.setIrrigationCarrierWidth(rs.getInt("irrcarrierwidth"));
            network.setIrrigationCarrierMaterial(rs.getString("irrcarriermaterial"));
            network.setIrrigationCarrierCondition(irrCarrierCondition);
            //
            network.setIrrigationDebitThresholdWidth(rs.getInt("irrdebitthresholdwidth"));
            network.setIrrigationDebitCDG(rs.getInt("irrdebitcdg"));
            network.setIrrigationDebitCipolleti(rs.getInt("irrdebitcipolleti"));
            network.setIrrigationDebitLength(rs.getInt("irrdebitlengths"));
            network.setIrrigationDebitHeight(rs.getInt("irrdebitheight"));
            network.setIrrigationDebitWidth(rs.getInt("irrdebitwidth"));
            network.setIrrigationDebitMaterial(rs.getString("irrdebitbuildingmaterial"));
            network.setIrrigationDebitCondition(irrDebitCondition);
            //
            network.setIrrigationPoolUSBR3(rs.getInt("irrpoolusbr3"));
            network.setIrrigationPoolBlock(rs.getInt("irrpoolblock"));
            network.setIrrigationPoolUSBR4(rs.getInt("irrpoolusbr4"));
            network.setIrrigationPoolVlogtor(rs.getInt("irrpoolvlogtor"));
            network.setIrrigationPoolLength(rs.getInt("irrpoollengths"));
            network.setIrrigationPoolHeight(rs.getInt("irrpoolheight"));
            network.setIrrigationPoolWidth(rs.getInt("irrpoolwidth"));
            network.setIrrigationPoolMaterial(rs.getString("irrpoolbuildingmaterial"));
            network.setIrrigationPoolCondition(irrPoolCondition);
            //
            network.setIrrigationHWFLotBlock(rs.getInt("irrhwfslotblock"));
            network.setIrrigationHWFTrapesium(rs.getInt("irrhwftrapesium"));
            network.setIrrigationHWFSlide(rs.getInt("irrhwfslide"));
            network.setIrrigationHWFTreeTop(rs.getInt("irrhwftreetop"));
            network.setIrrigationHWFLength(rs.getInt("irrhwflengths"));
            network.setIrrigationHWFHeight(rs.getInt("irrhwfheight"));
            network.setIrrigationHWFWidth(rs.getInt("irrhwfwidth"));
            network.setIrrigationHWFMaterial(rs.getString("irrhwfbuildingmaterial"));
            network.setIrrigationHWFCondition(irrHWFCondition);
            //
            network.setIrrigationProtectTransfer(rs.getInt("irrprottransfer"));
            network.setIrrigationProtectDisposal(rs.getInt("irrprotdisposal"));
            network.setIrrigationProtectDrain(rs.getInt("irrprotdrain"));
            network.setIrrigationProtectLength(rs.getInt("irrprotlengths"));
            network.setIrrigationProtectHeight(rs.getInt("irrprotheight"));
            network.setIrrigationProtectWidth(rs.getInt("irrprotwidth"));
            network.setIrrigationProtectMaterial(rs.getString("irrprotbuildingmaterial"));
            network.setIrrigationProtectCondition(irrProtectCondition);
            //
            network.setIrrigationTappedFor(rs.getInt("irrtapfor"));
            network.setIrrigationTappedSecond(rs.getInt("irrtapsecond"));
            network.setIrrigationTappedRegulator(rs.getInt("irrtapregulator"));
            network.setIrrigationTappedThird(rs.getInt("irrtapthird"));
            network.setIrrigationTappedLength(rs.getInt("irrtaplength"));
            network.setIrrigationTappedHeight(rs.getInt("irrtapheight"));
            network.setIrrigationTappedWidth(rs.getInt("irrtapwidth"));
            network.setIrrigationTappedMaterial(rs.getString("irrtapbuildingmaterial"));
            network.setIrrigationTappedCondition(irrTappedCondition);
            //
            network.setIrrigationSupportLevee(rs.getInt("irrsupplevee"));
            network.setIrrigationSupportWash(rs.getInt("irrsuppwash"));
            network.setIrrigationSupportBridge(rs.getInt("irrsuppbridge"));
            network.setIrrigationSupportKrib(rs.getInt("irrsuppkrib"));
            network.setIrrigationSupportLength(rs.getInt("irrsupplength"));
            network.setIrrigationSupportHeight(rs.getInt("irrsuppheight"));
            network.setIrrigationSupportWidth(rs.getInt("irrsuppwidth"));
            network.setIrrigationSupportMaterial(rs.getString("irrsuppbuildingmaterial"));
            network.setIrrigationSupportCondition(irrSupportCondition);

            network.setUserName(rs.getString("username"));
            network.setLastUserName(rs.getString("lastusername"));

            networks.add(network);
        }

        rs.close();
        pstm.close();

        return networks;
    }

    ArrayList<ItemsNetwork> getItemsNetworkNotInUsedItems(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsNetwork> networks = new ArrayList<ItemsNetwork>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsnetwork ").append(modifier);

        if (query.toString().contains("where")) {
            query.append(" and autoindex not in ").
                    append(" (select itemsindex from useditems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_NETWORK).
                    append(")");
        } else {
            query.append(" where autoindex not in ").
                    append(" (select itemsindex from useditems ").
                    append(" where itemstype = ").append(ItemsCategory.ITEMS_NETWORK).
                    append(")");
        }

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Long unitIndex = rs.getLong("unit");
            Long conditionIndex = rs.getLong("condition");
            Long irrConditionIndex = rs.getLong("irrcondition");
            Long irrcarrierconIndex = rs.getLong("irrcarriercondition");
            Long irrDebitConditionIndex = rs.getLong("irrdebitcondition");
            Long irrPoolConditionIndex = rs.getLong("irrpoolcondition");
            Long irrHWFConditionIndex = rs.getLong("irrhwfcondition");
            Long irrProtectConditionIndex = rs.getLong("irrprotcondition");
            Long irrTappedConditionIndex = rs.getLong("irrtapcondition");
            Long irrSupportConditionIndex = rs.getLong("irrsuppcondition");

            Unit unit = null;
            Condition condition = null;
            Condition irrCondition = null;
            Condition irrCarrierCondition = null;
            Condition irrDebitCondition = null;
            Condition irrPoolCondition = null;
            Condition irrHWFCondition = null;
            Condition irrProtectCondition = null;
            Condition irrTappedCondition = null;
            Condition irrSupportCondition = null;

            if (unitIndex != null) {
                unit = new Unit(unitIndex);
            }

            if (conditionIndex != null) {
                condition = new Condition(conditionIndex);
            }

            if (irrConditionIndex != null) {
                irrCondition = new Condition(irrConditionIndex);
            }

            if (irrcarrierconIndex != null) {
                irrCarrierCondition = new Condition(irrcarrierconIndex);
            }

            if (irrDebitConditionIndex != null) {
                irrDebitCondition = new Condition(irrDebitConditionIndex);
            }

            if (irrPoolConditionIndex != null) {
                irrPoolCondition = new Condition(irrPoolConditionIndex);
            }

            if (irrHWFConditionIndex != null) {
                irrHWFCondition = new Condition(irrHWFConditionIndex);
            }

            if (irrProtectConditionIndex != null) {
                irrProtectCondition = new Condition(irrProtectConditionIndex);
            }

            if (irrTappedConditionIndex != null) {
                irrTappedCondition = new Condition(irrTappedConditionIndex);
            }

            if (irrSupportConditionIndex != null) {
                irrSupportCondition = new Condition(irrSupportConditionIndex);
            }

            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;


            if (urbanIndex != null) {
                if (!urbanIndex.equals(Long.valueOf(0))) {
                    urban = new Region(urbanIndex);
                }
            }

            if (subUrbanIndex != null) {
                if (!subUrbanIndex.equals(Long.valueOf(0))) {
                    subUrban = new Region(subUrbanIndex);
                }
            }

            ItemsNetwork network = new ItemsNetwork();
            network.setIndex(rs.getLong("autoindex"));
            network.setUnit(unit);
            network.setItemCode(rs.getString("itemcode"));
            network.setItemName(rs.getString("itemname"));
            network.setRegNumber(rs.getString("regnumber"));
            network.setDocumentNumber(rs.getString("documentnumber"));
            network.setDocumentDate(rs.getDate("documentdate"));
            network.setConstructionType(rs.getString("constructiontype"));
            network.setLength(rs.getInt("lengths"));
            network.setWidth(rs.getInt("width"));
            network.setWide(rs.getBigDecimal("wide"));
            network.setPrice(rs.getBigDecimal("price"));
            network.setCondition(condition);
            network.setAddressLocation(rs.getString("addresslocation"));
            network.setUrban(urban);
            network.setSubUrban(subUrban);
            network.setLandStatus(rs.getString("landstatus"));
            network.setLandCode(rs.getString("landcode"));
            network.setFundingSource(rs.getString("fundingsources"));
            network.setAcquisitionWay(rs.getString("acquisitionway"));
            network.setDescription(rs.getString("description"));
            //
            network.setStreetYearBuild(rs.getInt("styearbuilt"));
            network.setStreetLocation(rs.getString("stlocation"));
            network.setStreetFlatness(rs.getString("stflatness"));
            network.setStreetStartKM(rs.getInt("ststartkm"));
            network.setStreetEndKM(rs.getInt("stendkm"));
            network.setStreetWidth(rs.getString("stwidth"));
            network.setStreetSurface(rs.getString("stsurface"));
            network.setStreetSide(rs.getString("stside"));
            network.setStreetTrotoire(rs.getString("sttrotoire"));
            network.setStreetChannel(rs.getString("stchannel"));
            network.setStreetSafetyZone(rs.getBoolean("stsafetyzone"));
            //
            network.setBridgeStandarType(rs.getString("brdgstandardtype"));
            network.setBridgeYearBuild(rs.getInt("brdgyearbuild"));
            network.setBridgeLength(rs.getInt("brdglengths"));
            network.setBridgeWidth(rs.getInt("brdgwidth"));
            network.setBridgePurpose(rs.getString("brdgpurpose"));
            network.setBridgeMainMaterial(rs.getString("brdgmainmaterial"));
            network.setBridgeTopShape(rs.getString("brdgtopshape"));
            network.setBridgeOtherShape(rs.getString("brdgothershape"));
            network.setBridgeHeadShape(rs.getString("brdgheadshape"));
            network.setBridgeHeadMaterial(rs.getString("brdgheadmaterial"));
            network.setBridgePillar(rs.getString("brdgpillar"));
            network.setBridgePillarMaterial(rs.getString("brdgpillarmaterial"));
            //
            network.setIrrigationYearBuild(rs.getInt("irryearbuilt"));
            network.setIrrigationBuildingType(rs.getString("irrbuildingtype"));
            network.setIrrigationLength(rs.getInt("irrlengths"));
            network.setIrrigationHeight(rs.getInt("irrheight"));
            network.setIrrigationWidth(rs.getInt("irrwidth"));
            network.setIrrigationBuildingMaterial(rs.getString("irrbuildingmaterial"));
            network.setIrrigationCondition(irrCondition);
            //
            network.setIrrigationCarrierType(rs.getString("irrcarriertype"));
            network.setIrrigationCarrierLength(rs.getInt("irrcarrierlengths"));
            network.setIrrigationCarrierHeight(rs.getInt("irrcarrierheight"));
            network.setIrrigationCarrierWidth(rs.getInt("irrcarrierwidth"));
            network.setIrrigationCarrierMaterial(rs.getString("irrcarriermaterial"));
            network.setIrrigationCarrierCondition(irrCarrierCondition);
            //
            network.setIrrigationDebitThresholdWidth(rs.getInt("irrdebitthresholdwidth"));
            network.setIrrigationDebitCDG(rs.getInt("irrdebitcdg"));
            network.setIrrigationDebitCipolleti(rs.getInt("irrdebitcipolleti"));
            network.setIrrigationDebitLength(rs.getInt("irrdebitlengths"));
            network.setIrrigationDebitHeight(rs.getInt("irrdebitheight"));
            network.setIrrigationDebitWidth(rs.getInt("irrdebitwidth"));
            network.setIrrigationDebitMaterial(rs.getString("irrdebitbuildingmaterial"));
            network.setIrrigationDebitCondition(irrDebitCondition);
            //
            network.setIrrigationPoolUSBR3(rs.getInt("irrpoolusbr3"));
            network.setIrrigationPoolBlock(rs.getInt("irrpoolblock"));
            network.setIrrigationPoolUSBR4(rs.getInt("irrpoolusbr4"));
            network.setIrrigationPoolVlogtor(rs.getInt("irrpoolvlogtor"));
            network.setIrrigationPoolLength(rs.getInt("irrpoollengths"));
            network.setIrrigationPoolHeight(rs.getInt("irrpoolheight"));
            network.setIrrigationPoolWidth(rs.getInt("irrpoolwidth"));
            network.setIrrigationPoolMaterial(rs.getString("irrpoolbuildingmaterial"));
            network.setIrrigationPoolCondition(irrPoolCondition);
            //
            network.setIrrigationHWFLotBlock(rs.getInt("irrhwfslotblock"));
            network.setIrrigationHWFTrapesium(rs.getInt("irrhwftrapesium"));
            network.setIrrigationHWFSlide(rs.getInt("irrhwfslide"));
            network.setIrrigationHWFTreeTop(rs.getInt("irrhwftreetop"));
            network.setIrrigationHWFLength(rs.getInt("irrhwflengths"));
            network.setIrrigationHWFHeight(rs.getInt("irrhwfheight"));
            network.setIrrigationHWFWidth(rs.getInt("irrhwfwidth"));
            network.setIrrigationHWFMaterial(rs.getString("irrhwfbuildingmaterial"));
            network.setIrrigationHWFCondition(irrHWFCondition);
            //
            network.setIrrigationProtectTransfer(rs.getInt("irrprottransfer"));
            network.setIrrigationProtectDisposal(rs.getInt("irrprotdisposal"));
            network.setIrrigationProtectDrain(rs.getInt("irrprotdrain"));
            network.setIrrigationProtectLength(rs.getInt("irrprotlengths"));
            network.setIrrigationProtectHeight(rs.getInt("irrprotheight"));
            network.setIrrigationProtectWidth(rs.getInt("irrprotwidth"));
            network.setIrrigationProtectMaterial(rs.getString("irrprotbuildingmaterial"));
            network.setIrrigationProtectCondition(irrProtectCondition);
            //
            network.setIrrigationTappedFor(rs.getInt("irrtapfor"));
            network.setIrrigationTappedSecond(rs.getInt("irrtapsecond"));
            network.setIrrigationTappedRegulator(rs.getInt("irrtapregulator"));
            network.setIrrigationTappedThird(rs.getInt("irrtapthird"));
            network.setIrrigationTappedLength(rs.getInt("irrtaplength"));
            network.setIrrigationTappedHeight(rs.getInt("irrtapheight"));
            network.setIrrigationTappedWidth(rs.getInt("irrtapwidth"));
            network.setIrrigationTappedMaterial(rs.getString("irrtapbuildingmaterial"));
            network.setIrrigationTappedCondition(irrTappedCondition);
            //
            network.setIrrigationSupportLevee(rs.getInt("irrsupplevee"));
            network.setIrrigationSupportWash(rs.getInt("irrsuppwash"));
            network.setIrrigationSupportBridge(rs.getInt("irrsuppbridge"));
            network.setIrrigationSupportKrib(rs.getInt("irrsuppkrib"));
            network.setIrrigationSupportLength(rs.getInt("irrsupplength"));
            network.setIrrigationSupportHeight(rs.getInt("irrsuppheight"));
            network.setIrrigationSupportWidth(rs.getInt("irrsuppwidth"));
            network.setIrrigationSupportMaterial(rs.getString("irrsuppbuildingmaterial"));
            network.setIrrigationSupportCondition(irrSupportCondition);

            network.setUserName(rs.getString("username"));
            network.setLastUserName(rs.getString("lastusername"));

            networks.add(network);
        }

        rs.close();
        pstm.close();

        return networks;
    }

    void insertItemsMachineRoom(Connection conn, ItemsMachineRoom room) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into itemsmachineroom(room, items)").
                append("values(?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        if (room.getRoom() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, room.getRoom().getIndex());
        }


        if (room.getMachine() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, room.getMachine().getIndex());
        }

        pstm.executeUpdate();
        pstm.close();
    }

    void updateItemsMachineRoom(Connection conn, Long oldIndex, ItemsMachineRoom room) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update itemsmachineroom set room = ?, items=? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        if (room.getRoom() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, room.getRoom().getIndex());
        }

        if (room.getMachine() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, room.getMachine().getIndex());
        }

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsMachineRoom(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsmachineroom where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ItemsMachineRoom> getItemsMachineRoom(Connection conn) throws SQLException {
        ArrayList<ItemsMachineRoom> rooms = new ArrayList<ItemsMachineRoom>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsmachineroom");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            Long roomIndex = rs.getLong("room");

            Room room = null;

            if (roomIndex != null) {
                if (!roomIndex.equals(Long.valueOf(0))) {
                    room = new Room(roomIndex);
                }
            }

            Long machineIndex = rs.getLong("items");

            ItemsMachine machine = null;

            if (machineIndex != null) {
                if (!machineIndex.equals(Long.valueOf(0))) {
                    machine = new ItemsMachine();
                    machine.setIndex(machineIndex);
                }
            }


            ItemsMachineRoom roomInventory = new ItemsMachineRoom();
            roomInventory.setIndex(rs.getLong("autoindex"));
            roomInventory.setRoom(room);
            roomInventory.setMachine(machine);

            rooms.add(roomInventory);
        }

        rs.close();
        pstm.close();

        return rooms;
    }

    ArrayList<ItemsMachineRoom> getItemsMachineRoom(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsMachineRoom> rooms = new ArrayList<ItemsMachineRoom>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsmachineroom ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            Long roomIndex = rs.getLong("room");

            Room room = null;

            if (roomIndex != null) {
                if (!roomIndex.equals(Long.valueOf(0))) {
                    room = new Room(roomIndex);
                }
            }

            Long machineIndex = rs.getLong("items");

            ItemsMachine machine = null;

            if (machineIndex != null) {
                if (!machineIndex.equals(Long.valueOf(0))) {
                    machine = new ItemsMachine();
                    machine.setIndex(machineIndex);
                }
            }


            ItemsMachineRoom roomInventory = new ItemsMachineRoom();
            roomInventory.setIndex(rs.getLong("autoindex"));
            roomInventory.setRoom(room);
            roomInventory.setMachine(machine);

            rooms.add(roomInventory);
        }

        rs.close();
        pstm.close();

        return rooms;
    }

    void insertItemsFixedAssetRoom(Connection conn, ItemsFixedAssetRoom room) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into itemsfixedassetroom(room, items)").
                append("values(?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        if (room.getRoom() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, room.getRoom().getIndex());
        }


        if (room.getFixedAsset() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, room.getFixedAsset().getIndex());
        }

        pstm.executeUpdate();
        pstm.close();
    }

    void updateItemsFixedAssetRoom(Connection conn, Long oldIndex, ItemsFixedAssetRoom room) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update itemsfixedassetroom set room = ?, items=? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        if (room.getRoom() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, room.getRoom().getIndex());
        }

        if (room.getFixedAsset() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, room.getFixedAsset().getIndex());
        }

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsFixedAssetRoom(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemsfixedassetroom where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ItemsFixedAssetRoom> getItemsFixedAssetRoom(Connection conn) throws SQLException {
        ArrayList<ItemsFixedAssetRoom> rooms = new ArrayList<ItemsFixedAssetRoom>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsfixedassetroom");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            Long roomIndex = rs.getLong("room");

            Room room = null;

            if (roomIndex != null) {
                if (!roomIndex.equals(Long.valueOf(0))) {
                    room = new Room(roomIndex);
                }
            }

            Long fixedIndex = rs.getLong("items");

            ItemsFixedAsset fixedAsset = null;

            if (fixedIndex != null) {
                if (!fixedIndex.equals(Long.valueOf(0))) {
                    fixedAsset = new ItemsFixedAsset();
                    fixedAsset.setIndex(fixedIndex);
                }
            }


            ItemsFixedAssetRoom roomInventory = new ItemsFixedAssetRoom();
            roomInventory.setIndex(rs.getLong("autoindex"));
            roomInventory.setRoom(room);
            roomInventory.setFixedAsset(fixedAsset);

            rooms.add(roomInventory);
        }

        rs.close();
        pstm.close();

        return rooms;
    }

    ArrayList<ItemsFixedAssetRoom> getItemsFixedAssetRoom(Connection conn, String modifier) throws SQLException {
        ArrayList<ItemsFixedAssetRoom> rooms = new ArrayList<ItemsFixedAssetRoom>();

        StringBuilder query = new StringBuilder();
        query.append("select * from itemsfixedassetroom ").append(modifier);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            Long roomIndex = rs.getLong("room");

            Room room = null;

            if (roomIndex != null) {
                if (!roomIndex.equals(Long.valueOf(0))) {
                    room = new Room(roomIndex);
                }
            }

            Long fixedIndex = rs.getLong("items");

            ItemsFixedAsset fixedAsset = null;

            if (fixedIndex != null) {
                if (!fixedIndex.equals(Long.valueOf(0))) {
                    fixedAsset = new ItemsFixedAsset();
                    fixedAsset.setIndex(fixedIndex);
                }
            }


            ItemsFixedAssetRoom roomInventory = new ItemsFixedAssetRoom();
            roomInventory.setIndex(rs.getLong("autoindex"));
            roomInventory.setRoom(room);
            roomInventory.setFixedAsset(fixedAsset);

            rooms.add(roomInventory);
        }

        rs.close();
        pstm.close();

        return rooms;
    }

    // Rekap Inventaris
    ArrayList<InventoryRekap> getInventoryRekap(Connection conn, String modifier) throws SQLException {
        ArrayList<InventoryRekap> rekaps = new ArrayList<InventoryRekap>();

        StringBuilder query = new StringBuilder();
        query.append("select categorycode,categoryname, coalesce(total,0) total, coalesce(price,0) price from itemsCategory ic ").
                append("left join ").
                append("( ").
                append("select * from ").
                append("(select itemcode,total,price from ").
                append("(select substring(itemcode,1,5) itemcode,count(itemcode) total, sum(price) as price ").
                append("from itemsmachine ").
                append(modifier).
                append(" group by substring(itemcode,1,5))  as im2 ").
                append("union ").
                append("select itemcode, total,price  from ").
                append("(select substring(itemcode,1,2) itemcode, count(itemcode) total, sum(price) as price ").
                append("from itemsmachine ").
                append(modifier).
                append(" group by substring(itemcode,1,2))  as im5) as imx ").
                append("union ").
                append("select * from ").
                append("(select itemcode, total, price from ").
                append("(select substring(itemcode,1,2) itemcode,count(itemcode) total, sum(landprice) as price ").
                append("from itemsland ").
                append(modifier).
                append(" group by substring(itemcode,1,2)) as il2 ").
                append("union ").
                append("select itemcode, total, price from ").
                append("(select substring(itemcode,1,5) itemcode,count(itemcode) total, sum(landprice) as price ").
                append("from itemsland ").
                append(modifier).
                append(" group by substring(itemcode,1,5)) as il5) ilx ").
                append("union ").
                append("select * from ").
                append("(select itemcode,total,price from ").
                append("(select substring(itemcode,1,2) itemcode,count(itemcode) total, sum(buildingprice) price ").
                append("from itemsbuilding ").
                append(modifier).
                append(" group by substring(itemcode,1,2))  as ib2 ").
                append("union ").
                append("select itemcode, total,price  from ").
                append("(select substring(itemcode,1,5) itemcode,count(itemcode) total, sum(buildingprice) price ").
                append("from itemsbuilding ").
                append(modifier).
                append(" group by substring(itemcode,1,5))  as ib5) as ibx ").
                append("union ").
                append("select * from ").
                append("(select itemcode,total,price from ").
                append("(select substring(itemcode,1,2) itemcode, count(itemcode) total, sum(price) price ").
                append("from itemsnetwork ").
                append(modifier).
                append(" group by substring(itemcode,1,2))  as in2 ").
                append("union ").
                append("select itemcode, total,price  from ").
                append("(select substring(itemcode,1,5) itemcode,count(itemcode) total, sum(price) price ").
                append("from itemsnetwork ").
                append(modifier).
                append(" group by substring(itemcode,1,5))  as in5) as inx ").
                append("union ").
                append("select * from ").
                append("(select itemcode,total,price from ").
                append("(select substring(itemcode,1,2) itemcode, count(itemcode) total, sum(price) price ").
                append("from itemsfixedasset ").
                append(modifier).
                append(" group by substring(itemcode,1,2))  as ifa2 ").
                append("union ").
                append("select itemcode, total,price  from ").
                append("(select substring(itemcode,1,5) itemcode, count(itemcode) total, sum(price) price ").
                append("from itemsfixedasset ").
                append(modifier).
                append(" group by substring(itemcode,1,5))  as ifa5) as ifax ").
                append("union ").
                append("select * from ").
                append("(select itemcode,total,price from ").
                append("(select substring(itemcode,1,2) itemcode, count(itemcode) total, sum(price) price ").
                append("from itemsconstruction ").
                append(modifier).
                append(" group by substring(itemcode,1,2))  as icn2 ").
                append("union ").
                append("select itemcode, total,price  from ").
                append("(select substring(itemcode,1,5) itemcode, count(itemcode) total, sum(price) price ").
                append("from itemsconstruction ").
                append(modifier).
                append(" group by substring(itemcode,1,5))  as icn5) as icnx ").
                append(") itemsAll ").
                append("on itemsAll.itemcode = ic.categorycode ").
                append("where length(categorycode) <= 5 ").
                append("order by categorycode ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            InventoryRekap rekap = new InventoryRekap();
            rekap.setItemCode(rs.getString("categorycode"));
            rekap.setItemName(rs.getString("categoryname"));
            rekap.setTotal(rs.getInt("total"));
            rekap.setPrice(rs.getBigDecimal("price"));

            rekaps.add(rekap);
        }

        rs.close();
        pstm.close();

        return rekaps;
    }

    //
    void insertDeleteDraftItems(Connection conn, DeleteDraftItems deleteDraftItems) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into deletedraftitems(itemscode, itemsname, itemsindex, itemstype, locationcode,").
                append("specificationtype, owneddocument, acqusitionyear, price, condition,").
                append("description)").
                append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, deleteDraftItems.getItemCode());
        pstm.setString(++col, deleteDraftItems.getItemName());
        pstm.setLong(++col, deleteDraftItems.getItemIndex());
        pstm.setInt(++col, deleteDraftItems.getItemType());
        pstm.setString(++col, deleteDraftItems.getLocationCode());
        pstm.setString(++col, deleteDraftItems.getSpecificationType());
        pstm.setString(++col, deleteDraftItems.getOwnedDocument());
        pstm.setInt(++col, deleteDraftItems.getAcquisitionYear());
        pstm.setBigDecimal(++col, deleteDraftItems.getPrice());
        pstm.setString(++col, deleteDraftItems.getCondition());
        pstm.setString(++col, deleteDraftItems.getDescription());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateDeleteDraftItems(Connection conn, Long oldIndex, DeleteDraftItems deleteDraftItems) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update deletedraftitems set itemscode=?, itemsname=?, itemsindex=?, itemstype=?, ").
                append("locationcode=?, specificationtype=?, owneddocument=?, acqusitionyear=?, ").
                append("price=?, condition=?, description=? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, deleteDraftItems.getItemCode());
        pstm.setString(++col, deleteDraftItems.getItemName());
        pstm.setLong(++col, deleteDraftItems.getItemIndex());
        pstm.setInt(++col, deleteDraftItems.getItemType());
        pstm.setString(++col, deleteDraftItems.getLocationCode());
        pstm.setString(++col, deleteDraftItems.getSpecificationType());
        pstm.setString(++col, deleteDraftItems.getOwnedDocument());
        pstm.setInt(++col, deleteDraftItems.getAcquisitionYear());
        pstm.setBigDecimal(++col, deleteDraftItems.getPrice());
        pstm.setString(++col, deleteDraftItems.getCondition());
        pstm.setString(++col, deleteDraftItems.getDescription());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteDeleteDraftItems(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from deletedraftitems where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteDeleteDraftItems(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from deletedraftitems where itemcode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<DeleteDraftItems> getDeleteDraftItems(Connection conn) throws SQLException {
        ArrayList<DeleteDraftItems> deleteDraftItemss = new ArrayList<DeleteDraftItems>();

        StringBuilder query = new StringBuilder();
        query.append("select * from deletedraftitems");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            DeleteDraftItems deleteDraftItems = new DeleteDraftItems();
            deleteDraftItems.setIndex(rs.getLong("autoindex"));
            deleteDraftItems.setItemCode(rs.getString("itemscode"));
            deleteDraftItems.setItemName(rs.getString("itemsname"));
            deleteDraftItems.setItemIndex(rs.getLong("itemsindex"));
            deleteDraftItems.setItemType(rs.getInt("itemstype"));
            deleteDraftItems.setLocationCode(rs.getString("locationcode"));
            deleteDraftItems.setSpecificationType(rs.getString("specificationtype"));
            deleteDraftItems.setSpecificationType(rs.getString("specificationtype"));
            deleteDraftItems.setOwnedDocument(rs.getString("owneddocument"));
            deleteDraftItems.setAcquisitionYear(rs.getInt("acqusitionyear"));
            deleteDraftItems.setPrice(rs.getBigDecimal("price"));
            deleteDraftItems.setCondition(rs.getString("condition"));
            deleteDraftItems.setDescription(rs.getString("description"));

            deleteDraftItemss.add(deleteDraftItems);
        }

        rs.close();
        pstm.close();

        return deleteDraftItemss;
    }

    ArrayList<DeleteDraftItems> getDeleteDraftItems(Connection conn, String modifier) throws SQLException {
        ArrayList<DeleteDraftItems> deleteDraftItemss = new ArrayList<DeleteDraftItems>();

        StringBuilder query = new StringBuilder();
        query.append("select * from deletedraftitems ").append(modifier).
                append(" order by autoindex");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            DeleteDraftItems deleteDraftItems = new DeleteDraftItems();
            deleteDraftItems.setIndex(rs.getLong("autoindex"));
            deleteDraftItems.setItemCode(rs.getString("itemscode"));
            deleteDraftItems.setItemName(rs.getString("itemsname"));
            deleteDraftItems.setItemIndex(rs.getLong("itemsindex"));
            deleteDraftItems.setItemType(rs.getInt("itemstype"));
            deleteDraftItems.setLocationCode(rs.getString("locationcode"));
            deleteDraftItems.setSpecificationType(rs.getString("specificationtype"));
            deleteDraftItems.setSpecificationType(rs.getString("specificationtype"));
            deleteDraftItems.setOwnedDocument(rs.getString("owneddocument"));
            deleteDraftItems.setAcquisitionYear(rs.getInt("acqusitionyear"));
            deleteDraftItems.setPrice(rs.getBigDecimal("price"));
            deleteDraftItems.setCondition(rs.getString("condition"));
            deleteDraftItems.setDescription(rs.getString("description"));

            deleteDraftItemss.add(deleteDraftItems);
        }

        rs.close();
        pstm.close();

        return deleteDraftItemss;
    }

    //
    void insertUsedItems(Connection conn, UsedItems usedItems) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into useditems(itemscode, itemsname, itemsindex, itemstype, locationcode,").
                append("regnumber, documentnumber, itemsaddress, acqusitionway, acqusitionyear, ").
                append("construction, condition, wide, price, decreenumber, cooperationperiod,  ").
                append("thirdpartyaddress, description)").
                append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, usedItems.getItemCode());
        pstm.setString(++col, usedItems.getItemName());
        pstm.setLong(++col, usedItems.getItemIndex());
        pstm.setInt(++col, usedItems.getItemType());
        pstm.setString(++col, usedItems.getLocationCode());
        pstm.setString(++col, usedItems.getRegisterNumber());
        pstm.setString(++col, usedItems.getDocumentNumber());
        pstm.setString(++col, usedItems.getItemAddress());
        pstm.setString(++col, usedItems.getAcquisitionWay());
        pstm.setInt(++col, usedItems.getAcquisitionYear());
        pstm.setString(++col, usedItems.getConstruction());
        pstm.setString(++col, usedItems.getCondition());
        pstm.setBigDecimal(++col, usedItems.getWide());
        pstm.setBigDecimal(++col, usedItems.getPrice());
        pstm.setString(++col, usedItems.getDecreeNumber());
        pstm.setString(++col, usedItems.getCooperationPeriod());
        pstm.setString(++col, usedItems.getThirdPartyAddress());
        pstm.setString(++col, usedItems.getDescription());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateUsedItems(Connection conn, Long oldIndex, UsedItems usedItems) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update useditems set itemscode=?, itemsname=?, itemsindex=?, itemstype=?,  ").
                append("locationcode=?, regnumber=?, documentnumber=?, itemsaddress=?, ").
                append("acqusitionway=?, acqusitionyear=?, construction=?, condition=?, ").
                append("wide=?, price=?, decreenumber=?, cooperationperiod=?, thirdpartyaddress=?, ").
                append("description=? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, usedItems.getItemCode());
        pstm.setString(++col, usedItems.getItemName());
        pstm.setLong(++col, usedItems.getItemIndex());
        pstm.setInt(++col, usedItems.getItemType());
        pstm.setString(++col, usedItems.getLocationCode());
        pstm.setString(++col, usedItems.getRegisterNumber());
        pstm.setString(++col, usedItems.getDocumentNumber());
        pstm.setString(++col, usedItems.getItemAddress());
        pstm.setString(++col, usedItems.getAcquisitionWay());
        pstm.setInt(++col, usedItems.getAcquisitionYear());
        pstm.setString(++col, usedItems.getConstruction());
        pstm.setString(++col, usedItems.getCondition());
        pstm.setBigDecimal(++col, usedItems.getWide());
        pstm.setBigDecimal(++col, usedItems.getPrice());
        pstm.setString(++col, usedItems.getDecreeNumber());
        pstm.setString(++col, usedItems.getCooperationPeriod());
        pstm.setString(++col, usedItems.getThirdPartyAddress());
        pstm.setString(++col, usedItems.getDescription());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteUsedItems(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from useditems where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteUsedItems(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from useditems where itemcode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<UsedItems> getUsedItems(Connection conn) throws SQLException {
        ArrayList<UsedItems> usedItemses = new ArrayList<UsedItems>();

        StringBuilder query = new StringBuilder();
        query.append("select * from useditems");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            UsedItems usedItems = new UsedItems();
            usedItems.setIndex(rs.getLong("autoindex"));
            usedItems.setItemCode(rs.getString("itemscode"));
            usedItems.setItemName(rs.getString("itemsname"));
            usedItems.setItemIndex(rs.getLong("itemsindex"));
            usedItems.setItemType(rs.getInt("itemstype"));
            usedItems.setLocationCode(rs.getString("locationcode"));
            usedItems.setRegisterNumber(rs.getString("regnumber"));
            usedItems.setDocumentNumber(rs.getString("documentnumber"));
            usedItems.setItemAddress(rs.getString("itemsaddress"));
            usedItems.setAcquisitionWay(rs.getString("acqusitionway"));
            usedItems.setAcquisitionYear(rs.getInt("acqusitionyear"));
            usedItems.setConstruction(rs.getString("construction"));
            usedItems.setCondition(rs.getString("condition"));
            usedItems.setWide(rs.getBigDecimal("wide"));
            usedItems.setPrice(rs.getBigDecimal("price"));
            usedItems.setDecreeNumber(rs.getString("decreenumber"));
            usedItems.setCooperationPeriod(rs.getString("cooperationperiod"));
            usedItems.setThirdPartyAddress(rs.getString("thirdpartyaddress"));
            usedItems.setDescription(rs.getString("description"));

            usedItemses.add(usedItems);
        }

        rs.close();
        pstm.close();

        return usedItemses;
    }

    ArrayList<UsedItems> getUsedItems(Connection conn, String modifier) throws SQLException {
        ArrayList<UsedItems> usedItemses = new ArrayList<UsedItems>();

        StringBuilder query = new StringBuilder();
        query.append("select * from useditems ").append(modifier).
                append(" order by autoindex");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            UsedItems usedItems = new UsedItems();
            usedItems.setIndex(rs.getLong("autoindex"));
            usedItems.setItemCode(rs.getString("itemscode"));
            usedItems.setItemName(rs.getString("itemsname"));
            usedItems.setItemIndex(rs.getLong("itemsindex"));
            usedItems.setItemType(rs.getInt("itemstype"));
            usedItems.setLocationCode(rs.getString("locationcode"));
            usedItems.setRegisterNumber(rs.getString("regnumber"));
            usedItems.setDocumentNumber(rs.getString("documentnumber"));
            usedItems.setItemAddress(rs.getString("itemsaddress"));
            usedItems.setAcquisitionWay(rs.getString("acqusitionway"));
            usedItems.setAcquisitionYear(rs.getInt("acqusitionyear"));
            usedItems.setConstruction(rs.getString("construction"));
            usedItems.setCondition(rs.getString("condition"));
            usedItems.setWide(rs.getBigDecimal("wide"));
            usedItems.setPrice(rs.getBigDecimal("price"));
            usedItems.setDecreeNumber(rs.getString("decreenumber"));
            usedItems.setCooperationPeriod(rs.getString("cooperationperiod"));
            usedItems.setThirdPartyAddress(rs.getString("thirdpartyaddress"));
            usedItems.setDescription(rs.getString("description"));

            usedItemses.add(usedItems);
        }

        rs.close();
        pstm.close();

        return usedItemses;
    }
}
