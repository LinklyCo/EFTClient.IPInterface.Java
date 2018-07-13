
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

class MessageParser {

    enum IPClientResponseType {
        Logon('G'),
        Transaction('M'),
        QueryCard('J'),
        Configure('1'),
        ControlPanel('5'),
        SetDialog('2'),
        Settlement('P'),
        DuplicateReceipt('C'),
        GetLastTransaction('N'),
        Status('K'),
        Receipt('3'),
        Display('S'),
        GenericPOSCommand('X'),
        PinRequest('W'),
        ChequeAuth('H'),
        SendKey('Y'),
        ClientList('Q'),
        PinPadBusy('0'),
        CloudLogon('A');

        public char ResponseType;

        IPClientResponseType(char c) {
            ResponseType = c;
        }
    }

    MessageParser() {
    }

    //region String to EFTResponse

    EFTResponse parseMsgStr(String s) {
        return StringToEFTResponse(s);
    }

    private EFTResponse StringToEFTResponse(String msg) {
        if (msg.length() < 1) {
            System.out.println("msg is null or zero length");
            throw new IllegalArgumentException("msg is null or zero length");
        }
        EFTResponse eftResponse;
        IPClientResponseType responseType = ParseResponseType(msg.charAt(4));
        switch (responseType) {
            case Display:
                eftResponse = ParseDisplayResponseType(msg);
                break;
            case Receipt:
                eftResponse = ParseReceiptResponse(msg);
                break;
            case Logon:
                eftResponse = ParseLogonResponse(msg);
                break;
            case Transaction:
                eftResponse = ParseTransactionResponse(msg);
                break;
            case SetDialog:
                eftResponse = ParseSetDialogResponse(msg);
                break;
            case GetLastTransaction:
                eftResponse = ParseGetLastTransactionResponse(msg);
                break;
            case DuplicateReceipt:
                eftResponse = ParseEFTReprintReceiptResponse(msg);
                break;
            case ControlPanel:
                eftResponse = ParseControlPanelResponse(msg);
                break;
            case Settlement:
                eftResponse = ParseSettlementResponse(msg);
                break;
            case Status:
                eftResponse = ParseStatusResponse(msg);
                break;
            case PinPadBusy:
                eftResponse = ParsePinpadBusyResponse(msg);
                break;
//            case ChequeAuth:
//                eftResponse = ParseChequeAuthResponse(msg);
//                break;
//            case QueryCard:
//                eftResponse = ParseQueryCardResponse(msg);
//                break;
//            case GenericPOSCommand:
//                eftResponse = ParseGenericPOSCommandResponse(msg);
//                break;
//            case Configure:
//                eftResponse = ParseConfigMerchantResponse(msg);
//                break;
//            case CloudLogon:
//                eftResponse = ParseCloudLogonResponse(msg);
//                break;
//            case Clientlist:
//                eftResponse = ParseClientListResponse(msg);
//                break;
            default:
                System.out.println(String.format("Unknown message type: %s", responseType));
                throw new IllegalArgumentException(String.format("Unknown message type: %s", responseType));
        }
        return eftResponse;
    }

    private EFTResponse ParsePinpadBusyResponse(String msg) {
        EFTPinpadBusyResponse r = new EFTPinpadBusyResponse();
        return r;
    }

    private EFTResponse ParseSetDialogResponse(String msg) {
        int index = 6;
        EFTSetDialogResponse r = new EFTSetDialogResponse();
        r.Success = msg.charAt(index) == '1';

        return r;
    }

    private EFTResponse ParseControlPanelResponse(String msg) {
        int index = 6;
        EFTControlPanelResponse r = new EFTControlPanelResponse();

        r.Success = msg.charAt(index++) == '1';
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        return r;
    }

