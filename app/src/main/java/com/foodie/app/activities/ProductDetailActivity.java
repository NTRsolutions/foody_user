package com.foodie.app.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.adapter.AddOnsAdapter;
import com.foodie.app.adapter.SliderPagerAdapter;
import com.foodie.app.adapter.ViewCartAdapter;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.models.Addon;
import com.foodie.app.models.Image;
import com.foodie.app.models.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
    public static  TextView addOnsTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=ProductDetailActivity.this;

        addOnsTxt=(TextView)findViewById(R.id.add_ons_txt);

        product = GlobalData.isSelectedProduct;
        productName.setText(product.getName()+"\n"+product.getPrices().getCurrency()+product.getPrices().getPrice());
        productDescription.setText(product.getDescription());
        slider_image_list = new ArrayList<>();
        addonList=new ArrayList<>();
        addonList.addAll(product.getAddons());

        //Add ons Adapter
        addOnsRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        addOnsRv.setItemAnimator(new DefaultItemAnimator());
        addOnsRv.setHasFixedSize(false);
        addOnsRv.setNestedScrollingEnabled(false);

        AddOnsAdapter  addOnsAdapter = new AddOnsAdapter(addonList, context);
        addOnsRv.setAdapter(addOnsAdapter);

        slider_image_list.addAll(product.getImages());
        sliderPagerAdapter = new SliderPagerAdapter(this, slider_image_list);
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
}
