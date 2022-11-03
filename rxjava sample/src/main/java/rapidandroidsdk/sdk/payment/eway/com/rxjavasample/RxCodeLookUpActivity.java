package rapidandroidsdk.sdk.payment.eway.com.rxjavasample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.sdk.payment.eway.com.rxjavasample.R;
import android.widget.EditText;

import com.ewaypayments.sdk.android.RapidAPI;
import com.ewaypayments.sdk.android.entities.UserMessageResponse;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class RxCodeLookUpActivity extends AppCompatActivity {

    @Bind(R.id.errorCodes)EditText errorCodes;
    private ProgressDialog progressDialog;
    private String errorCodeNumber;
    private Subscription subscription = Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_code_look_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.submit)
    public void submitButton(){
        progressDialog = ProgressDialog.show(RxCodeLookUpActivity.this, "Processing", "Processing payment", true);
        fetchDataFromForm();
        subscription = RapidAPI.rxUserMessage(Locale.getDefault().getLanguage(),errorCodeNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserMessageResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        alertDialogResponse(e.getMessage());
                    }

                    @Override
                    public void onNext(UserMessageResponse userMessageResponse) {
                        alertDialogResponse(errorHandler(userMessageResponse.getErrorMessages()));
                    }
                });

    }

    private void fetchDataFromForm(){
        errorCodeNumber = noNullObjects(errorCodes.getText().toString());
    }

    private String noNullObjects(String number){
        if(number.isEmpty()||number == null){
            number = "0";
        }
        return number;
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

    public void alertDialogResponse(String result){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(RxCodeLookUpActivity.this)
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

    @OnClick(R.id.nextPageCode)
    public void nextPageButton(){
        Intent intent = new Intent(RxCodeLookUpActivity.this,RxSampleMainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        subscription.unsubscribe();
    }

}
