package pceft.sdk.eftclient.java;

public class EFTBasketDataCommandDelete implements IEFTBasketDataCommand {
    public EFTBasketDataCommandDelete() {}
    public String BasketId = "";
    public String getBasketId() {
        return BasketId;
    }
    public void setBasketId(String basketId) {
        BasketId = basketId;
    }

    public String BasketItemId = "";
    public String getBasketItemId() {
        return BasketItemId;
    }
    public void setBasketItemId(String basketItemId) {
        BasketItemId = basketItemId;
    }
}
