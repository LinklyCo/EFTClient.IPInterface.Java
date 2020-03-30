package com.example.testforinterfacejar;
import pceft.sdk.eftclient.java.*;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import java.sql.Date;
import java.time.Instant;

import static com.example.testforinterfacejar.SettingsFragment.*;

public class AsyncConnect extends AsyncTask<Integer, Void, Boolean> implements PCEFTPOSEventListener,SSLSocketListener  {
    AlertDialog alertDialog;
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Runs after the doInBackground function has finished
     */
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (StaticControls.ShowDialog && StaticControls.ResponseStringHeader != null && !StaticControls.ResponseStringHeader.equals("")){
            /**
             * Build the AlertDialog
             */
            alertDialog = new AlertDialog.Builder(MainFragment.cxt).create();
            alertDialog.setTitle(StaticControls.ResponseStringHeader);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setMessage(StaticControls.ResponseStringMessage);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("CLICKED OK");
                        }
                    });
            /**
             * Display the dialog pop-up
             */
            alertDialog.show();
            /**
             * Reset the values so that this doesn't call again when not needed,
             * and that it doesn't call with an incorrect value
             */
            StaticControls.ShowDialog = false;
            StaticControls.ResponseStringHeader = "";
            StaticControls.ResponseStringMessage = "";
        }
        if (StaticControls.ConnectExecute) {
            // So this part of the function doesn't call every time after ConnectExecute has been turned on
            StaticControls.ConnectExecute = false;
            /**
             * Socket initialization doesn't have an OnComplete event, so we check if it's connected here and display in a Dialog Pop-up
             */
            if (StaticControls.ctrlAndroidSSL.socket.isConnected()) {
                StaticControls.ResponseStringHeader = "Socket Connected";
                StaticControls.ResponseStringMessage = "Connected to pos.sandbox.cloud.peceftpos.com";
                /**
                 * Call the dialog execute to display the dialog pop-up
                 */
                StaticControls.DialogExecute();
            } else {
                StaticControls.ResponseStringHeader = "Connection Failure";
                StaticControls.ResponseStringMessage = "Failed to connect to pos.sandbox.cloud.pceftpos.com";
                StaticControls.DialogExecute();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Boolean doInBackground(Integer... integers) {
        try {
            /**
             * Stop recursive calls which can cause the pinpad to hang
             */
            if (StaticControls.inRequest)
            {
                throw new Exception("Another request is being sent");
            }
            StaticControls.inRequest = true;
            /**
             * For each integer in the paramater, do the corresponding request.
             * Due to the nature of AsyncTask, it's best to call this one at a time
             */
            for (int i = 0; i < integers.length; i++) {
                if (integers[i] == 0) { //REFRESH SOCKET
                    try {
                        StaticControls.ctrlAndroidSSL.close();
                    }catch(Exception e){
                        System.out.println("SSL Connection Close Failed");
                    }
                    StaticControls.ctrlAndroidSSL = new AsyncSSLSocketControl(this, "pos.sandbox.cloud.pceftpos.com", 443);
                    StaticControls.ConnectExecute = true;
                    StaticControls.inRequest = false;
                }
                else if (integers[i] == 1){ // Cloud Logon
                    DoCloudLogon();
                }
                else if (integers[i] == 2){ // DoTxn
                    DoTxn();
                }
                else if (integers[i] == 3){ // Receipt
                    DoReceipt();
                }
                else if (integers[i] == 4){ // Do Cloud Pair
                    DoCloudPair();
                }
                /**
                 * No request actually being sent, called up as -1 to ignore events and display dialog
                 */
                else if (integers[i] == -1){
                    StaticControls.inRequest = false;
                }
            }
            /**
             * Task finished without throwing, return TRUE
             */
            return Boolean.TRUE;
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            /**
             * Task finished due to an exception, return FALSE
             */
            return Boolean.FALSE;
        }
    }

    public void DoReceipt(){
        try {
            //Receipt request doesn't have any variables to initialize, so it can just be sent like this
            StaticControls.ctrlAndroidSSL.socketSend(new EFTReceiptRequest());
        } catch (Exception ex) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void DoTxn(){
        /**
         * Build a simple transaction request
         */
        EFTTransactionRequest newTxn = new EFTTransactionRequest();
        newTxn.Application = EFTTransactionRequest.TerminalApplication.EFTPOS;
        newTxn.TxnRef = "270220201003333 ";
        newTxn.BankDate = Date.from(Instant.now());
        /**
         * TransactionType selected in the spinner in the Settings Fragment
         */
        switch (txnTypeDropDown.getSelectedItemPosition())
        {
            case 0:
                newTxn.TxnType = EFTTransactionRequest.TransactionType.PurchaseCash;
                break;
            case 1:
                newTxn.TxnType = EFTTransactionRequest.TransactionType.Refund;
                break;
            case 2:
                newTxn.TxnType = EFTTransactionRequest.TransactionType.CashOut;
                break;
            default:
                newTxn.TxnType = EFTTransactionRequest.TransactionType.PurchaseCash;
                break;
        }
        /**
         * Parse text for Amt box to either AmtCash or AmtPurchase
         */
        try {
            if (newTxn.TxnType ==  EFTTransactionRequest.TransactionType.CashOut)
            {
                newTxn.AmtCash = Double.parseDouble(numberAmtbox.getText().toString());
                newTxn.AmtPurchase = 0;
            }
            else{
                newTxn.AmtPurchase = Double.parseDouble(numberAmtbox.getText().toString());
                newTxn.AmtCash = 0;
            }
        } catch (Exception ex) {
            if (newTxn.TxnType ==  EFTTransactionRequest.TransactionType.CashOut)
            {
                newTxn.AmtCash = 1.99;
                newTxn.AmtPurchase = 0;
            }
            else{
                newTxn.AmtPurchase = 1.99;
                newTxn.AmtCash = 0;
            }
        }
        newTxn.PurchaseAnalysisData = "";
        try {
            /**
             * Send the transaction request through the control
             */
            StaticControls.ctrlAndroidSSL.socketSend(newTxn);
        } catch (Exception e) {
            System.out.println("Cloud Token Logon Failed");
            System.out.println("MSG: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
            System.out.println("RAW: " + e.toString());
        }
    }

    public void DoCloudLogon(){
        /**
         * Send a cloud logon request using the token stored in static controls
         */
        EFTCloudTokenLogonRequest tknLgn = new EFTCloudTokenLogonRequest();
        tknLgn.setToken(StaticControls.Token);
        //if (ctrlAndroidSSL.socket.isConnected())
            try {
                StaticControls.ctrlAndroidSSL.socketSend(tknLgn);
            } catch (Exception e) {
                System.out.println("Cloud Token Logon Failed");
                System.out.println("MSG: " + e.getMessage());
                System.out.println("Cause: " + e.getCause());
                System.out.println("RAW: " + e.toString());
            }
    }

    public void DoCloudPair(){
        /**
         * Pair POS & Pinpad
         */
        EFTCloudPairRequest pair = new EFTCloudPairRequest();
        pair.setClientID(usernameTxtBox.getText().toString());
        pair.setPassword(passwordTxtBox.getText().toString());
        pair.setPairCode(paircodeTxtBox.getText().toString());
        try {
            StaticControls.ctrlAndroidSSL.socketSend(pair);
        } catch (Exception e) {
            System.out.println("Cloud Token Logon Failed");
            System.out.println("MSG: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
            System.out.println("RAW: " + e.toString());
        }
    }

    @Override
    public void onLogonEvent(EFTLogonResponse eftLogonResponse) {

    }

    @Override
    public void onReceiptEvent(EFTReceiptResponse eftReceiptResponse) {
        /**
         * Display the receipt received, then send back a receipt request
         * ReceiptSend() does {
         *         new AsyncConnect().execute(3);
         * }
         *
         */
        StaticControls.inRequest = false;
        StaticControls.ReturnString = "";
        for (int i = 0; i < eftReceiptResponse.ReceiptText.length;i++)
        {
            StaticControls.ReturnString += eftReceiptResponse.ReceiptText[i] + "\n";
        }
        StaticControls.ReceiptText();
        StaticControls.ReceiptSend();
    }

    @Override
    public void onTransactionEvent(EFTTransactionResponse eftTransactionResponse) {
        /**
         * Handle the response, in the code below we print out the response in json format, and save the response to staticcontrols
         * We display the response in a dialog pop-up, called in StaticControls.DialogExecute();
         */
        JSONObject json = new JSONObject(eftTransactionResponse);
        System.out.println(json.toString());
        StaticControls.inRequest = false;
        StaticControls.ReturnString = json.toString(1);
        StaticControls.ResponseStringHeader = eftTransactionResponse.ResponseText;
        StaticControls.ResponseStringMessage = "Response Code: " + eftTransactionResponse.getResponseCode();
        StaticControls.DialogExecute();
        //StaticControls.ReceiptText(); <- only displaying receipt which is sent to the POS in receipt response
    }

    @Override
    public void onDisplayEvent(EFTDisplayResponse eftDisplayResponse) {

    }

    @Override
    public void onStatusEvent(EFTStatusResponse eftStatusResponse) {

    }

    @Override
    public void onSettlementEvent(EFTSettlementResponse eftSettlementResponse) {

    }

    @Override
    public void onGetLastTransactionEvent(EFTGetLastTransactionResponse eftGetLastTransactionResponse) {

    }

    @Override
    public void onReprintReceiptEvent(EFTReprintReceiptResponse eftReprintReceiptResponse) {

    }

    @Override
    public void onControlPanelEvent(EFTControlPanelResponse eftControlPanelResponse) {

    }

    @Override
    public void onSetDialogEvent(EFTSetDialogResponse eftSetDialogResponse) {

    }

    @Override
    public void onPinpadBusyEvent(EFTPinpadBusyResponse eftPinpadBusyResponse) {

    }

    @Override
    public void onQueryCardEvent(EFTQueryCardResponse eftQueryCardResponse) {

    }

    @Override
    public void onGenericCommandEvent(EFTGenericCommandResponse eftGenericCommandResponse) {

    }

    @Override
    public void onClientListEvent(EFTClientListResponse eftClientListResponse) {

    }

    @Override
    public void onChequeAuthEvent(EFTChequeAuthResponse eftChequeAuthResponse) {

    }

    @Override
    public void onCloudPairEvent(EFTCloudPairResponse eftCloudPairResponse) {
        JSONObject json = new JSONObject(eftCloudPairResponse);
        System.out.println(json.toString());
        StaticControls.ReturnString = json.toString(1);
        StaticControls.ResponseStringHeader = eftCloudPairResponse.ResponseText;
        StaticControls.ResponseStringMessage = "Response Code: " + eftCloudPairResponse.getResponseCode();
        StaticControls.inRequest = false;
        StaticControls.DialogExecute();
        StaticControls.ReceiptText();
        if (eftCloudPairResponse.isSuccessFlag()){
            StaticControls._IsConnected = true;
            StaticControls.Token = eftCloudPairResponse.Token;
        }
        SettingsDemoPos.SaveSettings();
    }

    @Override
    public void onCloudTokenLogonEvent(EFTCloudTokenLogonResponse eftCloudTokenLogonResponse) {
        JSONObject json = new JSONObject(eftCloudTokenLogonResponse);
        System.out.println(json.toString());
        StaticControls.ReturnString = json.toString(1);
        StaticControls.ResponseStringHeader = eftCloudTokenLogonResponse.ResponseText;
        StaticControls.ResponseStringMessage = "Response Code: " + eftCloudTokenLogonResponse.getResponseCode();
        StaticControls.inRequest = false;
        StaticControls.DialogExecute();
        StaticControls.ReceiptText();
        if (eftCloudTokenLogonResponse.isSuccessFlag()){
            StaticControls._IsConnected = true;
        }
    }

    @Override
    public void onDefaultResponseEvent(String s) {

    }

    @Override
    public void onDisconnect() {

    }
}
