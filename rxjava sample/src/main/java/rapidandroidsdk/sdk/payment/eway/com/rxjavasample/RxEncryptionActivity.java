package rapidandroidsdk.sdk.payment.eway.com.rxjavasample;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.sdk.payment.eway.com.rxjavasample.R;
import android.widget.EditText;

import com.eway.payment.sdk.android.RapidAPI;
import com.eway.payment.sdk.android.beans.NVPair;
import com.eway.payment.sdk.android.entities.EncryptItemsResponse;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class RxEncryptionActivity extends AppCompatActivity {

    @Bind(R.id.cardNoEncrypt)EditText cardNumberEncryptEditText;
    @Bind(R.id.cvnNumberEncrypt)EditText cvnNumberEncryptEditText;

    private String cardNumber;
    private String cvnNumber;
    private ProgressDialog progressDialog;
    private Subscription subscription = Subscriptions.empty();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_encryption);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);


    }
    @OnClick(R.id.submit_encrypt)
    public void encryptButton(){
        progressDialog = ProgressDialog.show(RxEncryptionActivity.this, "Processing", "Processing payment", true);
        rxEncryption();
    }
    @OnClick(R.id.nextPageEncrypt)
    public void nextPageButton(){
        Intent intent = new Intent(RxEncryptionActivity.this,RxCodeLookUpActivity.class);
        startActivity(intent);
    }

    private void rxEncryption(){

        fetchDataFromForm();
        final ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", cardNumber));
        values.add(new NVPair("CVN", cvnNumber));


        subscription = RapidAPI.rxEncryptValues(values)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EncryptItemsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        alertDialogResponse(e.getMessage());
                    }

                    @Override
                    public void onNext(EncryptItemsResponse response) {
                        StringBuilder message = new StringBuilder();
                        for (NVPair pair : response.getItems()) {
                            message.append(pair.getName()).append(": ").append(pair.getValue()).append("\n");
                        }
                        String tempMessage = message.toString();
                        alertDialogResponse(tempMessage);
                    }
                });


    }
    public void alertDialogResponse(String result){
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        new AlertDialog.Builder(RxEncryptionActivity.this)
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



    private void fetchDataFromForm(){
        cardNumber = noNullObjects(cardNumberEncryptEditText.getText().toString());
        cvnNumber = noNullObjects(cvnNumberEncryptEditText.getText().toString());
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
        subscription.unsubscribe();
    }



}
