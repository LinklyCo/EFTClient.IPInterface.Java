package pceft.sdk.eftclient.java;

public class EFTClientListResponse extends EFTResponse {
    public EFTClientListResponse() {
        super(EFTClientListRequest.class);
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
    // |
    // V
    /**
     * MAKE INTO LIST
     */
    public ClientDesc Response = new ClientDesc();
    public ClientDesc getResponse() {
        return Response;
    }
    public void setResponse(ClientDesc response) {
        Response = response;
    }

}

class ClientDesc{
    public ClientDesc() {}

    public String ClientName = "";
    public String getClientName() {
        return ClientName;
    }
    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String ClientIP = "";
    public String getClientIP() {
        return ClientIP;
    }
    public void setClientIP(String clientIP) {
        ClientIP = clientIP;
    }

    public String ClientPort = "";
    public String getClientPort() {
        return ClientPort;
    }
    public void setClientPort(String clientPort) {
        ClientPort = clientPort;
    }

    public String ClientState = "";
    public String getClientState() {
        return ClientState;
    }
    public void setClientState(String clientState) {
        ClientState = clientState;
    }
}
