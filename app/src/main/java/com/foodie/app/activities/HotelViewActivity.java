package com.foodie.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foodie.app.HeaderView;
import com.foodie.app.R;
import com.foodie.app.adapter.HotelCatagoeryAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.model.Category;
import com.foodie.app.model.Shop;
import com.foodie.app.model.ShopsModel;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
    @BindView(R.id.restaurant_image)
    ImageView restaurantImage;
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
    @BindView(R.id.offer)
    TextView offer;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.delivery_time)
    TextView deliveryTime;
    private boolean isHideToolbarView = false;
    @BindView(R.id.toolbar_header_view)
    HeaderView toolbarHeaderView;
    @BindView(R.id.float_header_view)
    HeaderView floatHeaderView;

    Activity activity;
    int restaurantPosition = 0;


    Context context;
    public static ShopsModel shops;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    List<Category> categoryList;
    HotelCatagoeryAdapter catagoeryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_view);
        ButterKnife.bind(this);
        context = HotelViewActivity.this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        activity = HotelViewActivity.this;
        appBarLayout.addOnOffsetChangedListener(this);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            restaurantPosition = bundle.getInt("position");
        }
        shops = CommonClass.getInstance().list.get(restaurantPosition);

        if (shops.getOfferPercent() == null) {
            offer.setVisibility(View.GONE);
        } else {
            offer.setVisibility(View.VISIBLE);
            offer.setText("Flat " + shops.getOfferPercent().toString() + "% offer on all Orders");
        }

        if (shops.getRatings() != null) {
            Double ratingvalue = new BigDecimal(shops.getRatings().getRating().toString()).setScale(1, RoundingMode.HALF_UP).doubleValue();
            rating.setText("" + ratingvalue);
        } else
            rating.setText("No Rating");

        deliveryTime.setText(shops.getEstimatedDeliveryTime().toString() + "Mins");

        itemText = (TextView) findViewById(R.id.item_text);
        viewCart = (TextView) findViewById(R.id.view_cart);
        viewCartLayout = (RelativeLayout) findViewById(R.id.view_cart_layout);
        viewCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HotelViewActivity.this, ViewCartActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
            }
        });


        Glide.with(context).load(shops.getAvatar()).placeholder(R.drawable.item1).dontAnimate()
                .error(R.drawable.item1).into(restaurantImage);

        //Set Palette color
        Picasso.with(HotelViewActivity.this)
                .load(shops.getAvatar())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        assert restaurantImage != null;
                        restaurantImage.setImageBitmap(bitmap);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                        if (textSwatch == null) {
                                            Toast.makeText(HotelViewActivity.this, "Null swatch :(", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        collapsingToolbar.setContentScrimColor(textSwatch.getRgb());
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            Window window = getWindow();
                                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                            window.setStatusBarColor(textSwatch.getRgb());
                                        }
                                        headerViewTitle.setTextColor(textSwatch.getTitleTextColor());
                                        headerViewSubTitle.setTextColor(textSwatch.getBodyTextColor());
                                    }
                                });

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        //Set title
        collapsingToolbar.setTitle(" ");
        toolbarHeaderView.bindTo(shops.getName(), shops.getDescription());
        floatHeaderView.bindTo(shops.getName(), shops.getDescription());

        //Get category list data
        categoryList = shops.getCategories();

        //Set Categoery list adapter
        catagoeryAdapter = new HotelCatagoeryAdapter(this, activity, categoryList);
        accompanimentDishesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        accompanimentDishesRv.setItemAnimator(new DefaultItemAnimator());
        accompanimentDishesRv.setAdapter(catagoeryAdapter);

        if (CommonClass.getInstance().addCart != null) {

        }


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
                if (heartBtn.getTag().equals(0)) {
                    heartBtn.setTag(1);
                    heartBtn.setShapeResource(R.raw.heart);
                } else {
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


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CommonClass.getInstance().list != null) {
            List<ShopsModel> shopList = CommonClass.getInstance().list;
            for (int i = 0; i < shopList.size(); i++) {
                if (shopList.get(i).getId().equals(shops.getId())) {
                    shops = shopList.get(i);
                    categoryList = shops.getCategories();
                    catagoeryAdapter.notifyDataSetChanged();
                }
            }
        }

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


