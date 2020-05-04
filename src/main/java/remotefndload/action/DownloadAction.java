package remotefndload.action;

import java.util.HashMap;

public class DownloadAction extends LoaderAction {
    private HashMap params;
    
    public DownloadAction() {
        super();
    }

    @Override
    public String getActionType() {
        return "DOWNLOAD";
    }

    public HashMap getParams() {
        return params;
    }

    public void setParams(HashMap params) {
        this.params = params;
    }
}
