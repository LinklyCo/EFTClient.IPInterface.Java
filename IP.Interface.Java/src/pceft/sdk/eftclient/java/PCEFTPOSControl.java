package pceft.sdk.eftclient.java;

public class PCEFTPOSControl extends SocketEventListener {

    public SocketThread sckThread;

    public PCEFTPOSControl(PCEFTPOSEventListener listener, String hostname, int port)
    {
        super(listener);

        try {
            // Create a new socket thread
            sckThread = new SocketThread(hostname, port, this);
            sckThread.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void socketSend(EFTRequest eftRequest) {
        sckThread.socketSend(eftRequest);
    }

    public String close() {
        return sckThread.close();
    }
}