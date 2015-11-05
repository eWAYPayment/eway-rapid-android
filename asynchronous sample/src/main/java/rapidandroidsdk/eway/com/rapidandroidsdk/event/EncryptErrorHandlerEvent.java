package rapidandroidsdk.eway.com.rapidandroidsdk.event;



/**
 * Created by alexanderparra on 31/10/15.
 */
public class EncryptErrorHandlerEvent {
    public String errorEncrypt;
    public EncryptErrorHandlerEvent(String error){
        this.errorEncrypt = error;
    }
}
