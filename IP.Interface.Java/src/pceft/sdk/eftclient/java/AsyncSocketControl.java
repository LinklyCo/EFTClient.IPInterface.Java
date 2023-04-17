package pceft.sdk.eftclient.java;

import java.io.IOException;
import java.nio.channels.ClosedSelectorException;
import java.util.NoSuchElementException;

public class AsyncSocketControl extends SocketEventListener {
    public AsyncSocket socket;

    public AsyncSocketControl(PCEFTPOSEventListener listener, String hostname, int port) {
        super(listener);

        socket = new AsyncSocket(hostname, port, this);
        socket.start();

        //Give the socket thread some runtime to get a head-start on connection processing
        Thread.yield();
    }

    @Override
    public void socketSend(EFTRequest request) throws IOException, ClosedSelectorException, NoSuchElementException {
        socket.write(request);
        //Give the socket thread some runtime to get a head-start on sending data
        Thread.yield();
    }

    public void socketSendRaw(String request) throws Exception {
        socket.writeRaw(request);
        //Give the socket thread some runtime to get a head-start on sending data
        Thread.yield();
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public boolean close() {
        return close(1000);
    }

    public boolean close(int maxWaitMillis) {
        socket.close();
        try {
            socket.join(maxWaitMillis);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
