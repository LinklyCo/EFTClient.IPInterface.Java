package pceft.sdk.eftclient.java;

public class EFTConfigureMerchantResponse extends EFTResponse{
    public EFTConfigureMerchantResponse() {
        super(EFTConfigureMerchantRequest.class);
    }

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

    public String ResponseCode = "99";
    public String getResponseCode() {
        return ResponseCode;
    }
    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String ResponseText = "";
    public String getResponseText() {
        return ResponseText;
    }
    public void setResponseText(String responseText) {
        ResponseText = responseText;
    }
}
