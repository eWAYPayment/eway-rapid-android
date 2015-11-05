package rapidandroidsdk.eway.com.rapidandroidsdk.event;

/**
 * Created by alexanderparra on 31/10/15.
 */
public class UserMessageErrorHandlerEvent {
    public String UserMessageError;
    public UserMessageErrorHandlerEvent(String error){
        this.UserMessageError = error;
    }
}
