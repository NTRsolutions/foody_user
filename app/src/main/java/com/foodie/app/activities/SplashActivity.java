package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.helper.ConnectionHelper;
import com.foodie.app.helper.SharedHelper;
import com.foodie.app.models.AddCart;
import com.foodie.app.models.AddressList;
import com.foodie.app.models.User;
import com.foodie.app.utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.foodie.app.helper.GlobalData.addCart;

public class SplashActivity extends AppCompatActivity {

    int retryCount = 0;
    Context context;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    ConnectionHelper connectionHelper;
    String device_token, device_UDID;
    Utils utils = new Utils();
    String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);

        context = SplashActivity.this;
        connectionHelper = new ConnectionHelper(context);
        getDeviceToken();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 3000ms
                if (SharedHelper.getKey(context, "logged").equalsIgnoreCase("true") && SharedHelper.getKey(context, "logged") != null) {
                    GlobalData.getInstance().accessToken = SharedHelper.getKey(context, "access_token");
                    if(connectionHelper.isConnectingToInternet()){
                        getProfile();
                    }else {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    }

                } else {
                    startActivity(new Intent(SplashActivity.this, WelcomeScreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

            }
        }, 3000);
    }

    public void getDeviceToken() {
        try {
            if (!SharedHelper.getKey(context, "device_token").equals("") && SharedHelper.getKey(context, "device_token") != null) {
                device_token = SharedHelper.getKey(context, "device_token");
                utils.print(TAG, "GCM Registration Token: " + device_token);
            } else {
                device_token = "" + FirebaseInstanceId.getInstance().getToken();
                SharedHelper.putKey(context, "device_token", "" + FirebaseInstanceId.getInstance().getToken());
                utils.print(TAG, "Failed to complete token refresh: " + device_token);
            }
        } catch (Exception e) {
            device_token = "COULD NOT GET FCM TOKEN";
            utils.print(TAG, "Failed to complete token refresh");
        }

        try {
            device_UDID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            utils.print(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            utils.print(TAG, "Failed to complete device UDID");
        }
    }

    private void getProfile() {
        retryCount++;

        HashMap<String, String> map = new HashMap<>();
        map.put("device_type", "android");
        map.put("device_id", device_UDID);
        map.put("device_token", device_token);
        Call<User> getprofile = apiInterface.getProfile(map);
        getprofile.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    if (response.code() == 401) {
                        SharedHelper.putKey(context, "logged", "false");
                        startActivity(new Intent(context, LoginActivity.class));
                        finish();
                    }
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().toString());
                        Toast.makeText(context, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    SharedHelper.putKey(context, "logged", "true");
                    GlobalData.getInstance().profileModel = response.body();
                    addCart = new AddCart();
                    addCart.setProductList(response.body().getCart());
                    GlobalData.getInstance().addressList = new AddressList();
                    GlobalData.getInstance().addressList.setAddresses(response.body().getAddresses());
                    if (addCart.getProductList() != null && addCart.getProductList().size() != 0)
                        GlobalData.getInstance().addCartShopId = addCart.getProductList().get(0).getProduct().getShopId();
                    startActivity(new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (retryCount < 5) {
                    getProfile();
                }

            }
        });
    }

    public void displayMessage(String toastString) {
        try {
            Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }catch (Exception e){
            try{
                Toast.makeText(context,""+toastString,Toast.LENGTH_SHORT).show();
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
    }
}