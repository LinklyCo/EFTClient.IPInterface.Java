package pceft.sdk.eftclient.java;

/**
 * A PC-EFTPOS show control panel request object
 */
public class EFTControlPanelRequest extends EFTRequest {

    /**
     * Indicates which tabs of the PC-EFTPOS Client Control panel to display
     */
    public enum ControlPanelType {
        /**
         * <summary>Show the control panel with all tabs available.</summary>
         */
        Full('0'),
        /**
         * <summary>Show the control panel with only the settlement tab available.</summary>
         */
        Settlement('1'),
        /**
         * <summary>Show the control panel with only the journal viewer tab available.</summary>
         */
        JournalViewer('2'),
        /**
         * <summary>Show the control panel with only the pinpad setup tab available.</summary>
         */
        PinpadSetup('3'),
        /**
         * <summary>Show the control panel with only the status tab available.</summary>
         */
        Status('4');

        public char PanelType;

        ControlPanelType(char c) {
            PanelType = c;
        }
    }

    /**
     * Indicates when to trigger the OnDisplayControlPanel event
     */
    public enum ControlPanelReturnType {
        /**
         * Trigger the event immediately
         */
        Immediately('0'),
        /**
         * Trigger the event when the control panel is closed
         */
        WhenClosed('1'),
        /**
         * Trigger the event immediately and when the control panel is closed
         */
        ImmediatelyAndWhenClose('2');

        public char ReturnType;

        ControlPanelReturnType(char c) {
            ReturnType = c;
        }
    }

    /**
     * Constructs a default show control panel request object
     */
    public EFTControlPanelRequest() {
        super(true, EFTControlPanelResponse.class);
    }

    public ControlPanelType CPanelType = ControlPanelType.Full;

    public ControlPanelType getCPanelType() {
        return CPanelType;
    }

    public ControlPanelReturnType CPanelReturnType = ControlPanelReturnType.Immediately;

    public ControlPanelReturnType getCPanelReturnType() {
        return CPanelReturnType;
    }

    public ReceiptPrintModeType ReceiptPrintMode = ReceiptPrintModeType.POSPrinter;

    public ReceiptPrintModeType getReceiptPrintMode() {
        return ReceiptPrintMode;
    }

    public ReceiptCutModeType ReceiptCutMode = ReceiptCutModeType.DontCut;

    public ReceiptCutModeType getReceiptCutMode() {
        return ReceiptCutMode;
    }
}
