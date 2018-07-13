public class AsyncSocketControl implements SocketEventListener {

    PCEFTPOSEventListener listener;
    AsyncSocket socket;

    AsyncSocketControl(PCEFTPOSEventListener _listener, String hostname, int port) {
        listener = _listener;
        socket = new AsyncSocket(hostname, port, this);
        Thread thread = new Thread(socket);
        thread.start();
    }

    void socketSend(EFTRequest request) {
        try {
            socket.write(socket.selector.keys().iterator().next(), request);
        } catch (Exception e) {
            System.out.println("Could not write to the client.");
            e.printStackTrace();
        }
    }

    String close() {
        try {
            socket.close();
            return "Successfully closed the socket.";
        } catch (Exception e) {
            return e.getMessage();
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
        else if (r instanceof EFTControlPanelResponse)
            listener.onControlPanelEvent((EFTControlPanelResponse) r);
        else if (r instanceof EFTSetDialogResponse)
            listener.onSetDialogEvent((EFTSetDialogResponse) r);
        else if (r instanceof EFTPinpadBusyResponse)
            listener.onPinpadBusyEvent((EFTPinpadBusyResponse) r);
    }
}
