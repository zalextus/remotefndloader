package remotefndload;

import java.util.ArrayList;

public class LdtEntity implements Comparable {

    private String lctfile;
    private String name;
    private String prompt;
    private ArrayList<LdtEntityParam> params;

    public LdtEntity(String lctfile, String name, String prompt,
                     ArrayList<LdtEntityParam> params) {
        this.lctfile = lctfile;
        this.name = name;
        this.prompt = prompt;
        this.params = params;
    }

    public LdtEntity() {
        this.params = new ArrayList<LdtEntityParam>();
    }

    public void setLctfile(String lctfile) {
        this.lctfile = lctfile;
    }

    public String getLctfile() {
        return lctfile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setParams(ArrayList<LdtEntityParam> params) {
        this.params = params;
    }

    public ArrayList<LdtEntityParam> getParams() {
        return params;
    }
    
    public String toString() {
        return prompt;
    }

    public int compareTo(Object o) {
        LdtEntity entityOther = (LdtEntity)o;
        return prompt.compareTo(entityOther.getPrompt());
    }
}
