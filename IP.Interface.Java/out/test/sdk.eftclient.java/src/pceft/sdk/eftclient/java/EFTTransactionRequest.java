package pceft.sdk.eftclient.java;

import java.util.Date;

/**
 * A PC-EFTPOS transaction request object.
 */
public class EFTTransactionRequest extends EFTRequest{


    /**
     * PC-EFTPOS terminal applications
     */
    public enum TerminalApplication{
        EFTPOS("00"),
        Agency("01"),
        GiftCard("03"),
        Fuel("04"),
        Medicare("05"),
        Amex("06"),
        ChequeAuth("07"),
        Loyalty("02"),
        PrePaidCard("02"),
        ETS("02");

        TerminalApplication(String s){
            Application = s;
        }
        public String Application;
    }

    /**
     * EFTPOS transaction types
     */
    public enum TransactionType{
        /**
         * Transaction type was not set by the Pinpad (' ')
         */
        NotSet(' '),
        /**
         * A purchase with optional cash-out EFT transaction type ('P')
         */
        PurchaseCash('P'),
        /**
         * A cash-out only EFT transaction type ('C')
         */
        CashOut('C'),
        /**
         * A refund EFT transaction type ('R')
         */
        Refund('R'),
        /**
         * A pre-authorisation EFT transaction type ('A')
         */
        PreAuth('A'),
        /**
         * A pre-authorisation / completion EFT transaction type ('L')
         */
        PreAuthCompletion('L'),
        /**
         * A pre-authorisation / enquiry EFT transaction type ('N')
         */
        PreAuthEnquiry('N'),
        /**
         * A pre-authorisation / cancel EFT transaction type ('Q')
         */
        PreAuthCancel('Q'),
        /**
         * A completion EFT transaction type ('M')
         */
        Completion('M'),
        /**
         * A tip adjustment EFT transaction type ('T')
         */
        TipAdjust('T'),
        /**
         * A deposit EFT transaction type ('D')
         */
        Deposit('D'),
        /**
         * A withdrawal EFT transaction type ('W')
         */
        Withdrawal('W'),
        /**
         * A balance EFT transaction type ('B')
         */
        Balance('B'),
        /**
         * A voucher EFT transaction type ('V')
         */
        Voucher('V'),
        /**
         * A funds transfer EFT transaction type ('F')
         */
        FundsTransfer('F'),
        /**
         * An order request EFT transaction type ('O')
         */
        OrderRequest('O'),
        /**
         * A mini transaction history EFT transaction type ('H')
         */
        MiniTransactionHistory('H'),
        /**
         * An auth pin EFT transaction type ('X')
         */
        AuthPIN('X'),
        /**
         * An enhanced pin EFT transaction type ('K')
         */
        EnhancedPIN('K'),
        None('0');

        TransactionType(char c){
            TxnType =c;
        }
        public char TxnType;
    }

    /**
     * Supported EFTPOS account types
     */
    public enum AccountType{
        /**
         * The default account type for a card.
         */
        Default(' '),
        /**
         * The cheque account
         */
        Cheque('1'),
        /**
         * The credit account
         */
        Credit('2'),
        /**
         * The savings account
         */
        Savings('3');

        AccountType(char c){
            AccountType = c;
        }
        public char AccountType;

    }

    /**
     * The card entry method of the transaction
     */
    public enum CardEntryType{
        /**
         * Manual entry type was not set by the Pinpad.
         */
        NotSet(' '),
        /**
         * Unknown manual entry type. Pinpad may not support this flag.
         */
        Unknown('0'),
        /**
         * Card was swiped.
         */
        Swiped('S'),
        /**
         * Card number was keyed.
         */
        Keyed('K'),
        /**
         * Card number was read by a barcode scanner.
         */
        Barcode('B'),
        /**
         * Card number was read from a chip card.
         */
        ChipCard('E'),
        /**
         * Card number was read from a contactless reader.
         */
        Contactless('C');
        public char EntryType;

        CardEntryType(char c){
            EntryType = c;
        }
    }

