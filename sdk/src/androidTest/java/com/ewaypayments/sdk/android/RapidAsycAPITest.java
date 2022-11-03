package com.ewaypayments.sdk.android;


import android.test.InstrumentationTestCase;

import com.ewaypayments.sdk.android.beans.Address;
import com.ewaypayments.sdk.android.beans.CardDetails;
import com.ewaypayments.sdk.android.beans.Customer;
import com.ewaypayments.sdk.android.beans.LineItem;
import com.ewaypayments.sdk.android.beans.Payment;
import com.ewaypayments.sdk.android.beans.ShippingDetails;
import com.ewaypayments.sdk.android.beans.ShippingMethod;
import com.ewaypayments.sdk.android.beans.Transaction;
import com.ewaypayments.sdk.android.beans.TransactionType;
import com.ewaypayments.sdk.android.entities.EncryptItemsResponse;
import com.ewaypayments.sdk.android.entities.SubmitPayResponse;

import java.util.ArrayList;

public class RapidAsycAPITest extends InstrumentationTestCase implements RapidAPI.RapidRecordingTransactionListener,RapidAPI.RapidEncryptValuesListener {
    public static final String ENDPOINT = "https://api.sandbox.ewaypayments.com/staging-au/";
    public static final String PUBLIC_API_KEY = "epk-6C961B95-D93A-443C-BCB9-64B6DBDC1C1B";

    @Override
    public void setUp() throws Exception {
        RapidAPI.RapidEndpoint = ENDPOINT;
        RapidAPI.PublicAPIKey = PUBLIC_API_KEY;
    }

   /* @MediumTest
    public void testGenerateDeviceIdShouldReturnSameValue() throws Exception {
        assertEquals(Utils.getUniquePsuedoID(), Utils.getUniquePsuedoID());
    }

    @MediumTest
    public void testAsycSubmitPaymentSubmissionIDShouldBeCreated() throws Exception {
        Transaction transaction = getTransaction();
        RapidAPI.asycSubmitPayment(transaction,this);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                if (response instanceof SubmitPayResponse)
                    assertNotNull(((SubmitPayResponse) response).getAccessCode());
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });


    }

    @MediumTest
    public void testAsycSubmitPaymentWithInvalidPaymentAmountSubmissionIDShouldNull() throws Exception {
        Payment payment = new Payment();
        payment.setTotalAmount(0);
        Transaction transaction = getTransaction();
        transaction.setPayment(payment);
        RapidAPI.asycSubmitPayment(transaction);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertNull(((SubmitPayResponse) response).getAccessCode());
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });

    }
    /*@MediumTest
    public void testAsycSubmitPaymentWithEmptyPublicApiKey() throws Exception {
        RapidAPI.PublicAPIKey = null;
        Transaction transaction = getTransaction();
        RapidAPI.asycSubmitPayment(transaction);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertEquals(((SubmitPayResponse) response).getErrors(), "S9991");
            }

            @Override
            public <T>void onResponseReceivedFailure(T error) {
                assertEquals(((SubmitPayResponse) error).getErrors(), "S9991");
            }
        });

    }*/
   /* @MediumTest
    public void testAsycSubmitPaymentWithEmptyTransactionShouldSuccess() throws Exception {
        Transaction transaction = new Transaction();
        RapidAPI.asycSubmitPayment(transaction);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertTrue(((SubmitPayResponse) response).getErrors().length() > 0);
            }

            @Override
            public <T>void onResponseReceivedFailure(T error) {

            }
        });

    }

    @MediumTest
    public void testAsycEncryptValuesWithNoValuesShouldSucceed() throws Exception {
        ArrayList<NVPair> values = new ArrayList<>();
        RapidAPI.asycEncryptValues(values);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertEquals(((EncryptItemsResponse) response).getItems().size(), 0);
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });

    }


    @MediumTest
    public void testAyscEncryptValuesWithNullValuesShouldSucceed() throws Exception {
        ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair(null, null));
        RapidAPI.asycEncryptValues(values);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertEquals(((EncryptItemsResponse) response).getItems().size(), 0);
            }

            @Override
            public <T>void onResponseReceivedFailure(T error) {
                assertEquals(((EncryptItemsResponse) error).getItems().size(), 0);
            }
        });

    }*/
   /*@MediumTest
    public void testAsycEncryptValuesShouldSucceed() throws Exception {
        ArrayList<NVPair> values = new ArrayList<>();
        values.add(new NVPair("Card", "4444333322221111"));
        values.add(new NVPair("CVN", "123"));

       RapidAPI.addRapidEncrypyValuesListener(new RapidAPI.RapidEncryptValuesListener() {
           @Override
           public void onResponseReceivedSuccess(EncryptItemsResponse response) {
               assertEquals("Card", response.getItems().get(0).getName());
               assertNotNull(response.getItems().get(0).getValue());

               assertEquals("CVN", response.getItems().get(1).getName());
               assertNotNull(response.getItems().get(1).getValue());
               assertNull(response.getErrors());


           }

           @Override
           public void onResponseReceivedError(String errorResponse) {

           }

           @Override
           public void onResponseReceivedFailure(String errorFailure) {

           }
       });
        RapidAPI.asycEncryptValues(values);
        //subscribe
        /*RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                if (response instanceof EncryptItemsResponse) {
                    assertEquals("Card", ((EncryptItemsResponse) response).getItems().get(0).getName());
                    assertNotNull(((EncryptItemsResponse) response).getItems().get(0).getValue());

                    assertEquals("CVN", ((EncryptItemsResponse) response).getItems().get(1).getName());
                    assertNotNull(((EncryptItemsResponse) response).getItems().get(1).getValue());
                    assertNull(((EncryptItemsResponse) response).getErrors());

                }
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

                try {
                    assertNotNull(((ResponseBody) error).string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });*/



