/**
 * The source of the card number
 */
public enum PanSource {
    /**
     * Indicates the customer will be prompted to swipe, insert, or present their card.
     */
    Default(' '),
    /**
     * Indicates the POS has captured the Track2 from the customer card and it is stored in the PAN property
     */
    POSSwiped('S'),
    /**
     * Indicates the POS operator has keyed in the card number and it is stored in the PAN property
     */
    POSKeyed('K'),
    /**
     * Indicates the card number was captured from the internet and is stored in the PAN property
     */
    Internet('0'),
    /**
     * Indicates the card number was captured from a telephone order and it is stored in the PAN property
     */
    TeleOrder('1'),
    /**
     * Indicates the card number was captured from a mail order and it is stored in the PAN property
     */
    MailOrder('2'),
    /**
     * Indicates the POS operator has keyed in the card number and it is stored in the PAN property
     */
    CustomerPresent('3'),
    /**
     * Indicates the card number was captured for a recurring transaction and it is stored in the PAN property
     */
    RecurringTransaction('4'),
    /**
     * Indicates the card number was captured for an installment payment and it is stored in the PAN property
     */
    Installment('5');

    public char PanSource;
    PanSource(char c){
        PanSource = c;
    }

}
