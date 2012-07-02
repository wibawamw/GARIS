package org.motekar.project.civics.archieve.utils.misc;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLException;
import net.n3.nanoxml.XMLWriter;
import org.motekar.util.user.misc.SimpleStringCipher;

/**
 *
 * @author Muhamad Wibawa
 */
public class ArchievePropertiesBuilder {
    private ArchieveProperties prop = null;
    private XMLElement root;

    public ArchievePropertiesBuilder(ArchieveProperties prop) {
        this.prop = prop;
    }

    public void createXML() throws IOException, XMLException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Writer writ = new FileWriter(prop.getXMLFile());
        XMLWriter writer = new XMLWriter(writ);
        createAppTag();
        writer.write(root);
    }

    private void createAppTag() throws XMLException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        root = new XMLElement("Properties");
        root.setAttribute("AppName", prop.getApplicationName());
        root.setAttribute("AppVersion", prop.getApplicationVersion());

        root.addChild(createServerTag());
        root.addChild(createDatabaseTag());
        root.addChild(createUserNameTag());
        root.addChild(createPasswordTag());
    }

    private XMLElement createServerTag() throws XMLException {
        XMLElement child = new XMLElement("Server");
        child.setContent(prop.getServerName());

        return child;
    }

    private XMLElement createDatabaseTag() throws XMLException {
        XMLElement child = new XMLElement("Database");
        child.setContent(prop.getDatabase());

        return child;
    }
    private XMLElement createUserNameTag() throws XMLException{
        XMLElement child = new XMLElement("UserName");
        child.setContent(prop.getUserName());

        return child;
    }

    private XMLElement createPasswordTag() throws XMLException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        XMLElement child = new XMLElement("Password");

        String password = prop.getPassword();
        child.setContent(SimpleStringCipher.encrypt(password));

        return child;
    }

}
