package com.foodie.app.utils;

import com.foodie.app.model.OtpModel;
import com.foodie.app.model.ShopsModel;

import java.util.List;

/**
 * Created by Tamil on 9/22/2017.
 */

public class CommonClass {

    public OtpModel otpModel=null;

    public static double latitude=13.0587107;

    public static double longitude=80.2757063;

    public  static List<ShopsModel> list;

    public static int otpValue=0;

    public static String accessToken="";

    private static final CommonClass ourInstance = new CommonClass();

    public static CommonClass getInstance() {
        return ourInstance;
    }

    private CommonClass() {
    }
}
