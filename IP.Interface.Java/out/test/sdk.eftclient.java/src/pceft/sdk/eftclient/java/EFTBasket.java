package pceft.sdk.eftclient.java;

import java.util.ArrayList;

public class EFTBasket {
    /**
     * https://github.com/pceftpos/EFTClient.IPInterface.CSharp/blob/master/IPInterface/Model/EFTBasketData.cs
     */

    /**
     * Max Length 32
     */
    public String id = "";
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
     * ITEMS
     */
    public ArrayList<EFTBasketItem> items = new ArrayList<EFTBasketItem>();
    public ArrayList<EFTBasketItem> getItems() {
        return items;
    }
    public void setItems(ArrayList<EFTBasketItem> items) {
        this.items = items;
    }

    /**
     * AMOUNT
     */
    public int amt = 0;
    public int getAmt() {
        return amt;
    }
    public void setAmt(int amt) {
        this.amt = amt;
    }

    /**
     * TAX
     */
    public int tax = 0;
    public int getTax() {
        return tax;
    }
    public void setTax(int tax) {
        this.tax = tax;
    }


    /**
     * DISCOUNT
     */
    public int dis = 0;
    public int getDis() {
        return dis;
    }
    public void setDis(int discount) {
        dis = discount;
    }

    /**
     * SURCHARGE
     */
    public int sur = 0;
    public int getSur() {
        return sur;
    }
    public void setSur(int surcharge) {
        sur = surcharge;
    }
}
