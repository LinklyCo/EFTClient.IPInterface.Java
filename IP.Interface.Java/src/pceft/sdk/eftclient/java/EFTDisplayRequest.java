package pceft.sdk.eftclient.java;

public class EFTDisplayRequest extends EFTRequest {
    public EFTDisplayRequest(){
        super(false, EFTDisplayResponse.class);
    }

    public enum DisplayRequestFlag{

        Disabled('0'),

        Enabled('1');

        public char flagType;
        DisplayRequestFlag(char flag) {flagType = flag;}
    }

    public enum InputDataType{
        ASCII('1'),

        FormattedAmount('2'),

        DecimalFormatted('3'),

        MaskedPassword('4'),

        SupervisorRequest('5'),

        OneKey('6');

        public char InputType;
        InputDataType(char type) {InputType = type;}

    }

    public enum GraphicsCodeType{
        Processing('0'),

        Verify('1'),

        Question('2'),

        Card('3'),

        Account('4'),

        Pin('5'),

        Finished('6');

        public char GraphicType;
        GraphicsCodeType(char type) {GraphicType = type;}
    }

    /**
     * Number of lines of text that will follow (normally 02)
     */
    public String NumberOfLines = "";
    public String getNumberOfLines() {
        return NumberOfLines;
    }
    public void setNumberOfLines(String numberLines) {
        NumberOfLines = numberLines;
    }

    /**
     * Length of each line of text that will follow (normally 20)
     */
    public String LineLength = "";
    public String getLineLength() {
        return LineLength;
    }
    public void setLineLength(String lineLength) {
        LineLength = lineLength;
    }

    /**
     * The text to display. Length is number of lines * line length
     */
    public String DisplayText = "";
    public String getDisplayText() {
        return DisplayText;
    }
    public void setDisplayText(String displayText) {
        DisplayText = displayText;
    }

    /**
     * Indicates whether the CANCEL key can be pressed.
     */
    public DisplayRequestFlag CancelKeyFlag = DisplayRequestFlag.Disabled;
    public DisplayRequestFlag getCancelKeyFlag() {
        return CancelKeyFlag;
    }
    public void setCancelKeyFlag(DisplayRequestFlag cancelFlag) {
        CancelKeyFlag = cancelFlag;
    }
    public void setCancelKeyFlag(boolean cancelFlag) {
        CancelKeyFlag = (cancelFlag?DisplayRequestFlag.Enabled:DisplayRequestFlag.Disabled);
    }

    /**
     * Indicates whether the ACCEPT/YES key can be pressed.
     */
    public DisplayRequestFlag AcceptKeyFlag = DisplayRequestFlag.Disabled;
    public DisplayRequestFlag getAcceptKeyFlag() {
        return AcceptKeyFlag;
    }
    public void setAcceptKeyFlag(DisplayRequestFlag acceptFlag) {
        AcceptKeyFlag = acceptFlag;
    }
    public void setAcceptKeyFlag(boolean acceptFlag) {
        AcceptKeyFlag = (acceptFlag?DisplayRequestFlag.Enabled:DisplayRequestFlag.Disabled);
    }

    /**
     * Indicates whether the DECLINE/NO key can be pressed.
     */
    public DisplayRequestFlag DeclineKeyFlag = DisplayRequestFlag.Disabled;
    public DisplayRequestFlag getDeclineKeyFlag() {
        return DeclineKeyFlag;
    }
    public void setDeclineKeyFlag(DisplayRequestFlag declineFlag) {
        DeclineKeyFlag = declineFlag;
    }
    public void setDeclineKeyFlag(boolean declineFlag) {
        DeclineKeyFlag = (declineFlag?DisplayRequestFlag.Enabled:DisplayRequestFlag.Disabled);
    }

    /**
     * Indicates whether the AUTHORISE key can be pressed.
     */
    public DisplayRequestFlag AuthoriseKeyFlag = DisplayRequestFlag.Disabled;
    public DisplayRequestFlag getAuthoriseKeyFlag() {
        return AuthoriseKeyFlag;
    }
    public void setAuthoriseKeyFlag(DisplayRequestFlag authoriseFlag) {
        AuthoriseKeyFlag = authoriseFlag;
    }
    public void setAuthoriseKeyFlag(boolean authFlag) {
        AuthoriseKeyFlag = (authFlag?DisplayRequestFlag.Enabled:DisplayRequestFlag.Disabled);
    }

    /**
     * 	Input Data Field Enabled Key.
     */
    public InputDataType InputDataField = InputDataType.ASCII;
    public InputDataType getInputDataField() {
        return InputDataField;
    }
    public void setInputDataField(InputDataType inputData) {
        InputDataField = inputData;
    }

    /**
     * Indicates whether the OK key can be pressed.
     */
    public DisplayRequestFlag OKKeyFlag = DisplayRequestFlag.Disabled;
    public DisplayRequestFlag getOKKeyFlag() {
        return OKKeyFlag;
    }
    public void setOKKeyFlag(DisplayRequestFlag OKFlag) {
        OKKeyFlag = OKFlag;
    }
    public void setOKKeyFlag(boolean OKFlag) {
        OKKeyFlag = (OKFlag?DisplayRequestFlag.Enabled:DisplayRequestFlag.Disabled);
    }

    /**
     * 	Input Data Field Enabled Key.
     */
    public GraphicsCodeType GraphicsCode = GraphicsCodeType.Processing;
    public GraphicsCodeType getGraphicsCode() {
        return GraphicsCode;
    }
    public void setGraphicsCode(GraphicsCodeType graphicsCode) {
        GraphicsCode = graphicsCode;
    }


    /**
     * Additional information sent with the request.
     * @value Type: String
     */
    public String PurchaseAnalysisData = "";
    public String getPurchaseAnalysisData() {
        return PurchaseAnalysisData;
    }
    public void setPurchaseAnalysisData(String purchaseAnalysisData) {
        PurchaseAnalysisData = purchaseAnalysisData;
    }

}
