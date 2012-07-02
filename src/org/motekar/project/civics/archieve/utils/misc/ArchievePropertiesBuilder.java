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
        root.addChild(createCompanyTag());
        root.addChild(createAddressTag());
        root.addChild(createStateTag());
        root.addChild(createCapitalTag());
        root.addChild(createProvinceTag());
        root.addChild(createDivisionTag());
        root.addChild(createLogosTag());
        root.addChild(createLogos2Tag());
        root.addChild(createLogos3Tag());
        root.addChild(createLogos4Tag());
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

    private XMLElement createCompanyTag() throws XMLException {
        XMLElement child = new XMLElement("Company");
        if (!prop.getAddress().equals("")) {
            child.setAttribute("Name", prop.getCompany());
            child.addChild(createAddressTag());
        } else {
            child.setContent(prop.getCompany());
        }

        return child;
    }

    private XMLElement createAddressTag() throws XMLException {
        XMLElement child = new XMLElement("Address");
        child.setContent(prop.getAddress());

        return child;
    }

    private XMLElement createCapitalTag() throws XMLException {
        XMLElement child = new XMLElement("Capital");
        child.setContent(prop.getCapital());

        return child;
    }

    private XMLElement createStateTag() throws XMLException {
        XMLElement child = new XMLElement("State");
        child.setAttribute("Type", prop.getStateType());
        child.setContent(prop.getState());

        return child;
    }

    private XMLElement createProvinceTag() throws XMLException {
        XMLElement child = new XMLElement("Province");
        child.setContent(prop.getProvince());

        return child;
    }


    private XMLElement createDivisionTag() throws XMLException {
        XMLElement child = new XMLElement("Division");
        child.setAttribute("Code", prop.getDivisionCode());
        child.setAttribute("Name", prop.getDivisionName());
        return child;
    }

    private XMLElement createLogosTag() throws XMLException {
        XMLElement child = new XMLElement("Logos");
        child.setAttribute("Name", prop.getLogoFileName());

        return child;
    }

    private XMLElement createLogos2Tag() throws XMLException {
        XMLElement child = new XMLElement("Logos2");
        child.setAttribute("Name", prop.getLogo2FileName());

        return child;
    }

    private XMLElement createLogos3Tag() throws XMLException {
        XMLElement child = new XMLElement("Logos3");
        child.setAttribute("Name", prop.getLogo3FileName());

        return child;
    }
    
    private XMLElement createLogos4Tag() throws XMLException {
        XMLElement child = new XMLElement("Logos4");
        child.setAttribute("Name", prop.getLogo4FileName());

        return child;
    }

}
