package pceft.sdk.eftclient.java;

public class EFTPinpadBusyResponse extends EFTResponse {

    public EFTPinpadBusyResponse() {
        super(null);
    }

    public String ResponseCode = "BY";

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String ResponseText = "PinpadBusy";

    public String getResponseText() {
        return ResponseText;
    }

    public void setResponseText(String responseText) {
        ResponseText = responseText;
    }


}
