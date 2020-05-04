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

public class GeneralSettings {
    private String lookandfeel;
    private File lastUsedDir;
    private static GeneralSettings generalSetting;


    public static GeneralSettings getInstance() {
        if (generalSetting == null) {
            try {
                generalSetting = new GeneralSettings();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return generalSetting;
    }


    private GeneralSettings() throws Exception {
        load();
    }

    public String getLookandfeel() {
        return (lookandfeel == null || lookandfeel.length() == 0) ? UIManager.getSystemLookAndFeelClassName() : lookandfeel;
    }

    public void setLookandfeel(String lookandfeel) {
        this.lookandfeel = lookandfeel;
    }

    private void load() throws ParserConfigurationException, SAXException, IOException {
        File file = new File("general.xml");
        if (file.exists()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodeList = document.getElementsByTagName("lookandfeel");
            if (nodeList != null && nodeList.getLength() > 0) {
                Element element = (Element)nodeList.item(0);
                String lookandfeel = element.getTextContent();
                setLookandfeel(lookandfeel);
            }
            nodeList = document.getElementsByTagName("lastuseddir");
            if (nodeList != null && nodeList.getLength() > 0) {
                Element element = (Element)nodeList.item(0);
                String lastUsedDir = element.getTextContent();
                if (lastUsedDir != null && lastUsedDir.length() > 0)
                    setLastUsedDir(new File(lastUsedDir));
            }
        }
    }

    public void save() throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element generalElement = document.createElement("general");

        Element keyElement = document.createElement("lookandfeel");
        keyElement.setTextContent(getLookandfeel());
        generalElement.appendChild(keyElement);

        keyElement = document.createElement("lastuseddir");
        if (getLastUsedDir() != null)
            keyElement.setTextContent(getLastUsedDir().getAbsolutePath());
        generalElement.appendChild(keyElement);

        document.appendChild(generalElement);

        XmlUtil.saveDocumentToFile(document, "general.xml");
    }

    public File getLastUsedDir() {
        return lastUsedDir;
    }

    public void setLastUsedDir(File lastUsedDir) {
        this.lastUsedDir = lastUsedDir;
    }
}
