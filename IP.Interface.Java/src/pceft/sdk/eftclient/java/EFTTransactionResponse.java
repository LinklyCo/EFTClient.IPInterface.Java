package pceft.sdk.eftclient.java;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A PC-EFTPOS terminal transaction response object.
 */
public class EFTTransactionResponse extends EFTResponse {
    /**
     * Constructs a default terminal transaction response object
     */
    public EFTTransactionResponse(){
        super(EFTTransactionRequest.class);
    }

    /**
     * the type of transaction to perform
     * @value Type: TransactionType. The default is TransactionType.PurchaseCash
     */
    public EFTTransactionRequest.TransactionType TxnType = EFTTransactionRequest.TransactionType.PurchaseCash;
    public EFTTransactionRequest.TransactionType getTxnType() {
        return TxnType;
    }
    public void setTxnType(EFTTransactionRequest.TransactionType txnType) {
        TxnType = txnType;
    }

    /**
     * Two digit merchant code
     * @value Type: String. The default is "00"
     */
    public String Merchant = "00";
    public String getMerchant() {
        return Merchant;
    }
    public void setMerchant(String merchant) {
        Merchant = merchant;
    }

    /**
     * Indicates the card type that was used in the transaction.
     */
    public String CardType = "";
    public String getCardType() {
        return CardType;
    }
    public void setCardType(String cardType) {
        CardType = cardType;
    }

    /**
     * Indicates the card type that was used in the transaction.
     */
    public int CardName = 0;
    public int getCardName() {
        return CardName;
    }
    public void setCardName(int cardName) {
        CardName = cardName;
    }

    /**
     * Used to retrieve the transaction from the batch.
     * @value Type: String
     * The retrieval reference number is used when performing a tip adjustment transaction
     */
    public String RRN = "";
    public String getRRN() {
        return RRN;
    }
    public void setRRN(String RRN) {
        this.RRN = RRN;
    }

    /**
     * Indicates which settlement batch this transaction will be included in.
     * @value Type: Date. Settlement date is returned from the bank.
     * Use this property to balance POS EFT totals with settlement EFT totals.
     */
    public Date SettlementDate = Calendar.getInstance().getTime();
    public Date getSettlementDate() {
        return SettlementDate;
    }
    public void setSettlementDate(Date settlementDate) {
        SettlementDate = settlementDate;
    }
    public Date setSettlementDate(String msg){
        DateFormat df = new SimpleDateFormat("ddMM");
        Date settlementDate = null;
        try{
            settlementDate = df.parse(msg);
        }
        catch (ParseException ex){

        }
        return settlementDate;
    }

    /**
     * The cash amount for the transaction.
     * @value Type: Double. The default is 0
     * This property is mandatory for a Transaction.CashOut transaction type
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
     * @value Type: Double. The default is 0
     * This property is mandatory for all but TransactionType.CashOut transaction types
     */
    public double AmtPurchase = 0;
    public double getAmtPurchase() {
        return AmtPurchase;
    }
    public void setAmtPurchase(double amtPurchase) {
        AmtPurchase = amtPurchase;
    }

    /**
     * The tip amount for the transaction
     * @value Type: Double. Echoed from the request
     */
    public double AmtTip = 0;
    public double getAmtTip() {
        return AmtTip;
    }
    public void setAmtTip(double amtTip) {
        AmtTip = amtTip;
    }

    /**
     * The authorisation number for the transaction
     */
    public int AuthCode = 0;
    public int getAuthCode() {
        return AuthCode;
    }
    public void setAuthCode(int authCode) {
        AuthCode = authCode;
    }

    /**
     * The reference number to attach to the transaction. This will appear on the receipt.
     * @value Type: String
     * This property is optional but it uis usually populated by a unique transaction identifier that can be used for retrieval
     */
    public String TxnRef = "";
    public String getTxnRef() {
        return TxnRef;
    }
    public void setTxnRef(String txnRef) {
        TxnRef = txnRef;
    }

    /**
     * The card number to use when pan source of POS keyed is used.
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
     * @value type: String in MMYY format
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
     * The track 2 data on the magnetic stripe of the card
     * @value Type: String.
     * This property contains the partial track 2 data from the card used in this transaction.
     */
    public String Track2 = "";
    public String getTrack2() {
        return Track2;
    }
    public void setTrack2(String track2) {
        Track2 = track2;
    }

