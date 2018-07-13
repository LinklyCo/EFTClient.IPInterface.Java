/**
 * A PC-EFTPOS duplicate receipt request object
 */
public class EFTReprintReceiptRequest extends EFTRequest {

    enum ReprintType {
        /**
         * Get the last receipt
         */
        GetLast('2'),
        /**
         * Reprint the last receipt
         */
        Reprint('1');

        public char PrintType;

        ReprintType(char c) {
            PrintType = c;
        }
    }

    /**
     * Constructs a default EFTDuplicateReceiptRequest object
     */
    public EFTReprintReceiptRequest() {
        super(true, EFTReprintReceiptResponse.class);
    }

    /**
     * <summary>Two digit merchant code. Default is "00"</summary>
     */
    public String Merchant = "00";

    /**
     * <summary>Indicates where the request is to be sent to. Should normally be EFTPOS.</summary>
     */
    public EFTTransactionRequest.TerminalApplication Application = EFTTransactionRequest.TerminalApplication.EFTPOS;

    /**
     * <summary>Indicates whether to trigger receipt events.</summary>
     */
    public ReceiptPrintModeType ReceiptPrintMode = ReceiptPrintModeType.POSPrinter;

    /**
     * <summary>Indicates whether PC-EFTPOS should cut receipts.</summary>
     */
    public ReceiptCutModeType ReceiptCutMode = ReceiptCutModeType.DontCut;

    /**
     * <summary>Indicates whether the receipt should be returned or reprinted.</summary>
     */
    public ReprintType ReprntType = ReprintType.GetLast;
}
