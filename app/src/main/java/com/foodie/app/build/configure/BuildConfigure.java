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
//    public static String BASE_URL = "http://142.93.250.254/";
    public static String BASE_URL = "https://ditchthekitch.ca/";
    public static String CLIENT_SECRET = "3G3sWLWNZrlSP8b64itBIv116XAFcUmH83dNPY70";
    public static String CLIENT_ID = "2";

    //Pubnub for Chat
    public static  String PUBNUB_PUBLISH_KEY = "pub-c-782d6b72-7439-4076-8da3-23e51b1e1b35";
    public static  String PUBNUB_SUBSCRIBE_KEY = "sub-c-af85e7c8-9a45-11e8-9a7c-62794ce13da1";
    public static   String PUBNUB_CHANNEL_NAME = "21";

    //Stripe for card payment
    public static String STRIPE_PK = "pk_test_39kly6aEfUEfvMpRnN6BnxLb";

}
