package com.example.testforinterfacejar;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class SettingsDemoPos {

    /**
     * Application context, required for saving to the FilesDir(), not required to be saved however
     */
    public static Context context;

    /**
     * Username, Password, Token
     * Username & password stored autofills the Username + password textboxes on reload
     * Token stored for Cloud logons
     */
    public static String CloudUsername = "";
    public static String getCloudUsername() {
        return CloudUsername;
    }
    public static void setCloudUsername(String cloudUsername) {
        CloudUsername = cloudUsername;
    }

    public static String CloudPassword = "";
    public static String getCloudPassword() {
        return CloudPassword; }
    public static void setCloudPassword(String cloudPassword) {
        CloudPassword = cloudPassword;
    }

    public static String CloudToken = "";
    public static String getCloudToken() {
        return CloudToken;
    }
    public static void setCloudToken(String cloudToken) {
        CloudToken = cloudToken;
    }

    public static void SaveSettings(){
        /**
         * Save the settings file inside of a .json file
         * context.getFilesDir() saves inside data/com.example.testforinterfacejar
         */
        org.json.simple.JSONObject _settingsJSON = new org.json.simple.JSONObject();
        setCloudToken(StaticControls.Token);
        setCloudPassword(SettingsFragment.passwordTxtBox.getText().toString());
        setCloudUsername(SettingsFragment.usernameTxtBox.getText().toString());
        _settingsJSON.put("cloudUsername", getCloudUsername());
        _settingsJSON.put("cloudPassword",getCloudPassword());
        _settingsJSON.put("cloudToken", getCloudToken());

        try {
            FileOutputStream f = new FileOutputStream( context.getFilesDir() + "\\javademopos.json", false);
            f.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter file = new FileWriter(context.getFilesDir() + "\\javademopos.json")) {
            file.write(_settingsJSON.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void LoadSettings(){

        JSONParser parser = new JSONParser();
        try (FileReader file = new FileReader(context.getFilesDir() + "\\javademopos.json")) {
            Object obj = parser.parse(file);
            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;

            setCloudUsername((String) jsonObject.get("cloudUsername"));
            setCloudPassword((String) jsonObject.get("cloudPassword"));
            setCloudToken((String) jsonObject.get("cloudToken"));
            StaticControls.Token = CloudToken;
            SettingsFragment.passwordTxtBox.setText(CloudPassword);
            SettingsFragment.usernameTxtBox.setText(CloudUsername);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exc)
        {
            exc.printStackTrace();
        }

    }

}

