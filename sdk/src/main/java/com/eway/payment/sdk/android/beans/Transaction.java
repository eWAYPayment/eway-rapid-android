package com.eway.payment.sdk.android.beans;

import java.util.ArrayList;

public class Transaction {
    private TransactionType TransactionType;
    private Customer Customer;
    private ShippingDetails ShippingDetails;
    private Payment Payment;
    private ArrayList<LineItem> LineItems;
    private ArrayList<String> Options;
    private String PartnerID;

    public TransactionType getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        TransactionType = transactionType;
    }

    public Customer getCustomer() {
        return Customer;
    }

    public void setCustomer(Customer customer) {
        Customer = customer;
    }

    public ShippingDetails getShippingDetails() {
        return ShippingDetails;
    }

    public void setShippingDetails(ShippingDetails shippingDetails) {
        ShippingDetails = shippingDetails;
    }

    public Payment getPayment() {
        return Payment;
    }

    public void setPayment(Payment payment) {
        Payment = payment;
    }

    public ArrayList<LineItem> getLineItems() {
        return LineItems;
    }

    public void setLineItems(ArrayList<LineItem> lineItems) {
        LineItems = lineItems;
    }

    public ArrayList<String> getOptions() {
        return Options;
    }

    public void setOptions(ArrayList<String> options) {
        Options = options;
    }

    public String getPartnerID() {
        return PartnerID;
    }

    public void setPartnerID(String partnerID) {
        PartnerID = partnerID;
    }
}
