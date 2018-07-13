public class EFTSettlementResponse extends EFTResponse {

    public EFTSettlementResponse() {
        super(EFTSettlementRequest.class);
    }

    /**
     * <summary>Two digit merchant code. The default is "00"</summary>
     */
    public String Merchant = "";

    public String getMerchant() {
        return Merchant;
    }

    /**
     * <summary>Settlement data</summary>
     */
    public String SettlementData = "";

    public String getSettlementData() {
        return SettlementData;
    }

    /**
     * <summary>Indicates if the request was successful.</summary>
     */
    public boolean Success = false;

    public boolean isSuccess() {
        return Success;
    }

    /**
     * <summary>The response code of the request. A 2 character response code. "00" indicates a successful response.</summary>
     */
    public String ResponseCode = "";

    public String getResponseCode() {
        return ResponseCode;
    }

    /**
     * <summary>The response text for the response code.</summary>
     */
    public String ResponseText = "";

    public String getResponseText() {
        return ResponseText;
    }
}
