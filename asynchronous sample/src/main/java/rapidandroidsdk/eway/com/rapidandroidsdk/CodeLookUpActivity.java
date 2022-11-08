package rapidandroidsdk.eway.com.rapidandroidsdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.ewaypayments.sdk.android.RapidConfigurationException;
import com.squareup.otto.Subscribe;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rapidandroidsdk.eway.com.rapidandroidsdk.async.UserMessageClient;
import rapidandroidsdk.eway.com.rapidandroidsdk.bus.BusProvider;
import rapidandroidsdk.eway.com.rapidandroidsdk.event.UserMessageEvent;
import rapidandroidsdk.eway.com.rapidandroidsdk.utils.Utils;

public class CodeLookUpActivity extends AppCompatActivity {

    @Bind(R.id.errorCodes)EditText errorCodes;

    private String errorCodeNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_look_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.nextPageCode)
    public void nextPageButton(){
        Intent intent = new Intent(CodeLookUpActivity.this,SampleMainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.submit)
    public void submitButton(){
        try{
            fetchDataFromForm();
            UserMessageClient userMessageClient = new UserMessageClient();
            userMessageClient.userMessageClient(Locale.getDefault().getLanguage(), errorCodeNumber) ;

        }catch (RapidConfigurationException ex){
            ex.printStackTrace();
        }
    }
    @Subscribe
    public void onUserMessageEvent(UserMessageEvent event){
        Utils utils = new Utils(CodeLookUpActivity.this);
        utils.alertDialogResponse(utils.errorHandler(event.errorMessage.getErrorMessages()));
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
