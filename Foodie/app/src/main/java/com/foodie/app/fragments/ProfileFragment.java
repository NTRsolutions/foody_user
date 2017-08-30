package com.foodie.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.activities.AccountPaymentActivity;
import com.foodie.app.activities.ChangePasswordActivity;
import com.foodie.app.activities.EditAccountActivity;
import com.foodie.app.activities.FavouritesActivity;
import com.foodie.app.activities.LoginActivity;
import com.foodie.app.activities.ManageAddressActivity;
import com.foodie.app.activities.OrdersActivity;
import com.foodie.app.adapter.ProfileSettingsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by CSS22 on 22-08-2017.
 */

public class ProfileFragment extends Fragment {
    Activity activity;
    Context context;

    @BindView(R.id.user_profile)
    CircleImageView userProfileImg;
    @BindView(R.id.user_name)
    TextView userNameTxt;
    @BindView(R.id.phone)
    TextView phoneTxt;
    @BindView(R.id.email)
    TextView emailTxt;
    @BindView(R.id.edit)
    TextView editTxt;
    @BindView(R.id.profile_setting_lv)
    ListView profileSettingLv;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        this.activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.profile_settings));
        List<Integer> listIcons = new ArrayList<>();
        listIcons.add(R.drawable.ic_home);
        listIcons.add(R.drawable.ic_heart);
        listIcons.add(R.drawable.ic_payment);
        listIcons.add(R.drawable.ic_orders);
        listIcons.add(R.drawable.ic_padlock);
        ProfileSettingsAdapter adbPerson = new ProfileSettingsAdapter(context, list, listIcons);
        profileSettingLv.setAdapter(adbPerson);
        profileSettingLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openSettingPage(position);
            }
        });

        return view;

    }

    @OnClick({R.id.edit, R.id.logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit:
                startActivity(new Intent(context, EditAccountActivity.class));
                break;
            case R.id.logout:
                startActivity(new Intent(context, LoginActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    private void openSettingPage(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(context, ManageAddressActivity.class));
                break;
            case 1:
                startActivity(new Intent(context, FavouritesActivity.class));
                break;
            case 2:
                startActivity(new Intent(context, AccountPaymentActivity.class));
                break;
            case 3:
                startActivity(new Intent(context, OrdersActivity.class));
                break;
            case 4:
                startActivity(new Intent(context, ChangePasswordActivity.class));
                break;
            default:

        }
    }



}
