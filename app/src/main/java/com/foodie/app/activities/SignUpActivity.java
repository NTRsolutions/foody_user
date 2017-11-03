package com.foodie.app.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.CountryPicker.Country;
import com.foodie.app.CountryPicker.CountryPicker;
import com.foodie.app.CountryPicker.CountryPickerListener;
import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.build.configure.BuildConfigure;
import com.foodie.app.helper.ConnectionHelper;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.helper.SharedHelper;
import com.foodie.app.models.AddCart;
import com.foodie.app.models.AddressList;
import com.foodie.app.models.LoginModel;
import com.foodie.app.models.Otp;
import com.foodie.app.models.RegisterModel;
import com.foodie.app.models.User;
import com.foodie.app.utils.TextUtils;
import com.foodie.app.utils.Utils;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.email)
    EditText emailEdit;
    @BindView(R.id.name)
    EditText nameEdit;
    @BindView(R.id.password)
    EditText passwordEdit;
    @BindView(R.id.sign_up)
    Button signUpBtn;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    @BindView(R.id.app_logo)
    ImageView appLogo;

    String name, email, password, strConfirmPassword;
    String GRANT_TYPE = "password";
    Context context;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;
    CustomDialog customDialog;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.password_eye_img)
    ImageView passwordEyeImg;
    @BindView(R.id.confirm_password_eye_img)
    ImageView confirmPasswordEyeImg;
    ConnectionHelper connectionHelper;
    Activity activity;

    String device_token, device_UDID;
    Utils utils = new Utils();
    String TAG = "Login";
    @BindView(R.id.countryImage)
    ImageView countryImage;
    @BindView(R.id.countryNumber)
    TextView countryNumber;
    @BindView(R.id.et_mobile_number)
    EditText etMobileNumber;
    @BindView(R.id.mobile_number_layout)
    RelativeLayout mobileNumberLayout;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.password_layout)
    RelativeLayout passwordLayout;
    @BindView(R.id.confirm_password_layout)
    RelativeLayout confirmPasswordLayout;
    private CountryPicker mCountryPicker;
    String country_code = "+91";
    private static final int REQUEST_LOCATION = 1450;
    GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        context = SignUpActivity.this;
        activity = SignUpActivity.this;
        connectionHelper = new ConnectionHelper(context);
        customDialog = new CustomDialog(context);
        mCountryPicker = CountryPicker.newInstance("Select Country");
        passwordEyeImg.setTag(1);
        confirmPasswordEyeImg.setTag(1);
        if (!GlobalData.loginBy.equals("manual")) {
            confirmPasswordLayout.setVisibility(View.GONE);
            passwordLayout.setVisibility(View.GONE);
            mobileNumberLayout.setVisibility(View.VISIBLE);
            nameEdit.setText(GlobalData.name);
            emailEdit.setText(GlobalData.email);
        } else {
            confirmPasswordLayout.setVisibility(View.VISIBLE);
            passwordLayout.setVisibility(View.VISIBLE);
            mobileNumberLayout.setVisibility(View.GONE);
        }

        // You can limit the displayed countries
        ArrayList<Country> nc = new ArrayList<>();
        for (Country c : Country.getAllCountries()) {
//            if (c.getDialCode().endsWith("0")) {
            nc.add(c);
//            }
        }
        // and decide, in which order they will be displayed
        Collections.reverse(nc);
        mCountryPicker.setCountriesList(nc);
        setListener();
        //Social login logout
        signOut();
        LoginManager.getInstance().logOut();


           /*----------------Face Integration---------------*/
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    "com.foodie.app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
        getDeviceToken();

    }

    private void setListener() {
        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID) {
                countryNumber.setText(dialCode);
                countryImage.setImageResource(flagDrawableResID);
                mCountryPicker.dismiss();
            }
        });
        countryNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
        countryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
        getUserCountryInfo();
    }
    private void getUserCountryInfo() {
        Locale current = getResources().getConfiguration().locale;
        Country country = Country.getCountryFromSIM(context);
        if (country != null) {
            countryImage.setImageResource(country.getFlag());
            countryNumber.setText(country.getDialCode());
            country_code = country.getDialCode();
        } else {
            Toast.makeText(context, "Required Sim", Toast.LENGTH_SHORT).show();
        }
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

    @OnClick({R.id.sign_up, R.id.back_img, R.id.password_eye_img, R.id.confirm_password_eye_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_up:
                initValues();
                break;
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.password_eye_img:
                if (passwordEyeImg.getTag().equals(1)) {
                    passwordEdit.setTransformationMethod(null);
                    passwordEyeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_close));
                    passwordEyeImg.setTag(0);
                } else {
                    passwordEyeImg.setTag(1);
                    passwordEdit.setTransformationMethod(new PasswordTransformationMethod());
                    passwordEyeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_open));
                }
                break;
            case R.id.confirm_password_eye_img:
                if (confirmPasswordEyeImg.getTag().equals(1)) {
                    confirmPassword.setTransformationMethod(null);
                    confirmPasswordEyeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_close));
                    confirmPasswordEyeImg.setTag(0);
                } else {
                    confirmPasswordEyeImg.setTag(1);
                    confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    confirmPasswordEyeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_open));
                }
                break;
        }
    }


    public void signup(HashMap<String, String> map) {
        customDialog.show();
        Call<RegisterModel> call = apiInterface.postRegister(map);
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                if (response.body() != null) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("username", GlobalData.getInstance().mobile);
                    map.put("password", password);
                    map.put("grant_type", GRANT_TYPE);
                    map.put("client_id", BuildConfigure.CLIENT_ID);
                    map.put("client_secret", BuildConfigure.CLIENT_SECRET);
                    login(map);
                } else if (response.errorBody() != null) {
                    customDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("phone"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {

            }
        });

    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //taken from google api console (Web api client id)
