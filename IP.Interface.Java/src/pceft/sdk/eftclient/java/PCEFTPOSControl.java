package pceft.sdk.eftclient.java;

public class PCEFTPOSControl implements SocketEventListener{

    public SocketThread sckThread;
    public PCEFTPOSEventListener listener;

    public PCEFTPOSControl(PCEFTPOSEventListener listener, String hostname, int port)
    {
        // Set the listener
        this.listener = listener;

        try {
            // Create a new socket thread
            sckThread = new SocketThread(hostname, port, this);
            sckThread.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public String close() {
        return sckThread.close();
    }

    public void socketSend( EFTRequest eftRequest)
    {
        sckThread.socketSend(eftRequest);
    }

    public void socketReceive( String s )
    {
        // Create a new message from the socket string
        MessageParser msg = new MessageParser();
        EFTResponse r = msg.parseMsgStr(s);
        // cmd code should probably be mapped to a number
        if( r instanceof EFTLogonResponse )
            listener.onLogonEvent((EFTLogonResponse) r);
        else if ( r instanceof EFTReceiptResponse )
            listener.onReceiptEvent((EFTReceiptResponse) r);
        else if ( r instanceof EFTTransactionResponse )
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