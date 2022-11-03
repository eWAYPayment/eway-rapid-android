package rapidandroidsdk.eway.com.rapidandroidsdk.event;

import com.ewaypayments.sdk.android.beans.Payment;

/**
 * Created by alexanderparra on 1/11/15.
 */
public class PaymentExceptionEvent {
    public String paymentException;
    public PaymentExceptionEvent(String error){
        this.paymentException = error;
    }
}
