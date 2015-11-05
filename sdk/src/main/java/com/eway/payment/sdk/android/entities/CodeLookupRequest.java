package com.eway.payment.sdk.android.entities;

import java.util.ArrayList;
import java.util.List;

public class CodeLookupRequest {
    private String Language;
    ArrayList<String> ErrorCodes;

    public CodeLookupRequest(String language, List<String> errorCodes) {
        this.Language = language;
        this.ErrorCodes = new ArrayList<String>(errorCodes);
    }

    public CodeLookupRequest(String s) {

    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public ArrayList<String> getErrorCodes() {
        return ErrorCodes;
    }

    public void setErrorCodes(ArrayList<String> errorCodes) {
        ErrorCodes = errorCodes;
    }
}
