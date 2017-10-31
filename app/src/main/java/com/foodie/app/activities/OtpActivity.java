package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.models.Otp;

import org.json.JSONObject;

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
            isSignUp = bundle.getBoolean("signup");
        }
        mobileNumberTxt.setText(GlobalData.mobile);
        otpValue1.setText(String.valueOf(GlobalData.otpValue));
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void getOtpVerification(String mobile) {
        customDialog.show();
        Call<Otp> call = apiInterface.postOtp(mobile);
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
            public void onFailure(@NonNull Call<Otp> call,@NonNull Throwable t) {

            }
        });

    }

    @OnClick({R.id.otp_continue, R.id.resend_otp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.otp_continue:
                Log.d("OtpData", otpValue1.getText().toString() + " = " + GlobalData.getInstance().otpValue);
                if (otpValue1.getText().toString().equals("" + GlobalData.getInstance().otpValue)) {
                    if (isSignUp) {
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
                getOtpVerification(GlobalData.mobile);
                break;
        }
    }
}
