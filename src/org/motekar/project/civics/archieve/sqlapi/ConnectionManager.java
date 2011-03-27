package org.motekar.project.civics.archieve.sqlapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import net.n3.nanoxml.XMLException;
import org.motekar.project.civics.archieve.utils.misc.ArchieveProperties;
import org.motekar.project.civics.archieve.utils.misc.ArchievePropertiesReader;
import org.openide.util.Exceptions;

/**
 *
 * @author Muhamad Wibawa
 */
public class ConnectionManager {

    private String userName = "";
    private String password = "";
    private String url = "";
    private Connection conn = null;
    private String server = "";
    private String dbname = "";
    private File file = null;
    private ArchieveProperties config;

    public ConnectionManager(File file) {
        this.file = file;
    }

    public Connection connect() {
        try {
            config = new ArchieveProperties(file.getName());
            ArchievePropertiesReader reader = new ArchievePropertiesReader(config.getXMLFile());
            reader.loadXML();
            config = reader.getProperties();
            server = config.getServerName();
            dbname = config.getDatabase();
            userName = config.getUserName();
            password = config.getPassword();
            System.out.println("DBName = " + dbname);
            url = "jdbc:postgresql://" + server + "/" + dbname;
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Connect Success");

        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
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
        return conn;
    }

    public ArchieveProperties getConfig() {
        return config;
    }

    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("Disconnect failed: " + e.getMessage());
        }
    }
}
