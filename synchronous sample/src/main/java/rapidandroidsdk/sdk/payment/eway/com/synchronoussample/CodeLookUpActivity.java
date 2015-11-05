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
import com.eway.payment.sdk.android.entities.UserMessageResponse;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodeLookUpActivity extends AppCompatActivity {
    @Bind(R.id.errorCodes)EditText errorCodes;

    private String errorCodeNumber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_look_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);


    }
    @OnClick(R.id.submit)
    public void sumitButton(){
        progressDialog = ProgressDialog.show(CodeLookUpActivity.this, "Processing", "Processing payment", true);
        new HttpAsyncTask().execute();

    }

    @OnClick(R.id.nextPageCode)
    public void nextPageButton(){
        Intent intent = new Intent(CodeLookUpActivity.this,SampleMainActivity.class);
        startActivity(intent);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            errorCodeNumber = noNullObjects(errorCodes.getText().toString());
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                UserMessageResponse response = RapidAPI.userMessage(Locale.getDefault().getLanguage(), errorCodeNumber);
                if (!response.getErrorMessages().isEmpty())
                    return errorHandler(response.getErrorMessages());
                else
                    return response.getErrors();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RapidConfigurationException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            super.onPostExecute(s);
            new AlertDialog.Builder(CodeLookUpActivity.this)
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


}
