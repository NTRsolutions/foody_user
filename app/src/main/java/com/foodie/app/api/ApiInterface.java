package com.foodie.app.api;

/**
 * Created by santhosh@appoets.com on 30-08-2017.
 */

import com.foodie.app.model.Restaurant;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {


    @GET("profile")
    Call<Restaurant> getProfile();

    @POST("/signup")
    Call<Restaurant> getSignup(@FieldMap HashMap<String,String> params);


}
