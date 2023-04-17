package pceft.sdk.eftclient.java;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

//TODO: [STRY0229669] Replace inline magic numbers for field lengths with consts

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
        CloudResponse('A');

        public final char ResponseType;

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

    private EFTResponse StringToEFTResponse(String msg) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (msg.length() < 1) {
            System.out.println("msg is null or zero length");
            return null;
        }
        IPClientResponseType responseType = ParseResponseType(msg.charAt(5));
        switch (responseType) {
            case Display:            return ParseDisplayResponseType(msg);
            case Receipt:            return ParseReceiptResponse(msg);
            case Logon:              return ParseLogonResponse(msg);
            case Transaction:        return ParseTransactionResponse(msg);
            case SetDialog:          return ParseSetDialogResponse(msg);
            case GetLastTransaction: return ParseGetLastTransactionResponse(msg);
            case DuplicateReceipt:   return ParseEFTReprintReceiptResponse(msg);
            case ControlPanel:       return ParseControlPanelResponse(msg);
            case Settlement:         return ParseSettlementResponse(msg);
            case Status:             return ParseStatusResponse(msg);
            case PinPadBusy:         return ParsePinpadBusyResponse(msg);
            case ChequeAuth:         return ParseChequeAuthResponse(msg);
            case QueryCard:          return ParseQueryCardResponse(msg);
            case GenericPOSCommand:  return ParseGenericPOSCommandResponse(msg);
            case Configure:          return ParseConfigMerchantResponse(msg);
            case CloudResponse:      return ParseCloudResponse(msg);
            case ClientList:         return ParseClientListResponse(msg);
            default:
                System.out.printf("Unknown message type: %s%n", responseType);
                throw new IllegalArgumentException(String.format("Unknown message type: %s", responseType));
        }
    }

    private EFTResponse ParseConfigMerchantResponse(String msg) throws IndexOutOfBoundsException {
        int index = 7;
        EFTConfigureMerchantResponse r = new EFTConfigureMerchantResponse();
        r.setSuccessFlag(msg.charAt(index));
        index++;
        r.setResponseCode(msg.substring(index, index + 2));
        index += 2;
        r.setResponseText(msg.substring(index, index + 20));
        return r;
    }

    private EFTResponse ParseChequeAuthResponse(String msg) throws IndexOutOfBoundsException {
        EFTChequeAuthResponse r = new EFTChequeAuthResponse();
        int index = 7;
        r.SuccessFlag = msg.charAt(index) == '1';
        index++;
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        r.Merchant = msg.substring(index, index + 2);
        index += 2;
        try { r.AmtPurchase = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.AuthCode =  msg.substring(index, index + 6); } catch (Exception ignored) { }
        index += 6;
        try { r.ReferenceCode = msg.substring(index, index + 16); } catch (Exception ignored) { }
        return r;
    }

    private EFTResponse ParseClientListResponse(String msg) throws IndexOutOfBoundsException {
        int index = 7;
        EFTClientListResponse r = new EFTClientListResponse();
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.setResponseText(msg.substring(index, index + 20));
        // TODO: Convert msg.Response to ClientDesc Class
        return r;
    }

    private EFTResponse ParseCloudPairResponse(String msg) throws IndexOutOfBoundsException {
        int index = 7;
        EFTCloudPairResponse r = new EFTCloudPairResponse();
        r.setSuccessFlag(msg.charAt(index++));
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        //Removing RedirectPort/RedirectAddress as it is no longer in spec

        //TOKEN
        if (msg.length() - index >= 3) {
            int len = Integer.parseInt(msg.substring(index, index + 3));
            index += 3;

            if (msg.length() - index != len) {
                System.out.println("Unexpected token length");
            }

            r.setToken(msg.substring(index, index + len));
        }

        return r;
    }

    private EFTResponse ParseCloudLogonResponse(String msg) throws IndexOutOfBoundsException {
        int index = 7;
        EFTCloudLogonResponse r = new EFTCloudLogonResponse();
        r.setSuccessFlag(msg.charAt(index));
        index++;
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        return r;
    }

    private EFTResponse ParseCloudTokenLogonResponse(String msg) throws IndexOutOfBoundsException {
        int index = 7;
        EFTCloudTokenLogonResponse r = new EFTCloudTokenLogonResponse();
        r.setSuccessFlag(msg.charAt(index));
        index++;
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        return r;
    }

    private EFTResponse ParseCloudResponse(String msg) throws IndexOutOfBoundsException {
        char subcodeType = msg.charAt(6);
        // Scenario - Based on sub code whether LOGON, TOKEN LOGON or PAIR RESPONSE
        switch (subcodeType) {
            case 'T': return ParseCloudTokenLogonResponse(msg); // TOKEN RESPONSE
            case 'P': return ParseCloudPairResponse(msg);       // PAIR RESPONSE
            default:  return ParseCloudLogonResponse(msg);
        }
    }

    private EFTResponse ParseQueryCardResponse(String msg) throws IndexOutOfBoundsException, IllegalArgumentException {
        int index = 6;
        EFTQueryCardResponse r = new EFTQueryCardResponse();
        r.setAccountType(ParseAccountType(msg.charAt(index)));
        index++;
        r.SuccessFlag = msg.charAt(index) == '1';
        index++;
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        if (msg.length() >= 153) {
            r.setTrack2(msg.substring(index, index + 40));
            index += 40;
            r.setTrack1_Track3(msg.substring(index, index + 80));
            index += 80;
            r.setTracksRead(ParseTracksRead(msg.charAt(index)));
            index++;
            r.setBinNumber(msg.substring(index, index + 2));
            index += 2;
            r.setPurchaseAnalysisData(ParsePurchaseAnalysisData(msg, index));
        }
        return r;
    }

    private EFTResponse ParseGenericPOSCommandResponse(String msg) throws IndexOutOfBoundsException {
        int index = 6;
        EFTGenericCommandResponse r = new EFTGenericCommandResponse();
        //PARSE RESPONSE ACCORDING TO SUB CODE
        r.CommandType.SubCodeType = msg.charAt(index);
        index++;
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        switch (r.CommandType.SubCodeType) {
            case 'Z':
                r.SlaveResponse = msg.substring(index);
                break;
            case '%':
                r.ResponseText = msg.substring(index);
                break;
            case '2':
                r.ResponseText = msg.substring(index, index + 20);
                index += 20;
                if (msg.length() > index) {
                    try {
                        int pwdLength = Integer.parseInt(msg.substring(index, index + 2));
                        index += 2;
                        r.Password = msg.substring(index, index + pwdLength);
                    } catch (Exception ignored) { }
                }
                break;
            case '@':
                //ACCORDING TO C#
                try {
                    index = 22;
                    int headerLength = Integer.parseInt(msg.substring(index, index + 6));
                    index += 6;
                    r.Header = msg.substring(index, index + headerLength);
                    index += headerLength;
                    r.Content = msg.substring(index);
                } catch (Exception ignored) { }
                break;
            default:
                r.ResponseText = msg.substring(index, index + 20);
                index += 20;
                r.Data = msg.substring(index);
                break;
        }
        if (r.ResponseText.trim().equalsIgnoreCase("APPROVED")) {
            r.SuccessFlag = true;
        }
        return r;
    }


    private EFTResponse ParsePinpadBusyResponse(String ignoredMsg) {
        return new EFTPinpadBusyResponse();
    }

    private EFTResponse ParseSetDialogResponse(String msg) throws IndexOutOfBoundsException {
        int index = 7;
        EFTSetDialogResponse r = new EFTSetDialogResponse();
        r.Success = msg.charAt(index) == '1';

        return r;
    }

    private EFTResponse ParseControlPanelResponse(String msg) throws IndexOutOfBoundsException {
        int index = 7;
        EFTControlPanelResponse r = new EFTControlPanelResponse();

        r.Success = msg.charAt(index) == '1';
        index++;
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        return r;
    }

    private EFTResponse ParseEFTReprintReceiptResponse(String msg) throws IndexOutOfBoundsException {
        EFTReprintReceiptResponse r = new EFTReprintReceiptResponse();
        int index = 7;
        r.Success = msg.charAt(index) == '1';
        index++;
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

    private EFTResponse ParseGetLastTransactionResponse(String msg) throws IndexOutOfBoundsException, IllegalArgumentException {
        EFTGetLastTransactionResponse r = new EFTGetLastTransactionResponse();
        int index = 7;
        r.setSuccess(msg.charAt(index));
        index++;
        r.LastTransactionSuccess = msg.charAt(index) == '1';
        index++;
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
        r.setTxnType(ParseTxnType(msg.charAt(index)));
        index++;
        try { r.setCardAccountType(ParseCardAccountType(msg.substring(index, index + 7))); } catch (Exception ignored) { }
        index += 7;
        try { r.AmtCash = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.AmtPurchase = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.AmtTip = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.AuthCode = Integer.parseInt(msg.substring(index, index + 6)); } catch (Exception ignored) { }
        index += 6;
        try { r.TxnRef = msg.substring(index, index + 16); } catch (Exception ignored) { }
        index += 16;
        try { r.Stan = Integer.parseInt(msg.substring(index, index + 6).trim()); } catch (Exception ignored) { }
        index += 6;
        try { r.Caid = msg.substring(index, index + 15); } catch (Exception ignored) { }
        index += 15;
        try { r.Catid = msg.substring(index, index + 8); } catch (Exception ignored) { }
        index += 8;
        try { r.DateExpiry = msg.substring(index, index + 4); } catch (Exception ignored) { }
        index += 4;
        try { r.SettlementDate = r.setSettlementDate(msg.substring(index, index + 4)); } catch (Exception ignored) { }
        index += 4;
        try { r.BankDate = r.setBankDate(msg.substring(index, index + 12)); } catch (Exception ignored) { }
        index += 12;
        try { r.CardType = msg.substring(index, index + 20); } catch (Exception ignored) { }
        index += 20;
        try { r.Pan = msg.substring(index, index + 20); } catch (Exception ignored) { }
        index += 20;
        try { r.Track2 = msg.substring(index, index + 40); } catch (Exception ignored) { }
        index += 40;
        try { r.RRN = msg.substring(index, index + 12); } catch (Exception ignored) { }
        index += 12;
        try { r.CardName = Integer.parseInt(msg.substring(index, index + 2)); } catch (Exception ignored) { }
        index += 2;
        try { r.TxnFlags = new TxnFlags(msg.substring(index, index + 8).toCharArray()); } catch (Exception ignored) { }
        index += 8;
        try { r.BalanceReceived = msg.charAt(index) == '1'; } catch (Exception ignored) { }
        index++;
        try { r.AvailableBalance = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.ClearedFundsBalance = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        r.setPurchaseAnalysisData(ParsePurchaseAnalysisData(msg, index));

        return r;
    }

    private EFTResponse ParseSettlementResponse(String msg) throws IndexOutOfBoundsException {
        EFTSettlementResponse r = new EFTSettlementResponse();
        int index = 7;
        r.Success = msg.charAt(index++) == '1';
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        try { r.SettlementData = msg.substring(index); } catch (Exception ignored) { }
        return r;
    }

    private EFTResponse ParseStatusResponse(String msg) throws IndexOutOfBoundsException {
        final SimpleDateFormat sf = new SimpleDateFormat("ddMMyy");
        int index = 7;
        EFTStatusResponse r = new EFTStatusResponse();
        r.Success = msg.charAt(index) == '1';
        index++;
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        try { r.Merchant = msg.substring(index, index + 2); } catch (Exception ignored) { }
        index += 2;
        try { r.AIIC = msg.substring(index, index + 11); } catch (Exception ignored) { }
        index += 11;
        try { r.NII = Integer.parseInt(msg.substring(index, index + 3)); } catch (Exception ignored) { }
        index += 3;
        try { r.Caid = msg.substring(index, index + 15); } catch (Exception ignored) { }
        index += 15;
        try { r.Catid = msg.substring(index, index + 8); } catch (Exception ignored) { }
        index += 8;
        try { r.Timeout = Integer.parseInt(msg.substring(index, index + 3)); } catch (Exception ignored) { }
        index += 3;
        try { r.LoggedOn = msg.charAt(index) == '1'; } catch (Exception ignored) { }
        index++;
        try { r.PinpadSerialNumber = msg.substring(index, index + 16); } catch (Exception ignored) { }
        index += 16;
        try { r.PinpadVersion = msg.substring(index, index + 16); } catch (Exception ignored) { }
        index += 16;
        try { r.BankDescription = msg.substring(index, index + 32); } catch (Exception ignored) { }
        index += 32;
        //Skip data length
        index += 3;
        try { r.SAFCount = Integer.parseInt(msg.substring(index, index + 4)); } catch (Exception ignored) { }
        index += 4;
        try { r.setNetType(ParseNetType(msg.charAt(index))); } catch (Exception ignored) { }
        index++;
        try { r.HardwareSerial = msg.substring(index, index + 16); } catch (Exception ignored) { }
        index += 16;
        try { r.RetailerName = msg.substring(index, index + 40); } catch (Exception ignored) { }
        index += 40;
        try { r.OptionsFlags = ParsePinpadOptions(msg.substring(index, index + 32).toCharArray()); } catch (Exception ignored) { }
        index += 32;
        try { r.SAFCreditLimit = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.SAFDebitLimit = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.MaxSAF = Integer.parseInt(msg.substring(index, index + 3)); } catch (Exception ignored) { }
        index += 3;
        try { r.KeyHandlingScheme = r.setKeyHandlingScheme(msg.charAt(index)); } catch (Exception ignored) { }
        index++;
        try { r.CashoutLimit = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.RefundLimit = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.CPATVersion = msg.substring(index, index + 6); } catch (Exception ignored) { }
        index += 6;
        try { r.NameTableVersion = msg.substring(index, index + 6); } catch (Exception ignored) { }
        index += 6;
        try { r.TerminalCommType = r.setTerminalCommsType(msg.charAt(index)); } catch (Exception ignored) { }
        index++;
        try { r.CardMisreadCount = Integer.parseInt(msg.substring(index, index + 6)); } catch (Exception ignored) { }
        index += 6;
        try { r.TotalMemoryInTerminal = Integer.parseInt(msg.substring(index, index + 4)); } catch (Exception ignored) { }
        index += 4;
        try { r.FreeMemoryInTerminal = Integer.parseInt(msg.substring(index, index + 4)); } catch (Exception ignored) { }
        index += 4;
        try { r.TerminalType = r.setTerminalType(msg.substring(index, index + 4)); } catch (Exception ignored) { }
        index += 4;
        try { r.NumAppsInTerminal = Integer.parseInt(msg.substring(index, index + 2)); } catch (Exception ignored) { }
        index += 2;
        try { r.NumLinesOnDisplay = Integer.parseInt(msg.substring(index, index + 2)); } catch (Exception ignored) { }
        index += 2;
        try { r.HardwareInceptionDate = sf.parse(msg.substring(index, index + 6)); } catch (Exception ignored) { }
        return r;
    }

    private EFTResponse ParseDisplayResponseType(String msg) throws IndexOutOfBoundsException {
        int index = 7;
        EFTDisplayResponse r = new EFTDisplayResponse();
        try {
            r.NumberOfLines = Integer.parseInt(msg.substring(index, index + 2));
        } catch (Exception e) {
            r.NumberOfLines = 2;
        }
        index += 2;
        try {
            r.LineLength = Integer.parseInt(msg.substring(index, index + 2));

        } catch (Exception e) {
            r.LineLength = 20;
        }
        index += 2;
        for (int i = 0; i < r.NumberOfLines; i++) {
            r.DisplayText[i] = msg.substring(index, index + r.LineLength);
            index += r.LineLength;
        }
        r.CancelKeyFlag = (msg.charAt(index) == '1');
        index++;
        r.AcceptYesKeyFlag = (msg.charAt(index) == '1');
        index++;
        r.DeclineNoKeyFlag = (msg.charAt(index) == '1');
        index++;
        r.AuthoriseKeyFlag = (msg.charAt(index) == '1');
        index++;
        r.Input = r.setInput(msg.charAt(index));
        index++;
        r.OKKeyFlag = (msg.charAt(index) == '1');
        index += 3;
        r.Graphic = r.setGraphic(msg.charAt(index));
        index++;
        r.setPurchaseAnalysisData(ParsePurchaseAnalysisData(msg, index));
        return r;
    }

    private EFTResponse ParseTransactionResponse(String msg) throws IndexOutOfBoundsException, IllegalArgumentException {
        int index = 7;
        EFTTransactionResponse r = new EFTTransactionResponse();
        r.setSuccess(msg.charAt(index));
        index++;
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        r.Merchant = msg.substring(index, index + 2);
        index += 2;
        r.setTxnType(ParseTxnType(msg.charAt(index)));
        index++;
        try { r.setCardAccountType(ParseCardAccountType(msg.substring(index, index + 7))); } catch (Exception ignored) { }
        index += 7;
        try { r.AmtCash = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.AmtPurchase = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.AmtTip = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.AuthCode = Integer.parseInt(msg.substring(index, index + 6)); } catch (Exception ignored) { }
        index += 6;
        try { r.TxnRef = msg.substring(index, index + 16); } catch (Exception ignored) { }
        index += 16;
        try { r.Stan = Integer.parseInt(msg.substring(index, index + 6).trim()); } catch (Exception ignored) { }
        index += 6;
        try { r.Caid = msg.substring(index, index + 15); } catch (Exception ignored) { }
        index += 15;
        try { r.Catid = msg.substring(index, index + 8); } catch (Exception ignored) { }
        index += 8;
        try { r.DateExpiry = msg.substring(index, index + 4); } catch (Exception ignored) { }
        index += 4;
        try { r.SettlementDate = r.setSettlementDate(msg.substring(index, index + 4)); } catch (Exception ignored) { }
        index += 4;
        try { r.BankDate = r.setBankDate(msg.substring(index, index + 12)); } catch (Exception ignored) { }
        index += 12;
        try { r.CardType = msg.substring(index, index + 20); } catch (Exception ignored) { }
        index += 20;
        try { r.Pan = msg.substring(index, index + 20); } catch (Exception ignored) { }
        index += 20;
        try { r.Track2 = msg.substring(index, index + 40); } catch (Exception ignored) { }
        index += 40;
        try { r.RRN = msg.substring(index, index + 12); } catch (Exception ignored) { }
        index += 12;
        try { r.CardName = Integer.parseInt(msg.substring(index, index + 2)); } catch (Exception ignored) { }
        index += 2;
        try { r.TxnFlags = new TxnFlags(msg.substring(index, index + 8).toCharArray()); } catch (Exception ignored) { }
        index += 8;
        try { r.BalanceReceived = msg.charAt(index) == '1'; } catch (Exception ignored) { }
        index++;
        try { r.AvailableBalance = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        try { r.ClearedFundsBalance = Double.parseDouble(msg.substring(index, index + 9)) / 100; } catch (Exception ignored) { }
        index += 9;
        r.setPurchaseAnalysisData(ParsePurchaseAnalysisData(msg, index));

        return r;
    }

    private EFTResponse ParseLogonResponse(String msg) throws IndexOutOfBoundsException {
        int index = 7;
        DateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
        EFTLogonResponse r = new EFTLogonResponse();

        r.setSuccess(msg.charAt(index));
        index++;
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        index += 20;
        try { r.Catid = msg.substring(index, index + 8); } catch (Exception ignored) { }
        index += 8;
        try { r.Caid = msg.substring(index, index + 15); } catch (Exception ignored) { }
        index += 15;
        try { r.BankDate = df.parse(msg.substring(index, index + 12)); } catch (Exception ignored) { }
        index += 12;
        try { r.Stan = Integer.parseInt(msg.substring(index, index + 6)); } catch (Exception ignored) { }
        index += 6;
        try { r.PinPadVersion = msg.substring(index, index + 16); } catch(Exception ignored) { }
        index += 16;
        r.setPurchaseAnalysisData(ParsePurchaseAnalysisData(msg, index));

        return r;
    }

    private EFTResponse ParseReceiptResponse(String msg) throws IndexOutOfBoundsException {
        int index = 6;
        EFTReceiptResponse r = new EFTReceiptResponse();
        try { r.Receipt = ParseReceiptType(msg.charAt(index)); } catch (IllegalArgumentException ignored) { }
        index++;
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

    private IPClientResponseType ParseResponseType(char c) throws IllegalArgumentException {
        switch (c) {
            case 'G': return IPClientResponseType.Logon;
            case 'M': return IPClientResponseType.Transaction;
            case 'J': return IPClientResponseType.QueryCard;
            case '1': return IPClientResponseType.Configure;
            case '5': return IPClientResponseType.ControlPanel;
            case '2': return IPClientResponseType.SetDialog;
            case 'P': return IPClientResponseType.Settlement;
            case 'C': return IPClientResponseType.DuplicateReceipt;
            case 'N': return IPClientResponseType.GetLastTransaction;
            case 'K': return IPClientResponseType.Status;
            case '3': return IPClientResponseType.Receipt;
            case 'S': return IPClientResponseType.Display;
            case 'X': return IPClientResponseType.GenericPOSCommand;
            case 'W': return IPClientResponseType.PinRequest;
            case 'H': return IPClientResponseType.ChequeAuth;
            case 'Y': return IPClientResponseType.SendKey;
            case 'Q': return IPClientResponseType.ClientList;
            case 'A': return IPClientResponseType.CloudResponse;
            case '0': return IPClientResponseType.PinPadBusy;
            default:  throw new IllegalArgumentException("No valid response type");
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
        } else if (eftRequest instanceof EFTDisplayRequest) {
            return BuildEFTDisplayRequest((EFTDisplayRequest) eftRequest);
        } else if (eftRequest instanceof EFTGenericCommandRequest) {
            return BuildEFTGenericCommandRequest((EFTGenericCommandRequest) eftRequest);
        } else if (eftRequest instanceof EFTControlPanelRequest) {
            return BuildEFTControlPanelRequest((EFTControlPanelRequest) eftRequest);
        } else if (eftRequest instanceof EFTQueryCardRequest) {
            return BuildEFTQueryCardRequest((EFTQueryCardRequest) eftRequest);
        } else if (eftRequest instanceof EFTClientListRequest) {
            return BuildEFTClientListRequest((EFTClientListRequest) eftRequest);
        } else if (eftRequest instanceof EFTChequeAuthRequest) {
            return BuildEFTChequeAuthRequest((EFTChequeAuthRequest) eftRequest);
        } else if (eftRequest instanceof EFTConfigureMerchantRequest) {
            return BuildEFTConfigureMerchantRequest((EFTConfigureMerchantRequest) eftRequest);
        } else if (eftRequest instanceof EFTCloudPairRequest) {
            return BuildEFTCloudPairRequest((EFTCloudPairRequest) eftRequest);
        } else if (eftRequest instanceof EFTCloudTokenLogonRequest) {
            return BuildEFTCloudTokenLogonRequest((EFTCloudTokenLogonRequest) eftRequest);
        } else if (eftRequest instanceof EFTCloudLogonRequest) {
            return BuildEFTCloudLogonRequest((EFTCloudLogonRequest) eftRequest);
        } else {
            return new StringBuilder();
        }
    }

    private StringBuilder BuildEFTCloudPairRequest(EFTCloudPairRequest v) {
        StringBuilder r = new StringBuilder();
        //PAD RIGHT TO LENGTH 16
        r.append('A');
        r.append('P');
        r.append(PadRightAndCut(v.ClientID, 16));
        r.append(PadRightAndCut(v.Password, 16));
        r.append(PadRightAndCut(v.PairCode, 16));
        return r;
    }

    private StringBuilder BuildEFTCloudLogonRequest(EFTCloudLogonRequest v) {
        StringBuilder r = new StringBuilder();
        //PAD RIGHT TO LENGTH 16
        r.append('A');
        r.append(' ');
        r.append(PadRightAndCut(v.ClientID, 16));
        r.append(PadRightAndCut(v.Password, 16));
        r.append(PadRightAndCut(v.PairCode, 16));
        return r;
    }

    private StringBuilder BuildEFTCloudTokenLogonRequest(EFTCloudTokenLogonRequest v) {
        StringBuilder r = new StringBuilder();
        r.append('A');
        r.append('T');
        r.append(PadLeft_Length(v.Token.length()));
        r.append(v.Token);
        return r;
    }

    private StringBuilder BuildEFTClientListRequest(EFTClientListRequest ignored) {
        StringBuilder r = new StringBuilder();
        r.append("Q0");
        return r;
    }

    private StringBuilder BuildEFTConfigureMerchantRequest(EFTConfigureMerchantRequest v) {
        StringBuilder r = new StringBuilder();
        r.append("10");
        r.append(v.Merchant);
        r.append(v.AIIC);
        r.append(v.Nii);
        r.append(v.Caid);
        r.append(v.Catid);
        r.append(PadLeft_Length(v.Timeout));
        r.append(v.Application.Application);
        return r;
    }

    private StringBuilder BuildEFTControlPanelRequest(EFTControlPanelRequest v) {
        StringBuilder r = new StringBuilder();
        r.append('5'); //Command Code
        r.append(v.CPanelType.PanelType);
        r.append(v.ReceiptPrintMode.PrintType);
        r.append(v.ReceiptCutMode.CutType);
        r.append(v.CPanelReturnType.ReturnType);
        return r;
    }

    private StringBuilder BuildEFTChequeAuthRequest(EFTChequeAuthRequest v) {
        StringBuilder r = new StringBuilder();
        r.append('H'); //Command Code
        r.append('0');
        r.append(v.Application.Application);
        r.append(' ');
        r.append(v.BranchCode);
        r.append(v.AccountNumber);
        r.append(v.SerialNumber);
        r.append(PadLeftAsInt(v.AmtPurchase));
        r.append(v.ChequeType.chequeAuthType);
        r.append(v.TxnRef);
        return r;
    }

    private StringBuilder BuildEFTQueryCardRequest(EFTQueryCardRequest v) {
        StringBuilder r = new StringBuilder();
        // Append String With Request Vars
        r.append('J');
        r.append(v.AccountType.sAccountType);
        r.append(v.Application);
        r.append(v.Merchant);
        r.append(PadLeft_Length(v.getPurchaseAnalysisData().length()));
        r.append(v.getPurchaseAnalysisData());
        return r;
    }

    private String BuildEFTBasketDataRequest(EFTGenericCommandRequest v) {
        //ATTACHMENT TO GENERIC COMMAND

        String jsonContent = "{}";

        if (v.BasketCommand instanceof EFTBasketDataCommandAdd) {
            EFTBasketDataCommandAdd c = (EFTBasketDataCommandAdd)v.BasketCommand;
            JSONObject json = new JSONObject(c.Basket);
            jsonContent = json.toString(0);
        } else if (v.BasketCommand instanceof EFTBasketDataCommandCreate) {
            EFTBasketDataCommandCreate c = (EFTBasketDataCommandCreate)v.BasketCommand;
            JSONObject json = new JSONObject(c.Basket);
            jsonContent = json.toString(0);
        } else if (v.BasketCommand instanceof EFTBasketDataCommandDelete) {
            EFTBasketDataCommandDelete c = (EFTBasketDataCommandDelete)v.BasketCommand;
            EFTBasket basket = new EFTBasket();
            basket.id = c.BasketId;
            basket.items = new ArrayList<>();
            basket.items.add(new EFTBasketItem(c.BasketItemId));
            JSONObject json = new JSONObject(basket);
            jsonContent = json.toString(0);
        } else if (v.BasketCommand instanceof EFTBasketDataCommandRaw) {
            EFTBasketDataCommandRaw c = (EFTBasketDataCommandRaw)v.BasketCommand;
            jsonContent = c.BasketContent;
        } else {
            System.out.printf("Unknown basket command '%s'%n", v.getClass().getTypeName());
        }

        return PadLeft_BasketLength(jsonContent.length()) + jsonContent;
    }

    private StringBuilder BuildEFTGenericCommandRequest(EFTGenericCommandRequest v) {
        StringBuilder r = new StringBuilder();
        r.append('X');
        r.append(v.CommandType.SubCodeType);
        switch (v.CommandType.SubCodeType) {
            case '0': // Display Data
                r.append(PadLeftAsInt_DoubleDigit(v.NumberOfLines));
                r.append(PadLeft_Length(v.Timeout));
                r.append(v.DisplayMap);
                r.append(PadRightAndCut(v.PinpadKeyMap, 8));
                r.append(PadRightAndCut(v.POSKeyMap, 8));
                r.append(PadLeft_Length(v.LineLength));
                r.append(PadRightAndCut(v.POSDisplayData, 40));
                r.append(v.PinpadLineData);
                break;
            case '1': // Print Data
                r.append(v.NumberOfLines);
                r.append(v.PrinterByteMap);
                r.append(v.LineLength);
                r.append(v.PrintData);
                break;
            case 'Z': // Slave Command
                r.append(v.SlaveRequest);
                break;
            case '%': // BASKET DATA
                r.append(BuildEFTBasketDataRequest(v));
                break;
            case '2': // GET PASSWORD
                r.append(PadLeftAsInt_DoubleDigit(v.MinPasswordLength));
                r.append(PadLeftAsInt_DoubleDigit(v.MaxPasswordLength));
                r.append(PadLeft_Length(v.Timeout));
                r.append("0");
                r.append(v.PasswordDisplay.pwrdDisplay);
                break;
            case '@': //PAY AT TABLE
                r.append(v.Header);
                r.append(v.Content);
                break;
        }
        return r;
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

    private StringBuilder BuildEFTDisplayRequest(EFTDisplayRequest v) {
        StringBuilder r = new StringBuilder();
        r.append('S');
        r.append('0');
        r.append(v.NumberOfLines);
        r.append(v.LineLength);
        r.append(v.DisplayText);
        r.append(v.CancelKeyFlag.flagType);
        r.append(v.AcceptKeyFlag.flagType);
        r.append(v.DeclineKeyFlag.flagType);
        r.append(v.AuthoriseKeyFlag.flagType);
        r.append(v.InputDataField.InputType);
        r.append(v.OKKeyFlag.flagType);
        r.append("  ");  //RESERVED SPACES
        r.append(v.GraphicsCode.GraphicType);
        r.append(PadLeft_Length(v.PurchaseAnalysisData.length()));
        r.append(v.PurchaseAnalysisData);
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
        r.append(PadLeft_Length(v.PurchaseAnalysisData.length()));
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
        r.append(PadLeft_Length(v.PurchaseAnalysisData.length())); // ADDED SINCE WAS FORGOTTEN
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
        r.append(PadLeft_Length(v.PurchaseAnalysisData.length()));
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
        if (Flags[index++] == '1') flags.add(EFTStatusRequest.PinpadOptionFlags.StartCash);
        return flags;
    }

    private EFTReceiptRequest.ReceiptType ParseReceiptType(char c) {
        switch (c) {
            case 'L': return EFTReceiptRequest.ReceiptType.Logon;
            case 'C': return EFTReceiptRequest.ReceiptType.Customer;
            case 'M': return EFTReceiptRequest.ReceiptType.Merchant;
            case 'S': return EFTReceiptRequest.ReceiptType.Settlement;
            case 'R': return EFTReceiptRequest.ReceiptType.ReceiptText;
            default: throw new IllegalArgumentException("Invalid receipt type");
        }
    }

    public EFTTransactionRequest.AccountType ParseCardAccountType(String msg) {
        switch (msg.trim().toUpperCase()) {
            case "CREDIT":  return EFTTransactionRequest.AccountType.Credit;
            case "SAVINGS": return EFTTransactionRequest.AccountType.Savings;
            case "CHEQUE":  return EFTTransactionRequest.AccountType.Cheque;
            default: return EFTTransactionRequest.AccountType.Default;
        }
    }

    public EFTTransactionRequest.TransactionType ParseTxnType(char c) {
        switch (c) {
            case ' ': return EFTTransactionRequest.TransactionType.NotSet;
            case 'P': return EFTTransactionRequest.TransactionType.PurchaseCash;
            case 'C': return EFTTransactionRequest.TransactionType.CashOut;
            case 'R': return EFTTransactionRequest.TransactionType.Refund;
            case 'A': return EFTTransactionRequest.TransactionType.PreAuth;
            case 'L': return EFTTransactionRequest.TransactionType.PreAuthCompletion;
            case 'N': return EFTTransactionRequest.TransactionType.PreAuthEnquiry;
            case 'Q': return EFTTransactionRequest.TransactionType.PreAuthCancel;
            case 'M': return EFTTransactionRequest.TransactionType.Completion;
            case 'T': return EFTTransactionRequest.TransactionType.TipAdjust;
            case 'D': return EFTTransactionRequest.TransactionType.Deposit;
            case 'W': return EFTTransactionRequest.TransactionType.Withdrawal;
            case 'B': return EFTTransactionRequest.TransactionType.Balance;
            case 'V': return EFTTransactionRequest.TransactionType.Voucher;
            case 'F': return EFTTransactionRequest.TransactionType.FundsTransfer;
            case 'O': return EFTTransactionRequest.TransactionType.OrderRequest;
            case 'H': return EFTTransactionRequest.TransactionType.MiniTransactionHistory;
            case 'X': return EFTTransactionRequest.TransactionType.AuthPIN;
            case 'K': return EFTTransactionRequest.TransactionType.EnhancedPIN;
            case '0': return EFTTransactionRequest.TransactionType.None;
            default: throw new IllegalArgumentException(("No valid transaction type"));
        }
    }

    public EFTStatusRequest.NetworkType ParseNetType(char c) {
        switch (c) {
            case ('0'): return EFTStatusRequest.NetworkType.Leased;
            case ('1'): return EFTStatusRequest.NetworkType.Dialup;
            default:    return EFTStatusRequest.NetworkType.Unknown;
        }
    }

    public EFTTransactionRequest.AccountType ParseAccountType(char c) {
        switch(c) {
            default : return EFTTransactionRequest.AccountType.Default;
            case '1': return EFTTransactionRequest.AccountType.Cheque;
            case '2': return EFTTransactionRequest.AccountType.Credit;
            case '3': return EFTTransactionRequest.AccountType.Savings;
        }
    }

    public EFTQueryCardResponse.TrackFlags ParseTracksRead(char tracksRead) throws IllegalArgumentException {
        switch(tracksRead) {
            default:  return EFTQueryCardResponse.TrackFlags.None;
            case '1': return EFTQueryCardResponse.TrackFlags.Track1;
            case '2': return EFTQueryCardResponse.TrackFlags.Track2;
            case '3': return EFTQueryCardResponse.TrackFlags.Tracks1and2;
            case '4': return EFTQueryCardResponse.TrackFlags.Track3;
            case '6': return EFTQueryCardResponse.TrackFlags.Tracks2and3;
            case '5':
            case '7': throw new IllegalArgumentException("Tracks 1 and 3 cannot be set at the same time");
        }
    }

    private static String PadLeftAsInt(double v) {
        return String.format("%09d", (int) (v * 100 + 0.0001d));
    }

    private static String PadLeftAsInt(int v) {
        return String.format("%04d", v);
    }

    private static String PadLeft_BasketLength(int v) {
        return String.format("%06d", v);
    }

    private static String PadLeft_Length(int v) {
        return String.format("%03d", v);
    }

    private static String PadLeftAsInt_DoubleDigit(int v) {
        return String.format("%02d", v);
    }

    private static String PadRightAndCut(String v, int totalWidth) {
        if (v.length() == totalWidth)
            return v;
        else if (v.length() < totalWidth)
            return String.format("%1$-" + totalWidth + "s", v);
        else return v.substring(0, totalWidth);
    }

    private static String ParsePurchaseAnalysisData(String msg, int index) {
        //Check that the PAD data has length prefix
        if (msg.length() >= index + 3) {
            try {
                int padLength = Integer.parseInt(msg.substring(index, index + 3));
                if (msg.length() == index + 3 + padLength) {
                    //Length matches, skip it!
                    index += 3;
                }
            } catch (Exception ignored) { }
        }

        //Return pad data if any
        return (msg.length() > index) ? msg.substring(index) : "";
    }

    //endregion
}