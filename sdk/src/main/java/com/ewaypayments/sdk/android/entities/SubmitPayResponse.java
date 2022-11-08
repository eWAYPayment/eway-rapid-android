package com.ewaypayments.sdk.android.entities;

/**
 * Created by alexander.parra on 23/10/2015.
 */
public class SubmitPayResponse {

    private String AccessCode;
    private String Status;
    private String Errors;

    public SubmitPayResponse(String errors, String accessCode, String status) {
        Errors = errors;
        AccessCode = accessCode;
        Status = status;
    }

    public SubmitPayResponse(){}


    public String getAccessCode() {
        return AccessCode;
    }

    public void setAccessCode(String accessCode) {
        AccessCode = accessCode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getErrors() {
        return Errors;
    }

    public void setErrors(String errors) {
        Errors = errors;
    }
}
