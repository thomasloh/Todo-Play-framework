package com.twilio;

import java.util.HashMap;
import java.util.Map;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestResponse;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Sms;

public class TodoNotification {
    /* Twilio REST API version */
    public static final String ACCOUNTSID = "ACa49f0b4fa8c6452094aeadb2e561ab5c";
    public static final String AUTHTOKEN = "d14e8995d49026301722cab8e141c81b";
    
    // Send sms
    public void send(String body, String phone) {
        /* Instantiate a new Twilio Rest Client */
        TwilioRestClient client = new TwilioRestClient(ACCOUNTSID, AUTHTOKEN);

        // Get the account and call factory class
        Account acct = client.getAccount();
        SmsFactory smsFactory = acct.getSmsFactory();

        //build map of server admins
        Map<String,String> admins = new HashMap<String,String>();
        admins.put(phone, "Thomas");
       
       // Twilio sandbox number
        String fromNumber = "415-599-2671";

    	// Iterate over all our server admins
        for (String toNumber : admins.keySet()) {
            
            //build map of post parameters 
            Map<String,String> params = new HashMap<String,String>();
            params.put("From", fromNumber);
            params.put("To", toNumber);
            params.put("Body", body);

            try {
                // send an sms a call  
                // ( This makes a POST request to the SMS/Messages resource)
                Sms sms = smsFactory.create(params);
                System.out.println("Success sending SMS: " + sms.getSid());
            }
            catch (TwilioRestException e) {
                e.printStackTrace();
            }
        }
    }
           
}
