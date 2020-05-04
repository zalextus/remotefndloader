package remotefndload.history;

import remotefndload.action.LoaderAction;

import java.util.ArrayList;
import java.util.List;

public class HistoryActions {
    
    private List<LoaderAction> list = new ArrayList<LoaderAction>();
    
    public HistoryActions() {
        super();
    }

    public List<LoaderAction> getList() {
        return list;
    }

    public void setList(List<LoaderAction> list) {
        this.list = list;
    }
}
