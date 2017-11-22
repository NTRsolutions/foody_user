package com.foodie.app.build.configure;

/**
 * Created by Tamil on 9/22/2017.
 */

public class BuildConfigure {

    /*  Live Mode*/
//    public static String BASE_URL = "http://foodie.appoets.co/";
//    public static String CLIENT_SECRET = "D05WfB9aCBPCel6St5lOl2Cc1hqBwYoudmqxX7Ti";
//    public static final String PUBNUB_PUBLISH_KEY = "pub-c-7bce9b6e-c9ec-44ad-98c2-35eafbbcacb1";
//    public static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-046e0baa-bb01-11e7-b0ca-ee8767eb9c7d";

    /*   Dev Mode*/
    public static String BASE_URL = "http://foodie.venturedemos.com/";
    public static String CLIENT_SECRET = "Xbe0mfn3DZslxO2oReCxvDZEhqHzaBbfHKEywClW";
    public static String CLIENT_ID = "2";

    //Pubnub for Chat
    public static  String PUBNUB_PUBLISH_KEY = "pub-c-2e80d162-68ec-4005-b6ca-5b416c9b2b8a";
    public static  String PUBNUB_SUBSCRIBE_KEY = "sub-c-68fb6f54-bb01-11e7-bcea-b64ad28a6f98";
    public static   String PUBNUB_CHANNEL_NAME = "21";

    //Stripe for card payment
    public static String STRIPE_PK = "pk_test_39kly6aEfUEfvMpRnN6BnxLb";


}
