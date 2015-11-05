package com.eway.payment.sdk.android.beans;

import android.content.ContentResolver;
import android.provider.Settings;

public class Customer {
    private String TokenCustomerID;
    private String Reference;
    private String Title;
    private String FirstName;
    private String LastName;
    private String CompanyName;
    private String JobDescription;
    private Address	Address;
    private String Phone;
    private String Mobile;
    private String Fax;
    private String Url;
    private String Comments;
    private CardDetails	CardDetails;
    private String Email;

    public String getTokenCustomerID() {
        return TokenCustomerID;
    }

    public void setTokenCustomerID(String tokenCustomerID) {
        TokenCustomerID = tokenCustomerID;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

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

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getJobDescription() {
        return JobDescription;
    }

    public void setJobDescription(String jobDescription) {
        JobDescription = jobDescription;
    }

    public Address getAddress() {
        return Address;
    }

    public void setAddress(Address address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public CardDetails getCardDetails() {
        return CardDetails;
    }

    public void setCardDetails(CardDetails cardDetails) {
        CardDetails = cardDetails;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
