package rapidandroidsdk.eway.com.rapidandroidsdk.event;

/**
 * Created by alexanderparra on 1/11/15.
 */
public class EncryptExceptionEvent {
    public String encryptException;
    public EncryptExceptionEvent(String error){
        this.encryptException = error;
    }
}
