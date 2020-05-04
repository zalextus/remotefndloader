package remotefndload.action;

import java.io.File;

public abstract class LoaderAction {
    private String hostName;
    private String entityName;
    private File file;
    private String languageName;
    private String stringName;

    public LoaderAction() {
        super();
    }

    abstract public String getActionType();

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
    
    public String toString() {
        return getActionType() + " " + hostName + " " + entityName + " " + 
               languageName + " " + file.getName();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
