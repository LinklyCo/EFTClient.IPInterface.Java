package pceft.sdk.eftclient.java;

public class EFTBasketItem{
    public EFTBasketItem() {}
    public EFTBasketItem(String _ID) {id = _ID;}
    // region requiredValues
    /**
     * UNIQUE ID, MAX 32 LENGTH
     */
    public String id = "";
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String sku = "";
    public String getSku() {
        return sku;
    }
    public void setSku(String SKU) {
        this.sku = SKU;
    }

    /**
     * QUANTITY
     */
    public int qty = 0;
    public int getQty() {
        return qty;
    }
    public void setQty(int quantity) {
        qty = quantity;
    }

    public int amt = 0;
    public int getAmt() {
        return amt;
    }
    public void setAmt(int amount) {
        amt = amount;
    }

    // endregion

    // region optionalValues

    public int tax = 0;
    public int getTax() {
        return tax;
    }
    public void setTax(int tax) {
        this.tax = tax;
    }

    public int dis= 0;
    public int getDis() {
        return dis;
    }
    public void setDis(int discount) {
        dis = discount;
    }

    /**
     *  European Article Number
     */
    public String ean = "";
    public String getEan() {
        return ean;
    }
    public void setEan(String EAN) {
        this.ean = EAN;
    }

    /**
     * Universal Product Code
     */
    public String upc = "";
    public String getUpc() {
        return upc;
    }
    public void setUpc(String UPC) {
        this.upc = UPC;
    }

    /**
     * Global Trade Item Number
     */
    public String gtin = "";
    public String getGtin() {
        return gtin;
    }
    public void setGtin(String GTIN) {
        this.gtin = GTIN;
    }

    /**
     * SHORT NAME, MAX 24 LENGTH
     */
    public String name = "";
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DESCRIPTION OF ITEM, MAX 255 LENGTH
     */
    public String desc = "";
    public String getDesc() {
        return desc;
    }
    public void setDesc(String description) {
        desc = description;
    }

    public String srl = "";
    public String getSrl() {
        return srl;
    }
    public void setSrl(String SRL) {
        this.srl = SRL;
    }

    /**
     * IMAGE
     */
    public String img = "";
    public String getImg() {
        return img;
    }
    public void setImg(String url) {
        img = url;
    }

    public String link = "";
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * TAG. MAX 64 LENGTH
     */
    public String tag = "";
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    // endregion
}