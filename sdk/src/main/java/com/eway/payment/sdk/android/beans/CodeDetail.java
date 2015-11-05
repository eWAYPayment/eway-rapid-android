package com.eway.payment.sdk.android.beans;

public class CodeDetail {
    private String ErrorCode;
    private String DisplayMessage;

    public CodeDetail() {
    }

    public CodeDetail(String errorCode, String displayMessage) {
        ErrorCode = errorCode;
        DisplayMessage = displayMessage;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getDisplayMessage() {
        return DisplayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        DisplayMessage = displayMessage;
    }
}
