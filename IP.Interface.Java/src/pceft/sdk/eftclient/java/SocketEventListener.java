package pceft.sdk.eftclient.java;

import java.io.IOException;
import java.nio.channels.ClosedSelectorException;
import java.util.NoSuchElementException;

public abstract class SocketEventListener {
    public PCEFTPOSEventListener listener;

    public SocketEventListener(PCEFTPOSEventListener listener) {
        this.listener = listener;
    }

    public abstract void socketSend( EFTRequest eftRequest) throws IOException, ClosedSelectorException, NoSuchElementException;

    public void socketReceive( String s )
    {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            //return string so we can see it
            listener.onDefaultResponseEvent(s);
        }
    }
}