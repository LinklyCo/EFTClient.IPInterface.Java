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

1.	Installation process

* Clone the repository.
* Open project.
* Build.


I built a .JAR file without the dialogs (and one with the dialogs) and was able to use the classes just fine.
In fact, using the JAR with the window classes, I could build the whole project with one method
```
    public static void main([] args){
    AsyncMainWindow.main(null);
    }
```
and it created the UI, the sockets, and I was able to carry on like I had the original project.

# Build and Test

The default starting class is the async socket class that uses Selectors, Channels, and Keys. General flow is:
* Create Request
* Send request on socket
* Check socket for response (in this instance the loop runs constantly)
* Raise corresponding response event
* Handle event i.e. displays, receipts etc.

You will need a class that implements PCEFTPOSEventListener.
The SocketEventListener implements SocketReceive and SocketSend, these are the abstracted read and write methods to the socket object.
The SocketReceive method parses the message and passes the messages to the PCEFTPOSEventListener object through a method call.

i.e.
```
if (EFTResponse r instanceof EFTTransactionResponse)
    listener.onTransactionEvent(r);
```

The PCEFTPOSEventListener will handle the events raised by the SocketEventListener.

i.e.
```
@Override
public void onTransactionEvent(EFTTransactionResponse msg){
//Do stuff
}
```

In this project the AsyncSocketControl, PCEFTPOSControl, and PCEFTPOSControlAsync implement the SocketEventListener.
In their constructors, they take in a PCEFTPOSEventListener as a parameter, and the event method calls are passed to this object.

The Frame/Window/Dialog classes implement the PCEFTPOSEventListener as that is the endpoint for the events. What I mean by that is, we care about the events being raised at the UI level because we want to know what has happened to the request.


         +-----------+      +-----------------------+          +---------------------------+
         |           |      |               |       |          |              |            |
         |   Socket  +----->+ SocketListener|       |          | PCEFTPOS     |            |
         |           |      |               |       +----------> EventListener|            |
         +-----------+      +---------------+       |          |              |            |
                            |                       |          |              |            |
                            |                       |          +--------------+            |
                            |                       |          |                           |
                            |     Control Class     |          |                           |
                            |                       |          |      UI Class             |
                            |                       |          |                           |
                            |                       |          |                           |
                            |                       |          |                           |
                            +-----------------------+          +---------------------------+


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



# Contribute
If there is something missing, or something that doesn't work the way it should then please send an email to devsupport@pceftpos.com.au and we'll add it in/take it out.

