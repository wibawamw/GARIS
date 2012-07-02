package org.motekar.project.civics.archieve.assets.master.sqlapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Room;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.project.civics.archieve.sqlapi.CommonSQL;
import org.motekar.util.user.objects.UserAccount;

/**
 *
 * @author Muhamad Wibawa
 */
public class AssetMasterSQL extends CommonSQL {
    
    void insertRegion(Connection conn, Region region) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into region(regioncode,regionname,regiontype,regiongroup)").
                append("values(?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, region.getRegionCode());
        pstm.setString(++col, region.getRegionName());
        pstm.setInt(++col, region.getRegionType());
        pstm.setBoolean(++col, region.isGroup());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateRegion(Connection conn, Long oldIndex, Region region) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update region set regioncode = ?, regionname = ?, regiontype = ?, ").
                append("regiongroup = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, region.getRegionCode());
        pstm.setString(++col, region.getRegionName());
        pstm.setInt(++col, region.getRegionType());
        pstm.setBoolean(++col, region.isGroup());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteRegion(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from region where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteRegion(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from region where regioncode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Region> getRegion(Connection conn) throws SQLException {
        ArrayList<Region> regions = new ArrayList<Region>();

        StringBuilder query = new StringBuilder();
        query.append("select reg.*, coalesce(superregion,0) superregion from region reg ").
                append("left join regionstructure struct on reg.autoindex = subregion ").
                append("order by reg.regioncode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Region region = new Region();
            region.setIndex(rs.getLong("autoindex"));
            region.setRegionCode(rs.getString("regioncode"));
            region.setRegionName(rs.getString("regionname"));
            region.setRegionType(rs.getInt("regiontype"));
            region.setGroup(rs.getBoolean("regiongroup"));
            region.setParentIndex(rs.getLong("superregion"));

            regions.add(region);
        }

        rs.close();
        pstm.close();

        return regions;
    }
    
    ArrayList<Region> getUrbanRegion(Connection conn) throws SQLException {
        ArrayList<Region> regions = new ArrayList<Region>();

        StringBuilder query = new StringBuilder();
        query.append("select * from region ").
                append("where regiontype = ").
                append(Region.TYPE_URBAN);

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Region region = new Region();
            region.setIndex(rs.getLong("autoindex"));
            region.setRegionCode(rs.getString("regioncode"));
            region.setRegionName(rs.getString("regionname"));
            region.setRegionType(rs.getInt("regiontype"));
            region.setGroup(rs.getBoolean("regiongroup"));

            regions.add(region);
        }

        rs.close();
        pstm.close();

        return regions;
    }
    
    ArrayList<Region> getSubUrbanRegion(Connection conn, Long parentIndex) throws SQLException {
        ArrayList<Region> regions = new ArrayList<Region>();

        StringBuilder query = new StringBuilder();
        query.append("select * from region ").
                append("where regiontype = ").
                append(Region.TYPE_SUB_URBAN).
                append(" and autoindex in ").
                append("(select subregion from regionstructure ").
                append("where superregion = ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, parentIndex);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Region region = new Region();
            region.setIndex(rs.getLong("autoindex"));
            region.setRegionCode(rs.getString("regioncode"));
            region.setRegionName(rs.getString("regionname"));
            region.setRegionType(rs.getInt("regiontype"));
            region.setGroup(rs.getBoolean("regiongroup"));

            regions.add(region);
        }

        rs.close();
        pstm.close();

        return regions;
    }

    public Region getRegionByIndex(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from region ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        Region region = null;

        while (rs.next()) {
            region = new Region();
            region.setIndex(rs.getLong("autoindex"));
            region.setRegionCode(rs.getString("regioncode"));
            region.setRegionName(rs.getString("regionname"));
            region.setRegionType(rs.getInt("regiontype"));
            region.setGroup(rs.getBoolean("regiongroup"));
        }

        rs.close();
        pstm.close();

        return region;
    }

    void insertRegionStructure(Connection conn, Region parent, Region child) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into regionstructure(superregion,subregion)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, parent.getIndex());
        pstm.setLong(++col, child.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteRegionStructureByChild(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from regionstructure where superaccount = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    void insertUnit(Connection conn, Unit unit) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into unit(unitcode,unitname,streetname,rt,rw,urban,suburban,unittype,unitgroup)").
                append("values(?,?,?,?,?,?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, unit.getUnitCode());
        pstm.setString(++col, unit.getUnitName());
        pstm.setString(++col, unit.getStreetName());
        pstm.setString(++col, unit.getRt());
        pstm.setString(++col, unit.getRw());

        if (unit.getUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, unit.getUrban().getIndex());
        }

        if (unit.getSubUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, unit.getSubUrban().getIndex());
        }
        
        pstm.setInt(++col, unit.getUnitType());
        pstm.setBoolean(++col, unit.isGroup());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateUnit(Connection conn, Long oldIndex, Unit unit) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update unit set unitcode = ?, unitname = ?, streetname = ?, ").
                append("rt = ?, rw = ?, urban = ?, suburban = ?, unittype = ?, unitgroup = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, unit.getUnitCode());
        pstm.setString(++col, unit.getUnitName());
        pstm.setString(++col, unit.getStreetName());
        pstm.setString(++col, unit.getRt());
        pstm.setString(++col, unit.getRw());

        if (unit.getUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, unit.getUrban().getIndex());
        }

        if (unit.getSubUrban() == null) {
            pstm.setNull(++col, Types.BIGINT);
        } else {
            pstm.setLong(++col, unit.getSubUrban().getIndex());
        }
        
        pstm.setInt(++col, unit.getUnitType());
        pstm.setBoolean(++col, unit.isGroup());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }
    
    void updateUnitGroup(Connection conn, Unit unit,boolean group) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update unit set unitgroup = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setBoolean(++col, group);

        pstm.setLong(++col, unit.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteUnit(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from unit where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteUnit(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from unit where unitcode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Unit> getUnit(Connection conn) throws SQLException {
        ArrayList<Unit> units = new ArrayList<Unit>();

        StringBuilder query = new StringBuilder();
        query.append("select un.*, coalesce(superunit,0) superunit from unit un ").
                append("left join unitstructure struct on un.autoindex = subunit ").
                append("order by un.unitcode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;

            if (urbanIndex != null) {
                urban = new Region(urbanIndex);
            }

            if (subUrbanIndex != null) {
                subUrban = new Region(subUrbanIndex);
            }

            Unit unit = new Unit();
            unit.setIndex(rs.getLong("autoindex"));
            unit.setUnitCode(rs.getString("unitcode"));
            unit.setUnitName(rs.getString("unitname"));
            unit.setStreetName(rs.getString("streetname"));
            unit.setRt(rs.getString("rt"));
            unit.setRw(rs.getString("rw"));

            unit.setUrban(urban);
            unit.setSubUrban(subUrban);
            
            unit.setUnitType(rs.getInt("unittype"));
            unit.setGroup(rs.getBoolean("unitgroup"));
            unit.setParentIndex(rs.getLong("superunit"));

            units.add(unit);
        }

        rs.close();
        pstm.close();

        return units;
    }
    
    ArrayList<Unit> getUnit(Connection conn,Integer type) throws SQLException {
        ArrayList<Unit> units = new ArrayList<Unit>();

        StringBuilder query = new StringBuilder();
        query.append("select * from unit ").
                append("where unittype = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, type);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {

            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;

            if (urbanIndex != null) {
                urban = new Region(urbanIndex);
            }

            if (subUrbanIndex != null) {
                subUrban = new Region(subUrbanIndex);
            }

            Unit unit = new Unit();
            unit.setIndex(rs.getLong("autoindex"));
            unit.setUnitCode(rs.getString("unitcode"));
            unit.setUnitName(rs.getString("unitname"));
            unit.setStreetName(rs.getString("streetname"));
            unit.setRt(rs.getString("rt"));
            unit.setRw(rs.getString("rw"));

            unit.setUrban(urban);
            unit.setSubUrban(subUrban);
            
            unit.setUnitType(rs.getInt("unittype"));
            unit.setGroup(rs.getBoolean("unitgroup"));

            units.add(unit);
        }

        rs.close();
        pstm.close();

        return units;
    }

    public Unit getUnitByIndex(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from unit ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        Unit unit = null;

        while (rs.next()) {
            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;

            if (urbanIndex != null) {
                urban = new Region(urbanIndex);
            }

            if (subUrbanIndex != null) {
                subUrban = new Region(subUrbanIndex);
            }

            unit = new Unit();
            unit.setIndex(rs.getLong("autoindex"));
            unit.setUnitCode(rs.getString("unitcode"));
            unit.setUnitName(rs.getString("unitname"));
            unit.setStreetName(rs.getString("streetname"));
            unit.setRt(rs.getString("rt"));
            unit.setRw(rs.getString("rw"));

            unit.setUrban(urban);
            unit.setSubUrban(subUrban);
            
            unit.setUnitType(rs.getInt("unittype"));
            unit.setGroup(rs.getBoolean("unitgroup"));
        }

        rs.close();
        pstm.close();

        return unit;
    }
    
    public Unit getUnitByUser(Connection conn, UserAccount userAccount) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from unit ").
                append("where autoindex in ").
                append("(select unit from uaccount ").
                append("where username = ?) ");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setString(1, userAccount.getUserName());

        ResultSet rs = pstm.executeQuery();

        Unit unit = null;

        while (rs.next()) {
            Long urbanIndex = rs.getLong("urban");
            Long subUrbanIndex = rs.getLong("suburban");

            Region urban = null;
            Region subUrban = null;

            if (urbanIndex != null) {
                urban = new Region(urbanIndex);
            }

            if (subUrbanIndex != null) {
                subUrban = new Region(subUrbanIndex);
            }

            unit = new Unit();
            unit.setIndex(rs.getLong("autoindex"));
            unit.setUnitCode(rs.getString("unitcode"));
            unit.setUnitName(rs.getString("unitname"));
            unit.setStreetName(rs.getString("streetname"));
            unit.setRt(rs.getString("rt"));
            unit.setRw(rs.getString("rw"));

            unit.setUrban(urban);
            unit.setSubUrban(subUrban);
            
            unit.setUnitType(rs.getInt("unittype"));
            unit.setGroup(rs.getBoolean("unitgroup"));
        }

        rs.close();
        pstm.close();

        return unit;
    }

    void insertUnitStructure(Connection conn, Unit parent, Unit child) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into unitstructure(superunit,subunit)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, parent.getIndex());
        pstm.setLong(++col, child.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }
    
    void insertItemsCategory(Connection conn, ItemsCategory itemsCategory) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into itemscategory(categorycode,categoryname,isgroup,types)").
                append("values(?,?,?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, itemsCategory.getCategoryCode());
        pstm.setString(++col, itemsCategory.getCategoryName());
        pstm.setBoolean(++col, itemsCategory.isGroup());
        pstm.setInt(++col, itemsCategory.getTypes());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateItemsCategory(Connection conn, Long oldIndex, ItemsCategory itemsCategory) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update itemscategory set categorycode = ?, categoryname = ?, isgroup = ?, types = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, itemsCategory.getCategoryCode());
        pstm.setString(++col, itemsCategory.getCategoryName());
        pstm.setBoolean(++col, itemsCategory.isGroup());
        pstm.setInt(++col, itemsCategory.getTypes());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }
    
    void updateItemsCategory(Connection conn, ItemsCategory parent, boolean group) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update itemscategory set isgroup = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setBoolean(++col, group);

        pstm.setLong(++col, parent.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsCategory(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemscategory where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteItemsCategory(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from itemscategory where categorycode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<ItemsCategory> getItemsCategory(Connection conn) throws SQLException {
        ArrayList<ItemsCategory> itemsCategorys = new ArrayList<ItemsCategory>();

        StringBuilder query = new StringBuilder();
        query.append("select cat.*, coalesce(supercategory,0) supercategory from itemscategory cat ").
                append("left join itemscategorystructure struct on cat.autoindex = subcategory ").
                append("order by supercategory,categorycode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ItemsCategory itemsCategory = new ItemsCategory();
            itemsCategory.setIndex(rs.getLong("autoindex"));
            itemsCategory.setCategoryCode(rs.getString("categorycode"));
            itemsCategory.setCategoryName(rs.getString("categoryname"));
            itemsCategory.setGroup(rs.getBoolean("isgroup"));
            itemsCategory.setTypes(rs.getInt("types"));
            itemsCategory.setParentIndex(rs.getLong("supercategory"));

            itemsCategorys.add(itemsCategory);
        }

        rs.close();
        pstm.close();

        return itemsCategorys;
    }
    
    ArrayList<ItemsCategory> getItemsCategorys(Connection conn) throws SQLException {
        ArrayList<ItemsCategory> itemsCategorys = new ArrayList<ItemsCategory>();

        StringBuilder query = new StringBuilder();
        query.append("select * from  itemscategory ").
                append("order by categorycode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ItemsCategory itemsCategory = new ItemsCategory();
            itemsCategory.setIndex(rs.getLong("autoindex"));
            itemsCategory.setCategoryCode(rs.getString("categorycode"));
            itemsCategory.setCategoryName(rs.getString("categoryname"));
            itemsCategory.setGroup(rs.getBoolean("isgroup"));
            itemsCategory.setStyled(true);

            itemsCategorys.add(itemsCategory);
        }

        rs.close();
        pstm.close();

        return itemsCategorys;
    }
    
    ArrayList<ItemsCategory> getItemsCategory(Connection conn,Integer types,Long parentIndex) throws SQLException {
        ArrayList<ItemsCategory> itemsCategorys = new ArrayList<ItemsCategory>();

        StringBuilder query = new StringBuilder();
        query.append("select * from ").
                append("(select cat.*, coalesce(supercategory,0) supercategory from itemscategory cat ").
                append("left join itemscategorystructure struct on cat.autoindex = subcategory) as ic ").
                append("where types = ? ").
                append("and supercategory  = ? ").
                append("order by supercategory,categorycode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, types);
        pstm.setLong(2, parentIndex);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ItemsCategory itemsCategory = new ItemsCategory();
            itemsCategory.setIndex(rs.getLong("autoindex"));
            itemsCategory.setCategoryCode(rs.getString("categorycode"));
            itemsCategory.setCategoryName(rs.getString("categoryname"));
            itemsCategory.setGroup(rs.getBoolean("isgroup"));
            itemsCategory.setTypes(rs.getInt("types"));
            itemsCategory.setParentIndex(rs.getLong("supercategory"));
            itemsCategory.setStyled(true);

            itemsCategorys.add(itemsCategory);
        }

        rs.close();
        pstm.close();

        return itemsCategorys;
    }
    
    ArrayList<ItemsCategory> getItemsCategory(Connection conn,Long parentIndex) throws SQLException {
        ArrayList<ItemsCategory> itemsCategorys = new ArrayList<ItemsCategory>();

        StringBuilder query = new StringBuilder();
        query.append("select * from ").
                append("(select cat.*, coalesce(supercategory,0) supercategory from itemscategory cat ").
                append("left join itemscategorystructure struct on cat.autoindex = subcategory) as ic ").
                append("where supercategory  = ? ").
                append("order by supercategory,categorycode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, parentIndex);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ItemsCategory itemsCategory = new ItemsCategory();
            itemsCategory.setIndex(rs.getLong("autoindex"));
            itemsCategory.setCategoryCode(rs.getString("categorycode"));
            itemsCategory.setCategoryName(rs.getString("categoryname"));
            itemsCategory.setGroup(rs.getBoolean("isgroup"));
            itemsCategory.setTypes(rs.getInt("types"));
            itemsCategory.setParentIndex(rs.getLong("supercategory"));
            itemsCategory.setStyled(true);

            itemsCategorys.add(itemsCategory);
        }

        rs.close();
        pstm.close();

        return itemsCategorys;
    }
    
    ArrayList<ItemsCategory> getItemsCategory(Connection conn,Integer types) throws SQLException {
        ArrayList<ItemsCategory> itemsCategorys = new ArrayList<ItemsCategory>();

        StringBuilder query = new StringBuilder();
        query.append("select * from ").
                append("(select cat.*, coalesce(supercategory,0) supercategory from itemscategory cat ").
                append("left join itemscategorystructure struct on cat.autoindex = subcategory) as ic ").
                append("where types  = ? ").
                append("order by supercategory,categorycode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, types);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ItemsCategory itemsCategory = new ItemsCategory();
            itemsCategory.setIndex(rs.getLong("autoindex"));
            itemsCategory.setCategoryCode(rs.getString("categorycode"));
            itemsCategory.setCategoryName(rs.getString("categoryname"));
            itemsCategory.setGroup(rs.getBoolean("isgroup"));
            itemsCategory.setTypes(rs.getInt("types"));
            itemsCategory.setParentIndex(rs.getLong("supercategory"));
            itemsCategory.setStyled(true);

            itemsCategorys.add(itemsCategory);
        }

        rs.close();
        pstm.close();

        return itemsCategorys;
    }
    
    
    ItemsCategory getParentItemsCategory(Connection conn,Integer types) throws SQLException {
        ItemsCategory itemsCategory = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from ").
                append("(select cat.*, coalesce(supercategory,0) supercategory from itemscategory cat ").
                append("left join itemscategorystructure struct on cat.autoindex = subcategory) as ic ").
                append("where types = ? ").
                append("and supercategory  = ? ").
                append("order by supercategory,categorycode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setInt(1, types);
        pstm.setLong(2, Long.valueOf(0));

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            itemsCategory = new ItemsCategory();
            itemsCategory.setIndex(rs.getLong("autoindex"));
            itemsCategory.setCategoryCode(rs.getString("categorycode"));
            itemsCategory.setCategoryName(rs.getString("categoryname"));
            itemsCategory.setGroup(rs.getBoolean("isgroup"));
            itemsCategory.setTypes(rs.getInt("types"));
            itemsCategory.setParentIndex(rs.getLong("supercategory"));
            itemsCategory.setStyled(true);

        }

        rs.close();
        pstm.close();

        return itemsCategory;
    }
    
    ItemsCategory getParentItemsCategory(Connection conn) throws SQLException {
        ItemsCategory itemsCategory = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from ").
                append("(select cat.*, coalesce(supercategory,0) supercategory from itemscategory cat ").
                append("left join itemscategorystructure struct on cat.autoindex = subcategory) as ic ").
                append("where supercategory  = ? ").
                append("order by supercategory,categorycode");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, Long.valueOf(0));

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            itemsCategory = new ItemsCategory();
            itemsCategory.setIndex(rs.getLong("autoindex"));
            itemsCategory.setCategoryCode(rs.getString("categorycode"));
            itemsCategory.setCategoryName(rs.getString("categoryname"));
            itemsCategory.setGroup(rs.getBoolean("isgroup"));
            itemsCategory.setTypes(rs.getInt("types"));
            itemsCategory.setParentIndex(rs.getLong("supercategory"));
            itemsCategory.setStyled(true);

        }

        rs.close();
        pstm.close();

        return itemsCategory;
    }
    
    
    ArrayList<ItemsCategory> getItemsCategorys(Connection conn,String modifier) throws SQLException {
        ArrayList<ItemsCategory> itemsCategorys = new ArrayList<ItemsCategory>();

        StringBuilder query = new StringBuilder();
        query.append("select * from (").
                append(modifier).
                append(") as ic ").
                append("order by categorycode");
        
        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            ItemsCategory itemsCategory = new ItemsCategory();
            itemsCategory.setIndex(rs.getLong("autoindex"));
            itemsCategory.setCategoryCode(rs.getString("categorycode"));
            itemsCategory.setCategoryName(rs.getString("categoryname"));
            itemsCategory.setGroup(rs.getBoolean("isgroup"));
            itemsCategory.setTypes(rs.getInt("types"));
            itemsCategory.setStyled(true);

            itemsCategorys.add(itemsCategory);
        }

        rs.close();
        pstm.close();

        return itemsCategorys;
    }
    
    public ItemsCategory getItemsCategoryByIndex(Connection conn, Long index) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from itemscategory ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        ItemsCategory itemsCategory = null;

        while (rs.next()) {
            itemsCategory = new ItemsCategory();
            itemsCategory.setIndex(rs.getLong("autoindex"));
            itemsCategory.setCategoryCode(rs.getString("categorycode"));
            itemsCategory.setCategoryName(rs.getString("categoryname"));
            itemsCategory.setGroup(rs.getBoolean("isgroup"));
        }

        rs.close();
        pstm.close();

        return itemsCategory;
    }
    
    public ItemsCategory getItemsCategoryByCode(Connection conn, String code) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("select * from itemscategory ").
                append("where categorycode = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setString(1, code);

        ResultSet rs = pstm.executeQuery();

        ItemsCategory itemsCategory = null;

        while (rs.next()) {
            itemsCategory = new ItemsCategory();
            itemsCategory.setIndex(rs.getLong("autoindex"));
            itemsCategory.setCategoryCode(rs.getString("categorycode"));
            itemsCategory.setCategoryName(rs.getString("categoryname"));
            itemsCategory.setGroup(rs.getBoolean("isgroup"));
        }

        rs.close();
        pstm.close();

        return itemsCategory;
    }

    void insertItemsCategoryStructure(Connection conn, ItemsCategory parent, ItemsCategory child) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into itemscategorystructure(supercategory,subcategory)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setLong(++col, parent.getIndex());
        pstm.setLong(++col, child.getIndex());

        pstm.executeUpdate();
        pstm.close();
    }
    
