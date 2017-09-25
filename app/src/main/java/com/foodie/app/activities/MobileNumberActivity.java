package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.CountryPicker.Country;
import com.foodie.app.CountryPicker.CountryPicker;
import com.foodie.app.CountryPicker.CountryPickerListener;
import com.foodie.app.R;
import com.foodie.app.build.api.APIError;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.build.api.ErrorUtils;
import com.foodie.app.model.OtpModel;
import com.foodie.app.utils.CommonClass;
import com.foodie.app.utils.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MobileNumberActivity extends AppCompatActivity {


    @BindView(R.id.app_logo)
    ImageView appLogo;
    @BindView(R.id.et_mobile_number)
    EditText etMobileNumber;
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.connect_with)
    TextView connectWith;
    @BindView(R.id.facebook_login)
    ImageView facebookLogin;
    @BindView(R.id.google_login)
    ImageView googleLogin;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    @BindView(R.id.countryImage)
    ImageView mCountryFlagImageView;
    @BindView(R.id.countryNumber)
    TextView mCountryDialCodeTextView;
    private CountryPicker mCountryPicker;
    String country_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);
        ButterKnife.bind(this);
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


    @OnClick(R.id.next_btn)
    public void onViewClicked() {

        String mobileNumber = etMobileNumber.getText().toString();
        if (TextUtils.isEmpty(mobileNumber)) {

        } else {
            getOtpVerification("+91" + mobileNumber);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void getOtpVerification(String mobile) {
        Call<OtpModel> call = apiInterface.postOtp(mobile);
        call.enqueue(new Callback<OtpModel>() {
            @Override
            public void onResponse(Call<OtpModel> call, Response<OtpModel> response) {
                Log.i("Otp_response", response.body().toString());
                if (response.isSuccessful()) {
                    // use response data and do some fancy stuff :)
                    Toast.makeText(MobileNumberActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    CommonClass.getInstance().otpValue = response.body().getOtp();
                    startActivity(new Intent(MobileNumberActivity.this, OtpActivity.class));
                } else {
                    // parse the response body …
                    APIError error = ErrorUtils.parseError(response);
                    // … and use it to show error information
                    // … or just log the issue like we’re doing :)
                    Log.d("error message", error.message());
                }


            }

            @Override
            public void onFailure(Call<OtpModel> call, Throwable t) {

            }
        });

    }

    private void setListener() {
        mCountryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode,
                                        int flagDrawableResID) {
                mCountryDialCodeTextView.setText(dialCode);
                mCountryFlagImageView.setImageResource(flagDrawableResID);
                mCountryPicker.dismiss();
            }
        });
        mCountryFlagImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
        getUserCountryInfo();
    }

    private void getUserCountryInfo() {
        Locale current = getResources().getConfiguration().locale;
        Country country = Country.getCountryFromSIM(MobileNumberActivity.this);
        if (country != null) {
            mCountryFlagImageView.setImageResource(country.getFlag());
            mCountryDialCodeTextView.setText(country.getDialCode());
            country_code = country.getDialCode();
        } else {
            Toast.makeText(MobileNumberActivity.this, "Required Sim", Toast.LENGTH_SHORT).show();
        }
    }


}