package com.ewaypayments.sdk.android.beans;

public class NVPair {
    private String Name;
    private String Value;

    public NVPair() {
    }

    public NVPair(String name, String value) {
        Name = name;
        Value = value;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
