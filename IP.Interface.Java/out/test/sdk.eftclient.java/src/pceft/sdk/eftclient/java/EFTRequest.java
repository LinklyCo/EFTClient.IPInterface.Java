package pceft.sdk.eftclient.java;

import java.lang.reflect.Type;

/**
 * Abstract base class for the EFT client requests
 */
public abstract class EFTRequest {
    /**
     * Receipt mode (pos, windows or pinpad printer
     * Sometimes called the "ReceiptAutoPrint" flag
     */
    public enum ReceiptPrintModeType{
        /**
         * Receipts will be passed back to the POS in the PrintReceipt event
         */
        POSPrinter('0'),
        /**
         * The EFT-Client will attempt to print using the printer configured in the EFT-Client (Windows only)
         */
        EFTClientPrinter('1'),
        /**
         * Receipts will be printed using the pinpad printer
         */
        PinpadPrinter('9');

        ReceiptPrintModeType(char c){
            PrintType = c;
        }
        public char PrintType;
    }

    /**
     * Receipt cut mode (cut or don't cut). Used when the EFT-Client is handling receipt (ReceiptPrintMode = ReceiptPrintModeType.EFTClientPrinter)
     */
    public enum ReceiptCutModeType{
        /**
         * Don't cut receipts
         */
        DontCut('0'),
        /**
         * Cut receipts
         */
        Cut('1');

        public char CutType;
        ReceiptCutModeType(char c){
            CutType = c;
        }
    }

    protected boolean isStartOfTransaction = true;
    protected Type pairedResponseType = null;
    private EFTRequest(){

    }

    public EFTRequest(boolean isStart, Type pairedResponse){
        if(pairedResponse != null && !pairedResponse.getClass().isInstance(EFTResponse.class)){
            throw new IllegalStateException("pairedResponseType must be based on EFTResponse");
        }
        isStartOfTransaction = isStart;
        pairedResponseType = pairedResponse;
    }

    /**
     * True if this request starts a paired transaction request/response with displays etc (i.e. transaction, logon, settlement etc)
     * @return
     */
    public boolean isStartOfTransaction() {
        return isStartOfTransaction;
    }

    /**
     * Indicates the paired EFTResponse for this EFTRequest if one exists. Null otherwise.
     * e.g. EFTLogonRequest will have a paired EFTLogonResponse response.
     * @return
     */
    public Type getPairedResponseType() {
        return pairedResponseType;
    }
}
