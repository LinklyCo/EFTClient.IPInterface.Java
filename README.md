# Introduction 
TODO: Give a short introduction of your project. Let this section explain the objectives or the motivation behind this project.
This is essentially a copy of the C# IPInterface module from PC-EFTPOS. It doesn't have all the classes the C# does, but it has the ones that most POS vendors will use, and exempts the niche classes.
Most of the classes have explicit getters and setters for the properties. The setters are there because I was copying the C#, and the {get;set;} syntax didn't work. I realised some time through that it was unnecessary.
However, the JSON.org library I've included to make response displaying a bit easier requires the getProperty and isProperty methods to be defined in the class so it can work.
If you are using this for your POS, strictly speaking you don't need either of these methods because they're implicit (unless you want private properties).

There are two types of asynchronous sockets with their own PCEFTPOSControl classes.
The first one I did AsyncSocketThread uses AtomicIntegers as a way to control concurrency (?) but tbh I don't really get it.
It also had the issue of recursively calling the read function using the same buffer. This would be ok, but the buffer doesn't actually get cleared using the .clear() function, it just pretends to clear it.

I had a look around the internets and came across the template for the AsyncSocket class, which uses Selectors, Channels and Keys.
Tbh I don't really understand this one either but it looks a little nicer, it runs in its own thread and it doesn't keep the same buffer so I'm not getting the previous transaction receipt data in my purchaseAnalysisData field.

I've elected to keep them both in, just in case someone prefers the use of one over the other.


--------------I've changed EFT classes up to GetLastTransaction, changing access modifiers to package-private or private where IntelliJ recommends.--------------


# Getting Started
TODO: Guide users through getting your code up and running on their own system. In this section you can talk about:
1.	Installation process

* Clone the repository.
* Open project.
* Build.
* Cross fingers.
* ???
* Profit.

I built a .JAR file excluding the dialogs (and including the dialogs) and was able to use the classes just fine.
In fact, using the JAR with the window classes, I could build the whole project with one method
    public static void main([] args){
    AsyncMainWindow.main(null);
    }

and it created the UI, the sockets, and I was able to carry on like I had the original project.

# Build and Test
TODO: Describe and show how to build your code and run the tests. 
This is using the IPClient, not the IPClientAsync, from the C# code. As such, you need to check the responses and raise events for the POS to handle. General flow is:
* Create Request
* Send request on socket
* Check socket for response (in this instance the loop runs constantly)
* Raise corresponding response event
* Handle event i.e. displays, receipts etc.

You will need a class that implements PCEFTPOSEventListener.

The SocketEventListener implements SocketReceive and SocketSend, these are the abstracted read and write methods to the socket object.

The SocketReceive method parses the message and passes the messages to the PCEFTPOSEventListener object through a method call.

i.e.
if (EFTResponse r instanceof EFTTransactionResponse)
    listener.onTransactionEvent(r);

The PCEFTPOSEventListener will handle the events raised by the SocketEventListener.

i.e.

@Override
public void onTransactionEvent(EFTTransactionResponse msg){
//Do stuff
}
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

If you want to learn more about creating good readme files then refer the following [guidelines](https://www.visualstudio.com/en-us/docs/git/create-a-readme). You can also seek inspiration from the below readme files:
- [ASP.NET Core](https://github.com/aspnet/Home)
- [Visual Studio Code](https://github.com/Microsoft/vscode)
- [Chakra Core](https://github.com/Microsoft/ChakraCore)