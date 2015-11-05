package rapidandroidsdk.sdk.payment.eway.com.rxjavasample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.sdk.payment.eway.com.rxjavasample.R;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.eway.payment.sdk.android.RapidAPI;
import com.eway.payment.sdk.android.RapidConfigurationException;
import com.eway.payment.sdk.android.beans.CardDetails;
import com.eway.payment.sdk.android.beans.Customer;
import com.eway.payment.sdk.android.beans.NVPair;
import com.eway.payment.sdk.android.beans.Payment;
import com.eway.payment.sdk.android.beans.Transaction;
import com.eway.payment.sdk.android.beans.TransactionType;
import com.eway.payment.sdk.android.entities.EncryptItemsResponse;
import com.eway.payment.sdk.android.entities.SubmitPayResponse;
import com.eway.payment.sdk.android.entities.UserMessageResponse;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class RxSampleMainActivity extends AppCompatActivity {
    @Bind(R.id.expMonth)
    Spinner monthSpinner;
    @Bind(R.id.expYear)
    Spinner yearSpinner;
    @Bind(R.id.totalAmount)
    EditText totalAmountEditText;
    @Bind(R.id.cardName)
    EditText cardNameEditText;
    @Bind(R.id.cardNumber)
    EditText cardNumberEditText;
    @Bind(R.id.cvn)
    EditText cvnEditText;
    @Bind(R.id.transTypes)
    Spinner transTypesSpinner;

    private ProgressDialog progressDialog;
    private String totalamount;
    private String cardName;
    private String cardNumber;
    private String cvnNumber;
    private String expMonth;
    private String expYear;
    private String transTypes;
    private CardDetails cardDetails = new CardDetails();
    private Transaction transaction = new Transaction();
    private Payment payment = new Payment();
    private Customer customer = new Customer();
    private Subscription subscription = Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_sample_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        transTypesPopulator();
        dateTimePopulator();
        RapidAPI.PublicAPIKey = "epk-6C961B95-D93A-443C-BCB9-64B6DBDC1C1B";
        RapidAPI.RapidEndpoint = "https://api.sandbox.ewaypayments.com/";

    }

    @OnClick(R.id.submit)
    public void sumitButton() {
        try {
            progressDialog = ProgressDialog.show(RxSampleMainActivity.this, "Processing", "Processing payment", true);
            fetchDataFromForm();
            totalamount = "200";
            cardName = "Jhon Smith";
            cardNumber = "4444333322221111";
            cvnNumber = "123";
            expMonth = "12";
            expYear = "2025";
            payment.setTotalAmount(Integer.parseInt(totalamount));
            cardDetails.setName(cardName);
            fetchEncryptData(cardNumber, cvnNumber);
        } catch (RapidConfigurationException e) {
           e.printStackTrace();
        }

    }

    private void fetchEncryptData(String cardNumber, String cvnNumber) throws RapidConfigurationException {

        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", cardNumber));
        values.add(new NVPair("CVN", cvnNumber));


      RapidAPI.rxEncryptValues(values)
              .flatMap(new Func1<EncryptItemsResponse, Observable<SubmitPayResponse>>() {
                  @Override
                  public Observable<SubmitPayResponse> call(final EncryptItemsResponse response) {
                      if (response.getErrors() == null) {
                          cardDetails.setNumber(response.getItems().get(0).getValue());
                          cardDetails.setCVN(response.getItems().get(1).getValue());
                          cardDetails.setExpiryMonth(expMonth);
                          cardDetails.setExpiryYear(expYear);
                          customer.setCardDetails(cardDetails);
                          transaction.setTransactionType(TransactionType.valueOf((transTypes.isEmpty() ? TransactionType.Purchase.toString() : transTypes.toString())));
                          transaction.setPayment(payment);
                          transaction.setCustomer(customer);
                          return RapidAPI.rxSubmitPayment(transaction);
                      } else {
                          final SubmitPayResponse submitResp = new SubmitPayResponse();
                          return Observable.create(new Observable.OnSubscribe<SubmitPayResponse>() {
                              @Override
                              public void call(Subscriber<? super SubmitPayResponse> subscriber) {
                                  RapidAPI.rxUserMessage(Locale.getDefault().getLanguage(), response.getErrors())
                                          .subscribe(new Action1<UserMessageResponse>() {
                                              @Override
                                              public void call(UserMessageResponse userMessageResponse) {
                                                  submitResp.setErrors(errorHandler(userMessageResponse.getErrorMessages()));
                                              }
                                          });
                                  subscriber.onNext(submitResp);
                              }
                          });
                      }

                  }

              }).observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Observer<SubmitPayResponse>() {
                  @Override
                  public void onCompleted() {

                  }

                  @Override
                  public void onError(Throwable e) {
                       alertDialogResponse(e.getMessage());
                  }

                  @Override
                  public void onNext(SubmitPayResponse response) {
                      if (response.getErrors() != null) {
                          alertDialogResponse(response.getErrors());
                      } else {
                          alertDialogResponse(response.getAccessCode());
                      }
                  }
              });
    }

    private void transTypesPopulator() {
        Spinner transTypesSpinner = (Spinner) findViewById(R.id.transTypes);
        List<String> list = new ArrayList<>();
        for (TransactionType type : TransactionType.values()) {
            list.add(type.name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transTypesSpinner.setAdapter(dataAdapter);

    }

    private void dateTimePopulator() {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, makeSequence(1, 12));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makeSequence
                (Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.YEAR) + 10));
        yearSpinner.setAdapter(dataAdapter);
    }

    private List<String> makeSequence(int begin, int end) {
        List<String> ret = new ArrayList(end - begin + 1);

        for (int i = begin; i <= end; ret.add(
                ((end > 12 ? "0000" : "00") + Integer.toString(i)).substring(Integer.toString(i++).length())))
            ;

        return ret;
    }

    private String errorHandler(List<String> response) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (String errorMsg : response) {
            result.append("Message ").append(i).append(" = ").append(errorMsg).append("\n");
            i++;
        }
        return result.toString();
    }
    private void fetchDataFromForm(){

        totalamount = noNullObjects(totalAmountEditText.getText().toString());
        cardName = noNullObjects(cardNameEditText.getText().toString());
        cardNumber = noNullObjects(cardNumberEditText.getText().toString());
        cvnNumber = noNullObjects(cvnEditText.getText().toString());
        expMonth = monthSpinner.getSelectedItem().toString();
        expYear = yearSpinner.getSelectedItem().toString();
        transTypes = transTypesSpinner.getSelectedItem().toString();

    }
    private String noNullObjects(String number){
        if(number.isEmpty()||number == null){
            number = "0";
        }
        return number;
    }

    public void alertDialogResponse(String result){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(RxSampleMainActivity.this)
                .setTitle("Result")
                .setMessage(result)
                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        subscription.unsubscribe();

    }

}
