package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.foodie.app.helper.CommonClass;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.helper.SharedHelper;
import com.foodie.app.model.GetProfileModel;
import com.foodie.app.model.LoginModel;
import com.foodie.app.utils.TextUtils;

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
    ImageView facebookLogin;
    @BindView(R.id.google_login)
    ImageView googleLogin;

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
    TextView mCountryDialCodeTextView;
    private CountryPicker mCountryPicker;
    String country_code="+91";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        context = LoginActivity.this;


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

    }

    @OnClick({R.id.login_btn, R.id.forgot_password, R.id.donnot_have_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                initValues();
                break;
            case R.id.forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.donnot_have_account:
                startActivity(new Intent(LoginActivity.this, MobileNumberActivity.class));
                break;
        }
    }

    private void setListener() {
        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID) {
                mCountryDialCodeTextView.setText(dialCode);
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
            mCountryDialCodeTextView.setText(country.getDialCode());
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
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();

        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("username", country_code + mobile);
            map.put("password", password);
            map.put("grant_type", GRANT_TYPE);
            map.put("client_id", BuildConfigure.CLIENT_ID);
            map.put("client_secret", BuildConfigure.CLIENT_SECRET);
            login(map);
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
                if (response.body() != null) {
                    CommonClass.getInstance().accessToken = response.body().getTokenType() + " " + response.body().getAccessToken();
                    //Get Profile data
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
        Call<GetProfileModel> getprofile = apiInterface.getProfile();
        getprofile.enqueue(new Callback<GetProfileModel>() {
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                customDialog.dismiss();
                SharedHelper.putKey(context, "logged", "true");
                startActivity(new Intent(LoginActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }

            @Override
            public void onFailure(Call<GetProfileModel> call, Throwable t) {
                customDialog.dismiss();

            }
        });
    }


}
