package pceft.sdk.eftclient.java;

/**
 * A PC-EFTPOS receipt response object
 */
public class EFTReceiptResponse extends EFTResponse {

    public EFTReceiptResponse(){
        super(EFTReceiptRequest.class);
    }

    /**
     * The receipt type
     * @value The default is CustomerReceipt
     */
    public EFTReceiptRequest.ReceiptType Receipt = EFTReceiptRequest.ReceiptType.Customer;
    public EFTReceiptRequest.ReceiptType getReceipt() {
        return Receipt;
    }
    public void setReceipt(EFTReceiptRequest.ReceiptType receipt) {
        Receipt = receipt;
    }

    /**
     * Receipt text to be printed
     */
    public String[] ReceiptText = new String[]{""};
    public String[] getReceiptText() {
        return ReceiptText;
    }
    public void setReceiptText(String[] receiptText) {
        ReceiptText = receiptText;
    }

    /**
     * Receipt response is a pre-print
     */
    public boolean IsPrePrint = false;
    public boolean isPrePrint() {
        return IsPrePrint;
    }
    public void setPrePrint(boolean prePrint) {
        IsPrePrint = prePrint;
    }
}
