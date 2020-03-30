package pceft.sdk.eftclient.java;

public class EFTQueryCardResponse extends EFTResponse {
    public EFTQueryCardResponse() {
        super( EFTQueryCardRequest.class);
    }

    public enum SelectAccountType{
        NoAccountSelected(' '),

        SelectAccount_ReadCard('1'),

        GetAccountOnly('5');

        SelectAccountType(char c){
            sAccountType = c;
        }
        public char sAccountType;
    }

    public SelectAccountType AccountType = SelectAccountType.NoAccountSelected;
    public SelectAccountType getAccountType() {return AccountType;}
    public void setAccountType(SelectAccountType accountType) {
        AccountType = accountType;
    }

    public boolean SuccessFlag = false;
    public boolean getSuccessFlag() {return SuccessFlag;}
    public void setSuccessFlag(boolean successFlag){
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
    public String getResponseText() {return ResponseText;}
    public void setResponseText(String responseText) {
        ResponseText = responseText;
    }

    public String Track2 = "";
    public String getTrack2() {return Track2;}
    public void setTrack2(String track2) {
        Track2 = track2;
    }

    public String Track1_Track3 = "";
    public String getTrack1_Track3() {
        return Track1_Track3;
    }
    public void setTrack1_Track3(String track1_Track3) {
        Track1_Track3 = track1_Track3;
    }

    public char TracksRead = '1';
    public char getTracksRead() {
        return TracksRead;
    }
    public void setTracksRead(char tracksRead) {
        TracksRead = tracksRead;
    }

    public String BinNumber = "00";
    public String getBinNumber() {
        return BinNumber;
    }
    public void setBinNumber(String binNumber) {
        BinNumber = binNumber;
    }

    /**
     * Additional information sent with the request.
     * @value Type: String
     */
    public String PurchaseAnalysisData = "";
    public String getPurchaseAnalysisData() {
        return PurchaseAnalysisData;
    }
    public void setPurchaseAnalysisData(String purchaseAnalysisData) {
        PurchaseAnalysisData = purchaseAnalysisData;
    }
}
