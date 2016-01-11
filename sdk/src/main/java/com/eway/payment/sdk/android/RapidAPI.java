package com.eway.payment.sdk.android;

import android.util.Base64;

import com.eway.payment.sdk.android.beans.CodeDetail;
import com.eway.payment.sdk.android.beans.NVPair;
import com.eway.payment.sdk.android.beans.Transaction;
import com.eway.payment.sdk.android.entities.CodeLookupRequest;
import com.eway.payment.sdk.android.entities.CodeLookupResponse;
import com.eway.payment.sdk.android.entities.EncryptItemsResponse;
import com.eway.payment.sdk.android.entities.EncryptValuesRequest;
import com.eway.payment.sdk.android.entities.SubmitPayResponse;
import com.eway.payment.sdk.android.entities.SubmitPaymentRequest;
import com.eway.payment.sdk.android.entities.UserMessageResponse;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;

import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RapidAPI {
    private static final String PRODUCTION = "Production";
    private static final String PRODUCTION_URL = "https://api.ewaypayments.com";
    private static final String SANDBOX = "Sandbox";
    private static final String SANDBOX_URL = "https://api.sandbox.ewaypayments.com";
    private static final String PROCESSPAYMENT = "ProcessPayment";
    private static final String PAYMENT = "payment";
    private static final String ECRYPT = "eCrypt";
    private static final String ENCRYPT = "encrypt";
    private static final String CODELOOKUP = "CodeLookup";
    private static final String VERSIONREPORTED = "1.2";
    public static String RapidEndpoint;
    public static String PublicAPIKey;


    private RapidAPI() {}

    // Synchronous payment
    public static SubmitPayResponse submitPayment(Transaction transaction) throws IOException, RapidConfigurationException {
        try {
            errorCheck();
            if (transaction == null) {
                return new SubmitPayResponse("S9995", "", "");
            }
            SubmitPaymentRequest request = buildSubmitRequest(transaction);
            Call<SubmitPayResponse> call = callPost().submitPayment(request);
            Response<SubmitPayResponse> submitPayResponse = call.execute();
            if(submitPayResponse.errorBody() != null)
                errorList(submitPayResponse.code());

            return submitPayResponse.body();
        } catch (RapidConfigurationException e) {
            SubmitPayResponse response = new SubmitPayResponse(e.getErrorCodes(), "", "");
            throw new RapidConfigurationException(response.getErrors());
        }
    }
    // Asynchronous payment using callback
    public static void asycSubmitPayment(Transaction transaction, final RapidRecordingTransactionListener callback){
        try {
            errorCheck();
            if (transaction == null) {
                throw new RapidConfigurationException("S9995");
            }
            SubmitPaymentRequest request = buildSubmitRequest(transaction);
            callPost().submitPayment(request).enqueue(new Callback<SubmitPayResponse>() {
                @Override
                public void onResponse(Response<SubmitPayResponse> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        if (response.body().getErrors() == null) {
                            callback.onResponseReceivedSuccess(response.body());
                        } else {
                            callback.onResponseReceivedError(response.body().getErrors());
                        }
                    } else {
                        try {
                            errorList(response.code());
                        } catch (RapidConfigurationException e) {
                            try {
                                callback.onResponseReceivedException(new SubmitPayResponse(e.getErrorCodes(), null, null));
                            } catch (RapidConfigurationException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onResponseReceivedFailure(t.getMessage());
                }
            });
        }catch (RapidConfigurationException ex){
            try {
                callback.onResponseReceivedException(new SubmitPayResponse(ex.getErrorCodes(), null, null));
            } catch (RapidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    //rxJava SubmitPayment
    public static Observable<SubmitPayResponse> rxSubmitPayment(Transaction transaction)  {
        try {
            errorCheck();
            if (transaction == null) {
                throw new RapidConfigurationException("S9995");
            }
            SubmitPaymentRequest request = buildSubmitRequest(transaction);
            return rxCallPost().rxObsSubmitPayment(request)
                    .observeOn(Schedulers.io())
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends SubmitPayResponse>>() {
                        @Override
                        public Observable<? extends SubmitPayResponse> call(final Throwable throwable) {
                            try {
                                errorCheck();
                                if (throwable.getMessage().contains("401"))
                                    throw new RapidConfigurationException("S9993");
                                if (throwable.getMessage().contains("443"))
                                    throw new RapidConfigurationException("S9991");

                                throw new RapidConfigurationException("S9992");
                            } catch (final RapidConfigurationException e) {
                                return Observable.create(new Observable.OnSubscribe<SubmitPayResponse>() {
                                    @Override
                                    public void call(Subscriber<? super SubmitPayResponse> subscriber) {
                                        try {
                                            subscriber.onError(new Throwable(e.getErrorCodes()));
                                        } catch (RapidConfigurationException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .flatMap(new Func1<SubmitPayResponse, Observable<SubmitPayResponse>>() {
                        @Override
                        public Observable<SubmitPayResponse> call(final SubmitPayResponse submitPayResponse) {
                            return Observable.create(new Observable.OnSubscribe<SubmitPayResponse>() {
                                @Override
                                public void call(Subscriber<? super SubmitPayResponse> subscriber) {
                                    subscriber.onCompleted();
                                    subscriber.onNext(submitPayResponse);
                                }
                            });
                        }
                    });
        } catch (final RapidConfigurationException e) {
            return Observable.create(new Observable.OnSubscribe<SubmitPayResponse>() {
                @Override
                public void call(Subscriber<? super SubmitPayResponse> subscriber) {
                    try {
                        subscriber.onError(new Throwable(e.getErrorCodes()));
                    } catch (RapidConfigurationException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

    }

    // Synchronous Encryption
    public static EncryptItemsResponse encryptValues(ArrayList<NVPair> Values) throws IOException, RapidConfigurationException {
        try {
            errorCheck();
            EncryptValuesRequest request = buildEncryptValues(Values);
            Call<EncryptItemsResponse> call =callPost().encryptValues(request);
            Response<EncryptItemsResponse> encryptValuesRequestResponse = call.execute();

            if(encryptValuesRequestResponse.errorBody()!= null) {
              errorList(encryptValuesRequestResponse.raw().code());
            }
            return encryptValuesRequestResponse.body();
        } catch (RapidConfigurationException e) {
            return new EncryptItemsResponse(e.getErrorCodes(), null,String.valueOf(0));
        }
    }
    // Asynchronous Encryption
    public static void asycEncryptValues(ArrayList<NVPair> Values, final RapidEncryptValuesListener callback){
        try {
            errorCheck();
            final EncryptValuesRequest request = buildEncryptValues(Values);
            callPost().encryptValues(request).enqueue(new Callback<EncryptItemsResponse>() {
                @Override
                public void onResponse(Response<EncryptItemsResponse> response, Retrofit retrofit) {

                    if (response.isSuccess()) {
                        if (response.body().getErrors() == null) {
                            callback.onResponseReceivedSuccess(response.body());
                        } else {
                            callback.onResponseReceivedError(response.body().getErrors());
                        }
                    } else {
                        try {
                            errorList(response.code());
                        } catch (RapidConfigurationException e) {
                            try {
                                callback.onResponseReceivedException(new EncryptItemsResponse(e.getErrorCodes(), null, String.valueOf(0)));
                            } catch (RapidConfigurationException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onResponseReceivedFailure(t.getMessage());
                }
            });
        }catch (RapidConfigurationException ex){
            try {
                callback.onResponseReceivedException(new EncryptItemsResponse(ex.getErrorCodes(), null, String.valueOf(0)));
            } catch (RapidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }
    // rxJava Encryption
    public static Observable<EncryptItemsResponse> rxEncryptValues(ArrayList<NVPair> Values) {
        try {
            errorCheck();
            EncryptValuesRequest request = buildEncryptValues(Values);
            return rxCallPost().rxObsEncryptValues(request)
                    .observeOn(Schedulers.io())
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends EncryptItemsResponse>>() {
                        @Override
                        public Observable<? extends EncryptItemsResponse> call(final Throwable throwable) {
                            try {
                                if (throwable.getMessage().contains("401"))
                                    throw new RapidConfigurationException("S9993");
                                if (throwable.getMessage().contains("443"))
                                    throw new RapidConfigurationException("S9991");

                                throw new RapidConfigurationException("S9992");
                            } catch (final RapidConfigurationException e) {
                                return Observable.create(new Observable.OnSubscribe<EncryptItemsResponse>() {
                                    @Override
                                    public void call(Subscriber<? super EncryptItemsResponse> subscriber) {
                                        try {
                                            subscriber.onError(new Throwable(e.getErrorCodes()));
                                        } catch (RapidConfigurationException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .flatMap(new Func1<EncryptItemsResponse, Observable<EncryptItemsResponse>>() {
                        @Override
                        public Observable<EncryptItemsResponse> call(final EncryptItemsResponse encryptItemsResponse) {
                            return Observable.create(new Observable.OnSubscribe<EncryptItemsResponse>() {
                                @Override
                                public void call(Subscriber<? super EncryptItemsResponse> subscriber) {
                                    subscriber.onCompleted();
                                    subscriber.onNext(encryptItemsResponse);

                                }
                            });
                        }
                    });
        } catch (final RapidConfigurationException e) {
            return Observable.create(new Observable.OnSubscribe<EncryptItemsResponse>() {
                @Override
                public void call(Subscriber<? super EncryptItemsResponse> subscriber) {
                    try {
                        subscriber.onError(new Throwable(e.getErrorCodes()));
                    } catch (RapidConfigurationException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

    }

    //Synchronous UserMessage
    public static UserMessageResponse userMessage(String Language, String ErrorCodes) throws IOException, RapidConfigurationException {
        try {
            Call<CodeLookupResponse> call =callPost().codeLookUp(new CodeLookupRequest(nullSafeGetLocale(Language), parseErrorCodeList(ErrorCodes)));
            Response <CodeLookupResponse> codeLookupResponse = call.execute();
            ArrayList<String> errorMessages = new ArrayList<>();
            for (CodeDetail codeDetail : codeLookupResponse.body().getCodeDetails()) {
                errorMessages.add(codeDetail.getDisplayMessage());
            }
            return new UserMessageResponse(errorMessages, null);
        } catch (RapidConfigurationException e) {
            return new UserMessageResponse(null, e.getErrorCodes());
        }
    }
   //asynchronous userMessage
    public static void asycUserMessage(String Language, String ErrorCodes,final RapidUserMessageListener callback) throws RapidConfigurationException {
        callPost().codeLookUp(new CodeLookupRequest(nullSafeGetLocale(Language),parseErrorCodeList(ErrorCodes)))
                .enqueue(new Callback<CodeLookupResponse>() {
                    @Override
                    public void onResponse(Response<CodeLookupResponse> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            if(response.body().getErrors()==null) {
                                ArrayList<String> errorMessages = new ArrayList<>();
                                for (CodeDetail codeDetail : response.body().getCodeDetails()) {
                                    errorMessages.add(codeDetail.getDisplayMessage());
                                }
                                callback.onResponseReceivedSuccess(new UserMessageResponse(errorMessages,null));
                            }else {
                                callback.onResponseReceivedError(response.body().getErrors());
                            }

                        } else {
                            try {
                                errorList(response.code());
                            } catch (RapidConfigurationException e) {
                                try {
                                    callback.onResponseReceivedException(new UserMessageResponse(null,e.getErrorCodes()));
                                } catch (RapidConfigurationException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        callback.onResponseReceivedFailure(t.getMessage());
                    }
                });

    }
    // rxJava UserMessage
    public static Observable<UserMessageResponse> rxUserMessage(String Language, String ErrorCodes) {
        try {
            errorCheck();
            return rxCallPost().rxCodeLookUp(new CodeLookupRequest(nullSafeGetLocale(Language),parseErrorCodeList(ErrorCodes)))
                    .observeOn(Schedulers.io())
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends CodeLookupResponse>>() {
                        @Override
                        public Observable<? extends CodeLookupResponse> call(Throwable throwable) {
                            try {
                                if (throwable.getMessage().contains("401"))
                                    throw new RapidConfigurationException("S9993");
                                if (throwable.getMessage().contains("443"))
                                    throw new RapidConfigurationException("S9991");

                                throw new RapidConfigurationException("S9992");
                            } catch (final RapidConfigurationException e) {
                                return Observable.create(new Observable.OnSubscribe<CodeLookupResponse>() {
                                    @Override
                                    public void call(Subscriber<? super CodeLookupResponse> subscriber) {
                                        try {
                                            subscriber.onError(new Throwable(e.getErrorCodes()));
                                        } catch (RapidConfigurationException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .flatMap(new Func1<CodeLookupResponse, Observable<UserMessageResponse>>() {
                        @Override
                        public Observable<UserMessageResponse> call(final CodeLookupResponse codeLookupResponse) {
                            return Observable.create(new Observable.OnSubscribe<UserMessageResponse>() {
                                @Override
                                public void call(Subscriber<? super UserMessageResponse> subscriber) {
                                    ArrayList<String> errorMessages = new ArrayList<>();
                                    for (CodeDetail codeDetail : codeLookupResponse.getCodeDetails()) {
                                        errorMessages.add(codeDetail.getDisplayMessage());
                                    }
                                    subscriber.onNext(new UserMessageResponse(errorMessages, null));
                                    subscriber.onCompleted();

                                }
                            });
                        }
                    });

        } catch (final RapidConfigurationException e) {
            return Observable.create(new Observable.OnSubscribe<UserMessageResponse>() {
                @Override
                public void call(Subscriber<? super UserMessageResponse> subscriber) {
                    subscriber.onError(new Throwable(e.getMessage()));
                }
            });
        }

    }

    private static ArrayList<String> parseErrorCodeList(String ErrorCodes) {
        return (ErrorCodes != null) ? Utils.newArrayList(ErrorCodes.trim().replaceAll("\\s+", "").split(",")) : Utils.<String>newArrayList();
    }

    private static String nullSafeGetLocale(String Language) {
        return (Language != null) ? Language : Locale.getDefault().toString();
    }

//=================================================================================
    /**
    * Retrofit Adapters
    *
    ***/

    // Adapter Asynchronous and Synchronous call post for  Encryption
    protected static rapidAndroidApi callPost() throws RapidConfigurationException{
        //Restful call
        String baseUrl = changeUrl(RapidEndpoint);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getEwayClient())
                .build();

        rapidAndroidApi adapter = retrofit.create(rapidAndroidApi.class);

        return adapter;
    }

    //Adapter for Asynchronous call rxjava
    protected static rapidAndroidApi rxCallPost() throws RapidConfigurationException{
        //Restful call
        String baseUrl = changeUrl(RapidEndpoint);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getEwayClient())
                .build();

        rapidAndroidApi adapter = retrofit.create(rapidAndroidApi.class);

        return adapter;
    }

//======================================================================================

    /**
     * Submit Request
     * @param transaction
     * @return
     */

    public static SubmitPaymentRequest buildSubmitRequest(Transaction transaction){

        SubmitPaymentRequest request = new SubmitPaymentRequest();
        request.setMethod(PROCESSPAYMENT);
        request.setCustomer(Utils.transformCustomer(transaction.getCustomer()));
        request.setShippingAddress(Utils.transformShippingAddress(transaction.getShippingDetails()));
        request.setShippingMethod(Utils.nullSafeGetShippingMethod(transaction));
        request.setItems(transaction.getLineItems());
        request.setOptions(Utils.transformOptions(transaction.getOptions()));
        request.setPayment(transaction.getPayment());
        request.setDeviceID(Utils.getUniquePsuedoID());
        request.setPartnerID(transaction.getPartnerID());
        request.setTransactionType(Utils.nullSafeGetTransactionType(transaction));

        return request;

    }

    /**
     * Encryption request
     * @param Values
     * @return
     */

    public static EncryptValuesRequest buildEncryptValues(ArrayList<NVPair> Values){

        EncryptValuesRequest request = new EncryptValuesRequest();
        request.setMethod(ECRYPT);
        request.setItems(Values);
        return request;
    }

//====================================================================================
    /**
     * rapidAndroidApi (Retrofit)
     */
    public interface rapidAndroidApi{

        // Asynchronous or synchronous api
        @Headers("Content-Type: application/json")
        @POST("/payment")
        Call<SubmitPayResponse> submitPayment(@Body SubmitPaymentRequest request);

        @Headers("Content-Type: application/json")
        @POST("/payment")
        Observable<SubmitPayResponse> rxObsSubmitPayment(@Body SubmitPaymentRequest request);

        @Headers("Content-Type: application/json")
        @POST("/encrypt")
        Call<EncryptItemsResponse> encryptValues(@Body EncryptValuesRequest request);

        @Headers("Content-Type: application/json")
        @POST("/encrypt")
        Observable<EncryptItemsResponse> rxObsEncryptValues(@Body EncryptValuesRequest request);

        @Headers("Content-Type: application/json")
        @POST("/codelookup")
        Call<CodeLookupResponse> codeLookUp(@Body CodeLookupRequest request);

        @Headers("Content-Type: application/json")
        @POST("/codelookup")
        Observable<CodeLookupResponse> rxCodeLookUp(@Body CodeLookupRequest request);

    }
//=============================================================================================
    /**
     * OkhttpClient
     * @return
     * @throws RapidConfigurationException
     */

    private static OkHttpClient getEwayClient() throws RapidConfigurationException {
        OkHttpClient client = new OkHttpClient();

        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                String credential = Credentials.basic(PublicAPIKey, "");
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .header("Authorization", credential)
                        .header("User-Agent",";eWAY SDK Android " + VERSIONREPORTED)
                        .method(originalRequest.method(),originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        client.setConnectTimeout(3, TimeUnit.MINUTES);
        client.setReadTimeout(3, TimeUnit.MINUTES);
        return client;

    }
//===================================================================================

    public interface RapidRecordingTransactionListener{

        void onResponseReceivedSuccess(SubmitPayResponse response);

        void onResponseReceivedError(String errorResponse);

        void onResponseReceivedFailure(String errorFailure);

        void onResponseReceivedException(SubmitPayResponse exception);
    }

    public interface RapidEncryptValuesListener{

        void onResponseReceivedSuccess(EncryptItemsResponse response);

        void onResponseReceivedError(String errorResponse);

        void onResponseReceivedFailure(String errorFailure);

        void onResponseReceivedException(EncryptItemsResponse exception);

    }

    public interface RapidUserMessageListener{

        void onResponseReceivedSuccess(UserMessageResponse response);

        void onResponseReceivedError(String errorResponse);

        void onResponseReceivedFailure(String errorFailure);

        void onResponseReceivedException(UserMessageResponse exception);

    }


    private static void errorList(int responseCode)throws RapidConfigurationException{
        if(responseCode == 401)
            throw new RapidConfigurationException("S9993");
        if(responseCode == 443)
            throw new RapidConfigurationException("S9991");

        throw new RapidConfigurationException("S9992");

    }


    private static void errorCheck() throws RapidConfigurationException{
        if (PublicAPIKey == null || PublicAPIKey.isEmpty()) {
            throw new RapidConfigurationException("S9991");
        }
        if (RapidEndpoint == null || RapidEndpoint.isEmpty()) {
            throw new RapidConfigurationException("S9990");
        }

        String urlPattern="^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
         if(!RapidEndpoint.matches(urlPattern))
             throw new RapidConfigurationException("S9992");
    }

    public static String changeUrl(String url) throws RapidConfigurationException {
        String baseUrl = url;
        errorCheck();
        if (PRODUCTION.equalsIgnoreCase(baseUrl)) {
            baseUrl = PRODUCTION_URL;
        } else if (SANDBOX.equalsIgnoreCase(baseUrl)) {
            baseUrl = SANDBOX_URL;
        }
        return baseUrl;
    }




}
