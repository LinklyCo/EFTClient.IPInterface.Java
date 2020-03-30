package com.example.testforinterfacejar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    static TextView receiptbox;

    static Context cxt;

    public void Init(View view)
    {
        /**
         * Initiate the fragment with button listeners and other ids
         */
        receiptbox = (TextView) getView().findViewById(R.id.ReceiptBox);
        Button btnCloudLogon = (Button) getView().findViewById(R.id.btnCloudLogon);
        btnCloudLogon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    cxt = getContext();
                    new AsyncConnect().execute(1);
                } catch (Exception ex)
                {
                    receiptbox.setText("Connection Failed With Exception: " + ex.getMessage());
                }
            }
        });
        Button btnTxn = (Button) getView().findViewById(R.id.btnDoTxn) ;
        btnTxn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    /**
                     * Send transaction request if the POS is Connected
                     */
                    cxt = getContext();
                    if (StaticControls._IsConnected) {
                        new AsyncConnect().execute(2);
                    }
                    else{
                        receiptbox.setText("Cannot Perform Transaction As Socket Has Not Logged On");
                    }
                } catch (Exception ex) {
                    receiptbox.setText("Connection Failed With Exception: " + ex.getMessage());
                }
            }
        });
        try {
            /**
             * Initialise the socket on load
             * Requests will not able to be sent if the socket has not been initialized beforehand
             */
            cxt = getContext();
            new AsyncConnect().execute(0);
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            receiptbox.setText("Connection Failed With Exception: " + ex.getMessage());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Init(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        //Intent activity = new Intent(getActivity(), MainActivity.class);
        //startActivity(activity);
        return view;
    }
}
