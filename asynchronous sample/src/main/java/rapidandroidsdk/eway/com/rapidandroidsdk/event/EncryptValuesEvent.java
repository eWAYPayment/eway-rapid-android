package rapidandroidsdk.eway.com.rapidandroidsdk.event;

import com.eway.payment.sdk.android.entities.EncryptItemsResponse;

/**
 * Created by alexanderparra on 31/10/15.
 */


public class EncryptValuesEvent {
    public EncryptItemsResponse encryptItemsResponse;

    public EncryptValuesEvent(EncryptItemsResponse encryptResp){
        this.encryptItemsResponse = encryptResp;
    }

}
