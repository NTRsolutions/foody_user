package com.foodie.app.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.foodie.app.BuildConfig;
import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.activities.AccountPaymentActivity;
import com.foodie.app.activities.ChangePasswordActivity;
import com.foodie.app.activities.EditAccountActivity;
import com.foodie.app.activities.FavouritesActivity;
import com.foodie.app.activities.LoginActivity;
import com.foodie.app.activities.ManageAddressActivity;
import com.foodie.app.activities.NotificationActivity;
import com.foodie.app.activities.OrdersActivity;
import com.foodie.app.activities.PromotionActivity;
import com.foodie.app.activities.WelcomeScreenActivity;
import com.foodie.app.adapter.ProfileSettingsAdapter;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.helper.SharedHelper;
import com.foodie.app.utils.ListViewSizeHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class ProfileFragment extends Fragment {
    @BindView(R.id.text_line)
    TextView textLine;
    @BindView(R.id.app_version)
    TextView appVersion;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.logout)
    Button logout;
    @BindView(R.id.arrow_image)
    ImageView arrowImage;
    @BindView(R.id.list_layout)
    RelativeLayout listLayout;
    @BindView(R.id.myaccount_layout)
    LinearLayout myaccountLayout;
    @BindView(R.id.error_layout)
    RelativeLayout errorLayout;
    @BindView(R.id.login_btn)
    Button loginBtn;
    private Activity activity;
    private Context context;

    @BindView(R.id.profile_setting_lv)
    ListView profileSettingLv;

    private ViewGroup toolbar;
    private View toolbarLayout;
    ImageView userImage;
    TextView userName, userPhone, userEmail;

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

        if (GlobalData.getInstance().profileModel != null) {
            errorLayout.setVisibility(View.GONE);
            final List<String> list = Arrays.asList(getResources().getStringArray(R.array.profile_settings));
            List<Integer> listIcons = new ArrayList<>();
            listIcons.add(R.drawable.home);
            listIcons.add(R.drawable.heart);
            listIcons.add(R.drawable.payment);
            listIcons.add(R.drawable.ic_myorders);
            listIcons.add(R.drawable.ic_notifications);
            listIcons.add(R.drawable.ic_promotion_details);
            listIcons.add(R.drawable.padlock);
            ProfileSettingsAdapter adbPerson = new ProfileSettingsAdapter(context, list, listIcons);
            profileSettingLv.setAdapter(adbPerson);
            ListViewSizeHelper.getListViewSize(profileSettingLv);
            profileSettingLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openSettingPage(position);
                }
            });
            arrowImage.setTag(true);
//            collapse(listLayout);
            HomeActivity.updateNotificationCount(context, GlobalData.getInstance().notificationCount);
        } else {
            //set Error Layout
            errorLayout.setVisibility(View.VISIBLE);
        }

        String VERSION_NAME = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        appVersion.setText("Version " + VERSION_NAME + " ("+String.valueOf(versionCode)+ ")");


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.updateNotificationCount(context, GlobalData.getInstance().notificationCount);
        initView();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (toolbar != null) {
            toolbar.removeView(toolbarLayout);
        }

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
                startActivity(new Intent(context, NotificationActivity.class));
                break;
            case 5:
                startActivity(new Intent(context, PromotionActivity.class));
                break;
            case 6:
                startActivity(new Intent(context, ChangePasswordActivity.class));
                break;
            default:
        }
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        System.out.println("ProfileFragment");
        toolbar = (ViewGroup) getActivity().findViewById(R.id.toolbar);
        if (GlobalData.getInstance().profileModel != null) {
            toolbar.setVisibility(View.VISIBLE);
            toolbarLayout = LayoutInflater.from(context).inflate(R.layout.toolbar_profile, toolbar, false);
            userImage = (ImageView) toolbarLayout.findViewById(R.id.user_image);
            userName = (TextView) toolbarLayout.findViewById(R.id.user_name);
            userPhone = (TextView) toolbarLayout.findViewById(R.id.user_phone);
            userEmail = (TextView) toolbarLayout.findViewById(R.id.user_mail);
            initView();
            Button editBtn = (Button) toolbarLayout.findViewById(R.id.edit);
            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, EditAccountActivity.class));
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, EditAccountActivity.class));
                }
            });
            toolbar.addView(toolbarLayout);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }

    private void initView() {
        if (GlobalData.profileModel != null) {
            Glide.with(context).load(GlobalData.profileModel.getAvatar())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error((R.drawable.man))
                    .into(userImage);
            userPhone.setText(GlobalData.profileModel.getPhone());
            userName.setText(GlobalData.profileModel.getName());
            userEmail.setText(" - " + GlobalData.profileModel.getEmail());
        }
    }

    @OnClick({R.id.arrow_image, R.id.logout, R.id.myaccount_layout, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myaccount_layout:
                if (arrowImage.getTag().equals(true)) {
                    //rotate arrow image
                    arrowImage.animate().setDuration(500).rotation(180).start();
                    arrowImage.setTag(false);
                    //collapse animation
                    collapse(listLayout);
                    viewLine.setVisibility(View.VISIBLE);
                    textLine.setVisibility(View.GONE);
                } else {
                    //rotate arrow image
                    arrowImage.animate().setDuration(500).rotation(360).start();
                    arrowImage.setTag(true);
                    viewLine.setVisibility(View.GONE);
                    textLine.setVisibility(View.VISIBLE);
                    //expand animation
                    expand(listLayout);
                }
                break;
            case R.id.logout:
                alertDialog();
                break;
            case R.id.login_btn:
                SharedHelper.putKey(context, "logged", "false");
                startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                getActivity().finish();
                break;
        }
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to logout?")
                .setPositiveButton(getResources().getString(R.string.logout), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        SharedHelper.putKey(context, "logged", "false");
                        startActivity(new Intent(context, WelcomeScreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        GlobalData.getInstance().profileModel = null;
                        getActivity().finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(getResources().getColor(R.color.theme));
        nbutton.setTypeface(nbutton.getTypeface(), Typeface.BOLD);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(getResources().getColor(R.color.theme));
        pbutton.setTypeface(pbutton.getTypeface(), Typeface.BOLD);
    }

}
