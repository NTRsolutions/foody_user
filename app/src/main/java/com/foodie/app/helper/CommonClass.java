package com.foodie.app.helper;

import com.foodie.app.model.AddCart;
import com.foodie.app.model.Address;
import com.foodie.app.model.AddressList;
import com.foodie.app.model.Cart;
import com.foodie.app.model.Category;
import com.foodie.app.model.Order;
import com.foodie.app.model.User;
import com.foodie.app.model.Otp;
import com.foodie.app.model.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tamil on 9/22/2017.
 */

public class CommonClass {

    public Otp otpModel = null;

    public static double latitude = 13.0587107;
    public static double longitude = 80.2757063;
    public static String addressHeader = "";

    public static String address = "";
    public static User profileModel = null;
    public static Address selectedAddress = null;
    public static Order isSelectedOrder = null;
    public static AddCart addCart = null;

    public static List<Shop> list;
    public static List<Category> categoryList;
    public static List<Cart> cartList;
    public static AddressList addressList=null;

    public static Shop selectedShop;

    public static int otpValue = 0;
    public static String mobile = "";
    public static String currencySymbol = "₹";
    public static int notificationCount = 0;

    public static ArrayList<HashMap<String, String>> foodCart;
    public static String accessToken = "";

    private static final CommonClass ourInstance = new CommonClass();

    public static CommonClass getInstance() {
        return ourInstance;
    }

    private CommonClass() {
    }
}
