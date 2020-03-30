package pceft.sdk.eftclient.java;

public class EFTDisplayResponse extends EFTResponse {
    EFTDisplayResponse() {
        super(EFTDisplayRequest.class);
    }

    public enum InputType{
        None('0'),
        Normal('1'),
        Amount('2'),
        Decimal('3'),
        Password('4');

        public char Input;

        InputType(char c){
            Input = c;
        }
    }

    public enum GraphicCode{
        Processing('0'),
        Verify('1'),
        Question('2'),
        Card('3'),
        Account('4'),
        PIN('5'),
        Finished('6'),
        None(' ');

        public char Code;
        GraphicCode(char c){
            Code = c;
        }
    }

    /**
     * Number of lines to display
     * @value Type: int.
     */
    public int NumberOfLines = 2;

    public int getNumberOfLines() {
        return NumberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        NumberOfLines = numberOfLines;
    }

    /**
     * Number of characters per display line.
     * @value Type: int.
     */
    public int LineLength = 20;

    public int getLineLength() {
        return LineLength;
    }

    public void setLineLength(int lineLength) {
        LineLength = lineLength;
    }

    /**
     * Text to be displayed. Each display line is concatenated.
     * @value Type: String array
     */
    public String[] DisplayText = new String[]{"", ""};

    public String[] getDisplayText() {
        return DisplayText;
    }

    public void setDisplayText(String[] displayText) {
        DisplayText = displayText;
    }

    /**
     * Indicates whether the Cancel button is to be displayed.
     * @value Type: boolean.
     */
    public boolean CancelKeyFlag = false;

    public boolean isCancelKeyFlag() {
        return CancelKeyFlag;
    }

    public void setCancelKeyFlag(boolean cancelKeyFlag) {
        CancelKeyFlag = cancelKeyFlag;
    }

    /**
     * Indicates whether the Accept/Yes button is to be displayed.
     * @value Type: boolean.
     */
    public boolean AcceptYesKeyFlag = false;

    public boolean isAcceptYesKeyFlag() {
        return AcceptYesKeyFlag;
    }

    public void setAcceptYesKeyFlag(boolean acceptYesKeyFlag) {
        AcceptYesKeyFlag = acceptYesKeyFlag;
    }

    /**
     * Indicates whether the Decline/No button is to be displayed.
     * @value Type: boolean.
     */
    public boolean DeclineNoKeyFlag = false;

    public boolean isDeclineNoKeyFlag() {
        return DeclineNoKeyFlag;
    }

    public void setDeclineNoKeyFlag(boolean declineNoKeyFlag) {
        DeclineNoKeyFlag = declineNoKeyFlag;
    }

    /**
     * Indicates whether the Authorise button is to be displayed.
     * @value Type: boolean.
     */
    public boolean AuthoriseKeyFlag = false;

    public boolean isAuthoriseKeyFlag() {
        return AuthoriseKeyFlag;
    }

    public void setAuthoriseKeyFlag(boolean authoriseKeyFlag) {
        AuthoriseKeyFlag = authoriseKeyFlag;
    }

    /**
     * Indicates whether the OK button is to be displayed.
     * @value Type: boolean.
     */
    public boolean OKKeyFlag = false;

    public boolean isOKKeyFlag() {
        return OKKeyFlag;
    }

    public void setOKKeyFlag(boolean OKKeyFlag) {
        this.OKKeyFlag = OKKeyFlag;
    }

    public InputType Input = InputType.None;

    public InputType getInput() {
        return Input;
    }

    public void setInput(InputType input) {
        Input = input;
    }

    public InputType setInput(char c) {
        InputType result = null;
        switch (c){
            case '0':
            default:
                result = InputType.None;
                break;
            case '1':
                result = InputType.Normal;
                break;
            case '2':
                result = InputType.Amount;
                break;
            case '3':
                result = InputType.Decimal;
                break;
            case '4':
                result = InputType.Password;
                break;
        }
        return result;
    }

    public GraphicCode Graphic = GraphicCode.None;

    public GraphicCode getGraphic() {
        return Graphic;
    }

    public void setGraphic(GraphicCode graphic) {
        Graphic = graphic;
    }

    public GraphicCode setGraphic(char c) {
        GraphicCode result = null;
        switch (c){
            case('0'):
                result = GraphicCode.Processing;
                break;
            case('1'):
                result = GraphicCode.Verify;
                break;
            case('2'):
                result = GraphicCode.Question;
                break;
            case('3'):
                result = GraphicCode.Card;
                break;
            case('4'):
                result = GraphicCode.Account;
                break;
            case('5'):
                result = GraphicCode.PIN;
                break;
            case('6'):
                result = GraphicCode.Finished;
                break;
            case(' '):
            default:
                result = GraphicCode.None;
                break;
        }
        return result;
    }

    public String PurchaseAnalysisData = "";

    public String getPurchaseAnalysisData() {
        return PurchaseAnalysisData;
    }

    public void setPurchaseAnalysisData(String purchaseAnalysisData) {
        PurchaseAnalysisData = purchaseAnalysisData;
    }
}
