package com.eway.payment.sdk.android.entities;

import com.eway.payment.sdk.android.beans.NVPair;

import java.util.ArrayList;

public class EncryptValuesRequest {
    private String Method;
    private ArrayList<NVPair> Items;

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public ArrayList<NVPair> getItems() {
        return Items;
    }

    public void setItems(ArrayList<NVPair> items) {
        Items = items;
    }
}
