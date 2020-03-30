package pceft.sdk.eftclient.java;

public class EFTStatusRequest extends EFTRequest {
    /**
     * Indicates the requested status type
     */
    public enum StatusType {
        /**
         * Request the EFT status from the pinpad
         */
        Standard('0'),
        /**
         * Not support by all pinpads
         */
        TerminalAppInfo('1'),
        /**
         * Not support by all pinpads
         */
        AppCPAT('2'),
        /**
         * Not support by all pinpads
         */
        AppNameTable('3'),
        /**
         * Undefined
         */
        Undefined('4'),
        /**
         * Not support by all pinpads
         */
        PreSwipe('5');

        public char StatType;

        StatusType(char c) {
            StatType = c;
        }
    }

    public enum EFTTerminalType {
        /**
         * <summary>Ingenico NPT 710 PIN pad terminal.</summary>
         */
        IngenicoNPT710,
        /**
         * <summary>Ingenico NPT PX328 PIN pad terminal.</summary>
         */
        IngenicoPX328,
        /**
         * <summary>Ingenico NPT i5110 PIN pad terminal.</summary>
         */
        Ingenicoi5110,
        /**
         * <summary>Ingenico NPT i3070 PIN pad terminal.</summary>
         */
        Ingenicoi3070,
        /**
         * <summary>Sagem PIN pad terminal.</summary>
         */
        Sagem,
        /**
         * <summary>Verifone PIN pad terminal.</summary>
         */
        Verifone,
        /**
         * <summary>Keycorp PIN pad terminal.</summary>
         */
        Keycorp,
        /**
         * <summary>Unknown PIN pad terminal.</summary>
         */
        Unknown
    }

    /**
     * Pinpad terminal supported options
     */
    public enum PinpadOptionFlags {
        /**
         * 0x0001
         */
        Tipping,
        /**
         * 0x0002
         */
        PreAuth,
        /**
         * 0x0004
         */
        Completions,
        /**
         * 0x0008
         */
        CashOut,
        /**
         * 0x0010
         */
        Refund,
        /**
         * 0x0020
         */
        Balance,
        /**
         * 0x0040
         */
        Deposit,
        /**
         * 0x0080
         */
        Voucher,
        /**
         * 0x0100
         */
        MOTO,
        /**
         * 0x0200
         */
        AutoCompletion,
        /**
         * 0x0400
         */
        EFB,
        /**
         * 0x0800
         */
        EMV,
        /**
         * 0x1000
         */
        Training,
        /**
         * 0x2000
         */
        Withdrawal,
        /**
         * 0x4000
         */
        Transfer,
        /**
         * 0x8000
         */
        StartCash
    }

    /**
     * Pinpad terminal key handling scheme
     */
    public enum KeyHandlingType {
        /**
         * Single-DES encryption standard
         */
        SingleDES('0'),
        /**
         * Triple-DES encryption standard
         */
        TripleDES('1'),
        /**
         * Unknown encryption standard
         */
        Unknown;

        public char EncryptionType;

        KeyHandlingType(char c) {
            EncryptionType = c;
        }

        KeyHandlingType() {
        }
    }

    /**
     * Pinpad terminal network option
     */
    public enum NetworkType {

        Leased('0'),
        Dialup('1'),
        Unknown;

        public char NetType;

        NetworkType(char c) {
            NetType = c;
        }

        NetworkType() {
        }
    }

    /**
     * Pinpad terminal communication option
     */
    public enum TerminalCommsType {
        Cable('0'),
        Infrared('1'),
        Unknown;

        public char CommsType;

        TerminalCommsType(char c) {
            CommsType = c;
        }

        TerminalCommsType() {
        }
    }

    public EFTStatusRequest() {
        super(true, EFTStatusResponse.class);
    }

    /**
     * Two digit merchant code. Default is "00"
     */
    public String Merchant = "00";

    public String getMerchant() {
        return Merchant;
    }

    public void setMerchant(String merchant) {
        Merchant = merchant;
    }

    /**
     * Type of status to perform. The default is StatusType.Standard
     */
    public StatusType StatType = StatusType.Standard;

    public StatusType getStatType() {
        return StatType;
    }

    public void setStatType(StatusType statType) {
        StatType = statType;
    }

    /**
     * Indicates where the request is to be sent. Should normally be EFTPOS.
     */
    public EFTTransactionRequest.TerminalApplication Application = EFTTransactionRequest.TerminalApplication.EFTPOS;

    public EFTTransactionRequest.TerminalApplication getApplication() {
        return Application;
    }

    public void setApplication(EFTTransactionRequest.TerminalApplication application) {
        Application = application;
    }
}
