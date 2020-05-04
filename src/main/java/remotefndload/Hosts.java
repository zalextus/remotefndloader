package remotefndload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Hosts {
    private ArrayList<Host> list;

    public Hosts() throws Exception {
        load();
    }

    private void load() throws Exception {
        list = new ArrayList<Host>();
        File file = new File("hosts.xml");
        if (file.exists()) {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodesList = document.getElementsByTagName("host");
            for (int i = 0; i < nodesList.getLength(); i++) {
                Element e = (Element)nodesList.item(i);
                
                String dbpassword = e.getAttribute("dbpassword");
                String decryptedDbpassword = null;
                try {
                    decryptedDbpassword = Crypt.decrypt(dbpassword);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    decryptedDbpassword = dbpassword;
                }

                String ospassword = e.getAttribute("ospassword");
                String decryptedOspassword = null;
                try {
                    decryptedOspassword = Crypt.decrypt(ospassword);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    decryptedOspassword = ospassword;
                }
                
                Host connection =
                    new Host(e.getAttribute("prompt"), 
                             e.getAttribute("hostname"),
                             e.getAttribute("dbuser"),
                             decryptedDbpassword,
                             e.getAttribute("osuser"),
                             decryptedOspassword,
                             e.getAttribute("osdirname"),
                             e.getAttribute("envcmd"),
                             e.getAttribute("command"),
                             e.getAttribute("remotetmpcmdfilename"));
                list.add(connection);
            }
        }
        
        sort();
    }

    public void save() throws Exception {
    	DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
    	Document document = builder.newDocument();
    	Element hostsElement = document.createElement("hosts");
    	for (int i = 0; i < list.size(); i++) {
    		Host host = list.get(i);
    		Element hostElement = document.createElement("host");
    		hostElement.setAttribute("prompt", host.getPrompt());
    		hostElement.setAttribute("hostname", host.getHostname());
    		hostElement.setAttribute("dbuser", host.getDbUser());
    		hostElement.setAttribute("dbpassword", Crypt.encrypt(host.getDbPassword()));
    		hostElement.setAttribute("osuser", host.getOsUser());
    		hostElement.setAttribute("ospassword", Crypt.encrypt(host.getOsPassword()));
    		hostElement.setAttribute("osdirname", host.getOsdirname());
    		hostElement.setAttribute("envcmd", host.getEnvcmd());
    	    hostElement.setAttribute("command", host.getCommand());
    	    hostElement.setAttribute("remotetmpcmdfilename", host.getRemoteTempCommandFileName());
    		hostsElement.appendChild(hostElement);
    	}

    	document.appendChild(hostsElement);

    	XmlUtil.saveDocumentToFile(document, "hosts.xml");
    }

    public ArrayList<Host> getHostsList() {
        return list;
    }

    private void sort() {
        Object[] oa = list.toArray();
        if (oa == null) return;
        if (oa.length < 2) return;
        Arrays.sort(oa);
        list.clear();
        for (Object o: oa) {
            list.add((Host)o);
        }
    }
    
    public Host getHostByPrompt(String prompt) {
        if (prompt == null) return null;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Host host = list.get(i);
            if (prompt.equals(host.getPrompt()))
                return host;
        }
        return null;
    }
}
