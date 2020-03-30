package pceft.sdk.eftclient.java;

public class EFTCloudPairRequest extends EFTRequest{
    public EFTCloudPairRequest() {super(true, EFTCloudPairResponse.class);}

    public String ClientID = "";
    public String getClientID() {
        return ClientID;
    }
    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String Password = "";
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }

    public String PairCode = "";
    public String getPairCode() {
        return PairCode;
    }
    public void setPairCode(String pairCode) {
        PairCode = pairCode;
    }

}
