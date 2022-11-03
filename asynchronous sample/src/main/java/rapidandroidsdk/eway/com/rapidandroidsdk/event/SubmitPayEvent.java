package rapidandroidsdk.eway.com.rapidandroidsdk.event;

import com.ewaypayments.sdk.android.entities.SubmitPayResponse;

/**
 * Created by alexanderparra on 31/10/15.
 */
public class SubmitPayEvent {
    public final SubmitPayResponse submitPayResponse;

    public SubmitPayEvent(SubmitPayResponse submitResponse){
        this.submitPayResponse = submitResponse;
    }
}
