import pceft.sdk.eftclient.java.*;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.sql.Date;
import java.time.Instant;

public class AsyncMainWindow implements PCEFTPOSEventListener, SSLSocketListener {
    private AsyncSocketControl AsyncCtrl;
    private AsyncSSLSocketControl AsyncSSLCtrl;
    private SettingsDemoPos _Settings = new SettingsDemoPos();
    private JTextArea textArea1;
    private JButton btnDoThing;
    private JPanel Panel1;
    private JButton btnClose;
    private JButton btnClearText;
    private JComboBox<String> cmbTransactions;
    private JComboBox<String> cmbOptions;
    private JTextField txtPurchaseAmount;
    private JTextField txtCashOutAmount;
    private JTextField txtDialogTitle;
    private JTextField txtIPAddress;
    private JTextField txtPort;
    private JButton btnChangeSocket;
    private JTextField txtDisplayLine1;
    private JTextField txtDisplayLine2;
    private JButton btnOK;
    private JButton btnCancel;
    private JButton btnYes;
    private JButton btnNo;
    private JButton btnDisconnect;
    private JTextField pairCodeText;
    private JPanel TransactionPanel;
    private JTextField MerchantBox;
    private JComboBox ApplicationCmbBox;
    private JTextField PADTxtBox;
    private JLabel PanelLBl1;
    private JLabel PanelLBl2;
    private JLabel PanelLBl3;
    private JLabel PanelLBl4;
    private JLabel PanelLBl5;
    private JButton saveBtn;
    private JCheckBox SSLCheckBox;

    private boolean _isConnected = false;

    private String defaultCloudAddress = "pos.sandbox.cloud.pceftpos.com";
    private int defaultCloudPort = 443;
    private String defaultLocalAddress = "127.0.0.1";
    private int defaultLocalPort = 2011;

    private String HostName;
    private int Port;

    //region strings
    private String purchaseamt = "Purchase Amount:";
    private String cashoutamt = "Cashout Amount:";
    private String merhcantLbl = "Merchant Number:";
    private String appLbl = "Application:";
    private String padLbl = "Purchase Analysis Data:";

    private String IDLbl = "Client ID:";
    private String PasswordLbl = "Password:";
    private String PairCodeLbl = "Pair Code:";
    private String TokenLbl = "Token:";
    //endregion

