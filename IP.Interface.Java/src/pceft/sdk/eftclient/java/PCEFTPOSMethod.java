package pceft.sdk.eftclient.java;

public class PCEFTPOSMethod {
    public PCEFTPOSMethod(){

    }

    public PCEFTPOSMethod(MethodType type){
        switch (type){
            case GetEntryPoints:
                break;
            case DisplayOnPinpad:
                break;
            case EndSession:
                break;
            default:
                break;

        }
    }


}
