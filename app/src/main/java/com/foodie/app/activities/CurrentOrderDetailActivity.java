package com.foodie.app.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.foodie.app.R;
import com.foodie.app.fragments.HomeFragment;
import com.foodie.app.fragments.OrderDetailFragment;
import com.foodie.app.fragments.OrderViewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentOrderDetailActivity extends AppCompatActivity {

    @BindView(R.id.order_id_txt)
    TextView orderIdTxt;
    @BindView(R.id.order_item_txt)
    TextView orderItemTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.order_placed_img)
    ImageView orderPlacedImg;
    @BindView(R.id.order_confirmed_img)
    ImageView orderConfirmedImg;
    @BindView(R.id.order_processed_img)
    ImageView orderProcessedImg;
    @BindView(R.id.order_pickedup_img)
    ImageView orderPickedupImg;
    @BindView(R.id.order_succeess_image)
    ImageView orderSucceessImage;
    @BindView(R.id.order_status_txt)
    TextView orderStatusTxt;
    @BindView(R.id.order_status_layout)
    RelativeLayout orderStatusLayout;

    Fragment orderFullViewFragment;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order_detail);
        ButterKnife.bind(this);


        //set Toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0, 0);


        //set Fragment
        orderFullViewFragment = new OrderViewFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.order_detail_fargment, orderFullViewFragment).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }
}
