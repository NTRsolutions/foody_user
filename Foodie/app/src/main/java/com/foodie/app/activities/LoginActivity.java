package com.foodie.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.foodie.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.mobile_edittext)
    EditText mobileEdit;
    @BindView(R.id.password_edittext)
    EditText passwordEdit;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.forgotTxt)
    TextView forgotTxt;
    @BindView(R.id.signUpTxt)
    TextView signUpTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }
}
