package pceft.sdk.eftclient.java;

import java.time.Instant;
import java.util.Date;

/**
 * A PC-EFTPOS terminal logon response object
 */
public class EFTLogonResponse extends EFTResponse{
    /**
     * Constructs a default terminal logon response object
     */
    public EFTLogonResponse(){
        super(EFTLogonRequest.class);
    }

    /**
     * Pinpad software version
     */
    public String PinPadVersion = "";
    public String getPinPadVersion() {
        return PinPadVersion;
    }
    public void setPinPadVersion(String pinPadVersion) {
        PinPadVersion = pinPadVersion;
    }

    /**
     * Indicates if the request was successful
     */
    public boolean Success = false;
    public boolean isSuccess() {
        return Success;
    }
    public void setSuccess(boolean success) {
        Success = success;
    }
    public void setSucces(char c){
        if (c == '1')
            Success = true;
        else if (c == '0')
            Success = false;
    }

    /**
     * The response code of the request
     * @value A 2 character response code. "00" indicates a successful response
     */
    public String ResponseCode = "";
    public String getResponseCode() {
        return ResponseCode;
    }
    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    /**
     * The response text for the response code
     */
    public String ResponseText = "";
    public String getResponseText() {
        return ResponseText;
    }
    public void setResponseText(String responseText) {
        ResponseText = responseText;
    }

    /**
     * Date and time of the response returned by the bank
     */
    public Date BankDate = Date.from(Instant.EPOCH);
    public Date getBankDate() {
        return BankDate;
    }
    public void setBankDate(Date bankDate) {
        BankDate = bankDate;
    }

    /**
     * Terminal ID configured in the Pinpad
     */
    public String Catid = "";
    public String getCatid() {
        return Catid;
    }
    public void setCatid(String catid) {
        Catid = catid;
    }

    /**
     * Merchant ID configured in the Pinpad
     */
    public String Caid = "";
    public String getCaid() {
        return Caid;
    }
    public void setCaid(String caid) {
        Caid = caid;
    }

    /**
     * System Trace Audit Number
     */
    public int Stan = 0;
    public int getStan() {
        return Stan;
    }
    public void setStan(int stan) {
        Stan = stan;
    }

    /**
     * Additional information sent with the response
     */
    public String PurchaseAnalysisData = "";
    public String getPurchaseAnalysisData() {
        return PurchaseAnalysisData;
    }
    public void setPurchaseAnalysisData(String purchaseAnalysisData) {
        PurchaseAnalysisData = purchaseAnalysisData;
    }
}
