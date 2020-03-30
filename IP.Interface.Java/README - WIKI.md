# IP.Interface.Java Wiki

For more information on messages and TCP/IP communications with PC-EFTPOS/Linkly related software, check out the [API Documentation for TCPIP](https://pceftpos.com/apidoc/TCPIP/#message-specification)

## Basket Data
*[Basket Data Documentation](https://pceftpos.com/apidoc/TCPIP/#appendix-g-basket-api)*

```java
EFTGenericCommandRequest genCmd = new EFTGenericCommandRequest();
genCmd.CommandType = EFTGenericCommandRequest.SubCode.BasketData;
genCmd.BasketCommand = [IEFTBasketDataCommand]
```

IEFTBasketDataCommand:

|Command|Values|
|----------------------------|-----------------------|
|<b>*EFTBasketDataCommandCreate*</b>|EFTBasket Basket     |
|<b>*EFTBasketDataCommandAdd* </b>  |EFTBasket Basket     |
|<b>*EFTBasketDataCommandDelete*</b>|String BasketID <br>String BasketItemID    |
|<b>*EFTBasketDataCommandRaw* </b>  |String BasketContent |

### EFTBasket

| Type                     | Property |
| ------------------------ | -------- |
| String                   | id       |
| ArrayList(EFTBasketItem) | items    |
| int                      | amt      |
| int                      | tax      |
| int                      | dis      |
| int                      | sur      |

### EFTBasketItem

| Type   | Property |
| ------ | -------- |
| String | id       |
| String | sku      |
| int    | qty      |
| int    | amt      |
| int    | tax      |
| int    | dis      |
| String | ean      |
| String | upc      |
| String | gtin     |
| String | name     |
| String | desc     |
| String | srl      |
| String | img      |
| String | link     |
| String | tag      |

## Requests

### EFTRequest - Base Class

```java
public abstract class EFTRequest {
    public enum ReceiptPrintModeType{
        POSPrinter('0'),
        EFTClientPrinter('1'),
        PinpadPrinter('9');
        ReceiptPrintModeType(char c){
            PrintType = c;
        }
        public char PrintType;
    }
    public enum ReceiptCutModeType{
        DontCut('0'),
        Cut('1');

        public char CutType;
        ReceiptCutModeType(char c){
            CutType = c;
        }
    }
    protected boolean isStartOfTransaction = true;
    protected Type pairedResponseType = null;
    private EFTRequest(){}
    public EFTRequest(boolean isStart, Type pairedResponse){
        if(pairedResponse != null && !pairedResponse.getClass().isInstance(EFTResponse.class)){
            throw new IllegalStateException("pairedResponseType must be based on EFTResponse");
        }
        isStartOfTransaction = isStart;
        pairedResponseType = pairedResponse;
    }
    public boolean isStartOfTransaction() {
        return isStartOfTransaction;
    }
    public Type getPairedResponseType() {
        return pairedResponseType;
    }
}
```

*Note: Some request classes have default values, so we can call a request without setting any other values*

### EFTChequeAuthRequest

```java
//EXAMPLE CODE
EFTChequeAuthRequest chqAuth = new EFTChequeAuthRequest();
AsyncSocketControl ctrl.socketSend(chqAuth);
```

### EFTClientListRequest

```java
//EXAMPLE CODE
EFTClientListRequest client = new EFTClientListRequest();
AsyncSocketControl ctrl.socketSend(client);
```

### EFTCloudLogonRequest

```java
//EXAMPLE CODE
EFTCloudLogonRequest Lgn = new EFTCloudLogonRequest();
Lgn.setClientID("ClientID");
Lgn.setPairCode("PairCode");
Lgn.setPassword("Password");
AsyncSSLSocketControl ctrl.socketSend(Lgn);
```

### EFTCloudPairRequest

```java
//EXAMPLE CODE
EFTCloudPairRequest pair = new EFTCloudPairRequest();
pair.setClientID("ClientID");
pair.setPairCode("PairCode");
pair.setPassword("Password");
AsyncSSLSocketControl ctrl.socketSend(pair);
```

### EFTCloudTokenLogonRequest

```java
//EXAMPLE CODE
EFTCloudTokenLogonRequest tknLgn = new EFTCloudTokenLogonRequest();
tknLgn.setToken([Token_String]);
AsyncSSLSocketControl ctrl.socketSend(tknLgn);
```

### EFTConfigureMerchantRequest

```java
//EXAMPLE CODE
EFTConfigureMerchantRequest cfgMrchnt = new EFTConfigureMerchantRequest();
AsyncSSLSocketControl ctrl.socketSend(cfgMrchnt);
```

### EFTControlPanelRequest

```java
//EXAMPLE REQUEST CODE
EFTControlPanelRequest cntrlPnl = new EFTControlPanelRequest();
cntrlPnl.CPanelType = EFTControlPanelRequest.ControlPanelType.Full;
cntrlPnl.ReceiptPrintMode = EFTRequest.ReceiptPrintModeType.PinpadPrinter;
cntrlPnl.ReceiptCutMode = EFTRequest.ReceiptCutModeType.DontCut;
cntrlPnl.CPanelReturnType = EFTControlPanelRequest.ControlPanelReturnType.ImmediatelyAndWhenClose;
AsyncSocketControl ctrl.socketSend(cntrlPnl);
```

| ControlPanelType               | Description                               |
| ------------------------------ | ----------------------------------------- |
| ControlPanelType.Full          | Display Full Control Panel                |
| ControlPanelType.Settlement    | Display Settlement Control Panel only     |
| ControlPanelType.JournalViewer | Display Journal Viewer Control Panel only |
| ControlPanelType.PinpadSetup   | Display Pinpad Setup Control Panel only   |
| ControlPanelType.Status        | Display Status Control Panel only         |

### EFTDisplayRequest

```java
//EXAMPLE CODE
EFTDisplayRequest display = new EFTDisplayRequest();
display.setNumberOfLines("02");
display.setLineLength("20");
display.setDisplayText("123456789009876543211234567890098765432D");
display.setCancelKeyFlag(true);
display.setAcceptKeyFlag(true);
display.setDeclineKeyFlag(true);
display.setAuthoriseKeyFlag(true);
display.setInputDataField(EFTDisplayRequest.InputDataType.ASCII);
display.setOKKeyFlag(true);
display.setGraphicsCode(EFTDisplayRequest.GraphicsCodeType.Processing);
AsyncSocketControl ctrl.socketSend(display);
```

### EFTGenericCommandRequest

*[TCPIP Documentation](https://pceftpos.com/apidoc/TCPIP/#generic-pos-command-display-data)*

Requirements:

- PosCommand.bcx (For Display & Print Data)
- TPP_PAT.bcx (For PayAtTable)

##### Pay At Table

```java
//EXAMPLE CODE
EFTGenericCommandRequest genCmd = new EFTGenericCommandRequest();
genCmd.CommandType = EFTGenericCommandRequest.SubCode.PayAtTable;
genCmd.Header = "[Insert String Here]";
genCmd.Content = "[Insert String Here]";
```

##### Get Password

```java
//EXAMPLE CODE
EFTGenericCommandRequest genCmd = new EFTGenericCommandRequest();
genCmd.CommandType = EFTGenericCommandRequest.SubCode.GetPassword;
genCmd.MinPasswordLength = 6; // ANY NUMBER BETWEEN 0-99
genCmd.MaxPasswordLength = 8; // ANY NUMBER BETWEEN 0-99
genCmd.Timeout = 0; // ANY NUMBER BETWEEN 0-255
genCmd.PasswordDisplay = EFTGenericCommandRequest._PasswordDisplay.Enter_Code;
```

### EFTGetLastTransactionRequest

```java
//EXAMPLE CODE
EFTGetLastTransactionRequest getLast = new EFTGetLastTransactionRequest();
AsyncSocketControl ctrl.socketSend(getLast);
```

### EFTLogonRequest

```java
//EXAMPLE CODE
EFTLogonRequest Logon = new EFTLogonRequest();
Logon.logonType = EFTLogonRequest.LogonType.Standard;
Logon.ReceiptPrintMode = EFTRequest.ReceiptPrintModeType.POSPrinter;
AsyncSocketControl ctrl.socketSend(Logon);
```

| LogonType | Description                                              |
| --------- | -------------------------------------------------------- |
| Standard  | Standard logon type                                      |
| TMSFull   | TMS Logon (TMS_PARAMS & TMS_SW also supported in TCP/IP) |
| RSA       | RSA logon type                                           |

### EFTQueryCardRequest

```java
//EXAMPLE CODE
EFTQueryCardRequest crdQuery = new EFTQueryCardRequest();
crdQuery.setAccountType(EFTQueryCardRequest.SelectAccountType.NoAccountSelected);
crdQuery.Application = EFTQueryCardRequest.TerminalApplication.EFTPOS;
crdQuery.Merchant = "00";
AsyncSocketControl ctrl.socketSend(crdQuery);
```

### EFTReceiptRequest

```java
AsyncSocketControl ctrl.socketSend(new EFTReceiptRequest());
```

### EFTReprintReceiptRequest

```java
EFTReprintReceiptRequest reprint = new EFTReprintReceiptRequest();
reprint.ReprntType = EFTReprintReceiptRequest.ReprintType.Reprint;
AsyncSocketControl ctrl.socketSend(reprint);
```

| ReprintType | Description                                               |
| ----------- | --------------------------------------------------------- |
| GetLast     | Get the last receipt                                      |
| Reprint     | reprint receipt, can use TxnRef to reprint prior receipts |

### EFTSendKeyRequest

```java
EFTSendKeyRequest keyPress = new EFTSendKeyRequest();
keyPress.Key = EFTSendKeyRequest.EFTPOSKey.YesAccept;
AsyncSocketControl ctrl.socketSend(keyPress);
```

| EFTPOSKey | Description          |
| --------- | -------------------- |
| OKCancel  | CANCEL key or OK key |
| YesAccept | YES key              |
| NoDecline | NO key               |
| Authorise | AUTH key             |

### EFTSetDialogRequest

```java
EFTSetDialogRequest dialog = new EFTSetDialogRequest();
dialog.DialogTitle = txtDialogTitle.getText();
dialog.dialogType = EFTSetDialogRequest.DialogType.Standard;
dialog.DialogX = 10;
dialog.DialogY = 10;
dialog.DisableDisplayEvents = false;
dialog.EnableTopmost = false;
AsyncSocketControl ctrl.socketSend(dialog);
```

| DialogType  | Description                     |
| ----------- | ------------------------------- |
| Standard    | Standard dialog display         |
| TouchScreen | dialog display for touchscreens |
| Hidden      | Hide dialog                     |

### EFTSettlementRequest

```java
EFTSettlementRequest settle = new EFTSettlementRequest();
settle.SettleType = EFTSettlementRequest.SettlementType.Settlement;
AsyncSocketControl ctrl.socketSend(settle);
```

### EFTStatusRequest

```java
EFTStatusRequest Status = new EFTStatusRequest();
Status.StatType = EFTStatusRequest.StatusType.Standard;
AsyncSocketControl ctrl.socketSend(Status);
```

### EFTTransactionRequest

```java
//EXAMPLE CODE
EFTTransactionRequest newTxn = new EFTTransactionRequest();
newTxn.AmtPurchase = 15.00;
newTxn.AmtCash = 0.00;
newTxn.Merchant = MerchantBox.getText();
newTxn.Application = EFTTransactionRequest.TerminalApplication.EFTPOS;
newTxn.TxnRef = "270220201003333 ";
newTxn.BankDate = Date.from(Instant.now());
newTxn.PurchaseAnalysisData = PADTxtBox.getText();
newTxn.TxnType = EFTTransactionRequest.TransactionType.PurchaseCash;
AsyncSocketControl ctrl.socketSend(newTxn);
```

*read the [API Documentation for TCPIP](https://pceftpos.com/apidoc/TCPIP/#message-specification) for more information on application types & transaction types*

## Responses

### EFTResponse -Base Class

```java
public abstract class EFTResponse {
    protected Type pairedRequestType = null;
    private EFTResponse(){}
    public EFTResponse(Type pairedRequest){
        if(pairedRequest != null && !pairedRequest.getClass().isInstance(EFTRequest.class)){
            throw new IllegalStateException("pairedRequestType must be based on EFTRequest");
        }
        pairedRequestType = pairedRequest;
    }
    public Type getPairedRequestType() {
        return pairedRequestType;
    }
}
```

### EFTChequeAuthResponse

```java
boolean SuccessFlag;
String ResponseCode;
String ResponseText;
String Merchant;
double AmtPurchase;
String AuthCode;
String ReferenceCode;
```

### EFTClientListResponse

```java
String ResponseCode;
String ResponseText;
ClientDesc Response;
```

<b>ClientDesc</b>

```java
String ClientName;
String ClientIP;
String ClientPort;
String ClientState;
```

### EFTCloudLogonResponse

```java
boolean SuccessFlag;
String ResponseCode;
String ResponseText;
```

### EFTCloudPairResponse

```java
boolean SuccessFlag;
String ResponseCode;
String ResponseText;
int RedirectPort;
String RedirectAddress;
String Token;
```

### EFTCloudTokenLogonResponse

```java
boolean SuccessFlag;
String ResponseCode;
String ResponseText;
```

### EFTConfigureMerchantResponse

```java
boolean SuccessFlag;
String ResponseCode;
String ResponseText;
```

### EFTControlPanelResponse

```java
boolean Success;
String ResponseCode;
String ResponseText;
```

### EFTDisplayResponse

```java
enum InputType{
        None('0'),
        Normal('1'),
        Amount('2'),
        Decimal('3'),
        Password('4');
}
enum GraphicCode{
        Processing('0'),
        Verify('1'),
        Question('2'),
        Card('3'),
        Account('4'),
        PIN('5'),
        Finished('6'),
        None(' ');
}
```

```java
int NumberOfLines;
int LineLength;
String[] DisplayText;
boolean CancelKeyFlag;
boolean AcceptYesKeyFlag;
boolean DeclineNoKeyFlag;
boolean AuthoriseKeyFlag;
boolean OKKeyFlag;
InputType Input;
GraphicCode Graphic;
String PurchaseAnalysisData;
```

### EFTGenericCommandResponse

```java
enum SubCode{
        DisplayData('0'),
        PrintData('1'),
        GetPassword('2'),
        SlaveCommand('Z'),
        PayAtTable('@'),
        BasketData('%');
}
```

```java
SubCode CommandType;
String ResponseCode;
String ResponseText;
String Data;
String SlaveResponse;
boolean SuccessFlag;
String Password;
String Header;
String Content;
```

### EFTGetLastTransactionResponse

```java
String Merchant;
TransactionType TxnType; //See https://pceftpos.com/apidoc/TCPIP/#appendix-d-txntype
String CardType;
int CardName;
String RRN;
Date SettlementDate;
double AmtCash;
double AmtPurchase;
double AmtTip;
int AuthCode;
String TxnRef;
String Pan;
String DateExpiry;
String Track2;
AccountType CardAccountType; //SAVINGS, CREDIT, CHEQUE
boolean LastTransactionSuccess;
TxnFlags TxnFlags; //See https://pceftpos.com/apidoc/TCPIP/#transaction
boolean BalanceReceived;
double AvailableBalance;
boolean IsTrainingMode;
boolean Success;
String ResponseCode;
String ResponseText;
Date BankDate;
String Catid;
String Caid;
int Stan;
String PurchaseAnalysisData;
```

### EFTLogonResponse

```java
String PinPadVersion;
boolean Success;
String ResponseCode;
String ResponseText;
Date BankDate;
String Catid;
String Caid;
int Stan;
String PurchaseAnalysisData;
```

### EFTPinpadBusyResponse

```java
String ResponseCode = "BY";
String ResponseText = "PinpadBusy";
```

*In a pinpad busy response, Response Code and Response Text will always be the same*

### EFTQueryCardResponse

```java
enum SelectAccountType{
        NoAccountSelected(' '),
        SelectAccount_ReadCard('1'),
        GetAccountOnly('5');
}
```

```java
SelectAccountType AccountType;
boolean SuccessFlag;
String ResponseCode;
String ResponseText;
String Track2;
String Track1_Track3;
char TracksRead;
String BinNumber;
String PurchaseAnalysisData;
```

### EFTReceiptResponse

```java
public enum ReceiptType {
        Logon('L'),
        Customer('C'),
        Merchant('M'),
        Settlement('S'),
        ReceiptText('R');
}
```

```java
ReceiptType Receipt;
String[] ReceiptText;
boolean IsPrePrint;
```

### EFTReprintReceiptResponse

```java
String Merchant;
String[] ReceiptText;
boolean Success;
String ResponseCode;
String ResponseText;
```

### EFTSetDialogResponse

```java
boolean Success;
```

### EFTSettlementResponse

```java
String Merchant;
String SettlementData;
boolean Success;
String ResponseCode;
String ResponseText;
```

### EFTStatusResponse

```java
public enum NetworkType {
        Leased('0'),
        Dialup('1'),
        Unknown;
}
```

```java
public enum PinpadOptionFlags {
        Tipping,
        PreAuth,
        Completions,
        CashOut,
        Refund,
        Balance,
        Deposit,
        Voucher,
        MOTO,
        AutoCompletion,
        EFB,
        EMV,
        Training,
        Withdrawal,
        Transfer,
        StartCash
    }
```

```java
public enum KeyHandlingType {

        SingleDES('0'),
        TripleDES('1'),
        Unknown;
}
```

```java
public enum TerminalCommsType {
        Cable('0'),
        Infrared('1'),
        Unknown;
}
```

```java
public enum EFTTerminalType {
        IngenicoNPT710,
        IngenicoPX328,
        Ingenicoi5110,
        Ingenicoi3070,
        Sagem,
        Verifone,
        Keycorp,
        Unknown
}
```

```java
String Merchant;
String AIIC;
int NII;
String Caid;
String Catid;
int Timeout;
boolean LoggedOn;
String PinpadSerialNumber;
String PinpadVersion;
char BankCode;
String BankDescription;
String KVC;
int SAFCount;
NetworkType NetType;
String HardwareSerial;
String RetailerName;
EnumSet<PinpadOptionFlags> OptionsFlags;
Double SAFCreditLimit;
Double SAFDebitLimit;
int MaxSAF;
KeyHandlingType KeyHandlingScheme;
Double CashoutLimit;
Double RefundLimit;
String CPATVersion;
String NameTableVersion;
TerminalCommsType TerminalCommType;
int CardMisreadCount;
int TotalMemoryInTerminal;
int FreeMemoryInTerminal;
EFTTerminalType TerminalType;
int NumAppsInTerminal;
int NumLinesOnDisplay;
Date HardwareInceptionDate;
boolean Success;
String ResponseCode;
String ResponseText;
```

### EFTTransactionResponse

```java
TransactionType TxnType; //See https://pceftpos.com/apidoc/TCPIP/#appendix-d-txntype
String Merchant;
String CardType;
int CardName;
String RRN;
Date SettlementDate;
double AmtCash;
double AmtPurchase;
double AmtTip;
int AuthCode;
String TxnRef;
String Pan;
String DateExpiry;
String Track2;
AccountType CardAccountType; //SAVINGS, CREDIT, CHEQUE
TxnFlags TxnFlags; //See https://pceftpos.com/apidoc/TCPIP/#transaction
boolean BalanceReceived;
double AvailableBalance;
double ClearedFundsBalance;
boolean Success;
String ResponseCode;
String ResponseText;
Date BankDate;
String Catid;
String Caid;
int Stan;
String PurchaseAnalysisData;
```