    /**
     * The communications method used to process the transaction.
     */
    public enum CommsMethodType{
        /**
         * Comms methos type was not set by the Pinpad.
         */
        NotSet(' '),
        /**
         * Transaction was sent to the bank using an unknown method.
         */
        Unknown('0'),
        /**
         * Transaction was sent to the bank using a P66 modem.
         */
        P66('1'),
        /**
         * Transaction was sent to the bank using an Argent.
         */
        Argent('2'),
        /**
         * Transaction was sent to the bank using an X25.
         */
        X25('3');
        public char CommsMethod;
        CommsMethodType(char c){
            CommsMethod = c;
        }
    }

    /**
     * The currency conversion status for the transaction.
     */
    public enum CurrencyStatus{
        /**
         * Currency conversion status was not set by the Pinpad.
         */
        NotSet(' '),
        /**
         * Transaction amount was processed in Australian Dollarydoos
         */
        AUD('0'),
        /**
         * Transaction amount was currency converted.
         */
        Converted('1');
        public char CurrencyStat;
        CurrencyStatus(char c){
            CurrencyStat = c;
        }
    }

    /**
     * The Pay Pass status of the transaction.
     */
    public enum PayPassStatus{
        /**
         * Pay Pass conversion status was not set by the Pinpad.
         */
        NotSet(' '),
        /**
         * Pay Pass was used in the transaction.
         */
        PayPassUsed('1'),
        /**
         * Pay Pass was not used in the transaction.
         */
        PayPassNotUsed('0');
        public char PayPassStat;
        PayPassStatus(char c){
            PayPassStat = c;
        }
    }



    /**
     * Constructs a default EFTTransactionRequest object
     */
    public EFTTransactionRequest() {
        super(true, EFTTransactionResponse.class);
    }

    /**
     * The type of transaction to perform.
     * @value The default is TransactionType.PurchaseCash
     */
    public TransactionType TxnType = TransactionType.PurchaseCash;
    public TransactionType getTxnType() {
        return TxnType;
    }
    public void setTxnType(TransactionType txnType) {
        TxnType = txnType;
    }

    /**
     * Two digit merchant code
     * @value The default is "00"
     */
    public String Merchant = "00";
    public String getMerchant() {
        return Merchant;
    }
    public void setMerchant(String merchant) {
        Merchant = merchant;
    }

