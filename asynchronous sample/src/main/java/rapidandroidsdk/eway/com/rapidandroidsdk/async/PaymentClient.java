package rapidandroidsdk.eway.com.rapidandroidsdk.async;

import com.ewaypayments.sdk.android.RapidAPI;
import com.ewaypayments.sdk.android.RapidConfigurationException;
import com.ewaypayments.sdk.android.beans.Transaction;
import com.ewaypayments.sdk.android.entities.SubmitPayResponse;
import com.squareup.otto.Produce;

import rapidandroidsdk.eway.com.rapidandroidsdk.bus.BusProvider;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.PaymentErrorHandlerEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.PaymentExceptionEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.SubmitPayEvent;

/**
 * Created by alexanderparra on 31/10/15.
 */
public class PaymentClient implements RapidAPI.RapidRecordingTransactionListener {

    public void PaymentProcess(Transaction transaction) throws RapidConfigurationException {
        RapidAPI.asycSubmitPayment(transaction,this);
    }

    @Override
    public void onResponseReceivedSuccess(SubmitPayResponse response) {
        BusProvider.getInstance().post(produceSubmitPayment(response));
    }

    @Override
    public void onResponseReceivedError(String errorResponse) {
        BusProvider.getInstance().post(produceErrorSubmitPaymentEvent(errorResponse));
    }

    @Override
    public void onResponseReceivedFailure(String errorFailure) {
        BusProvider.getInstance().post(produceErrorSubmitPaymentEvent(errorFailure));
    }

    @Override
    public void onResponseReceivedException(SubmitPayResponse exception) {
        BusProvider.getInstance().post(produceExceptionPaymentEvent(exception.getErrors()));
    }

    @Produce
    public SubmitPayEvent produceSubmitPayment(SubmitPayResponse response){
        return new SubmitPayEvent(response);
    }
    @Produce
    public PaymentErrorHandlerEvent produceErrorSubmitPaymentEvent(String response){
        return new PaymentErrorHandlerEvent(response);
    }
    @Produce
    public PaymentExceptionEvent produceExceptionPaymentEvent(String response){
        return new PaymentExceptionEvent(response);
    }

}
