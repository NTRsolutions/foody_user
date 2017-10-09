package com.foodie.app.build.api;

/**
 * Created by tamil@appoets.com on 30-08-2017.
 */

import com.foodie.app.model.AddCart;
import com.foodie.app.model.Address;
import com.foodie.app.model.Category;
import com.foodie.app.model.ChangePassword;
import com.foodie.app.model.ForgotPassword;
import com.foodie.app.model.Message;
import com.foodie.app.model.Order;
import com.foodie.app.model.ResetPassword;
import com.foodie.app.model.User;
import com.foodie.app.model.LoginModel;
import com.foodie.app.model.Otp;
import com.foodie.app.model.RegisterModel;
import com.foodie.app.model.Shop;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiInterface {



    /*-------------USER--------------------*/

    @GET("api/user/profile")
    Call<User> getProfile();

    @FormUrlEncoded
    @POST("api/user/otp")
    Call<Otp> postOtp(@Field("phone") String mobile);

    @FormUrlEncoded
    @POST("api/user/register")
    Call<RegisterModel> postRegister(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("oauth/token")
    Call<LoginModel> postLogin(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/user/forgot/password")
    Call<ForgotPassword> forgotPassword(@Field("phone") String mobile);

    @FormUrlEncoded
    @POST("api/user/reset/password")
    Call<ResetPassword> resetPassword(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("api/user/profile/password")
    Call<ChangePassword> changePassword(@FieldMap HashMap<String, String> params);

    /*-------------SHOP--------------------*/

    @GET("api/user/shops")
    Call<List<Shop>> getshops(@QueryMap HashMap<String, String> params);

    @GET("api/user/categories")
    Call<List<Category>> getCategories(@QueryMap HashMap<String, String> params);

    /*-------------CART--------------------*/

    @FormUrlEncoded
    @POST("api/user/cart")
    Call<AddCart> postAddCart(@FieldMap HashMap<String, String> params);

    @GET("api/user/cart")
    Call<AddCart> getViewCart();

    @FormUrlEncoded
    @POST("api/user/order")
    Call<Order> postCheckout(@Field("user_address_id") Integer id);

    /*-------------ADDRESS--------------------*/

    @GET("api/user/address")
    Call<List<Address>> getAddresses();

    @POST("api/user/address")
    Call<Address> saveAddress(@Body Address address);

    @PATCH("api/user/address/{id}")
    Call<Address> updateAddress(@Path("id") int id, @Body Address address);

    @DELETE("api/user/address/{id}")
    Call<Message> deleteAddress(@Path("id") int id);


    /*-------------ORDER--------------------*/

    @GET("api/user/ongoing/order")
    Call<List<Order>> getOngoingOrders();

    @GET("api/user/order")
    Call<List<Order>> getPastOders();


}
