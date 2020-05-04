package remotefndload;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class LdtEntities {
    private ArrayList<LdtEntity> list;

    public LdtEntities() throws Exception {
        load();
    }

    private void load() throws Exception {
        list = new ArrayList<LdtEntity>();
        File file = new File("entities.xml");
        if (file.exists()) {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            NodeList nodesList = document.getElementsByTagName("entity");
            for (int i = 0; i < nodesList.getLength(); i++) {
                Element tagLdtEntity = (Element)nodesList.item(i);
                String lctfile = tagLdtEntity.getAttribute("lctfile");
                String name = tagLdtEntity.getAttribute("name");
                String prompt = tagLdtEntity.getAttribute("prompt");

                NodeList nodesListParams =
                    tagLdtEntity.getElementsByTagName("param");
                int count = nodesListParams.getLength();
                ArrayList<LdtEntityParam> params =
                    new ArrayList<LdtEntityParam>();
                for (int k = 0; k < count; k++) {
                    Element tagParam = (Element)nodesListParams.item(k);
                    String paramName = tagParam.getAttribute("name");
                    String paramPrompt = tagParam.getAttribute("prompt");
                    String paramIsOptional = tagParam.getAttribute("optional");
                    String hint = tagParam.getAttribute("hint");
                    LdtEntityParam param =
                        new LdtEntityParam(paramName, paramPrompt,
                                           Boolean.valueOf(paramIsOptional),
                                           hint);
                    params.add(param);
                }

                LdtEntity ldtEntity =
                    new LdtEntity(lctfile, name, prompt, params);
                list.add(ldtEntity);
            }
        }
        
        sort();
    }

    public void save() throws Exception {
        DocumentBuilderFactory documentBuilderFactory =
            DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element ldtentitiesElement = document.createElement("entities");
        document.appendChild(ldtentitiesElement);

        Iterator<LdtEntity> iter = list.iterator();
        while (iter.hasNext()) {
            LdtEntity ldtEntity = iter.next();
            Element ldtentityElement = document.createElement("entity");
            ldtentityElement.setAttribute("lctfile", ldtEntity.getLctfile());
            ldtentityElement.setAttribute("name", ldtEntity.getName());
            ldtentityElement.setAttribute("prompt", ldtEntity.getPrompt());

            ArrayList<LdtEntityParam> params = ldtEntity.getParams();
            Iterator<LdtEntityParam> iterParams = params.iterator();
            while (iterParams.hasNext()) {
                LdtEntityParam param = iterParams.next();
                Element paramElement = document.createElement("param");
                paramElement.setAttribute("name", param.getName());
                paramElement.setAttribute("prompt", param.getPrompt());
                paramElement.setAttribute("optional",
                                          String.valueOf(param.isOptional()));
                paramElement.setAttribute("hint", param.getHint());

                ldtentityElement.appendChild(paramElement);
            }

            ldtentitiesElement.appendChild(ldtentityElement);
        }

        XmlUtil.saveDocumentToFile(document, "entities.xml");

    }

    public ArrayList<LdtEntity> getLdtEntitiesList() {
        return list;
    }
    
    private void sort() {
        Object[] oa = list.toArray();
        if (oa == null) return;
        if (oa.length < 2) return;
        Arrays.sort(oa);
        list.clear();
        for (Object o: oa) {
            list.add((LdtEntity)o);
        }
    }

    public LdtEntity getLdtEntityByPrompt(String prompt) {
        if (prompt == null) return null;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            LdtEntity entity = list.get(i);
            if (prompt.equals(entity.getPrompt()))
                return entity;
        }
        return null;
    }

}