    private EFTResponse ParseEFTReprintReceiptResponse(String msg) {
        EFTReprintReceiptResponse r = new EFTReprintReceiptResponse();
        int index = 6;
        r.Success = msg.charAt(index++) == '1';
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        List<String> receiptLines = new ArrayList<>();
        String receipt = msg.substring(index);
        index = 0;
        boolean done = false;
        while (!done) {
            int lineLength = receipt.substring(index).indexOf("\r\n");
            if (lineLength > 0) {
                receiptLines.add(receipt.substring(index, index + lineLength));
                index += lineLength + 2;
                if (index >= receipt.length())
                    done = true;
            } else
                done = true;
        }
        r.ReceiptText = receiptLines.toArray(r.ReceiptText);
        return r;
    }

    private EFTResponse ParseGetLastTransactionResponse(String msg) {
        EFTGetLastTransactionResponse r = new EFTGetLastTransactionResponse();
        int index = 6;
        r.Success = msg.charAt(index++) == '1';
        r.LastTransactionSuccess = msg.charAt(index++) == '1';
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        r.Merchant = msg.substring(index, index + 2);
        index += 2;
        if (Character.isLowerCase(msg.charAt(index))) {
            r.IsTrainingMode = true;
            msg = msg.substring(0, index) + Character.toUpperCase(msg.charAt(index)) + msg.substring(index + 1);
        }
        //index++;
        r.TxnType = r.setTxnType(msg.charAt(index++));
        String accountType = msg.substring(index, index + 7);
        switch (accountType) {
            case "Credit ":
                r.CardAccountType = EFTTransactionRequest.AccountType.Credit;
                break;
            case "Savings":
                r.CardAccountType = EFTTransactionRequest.AccountType.Savings;
                break;
            case "Cheque ":
                r.CardAccountType = EFTTransactionRequest.AccountType.Cheque;
                break;
            default:
                r.CardAccountType = EFTTransactionRequest.AccountType.Default;
                break;
        }
        index += 7;
        try {
            r.AmtCash = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.AmtCash = 0d;
        }
        index += 9;
        try {
            r.AmtPurchase = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.AmtPurchase = 0d;
        }
        index += 9;
        try {
            r.AmtTip = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.AmtTip = 0d;
        }
        index += 9;
        try {
            r.AuthCode = Integer.parseInt(msg.substring(index, index + 6));
        } catch (Exception e) {
            e.printStackTrace();
            r.AuthCode = 0;
        }
        index += 6;
        r.TxnRef = msg.substring(index, index + 16);
        index += 16;
        try {
            r.Stan = Integer.parseInt(msg.substring(index, index + 6).trim());
        } catch (Exception e) {
            e.printStackTrace();
            r.Stan = 0;
        }
        index += 6;
        r.Caid = msg.substring(index, index + 15);
        index += 15;
        r.Catid = msg.substring(index, index + 8);
        index += 8;
        r.DateExpiry = msg.substring(index, index + 4);
        index += 4;
        r.SettlementDate = r.setSettlementDate(msg.substring(index, index + 4));
        index += 4;
        r.BankDate = r.setBankDate(msg.substring(index, index + 12));
        index += 12;
        r.CardType = msg.substring(index, index + 20);
        index += 20;
        r.Pan = msg.substring(index, index + 20);
        index += 20;
        r.Track2 = msg.substring(index, index + 40);
        index += 40;
        r.RRN = msg.substring(index, index + 12);
        index += 12;
        try {
            r.CardName = Integer.parseInt(msg.substring(index, index + 2));
        } catch (Exception e) {
            e.printStackTrace();
            r.CardName = 0;
        }
        index += 2;
        r.TxnFlags = new TxnFlags(msg.substring(index, index + 8).toCharArray());
        index += 8;
        r.BalanceReceived = msg.charAt(index++) == '1';
        try {
            r.AvailableBalance = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.AvailableBalance = 0d;
        }
        index += 9;
        try {
            Integer.parseInt(msg.substring(index, index + 3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        index += 3;
        r.PurchaseAnalysisData = msg.substring(index).trim();
        return r;
    }

    private EFTResponse ParseSettlementResponse(String msg) {
        EFTSettlementResponse r = new EFTSettlementResponse();
        int index = 6;
        r.Success = msg.charAt(index++) == '1';
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        if (msg.length() > 25) {
            r.SettlementData = msg.substring(index);
        }
        return r;
    }

    private EFTResponse ParseStatusResponse(String msg) {
        int index = 6;
        EFTStatusResponse r = new EFTStatusResponse();
        r.Success = msg.charAt(index++) == '1';
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        if (index >= msg.length())
            return r;
        r.Merchant = msg.substring(index, index + 2);
        index += 2;
        r.AIIC = msg.substring(index, index + 11);
        index += 11;
        try {
            r.NII = Integer.parseInt(msg.substring(index, index + 3));
        } catch (Exception e) {
            e.printStackTrace();
            r.NII = 0;
        }
        index += 3;
        r.Caid = msg.substring(index, index + 15);
        index += 15;
        r.Catid = msg.substring(index, index + 8);
        index += 8;
        try {
            r.Timeout = Integer.parseInt(msg.substring(index, index + 3));
        } catch (Exception e) {
            e.printStackTrace();
            r.Timeout = 0;
        }
        index += 3;
        r.LoggedOn = msg.charAt(index++) == '1';
        r.PinpadSerialNumber = msg.substring(index, index + 16);
        index += 16;
        r.PinpadVersion = msg.substring(index, index + 16);
        index += 16;
        r.BankDescription = msg.substring(index, index + 32);
        index += 32;
        int padLength = 0;
        try {
            padLength = Integer.parseInt(msg.substring(index, index + 3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        index += 3;
        if (msg.length() - index < padLength)
            return r;
        try {
            r.SAFCount = Integer.parseInt(msg.substring(index, index + 4));
        } catch (Exception e) {
            e.printStackTrace();
            r.SAFCount = 0;
        }
        index += 4;
        r.NetType = r.setNetType(msg.charAt(index));
        index++;
        r.HardwareSerial = msg.substring(index, index + 16);
        index += 16;
        r.RetailerName = msg.substring(index, index + 40);
        index += 40;
        r.OptionsFlags = ParsePinpadOptions(msg.substring(index, index + 32).toCharArray());
        index += 32;
        try {
            r.SAFCreditLimit = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.SAFCreditLimit = 0d;
        }
        index += 9;
        try {
            r.SAFDebitLimit = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.SAFDebitLimit = 0d;
        }
        index += 9;
        try {
            r.MaxSAF = Integer.parseInt(msg.substring(index, index + 3));
        } catch (Exception e) {
            r.MaxSAF = 0;
        }
        index += 3;
        r.KeyHandlingScheme = r.setKeyHandlingScheme(msg.charAt(index++));
        try {
            r.CashoutLimit = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            r.CashoutLimit = 0d;
        }
        index += 9;
        try {
            r.RefundLimit = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.RefundLimit = 0d;
        }
        index += 9;
        r.CPATVersion = msg.substring(index, index + 6);
        index += 6;
        r.NameTableVersion = msg.substring(index, index + 6);
        index += 6;
        r.TerminalCommType = r.setTerminalCommsType(msg.charAt(index++));
        try {
            r.CardMisreadCount = Integer.parseInt(msg.substring(index, index + 6));
        } catch (Exception e) {
            r.CardMisreadCount = 0;
        }
        index += 6;
        try {
            r.TotalMemoryInTerminal = Integer.parseInt(msg.substring(index, index + 4));
        } catch (Exception e) {
            e.printStackTrace();
            r.TotalMemoryInTerminal = 0;
        }
        index += 4;
        try {
            r.FreeMemoryInTerminal = Integer.parseInt(msg.substring(index, index + 4));
        } catch (Exception e) {
            e.printStackTrace();
            r.FreeMemoryInTerminal = 0;
        }
        index += 4;
        r.TerminalType = r.setTerminalType(msg.substring(index, index + 4));
        index += 4;
        try {
            r.NumAppsInTerminal = Integer.parseInt(msg.substring(index, index + 2));
        } catch (Exception e) {
            e.printStackTrace();
            r.NumAppsInTerminal = 0;
        }
        index += 2;
        try {
            r.NumLinesOnDisplay = Integer.parseInt(msg.substring(index, index + 2));
        } catch (Exception e) {
            e.printStackTrace();
            r.NumLinesOnDisplay = 0;
        }
        index += 2;
        SimpleDateFormat sf = new SimpleDateFormat("ddMMyy");
        try {
            r.HardwareInceptionDate = sf.parse(msg.substring(index, index + 6));
        } catch (Exception e) {
            e.printStackTrace();
            r.HardwareInceptionDate = Date.from(Instant.now());
        }
        return r;
    }

    private EFTResponse ParseDisplayResponseType(String msg) {
        int index = 6;
        EFTDisplayResponse r = new EFTDisplayResponse();
        r.NumberOfLines = Integer.parseInt(msg.substring(index, index + 2));
        index += 2;
        r.LineLength = Integer.parseInt(msg.substring(index, index + 2));
        index += 2;
        for (int i = 0; i < r.NumberOfLines; i++) {
            r.DisplayText[i] = msg.substring(index, index + r.LineLength);
            index += r.LineLength;
        }
        r.CancelKeyFlag = (msg.substring(index, index + 1).equals("1"));
        index++;
        r.AcceptYesKeyFlag = (msg.substring(index, index + 1).equals("1"));
        index++;
        r.DeclineNoKeyFlag = (msg.substring(index, index + 1).equals("1"));
        index++;
        r.AuthoriseKeyFlag = (msg.substring(index, index + 1).equals("1"));
        index++;
        r.Input = r.setInput(msg.charAt(index));
        index++;
        r.OKKeyFlag = (msg.substring(index, index + 1).equals("1"));
        index += 3;
        r.Graphic = r.setGraphic(msg.charAt(index));
        index++;
        int padLength = Integer.parseInt(msg.substring(index, index + 3));
        index += 3;
        r.PurchaseAnalysisData = msg.substring(index, index + padLength);
        return r;

    }

    private EFTResponse ParseTransactionResponse(String msg) {
        int index = 6;
        EFTTransactionResponse r = new EFTTransactionResponse();
        r.setSucces(msg.charAt(index));
        index++;
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        r.Merchant = msg.substring(index, index + 2);
        index += 2;
        r.TxnType = r.setTxnType(msg.charAt(index));
        index++;
        r.CardAccountType = r.setCardAccountType(msg.substring(index, index + 7));
        index += 7;
        try {
            r.AmtCash = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.AmtCash = 0d;
        }
        index += 9;
        try {
            r.AmtPurchase = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.AmtPurchase = 0d;
        }
        index += 9;
        try {
            r.AmtTip = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.AmtTip = 0d;
        }
        index += 9;
        try {
            r.AuthCode = Integer.parseInt(msg.substring(index, index + 6));
        } catch (Exception e) {
            e.printStackTrace();
            r.AuthCode = 0;
        }
        index += 6;
        r.TxnRef = msg.substring(index, index + 16);
        index += 16;
        try {
            r.Stan = Integer.parseInt(msg.substring(index, index + 6).trim());
        } catch (Exception e) {
            e.printStackTrace();
            r.Stan = 0;
        }
        index += 6;
        r.Caid = msg.substring(index, index + 15);
        index += 15;
        r.Catid = msg.substring(index, index + 8);
        index += 8;
        r.DateExpiry = msg.substring(index, index + 4);
        index += 4;
        r.SettlementDate = r.setSettlementDate(msg.substring(index, index + 4));
        index += 4;
        r.BankDate = r.setBankDate(msg.substring(index, index + 12));
        index += 12;
        r.CardType = msg.substring(index, index + 20);
        index += 20;
        r.Pan = msg.substring(index, index + 20);
        index += 20;
        r.Track2 = msg.substring(index, index + 40);
        index += 40;
        r.RRN = msg.substring(index, index + 12);
        index += 12;
        try {
            r.CardName = Integer.parseInt(msg.substring(index, index + 2));
        } catch (Exception e) {
            e.printStackTrace();
            r.CardName = 0;
        }
        index += 2;
        r.TxnFlags = new TxnFlags(msg.substring(index, index + 8).toCharArray());
        index += 8;
        r.BalanceReceived = msg.substring(index, index + 1).equals("1");
        index++;
        try {
            r.AvailableBalance = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.AvailableBalance = 0d;
        }
        index += 9;
        try {
            r.ClearedFundsBalance = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            e.printStackTrace();
            r.ClearedFundsBalance = 0d;
        }
        index += 9;
        r.PurchaseAnalysisData = msg.substring(index).trim();
        return r;
    }

    private EFTResponse ParseLogonResponse(String msg) {
        int index = 6;
        EFTLogonResponse r = new EFTLogonResponse();
        r.setSucces(msg.charAt(index));
        index++;
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        if (msg.length() > 25) {
            DateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
            r.Catid = msg.substring(index, index + 8);
            index += 8;
            r.Caid = msg.substring(index, index + 15);
            index += 15;
            try {
                r.BankDate = df.parse(msg.substring(index, index + 12));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            index += 12;
            try {
                r.Stan = Integer.parseInt(msg.substring(index, index + 6));
            } catch (Exception e) {
                e.printStackTrace();
                r.Stan = 0;
            }
            index += 6;
            r.PinPadVersion = msg.substring(index, index + 16);
            index += 16;
            r.PurchaseAnalysisData = msg.substring(index, msg.length() - index).trim();
        }
        return r;
    }

    private EFTResponse ParseReceiptResponse(String msg) {
        int index = 5;
        EFTReceiptResponse r = new EFTReceiptResponse();
        try {
            r.Receipt = ParseReceiptType(msg.charAt(index));
            index++;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (r.Receipt != EFTReceiptRequest.ReceiptType.ReceiptText) {
            r.setPrePrint(true);
        } else {
            List<String> receiptLines = new ArrayList<>();
            boolean done = false;
            while (!done) {
                int lineLength = msg.substring(index).indexOf("\r\n");
                if (lineLength > 0) {
                    receiptLines.add(msg.substring(index, index + lineLength));
                    index += lineLength + 2;
                    if (index >= msg.length()) {
                        done = true;
                    }
                } else
                    done = true;
            }
            r.ReceiptText = receiptLines.toArray(r.ReceiptText);
        }
        return r;
    }

    private IPClientResponseType ParseResponseType(char c) {
        switch (c) {
            case 'G':
                return IPClientResponseType.Logon;
            case 'M':
                return IPClientResponseType.Transaction;
            case 'J':
                return IPClientResponseType.QueryCard;
            case '1':
                return IPClientResponseType.Configure;
            case '5':
                return IPClientResponseType.ControlPanel;
            case '2':
                return IPClientResponseType.SetDialog;
            case 'P':
                return IPClientResponseType.Settlement;
            case 'C':
                return IPClientResponseType.DuplicateReceipt;
            case 'N':
                return IPClientResponseType.GetLastTransaction;
            case 'K':
                return IPClientResponseType.Status;
            case '3':
                return IPClientResponseType.Receipt;
            case 'S':
                return IPClientResponseType.Display;
            case 'X':
                return IPClientResponseType.GenericPOSCommand;

            case 'W':
                return IPClientResponseType.PinRequest;

            case 'H':
                return IPClientResponseType.ChequeAuth;

            case 'Y':
                return IPClientResponseType.SendKey;

            case 'Q':
                return IPClientResponseType.ClientList;

            case 'A':
                return IPClientResponseType.CloudLogon;

            case '0':
                return IPClientResponseType.PinPadBusy;

            default:
                throw new IllegalArgumentException("No valid response type");
        }

    }

    //endregion

    //region EFTRequest to String

    String EFTRequestToString(EFTRequest eftRequest) {
        //Build the request string.
        StringBuilder request = BuildRequest(eftRequest);
        int len = request.length() + 5;
        request.insert(0, '#');
        request.insert(1, String.format("%04d", len));
        return request.toString();
    }

    private StringBuilder BuildRequest(EFTRequest eftRequest) {
        if (eftRequest instanceof EFTLogonRequest) {
            return BuildEFTLogonRequest((EFTLogonRequest) eftRequest);
        } else if (eftRequest instanceof EFTReceiptRequest) {
            return BuildEFTReceiptRequest();
        } else if (eftRequest instanceof EFTTransactionRequest) {
            return BuildEFTTransactionRequest((EFTTransactionRequest) eftRequest);
        } else if (eftRequest instanceof EFTSendKeyRequest) {
            return BuildSendKeyRequest((EFTSendKeyRequest) eftRequest);
        } else if (eftRequest instanceof EFTStatusRequest) {
            return BuildStatusRequest((EFTStatusRequest) eftRequest);
        } else if (eftRequest instanceof EFTSettlementRequest) {
            return BuildSettlementRequest((EFTSettlementRequest) eftRequest);
        } else if (eftRequest instanceof EFTReprintReceiptRequest) {
            return BuildEFTReprintReceiptRequest((EFTReprintReceiptRequest) eftRequest);
        } else if (eftRequest instanceof EFTGetLastTransactionRequest) {
            return BuildEFTGetLastTransactionRequest((EFTGetLastTransactionRequest) eftRequest);
        } else if (eftRequest instanceof EFTSetDialogRequest) {
            return BuildEFTSetDialogRequest((EFTSetDialogRequest) eftRequest);
        } else {
            return new StringBuilder();
        }
    }

    private StringBuilder BuildEFTSetDialogRequest(EFTSetDialogRequest v) {
        StringBuilder r = new StringBuilder();
        r.append('2');
        r.append(v.DisableDisplayEvents ? '5' : ' ');
        r.append(v.dialogType.Type);
        r.append(PadLeftAsInt(v.DialogX));
        r.append(PadLeftAsInt(v.DialogY));
        r.append(PadRightAndCut(v.dialogPosition.toString(), 12));
        r.append(v.EnableTopmost ? '1' : '0');
        r.append(PadRightAndCut(v.DialogTitle, 32));
        return r;
    }

    private StringBuilder BuildEFTReprintReceiptRequest(EFTReprintReceiptRequest v) {
        StringBuilder r = new StringBuilder();
        r.append('C');
        r.append(v.ReprntType.PrintType);
        r.append(PadRightAndCut(v.Merchant, 2));
        r.append(v.ReceiptCutMode.CutType);
        r.append(v.ReceiptPrintMode.PrintType);
        r.append(v.Application.Application);
        return r;
    }

    private StringBuilder BuildEFTGetLastTransactionRequest(EFTGetLastTransactionRequest v) {
        StringBuilder r = new StringBuilder();
        r.append('N');
        r.append('0');
        r.append(v.Application.Application);
        return r;
    }

    private StringBuilder BuildSettlementRequest(EFTSettlementRequest v) {
        StringBuilder r = new StringBuilder();
        r.append('P');
        r.append(v.SettleType.SettleType);
        r.append(PadRightAndCut(v.Merchant, 2));
        r.append(v.ReceiptPrintMode.PrintType);
        r.append(v.ReceiptCutMode.CutType);
        r.append(v.ResetTotals ? '1' : '0');
        r.append(v.Application.Application);
        r.append(v.PurchaseAnalysisData);
        return r;
    }

    private StringBuilder BuildStatusRequest(EFTStatusRequest v) {
        StringBuilder r = new StringBuilder();
        r.append('K');
        r.append(v.StatType.StatType);
        r.append(v.Merchant);
        r.append(v.Application.Application);
        return r;
    }

    private StringBuilder BuildEFTTransactionRequest(EFTTransactionRequest v) {
        StringBuilder r = new StringBuilder();
        DateFormat df = new SimpleDateFormat("ddMMyy");
        DateFormat dateFormat = new SimpleDateFormat("HHmmss");
        r.append('M');
        r.append('0');
        r.append(v.Merchant);
        r.append(v.TxnType.TxnType);
        r.append(v.TrainingMode ? '1' : '0');
        r.append(v.EnableTip ? '1' : '0');
        r.append(PadLeftAsInt(v.AmtCash));
        r.append(PadLeftAsInt(v.AmtPurchase));
        r.append(String.format("%06d", v.AuthCode));
        r.append(PadRightAndCut(v.TxnRef, 16));
        r.append(v.ReceiptPrintMode.PrintType);
        r.append(v.ReceiptCutMode.CutType);
        r.append(v.PanSrc.PanSource);
        r.append(PadRightAndCut(v.Pan, 20));
        r.append(PadRightAndCut(v.DateExpiry, 4));
        r.append(PadRightAndCut(v.Track2, 40));
        r.append(v.CardAccountType.AccountType);
        r.append(v.Application.Application);
        r.append(PadRightAndCut(v.RRN, 12));
        r.append(PadRightAndCut(v.CurrencyCode, 3));
        r.append(v.OriginalTxnType.TxnType);
        r.append((v.BankDate != null) ? df.format(v.BankDate) : "      ");
        r.append((v.Time != 0) ? dateFormat.format(v.Time) : "      ");
        r.append(PadRightAndCut(" ", 8));
        r.append(v.PurchaseAnalysisData);

        return r;
    }

    private StringBuilder BuildEFTReceiptRequest() {
        return new StringBuilder("3 ");
    }

    private StringBuilder BuildEFTLogonRequest(EFTLogonRequest v) {
        StringBuilder r = new StringBuilder();
        r.append('G');
        r.append(v.logonType.logType);
        r.append(v.Merchant);
        r.append(v.ReceiptPrintMode.PrintType);
        r.append(v.ReceiptCutMode.CutType);
        r.append(v.Application);
        r.append(v.PurchaseAnalysisData);
        return r;
    }

    private StringBuilder BuildSendKeyRequest(EFTSendKeyRequest v) {
        StringBuilder r = new StringBuilder();
        r.append("Y0");
        r.append(v.Key.Key);
        if (v.getKey() == EFTSendKeyRequest.EFTPOSKey.Authorise && !v.getData().equals("")) {
            r.append(PadRightAndCut(v.getData(), 20));
        }
        return r;
    }

    //endregion

    //region Tools
    private EnumSet<EFTStatusRequest.PinpadOptionFlags> ParsePinpadOptions(char[] Flags) {
        EnumSet<EFTStatusRequest.PinpadOptionFlags> flags = EnumSet.noneOf(EFTStatusRequest.PinpadOptionFlags.class);
        int index = 0;
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.Tipping);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.PreAuth);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.Completions);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.CashOut);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.Refund);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.Balance);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.Deposit);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.Voucher);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.MOTO);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.AutoCompletion);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.EFB);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.EMV);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.Training);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.Withdrawal);
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.Transfer);
        if (Flags[index] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.StartCash);
        return flags;
    }

    private EFTReceiptRequest.ReceiptType ParseReceiptType(char c) {
        switch (c) {
            case 'L':
                return EFTReceiptRequest.ReceiptType.Logon;
            case 'C':
                return EFTReceiptRequest.ReceiptType.Customer;
            case 'M':
                return EFTReceiptRequest.ReceiptType.Merchant;
            case 'S':
                return EFTReceiptRequest.ReceiptType.Settlement;
            case 'R':
                return EFTReceiptRequest.ReceiptType.ReceiptText;
            default:
                throw new IllegalArgumentException("Invalid receipt type");
        }
    }

    private static String PadLeftAsInt(double v) {
        return String.format("%09d", (int) (v * 100));
    }

    private static String PadLeftAsInt(int v) {
        return String.format("%04d", v);
    }

    private static String PadRightAndCut(String v, int totalWidth) {
        if (v.length() == totalWidth)
            return v;
        else if (v.length() < totalWidth)
            return String.format("%1$-" + totalWidth + "s", v);
        else return v.substring(0, totalWidth);
    }

    //endregion
}