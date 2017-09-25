package com.foodie.app.build.api;

/**
 * Created by tamil@appoets.com on 30-08-2017.
 */

import com.foodie.app.model.GetProfileModel;
import com.foodie.app.model.LoginModel;
import com.foodie.app.model.OtpModel;
import com.foodie.app.model.RegisterModel;
import com.foodie.app.model.Restaurant;
import com.foodie.app.utils.CommonClass;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {


    @GET("/api/user/profile")
    Call<GetProfileModel> getProfile();

    @FormUrlEncoded
    @POST("/api/user/otp")
    Call<OtpModel> postOtp(@Field("phone") String mobile);

    @FormUrlEncoded
    @POST("/api/user/register")
    Call<RegisterModel> postRegister(@FieldMap HashMap<String,String> params);

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<LoginModel> postLogin(@FieldMap HashMap<String,String> params);


}
