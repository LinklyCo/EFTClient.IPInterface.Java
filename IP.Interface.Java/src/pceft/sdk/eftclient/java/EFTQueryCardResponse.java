package pceft.sdk.eftclient.java;

public class EFTQueryCardResponse extends EFTResponse {
    public EFTQueryCardResponse() {
        super( EFTQueryCardRequest.class);
    }

    public enum TrackFlags{
        None,
        Track1,
        Track2,
        Track3,
        Tracks1and2,
        Tracks2and3
    }



    public EFTTransactionRequest.AccountType AccountType = EFTTransactionRequest.AccountType.Default;
    public EFTTransactionRequest.AccountType getAccountType() {return AccountType;}
    public void setAccountType(EFTTransactionRequest.AccountType accountType) {
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

    //Can only have track 1 or track 3 at any one time
    public String Track1_Track3 = "";
    public void setTrack1_Track3(String track1_Track3) {
        Track1_Track3 = track1_Track3;
    }
    public String getTrack1() {
        switch(TracksRead) {
            case Track1:
            case Tracks1and2:
                return Track1_Track3;
            default:
                return "";
        }
    }
    public String getTrack3() {
        switch(TracksRead) {
            case Track3:
            case Tracks2and3:
                return Track1_Track3;
            default:
                return "";
        }
    }

    public TrackFlags TracksRead = TrackFlags.None;
    public TrackFlags getTracksRead() {
        return TracksRead;
    }
    public void setTracksRead(TrackFlags tracksRead) {
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
