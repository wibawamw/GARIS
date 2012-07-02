package org.motekar.project.civics.archieve.sqlapi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import org.apache.commons.configuration.DataConfiguration;
import org.motekar.lib.common.utils.SimpleStringCipher;
import org.motekar.project.civics.archieve.utils.misc.ProfileAccount;
import org.motekar.util.user.misc.MotekarException;
import org.motekar.util.user.sqlapi.AuthBusinessLogic;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ConfigBusinessLogic {

    private Connection conn;
    private AuthBusinessLogic auth;
    private ConfigSQL sql = new ConfigSQL();

    public ConfigBusinessLogic(Connection conn) {
        this.conn = conn;
        this.auth = new AuthBusinessLogic(conn);
    }

    public void insertConfigurationSetting(Long session, DataConfiguration config) throws SQLException {
        int trans = Connection.TRANSACTION_READ_COMMITTED;
        try {

            if (!auth.isSessionExpired(session)) {
                throw new MotekarException("Session anda telah berakhir silahkan login kembali");
            }

            trans = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            sql.deleteConfigurationSetting(conn);

            Iterator it = config.getKeys();

            while (it.hasNext()) {
                Object obj = it.next();
                if (obj instanceof String) {
                    String key = (String) obj;
                    
                    System.out.println("Key = "+key);
                    
                    if (!key.equals("Logo") && !key.equals("Logo2") && !key.equals("Logo3")) {
                        String value = config.getString(key);
                        sql.insertConfigurationSetting(conn, SimpleStringCipher.encrypt(key), SimpleStringCipher.encrypt(value));
                    } else {
                        byte[] value = config.getByteArray(key);
                        sql.insertConfigurationSetting(conn, SimpleStringCipher.encrypt(key), value);
                    }
                }
            }


            conn.commit();
            conn.setAutoCommit(true);
            conn.setTransactionIsolation(trans);
        } catch (SQLException sqle) {
            Exceptions.printStackTrace(sqle);
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw sqle;
        } catch (Throwable anyOtherException) {
            Exceptions.printStackTrace(anyOtherException);
            try {
                conn.rollback();
                conn.setAutoCommit(true);
                conn.setTransactionIsolation(trans);
            } catch (Throwable e) {
            }
            throw new RuntimeException(anyOtherException);
        }
    }

    public ProfileAccount getProfileAccount() throws SQLException {
        try {
            DataConfiguration data = sql.getConfigurationSetting(conn);

            ProfileAccount profileAccount = null;
            if (data != null) {
                profileAccount = new ProfileAccount();
                profileAccount.setCompany(data.getString("Company"));
                profileAccount.setAddress(data.getString("Address"));
                profileAccount.setStateType(data.getString("StateType"));
                profileAccount.setState(data.getString("State"));
                profileAccount.setCapital(data.getString("Capital"));
                profileAccount.setProvince(data.getString("Province"));
                profileAccount.setByteLogo(data.getByteArray("Logo"));
                profileAccount.setByteLogo2(data.getByteArray("Logo2"));
                profileAccount.setByteLogo3(data.getByteArray("Logo3"));
            }

            return profileAccount;
        } catch (SQLException sqle) {
            throw sqle;
        } catch (Throwable anyOtherException) {
            throw new RuntimeException(anyOtherException);
        }
    }
}
