package pceft.sdk.eftclient.java;

public class EFTGenericCommandResponse extends EFTResponse {
    public EFTGenericCommandResponse() {
        super(EFTGenericCommandRequest.class);
    }

    //SHARED RESPONSE TYPES

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
     * Response Code e.g. 00 - Approved
     */
    public String ResponseCode = "";
    public String getResponseCode() {
        return ResponseCode;
    }
    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    /**
     * Response Text e.g. "SUCCESS             "
     */
    public String ResponseText = "";
    public String getResponseText() {
        return ResponseText;
    }
    public void setResponseText(String responseText) {
        ResponseText = responseText;
    }

    /**
     * Data
     */
    public String Data = "";
    public String getData() {
        return Data;
    }
    public void setData(String data) {
        Data = data;
    }

    // SLAVE COMMAND

    /**
     * Slave Response returned on response code 00 Sub Code 'Z'
     */
    public String SlaveResponse = "";
    public String getSlaveResponse() {
        return SlaveResponse;
    }
    public void setSlaveResponse(String response) {
        SlaveResponse = response;
    }

    // GET PASSWORD
    public boolean SuccessFlag = false;
    public boolean isSuccessFlag() {
        return SuccessFlag;
    }
    public void setSuccessFlag(boolean successFlag) {
        SuccessFlag = successFlag;
    }
    public void setSuccessFlag(char successFlag) {
        SuccessFlag = (successFlag == '1');
    }

    public String Password = "";
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }

    //PAY AT TABLE
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
}
