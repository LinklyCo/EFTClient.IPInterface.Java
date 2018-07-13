public class PCEFTPOSControl implements SocketEventListener{

    SocketThread sckThread;
    PCEFTPOSEventListener listener;

    public PCEFTPOSControl(PCEFTPOSEventListener listener, String hostname, int port)
    {
        // Set the listener
        this.listener = listener;

        // Create a new socket thread
        sckThread = new SocketThread(hostname, port, this);
        sckThread.start();
    }

    String close() {
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
    }

}