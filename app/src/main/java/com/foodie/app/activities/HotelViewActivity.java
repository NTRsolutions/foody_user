package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodie.app.HeaderView;
import com.foodie.app.R;
import com.foodie.app.adapter.AccompanimentDishesAdapter;
import com.foodie.app.model.RecommendedDish;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.attr.offset;

public class HotelViewActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recommended_dishes_rv)
    RecyclerView recommendedDishesRv;
    @BindView(R.id.accompaniment_dishes_rv)
    RecyclerView accompanimentDishesRv;
    @BindView(R.id.heart_btn)
    ShineButton heartBtn;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.restaurant_title2)
    TextView restaurantTitle2;
    @BindView(R.id.restaurant_subtitle2)
    TextView restaurantSubtitle2;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    public static TextView itemText;
    public static TextView viewCart;
    public static RelativeLayout viewCartLayout;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.header_view_title)
    TextView headerViewTitle;
    @BindView(R.id.header_view_sub_title)
    TextView headerViewSubTitle;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.scroll)
    NestedScrollView scroll;
    private boolean isHideToolbarView = false;
    @BindView(R.id.toolbar_header_view)
    HeaderView toolbarHeaderView;
    @BindView(R.id.float_header_view)
    HeaderView floatHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_view);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        collapsingToolbar.setTitle(" ");
        toolbarHeaderView.bindTo("Dominos Pizza", "American fast food");
        floatHeaderView.bindTo("Dominos Pizza", "American fast food");
        appBarLayout.addOnOffsetChangedListener(this);

        itemText = (TextView) findViewById(R.id.item_text);
        viewCart = (TextView) findViewById(R.id.view_cart);
        viewCartLayout = (RelativeLayout) findViewById(R.id.view_cart_layout);
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HotelViewActivity.this, ViewCartActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
            }
        });

//        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.e("scrollX", "" + scrollX);
//                Log.e("scrollY", "" + scrollY);
//                Log.e("oldScrollX", "" + oldScrollX);
//                Log.e("oldScrollY", "" + oldScrollY);
//
//                if (scrollY >= 50 && scrollY <= 280) {
//                    int staticData = 280;
//                    float alphaValue = (float) scrollY / staticData;
//                    heartBtn.setAlpha(1 - alphaValue);
//                    titleLayout.setAlpha(alphaValue);
//                    viewLine.setAlpha(alphaValue);
//                } else if (scrollY >= 280) {
//                    heartBtn.setAlpha(0);
//                    viewLine.setAlpha(1.0f);
//                    titleLayout.setAlpha(1.0f);
//                } else if (scrollY <= 50) {
//                    viewLine.setAlpha(0);
//                    titleLayout.setAlpha(0);
//                    heartBtn.setAlpha(1.0f);
//                }
//
//            }
//        });

        //Heart Animation Button
        if (heartBtn != null)
            heartBtn.init(this);
        heartBtn.setShineDistanceMultiple(1.8f);
        heartBtn.setTag(0);
        heartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(heartBtn.getTag().equals(0)){
                    heartBtn.setTag(1);
                    heartBtn.setShapeResource(R.raw.heart);
                }else {
                    heartBtn.setTag(0);
                    heartBtn.setShapeResource(R.raw.icc_heart);
                }

            }
        });
        heartBtn.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                Log.e("HeartButton", "click " + checked);
//                if (checked) {
//                    heartBtn.setShapeResource(R.raw.heart);
//                }
//                else{
//                    heartBtn.setShapeResource(R.raw.icc_heart);
//                }

            }
        });


        final ArrayList<RecommendedDish> list = new ArrayList<>();
        list.add(new RecommendedDish("Classic Brownie Eggless", "Brownies", "20", true, "url", "description", "available"));
        list.add(new RecommendedDish("Roasted Nuts Brownie", "Breakfast", "30", true, "url", "description", "out of stock"));
        list.add(new RecommendedDish("Red Velvet Brownie", "Interplay Brownie", "50", false, "url", "description", "available"));
        list.add(new RecommendedDish("Classic Brownie Eggless", "Brownies", "20", true, "url", "description", "available"));
        list.add(new RecommendedDish("Roasted Nuts Brownie", "Breakfast", "300", true, "url", "description", "available"));
        list.add(new RecommendedDish("Red Velvet Brownie", "Interplay Brownie", "200", false, "url", "description", "Next Available at 12.30pm tommorrow"));
        list.add(new RecommendedDish("Classic Brownie Eggless", "Brownies", "90", true, "url", "description", "available"));
        list.add(new RecommendedDish("Roasted Nuts Brownie", "Breakfast", "50", true, "url", "description", "out of stock"));
        AccompanimentDishesAdapter adapterAccompaniment = new AccompanimentDishesAdapter(list, this);
        accompanimentDishesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        accompanimentDishesRv.setItemAnimator(new DefaultItemAnimator());
        accompanimentDishesRv.setAdapter(adapterAccompaniment);


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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }
}


