/**
 * A PC-EFTPOS set dialog request object
 */
public class EFTSetDialogRequest extends EFTRequest {
    /**
     * The style of PC-EFTPOS dialog
     */
    enum DialogType {
        /**
         * The standard PC-EFTPOS dialog
         */
        Standard('0'),
        /**
         * The PC-EFTPOS dialog for touch screens. Has larger buttons
         */
        TouchScreen('1'),
        /**
         * Do not show the PC-EFTPOS dialogs
         */
        Hidden('2');

        public char Type;

        DialogType(char c) {
            Type = c;
        }
    }

    /**
     * The position of the PC-EFTPOS dialog. Currently not supported
     */
    enum DialogPosition {
        TopLeft,
        TopCentre,
        TopRight,
        MiddleLeft,
        Centre,
        MiddleRight,
        BottomLeft,
        BottomCentre,
        BottomRight
    }

    /**
     * Constructs a default set dialog request object
     */
    EFTSetDialogRequest() {
        super(true, EFTSetDialogResponse.class);
    }

    /**
     * Indicates the type of PC-EFTPOS dialog to use. The default is Standard.
     */
    DialogType dialogType = DialogType.Standard;

    public DialogType getDialogType() {
        return dialogType;
    }

    /**
     * Indicates the X position of the PC-EFTPOS dialog
     */
    int DialogX = 0;

    public int getDialogX() {
        return DialogX;
    }

    /**
     * Indicates the Y position of the PC-EFTPOS dialog
     */
    int DialogY = 0;

    public int getDialogY() {
        return DialogY;
    }

    /**
     * Indicates the position of the PC-EFTPOS dialog
     */
    DialogPosition dialogPosition = DialogPosition.Centre;

    public DialogPosition getDialogPosition() {
        return dialogPosition;
    }

    /**
     * Indicates if the PC-EFTPOS dialog is to be on top of all windows.
     */
    boolean EnableTopmost = true;

    public boolean isEnableTopmost() {
        return EnableTopmost;
    }

    /**
     * Set the title of the PC-EFTPOS dialog
     */
    String DialogTitle = "";

    public String getDialogTitle() {
        return DialogTitle;
    }

    /**
     * Disable all future display events to the POS
     */
    boolean DisableDisplayEvents = false;

    public boolean isDisableDisplayEvents() {
        return DisableDisplayEvents;
    }
}
