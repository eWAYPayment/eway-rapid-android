package com.eway.payment.sdk.android.entities;

import com.eway.payment.sdk.android.beans.LineItem;
import com.eway.payment.sdk.android.beans.Payment;

import java.util.List;

public class SubmitPaymentRequest {
    private Customer Customer;
    private ShippingAddress ShippingAddress;
    private String ShippingMethod;
    private List<LineItem> Items;
    private List<Options> Options;
    private Payment Payment;
    private String DeviceID;
    private String PartnerID;
    private String TransactionType;
    private String Method;

    public SubmitPaymentRequest.Customer getCustomer() {
        return Customer;
    }

    public void setCustomer(SubmitPaymentRequest.Customer customer) {
        Customer = customer;
    }

    public SubmitPaymentRequest.ShippingAddress getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(SubmitPaymentRequest.ShippingAddress shippingAddress) {
        ShippingAddress = shippingAddress;
    }

    public String getShippingMethod() {
        return ShippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        ShippingMethod = shippingMethod;
    }

    public List<LineItem> getItems() {
        return Items;
    }

    public void setItems(List<LineItem> items) {
        Items = items;
    }

    public List<SubmitPaymentRequest.Options> getOptions() {
        return Options;
    }

    public void setOptions(List<SubmitPaymentRequest.Options> options) {
        Options = options;
    }

    public Payment getPayment() {
        return Payment;
    }

    public void setPayment(Payment payment) {
        Payment = payment;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getPartnerID() {
        return PartnerID;
    }

    public void setPartnerID(String partnerID) {
        PartnerID = partnerID;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public static class Options {
        String Value;

        public Options() {
        }

        public Options(String value) {
            Value = value;
        }

        public String getValue() {
            return Value;
        }

        public void setValue(String value) {
            Value = value;
        }
    }

    public static class ShippingAddress extends Addressed {
        private String Phone;

        public String getPhone() {
            return Phone;
        }


        public void setPhone(String phone) {
            Phone = phone;
        }
    }

    public static abstract class Addressed {

        private String FirstName;
        private String LastName;
        private String Street1;
        private String Street2;
        private String City;
        private String State;
        private String PostalCode;
        private String Country;

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

        public String getStreet1() {
            return Street1;
        }

        public void setStreet1(String street1) {
            Street1 = street1;
        }

        public String getStreet2() {
            return Street2;
        }

        public void setStreet2(String street2) {
            Street2 = street2;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getPostalCode() {
            return PostalCode;
        }

        public void setPostalCode(String postalCode) {
            PostalCode = postalCode;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String country) {
            Country = country;
        }

    }

    public static class Customer extends Addressed {
        private String TokenCustomerID;
        private String Reference;
        private String Title;
        private String CompanyName;
        private String JobDescription;
        private String Email;
        private String Phone;
        private String Mobile;
        private String Comments;
        private String Fax;
        private String Url;
        private CardDetails CardDetails;

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

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getComments() {
            return Comments;
        }

        public void setComments(String comments) {
            Comments = comments;
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

        public SubmitPaymentRequest.CardDetails getCardDetails() {
            return CardDetails;
        }

        public void setCardDetails(SubmitPaymentRequest.CardDetails cardDetails) {
            CardDetails = cardDetails;
        }

        public String getTokenCustomerID() {
            return TokenCustomerID;
        }


        public void setTokenCustomerID(String tokenCustomerID) {
            TokenCustomerID = tokenCustomerID;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }
    }

    public static class CardDetails {
        private String Name;
        private String Number;
        private String ExpiryMonth;
        private String ExpiryYear;
        private String StartMonth;
        private String StartYear;
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

        public String getCVN() {
            return CVN;
        }

        public void setCVN(String CVN) {
            this.CVN = CVN;
        }
    }
}
