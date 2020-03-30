package pceft.sdk.eftclient.java;

public class EFTChequeAuthResponse extends EFTResponse {
    public EFTChequeAuthResponse() {
        super(EFTChequeAuthRequest.class);
    }

    public boolean SuccessFlag = false;
    public boolean isSuccessFlag() {
        return SuccessFlag;
    }
    public void setSuccessFlag(boolean successFlag) {
        SuccessFlag = successFlag;
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

    public String Merchant = "00";
    public String getMerchant() {
        return Merchant;
    }
    public void setMerchant(String merchant) {
        Merchant = merchant;
    }

    public double AmtPurchase = 0;
    public double getAmtPurchase() {
        return AmtPurchase;
    }
    public void setAmtPurchase(double amtPurchase) {
        AmtPurchase = amtPurchase;
    }

    public String AuthCode = "";
    public String getAuthCode() {
        return AuthCode;
    }
    public void setAuthCode(String authCode) {
        AuthCode = authCode;
    }

    public String ReferenceCode = "";
    public String getReferenceCode() {
        return ReferenceCode;
    }
    public void setReferenceCode(String referenceCode) {
        ReferenceCode = referenceCode;
    }
}
