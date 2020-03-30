package pceft.sdk.eftclient.java;

public enum ActionType {
    Unknown(-1),

    DisplayOptions(0),

    DisplayAndWait(1),

    DisplayWithButtons(2),

    GetNumericInput(3),

    GetAmountInput(4);

    public int actionType;
    ActionType(int i) {actionType = i;}

}
