package pceft.sdk.eftclient.java;

public class EFTBasketDataCommandCreate implements IEFTBasketDataCommand {
    public EFTBasketDataCommandCreate() {}
    public EFTBasket Basket = new EFTBasket();
    public EFTBasket getBasket() {
        return Basket;
    }
    public void setBasket(EFTBasket basket) {
        Basket = basket;
    }
}