    void insertCondition(Connection conn, Condition condition) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into condition(conditioncode,conditionname,startvalue,endvalue)").
                append("values(?,?,?, ?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, condition.getConditionCode());
        pstm.setString(++col, condition.getConditionName());
        pstm.setInt(++col, condition.getStartValue());
        pstm.setInt(++col, condition.getEndValue());

        pstm.executeUpdate();
        pstm.close();
    }

    void updateCondition(Connection conn, Long oldIndex, Condition condition) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update condition set conditioncode = ?, conditionname = ?, startvalue = ?, endvalue = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, condition.getConditionCode());
        pstm.setString(++col, condition.getConditionName());
        pstm.setInt(++col, condition.getStartValue());
        pstm.setInt(++col, condition.getEndValue());

        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }
    
    void deleteCondition(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from condition where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteCondition(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from condition where conditioncode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Condition> getCondition(Connection conn) throws SQLException {
        ArrayList<Condition> conditions = new ArrayList<Condition>();

        StringBuilder query = new StringBuilder();
        query.append("select * from condition");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            Condition condition = new Condition();
            condition.setIndex(rs.getLong("autoindex"));
            condition.setConditionCode(rs.getString("conditioncode"));
            condition.setConditionName(rs.getString("conditionname"));
            condition.setStartValue(rs.getInt("startvalue"));
            condition.setEndValue(rs.getInt("endvalue"));

            conditions.add(condition);
        }

        rs.close();
        pstm.close();

        return conditions;
    }
    
    Condition getCondition(Connection conn, Long index) throws SQLException {
        Condition condition = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from condition ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            condition = new Condition();
            condition.setIndex(rs.getLong("autoindex"));
            condition.setConditionCode(rs.getString("conditioncode"));
            condition.setConditionName(rs.getString("conditionname"));
            condition.setStartValue(rs.getInt("startvalue"));
            condition.setEndValue(rs.getInt("endvalue"));
        }

        rs.close();
        pstm.close();

        return condition;
    }
    
    
    void insertRoom(Connection conn, Room room) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into room(roomcode,roomname)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, room.getRoomCode());
        pstm.setString(++col, room.getRoomName());
        
        pstm.executeUpdate();
        pstm.close();
    }

    void updateRoom(Connection conn, Long oldIndex, Room room) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("update room set roomcode = ?, roomname = ? ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        int col = 0;

        pstm.setString(++col, room.getRoomCode());
        pstm.setString(++col, room.getRoomName());
        
        pstm.setLong(++col, oldIndex);

        pstm.executeUpdate();
        pstm.close();
    }
    
    void deleteRoom(Connection conn, Long index) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from room where autoindex = ?");
        pstm.setLong(1, index);
        pstm.executeUpdate();
        pstm.close();
    }

    void deleteRoom(Connection conn, String code) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from room where roomcode like '" + code + "%'");
        pstm.executeUpdate();
        pstm.close();
    }

    ArrayList<Room> getRoom(Connection conn) throws SQLException {
        ArrayList<Room> rooms = new ArrayList<Room>();

        StringBuilder query = new StringBuilder();
        query.append("select * from room");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            Room room = new Room();
            room.setIndex(rs.getLong("autoindex"));
            room.setRoomCode(rs.getString("roomcode"));
            room.setRoomName(rs.getString("roomname"));

            rooms.add(room);
        }

        rs.close();
        pstm.close();

        return rooms;
    }
    
    Room getRoom(Connection conn,Long index) throws SQLException {
        Room room = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from room ").
                append("where autoindex = ?");

        PreparedStatement pstm = conn.prepareStatement(query.toString());
        pstm.setLong(1, index);

        ResultSet rs = pstm.executeQuery();

        while (rs.next()) {
            
            room = new Room();
            room.setIndex(rs.getLong("autoindex"));
            room.setRoomCode(rs.getString("roomcode"));
            room.setRoomName(rs.getString("roomname"));

        }

        rs.close();
        pstm.close();

        return room;
    }
}
