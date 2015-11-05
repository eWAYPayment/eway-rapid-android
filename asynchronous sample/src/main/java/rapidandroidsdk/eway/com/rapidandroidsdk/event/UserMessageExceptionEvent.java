package rapidandroidsdk.eway.com.rapidandroidsdk.event;

/**
 * Created by alexanderparra on 1/11/15.
 */
public class UserMessageExceptionEvent {
    public String userMessageException;
    public UserMessageExceptionEvent(String error){
        this.userMessageException = error;
    }
}
