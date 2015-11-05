package rapidandroidsdk.eway.com.rapidandroidsdk.event;

/**
 * Created by alexanderparra on 31/10/15.
 */
public class PaymentErrorHandlerEvent {
    public String paymentError;
    public PaymentErrorHandlerEvent(String error){
        this.paymentError = error;
    }

}
