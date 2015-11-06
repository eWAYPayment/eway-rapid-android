package rapidandroidsdk.sdk.payment.eway.com.synchronoussample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sdk.payment.eway.com.synchronoussample.R;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RapidAPI.PublicAPIKey = "epk-6C961B95-D93A-443C-BCB9-64B6DBDC1C1B";
        RapidAPI.RapidEndpoint = "https://api.sandbox.ewaypayments.com/";
        transTypesPopulator();
        dateTimePopulator();

    }
    @OnClick(R.id.submit)
    public void sumitButton(){
        progressDialog = ProgressDialog.show(SampleMainActivity.this, "Processing", "Processing payment", true);
        new HttpAsyncTask().execute();
    }
    @OnClick(R.id.nextPage)
    public void nextPageButton(){
        Intent intent = new Intent(SampleMainActivity.this, EncryptionActivity.class);
        startActivity(intent);
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        String errorMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetchDataFromForm();

        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            new AlertDialog.Builder(SampleMainActivity.this)
                    .setTitle("Result")
                    .setMessage(s)
                    .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .create()
                    .show();
        }

        @Override
        protected String doInBackground(String... params) {

            Transaction transaction = new Transaction();
            Payment payment = new Payment();
            CardDetails cardDetails = new CardDetails();
            Customer customer = new Customer();
            payment.setTotalAmount(Integer.parseInt(totalamount));
            cardDetails.setName(cardName);
            //Encrypt card data before sending
            try {
                EncryptItemsResponse nCryptedData = encryptCard(cardNumber, cvnNumber);
                if (nCryptedData.getErrors() != null)
                    return errorHandler(RapidAPI.userMessage(Locale.getDefault().getLanguage(), nCryptedData.getErrors()).getErrorMessages());
                cardDetails.setNumber(nCryptedData.getItems().get(0).getValue());
                cardDetails.setCVN(nCryptedData.getItems().get(1).getValue());
                cardDetails.setExpiryMonth(expMonth);
                cardDetails.setExpiryYear(expYear);
                customer.setCardDetails(cardDetails);

                transaction.setTransactionType(TransactionType.valueOf((transTypes.isEmpty() ? TransactionType.Purchase.toString() : transTypes.toString())));
                transaction.setPayment(payment);
                transaction.setCustomer(customer);
                SubmitPayResponse response = null;
                response = RapidAPI.submitPayment(transaction);


                if (response.getErrors() == null)
                    return ("Succeed. Submission ID is: " + response.getAccessCode());
                else {
                    errorMessage = errorHandler(RapidAPI.userMessage(Locale.getDefault().getLanguage(), response.getErrors()).getErrorMessages());
                }

            } catch (RapidConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return errorMessage;
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

        private EncryptItemsResponse encryptCard(String cardNo, String CVN) throws IOException, RapidConfigurationException {
            ArrayList<NVPair> values = new ArrayList<>();
            values.add(new NVPair("Card", cardNo));
            values.add(new NVPair("CVN", CVN));
            return RapidAPI.encryptValues(values);
        }

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
                ((end > 12 ? "0000" : "00") + Integer.toString(i)).substring(Integer.toString(i++).length())));

        return ret;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
