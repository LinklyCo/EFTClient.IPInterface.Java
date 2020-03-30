package pceft.sdk.eftclient.java;

public class EFTCloudTokenLogonRequest extends EFTRequest {
    public EFTCloudTokenLogonRequest() {super(true, EFTCloudLogonResponse.class);}

    /**
     * Token received from a successful cloud pair request
     */
    public String Token = "";
    public String getToken() {
        return Token;
    }
    public void setToken(String token) {
        Token = token;
    }
}
