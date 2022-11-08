package com.ewaypayments.sdk.android.beans;

public class CardDetails {
    private String Name;
    private String Number;
    private String ExpiryMonth;
    private String ExpiryYear;
    private String StartMonth;
    private String StartYear;
    private String IssueNumber;
    private String CVN;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getExpiryMonth() {
        return ExpiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        ExpiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return ExpiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        ExpiryYear = expiryYear;
    }

    public String getStartMonth() {
        return StartMonth;
    }

    public void setStartMonth(String startMonth) {
        StartMonth = startMonth;
    }

    public String getStartYear() {
        return StartYear;
    }

    public void setStartYear(String startYear) {
        StartYear = startYear;
    }

    public String getIssueNumber() {
        return IssueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        IssueNumber = issueNumber;
    }

    public String getCVN() {
        return CVN;
    }

    public void setCVN(String CVN) {
        this.CVN = CVN;
    }
}
