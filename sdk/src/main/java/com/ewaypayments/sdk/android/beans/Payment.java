package com.ewaypayments.sdk.android.beans;

public class Payment {
    private int TotalAmount;
    private String InvoiceNumber;
    private String InvoiceDescription;
    private String InvoiceReference;
    private String CurrencyCode;

    public int getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public String getInvoiceDescription() {
        return InvoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        InvoiceDescription = invoiceDescription;
    }

    public String getInvoiceReference() {
        return InvoiceReference;
    }

    public void setInvoiceReference(String invoiceReference) {
        InvoiceReference = invoiceReference;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }
}
