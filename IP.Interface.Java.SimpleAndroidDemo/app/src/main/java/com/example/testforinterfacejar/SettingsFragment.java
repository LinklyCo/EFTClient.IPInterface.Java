package com.example.testforinterfacejar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static com.example.testforinterfacejar.MainFragment.receiptbox;

public class SettingsFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    static TextView numberAmtbox;
    @SuppressLint("StaticFieldLeak")
    static TextView usernameTxtBox;
    @SuppressLint("StaticFieldLeak")
    static TextView passwordTxtBox;
    @SuppressLint("StaticFieldLeak")
    static TextView paircodeTxtBox;
    @SuppressLint("StaticFieldLeak")
    static Spinner txnTypeDropDown;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //INITIALIZE THE OBJECTS FOR LATER ACCESSING
        numberAmtbox = getView().findViewById(R.id.amtTxtBox);
        usernameTxtBox = getView().findViewById(R.id.usernameTxtBox);
        passwordTxtBox = getView().findViewById(R.id.passwordTxtBox);
        paircodeTxtBox = getView().findViewById(R.id.paircodeTxtBox);
        txnTypeDropDown = getView().findViewById(R.id.dropDownType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.txn_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        txnTypeDropDown.setAdapter(adapter);
        SettingsDemoPos.context = getContext();
        SettingsDemoPos.LoadSettings();
        Button btnPair = (Button) getView().findViewById(R.id.btnPairCode) ;
        btnPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    /**
                     * Perform a cloud pair with the variables in the above text fields
                     */
                    MainFragment.cxt = getContext();
                    new AsyncConnect().execute(4); //CLOUD PAIR
                } catch (Exception ex) {
                    receiptbox.setText("Connection Failed With Exception: " + ex.getMessage());
                }
            }
        });
        Button btnRefreshSocket = (Button) getView().findViewById(R.id.btnRefreshSocket);
        btnRefreshSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    /**
                     * Refresh the socket connection
                     */
                    MainFragment.cxt = getContext();
                    StaticControls._IsConnected = false;
                    new AsyncConnect().execute(0);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                    receiptbox.setText("Connection Failed With Exception: " + ex.getMessage());
                }
            }
        });
    }
}