    /**
     * The account used for the transaction.
     * @value Type: AccountType.
     * This is the account type selected by the customer or provided in the request
     */
    public EFTTransactionRequest.AccountType CardAccountType = EFTTransactionRequest.AccountType.Default;
    public EFTTransactionRequest.AccountType getCardAccountType() {
        return CardAccountType;
    }
    public void setCardAccountType(EFTTransactionRequest.AccountType cardAccountType) {
        CardAccountType = cardAccountType;
    }

    /**
     * Flags that indicate how the transaction was processed.
     * @value Type: TxnFlags
     */
    public TxnFlags TxnFlags = new TxnFlags();
    public TxnFlags getTxnFlags() {
        return TxnFlags;
    }
    public void setTxnFlags(TxnFlags txnFlags) {
        TxnFlags = txnFlags;
    }

    /**
     * Indicates if an available balance is present in the response.
     * @value Type: boolean
     */
    public boolean BalanceReceived = false;
    public boolean isBalanceReceived() {
        return BalanceReceived;
    }
    public void setBalanceReceived(boolean balanceReceived) {
        BalanceReceived = balanceReceived;
    }

    /**
     * Balance available on the processed account.
     * @value type: double.
     */
    public double AvailableBalance = 0;
    public double getAvailableBalance() {
        return AvailableBalance;
    }
    public void setAvailableBalance(double availableBalance) {
        AvailableBalance = availableBalance;
    }

    /**
     * Cleared balance on the processed account.
     * @value Type: double
     */
    public double ClearedFundsBalance = 0;
    public double getClearedFundsBalance() {
        return ClearedFundsBalance;
    }
    public void setClearedFundsBalance(double clearedFundsBalance) {
        ClearedFundsBalance = clearedFundsBalance;
    }

    /**
     * Indicates if the request was successful.
     * @value Type: boolean
     */
    public boolean Success = false;

    public boolean isSuccess() {
        return Success;
    }
    public void setSuccess(boolean success) {
        Success = success;
    }
    public void setSuccess(char c){
        if(c == '1')
            Success = true;
        else if(c == '0')
            Success = false;
    }

    /**
     * The response code of the request
     * @value Type: String.
     * A 2 character response code. "00" indicates a successful response
     */
    public String ResponseCode = "";
    public String getResponseCode() {
        return ResponseCode;
    }
    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    /**
     * The response text for the response code.
     * @value Type: String.
     */
    public String ResponseText = "";
    public String getResponseText() {
        return ResponseText;
    }
    public void setResponseText(String responseText) {
        ResponseText = responseText;
    }

    /**
     * Date and time of the response returned by the bank.
     * @value Type: Date
     */
    public Date BankDate = Calendar.getInstance().getTime();
    public Date getBankDate() {
        return BankDate;
    }
    public void setBankDate(Date bankDate) {
        BankDate = bankDate;
    }
    public Date setBankDate(String msg){
        Date BankDate = null;
        DateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
        try {
            BankDate = df.parse(msg);
        }catch(ParseException ex){

        }
        return BankDate;
    }

    /**
     * Terminal ID configured in the Pinpad.
     * @value Type: String
     */
    public String Catid = "";
    public String getCatid() {
        return Catid;
    }
    public void setCatid(String catid) {
        Catid = catid;
    }

    /**
     * Merchant ID configured int he Pinpad.
     * @value Type: String.
     */
    public String Caid = "";
    public String getCaid() {
        return Caid;
    }
    public void setCaid(String caid) {
        Caid = caid;
    }

    /**
     * System Trace Audit Number
     * @value Type: int.
     */
    public int Stan = 0;
    public int getStan() {
        return Stan;
    }
    public void setStan(int stan) {
        Stan = stan;
    }

    /**
     * Additional information sent with the response
     * @value Type: String.
     */
    public String PurchaseAnalysisData = "";
    public String getPurchaseAnalysisData() {
        return PurchaseAnalysisData;
    }
    public void setPurchaseAnalysisData(String purchaseAnalysisData) {
        PurchaseAnalysisData = purchaseAnalysisData;
    }
}
