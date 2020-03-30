# Introduction - Developing With Android Studio Using IP.Interface.Java

<b>*Note: This Readme only goes in depth for android specific development. For more information regarding the functionality of the interface please refer to the Readme.md inside of the IP.Interface.Java directory*</b>

## Requirements For Developing With The TCP/IP Interface (Android):

- sdk.eftclient.java.jar
- PCEFTPOS/Linkly cloud account (https://pceftpos.com/apidoc/REST/#appendix-h-cloud-account)
- Pinpad connected to the cloud (Can use [virtual pinpad](https://pceftpos.com/apidoc/REST/#appendix-i-virtual-pin-pad) if no physical terminal accessible)

The project is set up to build straight inside visual studio, so clone the repo and have a play around with the demo.

## Setting Up The External Jar As A Library:

![setupjar](README_PIcS\setupjar.PNG)

>Package Name: pceft.sdk.eftclient.java

# Developing

```java
public class StaticControls {
    // Connecting Via SSL To PCEFTPOS.CLOUD
    public static AsyncSSLSocketControl ctrlAndroidSSL;
}
```

Due to the nature of android, all the socket connections functionality (connecting, sending requests, etc.) must run inside of an Asynchronous Task. In the code below, an Asynchronous Task is created with the parameter "Integer" & return type "Boolean", however these are not required to be any specific type.

```java
//Example Simple AsyncConnect Class Using AsyncTask
public class AsyncConnect extends AsyncTask<Integer, Void, Boolean> {
    //public String returnString = "";
    @RequiresApi(api = Build.VERSION_CODES.O) //<- due to setting date in Txn Request
    @Override
    protected Boolean doInBackground(Integer... integers) {
        try {
            for (int i = 0; i < integers.length; i++) {
                if (integers[i] == 0) { //REFRESH SOCKET
                    StaticControls.ctrlAndroidSSL = new AsyncSSLSocketControl(this, "pos.sandbox.cloud.pceftpos.com", 443);
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
            }
            return Boolean.TRUE;
        }catch(Exception ex){
            ex.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
```

Each function sends a EFTRequest. For example the code for sending a Token Logon would look like:

```java
public void DoCloudLogon(){
        EFTCloudTokenLogonRequest tknLgn = new EFTCloudTokenLogonRequest();
        tknLgn.setToken(StaticControls.Token);
            try {
                StaticControls.ctrlAndroidSSL.socketSend(tknLgn);
            } catch (Exception ex) {
                ex.PrintStackTrace();
            }
    }
```

Once this AsyncTask class has been created, it can be called from inside the Main Activity using the call:

> new AsyncConnect().execute(Integer... integers)

```java
try{
	new AsyncConnect().execute(1); //Cloud Logon Call
} catch (Exception ex) {
 	ex.PrintStackTrace();
}
```

<b>Note: Internet permissions also need to be added to the manifest.xml</b>

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

### Things to consider with android development:

- Although both socket control types are supported, most android POS' will be using the cloud. Cloud settings (username, password & token) should be saved securely
- One instance of the AsyncTask can only be executed once
- An AsyncTask might not finish before the next line of code is executed, so anything that relies on the Request finishing (e.g. dialog pop-up with Response Code) should be done in the "onPostExecute" function.

# Running IP.Interface.Java.SimpleAndroidDemo

![androiddemopos](README_PICS\demopos.PNG)

*Early version of the Android Demo*

A simple android demo POS is included to help with building an Android POS with the TCP/IP Interface. The project is setup to build in Android Studio to any android device (Virtual or non-virtual).

## Settings File

![Settings](README_PICS\settings.PNG)

The android demo app saves the Cloud Settings inside of a JSON file located inside the files directory of the android device.

```java
context.getFilesDir() + "\\javademopos.json")
```

## Basic App Flow

![FlowDiagram](README_PICS\SimpleFlowDiagram.png)

After a request/response completes, the Demo will display the Response Text & Response Code in a Dialog Pop-up.

# Contact Us 

<b>Send an email to devsupport@pceftpos.com.au for:</b>

- Enquiries into developing with the Java Interface
- Reporting any bugs/issues found when using the interface


# Release Notes

## 1.0.0.0 - Callum Hands 4/03/2020

- Initial Android Demo Commit
- Contains AsyncConnect AsyncTask for performing requests and responses with the AsyncSSLSocketControl