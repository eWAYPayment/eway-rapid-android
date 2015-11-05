package com.eway.payment.sdk.android.entities;

import java.util.ArrayList;

public class UserMessageResponse {
    private ArrayList<String> errorMessages;
    private String Errors;

    public UserMessageResponse() {
    }

    public UserMessageResponse(ArrayList<String> errorMessages, String errors) {
        this.errorMessages = errorMessages;
        this.Errors = errors;
    }

    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(ArrayList<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getErrors() {
        return Errors;
    }

    public void setErrors(String errors) {
        this.Errors = errors;
    }
}
