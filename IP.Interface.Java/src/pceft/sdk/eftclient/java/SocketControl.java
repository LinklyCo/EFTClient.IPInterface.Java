package pceft.sdk.eftclient.java;

public interface SocketControl {
    public void socketSend(EFTRequest request) throws Exception;

    String close();
}
