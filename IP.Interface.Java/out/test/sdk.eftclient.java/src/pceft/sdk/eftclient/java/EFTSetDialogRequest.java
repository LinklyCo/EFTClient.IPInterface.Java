package pceft.sdk.eftclient.java;

/**
 * A PC-EFTPOS set dialog request object
 */
public class EFTSetDialogRequest extends EFTRequest {
    /**
     * The style of PC-EFTPOS dialog
     */
    public enum DialogType {
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
    public enum DialogPosition {
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
    public EFTSetDialogRequest() {
        super(true, EFTSetDialogResponse.class);
    }

    /**
     * Indicates the type of PC-EFTPOS dialog to use. The default is Standard.
     */
    public DialogType dialogType = DialogType.Standard;

    public DialogType getDialogType() {
        return dialogType;
    }

    /**
     * Indicates the X position of the PC-EFTPOS dialog
     */
    public int DialogX = 0;

    public int getDialogX() {
        return DialogX;
    }

    /**
     * Indicates the Y position of the PC-EFTPOS dialog
     */
    public int DialogY = 0;

    public int getDialogY() {
        return DialogY;
    }

    /**
     * Indicates the position of the PC-EFTPOS dialog
     */
    public DialogPosition dialogPosition = DialogPosition.Centre;

    public DialogPosition getDialogPosition() {
        return dialogPosition;
    }

    /**
     * Indicates if the PC-EFTPOS dialog is to be on top of all windows.
     */
    public boolean EnableTopmost = true;

    public boolean isEnableTopmost() {
        return EnableTopmost;
    }

    /**
     * Set the title of the PC-EFTPOS dialog
     */
    public String DialogTitle = "";

    public String getDialogTitle() {
        return DialogTitle;
    }

    /**
     * Disable all future display events to the POS
     */
    public boolean DisableDisplayEvents = false;

    public boolean isDisableDisplayEvents() {
        return DisableDisplayEvents;
    }
}
