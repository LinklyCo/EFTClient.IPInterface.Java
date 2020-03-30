package pceft.sdk.eftclient.java;

/**
 * EFTPOS settlement card totals data
 */
public class SettlementCardTotals {

    public SettlementCardTotals() {

    }

    /**
     * <summary>Card type name.</summary>
     */
    public String CardName = "";

    /**
     * <summary>Count of purchases transactions made with this card type.</summary>
     */
    public int PurchaseCount = 0;

    /**
     * <summary>Total of purchases transactions made with this card type.</summary>
     */
    public Double PurchaseAmount = 0.0d;

    /**
     * <summary>Count of cash out transactions made with this card type.</summary>
     */
    public int CashOutCount = 0;

    /**
     * <summary>Total of cash out transactions made with this card type.</summary>
     */
    public Double CashOutAmount = 0.0d;

    /**
     * <summary>Count of refund transactions made with this card type.</summary>
     */
    public int RefundCount = 0;

    /**
     * <summary>Total of refund transactions made with this card type.</summary>
     */
    public Double RefundAmount = 0.0d;

    /**
     * <summary>Count of all transactions made with this card type.</summary>
     */
    public int TotalCount = 0;

    /**
     * <summary>Total of all transactions made with this card type.</summary>
     */
    public Double TotalAmount = 0.0d;
}
