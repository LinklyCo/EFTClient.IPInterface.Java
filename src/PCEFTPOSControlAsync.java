import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class PCEFTPOSControlAsync implements SocketEventListener {
    AsyncSocketThread socketThread;
    PCEFTPOSEventListener listener;
    AtomicInteger msgRead = new AtomicInteger(0);
    AtomicInteger msgWritten = new AtomicInteger(0);

    public PCEFTPOSControlAsync(PCEFTPOSEventListener _listener) {
        listener = _listener;
        try {
            socketThread = new AsyncSocketThread("127.0.0.1", 2011, new AtomicInteger(0), this);
        } catch (IOException e) {
            System.out.println("Could not connect to client.");
            e.printStackTrace();
        }

    }

    public void socketSend(EFTRequest request) {
        try {
            socketThread.startWrite(socketThread.sockChannel, request, msgWritten);
        } catch (Exception e) {
            System.out.println("Could not write to the client.");
            e.printStackTrace();
        }
    }

    @Override
    public void socketReceive(String s) {
        MessageParser msg = new MessageParser();
        EFTResponse r = msg.parseMsgStr(s);
        // cmd code should probably be mapped to a number
        if (r instanceof EFTLogonResponse)
            listener.onLogonEvent((EFTLogonResponse) r);
        else if (r instanceof EFTReceiptResponse)
            listener.onReceiptEvent((EFTReceiptResponse) r);
        else if (r instanceof EFTTransactionResponse)
            listener.onTransactionEvent((EFTTransactionResponse) r);
        else if (r instanceof EFTDisplayResponse)
            listener.onDisplayEvent((EFTDisplayResponse) r);
        else if (r instanceof EFTStatusResponse)
            listener.onStatusEvent((EFTStatusResponse) r);
        else if (r instanceof EFTSettlementResponse)
            listener.onSettlementEvent((EFTSettlementResponse) r);
        else if (r instanceof EFTGetLastTransactionResponse)
            listener.onGetLastTransactionEvent((EFTGetLastTransactionResponse) r);
        else if (r instanceof EFTReprintReceiptResponse)
            listener.onReprintReceiptEvent((EFTReprintReceiptResponse) r);
    }
}
