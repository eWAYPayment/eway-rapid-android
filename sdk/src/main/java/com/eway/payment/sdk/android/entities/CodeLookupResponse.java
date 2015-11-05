package com.eway.payment.sdk.android.entities;

import com.eway.payment.sdk.android.beans.CodeDetail;

import java.util.ArrayList;

public class CodeLookupResponse {
    private String Language;
    private ArrayList<CodeDetail> CodeDetails;
    private String Errors;

    public CodeLookupResponse(String errors, String language, ArrayList<CodeDetail> codeDetails) {
        Language = language;
        CodeDetails = codeDetails;
        Errors = errors;
    }

    public CodeLookupResponse() {

    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public ArrayList<CodeDetail> getCodeDetails() {
        return CodeDetails;
    }

    public void setCodeDetails(ArrayList<CodeDetail> codeDetails) {
        CodeDetails = codeDetails;
    }

    public String getErrors() {
        return Errors;
    }

    public void setErrors(String errors) {
        Errors = errors;
    }
}
