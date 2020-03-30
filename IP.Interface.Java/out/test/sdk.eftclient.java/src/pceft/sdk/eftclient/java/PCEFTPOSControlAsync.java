package pceft.sdk.eftclient.java;

import java.util.concurrent.atomic.AtomicInteger;

public class PCEFTPOSControlAsync implements SocketEventListener {
    public AsyncSocketThread socketThread;
    public PCEFTPOSEventListener listener;
    public AtomicInteger msgRead = new AtomicInteger(0);
    public AtomicInteger msgWritten = new AtomicInteger(0);

    public PCEFTPOSControlAsync(PCEFTPOSEventListener _listener) {
        listener = _listener;
        try {
            socketThread = new AsyncSocketThread("127.0.0.1", 2011, new AtomicInteger(0), this);
        } catch (Exception e) {
            System.out.println(String.format("Could not connect to client with the following reason: %s", e.getMessage()));
        }

    }

    public void socketSend(EFTRequest request) {
        try {
            socketThread.startWrite(socketThread.sockChannel, request, msgWritten);
        } catch (Exception e) {
            System.out.println(String.format("Could not write to the client due to the following reason: %s", e.getMessage()));
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
        else if (r instanceof EFTQueryCardResponse)
            listener.onQueryCardEvent((EFTQueryCardResponse) r);
        else if (r instanceof EFTGenericCommandResponse)
            listener.onGenericCommandEvent((EFTGenericCommandResponse) r);
        else if (r instanceof EFTClientListResponse)
            listener.onClientListEvent((EFTClientListResponse) r);
        else if (r instanceof EFTChequeAuthResponse)
            listener.onChequeAuthEvent((EFTChequeAuthResponse) r);
        else if (r instanceof EFTCloudPairResponse)
            listener.onCloudPairEvent((EFTCloudPairResponse) r);
        else {
            //return string so we can see it
            listener.onDefaultResponseEvent(s);
        }
    }
}
