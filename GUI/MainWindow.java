import javax.swing.*;
import java.awt.event.ItemEvent;
import java.sql.Date;
import java.time.Instant;

public class MainWindow implements PCEFTPOSEventListener{

    //region Frame Elements
    private JPanel Panel1;
    private JButton btnDoThing;
    private JButton btnClose;
    private JTextArea textArea1;
    private JComboBox<String> cmbTransactions;
    private JTextField txtPurchaseAmt;
    private JTextField txtCashOutAmt;
    private JButton btnClearText;
    private JComboBox<String> cmbOptions;
    private JTextField txtDialogTitle;
    private JTextField txtIPAddress;
    private JTextField txtPort;
    private JButton btnChangeSocket;

    private PCEFTPOSControl ctrl;
    private DisplayDialog dd = null;
    //endregion

    public static void main(String[] args ) {
        JFrame frame = new JFrame("Main Window");
        frame.setContentPane(new MainWindow().Panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private MainWindow() {

        //region Add Stuff to Frame
        cmbTransactions.addItem("Logon");
        cmbTransactions.addItem("Transaction");
        cmbTransactions.addItem("Status");
        cmbTransactions.addItem("Settlement");
        cmbTransactions.addItem("Get Last Transaction");
        cmbTransactions.addItem("Reprint Receipt");
        cmbTransactions.addItem("Set Dialog");
        cmbTransactions.addItemListener(e ->
        {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                switch (cmbTransactions.getSelectedIndex()) {
                    case 0:
                        cmbOptions.removeAllItems();
                        cmbOptions.addItem("Standard");
                        cmbOptions.addItem("TMS");
                        cmbOptions.addItem("RSA");
                        txtDialogTitle.setEnabled(false);
                        break;
                    case 1:
                        cmbOptions.removeAllItems();
                        cmbOptions.addItem("Purchase");
                        cmbOptions.addItem("Refund");
                        cmbOptions.addItem("CashOut");
                        txtDialogTitle.setEnabled(false);
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
                    default:
                        cmbOptions.removeAllItems();
                        txtDialogTitle.setEnabled(false);
                        break;
                }
            }
        });
        btnChangeSocket.addActionListener(e -> DoChangeSocket());
        //ctrl = new PCEFTPOSControlAsync(this);
        ctrl = new PCEFTPOSControl(this, txtIPAddress.getText(), Integer.parseInt(txtPort.getText()));
        btnDoThing.addActionListener(e -> DoTransaction());
        btnClose.addActionListener(e -> System.exit(0));
        btnClearText.addActionListener(e -> ClearTextArea());
        cmbTransactions.setSelectedIndex(1);
        //endregion

    }

    private void DoChangeSocket() {
        textArea1.setText(ctrl.close());
        ctrl = new PCEFTPOSControl(this, txtIPAddress.getText(), Integer.parseInt(txtPort.getText()));
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

        ctrl.socketSend(reprint);
    }

    private void DoGetLastTransaction() {
        EFTGetLastTransactionRequest getLast = new EFTGetLastTransactionRequest();
        ctrl.socketSend(getLast);
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
        ctrl.socketSend(settle);
    }

    private void DoPurchase() {
        EFTTransactionRequest newTxn = new EFTTransactionRequest();
        try {
            newTxn.AmtPurchase = Double.parseDouble(txtPurchaseAmt.getText());
            newTxn.AmtCash = Double.parseDouble(txtCashOutAmt.getText());
        } catch (Exception ex) {
            //Alert r = new Alert(Alert.AlertType.ERROR, "Please Enter A Valid Amount", ButtonType.OK);
            JOptionPane.showMessageDialog(null, "Please Enter A Valid Amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
            //r.show();
        }
        newTxn.Merchant = "00";
        newTxn.Application = EFTTransactionRequest.TerminalApplication.EFTPOS;
        newTxn.TxnRef = "1234";
        newTxn.BankDate = Date.from(Instant.now());
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

        ctrl.socketSend(newTxn);
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
        ctrl.socketSend(Logon);
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

        ctrl.socketSend(Status);
    }
    //endregion


    //region Events
    @Override
    public void onStatusEvent(EFTStatusResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onLogonEvent(EFTLogonResponse msg) {
//        textArea1.setText("\n");
//        textArea1.append("Logon Event Received\n");
//        if(msg.Success)
//            textArea1.append("Successful Logon");
//        else
//            textArea1.append("Failed Logon");
//        textArea1.append("Response Code: " + msg.ResponseCode + "\n");
//        textArea1.append("Response Text: " + msg.ResponseText + "\n");
//        textArea1.append("Merchant ID: " + msg.Caid + "\n") ;
//        textArea1.append("Terminal ID: " + msg.Catid + "\n");
//        textArea1.append("Date: " + msg.BankDate + "\n");
//        textArea1.append("Pinpad Version: " + msg.PinPadVersion + "\n");
//        textArea1.append("Purchase Analysis Data: " + msg.PurchaseAnalysisData + "\n");
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onReceiptEvent(EFTReceiptResponse msg) {
//        textArea1.setText("\n");
//        textArea1.append("Receipt Event Received\n");
//        textArea1.append("ReceiptType=" + msg.Receipt + '\n');
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));

        if (msg.Receipt.Receipt == 'R')
            for (String s : msg.ReceiptText
                    ) {
                textArea1.append(s + '\n');
            }
        ctrl.socketSend( new EFTReceiptRequest() );
    }

    @Override
    public void onTransactionEvent(EFTTransactionResponse msg) {
//        textArea1.setText("\n");
//        textArea1.append("Transaction Event Received\n");

        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onDisplayEvent(EFTDisplayResponse r) {
        if(dd == null) {
            dd = new DisplayDialog();
            dd.textArea1.setText(r.DisplayText[0]);
            dd.textArea2.setText(r.DisplayText[1]);
            dd.pack();
            dd.buttonOK.addActionListener(e -> ctrl.socketSend(new EFTSendKeyRequest()));
            dd.buttonCancel.addActionListener(e -> ctrl.socketSend(new EFTSendKeyRequest()));
            dd.setVisible(true);
        } else {
            dd.textArea1.setText(r.DisplayText[0]);
            dd.textArea2.setText(r.DisplayText[1]);
            dd.setVisible(true);
        }
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
        for (String s : msg.ReceiptText
                ) {
            textArea1.append(s + '\n');
        }
        ctrl.socketSend(new EFTReceiptRequest());
    }

    @Override
    public void onControlPanelEvent(EFTControlPanelResponse msg) {
        textArea1.append("\n");
        textArea1.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        textArea1.append(json.toString(1));
    }

    @Override
    public void onSetDialogEvent(EFTSetDialogResponse msg){
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

    //endregion

}
