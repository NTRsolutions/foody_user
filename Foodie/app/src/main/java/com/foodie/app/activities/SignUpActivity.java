package com.foodie.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.foodie.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_top)
    Toolbar toolbarTop;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        setSupportActionBar(toolbarTop);
        toolbarTop.setNavigationIcon(R.drawable.ic_back);
        toolbarTop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @OnClick({R.id.terms_and_conditions, R.id.sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.terms_and_conditions:
                break;
            case R.id.sign_up:
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