    /**
     * The currency code for this transaction
     * @value A 3 digit ISO currency code. The default is "   "
     */
    public String CurrencyCode = "   ";
    public String getCurrencyCode() {
        return CurrencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    /**
     * The original type of transaction for voucher entry.
     * @value The default is TransactionType.PurchaseCash
     */
    public TransactionType OriginalTxnType = TransactionType.PurchaseCash;
    public TransactionType getOriginalTxnType() {
        return OriginalTxnType;
    }
    public void setOriginalTxnType(TransactionType originalTxnType) {
        OriginalTxnType = originalTxnType;
    }

    /**
     * Date used for voucher or completion only
     * @value The default is null
     */
    public Date BankDate = null;
    public Date getBankDate() {
        return BankDate;
    }
    public void setBankDate(Date bankDate) {
        BankDate = bankDate;

    }

    /**
     * Time used for voucher entry or completion only
     * @value Default is 0
     */
    public long Time = 0;
    public long getTime() {
        return BankDate.getTime();
    }
    public void setTime(long time) {
        BankDate.setTime(time);
    }

    /**
     * Determines if the transaction is a training mode transaction
     * @value Set to TRUE if the transaction is to be performed in training mode. The default is FALSE.
     */
    public boolean TrainingMode = false;
    public boolean isTrainingMode() {
        return TrainingMode;
    }
    public void setTrainingMode(boolean trainingMode) {
        TrainingMode = trainingMode;
    }

    /**
     * Indicates if the transaction should be tippable
     * @value Set to TRUE if tipping is to be enabled for this transaction. The default is FALSE
     */
    public boolean EnableTip = false;
    public boolean isEnableTip() {
        return EnableTip;
    }
    public void setEnableTip(boolean enableTip) {
        EnableTip = enableTip;
    }

    /**
     * The cash amount for the transaction.
     * @value Type: Double. The default is 0
     */
    public double AmtCash = 0;
    public double getAmtCash() {
        return AmtCash;
    }
    public void setAmtCash(double amtCash) {
        AmtCash = amtCash;
    }

    /**
     * The purchase amount for the transaction.
     * @value Type: Double. The default is 0.
     */
    public double AmtPurchase = 0;
    public double getAmtPurchase() {
        return AmtPurchase;
    }
    public void setAmtPurchase(double amtPurchase) {
        AmtPurchase = amtPurchase;
    }

    /**
     * The authorisation number for the transaction.
     *
     *
     * @value Type: int
     */
    public int AuthCode = 0;
    public int getAuthCode(){
        return AuthCode;
    }
    public void setAuthCode(int authCode) {
        AuthCode = authCode;
    }

    /**
     * The reference number to attack to the transaction. This will appear on the receipt
     * @value Typ: String. This property is optional but it usually is populated by a unique transaction identifier that can be used for retrievals
     */
    public String TxnRef = "";
    public String getTxnRef() {
        return TxnRef;
    }
    public void setTxnRef(String txnRef) {
        TxnRef = txnRef;
    }

    /**
     * Indicates the source of the card number
     * @value Type: PanSource. The default is PanSource.Default
     * Use this property for card not present transactions
     */
    public PanSource PanSrc = PanSource.Default;
    public PanSource getPanSource() {
        return PanSrc;
    }
    public void setPanSource(PanSource panSrc) {
        PanSrc = panSrc;
    }

    /**
     * The card number to use when pan source of POS keyed is used
     * @value Type: String.
     * Use this property in conjunction with PanSource
     */
    public String Pan = "";
    public String getPan() {
        return Pan;
    }
    public void setPan(String pan) {
        Pan = pan;
    }

    /**
     * The expiry date of the card when pan source of POS keyed is used.
     * @valueType: String In MMYY format
     * Use this property in conjunction with PanSource when passing the card expiry date to PC-EFTPOS
     */
    public String DateExpiry = "";
    public String getDateExpiry() {
        return DateExpiry;
    }
    public void setDateExpiry(String dateExpiry) {
        DateExpiry = dateExpiry;
    }

    /**
     * The track 2 to use when pan source of POS swiped is used.
     * @value Type: String.
     * Use this property when PanSource is set to POSSwiped and passing the full Track2 from the card magnetic stripe to PC-EFTPOS
     */
    public String Track2 = "";
    public String getTrack2() {
        return Track2;
    }
    public void setTrack2(String track2) {
        Track2 = track2;
    }

    /**
     * The account to use for this transaction
     * @value Type: AccountType. Default is AccountType.Default.
     * Use default to prompt user to enter the account type.
     * Use this property in conjunction with PanSource when passing the account type to PC-EFTPOS
     */
    public AccountType CardAccountType = AccountType.Default;
    public AccountType getCardAccountType() {
        return CardAccountType;
    }
    public void setCardAccountType(AccountType cardAccountType) {
        CardAccountType = cardAccountType;
    }

    /**
     * The retrieval reference number for the transaction
     * @value Type: String.
     * This property is required for a TransactionType.TipAdjust transaction type
     */
    public String RRN = "";
    public String getRRN() {
        return RRN;
    }
    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    /**
     * Additional information sent with the request.
     * @value Type: String
     */
    public String PurchaseAnalysisData = "";
    public String getPurchaseAnalysisData() {
        return PurchaseAnalysisData;
    }
    public void setPurchaseAnalysisData(String purchaseAnalysisData) {
        PurchaseAnalysisData = purchaseAnalysisData;
    }

    /**
     * Indicates where the request is to be sent to. Should normally be EFTPOS.
     * @value Type TerminalApplication. The default is TerminalApplication.EFTPOS
     */
    public TerminalApplication Application = TerminalApplication.EFTPOS;
    public TerminalApplication getApplication() {
        return Application;
    }
    public void setApplication(TerminalApplication application) {
        Application = application;
    }

    /**
     * Indicates whether to trigger receipt events.
     * @value Type: ReceiptPrintModeType. The default is POSPrinter
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

    /**
     * The CVV (sometimes called the CCV) of the card.
     */
    public int CVV = 0;
    public int getCVV() {
        return CVV;
    }
    public void setCVV(int CVV) {
        this.CVV = CVV;
    }
}
