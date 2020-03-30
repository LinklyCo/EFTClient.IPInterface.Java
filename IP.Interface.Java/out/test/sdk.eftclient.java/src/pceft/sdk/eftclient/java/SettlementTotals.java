package pceft.sdk.eftclient.java;

/**
 * EFTPOS settlement totals data
 */
public class SettlementTotals {

    public SettlementTotals() {
    }

    /**
     * <summary>Settlement totals description.</summary>
     */
    public String TotalsDescription = "";

    /**
     * <summary>Count of purchases transactions made.</summary>
     */
    public int PurchaseCount = 0;

    /**
     * <summary>Total of purchases transactions made.</summary>
     */
    public Double PurchaseAmount = 0.0d;

    /**
     * <summary>Count of cash out transactions made.</summary>
     */
    public int CashOutCount = 0;

    /**
     * <summary>Total of cash out transactions made.</summary>
     */
    public Double CashOutAmount = 0.0d;

    /**
     * <summary>Count of refund transactions made.</summary>
     */
    public int RefundCount = 0;

    /**
     * <summary>Total of refund transactions made.</summary>
     */
    public Double RefundAmount = 0.0d;

    /**
     * <summary>Count of all transactions made.</summary>
     */
    public int TotalCount = 0;

    /**
     * <summary>Total of all transactions made.</summary>
     */
    public Double TotalAmount = 0.0d;

    /**
     * <summary>Extra settlement totals data.</summary>
     */
    public String Extra = "";
}
