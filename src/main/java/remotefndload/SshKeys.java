package remotefndload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class SshKeys {
    private ArrayList<SshKey> list;

    public SshKeys() throws Exception {
        load();
    }

    private void load() throws ParserConfigurationException, SAXException,
                               IOException {
        list = new ArrayList<SshKey>();
        File file = new File("sshkeys.xml");
        if (file.exists()) {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodesList = document.getElementsByTagName("key");
            for (int i = 0; i < nodesList.getLength(); i++) {
                Element e = (Element)nodesList.item(i);
                String filename = e.getAttribute("filename");
                String passphrase = e.getAttribute("passphrase");
                String decryptedPassphrase = null;
                try {
                    decryptedPassphrase = Crypt.decrypt(passphrase);
                } catch (Exception ex) {
                    decryptedPassphrase = passphrase;
                }
                SshKey sshkey = new SshKey(filename, decryptedPassphrase);
                list.add(sshkey);
            }
        }
    }

    public void save() throws Exception {
        DocumentBuilderFactory documentBuilderFactory =
            DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element keysElement = document.createElement("keys");
        for (int i = 0; i < list.size(); i++) {
            SshKey sshkey = list.get(i);
            Element keyElement = document.createElement("key");
            String filename = sshkey.getFilename();
            String passphrase = sshkey.getPassphrase();
            if (passphrase != null && !passphrase.isEmpty()) {
                int ret =
                    JOptionPane.showConfirmDialog(null, "Save passphrase for " +
                                                  filename + "?", "Confirm",
                                                  JOptionPane.YES_NO_OPTION);
                if (ret != JOptionPane.YES_OPTION)
                    passphrase = "";
            }
            keyElement.setAttribute("filename", filename);
            keyElement.setAttribute("passphrase", Crypt.encrypt(passphrase));
            keysElement.appendChild(keyElement);
        }

        document.appendChild(keysElement);

        XmlUtil.saveDocumentToFile(document, "sshkeys.xml");
    }

    public ArrayList<SshKey> getKeysList() {
        return list;
    }

}
