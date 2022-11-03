package rapidandroidsdk.eway.com.rapidandroidsdk.async;

import com.ewaypayments.sdk.android.RapidAPI;
import com.ewaypayments.sdk.android.RapidConfigurationException;
import com.ewaypayments.sdk.android.entities.SubmitPayResponse;
import com.ewaypayments.sdk.android.entities.UserMessageResponse;
import com.squareup.otto.Produce;

import java.util.ArrayList;

import rapidandroidsdk.eway.com.rapidandroidsdk.bus.BusProvider;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.SubmitPayEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.UserMessageErrorHandlerEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.UserMessageEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.UserMessageExceptionEvent;

/**
 * Created by alexanderparra on 31/10/15.
 */
public class UserMessageClient implements RapidAPI.RapidUserMessageListener {

    public void userMessageClient(String language, String errorCodes) throws RapidConfigurationException {
        RapidAPI.asycUserMessage(language,errorCodes,this);
    }
    @Override
    public void onResponseReceivedSuccess(UserMessageResponse response) {
        BusProvider.getInstance().post(produceMessageUserEvent(response));
    }

    @Override
    public void onResponseReceivedError(String errorResponse) {
        BusProvider.getInstance().post(produceMessageUserErrorEvent(errorResponse));

    }

    @Override
    public void onResponseReceivedFailure(String errorFailure) {

    }

    @Override
    public void onResponseReceivedException(UserMessageResponse exception) {
         BusProvider.getInstance().post(produceMessageUserExceptionEvent(exception.getErrors()));
    }

    @Produce
    public UserMessageEvent produceMessageUserEvent(UserMessageResponse response){
        return new UserMessageEvent(response);
    }

    @Produce
    public UserMessageErrorHandlerEvent produceMessageUserErrorEvent(String errorResponse){
        return new UserMessageErrorHandlerEvent(errorResponse);
    }

    @Produce
    public UserMessageExceptionEvent produceMessageUserExceptionEvent(String errorResponse){
        return new UserMessageExceptionEvent(errorResponse);
    }


}
