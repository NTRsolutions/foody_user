package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.helper.SharedHelper;
import com.foodie.app.model.User;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    Context context;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        context = SplashActivity.this;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 3000ms
                if (SharedHelper.getKey(context, "logged").equalsIgnoreCase("true") && SharedHelper.getKey(context, "logged") != null) {
                    CommonClass.getInstance().accessToken = SharedHelper.getKey(context, "access_token");
                    getProfile();
                } else {
                    startActivity(new Intent(SplashActivity.this, WelcomeScreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

            }
        }, 3000);
    }

    private void getProfile() {
        Call<User> getprofile = apiInterface.getProfile();
        getprofile.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                SharedHelper.putKey(context, "logged", "true");
                CommonClass.getInstance().profileModel = response.body();
                startActivity(new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {


            }
        });
    }
}
