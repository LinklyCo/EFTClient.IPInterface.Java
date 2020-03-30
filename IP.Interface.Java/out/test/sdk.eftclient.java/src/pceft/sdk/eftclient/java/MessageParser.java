package pceft.sdk.eftclient.java;
//import jdk.nashorn.internal.runtime.Debug;

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
        CloudResponse('A');

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
            return null;
        }
        EFTResponse eftResponse;
        IPClientResponseType responseType = ParseResponseType(msg.charAt(5));
        char subcodeType = msg.charAt(6);
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
            case ChequeAuth:
                eftResponse = ParseChequeAuthResponse(msg);
                break;
            case QueryCard:
                eftResponse = ParseQueryCardResponse(msg);
                break;
            case GenericPOSCommand:
                eftResponse = ParseGenericPOSCommandResponse(msg);
                break;
            case Configure:
                eftResponse = ParseConfigMerchantResponse(msg);
                break;
            case CloudResponse:
                // Scenario - Based on sub code whether LOGON, TOKEN LOGON or PAIR RESPONSE
                switch (subcodeType)
                {
                    case 'T': // TOKEN RESPONSE
                        eftResponse = ParseCloudTokenLogonResponse(msg);
                        break;
                    case 'P': // PAIR RESPONSE
                        eftResponse = ParseCloudPairResponse(msg);
                        break;
                    case ' ': // LOGON RESPONSE (OLD)
                        eftResponse = ParseCloudLogonResponse(msg);
                        break;
                    default:
                        System.out.println(String.format("Unknown message type: %s", responseType));
                        throw new IllegalArgumentException(String.format("Unknown message type: %s", responseType));
                }
                break;
            case ClientList:
                eftResponse = ParseClientListResponse(msg);
                break;
            default:
                System.out.println(String.format("Unknown message type: %s", responseType));
                throw new IllegalArgumentException(String.format("Unknown message type: %s", responseType));
        }
        return eftResponse;
    }

    private EFTResponse ParseConfigMerchantResponse(String msg) {
        EFTConfigureMerchantResponse r = new EFTConfigureMerchantResponse();
        r.setSuccessFlag(msg.charAt(8));
        r.setResponseCode(msg.substring(9,11));
        r.setResponseText(msg.substring(11));
        return r;
    }

    private EFTResponse ParseChequeAuthResponse(String msg) {
        EFTChequeAuthResponse r = new EFTChequeAuthResponse();
        r.SuccessFlag = msg.charAt(7) == '1';
        r.ResponseCode = msg.substring(8,10);
        r.ResponseText =  msg.substring(10,30);
        r.Merchant = msg.substring(30,32);
        try {
            r.AmtPurchase = Double.parseDouble(msg.substring(32, 41)) / 100;
        } catch (Exception e) {
            r.AmtPurchase = 0d;
        }
        r.AuthCode =  msg.substring(41,47);
        r.ReferenceCode = msg.substring(47,msg.length());
        return r;
    }

    private EFTResponse ParseClientListResponse(String msg) {
        EFTClientListResponse r = new EFTClientListResponse();
        r.ResponseCode = msg.substring(7,9);
        r.setResponseText(msg.substring(9,msg.length()));
        /**
         *  Convert msg.Response to ClientDesc Class
         */
        return r;
    }

    private EFTResponse ParseCloudPairResponse(String msg) {
        EFTCloudPairResponse r = new EFTCloudPairResponse();
        r.setSuccessFlag(msg.charAt(7));
        r.ResponseCode = msg.substring(8,10);
        r.ResponseText = msg.substring(10,30);
        r.RedirectPort = Integer.parseInt(msg.substring(30,36));
        try{
            int index = 36;
            int lengthAddress = Integer.parseInt(msg.substring(index, index + 3));
            index += 3;
            r.setRedirectAddress(msg.substring(index, index + lengthAddress));
            index += lengthAddress;
            // TOKEN
            index +=3;
            r.setToken(msg.substring(index));
        }catch (Exception e){

        }
        return r;
    }

    private EFTResponse ParseCloudLogonResponse(String msg) {
        EFTCloudLogonResponse r = new EFTCloudLogonResponse();
        r.setSuccessFlag(msg.charAt(7));
        r.ResponseCode = msg.substring(8,10);
        r.ResponseText = msg.substring(10,30);
        return r;
    }

    private EFTResponse ParseCloudTokenLogonResponse(String msg) {
        EFTCloudTokenLogonResponse r = new EFTCloudTokenLogonResponse();
        r.setSuccessFlag(msg.charAt(7));
        r.ResponseCode = msg.substring(8,10);
        r.ResponseText = msg.substring(10,30);
        return r;
    }

    private EFTResponse ParseQueryCardResponse(String msg) {
        EFTQueryCardResponse r = new EFTQueryCardResponse();
        /**
         *  Track Data Parse
         */
        r.AccountType.sAccountType = msg.charAt(6);
        r.SuccessFlag = msg.charAt(7) == '1';
        r.ResponseCode =  msg.substring(8,10);
        r.ResponseText = msg.substring(10,30);
        return r;
    }

    private EFTResponse ParseGenericPOSCommandResponse(String msg) {
        EFTGenericCommandResponse r = new EFTGenericCommandResponse();
        /**
         *  PARSE RESPONSE ACCORDING TO SUB CODE
         */
        r.CommandType.SubCodeType = msg.charAt(6);
        r.ResponseCode = msg.substring(7,9);
        switch (r.CommandType.SubCodeType){
            case 'Z':
                r.SlaveResponse = msg.substring(9, msg.length());
                break;
            case '%':
                r.ResponseText = msg.substring(9);
                break;
            case '2':
                r.ResponseText = msg.substring(9,29);
                if (msg.length() > 29)
                {
                    int pwdLength = 0;
                    try{
                        pwdLength = Integer.parseInt(msg.substring(29,31));
                        r.Password = msg.substring(31,31 + pwdLength);
                    }catch(Exception ex){
                    }
                }
                break;
            case '@':
                //ACCORDING TO C#
                int _HEADERLENGTH = 0;
                try{
                    _HEADERLENGTH = Integer.parseInt(msg.substring(22,28));
                    r.Header = msg.substring(28,28 + _HEADERLENGTH);
                    r.Content = msg.substring(28 + _HEADERLENGTH);
                }catch(Exception ex){
                }
                break;
            default:
                r.ResponseText = msg.substring(9,29);
                r.Data = msg.substring(29, msg.length());
                break;
        }
        if (r.ResponseText.equals("APPROVED            ")){
            r.SuccessFlag = true;
        }
        return r;
    }


    private EFTResponse ParsePinpadBusyResponse(String msg) {
        EFTPinpadBusyResponse r = new EFTPinpadBusyResponse();
        return r;
    }

    private EFTResponse ParseSetDialogResponse(String msg) {
        int index = 7;
        EFTSetDialogResponse r = new EFTSetDialogResponse();
        r.Success = msg.charAt(index) == '1';

        return r;
    }

    private EFTResponse ParseControlPanelResponse(String msg) {
        int index = 7;
        EFTControlPanelResponse r = new EFTControlPanelResponse();

        r.Success = msg.charAt(index++) == '1';
        r.ResponseCode = msg.substring(index, index + 2);
        index += 2;
        r.ResponseText = msg.substring(index, index + 20);
        return r;
    }

    private EFTResponse ParseEFTReprintReceiptResponse(String msg) {
        EFTReprintReceiptResponse r = new EFTReprintReceiptResponse();
        int index = 7;
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
        int index = 7;
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
            r.AmtCash = 0d;
        }
        index += 9;
        try {
            r.AmtPurchase = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            r.AmtPurchase = 0d;
        }
        index += 9;
        try {
            r.AmtTip = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            r.AmtTip = 0d;
        }
        index += 9;
        try {
            r.AuthCode = Integer.parseInt(msg.substring(index, index + 6));
        } catch (Exception e) {
            r.AuthCode = 0;
        }
        index += 6;
        r.TxnRef = msg.substring(index, index + 16);
        index += 16;
        try {
            r.Stan = Integer.parseInt(msg.substring(index, index + 6).trim());
        } catch (Exception e) {
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
            r.CardName = 0;
        }
        index += 2;
        r.TxnFlags = new TxnFlags(msg.substring(index, index + 8).toCharArray());
        index += 8;
        r.BalanceReceived = msg.charAt(index++) == '1';
        try {
            r.AvailableBalance = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            r.AvailableBalance = 0d;
        }
        index += 9;
        try {
            Integer.parseInt(msg.substring(index, index + 3));
        } catch (Exception e) {
        }
        index += 3;
        r.PurchaseAnalysisData = msg.substring(index).trim();
        return r;
    }

    private EFTResponse ParseSettlementResponse(String msg) {
        EFTSettlementResponse r = new EFTSettlementResponse();
        int index = 7;
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
        int index = 7;
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
        }
        index += 3;
        if (msg.length() - index < padLength)
            return r;
        try {
            r.SAFCount = Integer.parseInt(msg.substring(index, index + 4));
        } catch (Exception e) {
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
            r.SAFCreditLimit = 0d;
        }
        index += 9;
        try {
            r.SAFDebitLimit = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
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
            r.TotalMemoryInTerminal = 0;
        }
        index += 4;
        try {
            r.FreeMemoryInTerminal = Integer.parseInt(msg.substring(index, index + 4));
        } catch (Exception e) {
            r.FreeMemoryInTerminal = 0;
        }
        index += 4;
        r.TerminalType = r.setTerminalType(msg.substring(index, index + 4));
        index += 4;
        try {
            r.NumAppsInTerminal = Integer.parseInt(msg.substring(index, index + 2));
        } catch (Exception e) {
            r.NumAppsInTerminal = 0;
        }
        index += 2;
        try {
            r.NumLinesOnDisplay = Integer.parseInt(msg.substring(index, index + 2));
        } catch (Exception e) {
            r.NumLinesOnDisplay = 0;
        }
        index += 2;
        SimpleDateFormat sf = new SimpleDateFormat("ddMMyy");
        try {
            r.HardwareInceptionDate = sf.parse(msg.substring(index, index + 6));
        } catch (Exception e) {
            r.HardwareInceptionDate = Date.from(Instant.now());
        }
        return r;
    }

    private EFTResponse ParseDisplayResponseType(String msg) {
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
        try {
            int padLength = Integer.parseInt(msg.substring(index, index + 3));
            index += 3;
            r.PurchaseAnalysisData = msg.substring(index, index + padLength);
        } catch (Exception e) {
            r.PurchaseAnalysisData = "";
            return r;
        }
        return r;

    }

    private EFTResponse ParseTransactionResponse(String msg) {
        int index = 7;
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
            r.AmtCash = 0d;
        }
        index += 9;
        try {
            r.AmtPurchase = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            r.AmtPurchase = 0d;
        }
        index += 9;
        try {
            r.AmtTip = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            r.AmtTip = 0d;
        }
        index += 9;
        try {
            r.AuthCode = Integer.parseInt(msg.substring(index, index + 6));
        } catch (Exception e) {
            r.AuthCode = 0;
        }
        index += 6;
        r.TxnRef = msg.substring(index, index + 16);
        index += 16;
        try {
            r.Stan = Integer.parseInt(msg.substring(index, index + 6).trim());
        } catch (Exception e) {
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
            r.AvailableBalance = 0d;
        }
        index += 9;
        try {
            r.ClearedFundsBalance = Double.parseDouble(msg.substring(index, index + 9)) / 100;
        } catch (Exception e) {
            r.ClearedFundsBalance = 0d;
        }
        index += 9;
        r.PurchaseAnalysisData = msg.substring(index).trim();
        return r;
    }

    private EFTResponse ParseLogonResponse(String msg) {
        int index = 7;
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
            }
            index += 12;
            try {
                r.Stan = Integer.parseInt(msg.substring(index, index + 6));
            } catch (Exception e) {
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
        int index = 6;
        EFTReceiptResponse r = new EFTReceiptResponse();
        try {
            r.Receipt = ParseReceiptType(msg.charAt(index));
            index++;
        } catch (IllegalArgumentException e) {
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
                return IPClientResponseType.CloudResponse;

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

    private StringBuilder BuildEFTCloudPairRequest(EFTCloudPairRequest v){
        StringBuilder r = new StringBuilder();
        //PAD RIGHT TO LENGTH 16
        r.append('A');
        r.append('P');
        r.append(PadRightAndCut(v.ClientID,16));
        r.append(PadRightAndCut(v.Password,16));
        r.append(PadRightAndCut(v.PairCode,16));
        return r;
    }

    private StringBuilder BuildEFTCloudLogonRequest(EFTCloudLogonRequest v){
        StringBuilder r = new StringBuilder();
        //PAD RIGHT TO LENGTH 16
        r.append('A');
        r.append(' ');
        r.append(PadRightAndCut(v.ClientID,16));
        r.append(PadRightAndCut(v.Password,16));
        r.append(PadRightAndCut(v.PairCode,16));
        return r;
    }

    private StringBuilder BuildEFTCloudTokenLogonRequest(EFTCloudTokenLogonRequest v){
        StringBuilder r = new StringBuilder();
        r.append('A');
        r.append('T');
        r.append(PadLeft_Length(v.Token.length()));
        r.append(v.Token);
        return r;
    }

    private StringBuilder BuildEFTClientListRequest(EFTClientListRequest v) {
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

    private StringBuilder BuildEFTQueryCardRequest(EFTQueryCardRequest v){
        StringBuilder r = new StringBuilder();
        /**
         * Append String With Request Vars
         */
        r.append('J');
        r.append(v.AccountType.sAccountType);
        r.append(v.Application);
        r.append(v.Merchant);
        r.append(PadLeft_Length(v.getPurchaseAnalysisData().length()));
        r.append(v.getPurchaseAnalysisData());
        return r;
    }

    private StringBuilder BuildEFTBasketDataRequest(EFTGenericCommandRequest v) {
        StringBuilder r = new StringBuilder();
        //ATTACHMENT TO GENERIC COMMAND

        String jsonContent = "{}";

        if (v.BasketCommand instanceof EFTBasketDataCommandAdd)
        {
            EFTBasketDataCommandAdd c = (EFTBasketDataCommandAdd)v.BasketCommand;
            JSONObject json = new JSONObject(c.Basket);
            jsonContent = json.toString(0);
        }
        else if (v.BasketCommand instanceof EFTBasketDataCommandCreate)
        {
            EFTBasketDataCommandCreate c = (EFTBasketDataCommandCreate)v.BasketCommand;
            JSONObject json = new JSONObject(c.Basket);
            jsonContent = json.toString(0);
        }
        else if (v.BasketCommand instanceof EFTBasketDataCommandDelete)
        {
            EFTBasketDataCommandDelete c = (EFTBasketDataCommandDelete)v.BasketCommand;
            EFTBasket basket = new EFTBasket();
            basket.id = c.BasketId;
            basket.items = new ArrayList<EFTBasketItem>();
            basket.items.add(new EFTBasketItem(c.BasketItemId));
            JSONObject json = new JSONObject(basket);
            jsonContent = json.toString(0);
        }
        else if (v.BasketCommand instanceof EFTBasketDataCommandRaw)
        {
            EFTBasketDataCommandRaw c = (EFTBasketDataCommandRaw)v.BasketCommand;
            jsonContent = c.BasketContent;
        }
        r.append(PadLeft_BasketLength(jsonContent.length()));
        r.append(jsonContent);
        return r;
    }

    private StringBuilder BuildEFTGenericCommandRequest(EFTGenericCommandRequest v){
        StringBuilder r = new StringBuilder();
        r.append('X');
        r.append(v.CommandType.SubCodeType);
        switch (v.CommandType.SubCodeType){
            case '0': // Display Data
                r.append(PadLeftAsInt_DoubleDigit(v.NumberOfLines));
                r.append(PadLeft_Length(v.Timeout));
                r.append(v.DisplayMap);
                r.append(PadRightAndCut(v.PinpadKeyMap,8));
                r.append(PadRightAndCut(v.POSKeyMap,8));
                r.append(PadLeft_Length(v.LineLength));
                r.append(PadRightAndCut(v.POSDisplayData,40));
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
                r.append("0" + v.PasswordDisplay.pwrdDisplay);
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
        if (v.PurchaseAnalysisData.length() > 0) { //Pointless to add if no PurchaseAnalysisData
            r.append(PadLeft_Length(v.PurchaseAnalysisData.length())); // ADDED SINCE WAS FORGOTTEN
            r.append(v.PurchaseAnalysisData);
        }
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

    //endregion
}