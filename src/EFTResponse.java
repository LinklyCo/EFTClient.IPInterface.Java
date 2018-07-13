import java.lang.reflect.Type;

/**
 * Abstract base class for EFT client responses
 */
public abstract class EFTResponse {
    protected Type pairedRequestType = null;

    /**
     * Hidden default constructor
     */
    private EFTResponse(){}


    public EFTResponse(Type pairedRequest){
        if(pairedRequest != null && !pairedRequest.getClass().isInstance(EFTRequest.class)){
            throw new IllegalStateException("pairedRequestType must be based on EFTRequest");
        }
        pairedRequestType = pairedRequest;
    }

    /**
     * Indicates the paired EFTRequest for this EFTResponse if one exists. Null otherwise.
     * e.g. EFTLogonResponse will have a paired EFTLogonRequest request
     * @return
     */
    public Type getPairedRequestType() {
        return pairedRequestType;
    }
}
