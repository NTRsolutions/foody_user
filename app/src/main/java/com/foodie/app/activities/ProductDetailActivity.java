package com.foodie.app.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.adapter.AddOnsAdapter;
import com.foodie.app.adapter.SliderPagerAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.models.AddCart;
import com.foodie.app.models.Addon;
import com.foodie.app.models.Image;
import com.foodie.app.models.Product;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.foodie.app.adapter.AddOnsAdapter.list;


public class ProductDetailActivity extends AppCompatActivity {

    @BindView(R.id.product_slider)
    ViewPager productSlider;
    @BindView(R.id.product_slider_dots)
    LinearLayout productSliderDots;

    SliderPagerAdapter sliderPagerAdapter;
    List<Image> slider_image_list;
    int page_position = 0;
    @BindView(R.id.add_ons_rv)
    RecyclerView addOnsRv;
    @BindView(R.id.product_name)
    TextView productName;
    @BindView(R.id.product_description)
    TextView productDescription;

    Product product;
    List<Addon> addonList;
    Context context;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    public static TextView addOnsTxt;
    int cartId = 0;
    int quantity = 0;

    CustomDialog customDialog;

    public static TextView itemText;
    public static TextView viewCart;
    public static RelativeLayout addItemLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                onBackPressed();
            }
        });
        context = ProductDetailActivity.this;

        //Intialize
        addOnsTxt = (TextView) findViewById(R.id.add_ons_txt);
        itemText = (TextView) findViewById(R.id.item_text);
        viewCart = (TextView) findViewById(R.id.view_cart);
        addItemLayout = (RelativeLayout) findViewById(R.id.view_cart_layout);
        product = GlobalData.isSelectedProduct;
        if (GlobalData.addCart != null) {
            if (GlobalData.addCart.getProductList().size() != 0) {
                for (int i = 0; i < GlobalData.addCart.getProductList().size(); i++) {
                    if (GlobalData.addCart.getProductList().get(i).getProductId().equals(product.getId())) {
                        cartId = GlobalData.addCart.getProductList().get(i).getId();
                        quantity = GlobalData.addCart.getProductList().get(i).getQuantity();
                    }
                }
            }
        }

        addItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("product_id", product.getId().toString());
                if (product.getCart() != null && product.getCart().size() == 1 && product.getAddons().isEmpty()) {
                    map.put("quantity", String.valueOf(product.getCart().get(0).getQuantity() + 1));
                    map.put("cart_id", String.valueOf(product.getCart().get(0).getId()));
                } else if (product.getAddons().isEmpty() && cartId != 0) {
                    map.put("quantity", String.valueOf(quantity + 1));
                    map.put("cart_id", String.valueOf(cartId));
                } else {
                    map.put("quantity", "1");
                    if (!list.isEmpty()) {
                        for (int i = 0; i < list.size(); i++) {
                            Addon addon = list.get(i);
                            if (addon.getAddon().getChecked()) {
                                map.put("product_addons[" + "" + i + "]", addon.getId().toString());
                                map.put("addons_qty[" + "" + i + "]", addon.getQuantity().toString());
                            }
                        }
                    }
                }
                Log.e("AddCart_add", map.toString());
                addItem(map);
            }
        });


        productName.setText(product.getName() + "\n" + product.getPrices().getCurrency() + product.getPrices().getPrice());
        itemText.setText("1 Item | " + product.getPrices().getCurrency() + product.getPrices().getPrice());
        productDescription.setText(product.getDescription());
        slider_image_list = new ArrayList<>();
        addonList = new ArrayList<>();
        addonList.addAll(product.getAddons());
        if (addonList.size() == 0)
            addOnsTxt.setVisibility(View.GONE);
        else
            addOnsTxt.setVisibility(View.VISIBLE);

        //Add ons Adapter
        addOnsRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//        addOnsRv.setItemAnimator(new DefaultItemAnimator());
        addOnsRv.setHasFixedSize(false);
        addOnsRv.setNestedScrollingEnabled(false);

        AddOnsAdapter addOnsAdapter = new AddOnsAdapter(addonList, context);
        addOnsRv.setAdapter(addOnsAdapter);

        slider_image_list.addAll(product.getImages());
        sliderPagerAdapter = new SliderPagerAdapter(this, slider_image_list, true);
        productSlider.setAdapter(sliderPagerAdapter);
        addBottomDots(0);
        productSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addItem(HashMap<String, String> map) {
        Call<AddCart> call = apiInterface.postAddCart(map);
        call.enqueue(new Callback<AddCart>() {
            @Override
            public void onResponse(@NonNull Call<AddCart> call, @NonNull Response<AddCart> response) {
                if (response.isSuccessful()) {
                    GlobalData.addCart = response.body();
                    finish();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddCart> call, @NonNull Throwable t) {

            }
        });

    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[slider_image_list.size()];

        productSliderDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            productSliderDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
        finish();
    }
}
