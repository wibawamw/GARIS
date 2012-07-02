package org.motekar.project.civics.archieve.utils.misc;

import java.io.File;

/**
 *
 * @author Muhamad Wibawa
 */
public class ArchieveProperties {

    private String serverName = "";
    private String database = "";
    
    private String applicationName = "";
    private String applicationVersion = "";
    protected File propFile = null;
    private String userName = "";
    private String password = "";


    public ArchieveProperties() {
    }

    public ArchieveProperties(String fileName) {
        if (fileName != null) {
            propFile = new File(fileName);
        }
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public File getXMLFile() {
        return propFile;
    }

    public void setXMLFile(File propFile) {
        this.propFile = propFile;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
