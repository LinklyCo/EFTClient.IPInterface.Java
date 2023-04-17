package pceft.sdk.eftclient.java;

import java.util.concurrent.atomic.AtomicInteger;

public class PCEFTPOSControlAsync extends SocketEventListener {
    public AsyncSocketThread socketThread;
    public AtomicInteger msgRead = new AtomicInteger(0);
    public AtomicInteger msgWritten = new AtomicInteger(0);

    public PCEFTPOSControlAsync(PCEFTPOSEventListener listener) {
        super(listener);
        try {
            socketThread = new AsyncSocketThread("127.0.0.1", 2011, new AtomicInteger(0), this);
        } catch (Exception e) {
            System.out.println(String.format("Could not connect to client with the following reason: %s", e.getMessage()));
        }

    }

    @Override
    public void socketSend(EFTRequest request) {
        try {
            socketThread.startWrite(socketThread.sockChannel, request, msgWritten);
        } catch (Exception e) {
            System.out.println(String.format("Could not write to the client due to the following reason: %s", e.getMessage()));
        }
    }
}
