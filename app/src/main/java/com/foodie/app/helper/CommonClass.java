package com.foodie.app.helper;

import android.location.Location;

import com.foodie.app.model.AddCart;
import com.foodie.app.model.Address;
import com.foodie.app.model.AddressList;
import com.foodie.app.model.Cart;
import com.foodie.app.model.Category;
import com.foodie.app.model.Cuisine;
import com.foodie.app.model.Order;
import com.foodie.app.model.User;
import com.foodie.app.model.Otp;
import com.foodie.app.model.Shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tamil on 9/22/2017.
 */

public class CommonClass {

    public Otp otpModel = null;

    public static double latitude ;
    public static double longitude ;
    public static String addressHeader = "";
    public static Location CURRENT_LOCATION = null;

    public static String address = "";
    public static int addCartShopId =0;
    public static User profileModel = null;
    public static Address selectedAddress = null;
    public static Order isSelectedOrder = null;
    public static AddCart addCart = null;

    public static List<Shop> shopList;
    public static List<Cuisine> cuisineList;
    public static List<Category> categoryList=null;
    public static List<Order> onGoingOrderList;
    public static List<Order> pastOrderList;
    public static AddressList addressList=null;
    public static List<String> ORDER_STATUS = Arrays.asList("ORDERED", "RECEIVED", "ASSIGNED", "PROCESSING", "REACHED", "PICKEDUP", "ARRIVED", "COMPLETED");

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
