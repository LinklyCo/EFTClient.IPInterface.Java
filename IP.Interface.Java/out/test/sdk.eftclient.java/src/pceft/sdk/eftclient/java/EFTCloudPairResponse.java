package pceft.sdk.eftclient.java;

public class EFTCloudPairResponse extends EFTResponse {
    public EFTCloudPairResponse() { super(EFTCloudPairRequest.class);}

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

    public int RedirectPort = 0;
    public int getRedirectPort() {
        return RedirectPort;
    }
    public void setRedirectPort(int redirectPort) {
        RedirectPort = redirectPort;
    }

    public String RedirectAddress = "";
    public String getRedirectAddress() {
        return RedirectAddress;
    }
    public void setRedirectAddress(String redirectAddress) {
        RedirectAddress = redirectAddress;
    }

    public String Token = "";
    public String getToken() {
        return Token;
    }
    public void setToken(String token) {
        Token = token;
    }
}
