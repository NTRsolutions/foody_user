package com.foodie.app.utils;

import com.foodie.app.model.OtpModel;

/**
 * Created by Tamil on 9/22/2017.
 */

public class CommonClass {

    public OtpModel otpModel=null;

    public static int otpValue=0;

    public static String accessToken="";

    private static final CommonClass ourInstance = new CommonClass();

    public static CommonClass getInstance() {
        return ourInstance;
    }

    private CommonClass() {
    }
}
