package com.foodie.app.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.fragments.OrderViewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PastOrderDetailActivity extends AppCompatActivity {


    Fragment orderFullViewFragment;
    FragmentManager fragmentManager;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.source_image)
    ImageView sourceImage;
    @BindView(R.id.address_header)
    TextView addressHeader;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.source_layout)
    RelativeLayout sourceLayout;
    @BindView(R.id.dot_line)
    View dotLine;
    @BindView(R.id.destination_image)
    ImageView destinationImage;
    @BindView(R.id.destination_address_header)
    TextView destinationAddressHeader;
    @BindView(R.id.destination_address)
    TextView destinationAddress;
    @BindView(R.id.destination_layout)
    RelativeLayout destinationLayout;
    @BindView(R.id.view_line2)
    View viewLine2;
    @BindView(R.id.order_succeess_image)
    ImageView orderSucceessImage;
    @BindView(R.id.order_status_txt)
    TextView orderStatusTxt;
    @BindView(R.id.order_status_layout)
    RelativeLayout orderStatusLayout;
    @BindView(R.id.order_detail_fargment)
    FrameLayout orderDetailFargment;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

        //Toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
