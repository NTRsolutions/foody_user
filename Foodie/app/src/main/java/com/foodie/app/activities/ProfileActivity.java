package com.foodie.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.foodie.app.R;
import com.foodie.app.adapter.ProfileSettingsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.profile_setting_lv)
    ListView profileSettingLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        List<String> list = Arrays.asList(getResources().getStringArray(R.array.profile_settings));
        List<Integer> listIcons = new ArrayList<>();
        listIcons.add(R.drawable.ic_home);
        listIcons.add(R.drawable.ic_heart);
        listIcons.add(R.drawable.ic_payment);
        listIcons.add(R.drawable.ic_orders);
        listIcons.add(R.drawable.ic_padlock);
        ProfileSettingsAdapter adbPerson= new ProfileSettingsAdapter (ProfileActivity.this, list, listIcons);
        profileSettingLv.setAdapter(adbPerson);


    }
}
