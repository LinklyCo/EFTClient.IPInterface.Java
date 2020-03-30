package pceft.sdk.eftclient.java;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class EFTPinResponse extends EFTResponse {
    public EFTPinResponse() {super(EFTPinRequest.class);}

    public boolean SuccessFlag = false;
    public boolean isSuccessFlag() {
        return SuccessFlag;
    }
    public void setSuccessFlag(boolean successFlag) {
        SuccessFlag = successFlag;
    }
    public void setSuccessFlag(char successFlag) {
        SuccessFlag = (successFlag == '1');
    }

    public String ResponseCode = "99";
    public String getResponseCode() {
        return ResponseCode;
    }
    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String ResponseText = "";
    public String getResponseText() {
        return ResponseText;
    }
    public void setResponseText(String responseText) {
        ResponseText = responseText;
    }

    public String Merchant  = "00";
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

    public String AccountType = "";
    public String getAccountType() {
        return AccountType;
    }
    public void setAccountType(String accountType) {
        AccountType = accountType;
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

    public double AmountTip = 0.00f;
    public double getAmountTip() {
        return AmountTip;
    }
    public void setAmountTip(double amountTip) {
        AmountTip = amountTip;
    }

    public String AuthCode = "";
    public String getAuthCode() {
        return AuthCode;
    }
    public void setAuthCode(String authCode) {
        AuthCode = authCode;
    }

    public String TxnRef = "";
    public String getTxnRef() {
        return TxnRef;
    }
    public void setTxnRef(String txnRef) {
        TxnRef = txnRef;
    }

    public String Stan = "";
    public String getStan() {
        return Stan;
    }
    public void setStan(String stan) {
        Stan = stan;
    }

    public String Caid = "";
    public String getCaid() {
        return Caid;
    }
    public void setCaid(String caid) {
        Caid = caid;
    }

    public String Catid = "";
    public String getCatid() {
        return Catid;
    }
    public void setCatid(String catid) {
        Catid = catid;
    }

    public String DateExpiry = "";
    public String getDateExpiry() {
        return DateExpiry;
    }
    public void setDateExpiry(String dateExpiry) {
        DateExpiry = dateExpiry;
    }

    public String DateSettlement = "";
    public String getDateSettlement() {
        return DateSettlement;
    }
    public void setDateSettlement(String dateSettlement) {
        DateSettlement = dateSettlement;
    }

    public Date BankDate = Date.from(Instant.now());
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

    public EFTTransactionRequest.AccountType setCardAccountType(String msg){
        if(msg.toUpperCase().trim().equals("CREDIT")){
            return EFTTransactionRequest.AccountType.Credit;
        }
        else if(msg.toUpperCase().trim().equals("SAVINGS")){
            return EFTTransactionRequest.AccountType.Savings;
        }
        else if(msg.toUpperCase().trim().equals("CHEQUE")){
            return EFTTransactionRequest.AccountType.Cheque;
        }
        else
            return EFTTransactionRequest.AccountType.Default;
    }

    public String PAN = "";
    public String getPAN() {
        return PAN;
    }
    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    public String Track2 = "";
    public String getTrack2() {
        return Track2;
    }
    public void setTrack2(String track2) {
        Track2 = track2;
    }

    public String RRN = "";
    public String getRRN() {
        return RRN;
    }
    public void setRRN(String RRN) {
        this.RRN = RRN;
    }
}
