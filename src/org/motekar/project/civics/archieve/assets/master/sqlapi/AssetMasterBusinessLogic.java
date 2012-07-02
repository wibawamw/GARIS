package org.motekar.project.civics.archieve.assets.master.sqlapi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.motekar.project.civics.archieve.assets.master.objects.Condition;
import org.motekar.project.civics.archieve.assets.master.objects.ItemsCategory;
import org.motekar.project.civics.archieve.assets.master.objects.Region;
import org.motekar.project.civics.archieve.assets.master.objects.Room;
import org.motekar.project.civics.archieve.assets.master.objects.Unit;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.objects.UserAccount;
import org.motekar.util.user.sqlapi.AuthBusinessLogic;

/**
 *
 * @author Muhamad Wibawa
 */
public class AssetMasterBusinessLogic {
    
    private Connection conn;
    private AssetMasterSQL sql = new AssetMasterSQL();
    private AuthBusinessLogic auth;

    
    public AssetMasterBusinessLogic(Connection conn) {
        this.conn = conn;
        auth = new AuthBusinessLogic(conn);
    }
    
    public Region insertRegion(Long session, Region region) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            sql.insertRegion(conn, region);
            Long index = sql.getMaxIndex(conn, "region", "autoindex");
            region.setIndex(index);

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
        return region;
    }

    public Region insertRegion(Long session, Region parent, Region child) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            sql.insertRegion(conn, child);
            Long index = sql.getMaxIndex(conn, "region", "autoindex");
            child.setIndex(index);

            if (parent != null) {
                sql.insertRegionStructure(conn, parent, child);
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
        return child;
    }

    public Region updateRegion(Long session, Region oldRegion, Region newRegion) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateRegion(conn, oldRegion.getIndex(), newRegion);
            newRegion.setIndex(oldRegion.getIndex());

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
        return newRegion;
    }

    public void deleteRegion(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteRegion(conn, index);

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

    public void deleteRegion(Long session, String code) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteRegion(conn, code);

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

    public ArrayList<Region> getRegion(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getRegion(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ArrayList<Region> getUrbanRegion(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getUrbanRegion(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    
    public ArrayList<Region> getSubUrbanRegion(Long session,Region parent) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getSubUrbanRegion(conn,parent.getIndex());
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public Region getRegion(Long session,Region region) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getRegionByIndex(conn, region.getIndex());
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    
    public Unit insertUnit(Long session, Unit unit) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            sql.insertUnit(conn, unit);
            Long index = sql.getMaxIndex(conn, "unit", "autoindex");
            unit.setIndex(index);

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
        return unit;
    }

    public Unit insertUnit(Long session, Unit parent, Unit child) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            sql.insertUnit(conn, child);
            Long index = sql.getMaxIndex(conn, "unit", "autoindex");
            child.setIndex(index);
            
            sql.updateUnitGroup(conn, parent, parent.isGroup());
            
            if (parent != null) {
                sql.insertUnitStructure(conn, parent, child);
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
        return child;
    }

    public Unit updateUnit(Long session, Unit oldUnit, Unit newUnit) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateUnit(conn, oldUnit.getIndex(), newUnit);
            newUnit.setIndex(oldUnit.getIndex());

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
        return newUnit;
    }
    
    public Unit updateUnit(Long session, Unit oldUnit, Unit newUnit,Unit parentUnit) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            
            sql.updateUnitGroup(conn, parentUnit, true);
            
            sql.updateUnit(conn, oldUnit.getIndex(), newUnit);
            newUnit.setIndex(oldUnit.getIndex());

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
        return newUnit;
    }

    public void deleteUnit(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteUnit(conn, index);

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

    public void deleteUnit(Long session, String code, Unit parent) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateUnitGroup(conn, parent, parent.isGroup());
            
            sql.deleteUnit(conn, code);

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

    public ArrayList<Unit> getUnit(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getUnit(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ArrayList<Unit> getUnit(Long session,Integer type) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getUnit(conn,type);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public Unit getUnitByIndex(Long session,Unit unit) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getUnitByIndex(conn,unit.getIndex());
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public Unit getUnitByUser(Long session,UserAccount userAccount) throws SQLException {
        try {
            return sql.getUnitByUser(conn, userAccount);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ItemsCategory insertItemsCategory(Long session, ItemsCategory itemsCategory) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            sql.insertItemsCategory(conn, itemsCategory);
            Long index = sql.getMaxIndex(conn, "itemscategory", "autoindex");
            itemsCategory.setIndex(index);

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
        return itemsCategory;
    }

    public ItemsCategory insertItemsCategory(Long session, ItemsCategory parent, ItemsCategory child) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            sql.insertItemsCategory(conn, child);
            Long index = sql.getMaxIndex(conn, "itemscategory", "autoindex");
            child.setIndex(index);

            if (parent != null) {
                parent.setGroup(true);
                sql.updateItemsCategory(conn, parent, parent.isGroup());
                sql.insertItemsCategoryStructure(conn, parent, child);
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
        return child;
    }

    public ItemsCategory updateItemsCategory(Long session, ItemsCategory parent,boolean group) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            
            parent.setGroup(group);
            sql.updateItemsCategory(conn, parent, parent.isGroup());

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
        return parent;
    }
    
    public ItemsCategory updateItemsCategory(Long session, ItemsCategory oldCategory, ItemsCategory newCategory) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateItemsCategory(conn, oldCategory.getIndex(), newCategory);
            newCategory.setIndex(oldCategory.getIndex());

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
        return newCategory;
    }

    public void deleteItemsCategory(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteItemsCategory(conn, index);

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

    public void deleteItemsCategory(Long session, String code) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteItemsCategory(conn, code);

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

    public ArrayList<ItemsCategory> getItemsCategory(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getItemsCategory(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ArrayList<ItemsCategory> getItemsCategory(Long session,Integer types,Long parentIndex) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getItemsCategory(conn, types, parentIndex);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ArrayList<ItemsCategory> getItemsCategory(Long session,Long parentIndex) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getItemsCategory(conn, parentIndex);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ArrayList<ItemsCategory> getItemsCategory(Long session,Integer types) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getItemsCategory(conn, types);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ArrayList<ItemsCategory> getItemsCategorys(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getItemsCategorys(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ArrayList<ItemsCategory> getItemsCategorys(Long session,ItemsCategory baseCategory) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            
            StringTokenizer token = new StringTokenizer(baseCategory.getCategoryCode(), ".");
            int size = token.countTokens();
            
            StringBuilder builder = new StringBuilder();
            
            int substractor = 0;
            
            for (int i=0;i<size;i++) {
                if (i == 0) {
                    builder.append("select * from itemscategory ").
                            append("where types = ").append(baseCategory.getTypes()).
                            append(" and categorycode = substring('").
                            append(baseCategory.getCategoryCode()).
                            append("',1,length('").
                            append(baseCategory.getCategoryCode()).
                            append("')) ").
                            append(" UNION ");
                } else if (i == size - 1) {
                    builder.append("select * from itemscategory ").
                            append("where types = ").append(baseCategory.getTypes()).
                            append(" and categorycode = substring('").
                            append(baseCategory.getCategoryCode()).
                            append("',1,length('").
                            append(baseCategory.getCategoryCode()).
                            append("') - ").
                            append(substractor).
                            append(") ");
                } else {
                    builder.append("select * from itemscategory ").
                            append("where types = ").append(baseCategory.getTypes()).
                            append(" and categorycode = substring('").
                            append(baseCategory.getCategoryCode()).
                            append("',1,length('").
                            append(baseCategory.getCategoryCode()).
                            append("') - ").
                            append(substractor).
                            append(") ").
                            append(" UNION ");
                }
                substractor += 3;
            }
            
            return sql.getItemsCategorys(conn,builder.toString());
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ItemsCategory getParentItemsCategory(Long session,Integer types) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getParentItemsCategory(conn, types);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ItemsCategory getParentItemsCategory(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getParentItemsCategory(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public ItemsCategory getItemsCategoryByCode(Long session,String code) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getItemsCategoryByCode(conn,code);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public Condition insertCondition(Long session, Condition condition) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            sql.insertCondition(conn, condition);
            Long index = sql.getMaxIndex(conn, "condition", "autoindex");
            condition.setIndex(index);

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
        return condition;
    }

    public Condition updateCondition(Long session, Condition oldCondition, Condition newCondition) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateCondition(conn, oldCondition.getIndex(), newCondition);
            newCondition.setIndex(oldCondition.getIndex());

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
        return newCondition;
    }

    public void deleteCondition(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteCondition(conn, index);

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

    public void deleteCondition(Long session, String code) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteCondition(conn, code);

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

    public ArrayList<Condition> getCondition(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getCondition(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    
    public Condition getCondition(Long session,Condition condition) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getCondition(conn, condition.getIndex());
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    
    public Room insertRoom(Long session, Room room) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);


            sql.insertRoom(conn, room);
            Long index = sql.getMaxIndex(conn, "room", "autoindex");
            room.setIndex(index);

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
        return room;
    }

    public Room updateRoom(Long session, Room oldRoom, Room newRoom) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.updateRoom(conn, oldRoom.getIndex(), newRoom);
            newRoom.setIndex(oldRoom.getIndex());

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
        return newRoom;
    }

    public void deleteRoom(Long session, Long index) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteRoom(conn, index);

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

    public void deleteRoom(Long session, String code) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteRoom(conn, code);

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

    public ArrayList<Room> getRoom(Long session) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getRoom(conn);
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
    
    public Room getRoom(Long session,Room room) throws SQLException {
        try {
            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }
            return sql.getRoom(conn, room.getIndex());
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
}
