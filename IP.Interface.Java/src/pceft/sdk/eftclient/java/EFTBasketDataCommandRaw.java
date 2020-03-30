package pceft.sdk.eftclient.java;

public class EFTBasketDataCommandRaw implements IEFTBasketDataCommand {
    public EFTBasketDataCommandRaw() {}
    public String BasketContent = "";
    public String getBasketContent() {
        return BasketContent;
    }
    public void setBasketContent(String basketContent) {
        BasketContent = basketContent;
    }
}
