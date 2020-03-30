package pceft.sdk.eftclient.java;

public class EFTClientListRequest extends EFTRequest{
    public EFTClientListRequest() {
        super(true, EFTClientListResponse.class);
    }
}
