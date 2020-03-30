package pceft.sdk.eftclient.java;

import java.io.IOException;

public class AsyncSSLSocketControl implements SocketEventListener {

    public PCEFTPOSEventListener listener;
    public AsyncSSLSocket socket;

    public AsyncSSLSocketControl(PCEFTPOSEventListener _listener, String hostname, int port) {

        listener = _listener;
        socket = new AsyncSSLSocket(hostname, port, this);
        Thread thread = new Thread(socket);
        try {
            socket.initSocket();
            thread.start();
        } catch (IOException e){
            System.out.println("Socket Failed To Initialize");
        }
        catch (Exception e) {
            System.out.println("MSG: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
            System.out.println("RAW: " + e.toString());
            //throw e;
        }
    }

    public void setSslListener(SSLSocketListener SSL) {
        socket.sslListener = SSL;
    }

    public void socketSend(EFTRequest request) throws Exception {
        try {
            //socket.selector.keys().iterator().next(),
            socket.write(socket.selector.keys().iterator().next(), request);
        } catch (Exception e) {
            System.out.println("MSG: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
            System.out.println("RAW: " + e.toString());
            //throw e;
        }
    }

    public void socketSendRaw(String request) throws Exception {
        try {
            //socket.selector.keys().iterator().next(),
            socket.writeRaw(socket.selector.keys().iterator().next(), request);
        } catch (Exception e) {
            System.out.println("MSG: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
            System.out.println("RAW: " + e.toString());
            //throw e;
        }
    }

    public String close() {
        socket.close();
        return "Socket closed";
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
        else if (r instanceof EFTCloudTokenLogonResponse)
            listener.onCloudTokenLogonEvent((EFTCloudTokenLogonResponse) r);
        else {
            //return string so we can see it
            listener.onDefaultResponseEvent(s);
        }
    }
}
