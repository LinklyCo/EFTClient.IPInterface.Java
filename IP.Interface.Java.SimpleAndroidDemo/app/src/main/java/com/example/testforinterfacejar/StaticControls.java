package com.example.testforinterfacejar;

import pceft.sdk.eftclient.java.AsyncSSLSocketControl;
import pceft.sdk.eftclient.java.AsyncSocketControl;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Class for variables and functions that will be used across the entire project
 *
 *  SocketControls - instance of the SocketControl, initialized here so they can be accessed and modified within the whole project
 *
 *  Return/Response Strings - for receipts & dialog displays
 *
 *  Token - For performing logons. A Logon must be done before attempting to perform transactions when using cloud
 */
public class StaticControls {
    public static AsyncSocketControl ctrlAndroid;
    public static AsyncSSLSocketControl ctrlAndroidSSL;

    public static String ReturnString = "";

    public static String ResponseStringHeader = "";
    public static String ResponseStringMessage = "";

    public static boolean ConnectExecute = false;

    public static boolean ShowDialog = false;

    public static String Token = "";

    public static void DialogExecute(){
        ShowDialog = true;
        //Execute AsyncConnect with a non-existing command, meaning we skip right to onPostExecute
        new AsyncConnect().execute(-1);
    }

    public static boolean inRequest = false;

    /**
     * Send a receipt request
     */
    public static void ReceiptSend() {
        new AsyncConnect().execute(3);
    }

    public static void ReceiptText() {
        MainFragment.receiptbox.setText(ReturnString);
    }

    public static boolean _IsConnected = false;
}
