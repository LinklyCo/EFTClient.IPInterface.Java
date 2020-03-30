package pceft.sdk.eftclient.java;

/**
 * A PC-EFTPOS set dialog response object
 */
public class EFTSetDialogResponse extends EFTResponse {
    /**
     * Constructs a default set dialog response object
     */
    public EFTSetDialogResponse() {
        super(EFTSetDialogRequest.class);
    }

    public boolean Success = false;

    public boolean isSuccess() {
        return Success;
    }
}
