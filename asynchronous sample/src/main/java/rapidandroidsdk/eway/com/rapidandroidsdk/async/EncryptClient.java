package rapidandroidsdk.eway.com.rapidandroidsdk.async;

import com.ewaypayments.sdk.android.RapidAPI;
import com.ewaypayments.sdk.android.RapidConfigurationException;
import com.ewaypayments.sdk.android.beans.NVPair;
import com.ewaypayments.sdk.android.entities.EncryptItemsResponse;
import com.squareup.otto.Produce;

import java.util.ArrayList;

import rapidandroidsdk.eway.com.rapidandroidsdk.bus.BusProvider;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.EncryptErrorHandlerEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.EncryptExceptionEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.EncryptValuesEvent;

/**
 * Created by alexanderparra on 31/10/15.
 */
public class EncryptClient implements RapidAPI.RapidEncryptValuesListener{

    public void EncrypValuesClient(String cardNumber,String cvnNumber) throws RapidConfigurationException {

        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", cardNumber));
        values.add(new NVPair("CVN", cvnNumber));
        RapidAPI.asycEncryptValues(values,this);

    }

    @Override
    public void onResponseReceivedSuccess(EncryptItemsResponse response) {
        BusProvider.getInstance().post(produceEncryptEvent(response));
    }

    @Override
    public void onResponseReceivedError(String errorResponse) {
        BusProvider.getInstance().post(produceErrorEncryptEvent(errorResponse));
    }

    @Override
    public void onResponseReceivedFailure(String errorFailure) {
        BusProvider.getInstance().post(produceErrorEncryptEvent(errorFailure));
    }

    @Override
    public void onResponseReceivedException(EncryptItemsResponse exception) {
        BusProvider.getInstance().post(produceEncryptExceptionEvent(exception.getErrors()));
    }

    @Produce
    public EncryptValuesEvent produceEncryptEvent(EncryptItemsResponse response){
        return new EncryptValuesEvent(response);
    }
    @Produce
    public EncryptErrorHandlerEvent produceErrorEncryptEvent(String response){
        return  new EncryptErrorHandlerEvent(response);
    }
    @Produce
    public EncryptExceptionEvent produceEncryptExceptionEvent(String response){
        return new EncryptExceptionEvent(response);
    }
}
