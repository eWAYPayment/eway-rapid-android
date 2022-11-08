package rapidandroidsdk.eway.com.rapidandroidsdk.event;

import com.ewaypayments.sdk.android.entities.UserMessageResponse;

import java.util.ArrayList;

/**
 * Created by alexanderparra on 31/10/15.
 */
public class UserMessageEvent {
    public UserMessageResponse errorMessage;
    public UserMessageEvent(UserMessageResponse error){
        this.errorMessage = error;
    }
}
