package remotefndload;

public class LdtEntityParam {
    private String name;
    private String prompt;
    private boolean isOptional;
    private String hint;

    public LdtEntityParam(String name, String prompt, boolean isOptional, String hint) {
        this.setName(name);
        this.setPrompt(prompt);
        this.setOptional(isOptional);
        this.setHint(hint);
    }

    public LdtEntityParam() {
    }

    public String getName() {
        return name;
    }

    public String getPrompt() {
        return prompt;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }
}
