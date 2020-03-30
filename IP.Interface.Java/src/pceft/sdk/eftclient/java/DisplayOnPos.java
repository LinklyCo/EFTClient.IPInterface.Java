package pceft.sdk.eftclient.java;

public class DisplayOnPos {
    //XML
    public String Line1 = "";
    public String getLine1() {
        return Line1;
    }
    public void setLine1(String line1) {
        Line1 = line1;
    }

    public String Line2 = "";
    public String getLine2() {
        return Line2;
    }
    public void setLine2(String line2) {
        Line2 = line2;
    }

    public String Line3 = "";
    public String getLine3() {
        return Line3;
    }
    public void setLine3(String line3) {
        Line3 = line3;
    }

    public int GraphicCode = 0; // From ActiveX documentation: 0: Processing, 1: Verify, 2: Question, 3: Card, 4: Account, 5: PIN, 6: Finished
    public int getGraphicCode() {
        return GraphicCode;
    }
    public void setGraphicCode(int graphicCode) {
        GraphicCode = graphicCode;
    }

    public int DialogId = 0;
    public int getDialogId() {
        return DialogId;
    }
    public void setDialogId(int dialogId) {
        DialogId = dialogId;
    }

    public String DialogTitle = "";
    public String getDialogTitle() {
        return DialogTitle;
    }
    public void setDialogTitle(String dialogTitle) {
        DialogTitle = dialogTitle;
    }

    public int DialogType = 0;
    public int getDialogType() {
        return DialogType;
    }
    public void setDialogType(int dialogType) {
        DialogType = dialogType;
    }

    public int DialogX = 0;
    public int getDialogX() {
        return DialogX;
    }
    public void setDialogX(int dialogX) {
        DialogX = dialogX;
    }

    public int DialogY = 0;
    public int getDialogY() {
        return DialogY;
    }
    public void setDialogY(int dialogY) {
        DialogY = dialogY;
    }

    public boolean DialogTopMost = false;
    public boolean isDialogTopMost() {
        return DialogTopMost;
    }
    public void setDialogTopMost(boolean dialogTopMost) {
        DialogTopMost = dialogTopMost;
    }

    public int ClearKeyMask  = 1; // default to clear the keys. Set to 0 if you want to keep whats there.
    public int getClearKeyMask() {
        return ClearKeyMask;
    }
    public void setClearKeyMask(int clearKeyMask) {
        ClearKeyMask = clearKeyMask;
    }
}
