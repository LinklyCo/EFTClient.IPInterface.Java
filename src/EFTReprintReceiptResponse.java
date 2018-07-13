/**
 * A PC-EFTPOS duplicate receipt response object
 */
public class EFTReprintReceiptResponse extends EFTResponse {
    /**
     * Constructs a default duplicate receipt response object
     */
    public EFTReprintReceiptResponse() {
        super(EFTReprintReceiptRequest.class);
    }

    /**
     * <summary>Two digit merchant code</summary>
     */
    public String Merchant = "00";

    public String getMerchant() {
        return Merchant;
    }

    /**
     * <summary>Duplicate receipt text.</summary>
     */
    public String[] ReceiptText = new String[]{""};

    public String[] getReceiptText() {
        return ReceiptText;
    }

    /**
     * <summary>Indicates if the request was successful.</summary>
     */
    public boolean Success = false;

    public boolean isSuccess() {
        return Success;
    }

    /**
     * <summary>The response code of the request.</summary>
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
