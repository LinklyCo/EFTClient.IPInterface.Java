package pceft.sdk.eftclient.java;

/**
 * A PC-EFTPOS transaction request object.
 */

public class EFTGenericCommandRequest extends EFTRequest {
    public EFTGenericCommandRequest(){
        super(false, EFTGenericCommandResponse.class);
    }

    // SHARED TYPES
    /**
     * Sub Code Determining What POS Command
     */
    public enum SubCode{

        DisplayData('0'),

        PrintData('1'),

        GetPassword('2'),

        SlaveCommand('Z'),

        PayAtTable('@'),

        BasketData('%');

        public char SubCodeType;
        SubCode(char type) {SubCodeType = type;}
    }

    public SubCode CommandType = SubCode.DisplayData;
    public SubCode getCommandType() {
        return CommandType;
    }
    public void setCommandType(SubCode commandType) {
        CommandType = commandType;
    }

    /**
     * Number of lines of text that will follow (normally 02)
     */
    public int NumberOfLines = 2;
    public int getNumberOfLines() {
        return NumberOfLines;
    }
    public void setNumberOfLines(int numberLines) {
        NumberOfLines = numberLines;
    }

    /**
     * Length of each line of text that will follow (normally 020)
     */
    public int LineLength = 0;
    public int getLineLength() {
        return LineLength;
    }
    public void setLineLength(int lineLength) {
        LineLength = lineLength;
    }

    // DISPLAY DATA TYPES

    /**
     * 000 - 255 (Int in seconds)
     */
    public int Timeout = 0;
    public int getTimeout() {
        return Timeout;
    }
    public void setTimeout(int timeout) {
        Timeout = timeout;
    }

    /**
     * DisplayMap String (length 2)
     */
    public String DisplayMap = "";
    public String getDisplayMap() {
        return DisplayMap;
    }
    public void setDisplayMap(String displayMap) {
        DisplayMap = displayMap;
    }
    public void setDisplayMap(char pinpadDisplay, char posDisplay){
        DisplayMap = String.valueOf(pinpadDisplay) + String.valueOf(posDisplay);
    }

    /**
     * PinpadKeyMap String (length 8)
     */
    public String PinpadKeyMap = "";
    public String getPinpadKeyMap() {
        return PinpadKeyMap;
    }
    public void setPinpadKeyMap(String pinpadMap) {
        PinpadKeyMap = pinpadMap;
    }
    public void setPinpadKeyMap(char ENTERALLOWED, char CLEARALLOWED){
        PinpadKeyMap = String.valueOf(ENTERALLOWED) + String.valueOf(CLEARALLOWED) + "      "; // BYTES 2-7 NOT USED
    }

    /**
     * POSKeyMap String (length 8)
     */
    public String POSKeyMap = "";
    public String getPOSKeyMap() {
        return POSKeyMap;
    }
    public void setPOSKeyMap(String posMap) {
        POSKeyMap = posMap;
    }
    public void setPOSKeyMap(char OKBtnDisplay, char CancelBtnDisplay){
        POSKeyMap = String.valueOf(OKBtnDisplay) + String.valueOf(CancelBtnDisplay) + "      "; // BYTES 2-7 NOT USED
    }

    /**
     * POS Display Data (2 lines at 20 char each line). Length is 40
     */
    public String POSDisplayData = "";
    public String getPOSDisplayData() {
        return POSDisplayData;
    }
    public void setPOSDisplayData(String posDisplayData) {
        POSDisplayData = posDisplayData;
    }

    /**
     * The text to display. Length is number of lines * line length
     */
    public String PinpadLineData = "";
    public String getPinpadLineData() {
        return PinpadLineData;
    }
    public void setPinpadLineData(String lineData) {
        PinpadLineData = lineData;
    }

    // PRINT DATA TYPES

    /**
     * Printer ByteMap String (length 2)
     */
    public String PrinterByteMap = "";
    public String getPrinterByteMap() {
        return PrinterByteMap;
    }
    public void setPrinterByteMap(String printerMap) {
        PrinterByteMap = printerMap;
    }
    public void setPrinterByteMap(char NPTPrinter){
        PrinterByteMap = String.valueOf(NPTPrinter) + " ";
    }

    /**
     * The text to display. Length is number of lines * line length
     */
    public String PrintData = "";
    public String getPrintData() {
        return PrintData;
    }
    public void setPrintData(String printData) {
        PrintData = printData;
    }

    // SLAVE COMMAND TYPES
    /**
     * Request To Be Sent
     */
    public String SlaveRequest = "";
    public String getSlaveRequest() {
        return SlaveRequest;
    }
    public void setSlaveRequest(String request) {
        SlaveRequest = request;
    }

    // GET PASSWORD TYPES
    /**
     *
     */
    public int MinPasswordLength = 0;
    public int getMinPasswordLength() {
        return MinPasswordLength;
    }
    public void setMinPasswordLength(int minPasswordLength) {
        MinPasswordLength = minPasswordLength;
    }

    public int MaxPasswordLength = 0;
    public int getMaxPasswordLength() {
        return MaxPasswordLength;
    }
    public void setMaxPasswordLength(int maxPasswordLength) {
        MaxPasswordLength = maxPasswordLength;
    }
    public enum _PasswordDisplay
    {
        Enter_Code('1');
        public char pwrdDisplay;
        _PasswordDisplay(char c) {pwrdDisplay = c;}
    }

    public _PasswordDisplay PasswordDisplay = _PasswordDisplay.Enter_Code;
    public _PasswordDisplay getPasswordDisplay() {
        return PasswordDisplay;
    }
    public void setPasswordDisplay(_PasswordDisplay passwordDisplay) {
        PasswordDisplay = passwordDisplay;
    }

    //Pay At Table
    public String Header = "";
    public String getHeader() {
        return Header;
    }
    public void setHeader(String header) {
        Header = header;
    }

    public String Content = "";
    public String getContent() {
        return Content;
    }
    public void setContent(String content) {
        Content = content;
    }

    //BASKET DATA
    public IEFTBasketDataCommand BasketCommand = new EFTBasketDataCommandAdd();
    public IEFTBasketDataCommand getBasketCommand() {
        return BasketCommand;
    }
    public void setBasketCommand(IEFTBasketDataCommand basketCommand) {
        BasketCommand = basketCommand;
    }
}


