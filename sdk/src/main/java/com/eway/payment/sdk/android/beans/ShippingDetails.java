package com.eway.payment.sdk.android.beans;

public class ShippingDetails {
    private String FirstName;
    private String LastName;
    private ShippingMethod ShippingMethod;
    private Address ShippingAddress;
    private String Email;
    private String Phone;
    private String Fax;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public ShippingMethod getShippingMethod() {
        return ShippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        ShippingMethod = shippingMethod;
    }

    public Address getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        ShippingAddress = shippingAddress;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }
}
