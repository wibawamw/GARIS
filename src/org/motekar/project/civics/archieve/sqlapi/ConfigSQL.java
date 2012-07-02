package org.motekar.project.civics.archieve.sqlapi;

import java.security.InvalidKeyException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.commons.configuration.DataConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.motekar.util.user.misc.SimpleStringCipher;

/**
 *
 * @author Muhamad Wibawa
 */
public class ConfigSQL {
    
    void insertConfigurationSetting(Connection conn, String key,String value) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into configurationsetting(ckey,cvalue)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, key);
        pstm.setString(++col, value);


        pstm.executeUpdate();
        pstm.close();
    }
    
    void insertConfigurationSetting(Connection conn, String key,byte[] value) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("insert into configurationsetting(ckey,cvalue2)").
                append("values(?,?)");

        PreparedStatement pstm = conn.prepareStatement(query.toString());

        int col = 0;

        pstm.setString(++col, key);
        pstm.setBytes(++col, value);


        pstm.executeUpdate();
        pstm.close();
    }
    
    
    void deleteConfigurationSetting(Connection conn) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("delete from configurationsetting");
        pstm.executeUpdate();
        pstm.close();
    }
    
    DataConfiguration getConfigurationSetting(Connection conn) throws SQLException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        DataConfiguration config = null;

        StringBuilder query = new StringBuilder();
        query.append("select * from configurationsetting ");

        PreparedStatement pstm = conn.prepareStatement(query.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

        ResultSet rs = pstm.executeQuery();
        
        rs.last();
        int rowCount = rs.getRow();
        
        if (rowCount > 0) {
            config = new DataConfiguration(new PropertiesConfiguration());
        }
        
         rs.beforeFirst();
        
        while (rs.next()) {
            String key = rs.getString("ckey");
            String value = rs.getString("cvalue");
            byte[] value2 = rs.getBytes("cvalue2");
            
            if (value != null) {
                config.addProperty(SimpleStringCipher.decrypt(key), SimpleStringCipher.decrypt(value));
            } else {
                config.addProperty(SimpleStringCipher.decrypt(key), value2);
            }
        }

        rs.close();
        pstm.close();

        return config;
    }
}
