package pceft.sdk.eftclient.java;

public class EFTQueryCardRequest extends EFTRequest {
    public EFTQueryCardRequest() {
        super(true, EFTQueryCardResponse.class);
    }

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

    public enum SelectAccountType{
        NoAccountSelected('0'),

        SelectAccount_ReadCard('1'),

        GetAccountOnly('5');

        SelectAccountType(char c){
            sAccountType = c;
        }
        public char sAccountType;
    }

    public SelectAccountType AccountType = SelectAccountType.NoAccountSelected;
    public SelectAccountType getAccountType() {return AccountType;}
    public void setAccountType(SelectAccountType accountType) {
        AccountType = accountType;
    }

    public String Merchant = "00";
    public String getMerchant() {return Merchant;}
    public void setMerchant(String merchant) {
        Merchant = merchant;
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

}
