package com.foodie.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.build.configure.BuildConfigure;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.helper.ConnectionHelper;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.helper.SharedHelper;
import com.foodie.app.model.AddCart;
import com.foodie.app.model.AddressList;
import com.foodie.app.model.Cart;
import com.foodie.app.model.LoginModel;
import com.foodie.app.model.RegisterModel;
import com.foodie.app.model.User;
import com.foodie.app.utils.TextUtils;
import com.foodie.app.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
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
    @BindView(R.id.sign_up)
    Button signUpBtn;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    @BindView(R.id.app_logo)
    ImageView appLogo;

    String name, email, password, strConfirmPassword;
    String GRANT_TYPE = "password";
    Context context;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;
    CustomDialog customDialog;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.password_eye_img)
    ImageView passwordEyeImg;
    @BindView(R.id.confirm_password_eye_img)
    ImageView confirmPasswordEyeImg;
    ConnectionHelper connectionHelper;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        context = SignUpActivity.this;
        activity = SignUpActivity.this;
        connectionHelper= new ConnectionHelper(context);
        customDialog = new CustomDialog(context);
        passwordEyeImg.setTag(1);
        confirmPasswordEyeImg.setTag(1);

    }

    @OnClick({R.id.sign_up, R.id.back_img, R.id.password_eye_img, R.id.confirm_password_eye_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_up:
                initValues();
                break;
            case R.id.back_img:
                onBackPressed();
                break;
            case R.id.password_eye_img:
                if (passwordEyeImg.getTag().equals(1)) {
                    passwordEdit.setTransformationMethod(null);
                    passwordEyeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_close));
                    passwordEyeImg.setTag(0);
                } else {
                    passwordEyeImg.setTag(1);
                    passwordEdit.setTransformationMethod(new PasswordTransformationMethod());
                    passwordEyeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_open));
                }
                break;
            case R.id.confirm_password_eye_img:
                if (confirmPasswordEyeImg.getTag().equals(1)) {
                    confirmPassword.setTransformationMethod(null);
                    confirmPasswordEyeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_close));
                    confirmPasswordEyeImg.setTag(0);
                } else {
                    confirmPasswordEyeImg.setTag(1);
                    confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    confirmPasswordEyeImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_open));
                }
                break;
        }
    }


    public void signup(HashMap<String, String> map) {
        customDialog.show();
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
                    customDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("phone"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

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
                    SharedHelper.putKey(context, "access_token", response.body().getTokenType() + " " + response.body().getAccessToken());
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
        email = emailEdit.getText().toString();
        strConfirmPassword = confirmPassword.getText().toString();
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
            if(connectionHelper.isConnectingToInternet()){
                signup(map);
            }else {
                Utils.displayMessage(activity,context,getString(R.string.oops_connect_your_internet));
            }
        }

    }

    private void getProfile() {
        Call<User> getprofile = apiInterface.getProfile();
        getprofile.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {

                    if (response.code() == 401) {
                        SharedHelper.putKey(context, "logged", "false");
                        startActivity(new Intent(context, LoginActivity.class));
                        finish();
                    }

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().toString());
                        Toast.makeText(context, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    SharedHelper.putKey(context, "logged", "true");
                    CommonClass.getInstance().profileModel = response.body();
                    CommonClass.getInstance().addCart=new AddCart();
                    CommonClass.getInstance().addCart.setProductList(response.body().getCart());
                    CommonClass.getInstance().addressList=new AddressList();
                    CommonClass.getInstance().addressList.setAddresses(response.body().getAddresses());
                    startActivity(new Intent(context, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

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

