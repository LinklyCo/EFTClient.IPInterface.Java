package pceft.sdk.eftclient.java;
public class EFTChequeAuthRequest extends EFTRequest {
    public EFTChequeAuthRequest() {super(true, EFTChequeAuthResponse.class);}

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

    public String BranchCode = "012345";
    public String getBranchCode() {
        return BranchCode;
    }
    public void setBranchCode(String branchCode) {
        BranchCode = branchCode;
    }

    public String AccountNumber = "8887XXXZZZ7666";
    public String getAccountNumber() {
        return AccountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String SerialNumber = "0111CCCVVV2333";
    public String getSerialNumber() {
        return SerialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
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
     * Cheque Types
     */
    public enum ChequeAuthTypes{

        BusinessGuarantee('0'),

        PersonalGuarantee('1'),

        PersonalAppraisal('2');

        ChequeAuthTypes(char c) {
            chequeAuthType = c;
        }
        public char chequeAuthType;
    }

    public ChequeAuthTypes ChequeType = ChequeAuthTypes.BusinessGuarantee;
    public ChequeAuthTypes getChequeType() {
        return ChequeType;
    }
    public void setChequeType(ChequeAuthTypes chequeType) {
        ChequeType = chequeType;
    }

    public String TxnRef = "XXXXYYYYZZZZ";
    public String getTxnRef() {
        return TxnRef;
    }
    public void setTxnRef(String txnRef) {
        TxnRef = txnRef;
    }
}
