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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.api.ApiClient;
import com.foodie.app.api.ApiInterface;
import com.foodie.app.model.Restaurant;
import com.foodie.app.utils.TextUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.mobile)
    EditText mobileEdit;
    @BindView(R.id.email)
    EditText emailEdit;
    @BindView(R.id.name)
    EditText nameEdit;
    @BindView(R.id.password)
    EditText passwordEdit;
    @BindView(R.id.terms_and_conditions)
    TextView termsAndConditionsTxt;
    @BindView(R.id.sign_up)
    Button signUpBtn;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    @BindView(R.id.app_logo)
    ImageView appLogo;
    @BindView(R.id.layout_mobile_number)
    TextInputLayout layoutMobileNumber;
    @BindView(R.id.layout_email)
    TextInputLayout layoutEmail;
    @BindView(R.id.layout_name)
    TextInputLayout layoutName;
    @BindView(R.id.layout_password)
    TextInputLayout layoutPassword;
    @BindView(R.id.sigin_layout)
    LinearLayout siginLayout;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    String name,email,mobile,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

//        initValues();





    }

    @OnClick({R.id.terms_and_conditions, R.id.sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.terms_and_conditions:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
            case R.id.sign_up:
                startActivity(new Intent(SignUpActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
        }
    }


    public  void signup(HashMap<String,String> map){
        Call<Restaurant> call = apiInterface.getSignup(map);
        call.enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {

            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {

            }
        });

    }
    public  void  initValues(){
        name=nameEdit.getText().toString();
        mobile=mobileEdit.getText().toString();
        email=emailEdit.getText().toString();
        password=passwordEdit.getText().toString();
        if (TextUtils.isEmpty(name)) {
            layoutName.setError("Please enter your name");
        }
        else if(TextUtils.isEmpty(email)){
            layoutEmail.setError("Please enter your mail");
        }
        else if(TextUtils.isValidEmail(email)){
            layoutEmail.setError("Please enter valid mail address");

        }
        else if(TextUtils.isEmpty(mobile)){
            layoutMobileNumber.setError("Please enter your mail");
        }
        else if(TextUtils.isEmpty(password)){
            layoutPassword.setError("Please enter your mail");
        }
        else {
            HashMap<String,String> map= new HashMap<>();
            map.put("name",name);
            map.put("email",email);
            map.put("mobile",mobile);
            map.put("password",password);
            signup(map);
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
}