    public static void main(String[] args) {
        try { ;
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");

        } catch (Exception ignored) {
        }
        JFrame frame = new JFrame("Main Window");
        frame.setContentPane(new AsyncMainWindow().Panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

private void VisbilityExtensionPanel(boolean show) {
    PanelLBl1.setVisible(show);
    PanelLBl2.setVisible(show);
    PanelLBl3.setVisible(show);
    PanelLBl4.setVisible(show);
    PanelLBl5.setVisible(show);

    txtPurchaseAmount.setVisible(show);
    txtCashOutAmount.setVisible(show);
    MerchantBox.setVisible(show);
    ApplicationCmbBox.setVisible(show);
    PADTxtBox.setVisible(show);
}
    public AsyncMainWindow() {

        //region load settings and stuff
        _Settings.LoadSettings();
        txtIPAddress.setText(_Settings.getHostAddress());
        txtPort.setText("" + _Settings.getHostPort());
        //endregion

        SSLCheckBox.setSelected(_Settings.isUseSSL());
        _isConnected = !_Settings.UseSSL;
        SSLCheckBox.addActionListener(e -> {
            _Settings.setUseSSL(SSLCheckBox.isSelected());
            _isConnected = !_Settings.UseSSL;
            if (SSLCheckBox.isSelected())
            {
                txtIPAddress.setText(defaultCloudAddress);
                txtPort.setText("" + defaultCloudPort);
            }
            else{
                txtIPAddress.setText(defaultLocalAddress);
                txtPort.setText("" + defaultLocalPort);
            }
            Disconnect();
            DoChangeSocket();
            //Disconnect();
        });

        //region Add Stuff To Application Combo Box
        ApplicationCmbBox.addItem("EFTPOS");
        ApplicationCmbBox.addItem("Agency");
        ApplicationCmbBox.addItem("GiftCard");
        ApplicationCmbBox.addItem("Fuel");
        ApplicationCmbBox.addItem("Medicare");
        ApplicationCmbBox.addItem("Amex");
        ApplicationCmbBox.addItem("ChequeAuth");
        ApplicationCmbBox.addItem("Loyalty");
        ApplicationCmbBox.addItem("PrePaidCard");
        ApplicationCmbBox.addItem("ETS");
        //endregion

        //region Add Stuff to Frame
        cmbTransactions.addItem("Logon");
        cmbTransactions.addItem("Transaction");
        cmbTransactions.addItem("Status");
        cmbTransactions.addItem("Settlement");
        cmbTransactions.addItem("Get Last Transaction");
        cmbTransactions.addItem("Reprint Receipt");
        cmbTransactions.addItem("Set Dialog");
        cmbTransactions.addItem("Display Request");
        cmbTransactions.addItem("Generic POS Command");
        cmbTransactions.addItem("Display Control Panel");
        cmbTransactions.addItem("Query Card");
        cmbTransactions.addItem("Get Client List");
        cmbTransactions.addItem("Cheque Auth");
        cmbTransactions.addItem("Configure Merchant");
        cmbTransactions.addItem("Cloud Pair");
        cmbTransactions.addItem("Cloud Logon");
        cmbTransactions.addItemListener(e ->
        {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                btnDoThing.setEnabled(_isConnected);
                VisbilityExtensionPanel(false);
                switch (cmbTransactions.getSelectedIndex()) {
                    case 0:
                        cmbOptions.removeAllItems();
                        cmbOptions.addItem("Standard");
                        cmbOptions.addItem("TMS");
                        cmbOptions.addItem("RSA");
                        txtDialogTitle.setEnabled(false);
                        btnDoThing.setEnabled(true);
                        break;
                    case 1:
                        cmbOptions.removeAllItems();
                        cmbOptions.addItem("Purchase");
                        cmbOptions.addItem("Refund");
                        cmbOptions.addItem("CashOut");
                        VisbilityExtensionPanel(true);
                        PanelLBl1.setText(purchaseamt);
                        PanelLBl2.setText(cashoutamt);
                        PanelLBl3.setText(merhcantLbl);
                        PanelLBl4.setText(appLbl);
                        PanelLBl5.setText(padLbl);
                        txtPurchaseAmount.setText("1");
                        txtCashOutAmount.setText("0");
                        break;
                    case 2:
                        cmbOptions.removeAllItems();
                        cmbOptions.addItem("Standard");
                        cmbOptions.addItem("TerminalAppInfo");
                        txtDialogTitle.setEnabled(false);
                        break;
                    case 3:
                        cmbOptions.removeAllItems();
                        cmbOptions.addItem("Settlement");
                        cmbOptions.addItem("Pre-Settlement");
                        cmbOptions.addItem("Shift Totals");
                        cmbOptions.addItem("Last Settlement");
                        txtDialogTitle.setEnabled(false);
                        break;
                    case 5:
                        cmbOptions.removeAllItems();
                        cmbOptions.addItem("Reprint Last Receipt");
                        cmbOptions.addItem("Get Last Receipt");
                        txtDialogTitle.setEnabled(false);
                        break;
                    case 6:
                        cmbOptions.removeAllItems();
                        cmbOptions.addItem("Standard");
                        cmbOptions.addItem("Touchscreen");
                        cmbOptions.addItem("Hidden");
                        txtDialogTitle.setEnabled(true);
                        break;
                    //case 7:
                    case 8:
                        cmbOptions.removeAllItems();
                        cmbOptions.addItem("Display Data");
                        cmbOptions.addItem("Print Data");
                        cmbOptions.addItem("Slave Command");
                        cmbOptions.addItem("Basket Data");
                        cmbOptions.addItem("Get Password");
                        cmbOptions.addItem("Pay At Table");
                        txtDialogTitle.setEnabled(true);
                        break;
                    case 9:
                        cmbOptions.removeAllItems();
                        cmbOptions.addItem("Full Panel");
                        cmbOptions.addItem("Settlement Control Panel");
                        cmbOptions.addItem("Journal Viewer Control Panel");
                        cmbOptions.addItem("Pinpad Setup Control Panel");
                        cmbOptions.addItem("Status Control Panel");
                        txtDialogTitle.setEnabled(false);
                        break;
                    //case 10
                    //case 11
                    //case 12
                    case 14:

                        cmbOptions.removeAllItems();
                        PanelLBl1.setVisible(true);
                        PanelLBl2.setVisible(true);
                        PanelLBl3.setVisible(true);
                        PanelLBl1.setText(IDLbl);
                        PanelLBl2.setText(PasswordLbl);
                        PanelLBl3.setText(PairCodeLbl);
                        txtPurchaseAmount.setVisible(true);
                        txtPurchaseAmount.setText(_Settings.getCloudUsername());
                        txtCashOutAmount.setVisible(true);
                        txtCashOutAmount.setText(_Settings.getCloudPassword());
                        MerchantBox.setVisible(true);
                        btnDoThing.setEnabled(true);
                        break;
                    case 15:
                        cmbOptions.removeAllItems();
                        PanelLBl1.setVisible(true);
                        PanelLBl1.setText(TokenLbl);
                        txtPurchaseAmount.setVisible(true);
                        txtPurchaseAmount.setText(_Settings.getCloudToken());
                        btnDoThing.setEnabled(true);
                        break;
                    default:
                        cmbOptions.removeAllItems();
                        txtDialogTitle.setEnabled(false);
                        break;
                }
            }
        });
        btnChangeSocket.addActionListener(e -> DoChangeSocket());
        if (_Settings.UseSSL) {
            AsyncSSLCtrl = new AsyncSSLSocketControl(this, txtIPAddress.getText(), Integer.parseInt(txtPort.getText()));
            AsyncSSLCtrl.setSslListener(this);
        } else{
            AsyncCtrl = new AsyncSocketControl(this, txtIPAddress.getText(), Integer.parseInt(txtPort.getText()));
        }
        btnDoThing.addActionListener(e -> DoTransaction());
        btnClose.addActionListener(e -> System.exit(0));
        btnClearText.addActionListener(e -> ClearTextArea());
        saveBtn.addActionListener(e -> SettingsSave());
        cmbTransactions.setSelectedIndex(1);
        btnCancel.setEnabled(false);
        btnOK.setEnabled(false);
        btnYes.setEnabled(false);
        btnNo.setEnabled(false);
        btnNo.addActionListener(e -> SendNo());
        btnCancel.addActionListener(e -> SendOKCancel());
        btnOK.addActionListener(e -> SendOKCancel());
        btnYes.addActionListener(e -> SendYes());
        btnDisconnect.addActionListener(e -> Disconnect());
        //endregion

    }

    private void SettingsSave(){

        if (cmbTransactions.getSelectedIndex() == 14) {
            _Settings.setCloudUsername(txtPurchaseAmount.getText()); //TODO, NOT MAKE THIS THE ONLY BOX TO SAVE SETTINGS IN
            _Settings.setCloudPassword(txtCashOutAmount.getText());
        }
        _Settings.setHostAddress(txtIPAddress.getText());
        _Settings.setHostPort(Integer.parseInt(txtPort.getText()));

        _Settings.setUseSSL(SSLCheckBox.isSelected());

        _Settings.SaveSettings();
    }

    private void Disconnect() {
        _isConnected = false;
            if (AsyncSSLCtrl != null) {
                if (AsyncSSLCtrl.socket != null) {
                    if (AsyncSSLCtrl.socket.isConnected()) {
                        try {
                            textArea1.setText(AsyncSSLCtrl.close());
                        } catch (RuntimeException e) {
                            textArea1.setText(e.getMessage());
                        }
                    }
                    else
                    textArea1.setText("Socket already closed");
                }
            }
            if (AsyncCtrl != null) {
                if (AsyncCtrl.socket != null)
                    if (AsyncCtrl.socket.isConnected()) {
                        try {
                            textArea1.setText(AsyncCtrl.close());
                        } catch (RuntimeException e) {
                            textArea1.setText(e.getMessage());
                        }
                    }
                    else
                        textArea1.setText("Socket already closed");
            }
    }

    private void SendYes() {
        EFTSendKeyRequest keyPress = new EFTSendKeyRequest();
        keyPress.Key = EFTSendKeyRequest.EFTPOSKey.YesAccept;
        try {
            if (_Settings.UseSSL) {
                AsyncSSLCtrl.socketSend(keyPress);
            } else{
                AsyncCtrl.socketSend(keyPress);
            }
        } catch (Exception e) {
            textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
        }
    }

    private void SendOKCancel() {
        EFTSendKeyRequest keyPress = new EFTSendKeyRequest();
        keyPress.Key = EFTSendKeyRequest.EFTPOSKey.OKCancel;
        try {
            if (_Settings.UseSSL) {
                AsyncSSLCtrl.socketSend(keyPress);
            } else{
                AsyncCtrl.socketSend(keyPress);
            }
        } catch (Exception e) {
            textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
        }
    }

    private void SendNo() {
        EFTSendKeyRequest keyPress = new EFTSendKeyRequest();
        keyPress.Key = EFTSendKeyRequest.EFTPOSKey.NoDecline;
        try {
            if (_Settings.UseSSL) {
                AsyncSSLCtrl.socketSend(keyPress);
            } else{
                AsyncCtrl.socketSend(keyPress);
            }
        } catch (Exception e) {
            textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
        }
    }

    private void DoChangeSocket() {
        if (AsyncSSLCtrl != null) {
            if (AsyncSSLCtrl.socket != null) {
                if (AsyncSSLCtrl.socket.isConnected()) {
                    AsyncSSLCtrl.close();
                }
            }
        }
        if (AsyncCtrl != null) {
            if (AsyncCtrl.socket != null) {
                if (AsyncCtrl.socket.isConnected()) {
                    AsyncCtrl.close();
                }
            }
        }
        if (_Settings.UseSSL) {
            AsyncSSLCtrl = new AsyncSSLSocketControl(this, txtIPAddress.getText(), Integer.parseInt(txtPort.getText()));
            AsyncSSLCtrl.setSslListener(this);
            if (AsyncSSLCtrl.socket == null) {
                textArea1.setText(String.format("Socket is null %s", (AsyncSSLCtrl.socket.getException() != null) ? AsyncSSLCtrl.socket.getException().getMessage() : ""));
            }
            if (AsyncSSLCtrl.socket.selector == null) {
                textArea1.setText(String.format("Socket selector is null %s", (AsyncSSLCtrl.socket.getException() != null) ? AsyncSSLCtrl.socket.getException().getMessage() : ""));
            }
            if (AsyncSSLCtrl.socket.channel == null) {
                textArea1.setText(String.format("Socket channel is null %s", (AsyncSSLCtrl.socket.getException() != null) ? AsyncSSLCtrl.socket.getException().getMessage() : ""));
            }
            if (AsyncSSLCtrl.socket.isConnected()) {
                textArea1.setText(String.format("Socket connected on %s:%s", txtIPAddress.getText(), txtPort.getText()));
            }
        } else{
            AsyncCtrl = new AsyncSocketControl(this, txtIPAddress.getText(), Integer.parseInt(txtPort.getText()));
            if (AsyncCtrl.socket == null) {
                textArea1.setText(String.format("Socket is null %s", (AsyncCtrl.socket.getException() != null) ? AsyncCtrl.socket.getException().getMessage() : ""));
            }
            if (AsyncCtrl.socket.selector == null) {
                textArea1.setText(String.format("Socket selector is null %s", (AsyncCtrl.socket.getException() != null) ? AsyncCtrl.socket.getException().getMessage() : ""));
            }
            if (AsyncCtrl.socket.channel == null) {
                textArea1.setText(String.format("Socket channel is null %s", (AsyncCtrl.socket.getException() != null) ? AsyncCtrl.socket.getException().getMessage() : ""));
            }
            if (AsyncCtrl.socket.isConnected()) {
                textArea1.setText(String.format("Socket connected on %s:%s", txtIPAddress.getText(), txtPort.getText()));
            }
        }
    }

    private void ClearTextArea() {
        textArea1.setText("");
    }


    //region Request Types
    private void DoTransaction() {

        switch (cmbTransactions.getSelectedIndex()) {
            case 0:
                DoLogon();
                break;
            case 1:
                DoPurchase();
                break;
            case 2:
                DoStatus();
                break;
            case 3:
                DoSettlement();
                break;
            case 4:
                DoGetLastTransaction();
                break;
            case 5:
                DoReprintReceipt();
                break;
            case 6:
                DoSetDialog();
                break;
            case 7:
                DoDisplayRequest();
                break;
            case 8:
                DoGenericCommand();
                break;
            case 9:
                DoControlPanel();
                break;
            case 10:
                DoQueryCard();
                break;
            case 11:
                DoClientList();
                break;
            case 12:
                DoChequeAuth();
                break;
            case 13:
                DoConfigureMerchant();
                break;
            case 14:
                DoCloudPair();
                break;
            case 15:
                DoCloudLogon();
                break;
        }
    }

    private void DoCloudPair(){
        EFTCloudPairRequest pair = new EFTCloudPairRequest();
        pair.setClientID(txtPurchaseAmount.getText());
        pair.setPassword(txtCashOutAmount.getText());
        pair.setPairCode(MerchantBox.getText());
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(pair);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            textArea1.setText("SSL Is Not Being Used, Cloud Logon Unavailable");
        }
    }

