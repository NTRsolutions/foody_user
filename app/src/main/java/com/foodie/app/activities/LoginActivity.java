package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.app.BuildConfig;
import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.build.configure.BuildConfigure;
import com.foodie.app.model.LoginModel;
import com.foodie.app.model.Restaurant;
import com.foodie.app.utils.TextUtils;

import java.util.HashMap;

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
    @BindView(R.id.layout_mobile_number)
    TextInputLayout layoutMobileNumber;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.layout_password)
    TextInputLayout layoutPassword;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.donnot_have_account)
    TextView donnotHaveAccount;
    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.connect_with)
    TextView connectWith;
    @BindView(R.id.facebook_login)
    ImageView facebookLogin;
    @BindView(R.id.google_login)
    ImageView googleLogin;

    String mobile, password;
    String GRANT_TYPE = "password";
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.login_btn, R.id.sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                initValues();
                break;
            case R.id.sign_up:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
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
            layoutMobileNumber.setError("Please enter your mail");
        } else if (TextUtils.isEmpty(password)) {
            layoutPassword.setError("Please enter your mail");
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("username", "+91" + mobile);
            map.put("password", password);
            map.put("grant_type", GRANT_TYPE);
            map.put("client_id", BuildConfigure.CLIENT_ID);
            map.put("client_secret", BuildConfigure.CLIENT_SECRET);

            login(map);
        }

    }

    public void login(HashMap<String, String> map) {
        Call<LoginModel> call = apiInterface.postLogin(map);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.body() != null) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

            }
        });

    }


}
