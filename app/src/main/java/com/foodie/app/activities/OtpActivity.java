package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.foodie.app.R;

import me.philio.pinentry.PinEntryView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener{

    private PinEntryView pinEntryView;
    Button otp_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        pinEntryView = (PinEntryView) findViewById(R.id.otp_value1);
        otp_continue = (Button)findViewById(R.id.otp_continue);
        otp_continue.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.otp_continue:
                startActivity(new Intent(OtpActivity.this, SignUpActivity.class));
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