    private void DoCloudLogon(){
        EFTCloudTokenLogonRequest tknLgn = new EFTCloudTokenLogonRequest();
        tknLgn.setToken(txtPurchaseAmount.getText());
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(tknLgn);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
                textArea1.setText("SSL Is Not Being Used, Cloud Logon Unavailable");
        }
    }

    private void DoConfigureMerchant() {
        EFTConfigureMerchantRequest cfgMrchnt = new EFTConfigureMerchantRequest();
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(cfgMrchnt);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(cfgMrchnt);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    private void DoChequeAuth() {
        EFTChequeAuthRequest chqAuth = new EFTChequeAuthRequest();
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(chqAuth);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(chqAuth);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    private void DoClientList() {
        EFTClientListRequest client = new EFTClientListRequest();
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(client);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(client);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }


    private void DoQueryCard() {
        EFTQueryCardRequest crdQuery = new EFTQueryCardRequest();
        crdQuery.setAccountType(EFTQueryCardRequest.SelectAccountType.NoAccountSelected);
        crdQuery.Application = EFTQueryCardRequest.TerminalApplication.EFTPOS;
        crdQuery.Merchant = "00";
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(crdQuery);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(crdQuery);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    private void DoControlPanel() {
        EFTControlPanelRequest cntrlPnl = new EFTControlPanelRequest();
        switch (cmbOptions.getSelectedIndex()) {
            case 0:
                cntrlPnl.CPanelType = EFTControlPanelRequest.ControlPanelType.Full;
                break;
            case 1:
                cntrlPnl.CPanelType = EFTControlPanelRequest.ControlPanelType.Settlement;
                break;
            case 2:
                cntrlPnl.CPanelType = EFTControlPanelRequest.ControlPanelType.JournalViewer;
                break;
            case 3:
                cntrlPnl.CPanelType = EFTControlPanelRequest.ControlPanelType.PinpadSetup;
                break;
            case 4:
                cntrlPnl.CPanelType = EFTControlPanelRequest.ControlPanelType.Status;
                break;
        }
        cntrlPnl.ReceiptPrintMode = EFTRequest.ReceiptPrintModeType.PinpadPrinter;
        cntrlPnl.ReceiptCutMode = EFTRequest.ReceiptCutModeType.DontCut;
        cntrlPnl.CPanelReturnType = EFTControlPanelRequest.ControlPanelReturnType.ImmediatelyAndWhenClose;
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(cntrlPnl);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(cntrlPnl);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    private void DoGenericCommand() {
        EFTGenericCommandRequest genCmd = new EFTGenericCommandRequest();
        genCmd.SlaveRequest = txtDialogTitle.getText();
        switch (cmbOptions.getSelectedIndex()) {
            case 0:
                genCmd.CommandType = EFTGenericCommandRequest.SubCode.DisplayData;
                genCmd.NumberOfLines = 2;
                genCmd.Timeout = 60;
                genCmd.setDisplayMap('1', '1');
                genCmd.setPinpadKeyMap('1', '1');
                genCmd.setPOSKeyMap('1', '1');
                genCmd.LineLength = 20;
                genCmd.POSDisplayData = "ABCDEFGHIJ0123456789KLMNOPQRSTABCDEFGHIJ";
                genCmd.PinpadLineData = "ABCDEFGHIJ0123456789KLMNOPQRSTABCDEFGHIJ";
                break;
            case 1:
                genCmd.CommandType = EFTGenericCommandRequest.SubCode.PrintData;
                genCmd.NumberOfLines = 2;
                genCmd.setPrinterByteMap('1');
                genCmd.LineLength = 20;
                genCmd.PrintData = "ABCDEFGHIJ0123456789KLMNOPQRSTABCDEFGHIJ";
                break;
            case 2:
                genCmd.CommandType = EFTGenericCommandRequest.SubCode.SlaveCommand;
                break;
            case 3:
                genCmd.CommandType = EFTGenericCommandRequest.SubCode.BasketData;
                int TESTCASE = 4;//TESTING BASKET DATA, DELETE LATER
                switch (TESTCASE)
                {
                    case 0:
                        EFTBasketDataCommandCreate c = new EFTBasketDataCommandCreate();
                        c.Basket = new EFTBasket();
                        break;
                    case 1:
                        EFTBasketDataCommandAdd a = new EFTBasketDataCommandAdd();
                        a.Basket = new EFTBasket();
                        break;
                    case 2:
                        EFTBasketDataCommandRaw r = new EFTBasketDataCommandRaw();
                        r.BasketContent = "";
                        genCmd.BasketCommand = r;
                        break;
                    default:
                        EFTBasketDataCommandDelete d = new EFTBasketDataCommandDelete();
                        d.BasketId = "0";
                        d.BasketItemId = "0";
                        genCmd.BasketCommand = d;
                        break;
                }
                break;
            case 4:
                genCmd.CommandType = EFTGenericCommandRequest.SubCode.GetPassword;
                genCmd.MinPasswordLength = 6; // ANY NUMBER BETWEEN 0-99
                genCmd.MaxPasswordLength = 8; // ANY NUMBER BETWEEN 0-99
                genCmd.Timeout = 0; // ANY NUMBER BETWEEN 0-255
                genCmd.PasswordDisplay = EFTGenericCommandRequest._PasswordDisplay.Enter_Code;
                break;
            case 5:
                genCmd.CommandType = EFTGenericCommandRequest.SubCode.PayAtTable;
                genCmd.Header = "[Insert String Here]";
                genCmd.Content = "[Insert String Here]";
                break;
        }
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(genCmd);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(genCmd);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }


    private void DoSetDialog() {
        EFTSetDialogRequest dialog = new EFTSetDialogRequest();
        dialog.DialogTitle = txtDialogTitle.getText();
        switch (cmbOptions.getSelectedIndex()) {
            case 0:
                dialog.dialogType = EFTSetDialogRequest.DialogType.Standard;
                break;
            case 1:
                dialog.dialogType = EFTSetDialogRequest.DialogType.TouchScreen;
                break;
            case 2:
                dialog.dialogType = EFTSetDialogRequest.DialogType.Hidden;
                break;
        }
        dialog.DialogX = 10;
        dialog.DialogY = 10;
        dialog.DisableDisplayEvents = false;
        dialog.EnableTopmost = false;
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(dialog);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(dialog);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    private void DoReprintReceipt() {
        EFTReprintReceiptRequest reprint = new EFTReprintReceiptRequest();
        switch (cmbOptions.getSelectedIndex()) {
            case 0:
                reprint.ReprntType = EFTReprintReceiptRequest.ReprintType.Reprint;
                break;
            case 1:
                reprint.ReprntType = EFTReprintReceiptRequest.ReprintType.GetLast;
                break;
        }
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(reprint);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(reprint);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    private void DoGetLastTransaction() {
        EFTGetLastTransactionRequest getLast = new EFTGetLastTransactionRequest();
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(getLast);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(getLast);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    private void DoSettlement() {
        EFTSettlementRequest settle = new EFTSettlementRequest();
        switch (cmbOptions.getSelectedIndex()) {
            case 0:
                settle.SettleType = EFTSettlementRequest.SettlementType.Settlement;
                break;
            case 1:
                settle.SettleType = EFTSettlementRequest.SettlementType.PreSettlement;
                break;
            case 2:
                settle.SettleType = EFTSettlementRequest.SettlementType.SubShiftTotals;
                break;
            case 3:
                settle.SettleType = EFTSettlementRequest.SettlementType.LastSettlement;
                break;
        }
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(settle);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(settle);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    private void DoPurchase() {
        EFTTransactionRequest newTxn = new EFTTransactionRequest();
        try {
            newTxn.AmtPurchase = Double.parseDouble(txtPurchaseAmount.getText());
            newTxn.AmtCash = Double.parseDouble(txtCashOutAmount.getText());
        } catch (Exception ex) {
            //Alert r = new Alert(Alert.AlertType.ERROR, "Please Enter A Valid Amount", ButtonType.OK);
            JOptionPane.showMessageDialog(null, "Please Enter A Valid Amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
            //r.show();
        }
        newTxn.Merchant = MerchantBox.getText();
        switch (cmbTransactions.getSelectedIndex()) {
            case 0:
                newTxn.Application = EFTTransactionRequest.TerminalApplication.EFTPOS;
                break;
            case 1:
                newTxn.Application = EFTTransactionRequest.TerminalApplication.Agency;
                break;
            case 2:
                newTxn.Application = EFTTransactionRequest.TerminalApplication.GiftCard;
                break;
            case 3:
                newTxn.Application = EFTTransactionRequest.TerminalApplication.Fuel;
                break;
            case 4:
                newTxn.Application = EFTTransactionRequest.TerminalApplication.Medicare;
                break;
            case 5:
                newTxn.Application = EFTTransactionRequest.TerminalApplication.Amex;
                break;
            case 6:
                newTxn.Application = EFTTransactionRequest.TerminalApplication.ChequeAuth;
                break;
            case 7:
                newTxn.Application = EFTTransactionRequest.TerminalApplication.Loyalty;
                break;
            case 8:
                newTxn.Application = EFTTransactionRequest.TerminalApplication.PrePaidCard;
                break;
            case 9:
                newTxn.Application = EFTTransactionRequest.TerminalApplication.ETS;
                break;
            default:
                newTxn.Application = EFTTransactionRequest.TerminalApplication.EFTPOS;
                break;
        }
        //newTxn.Application = EFTTransactionRequest.TerminalApplication.EFTPOS;
        newTxn.TxnRef = "270220201003333 ";
        newTxn.BankDate = Date.from(Instant.now());
        newTxn.PurchaseAnalysisData = PADTxtBox.getText();
        switch (cmbOptions.getSelectedIndex()) {
            case 0:
                newTxn.TxnType = EFTTransactionRequest.TransactionType.PurchaseCash;
                break;
            case 1:
                newTxn.TxnType = EFTTransactionRequest.TransactionType.Refund;
                break;
            case 2:
                newTxn.TxnType = EFTTransactionRequest.TransactionType.CashOut;
                break;
        }
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(newTxn);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(newTxn);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    private void DoLogon() {
        //MessageParser msg = new MessageParser();
        // Should fill in the properties of msg here
        EFTLogonRequest Logon = new EFTLogonRequest();
        switch (cmbOptions.getSelectedIndex()) {
            case 0:
                Logon.logonType = EFTLogonRequest.LogonType.Standard;
                break;
            case 1:
                Logon.logonType = EFTLogonRequest.LogonType.TMSFull;
                break;
            case 2:
                Logon.logonType = EFTLogonRequest.LogonType.RSA;
                break;
        }
        Logon.ReceiptPrintMode = EFTRequest.ReceiptPrintModeType.POSPrinter;
        if (!_Settings.UseSSL) {
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(Logon);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    private void DoStatus() {
        EFTStatusRequest Status = new EFTStatusRequest();
        switch (cmbOptions.getSelectedIndex()) {
            case 0:
                Status.StatType = EFTStatusRequest.StatusType.Standard;
                break;
            case 1:
                Status.StatType = EFTStatusRequest.StatusType.TerminalAppInfo;
                break;
        }
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(Status);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(Status);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    private void DoDisplayRequest() {
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
        if (_Settings.UseSSL) {
            if (AsyncSSLCtrl.socket.isConnected())
                try {
                    AsyncSSLCtrl.socketSend(display);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        } else{
            if (AsyncCtrl.socket.isConnected())
                try {
                    AsyncCtrl.socketSend(display);
                } catch (Exception e) {
                    textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
                    System.out.println("MSG: " + e.getMessage());
                    System.out.println("Cause: " + e.getCause());
                    System.out.println("RAW: " + e.toString());
                }
            else
                textArea1.setText("Socket is not connected");
        }
    }

    //endregion


    //region Events
    @Override
    public void onLogonEvent(EFTLogonResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
        if (msg.Success){
            _isConnected = true;
        }
    }

    @Override
    public void onReceiptEvent(EFTReceiptResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
        if (msg.Receipt.Receipt == 'R')
            for (String s : msg.ReceiptText
            ) {
                textArea1.append(s + '\n');
            }
        try {
            if (_Settings.UseSSL)
            {
                AsyncSSLCtrl.socketSend(new EFTReceiptRequest());
            }
            else{
                AsyncCtrl.socketSend(new EFTReceiptRequest());
            }

        } catch (Exception e) {
            textArea1.setText(String.format("Socket write failed with the following exception: %s %s", e.toString(), e.getMessage()));
        }

    }

    @Override
    public void onTransactionEvent(EFTTransactionResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onDisplayEvent(EFTDisplayResponse msg) {
        btnNo.setEnabled(false);
        btnYes.setEnabled(false);
        btnOK.setEnabled(false);
        btnCancel.setEnabled(false);
        txtDisplayLine1.setText(msg.DisplayText[0]);
        txtDisplayLine2.setText(msg.DisplayText[1]);
        if (msg.AcceptYesKeyFlag)
            btnYes.setEnabled(true);
        if (msg.CancelKeyFlag) {
            btnCancel.setEnabled(true);
        }
        if (msg.OKKeyFlag) {
            btnOK.setEnabled(true);
        }
        if (msg.DeclineNoKeyFlag) {
            btnNo.setEnabled(true);
        }
    }

    @Override
    public void onStatusEvent(EFTStatusResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onSettlementEvent(EFTSettlementResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onGetLastTransactionEvent(EFTGetLastTransactionResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onReprintReceiptEvent(EFTReprintReceiptResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onControlPanelEvent(EFTControlPanelResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onSetDialogEvent(EFTSetDialogResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onPinpadBusyEvent(EFTPinpadBusyResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onQueryCardEvent(EFTQueryCardResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onGenericCommandEvent(EFTGenericCommandResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onClientListEvent(EFTClientListResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onChequeAuthEvent(EFTChequeAuthResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onCloudPairEvent(EFTCloudPairResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));

        if (msg.Token != null) {
            _Settings.setCloudToken(msg.getToken());
            _Settings.SaveSettings();
        }
        if (msg.SuccessFlag){
            _isConnected = true;
        }
    }

    @Override
    public void onCloudTokenLogonEvent(EFTCloudTokenLogonResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
        if (msg.SuccessFlag){
            _isConnected = true;
        }
    }

    @Override
    public void onDefaultResponseEvent(String msg) {
        textArea1.append("\n");
        textArea1.append(msg.toString());
    }

    @Override
    public void onDisconnect() {
        System.out.println("SSL DISCONNECT");
        _isConnected = false;
    }
    //endregion
}
