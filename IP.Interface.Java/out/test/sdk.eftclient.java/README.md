# Introduction

The PCEFTPOS IPInterface.Java implements the following most common PCEFTPOS APIs:
1. ControlPanel request and response.
2. Display request and response.
3. GetLastTransaction request and response.
4. Logon request and response.
5. PinpadBusy response.
6. Receipt request and response.
7. SendKey request.
8. SetDialog request and response.
9. Settlement request and response.
10. Status request and response.
11. Transaction request and response.

More information regarding the requests and responses can be found at https://www.pceftpos.com/apidoc/TCPIP.
There are two asynchronous socket classes and one synchronous socket class included in the project.
One async socket uses AtomicIntegers to control concurrency, the other uses Selectors, Channels, and Keys. Feel free to use either, or to implement your own favoured sockets.


# Getting Started

>View README-SETUP for in-depth tutorials on setting up the project

- Clone the repo from the [Public PC-EFTPOS Github](https://github.com/pceftpos/EFTClient.IPInterface.Java)
- If running the project with the test POS, open the project in IntelliJ IDEA, and select AsyncMainWindow as the main class
- Build & run the project


# Build and Test

The default starting class is the async socket class that uses Selectors, Channels, and Keys. General flow is:
* Create Request
* Send request on socket
* Check socket for response (in this instance the loop runs constantly)
* Raise corresponding response event
* Handle event i.e. displays, receipts etc.

A class that implements PCEFTPOSEventListener is required.

In this project the AsyncSocketControl, PCEFTPOSControl, and PCEFTPOSControlAsync implement the SocketEventListener.
In their constructors, they take in a PCEFTPOSEventListener as a parameter, and the event method calls are passed to this object.

The Frame/Window/Dialog classes implement the PCEFTPOSEventListener as that is the endpoint for the events. What I mean by that is, we care about the events being raised at the UI level because we want to know what has happened to the request.

![SocketDiagram](README_PICS\SocketDiagram.png)

* Step 1:
Add the .JAR as an external library.
* Step 2:
Create a GUI form class and implement PCEFTPOSEventListener and create the requisite method overrides.
* Step 3:
Create a new PCEFTPOSControl class (AsyncSocketControl, PCEFTPOSControlAsync) passing the Gui class as one of the parameters
* Step 4:
Use the Control.socketSend(EFTRequest request) method to send a request to the client.
* Step 5:
Handle the returned event.

# Using The Java Interface

### Implementing PCEFTPOSEventListener

![as](README_PICS\implement.PNG)

The Listener includes "onEvent" methods for EFTRequests.

![override](README_PICS\override.PNG)

In the example above, we use the listener to display a receipt on the form every time we receive a "EFTReceiptResponse".

The SocketEventListener implements SocketReceive and SocketSend, these are the abstracted read and write methods to the socket object.
The SocketReceive method parses the message and passes the messages to the PCEFTPOSEventListener object through a method call.

i.e.
```java
if (EFTResponse r instanceof EFTTransactionResponse)
    listener.onTransactionEvent(r);
```

The PCEFTPOSEventListener will handle the events raised by the SocketEventListener.

i.e.
```java
@Override
public void onTransactionEvent(EFTTransactionResponse msg){
//Do stuff
}
```

For example, if after a transaction, the POS wants to display the Response class in a text box "DisplayBox",
it can be done as simply as:

```java
@Override
public void onTransactionEvent(EFTTransactionResponse msg) {
        DisplayBox.append("\n");
        DisplayBox.append(msg.getClass().toString());
        JSONObject json = new JSONObject(msg);
        DisplayBox.append(json.toString(1));
}
```

This will only fire every time a EFTTransactionResponse is received.

In each 'onEvent' the response value 'msg' contains the full response body, which we can access individually. For example, if I wanted
to save the Token received:

```java
 @Override
    public void onCloudPairEvent(EFTCloudPairResponse msg) {
        if (msg.getToken() != null) {
            _Settings.setCloudToken(msg.getToken());
            _Settings.Save();
        }
    }
``` 

### Setting Up The Control (Async vs SSLAsync)

There are two socket controls to use in IP.Interface.Java. AsyncSocketControl & AsyncSSLSocketControl.

<b>AsyncSocketControl does not use SSL or any other security protocol, and is reccomended for local machine connections i.e. connections to the client on localhost</b>

<b>AsyncSSLSocketControl is for connections that require a SSL Handshake to be completed, i.e. connections to the PC-EFTPOS cloud</b>

Both need to be initialized inside the main class of the POS program.
```$aidl
private AsyncSSLSocketControl ctrl;
```

Both controls have a constructor that requires:
- A PCEFTPOSEventListener
- A Hostname to connect to
- The port of the endpoint

```java
    AsyncSSLSocketControl(PCEFTPOSEventListener _listener, String hostname, int port) {
        listener = _listener;
        socket = new AsyncSSLSocket(hostname, port, this);
        thread = new Thread(socket);
        thread.start();
    }
```

In AsyncMainWindow, the control is re-initialized everytime the 'Set Socket Connection' button is pressed
```java
ctrl = new AsyncSSLSocketControl(this, txtIPAddress.getText(), Integer.parseInt(txtPort.getText()));
```

###SSL Socket Event Listener

The AsyncSSLSocketControl class also has an optional SSL Listener, which if used, needs to be initialized, e.g.
 ```
ctrl.setSslListener(this);
 ```

The onDisconnect function can be overridden inside the class implementing SSLSocketListener.

```java
    @Override
    public void onDisconnect() {
        //SSL DISCONNECT CODE
    }
```

### Calling A Request
First the Socket Control needs to be initialized inside the main window:
```java
private AsyncSocketControl ctrl;
```
The request then needs to be initialized, before it gets sent through the socket control.

```java
private void DoPurchase() {
        EFTTransactionRequest purchaseReq = new EFTTransactionRequest();
        purchaseReq.AmtPurchase = 10.00;
        purchaseReq.AmtCash = 0.00;
        purchaseReq.Merchant = "00";
        purchaseReq.Application = EFTTransactionRequest.TerminalApplication.EFTPOS;
        purchaseReq.TxnRef = "1234";
        purchaseReq.BankDate = Date.from(Instant.now());
        purchaseReq.TxnType = EFTTransactionRequest.TransactionType.PurchaseCash;
        if (ctrl.socket.isConnected())
            try {
                ctrl.socketSend(purchaseReq);
            } catch (Exception e) {
                "Socket write failed with the following exception: %s %s", e.toString(), e.getMessage();
            }
```

The request is converted into a request string, like the below example:
```$xslt
#0158M000P000000000000000001000000001234            00 ********************    **************************************** 00               P120220
```

#### Important Values In Request String

|Byte|Value|Description|
|-------|-----------|-----------|
|0      |#          |Start Flag |
|1-4    | 0158      |Length Of Message|
|5      | M         | Command Code for transaction requests/responses |
|7-8    | 00        | Merchant|
|9      |P          |TxnType ("P" In This Case)|
|21-29  |000000100  | Amount Purchase In A Padded String|

Check out the message specification inside of the [API Documentation](https://pceftpos.com/apidoc/TCPIP/#message-specification) for TCPIP or the README-WIKI.md file for more information regarding request/response strings

# Contact Us 

<b>Send an email to devsupport@pceftpos.com.au for:</b>
- Enquiries into developing with the Java Interface
- Reporting any bugs/issues found when using the interface


# Release Notes

##v1.0.0.0 - Callum Hands

- Added AsyncSSLSocket for socket connections to pos.cloud.pceftpos
- Added Remaining EFTRequests, according to the [API Documentation for TCPIP](https://pceftpos.com/apidoc/TCPIP/#message-specification)

##v0.0.9.0 - Samuel Coianiz
- Initial Commit
    - Implemented most common EFTRequests 