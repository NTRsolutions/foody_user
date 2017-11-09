package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.helper.SharedHelper;
import com.foodie.app.models.AddCart;
import com.foodie.app.models.AddressList;
import com.foodie.app.models.LoginModel;
import com.foodie.app.models.Otp;
import com.foodie.app.models.RegisterModel;
import com.foodie.app.models.User;
import com.foodie.app.utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.philio.pinentry.PinEntryView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OtpActivity extends AppCompatActivity {


    @BindView(R.id.otp_image)
    ImageView otpImage;
    @BindView(R.id.verification_code_txt)
    TextView verificationCodeTxt;
    @BindView(R.id.veri_txt1)
    TextView veriTxt1;
    @BindView(R.id.veri_txt2)
    TextView veriTxt2;
    @BindView(R.id.rel_verificatin_code)
    RelativeLayout relVerificatinCode;
    @BindView(R.id.otp_value1)
    PinEntryView otpValue1;
    @BindView(R.id.otp_continue)
    Button otpContinue;
    Context context;
    boolean isSignUp = true;
    @BindView(R.id.mobile_number_txt)
    TextView mobileNumberTxt;
    CustomDialog customDialog;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    String device_token, device_UDID;
    Utils utils = new Utils();
    String TAG = "OTPACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        context = OtpActivity.this;
        customDialog = new CustomDialog(context);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            isSignUp = bundle.getBoolean("signup",true);
        }
        mobileNumberTxt.setText(GlobalData.mobile);
        otpValue1.setText(String.valueOf(GlobalData.otpValue));
        getDeviceToken();
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
            device_UDID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            utils.print(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            utils.print(TAG, "Failed to complete device UDID");
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void signup(HashMap<String, String> map) {
        customDialog.show();
        Call<RegisterModel> call = apiInterface.postRegister(map);
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                if (response.body() != null) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("login_by", GlobalData.loginBy);
                    map.put("accessToken", GlobalData.access_token);
                    login(map);
                } else if (response.errorBody() != null) {
                    customDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if(jObjError.has("phone"))
                            Toast.makeText(context, jObjError.optString("phone"), Toast.LENGTH_LONG).show();
                        else if(jObjError.has("email"))
                            Toast.makeText(context, jObjError.optString("email"), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
//                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Toast.makeText(OtpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                customDialog.dismiss();
            }
        });

    }

    private void login(HashMap<String, String> map) {
        Call<LoginModel> call;
        if (GlobalData.loginBy.equals("manual"))
            call = apiInterface.postLogin(map);
        else
            call = apiInterface.postSocialLogin(map);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.body() != null) {
                    SharedHelper.putKey(context, "access_token", response.body().getTokenType() + " " + response.body().getAccessToken());
                    GlobalData.getInstance().accessToken = response.body().getTokenType() + " " + response.body().getAccessToken();
                    //Get Profile data
                    getProfile();

                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

            }
        });
    }

    private void getProfile() {
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
                    GlobalData.getInstance().addCart = new AddCart();
                    GlobalData.getInstance().addCart.setProductList(response.body().getCart());
                    GlobalData.getInstance().addressList = new AddressList();
                    GlobalData.getInstance().addressList.setAddresses(response.body().getAddresses());
                    startActivity(new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }


    public void getOtpVerification(HashMap map) {
        customDialog.show();
        Call<Otp> call = apiInterface.postOtp(map);
        call.enqueue(new Callback<Otp>() {
            @Override
            public void onResponse(@NonNull Call<Otp> call, @NonNull Response<Otp> response) {

                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    customDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    customDialog.dismiss();
                    Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    GlobalData.getInstance().otpValue = response.body().getOtp();
                    otpValue1.setText(String.valueOf(GlobalData.otpValue));
                }

            }

            @Override
            public void onFailure(@NonNull Call<Otp> call, @NonNull Throwable t) {

            }
        });

    }

    @OnClick({R.id.otp_continue, R.id.resend_otp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.otp_continue:
                Log.d("OtpData", otpValue1.getText().toString() + " = " + GlobalData.getInstance().otpValue);
                if (otpValue1.getText().toString().equals("" + GlobalData.getInstance().otpValue)) {
                    if (!GlobalData.loginBy.equals("manual")) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", GlobalData.name);
                        map.put("email", GlobalData.email);
                        map.put("phone", GlobalData.mobile);
                        map.put("login_by", GlobalData.loginBy);
                        map.put("accessToken", GlobalData.access_token);
                        signup(map);
                    } else if (isSignUp) {
                        startActivity(new Intent(OtpActivity.this, SignUpActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
                        finish();
                    } else {
                        startActivity(new Intent(OtpActivity.this, ResetPasswordActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Enter otp is incorrect", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.resend_otp:
                HashMap<String,String> map1 = new HashMap();
                map1.put("phone",GlobalData.mobileNumber);
                getOtpVerification(map1);
                break;
        }
    }
}
