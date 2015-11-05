package com.eway.payment.sdk.android;

import java.util.ArrayList;

public class RapidConfigurationException extends Exception {
    private ArrayList<String> ErrorCodes;

    public RapidConfigurationException(String... errorCodes) {
        ErrorCodes = Utils.newArrayList(errorCodes);
    }

    public RapidConfigurationException(ArrayList<String> errorCodes) {
        ErrorCodes = errorCodes;
    }

    public String getErrorCodes() throws RapidConfigurationException {
        return Utils.join(",", ErrorCodes);
    }
}
