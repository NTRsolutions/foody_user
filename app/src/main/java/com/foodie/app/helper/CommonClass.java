package com.foodie.app.helper;

import com.foodie.app.model.User;
import com.foodie.app.model.OtpModel;
import com.foodie.app.model.ShopsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tamil on 9/22/2017.
 */

public class CommonClass {

    public OtpModel otpModel = null;

    public static double latitude = 13.0587107;
    public static double longitude = 80.2757063;
    public static String addressHeader = "";

    public static String address = "";
    public static User profileModel = null;

    public static List<ShopsModel> list;

    public static int otpValue = 0;
    public static String mobile = "";

    public static ArrayList<HashMap<String, String>> foodCart;
    public static String accessToken = "";

    private static final CommonClass ourInstance = new CommonClass();

    public static CommonClass getInstance() {
        return ourInstance;
    }

    private CommonClass() {
    }
}
