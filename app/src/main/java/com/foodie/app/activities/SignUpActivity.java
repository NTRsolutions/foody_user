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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.build.configure.BuildConfigure;
import com.foodie.app.helper.SharedHelper;
import com.foodie.app.model.GetProfileModel;
import com.foodie.app.model.LoginModel;
import com.foodie.app.model.RegisterModel;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.utils.TextUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {

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
    @BindView(R.id.sigin_layout)
    LinearLayout siginLayout;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    String name, email, password, strConfirmPassword;
    String GRANT_TYPE = "password";
    Context context;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        context = SignUpActivity.this;

    }

    @OnClick({R.id.terms_and_conditions, R.id.sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.terms_and_conditions:
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
            case R.id.sign_up:
                initValues();

                break;
        }
    }


    public void signup(HashMap<String, String> map) {
        Call<RegisterModel> call = apiInterface.postRegister(map);
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                if (response.body() != null) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("username", CommonClass.getInstance().mobile);
                    map.put("password", password);
                    map.put("grant_type", GRANT_TYPE);
                    map.put("client_id", BuildConfigure.CLIENT_ID);
                    map.put("client_secret", BuildConfigure.CLIENT_SECRET);

                    login(map);
                } else if (response.errorBody() != null) {


                }

            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {

            }
        });

    }

    private void login(HashMap<String, String> map) {
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

            }
        });
    }


    public void initValues() {
        name = nameEdit.getText().toString();
        strConfirmPassword = confirmPassword.getText().toString();
        email = emailEdit.getText().toString();
        password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your mail", Toast.LENGTH_SHORT).show();
        } else if (!TextUtils.isValidEmail(email)) {
            Toast.makeText(this, "Please enter valid mail", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(strConfirmPassword)) {
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
        } else if (!strConfirmPassword.equalsIgnoreCase(password)) {
            Toast.makeText(this, "Password and confirm password doesn't match", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", name);
            map.put("email", email);
            map.put("phone", CommonClass.getInstance().mobile);
            map.put("password", password);
            map.put("password_confirmation", strConfirmPassword);
            signup(map);
        }

    }

    private void getProfile() {
        Call<GetProfileModel> getprofile = apiInterface.getProfile();
        getprofile.enqueue(new Callback<GetProfileModel>() {
            @Override
            public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                SharedHelper.putKey(context, "logged", "true");
                startActivity(new Intent(SignUpActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }

            @Override
            public void onFailure(Call<GetProfileModel> call, Throwable t) {

            }
        });
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

