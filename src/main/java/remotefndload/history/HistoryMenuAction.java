package remotefndload.history;

import remotefndload.FrameMain;
import remotefndload.action.LoaderAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class HistoryMenuAction extends AbstractAction {

    private FrameMain frame;
    private LoaderAction historyAction;

    public HistoryMenuAction(FrameMain frame, LoaderAction historyAction) {
        super(historyAction.toString());
        this.frame = frame;
        this.historyAction = historyAction;
    }    

//    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        frame.populateUiFromAction(historyAction);
    }
}
