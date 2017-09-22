package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.model.OtpModel;
import com.foodie.app.utils.CommonClass;
import com.foodie.app.utils.TextUtils;

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
    @BindView(R.id.layout_mobile_number)
    TextInputLayout layoutMobileNumber;
    @BindView(R.id.next_btn)
    Button nextBtn;
    @BindView(R.id.connect_with)
    TextView connectWith;
    @BindView(R.id.facebook_login)
    ImageView facebookLogin;
    @BindView(R.id.google_login)
    ImageView googleLogin;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);
        ButterKnife.bind(this);




    }


    @OnClick(R.id.next_btn)
    public void onViewClicked() {

        String mobileNumber=etMobileNumber.getText().toString();
        if(TextUtils.isEmpty(mobileNumber)){

        }else {
           getOtpVerification("+91"+mobileNumber);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public  void getOtpVerification(String mobile){
        Call<OtpModel> call = apiInterface.postOtp(mobile);
        call.enqueue(new Callback<OtpModel>() {
            @Override
            public void onResponse(Call<OtpModel> call, Response<OtpModel> response) {
                Log.i("Otp_response",response.body().toString());
                Toast.makeText(MobileNumberActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                CommonClass.getInstance().otpValue=response.body().getOtp();
                startActivity(new Intent(MobileNumberActivity.this, OtpActivity.class));
            }

            @Override
            public void onFailure(Call<OtpModel> call, Throwable t) {

            }
        });

    }


}