//                .requestIdToken("795253286119-p5b084skjnl7sll3s24ha310iotin5k4.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

//                FirebaseAuth.getInstance().signOut();
                if(mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.d("MainAct", "Google User Logged out");
                               /* Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();*/
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("MAin", "Google API Client Connection Suspended");
            }
        });
    }

    private void login(HashMap<String, String> map) {
        Call<LoginModel> call = apiInterface.postLogin(map);
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


    public void initValues() {
        name = nameEdit.getText().toString();
        email = emailEdit.getText().toString();
        strConfirmPassword = confirmPassword.getText().toString();
        GlobalData.mobile = country_code + etMobileNumber.getText().toString();
        password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your mail", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isValidEmail(email)) {
            Toast.makeText(this, "Please enter valid mail", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty( GlobalData.mobile)&&!GlobalData.loginBy.equals("manual")) {
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)&&GlobalData.loginBy.equals("manual")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(strConfirmPassword)&&GlobalData.loginBy.equals("manual")) {
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
        } else if (!strConfirmPassword.equalsIgnoreCase(password)&&GlobalData.loginBy.equals("manual")) {
            Toast.makeText(this, "Password and confirm password doesn't match", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", name);
            map.put("email", email);
            map.put("phone", GlobalData.getInstance().mobile);
            map.put("password", password);
            map.put("password_confirmation", strConfirmPassword);
            if (connectionHelper.isConnectingToInternet()) {
                if(GlobalData.loginBy.equals("manual")){
                    signup(map);
                }else {
                    HashMap<String,String> map1=new HashMap<>();
                    map1.put("phone", GlobalData.mobile);
                    map1.put("login_by",GlobalData.loginBy);
                    map1.put("accessToken",GlobalData.access_token);
                    getOtpVerification(map1);
                }

            } else {
                Utils.displayMessage(activity, context, getString(R.string.oops_connect_your_internet));
            }
        }
    }

    public void getOtpVerification(HashMap<String, String> map) {
        customDialog.show();
        Call<Otp> call = apiInterface.postOtp(map);
        call.enqueue(new Callback<Otp>() {
            @Override
            public void onResponse(@NonNull Call<Otp> call, @NonNull  Response<Otp> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    customDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        if(jObjError.optString("error")!=null)
                        Toast.makeText(context, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                        else if(jObjError.optString("phone")!=null)
                            Toast.makeText(context, jObjError.optString("phone"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    customDialog.dismiss();
                    Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    GlobalData.getInstance().otpValue = response.body().getOtp();
                    startActivity(new Intent(context, OtpActivity.class));
                    finish();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Otp> call,@NonNull Throwable t) {
                customDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }
}

