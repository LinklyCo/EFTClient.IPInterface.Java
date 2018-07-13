/**
 * A PC-EFTPOS show control panel response object
 */
public class EFTControlPanelResponse extends EFTResponse {
    /**
     * Constructs a default show control panel response object
     */
    EFTControlPanelResponse() {
        super(EFTControlPanelRequest.class);
    }

    /**
     * Indicates if the request was successful
     */
    public boolean Success = false;

    public boolean isSuccess() {
        return Success;
    }

    /**
     * The response code for the request
     */
    String ResponseCode = "";

    public String getResponseCode() {
        return ResponseCode;
    }

    /**
     * The response text for the response code
     */
    String ResponseText = "";

    public String getResponseText() {
        return ResponseText;
    }
}
