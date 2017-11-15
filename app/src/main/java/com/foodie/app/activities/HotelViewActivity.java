package com.foodie.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.ViewSkeletonScreen;
import com.foodie.app.HeaderView;
import com.foodie.app.R;
import com.foodie.app.adapter.HotelCatagoeryAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.helper.ConnectionHelper;
import com.foodie.app.models.AddCart;
import com.foodie.app.models.Category;
import com.foodie.app.models.Favorite;
import com.foodie.app.models.Product;
import com.foodie.app.models.Shop;
import com.foodie.app.models.ShopDetail;
import com.foodie.app.utils.Utils;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.foodie.app.adapter.HotelCatagoeryAdapter.bottomSheetDialogFragment;

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
    public static TextView viewCartShopName;
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
    @BindView(R.id.root_layout)
    CoordinatorLayout rootLayout;
    private boolean isHideToolbarView = false;
    @BindView(R.id.toolbar_header_view)
    HeaderView toolbarHeaderView;
    @BindView(R.id.float_header_view)
    HeaderView floatHeaderView;
    int restaurantPosition = 0;
    boolean isShopIsChanged = true;
    int priceAmount = 0;
    int itemCount = 0;
    int itemQuantity = 0;
    Animation slide_down, slide_up;


    Context context;
    ConnectionHelper connectionHelper;
    Activity activity;
    public static Shop shops;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    public  static List<Category> categoryList;
    public  static List<Product> featureProductList;
    public  static  HotelCatagoeryAdapter catagoeryAdapter;
    ViewSkeletonScreen skeleton;
    boolean isFavourite=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_view);
        ButterKnife.bind(this);
        context = HotelViewActivity.this;
        activity = HotelViewActivity.this;
        connectionHelper = new ConnectionHelper(context);

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

        //Load animation
        slide_down = AnimationUtils.loadAnimation(context,
                R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(context,
                R.anim.slide_up);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            restaurantPosition = bundle.getInt("position");

        }
        isFavourite = getIntent().getBooleanExtra("is_fav", false);
        shops = GlobalData.getInstance().selectedShop;

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
        viewCartShopName = (TextView) findViewById(R.id.view_cart_shop_name);
        viewCart = (TextView) findViewById(R.id.view_cart);
        viewCartLayout = (RelativeLayout) findViewById(R.id.view_cart_layout);

        viewCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HotelViewActivity.this, ViewCartActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
            }
        });


        Glide.with(context).load(shops.getAvatar()).placeholder(R.drawable.ic_restaurant_place_holder).dontAnimate()
                .error(R.drawable.ic_restaurant_place_holder).into(restaurantImage);

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
                                        Palette.Swatch textSwatch = palette.getDarkMutedSwatch();
                                        if (textSwatch == null) {
                                            textSwatch = palette.getMutedSwatch();
                                            if (textSwatch != null) {
                                                collapsingToolbar.setContentScrimColor(textSwatch.getRgb());
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    Window window = getWindow();
                                                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                                    window.setStatusBarColor(textSwatch.getRgb());
                                                }
                                                headerViewTitle.setTextColor(textSwatch.getTitleTextColor());
                                                headerViewSubTitle.setTextColor(textSwatch.getBodyTextColor());
                                            }
                                        } else {
                                            collapsingToolbar.setContentScrimColor(textSwatch.getRgb());
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                Window window = getWindow();
                                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                                window.setStatusBarColor(textSwatch.getRgb());
                                            }
                                            headerViewTitle.setTextColor(textSwatch.getTitleTextColor());
                                            headerViewSubTitle.setTextColor(textSwatch.getBodyTextColor());
                                        }

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

        categoryList = new ArrayList<>();
        //Set Categoery shopList adapter
        catagoeryAdapter = new HotelCatagoeryAdapter(this, activity, categoryList);
        accompanimentDishesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        accompanimentDishesRv.setItemAnimator(new DefaultItemAnimator());
        accompanimentDishesRv.setAdapter(catagoeryAdapter);

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
        if (shops.getFavorite() != null||isFavourite) {
            heartBtn.setChecked(true);
            heartBtn.setTag(1);
        } else
            heartBtn.setTag(0);
        heartBtn.setShineDistanceMultiple(1.8f);
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
                if (connectionHelper.isConnectingToInternet()) {
                    if (checked) {
                        if (GlobalData.profileModel != null)
                            doFavorite(shops.getId());
                        else{
                            startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            overridePendingTransition(R.anim.slide_in_left, R.anim.anim_nothing);
                            finish();
                        }
                    } else {
                        deleteFavorite(shops.getId());
                    }
                } else {
                    Utils.displayMessage(activity, context, getString(R.string.oops_connect_your_internet));
                }
            }
        });
        skeleton = Skeleton.bind(rootLayout)
                .load(R.layout.skeleton_hotel_view)
                .show();

    }


    private void deleteFavorite(Integer id) {
        Call<Favorite> call = apiInterface.deleteFavorite(id);
        call.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                Favorite favorite = response.body();
            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {

            }
        });

    }

    private void doFavorite(Integer id) {
        Call<Favorite> call = apiInterface.doFavorite(id);
        call.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                Favorite favorite = response.body();
            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {

            }
        });

    }

    private void setViewcartBottomLayout(AddCart addCart) {
        priceAmount = 0;
        itemQuantity = 0;
        itemCount = 0;
        //get Item Count
        itemCount = addCart.getProductList().size();
        for (int i = 0; i < itemCount; i++) {
            //Get Total item Quantity
            itemQuantity = itemQuantity + addCart.getProductList().get(i).getQuantity();
            //Get product price
            if (addCart.getProductList().get(i).getProduct().getPrices().getPrice() != null)
                priceAmount = priceAmount + (addCart.getProductList().get(i).getQuantity() * addCart.getProductList().get(i).getProduct().getPrices().getPrice());
        }
        GlobalData.getInstance().notificationCount = itemQuantity;
        if (itemQuantity == 0) {
            HotelViewActivity.viewCartLayout.setVisibility(View.GONE);
            // Start animation
            viewCartLayout.startAnimation(slide_down);
        } else if (itemQuantity == 1) {
            if (shops.getId() == GlobalData.getInstance().addCart.getProductList().get(0).getProduct().getShopId()) {
                HotelViewActivity.viewCartShopName.setVisibility(View.VISIBLE);
                HotelViewActivity.viewCartShopName.setText("From : " + GlobalData.getInstance().addCart.getProductList().get(0).getProduct().getShop().getName());
            } else
                HotelViewActivity.viewCartShopName.setVisibility(View.GONE);
            String currency = addCart.getProductList().get(0).getProduct().getPrices().getCurrency();
            HotelViewActivity.itemText.setText("" + itemQuantity + " Item | " + currency + "" + priceAmount);
            if (HotelViewActivity.viewCartLayout.getVisibility() == View.GONE) {
                // Start animation
                HotelViewActivity.viewCartLayout.setVisibility(View.VISIBLE);
                viewCartLayout.startAnimation(slide_up);
            }
        } else {
            if (shops.getId() != GlobalData.getInstance().addCart.getProductList().get(0).getProduct().getShopId()) {
                HotelViewActivity.viewCartShopName.setVisibility(View.VISIBLE);
                HotelViewActivity.viewCartShopName.setText("From : " + GlobalData.getInstance().addCart.getProductList().get(0).getProduct().getShop().getName());
            } else
                HotelViewActivity.viewCartShopName.setVisibility(View.GONE);

            String currency = addCart.getProductList().get(0).getProduct().getPrices().getCurrency();
            HotelViewActivity.itemText.setText("" + itemQuantity + " Items | " + currency + "" + priceAmount);
            if (HotelViewActivity.viewCartLayout.getVisibility() == View.GONE) {
                // Start animation
                HotelViewActivity.viewCartLayout.setVisibility(View.VISIBLE);
                viewCartLayout.startAnimation(slide_up);
            }

        }
    }

    private void getCategories(HashMap<String, String> map) {
        Call<ShopDetail> call = apiInterface.getCategories(map);
        call.enqueue(new Callback<ShopDetail>() {
            @Override
            public void onResponse(Call<ShopDetail> call, Response<ShopDetail> response) {
                skeleton.hide();
                categoryList=response.body().getCategories();
                featureProductList=response.body().getFeaturedProducts();
                GlobalData.getInstance().categoryList = categoryList;
                GlobalData.getInstance().selectedShop.setCategories(categoryList);
                catagoeryAdapter = new HotelCatagoeryAdapter(context, activity, categoryList);
                accompanimentDishesRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                accompanimentDishesRv.setItemAnimator(new DefaultItemAnimator());
                accompanimentDishesRv.setAdapter(catagoeryAdapter);
                if (GlobalData.getInstance().addCart != null && GlobalData.getInstance().addCart.getProductList().size() != 0) {
                    setViewcartBottomLayout(GlobalData.getInstance().addCart);
                }
                catagoeryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ShopDetail> call, Throwable t) {

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
//        if (GlobalData.getInstance().shopList != null) {
//            List<Shop> shopList = GlobalData.getInstance().shopList;
//            for (int i = 0; i < shopList.size(); i++) {
//                if (shopList.get(i).getId().equals(shops.getId())) {
//                    shops = shopList.get(i);
//                    categoryList = GlobalData.getInstance().categoryList;
//                    catagoeryAdapter.notifyDataSetChanged();
//                }
//            }
//        }
        if(bottomSheetDialogFragment!=null)
            bottomSheetDialogFragment.dismiss();
        //get User Profile Data
        if (GlobalData.getInstance().profileModel != null) {
            HashMap<String, String> map = new HashMap<>();
            map.put("shop", String.valueOf(shops.getId()));
            map.put("user_id", String.valueOf(GlobalData.getInstance().profileModel.getId()));
            getCategories(map);
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("shop", String.valueOf(shops.getId()));
            getCategories(map);
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


