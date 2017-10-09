package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.helper.CommonClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.philio.pinentry.PinEntryView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        context = OtpActivity.this;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            isSignUp = bundle.getBoolean("signup");
        }
        mobileNumberTxt.setText(CommonClass.getInstance().mobile+"\n"+"OTP : "+CommonClass.getInstance().otpValue);

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.otp_continue)
    public void onViewClicked() {
        Log.d("OtpData", otpValue1.getText().toString() + " = " + CommonClass.getInstance().otpValue);
        if (otpValue1.getText().toString().equals("" + CommonClass.getInstance().otpValue)) {
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

    }
}
