package pceft.sdk.eftclient.java;

import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

public class EFTStatusResponse extends EFTResponse {
    /**
     * Default constructor for the Status Response
     */
    public EFTStatusResponse() {
        super(EFTStatusRequest.class);
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
     * The AIIC that is configured in the terminal
     */
    public String AIIC;

    public String getAIIC() {
        return AIIC;
    }

    public void setAIIC(String AIIC) {
        this.AIIC = AIIC;
    }

    /**
     * The NII that is configured in the terminal
     */
    public int NII;

    public int getNII() {
        return NII;
    }

    public void setNII(int NII) {
        this.NII = NII;
    }

    /**
     * Merchant ID configured in the pinpad
     */
    public String Caid;

    public String getCaid() {
        return Caid;
    }

    public void setCaid(String caid) {
        Caid = caid;
    }

    /**
     * Terminal ID configured in the pinpad
     */
    public String Catid = "";

    public String getCatid() {
        return Catid;
    }

    public void setCatid(String catid) {
        Catid = catid;
    }

    /**
     * The bank response timeout that is configured in the terminal
     */
    public int Timeout = 0;

    public int getTimeout() {
        return Timeout;
    }

    public void setTimeout(int timeout) {
        Timeout = timeout;
    }

    /**
     * Indicates if the pinpad is currently logged on
     */
    public boolean LoggedOn = false;

    public boolean isLoggedOn() {
        return LoggedOn;
    }

    public void setLoggedOn(boolean loggedOn) {
        LoggedOn = loggedOn;
    }

    /**
     * The serial number of the terminal
     */
    public String PinpadSerialNumber = "";

    public String getPinpadSerialNumber() {
        return PinpadSerialNumber;
    }

    public void setPinpadSerialNumber(String pinpadSerialNumber) {
        PinpadSerialNumber = pinpadSerialNumber;
    }

    /**
     * Pinpad software version
     */
    public String PinpadVersion = "";

    public String getPinpadVersion() {
        return PinpadVersion;
    }

    public void setPinpadVersion(String pinpadVersion) {
        PinpadVersion = pinpadVersion;
    }

    /**
     * The bank acquirer code
     */
    public char BankCode = ' ';

    public char getBankCode() {
        return BankCode;
    }

    public void setBankCode(char bankCode) {
        BankCode = bankCode;
    }

    /**
     * The bank description
     */
    public String BankDescription = "";

    public String getBankDescription() {
        return BankDescription;
    }

    public void setBankDescription(String bankDescription) {
        BankDescription = bankDescription;
    }

    /**
     * Key Verification Code
     */
    public String KVC = "";

    public String getKVC() {
        return KVC;
    }

    public void setKVC(String KVC) {
        this.KVC = KVC;
    }

    /**
     * Current number of stored transactions
     */
    public int SAFCount = 0;

    public int getSAFCount() {
        return SAFCount;
    }

    public void setSAFCount(int SAFCount) {
        this.SAFCount = SAFCount;
    }

    /**
     * The acquirer communications type
     */
    public EFTStatusRequest.NetworkType NetType = EFTStatusRequest.NetworkType.Unknown;

    public EFTStatusRequest.NetworkType getNetType() {
        return NetType;
    }

    public void setNetType(EFTStatusRequest.NetworkType netType) {
        NetType = netType;
    }

    /**
     * <summary>The hardware serial number.</summary>
     */

    public String HardwareSerial = "";

    public String getHardwareSerial() {
        return HardwareSerial;
    }

    public void setHardwareSerial(String hardwareSerial) {
        HardwareSerial = hardwareSerial;
    }

    /**
     * <summary>The merchant retailer name.</summary>
     */
    public String RetailerName = "";

    public String getRetailerName() {
        return RetailerName;
    }

    public void setRetailerName(String retailerName) {
        RetailerName = retailerName;
    }

    /**
     * <summary>PIN pad terminal supported options flags.</summary>
     */
    public EnumSet<EFTStatusRequest.PinpadOptionFlags> OptionsFlags = EnumSet.noneOf(EFTStatusRequest.PinpadOptionFlags.class);

    public EnumSet<EFTStatusRequest.PinpadOptionFlags> getOptionsFlags() {
        return OptionsFlags;
    }

    public void setOptionsFlags(EnumSet<EFTStatusRequest.PinpadOptionFlags> optionsFlags) {
        OptionsFlags = optionsFlags;
    }

    /**
     * <summary>Store-and forward credit limit.</summary>
     */
    public Double SAFCreditLimit = 0d;

    public Double getSAFCreditLimit() {
        return SAFCreditLimit;
    }

    public void setSAFCreditLimit(Double SAFCreditLimit) {
        this.SAFCreditLimit = SAFCreditLimit;
    }

    /**
     * <summary>Store-and-forward debit limit.</summary>
     */
    public Double SAFDebitLimit = 0d;

    public Double getSAFDebitLimit() {
        return SAFDebitLimit;
    }

    public void setSAFDebitLimit(Double SAFDebitLimit) {
        this.SAFDebitLimit = SAFDebitLimit;
    }

    /**
     * <summary>The maximum number of store transactions.</summary>
     */
    public int MaxSAF = 0;

    public int getMaxSAF() {
        return MaxSAF;
    }

    public void setMaxSAF(int maxSAF) {
        MaxSAF = maxSAF;
    }


    /**
     * <summary>The terminal key handling scheme.</summary>
     */
    public EFTStatusRequest.KeyHandlingType KeyHandlingScheme = EFTStatusRequest.KeyHandlingType.Unknown;

    public EFTStatusRequest.KeyHandlingType getKeyHandlingScheme() {
        return KeyHandlingScheme;
    }

    public void setKeyHandlingScheme(EFTStatusRequest.KeyHandlingType keyHandlingScheme) {
        KeyHandlingScheme = keyHandlingScheme;
    }

    public EFTStatusRequest.KeyHandlingType setKeyHandlingScheme(char c) {
        switch (c) {
            case ('0'):
                return EFTStatusRequest.KeyHandlingType.SingleDES;
            case ('1'):
                return EFTStatusRequest.KeyHandlingType.TripleDES;
            default:
                return EFTStatusRequest.KeyHandlingType.Unknown;
        }
    }

    /**
     * <summary>The maximum cash out limit.</summary>
     */
    public Double CashoutLimit = 0d;

    public Double getCashoutLimit() {
        return CashoutLimit;
    }

    public void setCashoutLimit(Double cashoutLimit) {
        CashoutLimit = cashoutLimit;
    }

    /**
     * <summary>The maximum refund limit.</summary>
     */
    public Double RefundLimit = 0d;

    public Double getRefundLimit() {
        return RefundLimit;
    }

    public void setRefundLimit(Double refundLimit) {
        RefundLimit = refundLimit;
    }

    /**
     * <summary>Card prefix table version.</summary>
     */
    public String CPATVersion = "";

    public String getCPATVersion() {
        return CPATVersion;
    }

    public void setCPATVersion(String CPATVersion) {
        this.CPATVersion = CPATVersion;
    }

    /**
     * <summary>Card name table version.</summary>
     */
    public String NameTableVersion = "";

    public String getNameTableVersion() {
        return NameTableVersion;
    }

    public void setNameTableVersion(String nameTableVersion) {
        NameTableVersion = nameTableVersion;
    }

    /**
     * <summary>The terminal to PC communication type.</summary>
     */
    public EFTStatusRequest.TerminalCommsType TerminalCommType = EFTStatusRequest.TerminalCommsType.Unknown;

    public EFTStatusRequest.TerminalCommsType getTerminalCommType() {
        return TerminalCommType;
    }

    public void setTerminalCommType(EFTStatusRequest.TerminalCommsType terminalCommType) {
        TerminalCommType = terminalCommType;
    }

    public EFTStatusRequest.TerminalCommsType setTerminalCommsType(char c) {
        switch (c) {
            case ('0'):
                return EFTStatusRequest.TerminalCommsType.Cable;
            case ('1'):
                return EFTStatusRequest.TerminalCommsType.Infrared;
            default:
                return EFTStatusRequest.TerminalCommsType.Unknown;
        }
    }

    /**
     * <summary>Number of card mis-reads.</summary>
     */
    public int CardMisreadCount = 0;

    public int getCardMisreadCount() {
        return CardMisreadCount;
    }

    public void setCardMisreadCount(int cardMisreadCount) {
        CardMisreadCount = cardMisreadCount;
    }

    /**
     * <summary>Number of memory pages in the PIN pad terminal.</summary>
     */
    public int TotalMemoryInTerminal = 0;

    public int getTotalMemoryInTerminal() {
        return TotalMemoryInTerminal;
    }

    public void setTotalMemoryInTerminal(int totalMemoryInTerminal) {
        TotalMemoryInTerminal = totalMemoryInTerminal;
    }

    /**
     * <summary>Number of free memory pages in the PIN pad terminal.</summary>
     */
    public int FreeMemoryInTerminal = 0;

    public int getFreeMemoryInTerminal() {
        return FreeMemoryInTerminal;
    }

    public void setFreeMemoryInTerminal(int freeMemoryInTerminal) {
        FreeMemoryInTerminal = freeMemoryInTerminal;
    }

    /**
     * <summary>The type of PIN pad terminal.</summary>
     */
    public EFTStatusRequest.EFTTerminalType TerminalType = EFTStatusRequest.EFTTerminalType.Unknown;

    public EFTStatusRequest.EFTTerminalType getTerminalType() {
        return TerminalType;
    }

    public void setTerminalType(EFTStatusRequest.EFTTerminalType terminalType) {
        TerminalType = terminalType;
    }

    public EFTStatusRequest.EFTTerminalType setTerminalType(String c) {
        if (c.equals("0062"))
            return EFTStatusRequest.EFTTerminalType.IngenicoNPT710;
        else if (c.equals("0069"))
            return EFTStatusRequest.EFTTerminalType.IngenicoPX328;
        else if (c.equals("7010"))
            return EFTStatusRequest.EFTTerminalType.Ingenicoi3070;
        else if (c.equals("5110"))
            return EFTStatusRequest.EFTTerminalType.Ingenicoi5110;
        else return EFTStatusRequest.EFTTerminalType.Unknown;
    }

    /**
     * <summary>Number of applications in the terminal.</summary>
     */
    public int NumAppsInTerminal = 0;

    public int getNumAppsInTerminal() {
        return NumAppsInTerminal;
    }

    public void setNumAppsInTerminal(int numAppsInTerminal) {
        NumAppsInTerminal = numAppsInTerminal;
    }

    /**
     * <summary>Number of available display line on the terminal.</summary>
     */
    public int NumLinesOnDisplay = 0;

    public int getNumLinesOnDisplay() {
        return NumLinesOnDisplay;
    }

    public void setNumLinesOnDisplay(int numLinesOnDisplay) {
        NumLinesOnDisplay = numLinesOnDisplay;
    }

    /**
     * <summary>The date the hardware was incepted.</summary>
     */
    public Date HardwareInceptionDate = Calendar.getInstance().getTime();

    public Date getHardwareInceptionDate() {
        return HardwareInceptionDate;
    }

    public void setHardwareInceptionDate(Date hardwareInceptionDate) {
        HardwareInceptionDate = hardwareInceptionDate;
    }

    /**
     * <summary>Indicates if the request was successful.</summary>
     */
    public boolean Success = false;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    /**
     * <summary>The response code of the request.</summary>
     */
    public String ResponseCode = "";

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    /**
     * <summary>The response text for the response code.</summary>
     */
    public String ResponseText = "";

    public String getResponseText() {
        return ResponseText;
    }

    public void setResponseText(String responseText) {
        ResponseText = responseText;
    }
}
