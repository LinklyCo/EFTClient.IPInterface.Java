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
}