package org.motekar.project.civics.archieve.utils.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLException;
import net.n3.nanoxml.XMLParserFactory;
import org.motekar.util.user.misc.SimpleStringCipher;

/**
 *
 * @author Muhamad Wibawa
 */
public class ArchievePropertiesReader {
    // PropertiesReader Variable

    protected ArchieveProperties prop = null;
    private IXMLParser parser;
    private File file;

    public ArchievePropertiesReader(File file) {
        this.file = file;
    }

    /**
     *
     * @throws java.lang.Exception
     */
    public void loadXML() throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, FileNotFoundException, XMLException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Reader read = new FileReader(file);

        this.parser = XMLParserFactory.createDefaultXMLParser();

        IXMLReader reader = new StdXMLReader(read);
        parser.setReader(reader);

        prop = new ArchieveProperties();
        setApplicationData();
        prop.setXMLFile(file);
    }

    /**
     *
     * @throws java.lang.Exception
     */
    private void setApplicationData() throws XMLException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        XMLElement xml = (XMLElement) parser.parse();
        prop.setApplicationName(xml.getAttribute("AppName", ""));
        prop.setApplicationVersion(xml.getAttribute("AppVersion", ""));
        setServer(xml);
        setDatabase(xml);
        setUserName(xml);
        setPassword(xml);
    }

    /**
     *
     * @param parent
     * @throws java.lang.Exception
     */
    private void setServer(XMLElement parent) throws XMLException {
        ArrayList<XMLElement> v = parent.getChildrenNamed("Server");
        XMLElement child = (XMLElement) v.get(0);
        prop.setServerName(child.getContent());
    }

    /**
     *
     * @param parent
     * @throws java.lang.Exception
     */
    private void setDatabase(XMLElement parent) throws XMLException {
        ArrayList<XMLElement> v = parent.getChildrenNamed("Database");
        XMLElement child = (XMLElement) v.get(0);
        prop.setDatabase(child.getContent());
    }

    /**
     *
     * @param parent
     * @throws java.lang.Exception
     */
    private void setUserName(XMLElement parent) throws XMLException {
        ArrayList<XMLElement> v = parent.getChildrenNamed("UserName");
        XMLElement child = (XMLElement) v.get(0);
        prop.setUserName(child.getContent());
    }

    /**
     *
     * @param parent
     * @throws java.lang.Exception
     */
    private void setPassword(XMLElement parent) throws XMLException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        ArrayList<XMLElement> v = parent.getChildrenNamed("Password");
        XMLElement child = (XMLElement) v.get(0);

        String password = SimpleStringCipher.decrypt(child.getContent());

        prop.setPassword(password);
    }

    /**
     *
     * @return
     */
    public ArchieveProperties getProperties() {
        return this.prop;
    }
}
