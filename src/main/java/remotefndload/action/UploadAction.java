package remotefndload.action;

public class UploadAction extends LoaderAction {
    private String customMode;
    private String uploadMode;
    
    public UploadAction() {
        super();
    }

    @Override
    public String getActionType() {
        return "UPLOAD";
    }

    public String getCustomMode() {
        return customMode;
    }

    public void setCustomMode(String customMode) {
        this.customMode = customMode;
    }

    public String getUploadMode() {
        return uploadMode;
    }

    public void setUploadMode(String uploadMode) {
        this.uploadMode = uploadMode;
    }
}
