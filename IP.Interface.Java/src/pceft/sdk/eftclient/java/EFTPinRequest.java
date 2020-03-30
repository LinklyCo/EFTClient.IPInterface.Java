package pceft.sdk.eftclient.java;

public class EFTPinRequest extends EFTRequest {
    public EFTPinRequest() {super (true, EFTPinResponse.class);}

    public String Merchant = "00";
    public String getMerchant() {
        return Merchant;
    }
    public void setMerchant(String merchant) {
        Merchant = merchant;
    }

    public enum ApplicationTypes{
        Auth('X'),

        PinChange('K');

        public char Application;
        ApplicationTypes(char c) {Application = c;}
    }

    public ApplicationTypes TxnType = ApplicationTypes.Auth;
    public ApplicationTypes getTxnType() {
        return TxnType;
    }
    public void setTxnType(ApplicationTypes txnType) {
        TxnType = txnType;
    }

    public boolean TrainingMode = false;
    public boolean isTrainingMode() {
        return TrainingMode;
    }
    public void setTrainingMode(boolean trainingMode) {
        TrainingMode = trainingMode;
    }

    public boolean EnableTipping = false;
    public boolean isEnableTipping() {
        return EnableTipping;
    }
    public void setEnableTipping(boolean enableTipping) {
        EnableTipping = enableTipping;
    }

    public double AmountCash = 0.00f;
    public double getAmountCash() {
        return AmountCash;
    }
    public void setAmountCash(double amountCash) {
        AmountCash = amountCash;
    }

    public double AmountPurchase = 0.00f;
    public double getAmountPurchase() {
        return AmountPurchase;
    }
    public void setAmountPurchase(double amountPurchase) {
        AmountPurchase = amountPurchase;
    }

    public String AuthCode = "XXXZZZ";
    public String getAuthCode() {
        return AuthCode;
    }
    public void setAuthCode(String authCode) {
        AuthCode = authCode;
    }

    public ReceiptPrintModeType ReceiptAutoPrint = ReceiptPrintModeType.POSPrinter;
    public ReceiptPrintModeType getReceiptAutoPrint() {
        return ReceiptAutoPrint;
    }
    public void setReceiptAutoPrint(ReceiptPrintModeType receiptAutoPrint) {
        ReceiptAutoPrint = receiptAutoPrint;
    }

    public ReceiptCutModeType CutReceipt = ReceiptCutModeType.DontCut;
    public ReceiptCutModeType getCutReceipt() {
        return CutReceipt;
    }
    public void setCutReceipt(ReceiptCutModeType cutReceipt) {
        CutReceipt = cutReceipt;
    }

    public char PanSource = 'K';
    public char getPanSource() {
        return PanSource;
    }
    public void setPanSource(char panSource) {
        PanSource = panSource;
    }

    public String Pan = "1111AAAAXXXXFFFF7777";
    public String getPan() {
        return Pan;
    }
    public void setPan(String pan) {
        Pan = pan;
    }

    public String DateExpiry = "";
    public String getDateExpiry() {
        return DateExpiry;
    }
    public void setDateExpiry(String dateExpiry) {
        DateExpiry = dateExpiry;
    }

    public String Track2 = "";
    public String getTrack2() {
        return Track2;
    }
    public void setTrack2(String track2) {
        Track2 = track2;
    }

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

    public AccountType CardAccountType = AccountType.Default;
    public AccountType getCardAccountType() {
        return CardAccountType;
    }
    public void setCardAccountType(AccountType cardAccountType) {
        CardAccountType = cardAccountType;
    }

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
    public TerminalApplication Application = TerminalApplication.EFTPOS;
    public TerminalApplication getApplication() {
        return Application;
    }
    public void setApplication(TerminalApplication application) {
        Application = application;
    }

    public String RRN = "";
    public String getRRN() {
        return RRN;
    }
    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    public String CardDataField = "";
    public String getCardDataField() {
        return CardDataField;
    }
    public void setCardDataField(String cardDataField) {
        CardDataField = cardDataField;
    }
}
