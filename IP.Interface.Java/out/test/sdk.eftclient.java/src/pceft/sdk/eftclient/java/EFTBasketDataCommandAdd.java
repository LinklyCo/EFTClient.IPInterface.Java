package pceft.sdk.eftclient.java;

public class EFTBasketDataCommandAdd implements IEFTBasketDataCommand {
    public EFTBasketDataCommandAdd() {}
    public EFTBasket Basket;
    public EFTBasket getBasket() {
        return Basket;
    }
    public void setBasket(EFTBasket basket) {
        Basket = basket;
    }
}
