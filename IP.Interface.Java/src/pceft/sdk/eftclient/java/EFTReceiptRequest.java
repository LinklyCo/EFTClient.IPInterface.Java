package pceft.sdk.eftclient.java;

/**
 * A PC-EFTPOS receipt request object
 */
public class EFTReceiptRequest extends EFTRequest {
    /**
     * Indicates the type of receipt that has been received.
     */
    public enum ReceiptType {
        /**
         * A logon receipt was received.
         */
        Logon('L'),
        /**
         * A customer transaction receipt was received.
         */
        Customer('C'),
        /**
         * A merchant transaction receipt was received.
         */
        Merchant('M'),
        /**
         * A settlement receipt was received. This receipt usually contains the signature receipt line and should be printed immediately
         */
        Settlement('S'),
        /**
         * Receipt text was received. Used internally by component. You should never receive this receipt type
         */
        ReceiptText('R');
        public char Receipt;

        ReceiptType(char c) {
            Receipt = c;
        }
    }

    /**
     * Constructs a default receipt response object
     */
    public EFTReceiptRequest(){
        super(false, EFTReceiptResponse.class);
    }
}