    /*@MediumTest
    public void testAsycUserMessagesWithNullErrorCodesShouldSuccess() throws Exception {
        RapidAPI.asycUserMessage("en-us", null);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertNotNull(((UserMessageResponse)response).getErrorMessages().size() == 0);
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });
    }

    @MediumTest
    public void testAsycUserMessagesWithNullLanguageCodesShouldUseDeviceLang() throws Exception {
        String errorCodes = ("V6101,V6068");
        RapidAPI.asycUserMessage(null, errorCodes);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertNotNull(((UserMessageResponse) response).getErrorMessages().get(0));
                assertNotNull(((UserMessageResponse) response).getErrorMessages().get(1));
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });
    }

    @MediumTest
    public void testAyscUserMessagesWithFrenchLocaleCodesShouldSucceed() throws Exception {
        String errorCodes = "V6101";
        RapidAPI.asycUserMessage(Locale.FRENCH.toString(), errorCodes);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertNotNull(((UserMessageResponse)response).getErrorMessages().get(0));

            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });

    }

    @MediumTest
    public void testUserMessagesShouldSucceed() throws Exception {
        String errorCodes = "V6101,V6068";
        RapidAPI.asycUserMessage("en-us", errorCodes);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertNotNull(((UserMessageResponse) response).getErrorMessages().get(0));
                assertNotNull(((UserMessageResponse) response).getErrorMessages().get(1));
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });

    }

    @MediumTest
    public void testGetEwayClientWithNullEndpointShouldReturnCodeS9990Msg() throws Exception {
        RapidAPI.RapidEndpoint = null;
        RapidAPI.PublicAPIKey = PUBLIC_API_KEY;
        RapidAPI.asycEncryptValues(null);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertEquals(((EncryptItemsResponse) response).getErrors(), "S9990");
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });
    }*/

    /*@MediumTest
    public void testGetEwayClientWithInvalidKeyShouldReturnCodeS9991Msg() throws Exception {
        RapidAPI.RapidEndpoint = null;
        RapidAPI.PublicAPIKey = "jasdadsdasadsj";
        RapidAPI.asycEncryptValues(null);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertEquals(((EncryptItemsResponse) response).getErrors(), "S9990");
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });

    }

    @MediumTest
    public void testGetEwayClientWithInvalidAPIKeyShouldReturnCodeS9991Msg() throws Exception {
        RapidAPI.RapidEndpoint = ENDPOINT;
        RapidAPI.PublicAPIKey = null;
        RapidAPI.asycEncryptValues(null);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertEquals(((EncryptItemsResponse) response).getErrors(), "S9991");
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });
    }

    @MediumTest
    public void testGetEwayClientWithInvalidInvalidEndpointShouldReturnCodeS9992Msg() throws Exception {
        RapidAPI.RapidEndpoint = "aaaaaaaaaa";
        RapidAPI.PublicAPIKey = PUBLIC_API_KEY;
        RapidAPI.asycEncryptValues(null);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertEquals(((EncryptItemsResponse) response).getErrors(), "S9990");
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });
    }

    @MediumTest
    public void  testEncryptValueWithUrlWithoutTrailingSlashShouldSuccess() throws Exception {
        RapidAPI.RapidEndpoint = "https://api.sandbox.ewaypayments.com/staging-au";
        RapidAPI.PublicAPIKey = PUBLIC_API_KEY;
        RapidAPI.asycEncryptValues(null);
        RapidAPI.addRapidRecordingTransactionListener(new RapidAPI.RapidRecordingTransactionListener() {
            @Override
            public <T> void onResponseReceivedSuccess(T response) {
                assertEquals(((EncryptItemsResponse) response).getErrors(),null);
            }

            @Override
            public <T> void onResponseReceivedFailure(T error) {

            }
        });
    }*/

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

    @Override
    public void onResponseReceivedSuccess(EncryptItemsResponse response) {

            assertEquals("Card", response.getItems().get(0).getName());
            assertNotNull(response.getItems().get(0).getName());
            assertEquals("CVN", response.getItems().get(1).getName());
            assertNotNull(response.getItems().get(1).getValue());
            assertNull(response.getErrors());



    }

    @Override
    public void onResponseReceivedSuccess(SubmitPayResponse response) {

    }

    @Override
    public void onResponseReceivedError(String errorResponse) {

    }

    @Override
    public void onResponseReceivedFailure(String errorFailure) {

    }
}

