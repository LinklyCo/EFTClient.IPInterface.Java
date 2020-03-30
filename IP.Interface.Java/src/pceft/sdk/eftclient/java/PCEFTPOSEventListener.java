package pceft.sdk.eftclient.java;

public interface PCEFTPOSEventListener {

    void onLogonEvent(EFTLogonResponse msg);

    void onReceiptEvent(EFTReceiptResponse msg);

    void onTransactionEvent(EFTTransactionResponse msg);

    void onDisplayEvent(EFTDisplayResponse msg);

    void onStatusEvent(EFTStatusResponse msg);

    void onSettlementEvent(EFTSettlementResponse msg);

    void onGetLastTransactionEvent(EFTGetLastTransactionResponse msg);

    void onReprintReceiptEvent(EFTReprintReceiptResponse msg);

    void onControlPanelEvent(EFTControlPanelResponse msg);

    void onSetDialogEvent(EFTSetDialogResponse msg);

    void onPinpadBusyEvent(EFTPinpadBusyResponse msg);

    void onQueryCardEvent(EFTQueryCardResponse msg);

    void onGenericCommandEvent(EFTGenericCommandResponse msg);

    void onClientListEvent(EFTClientListResponse msg);

    void onChequeAuthEvent(EFTChequeAuthResponse msg);

    void onCloudPairEvent(EFTCloudPairResponse msg);

    void onCloudTokenLogonEvent(EFTCloudTokenLogonResponse msg);

    // FEELING TEMPORARY, MIGHT DELETE LATER
    void onDefaultResponseEvent(String msg);

}