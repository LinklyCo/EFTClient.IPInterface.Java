package pceft.sdk.eftclient.java;

/**
 * Flags that indicate how the transaction was processed
 */
public class TxnFlags{
    char[] flags;

    /**
     * Constructs a TxnFlags object with default values
     */
    public TxnFlags(){}
    /**
     * Constructs a TxnFlags object
     */
    public TxnFlags(char[] flags){
        this.flags = new char[]{' ',' ',' ',' ',' ',' ',' ',' '};
        System.arraycopy(flags, 0,this.flags,0, (flags.length >8) ? 8 : flags.length);

        Offline = this.flags[0] == '1';
        ReceiptPrinted = this.flags[1] =='1';
        CardEntry = ParseCardEntryFlag(this.flags[2]);
        CommsMethod = ParseCommsMethodFlag(this.flags[3]);
        Currency = ParseCurrencyFlag(this.flags[4]);
        PayPass = ParsePayPassFlag(this.flags[5]);
        UndefinedFlag6 = this.flags[6];
        UndefinedFlag7 = this.flags[7];
    }

    /**
     * Indicates if a receipt was printed for this transaction
     * @value Set to TRUE if a receipt was printed
     */
    public boolean ReceiptPrinted = false;
    public boolean isReceiptPrinted() {
        return ReceiptPrinted;
    }
    public void setReceiptPrinted(boolean receiptPrinted) {
        ReceiptPrinted = receiptPrinted;
    }

    /**
     * Indicates if the transaction was approved offline
     * @value Set to TRUE if the transaction was approved offline
     */
    public boolean Offline = false;
    public boolean isOffline() {
        return Offline;
    }
    public void setOffline(boolean offline) {
        Offline = offline;
    }

    /**
     * Indicates the card entry type
     */
    public EFTTransactionRequest.CardEntryType CardEntry = EFTTransactionRequest.CardEntryType.NotSet;
    public EFTTransactionRequest.CardEntryType getCardEntry() {
        return CardEntry;
    }
    public void setCardEntry(EFTTransactionRequest.CardEntryType cardEntry) {
        CardEntry = cardEntry;
    }

    /**
     * Indicates the communications method used for this transaction
     */
    public EFTTransactionRequest.CommsMethodType CommsMethod = EFTTransactionRequest.CommsMethodType.NotSet;
    public EFTTransactionRequest.CommsMethodType getCommsMethod() {
        return CommsMethod;
    }
    public void setCommsMethod(EFTTransactionRequest.CommsMethodType commsMethod) {
        CommsMethod = commsMethod;
    }

    /**
     * Indicates the currency conversion status for this transaction.
     */
    public EFTTransactionRequest.CurrencyStatus Currency = EFTTransactionRequest.CurrencyStatus.NotSet;
    public EFTTransactionRequest.CurrencyStatus getCurrency() {
        return Currency;
    }
    public void setCurrency(EFTTransactionRequest.CurrencyStatus currency) {
        Currency = currency;
    }

    /**
     * Indicates the Pay Pass status for this transaction
     */
    public EFTTransactionRequest.PayPassStatus PayPass = EFTTransactionRequest.PayPassStatus.NotSet;
    public EFTTransactionRequest.PayPassStatus getPayPass() {
        return PayPass;
    }
    public void setPayPass(EFTTransactionRequest.PayPassStatus payPass) {
        PayPass = payPass;
    }

    /**
     * Undefined flag 6
     */
    public char UndefinedFlag6 = ' ';

    public char getUndefinedFlag6() {
        return UndefinedFlag6;
    }

    public void setUndefinedFlag6(char undefinedFlag6) {
        UndefinedFlag6 = undefinedFlag6;
    }

    /**
     * Undefined flag 7
     */
    public char UndefinedFlag7 = ' ';
    public char getUndefinedFlag7() {
        return UndefinedFlag7;
    }
    public void setUndefinedFlag7(char undefinedFlag7) {
        UndefinedFlag7 = undefinedFlag7;
    }

    //region EnumBullshit
    private EFTTransactionRequest.CardEntryType ParseCardEntryFlag(char c){
        switch (c){
            case ' ':
                return EFTTransactionRequest.CardEntryType.NotSet;
            case '0':
                return EFTTransactionRequest.CardEntryType.Unknown;
            case 'S':
                return EFTTransactionRequest.CardEntryType.Swiped;
            case 'K':
                return EFTTransactionRequest.CardEntryType.Keyed;
            case 'B':
                return EFTTransactionRequest.CardEntryType.Barcode;
            case 'E':
                return EFTTransactionRequest.CardEntryType.ChipCard;
            case 'C':
                return EFTTransactionRequest.CardEntryType.Contactless;
            default:
                return EFTTransactionRequest.CardEntryType.NotSet;
        }
    }

    private EFTTransactionRequest.CommsMethodType ParseCommsMethodFlag(char c){
        switch (c){
            case ' ':
            default:
                return EFTTransactionRequest.CommsMethodType.NotSet;
            case '0':
                return EFTTransactionRequest.CommsMethodType.Unknown;
            case '1':
                return EFTTransactionRequest.CommsMethodType.P66;
            case '2':
                return EFTTransactionRequest.CommsMethodType.Argent;
            case '3':
                return EFTTransactionRequest.CommsMethodType.X25;
        }
    }

    private EFTTransactionRequest.CurrencyStatus ParseCurrencyFlag(char c){
        switch (c){
            case ' ':
            default:
                return EFTTransactionRequest.CurrencyStatus.NotSet;
            case '0':
                return EFTTransactionRequest.CurrencyStatus.AUD;
            case '1':
                return EFTTransactionRequest.CurrencyStatus.Converted;
        }
    }

    private EFTTransactionRequest.PayPassStatus ParsePayPassFlag(char c){
        switch (c){
            case ' ':
            default:
                return EFTTransactionRequest.PayPassStatus.NotSet;
            case '1':
                return EFTTransactionRequest.PayPassStatus.PayPassUsed;
            case '0':
                return EFTTransactionRequest.PayPassStatus.PayPassNotUsed;
        }
    }

    //endregion

}