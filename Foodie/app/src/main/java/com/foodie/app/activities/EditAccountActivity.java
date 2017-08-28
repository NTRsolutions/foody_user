package com.foodie.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.foodie.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditAccountActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.username)
    EditText usernameEdit;
    @BindView(R.id.mobile_number)
    EditText mobileNumberEdit;
    @BindView(R.id.email_address)
    EditText emailAddressEdit;
    @BindView(R.id.update)
    Button updateBtn;
    @BindView(R.id.user_profile)
    CircleImageView userProfileImg;
    @BindView(R.id.edit_user_profile)
    ImageView editUserProfileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @OnClick({R.id.edit_user_profile, R.id.update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_user_profile:
                break;
            case R.id.update:
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
