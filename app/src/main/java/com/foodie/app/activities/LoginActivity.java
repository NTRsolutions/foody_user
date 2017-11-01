package com.foodie.app.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.foodie.app.helper.GlobalData;
import com.foodie.app.helper.ConnectionHelper;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.helper.SharedHelper;
import com.foodie.app.models.AddCart;
import com.foodie.app.models.AddressList;
import com.foodie.app.models.LoginModel;
import com.foodie.app.models.User;
import com.foodie.app.utils.TextUtils;
import com.foodie.app.utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

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

import static com.foodie.app.helper.GlobalData.latitude;
import static com.foodie.app.helper.GlobalData.longitude;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.app_logo)
    ImageView appLogo;
    @BindView(R.id.ed_mobile_number)
    EditText edMobileNumber;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.donnot_have_account)
    TextView donnotHaveAccount;
    @BindView(R.id.connect_with)
    TextView connectWith;
    @BindView(R.id.facebook_login)
    ImageButton facebookLogin;
    @BindView(R.id.google_login)
    ImageButton googleLogin;
    String mobile, password;
    String GRANT_TYPE = "password";
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    Context context;
    CustomDialog customDialog;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.countryImage)
    ImageView countryImage;
    @BindView(R.id.countryNumber)
    TextView countryNumber;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.eye_img)
    ImageView eyeImg;
    private CountryPicker mCountryPicker;
    String country_code = "+91";
    ConnectionHelper connectionHelper;
    Activity activity;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 0;

    String device_token, device_UDID;
    Utils utils = new Utils();
    String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = LoginActivity.this;
        activity = LoginActivity.this;
        connectionHelper = new ConnectionHelper(context);
        getDeviceToken();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
            }
        }

        mCountryPicker = CountryPicker.newInstance("Select Country");
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
        final boolean once = false;
        eyeImg.setTag(1);


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

    @OnClick({R.id.login_btn, R.id.forgot_password, R.id.donnot_have_account, R.id.back_img, R.id.eye_img, R.id.facebook_login, R.id.google_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                initValues();
                break;
            case R.id.forgot_password:
                startActivity(new Intent(LoginActivity.this, MobileNumberActivity.class).putExtra("signup", false));
                break;
            case R.id.donnot_have_account:
                startActivity(new Intent(LoginActivity.this, MobileNumberActivity.class));
                break;
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.facebook_login:
                Snackbar.make(this.findViewById(android.R.id.content), getResources().getString(R.string.coming_soon), Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.google_login:
                Snackbar.make(this.findViewById(android.R.id.content), getResources().getString(R.string.coming_soon), Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.eye_img:
                if (eyeImg.getTag().equals(1)) {
                    edPassword.setTransformationMethod(null);
                    eyeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_close));
                    eyeImg.setTag(0);
                } else {
                    eyeImg.setTag(1);
                    edPassword.setTransformationMethod(new PasswordTransformationMethod());
                    eyeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_open));
                }
                break;

        }
    }

    private void setListener() {
        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID) {
                countryNumber.setText(dialCode);
                country_code = dialCode;
                countryImage.setImageResource(flagDrawableResID);
                mCountryPicker.dismiss();
            }
        });

        countryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

        countryNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

        getUserCountryInfo();
    }

    private void getUserCountryInfo() {
        Locale current = getResources().getConfiguration().locale;
        Country country = Country.getCountryFromSIM(LoginActivity.this);
        if (country != null) {
            countryImage.setImageResource(country.getFlag());
            countryNumber.setText(country.getDialCode());
            country_code = country.getDialCode();
        } else {
            Toast.makeText(LoginActivity.this, "Required Sim", Toast.LENGTH_SHORT).show();
        }
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


    public void initValues() {
        mobile = edMobileNumber.getText().toString();
        password = edPassword.getText().toString();
        if (!isValidMobile(country_code + mobile)) {
            Toast.makeText(this, "Please enter valid mobile number 11", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();

        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("username", country_code + mobile);
            map.put("password", password);
            map.put("grant_type", GRANT_TYPE);
            map.put("client_id", BuildConfigure.CLIENT_ID);
            map.put("client_secret", BuildConfigure.CLIENT_SECRET);

            if (connectionHelper.isConnectingToInternet()) {
                login(map);
            } else {
                Utils.displayMessage(activity, context, getString(R.string.oops_connect_your_internet));
            }

        }
    }

    private boolean isValidMobile(String phone) {
        if (phone == null || phone.length() < 6 || phone.length() > 13) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }

    public void login(HashMap<String, String> map) {
        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        Call<LoginModel> call = apiInterface.postLogin(map);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    customDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    //Get Profile data
                    SharedHelper.putKey(context, "access_token", response.body().getTokenType() + " " + response.body().getAccessToken());
                    GlobalData.getInstance().accessToken = response.body().getTokenType() + " " + response.body().getAccessToken();
                    getProfile();
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                customDialog.dismiss();
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
                customDialog.dismiss();
                SharedHelper.putKey(context, "logged", "true");
                GlobalData.getInstance().profileModel = response.body();
                GlobalData.getInstance().addCart = new AddCart();
                GlobalData.getInstance().addCart.setProductList(response.body().getCart());
                GlobalData.getInstance().addressList = new AddressList();
                GlobalData.getInstance().addressList.setAddresses(response.body().getAddresses());
                startActivity(new Intent(LoginActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                customDialog.dismiss();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean FINE_LOCATIONPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean COARSE_LOCATIONPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (FINE_LOCATIONPermission && COARSE_LOCATIONPermission) {


                    } else {
                        Snackbar.make(this.findViewById(android.R.id.content),
                                "Please Grant Permissions to start service",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }

}
