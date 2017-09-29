package com.foodie.app.build.api;

/**
 * Created by tamil@appoets.com on 30-08-2017.
 */

import com.foodie.app.model.AddCart;
import com.foodie.app.model.ForgotPassword;
import com.foodie.app.model.ResetPassword;
import com.foodie.app.model.User;
import com.foodie.app.model.LoginModel;
import com.foodie.app.model.OtpModel;
import com.foodie.app.model.RegisterModel;
import com.foodie.app.model.ShopsModel;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("api/user/profile")
    Call<User> getProfile();



    @FormUrlEncoded
    @POST("api/user/otp")
    Call<OtpModel> postOtp(@Field("phone") String mobile);

    @FormUrlEncoded
    @POST("api/user/register")
    Call<RegisterModel> postRegister(@FieldMap HashMap<String,String> params);

    @FormUrlEncoded
    @POST("oauth/token")
    Call<LoginModel> postLogin(@FieldMap HashMap<String,String> params);

    @FormUrlEncoded
    @POST("api/user/forgot/password")
    Call<ForgotPassword> forgotPassword(@Field("phone") String mobile);

    @FormUrlEncoded
    @POST("api/user/reset/password")
    Call<ResetPassword> resetPassword(@FieldMap HashMap<String,String> params);


    @GET("api/user/shops")
    Call<List<ShopsModel>> getshops(@Query("latitude") double lat, @Query("longitude") double lng);

    @FormUrlEncoded
    @POST("api/user/cart")
    Call<AddCart> postAddCart(@FieldMap HashMap<String,String> params);

    @GET("api/user/cart")
    Call<AddCart> getViewCart();

}
