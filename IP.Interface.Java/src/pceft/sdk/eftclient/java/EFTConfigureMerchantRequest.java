package pceft.sdk.eftclient.java;

public class EFTConfigureMerchantRequest extends EFTRequest {
    public EFTConfigureMerchantRequest() {
        super(true, EFTConfigureMerchantResponse.class);
    }

    public String Merchant = "00";
    public String getMerchant() {
        return Merchant;
    }
    public void setMerchant(String merchant) {
        Merchant = merchant;
    }

    public String AIIC = "ABCDEF01234";
    public String getAIIC() {
        return AIIC;
    }
    public void setAIIC(String AIIC) {
        this.AIIC = AIIC;
    }

    public String Nii = "XYZ";
    public String getNii() {
        return Nii;
    }
    public void setNii(String nii) {
        Nii = nii;
    }

    /**
     * 15 byte Long Identifier
     */
    public String Caid = "ABCDE0123456789";
    public String getCaid() {
        return Caid;
    }
    public void setCaid(String caid) {
        Caid = caid;
    }

    /**
     * 8 byte Long Identifier
     */
    public String Catid = "ABCD0123";
    public String getCatid() {
        return Catid;
    }
    public void setCatid(String catid) {
        Catid = catid;
    }

    /**
     * 000 - 255 (seconds)
     */
    public int Timeout = 0;
    public int getTimeout() {
        return Timeout;
    }
    public void setTimeout(int timeout) {
        Timeout = timeout;
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
}
