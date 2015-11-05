package rapidandroidsdk.eway.com.rapidandroidsdk.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.List;

/**
 * Created by alexanderparra on 1/11/15.
 */
public class Utils {
    private Context mContext;

    public Utils(Context context){
        this.mContext = context;

    }

    public void alertDialogResponse(String result){

        new AlertDialog.Builder(mContext)
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

    public String errorHandler(List<String> response) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (String errorMsg : response) {
            result.append("Message ").append(i).append(" = ").append(errorMsg).append("\n");
            i++;
        }
        return result.toString();
    }
}
