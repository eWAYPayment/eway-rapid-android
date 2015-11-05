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
import android.widget.EditText;

import com.eway.payment.sdk.android.RapidAPI;
import com.eway.payment.sdk.android.RapidConfigurationException;
import com.eway.payment.sdk.android.beans.NVPair;
import com.eway.payment.sdk.android.entities.EncryptItemsResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EncryptionActivity extends AppCompatActivity {
    @Bind(R.id.cardNoEncrypt)EditText cardNumberEncryptEditText;
    @Bind(R.id.cvnNumberEncrypt)EditText cvnNumberEncryptEditText;

    private String cardNumber;
    private String cvnNumber;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

    }
    @OnClick(R.id.submit_encrypt)
    public void SubmitEncryptButton(){
        progressDialog = ProgressDialog.show(EncryptionActivity.this, "Processing", "Processing payment", true);
        new HttpAsycTask().execute();
    }


    @OnClick(R.id.nextPageEncrypt)
    public void NextPageButton(){
        Intent intent = new Intent(EncryptionActivity.this, CodeLookUpActivity.class);
        startActivity(intent);
    }


    private class HttpAsycTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cardNumber = noNullObjects(cardNumberEncryptEditText.getText().toString());
            cvnNumber = noNullObjects(cvnNumberEncryptEditText.getText().toString());

        }
        @Override
        protected String doInBackground(String... params) {
            ArrayList<NVPair> values = new ArrayList<>();
            values.add(new NVPair("Card", cardNumber));
            values.add(new NVPair("CVN", cvnNumber));
            EncryptItemsResponse response = null;
            try {
                response = RapidAPI.encryptValues(values);
                if (response.getErrors() == null){
                    StringBuilder message = new StringBuilder();
                    for(NVPair pair: response.getItems()){
                        message.append(pair.getName()).append(": ").append(pair.getValue()).append("\n");
                    }
                    return message.toString();
                }
                else
                    return errorHandler(RapidAPI.userMessage(Locale.getDefault().getLanguage(), response.getErrors()).getErrorMessages());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RapidConfigurationException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            new AlertDialog.Builder(EncryptionActivity.this)
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
        private String noNullObjects(String number){
            if(number.isEmpty()||number == null){
                number = "0";
            }
            return number;
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
