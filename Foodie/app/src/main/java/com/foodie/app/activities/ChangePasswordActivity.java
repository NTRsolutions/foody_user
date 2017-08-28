package com.foodie.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.foodie.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChangePasswordActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_top)
    Toolbar toolbarTop;
    @BindView(R.id.new_password)
    EditText newPassword;
    @BindView(R.id.re_enter_password)
    EditText reEnterPassword;
    @BindView(R.id.confirm)
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
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

    @OnClick(R.id.confirm)
    public void onViewClicked() {
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
