package rapidandroidsdk.eway.com.rapidandroidsdk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.eway.payment.sdk.android.RapidConfigurationException;
import com.eway.payment.sdk.android.beans.NVPair;
import com.eway.payment.sdk.android.beans.TransactionType;
import com.squareup.otto.Subscribe;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rapidandroidsdk.eway.com.rapidandroidsdk.async.EncryptClient;
import rapidandroidsdk.eway.com.rapidandroidsdk.async.PaymentClient;
import rapidandroidsdk.eway.com.rapidandroidsdk.async.UserMessageClient;
import rapidandroidsdk.eway.com.rapidandroidsdk.bus.BusProvider;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.EncryptValuesEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.UserMessageEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.utils.Utils;

/**
 * Created by alexanderparra on 1/11/15.
 */
public class EncryptionActivity extends AppCompatActivity {

    @Bind(R.id.cardNoEncrypt)EditText cardNumberEncryptEditText;
    @Bind(R.id.cvnNumberEncrypt)EditText cvnNumberEncryptEditText;

    private String cardNumber;
    private String cvnNumber;
    private ProgressDialog progressDialog;
    private EncryptClient encryptClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption);
        ButterKnife.bind(this);

    }
    @OnClick(R.id.nextPageEncrypt)
    public void nextPageButton(){
        Intent intent = new Intent(EncryptionActivity.this,CodeLookUpActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.submit_encrypt)
    public void submitButton(){

        try {
            progressDialog = ProgressDialog.show(EncryptionActivity.this, "Processing", "Processing payment", true);
            fetchDataFromForm();
            cardNumber ="4444333322221111";
            cvnNumber ="123";
            encryptClient = new EncryptClient();
            encryptClient.EncrypValuesClient(cardNumber,cvnNumber);
        } catch (RapidConfigurationException e) {
            e.printStackTrace();
        }

    }
    @Subscribe
    public void onEncryptEvent(EncryptValuesEvent event) throws RapidConfigurationException {

        StringBuilder message = new StringBuilder();
        for(NVPair pair: event.encryptItemsResponse.getItems()){
            message.append(pair.getName()).append(": ").append(pair.getValue()).append("\n");
        }
        String tempMessage = message.toString();

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        Utils utils = new Utils(EncryptionActivity.this);
        utils.alertDialogResponse(tempMessage);

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
