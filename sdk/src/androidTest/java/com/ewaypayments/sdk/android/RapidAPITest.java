package com.ewaypayments.sdk.android;


import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.ewaypayments.sdk.android.beans.Address;
import com.ewaypayments.sdk.android.beans.CardDetails;
import com.ewaypayments.sdk.android.beans.Customer;
import com.ewaypayments.sdk.android.beans.LineItem;
import com.ewaypayments.sdk.android.beans.NVPair;
import com.ewaypayments.sdk.android.beans.Payment;
import com.ewaypayments.sdk.android.beans.ShippingDetails;
import com.ewaypayments.sdk.android.beans.ShippingMethod;
import com.ewaypayments.sdk.android.beans.Transaction;
import com.ewaypayments.sdk.android.beans.TransactionType;
import com.ewaypayments.sdk.android.entities.EncryptItemsResponse;
import com.ewaypayments.sdk.android.entities.SubmitPayResponse;
import com.ewaypayments.sdk.android.entities.UserMessageResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RapidAPITest extends InstrumentationTestCase {
    public static final String ENDPOINT = "https://api.sandbox.ewaypayments.com/staging-au/";
    public static final String PUBLIC_API_KEY = "epk-6C961B95-D93A-443C-BCB9-64B6DBDC1C1B";

    @Override
    public void setUp() throws Exception {
        RapidAPI.RapidEndpoint = ENDPOINT;
        RapidAPI.PublicAPIKey = PUBLIC_API_KEY;
    }

    @MediumTest
    public void testGenerateDeviceIdShouldReturnSameValue() throws Exception {
        assertEquals(Utils.getUniquePsuedoID(), Utils.getUniquePsuedoID());
    }

    @MediumTest
    public void testSubmitPaymentSubmissionIDShouldBeCreated() throws Exception {
        Transaction transaction = getTransaction();
        SubmitPayResponse response = RapidAPI.submitPayment(transaction);
        assertNotNull(response.getAccessCode());
    }

    @MediumTest
    public void testSubmitPaymentWithInvalidPaymentAmountSubmissionIDShouldNull() throws Exception {
        Payment payment = new Payment();
        payment.setTotalAmount(0);
        Transaction transaction = getTransaction();
        transaction.setPayment(payment);
        assertNull(RapidAPI.submitPayment(transaction).getAccessCode());
    }

    /*@MediumTest
    public void testSubmitPaymentWithEmptyPublicApiKey() throws Exception {
        RapidAPI.PublicAPIKey = null;
        Transaction transaction = getTransaction();
        RapidAPI.submitPayment(transaction);
        assertEquals(RapidAPI.submitPayment(transaction).getErrors(), "S9991");
    }*/

    @MediumTest
    public void testSubmitPaymentWithEmptyTransactionShouldSuccess() throws Exception {
        Transaction transaction = new Transaction();
        assertTrue(RapidAPI.submitPayment(transaction).getErrors().length() > 0);
    }

    @MediumTest
    public void testEncryptValuesWithNoValuesShouldSucceed() throws Exception {
        ArrayList<NVPair> values = new ArrayList<>();
        EncryptItemsResponse response = RapidAPI.encryptValues(values);
        assertEquals(response.getItems().size(), 0);
    }

   @MediumTest
    public void testEncryptValuesWithNullValuesShouldSucceed() throws Exception {
        ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair(null, null));
        EncryptItemsResponse response = RapidAPI.encryptValues(values);
        assertEquals(response.getItems().size(), 0);
    }



    @MediumTest
    public void testEncryptValuesShouldSucceed() throws Exception {
        ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("card", "4444333322221111"));
        values.add(new NVPair("CVN", "123"));

        EncryptItemsResponse response = RapidAPI.encryptValues(values);
        assertEquals("card", response.getItems().get(0).getName());
        assertNotNull(response.getItems().get(0).getValue());

        assertEquals("CVN", response.getItems().get(1).getName());
        assertNotNull(response.getItems().get(1).getValue());
        assertNull(response.getErrors());
    }


    @MediumTest
    public void testUserMessagesWithNullErrorCodesShouldSuccess() throws Exception {
        UserMessageResponse response = RapidAPI.userMessage("en-us", null);
        assertNotNull(response.getErrorMessages().size() == 0);
    }

    @MediumTest
    public void testUserMessagesWithNullLanguageCodesShouldUseDeviceLang() throws Exception {
        String errorCodes = ("V6101,V6068");
        List<String> response = RapidAPI.userMessage(null, errorCodes).getErrorMessages();
        assertNotNull(response.get(0));
        assertNotNull(response.get(1));
    }

    @MediumTest
    public void testUserMessagesWithFrenchLocaleCodesShouldSucceed() throws Exception {
        String errorCodes = "V6101";

        List<String> response = RapidAPI.userMessage(Locale.FRENCH.toString(), errorCodes).getErrorMessages();
        assertNotNull(response.get(0));
    }

    @MediumTest
    public void testUserMessagesShouldSucceed() throws Exception {
        String errorCodes = "V6101,V6068";
        List<String> response = RapidAPI.userMessage("en-us", errorCodes).getErrorMessages();
        assertNotNull(response.get(0));
        assertNotNull(response.get(1));
    }

    @MediumTest
    public void testGetEwayClientWithNullEndpointShouldReturnCodeS9990Msg() throws Exception {
        RapidAPI.RapidEndpoint = null;
        RapidAPI.PublicAPIKey = PUBLIC_API_KEY;
        EncryptItemsResponse response = RapidAPI.encryptValues(null);
        assertEquals(response.getErrors(), "S9990");
    }

    @MediumTest
    public void testGetEwayClientWithInvalidKeyShouldReturnCodeS9991Msg() throws Exception {
        RapidAPI.RapidEndpoint = null;
        RapidAPI.PublicAPIKey = "jasdadsdasadsj";
        EncryptItemsResponse response = RapidAPI.encryptValues(null);
        assertEquals(response.getErrors(), "S9990");
    }

    @MediumTest
    public void testGetEwayClientWithInvalidAPIKeyShouldReturnCodeS9991Msg() throws Exception {
        RapidAPI.RapidEndpoint = ENDPOINT;
        RapidAPI.PublicAPIKey = null;
        EncryptItemsResponse response = RapidAPI.encryptValues(null);
        assertEquals(response.getErrors(), "S9991");
    }

    @MediumTest
    public void testGetEwayClientWithInvalidInvalidEndpointShouldReturnCodeS9992Msg() throws Exception {
        RapidAPI.RapidEndpoint = "aaaaaaaaaa";
        RapidAPI.PublicAPIKey = PUBLIC_API_KEY;
        EncryptItemsResponse response = RapidAPI.encryptValues(null);
        assertEquals("S9992", response.getErrors());
    }

    @MediumTest
    public void  testEncryptValueWithUrlWithoutTrailingSlashShouldSuccess() throws Exception {
        RapidAPI.RapidEndpoint = "https://api.sandbox.ewaypayments.com/staging-au";
        RapidAPI.PublicAPIKey = PUBLIC_API_KEY;
        EncryptItemsResponse response = RapidAPI.encryptValues(null);
        assertEquals(response.getErrors(),null);
    }

    private Transaction getTransaction() {
        Transaction transaction = new Transaction();
        CardDetails cardDetails = new CardDetails();
        cardDetails.setName("John Smith");
        cardDetails.setNumber("4444333322221111");
        cardDetails.setExpiryMonth("12");
        cardDetails.setExpiryYear("25");
        cardDetails.setCVN("123");
        Address address = new Address();
        address.setCity("El Segundo");
        address.setState("CA");
        address.setCountry("au");
        address.setPostalCode("123456");
        address.setStreet1("Street1");
        Customer customer = new Customer();
        customer.setCardDetails(cardDetails);
        customer.setLastName("John");
        customer.setFirstName("Smith");
        customer.setPhone("1234");
        customer.setTitle("Mr");
        customer.setAddress(address);
        ArrayList<String> options = new ArrayList<>();
        options.add("Gift package");
        transaction.setOptions(options);
        ArrayList<LineItem> lineItems = new ArrayList<>();
        LineItem lineItem1 = new LineItem();
        lineItem1.setSKU("11111");
        lineItem1.setDescription("a test item");
        lineItem1.setQuantity(1);
        lineItem1.setUnitCost(100);
        lineItem1.setTotal(100);
        lineItem1.setTax(10);
        lineItems.add(lineItem1);
        transaction.setLineItems(lineItems);
        Payment payment = new Payment();
        payment.setTotalAmount(1000);
        ShippingDetails shippingDetails = new ShippingDetails();
        shippingDetails.setLastName("John");
        shippingDetails.setFirstName("Smith");
        shippingDetails.setPhone("1234");
        shippingDetails.setShippingMethod(ShippingMethod.Military);
        shippingDetails.setShippingAddress(address);
        transaction.setTransactionType(TransactionType.Purchase);
        transaction.setPayment(payment);
        transaction.setCustomer(customer);
        transaction.setShippingDetails(shippingDetails);
        return transaction;
    }
}
