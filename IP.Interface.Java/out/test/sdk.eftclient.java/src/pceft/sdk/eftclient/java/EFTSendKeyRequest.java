package pceft.sdk.eftclient.java;

public class EFTSendKeyRequest extends EFTRequest {

    public enum EFTPOSKey{
        OKCancel('0'),
        YesAccept('1'),
        NoDecline('2'),
        Authorise('3');

        public char Key;
        EFTPOSKey(char c){
            Key = c;
        }
    }

    public EFTSendKeyRequest(){
        super(false, null);
        isStartOfTransaction = false;
    }

    public EFTPOSKey Key = EFTPOSKey.OKCancel;

    public EFTPOSKey getKey() {
        return Key;
    }

    public void setKey(EFTPOSKey key) {
        Key = key;
    }
    public EFTPOSKey setKey (char c){
        EFTPOSKey result = null;
        switch (c){
            case('0'):
            default:
                result = EFTPOSKey.OKCancel;
                break;
            case('1'):
                result = EFTPOSKey.YesAccept;
                break;
            case('2'):
                result = EFTPOSKey.NoDecline;
                break;
            case('3'):
                result = EFTPOSKey.Authorise;
                break;
        }
        return result;
    }

    /**
     * Data entered by the POS (e.g. for an 'input entry' dialog type)
     */
    public String Data = "";

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
