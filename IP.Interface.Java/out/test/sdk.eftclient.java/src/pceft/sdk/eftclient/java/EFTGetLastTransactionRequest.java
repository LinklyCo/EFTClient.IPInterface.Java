package pceft.sdk.eftclient.java;

/**
 * A PC-EFTPOS get last transaction request object
 */
public class EFTGetLastTransactionRequest extends EFTRequest {
    /**
     * Constructs a default EFTGetLastTransaction
     */
    public EFTGetLastTransactionRequest() {
        super(true, EFTGetLastTransactionResponse.class);
    }

    /**
     * <summary>Two digit merchant code. The default is "00"</summary>
     */
    public String Merchant = "00";

    /**
     * <summary>Indicates where the request is to be sent. Default is EFTPOS</summary>
     */
    public EFTTransactionRequest.TerminalApplication Application = EFTTransactionRequest.TerminalApplication.EFTPOS;
}
