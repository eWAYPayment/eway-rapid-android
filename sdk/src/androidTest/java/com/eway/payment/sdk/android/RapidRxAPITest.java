package com.eway.payment.sdk.android;

import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.eway.payment.sdk.android.beans.Address;
import com.eway.payment.sdk.android.beans.CardDetails;
import com.eway.payment.sdk.android.beans.Customer;
import com.eway.payment.sdk.android.beans.LineItem;
import com.eway.payment.sdk.android.beans.NVPair;
import com.eway.payment.sdk.android.beans.Payment;
import com.eway.payment.sdk.android.beans.ShippingDetails;
import com.eway.payment.sdk.android.beans.ShippingMethod;
import com.eway.payment.sdk.android.beans.Transaction;
import com.eway.payment.sdk.android.beans.TransactionType;
import com.eway.payment.sdk.android.entities.EncryptItemsResponse;
import com.eway.payment.sdk.android.entities.SubmitPayResponse;

import java.util.ArrayList;
import rx.functions.Action1;

/**
 * Created by alexander.parra on 26/10/2015.
 */
public class RapidRxAPITest extends InstrumentationTestCase {
    public static final String ENDPOINT = "https://api.sandbox.ewaypayments.com/staging-au/";
    public static final String PUBLIC_API_KEY = "epk-6C961B95-D93A-443C-BCB9-64B6DBDC1C1B";

    @Override
    public void setUp() throws Exception {
        RapidAPI.RapidEndpoint = ENDPOINT;
        RapidAPI.PublicAPIKey = PUBLIC_API_KEY;
    }

    @MediumTest
    public void testAsycSubmitPaymentSubmissionIDShouldBeCreated() throws Exception {
        Transaction transaction = getTransaction();
        RapidAPI.rxSubmitPayment(transaction).subscribe(new Action1<SubmitPayResponse>() {
            @Override
            public void call(SubmitPayResponse submitPayResponse) {
                assertNotNull(submitPayResponse.getAccessCode());
            }
        });

    }

    @MediumTest
    public void testAsycEncryptValuesShouldSucceed() throws Exception {
        ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", "4444333322221111"));
        values.add(new NVPair("CVN", "123"));
        RapidAPI.rxEncryptValues(values).subscribe(new Action1<EncryptItemsResponse>() {
            @Override
            public void call(EncryptItemsResponse encryptItemsResponse) {
                assertEquals("Card", encryptItemsResponse.getItems().get(0).getName());
                assertNotNull(encryptItemsResponse.getItems().get(0).getValue());

                assertEquals("CVN", encryptItemsResponse.getItems().get(1).getName());
                assertNotNull(encryptItemsResponse.getItems().get(1).getValue());
                assertNull(encryptItemsResponse.getErrors());
            }
        });

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
