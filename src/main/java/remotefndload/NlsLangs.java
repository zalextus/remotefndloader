package remotefndload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;


public class NlsLangs
{
  private ArrayList<NlsLang> list;

  public NlsLangs()
    throws Exception
  {
    load();
  }

  private void load()
    throws Exception
  {
    list = new ArrayList<NlsLang>();
    File file = new File("nlslangs.xml");
    if (file.exists())
    {
      DocumentBuilderFactory factory =
        DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(file);
      NodeList nodesList = document.getElementsByTagName("nlslang");
      for (int i = 0; i < nodesList.getLength(); i++)
      {
        Element e = (Element) nodesList.item(i);

        String dbpassword = e.getAttribute("dbpassword");
        String decryptedDbpassword = null;
        try
        {
          decryptedDbpassword = Crypt.decrypt(dbpassword);
        }
        catch (Exception ex)
        {
          System.out.println(ex.getMessage());
          decryptedDbpassword = dbpassword;
        }

        String ospassword = e.getAttribute("ospassword");
        String decryptedOspassword = null;
        try
        {
          decryptedOspassword = Crypt.decrypt(ospassword);
        }
        catch (Exception ex)
        {
          System.out.println(ex.getMessage());
          decryptedOspassword = ospassword;
        }

        NlsLang nlsLang =
          new NlsLang(e.getAttribute("oracleencoding"), e.getAttribute("javaencoding"));
        list.add(nlsLang);
      }
    }
  }

  public void save()
    throws Exception
  {
    DocumentBuilderFactory documentBuilderFactory =
      DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
    Document document = builder.newDocument();
    Element nlsLangsElement = document.createElement("nlslangs");
    for (int i = 0; i < list.size(); i++)
    {
      NlsLang nlsLang = list.get(i);
      Element nlsLangtElement = document.createElement("nlslang");
      nlsLangtElement.setAttribute("oracleencoding",
                                   nlsLang.getOracleEncoding());
      nlsLangtElement.setAttribute("javaencoding",
                                   nlsLang.getJavaEncoding());
      nlsLangsElement.appendChild(nlsLangtElement);
    }

    document.appendChild(nlsLangsElement);

    XmlUtil.saveDocumentToFile(document, "nlslangs.xml");
  }

  public ArrayList<NlsLang> getNlsLangsList()
  {
    return list;
  }
  
  public NlsLang getNlsLangByOracleName(String oracleEncoding) {
      if (oracleEncoding == null) return null;
      int size = list.size();
      for (int i = 0; i < size; i++) {
          NlsLang entity = list.get(i);
          if (oracleEncoding.equals(entity.getOracleEncoding()))
              return entity;
      }
      return null;
  }

}
