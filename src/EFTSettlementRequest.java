

public class EFTSettlementRequest extends EFTRequest {
    /**
     * EFTPOS settlement types
     */
    enum SettlementType {
        /**
         * Perform a settlement on the terminal. Can only be performed once a day.
         */
        Settlement('S'),
        /**
         * Not supported by all pinpads
         */
        PreSettlement('P'),
        /**
         * Not supported by all pinpads
         */
        LastSettlement('L'),
        /**
         * Not supported by all pinpads
         */
        SummaryTotals('U'),
        /**
         * Not supported by all pinpads
         */
        SubShiftTotals('H'),
        /**
         * Not supported by all pinpads
         */
        DetailedTransactionListing('I'),
        /**
         * Not supported by all pinpads
         */
        StartCash('M'),
        /**
         * Not supported by all pinpads
         */
        StoreAndForwardTotals('F'),
        /**
         * Only used by St George
         */
        DailyCashStatement('D');

        public char SettleType;

        SettlementType(char c) {
            SettleType = c;
        }
    }

    public EFTSettlementRequest() {
        super(true, EFTSettlementResponse.class);
    }

    /**
     * <summary>Two digit merchant code. The default is "00"</summary>
     */
    public String Merchant = "00";

    /**
     * <summary>EFT settlement type. The default is "SettlementType.Settlement"</summary>
     */
    public SettlementType SettleType = SettlementType.Settlement;

    /**
     * <summary>Whether the request should reset totals for Sub-Shift Settlement type. The default is FALSE.</summary>
     */
    public boolean ResetTotals = false;

    /**
     * <summary>Additional information sent with the request.</summary>
     */
    public String PurchaseAnalysisData = "";

    /**
     * <summary>Indicates where the request is to be sent to. Should normally be EFTPOS.</summary>
     */
    public EFTTransactionRequest.TerminalApplication Application = EFTTransactionRequest.TerminalApplication.EFTPOS;

    /**
     * <summary>Indicates whether to trigger receipt events. The default is POSPrinter.</summary>
     */
    public ReceiptPrintModeType ReceiptPrintMode = ReceiptPrintModeType.POSPrinter;

    /**
     * <summary>Indicates whether PC-EFTPOS should cut receipts. The default is DontCut. This property only applies when EFTRequest.ReceiptPrintMode is set to EFTClientPrinter.</summary>
     */
    public ReceiptCutModeType ReceiptCutMode = ReceiptCutModeType.DontCut;

}
