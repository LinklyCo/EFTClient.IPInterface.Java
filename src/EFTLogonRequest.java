public class EFTLogonRequest extends EFTRequest{
    /**
     * Indicates the type of logon to perform
     */
    enum LogonType {
        /**
         * Standard EFT logon to the bank.
         */
        Standard(' '),
        /**
         * Not supported by all pinpads
         */
        RSA('4'),
        /**
         * Not supported by all pinpads
         */
        TMSFull('5'),
        /**
         * Not supported by all pinpads
         */
        TMSParams('6'),
        /**
         * Not supported by all pinpads
         */
        TMSSoftware('7'),
        /**
         * exclude
         */
        Logoff('8'),
        /**
         * Enables diagnostics
         */
        Diagnostics('1');

        LogonType(char c) {
            logType = c;
        }
        public char logType;
    }
    EFTLogonRequest(){
        this(LogonType.Standard);
    }
    public EFTLogonRequest(LogonType logType){
        super(true, EFTLogonResponse.class);
        this.logonType = logType;

    }

    /**
     * Two digit merchant code
     * The default is "00"
     */
    public String Merchant = "00";
    public String getMerchant() {
        return Merchant;
    }
    public void setMerchant(String merchant) {
        Merchant = merchant;
    }

    /**
     * Type of logon to perform
     * @value The default is LogonType.Standard
     */
    public LogonType logonType = LogonType.Standard;
    public LogonType getLogonType() {
        return logonType;
    }
    public void setLogonType(LogonType logonType) {
        this.logonType = logonType;
    }

    /**
     * Additional information sent with the request
     */
    public String PurchaseAnalysisData = "";
    public String getPurchaseAnalysisData() {
        return PurchaseAnalysisData;
    }
    public void setPurchaseAnalysisData(String purchaseAnalysisData) {
        PurchaseAnalysisData = purchaseAnalysisData;
    }

    /**
     * Indicates where the request is to be sent to. Should normally be EFTPOS ('00')
     */
    public String Application = "00";
    public String getApplication() {
        return Application;
    }
    public void setApplication(String application) {
        Application = application;
    }

    /**
     * Indicates whether to trigger receipt events
     * @value Default is POSPrinter
     */
    public ReceiptPrintModeType ReceiptPrintMode = ReceiptPrintModeType.POSPrinter;
    public ReceiptPrintModeType getReceiptPrintMode() {
        return ReceiptPrintMode;
    }
    public void setReceiptPrintMode(ReceiptPrintModeType receiptPrintMode) {
        ReceiptPrintMode = receiptPrintMode;
    }

    /**
     * Indicates whether PC-EFTPOS should cut receipts
     */
    public ReceiptCutModeType ReceiptCutMode = ReceiptCutModeType.DontCut;
    public ReceiptCutModeType getReceiptCutMode() {
        return ReceiptCutMode;
    }
    public void setReceiptCutMode(ReceiptCutModeType receiptCutMode) {
        ReceiptCutMode = receiptCutMode;
    }
}
