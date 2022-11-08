package rapidandroidsdk.eway.com.rapidandroidsdk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.ewaypayments.sdk.android.RapidAPI;
import com.ewaypayments.sdk.android.RapidConfigurationException;
import com.ewaypayments.sdk.android.beans.CardDetails;
import com.ewaypayments.sdk.android.beans.Customer;
import com.ewaypayments.sdk.android.beans.Payment;
import com.ewaypayments.sdk.android.beans.Transaction;
import com.ewaypayments.sdk.android.beans.TransactionType;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rapidandroidsdk.eway.com.rapidandroidsdk.async.EncryptClient;
import rapidandroidsdk.eway.com.rapidandroidsdk.async.PaymentClient;
import rapidandroidsdk.eway.com.rapidandroidsdk.async.UserMessageClient;
import rapidandroidsdk.eway.com.rapidandroidsdk.bus.BusProvider;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.EncryptErrorHandlerEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.EncryptExceptionEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.EncryptValuesEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.PaymentErrorHandlerEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.PaymentExceptionEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.SubmitPayEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.UserMessageEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.UserMessageExceptionEvent;

public class SampleMainActivity extends AppCompatActivity {

    @Bind(R.id.expMonth)Spinner monthSpinner;
    @Bind(R.id.expYear)Spinner yearSpinner;
    @Bind(R.id.totalAmount)EditText totalAmountEditText;
    @Bind(R.id.cardName)EditText cardNameEditText;
    @Bind(R.id.cardNumber)EditText cardNumberEditText;
    @Bind(R.id.cvn)EditText cvnEditText;
    @Bind(R.id.transTypes)Spinner transTypesSpinner;


    private ProgressDialog progressDialog;
    private String totalamount;
    private String cardName;
    private String cardNumber;
    private String cvnNumber;
    private String expMonth;
    private String expYear;
    private String transTypes;
    private EncryptClient encryptClient;
    private CardDetails cardDetails = new CardDetails();
    private Transaction transaction = new Transaction();
    private Payment payment = new Payment();
    private Customer customer = new Customer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dateTimePopulator();
        transTypesPopulator();
        RapidAPI.PublicAPIKey = "epk-6C961B95-D93A-443C-BCB9-64B6DBDC1C1B";
        RapidAPI.RapidEndpoint = "https://api.sandbox.ewaypayments.com/";


    }
    @OnClick(R.id.submit)
    public void asyncPayment()  {
        try {
            progressDialog = ProgressDialog.show(SampleMainActivity.this,"Processing","Processing payment", true);
            fetchDataFromForm();
            payment.setTotalAmount(Integer.parseInt(totalamount));
            cardDetails.setName(cardName);
            encryptClient = new EncryptClient();
            encryptClient.EncrypValuesClient(cardNumber,cvnNumber);
        } catch (RapidConfigurationException e) {
            e.printStackTrace();
        }


    }
    @OnClick(R.id.nextPage)
    public void nextEncryption(){
        Intent intent = new Intent(SampleMainActivity.this,EncryptionActivity.class);
        SampleMainActivity.this.startActivity(intent);

    }

    @Subscribe
    public void onEncryptEvent(EncryptValuesEvent event) throws RapidConfigurationException {

        cardDetails.setNumber(event.encryptItemsResponse.getItems().get(0).getValue());
        cardDetails.setCVN(event.encryptItemsResponse.getItems().get(1).getValue());
        cardDetails.setExpiryMonth(expMonth);
        cardDetails.setExpiryYear(expYear);
        customer.setCardDetails(cardDetails);
        transaction.setTransactionType(TransactionType.valueOf((transTypes.isEmpty() ? TransactionType.Purchase.toString() : transTypes.toString())));
        transaction.setPayment(payment);
        transaction.setCustomer(customer);
        PaymentClient paymentClient = new PaymentClient();
        paymentClient.PaymentProcess(transaction);


    }
    @Subscribe
    public void onErrorEncryptEvent(EncryptErrorHandlerEvent event) throws RapidConfigurationException {
        UserMessageClient userMessageClient = new UserMessageClient();
        userMessageClient.userMessageClient(Locale.getDefault().getLanguage(), event.errorEncrypt);
    }

    @Subscribe
    public void onSubmitPaymentEvent(SubmitPayEvent event){
       alertDialogResponse(event.submitPayResponse.getAccessCode());
    }
    @Subscribe
    public void onErrorSubmitPaymentEvent(PaymentErrorHandlerEvent event) throws RapidConfigurationException {
        UserMessageClient userMessageClient = new UserMessageClient();
        userMessageClient.userMessageClient(Locale.getDefault().getLanguage(), event.paymentError) ;
    }
    @Subscribe
    public void onUserMessageEvent(UserMessageEvent event){
        alertDialogResponse(errorHandler(event.errorMessage.getErrorMessages()));
    }
    @Subscribe
    public void onEncryptExceptionEvent(EncryptExceptionEvent event){
        alertDialogResponse(event.encryptException);
    }

    @Subscribe
    public void onSubmitPaymentExceptionEvent(PaymentExceptionEvent event){
        alertDialogResponse(event.paymentException);
    }
    @Subscribe
    public void onUserMessageExceptionEvent(UserMessageExceptionEvent event){
        alertDialogResponse(event.userMessageException);
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

    public void alertDialogResponse(String result){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(SampleMainActivity.this)
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

    private String errorHandler(List<String> response) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (String errorMsg : response) {
            result.append("Message ").append(i).append(" = ").append(errorMsg).append("\n");
            i++;
        }
        return result.toString();
    }

    private String noNullObjects(String number){
        if(number.isEmpty()||number == null){
            number = "0";
        }
        return number;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        BusProvider.getInstance().register(this);

    }
    @Override
    protected void onPause(){
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }


}
