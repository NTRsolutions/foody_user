package com.foodie.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.adapter.AccompanimentDishesAdapter;
import com.foodie.app.adapter.RecommendedDishesAdapter;
import com.foodie.app.model.RecommendedDish;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HotelViewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recommended_dishes_rv)
    RecyclerView recommendedDishesRv;
    @BindView(R.id.accompaniment_dishes_rv)
    RecyclerView accompanimentDishesRv;
    @BindView(R.id.restaurant_title)
    TextView restaurantTitle;
    @BindView(R.id.restaurant_subtitle)
    TextView restaurantSubtitle;
    @BindView(R.id.title_layout)
    LinearLayout titleLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_view);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("scrollX",""+scrollX);
                Log.e("scrollY",""+scrollY);
                Log.e("oldScrollX",""+oldScrollX);
                Log.e("oldScrollY",""+oldScrollY);

                if (scrollY>= 50 && scrollY<=280){
                    int staticData=280;
                    float alphaValue=(float)scrollY/staticData;
                    heartBtn.setAlpha(1-alphaValue);
                    titleLayout.setAlpha(alphaValue);
                    viewLine.setAlpha(alphaValue);
                }
                else if (scrollY>=280){
                    heartBtn.setAlpha(0);
                    viewLine.setAlpha(1.0f);
                    titleLayout.setAlpha(1.0f);
                }
                else if(scrollY<= 50){
                    viewLine.setAlpha(0);
                    titleLayout.setAlpha(0);
                    heartBtn.setAlpha(1.0f);
                }

            }
        });

        //Heart Animation Button
        if (heartBtn != null)
            heartBtn.init(this);
        heartBtn.setShineDistanceMultiple(1.8f);
        heartBtn.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                Log.e("HeartButton", "click " + checked);
                if(checked){
                    heartBtn.setImageDrawable(getResources().getDrawable(R.drawable.icc_heart));
                }
            }
        });


        final ArrayList<RecommendedDish> list = new ArrayList<>();
        list.add(new RecommendedDish("Classic Brownie Eggless", "Brownies", "$2", true, "url", "description"));
        list.add(new RecommendedDish("Roasted Nuts Brownie", "Breakfast", "$3", true, "url", "description"));
        list.add(new RecommendedDish("Red Velvet Brownie", "Interplay Brownie", "$4", false, "url", "description"));
        list.add(new RecommendedDish("Classic Brownie Eggless", "Brownies", "$2", true, "url", "description"));
        list.add(new RecommendedDish("Roasted Nuts Brownie", "Breakfast", "$3", true, "url", "description"));
        list.add(new RecommendedDish("Red Velvet Brownie", "Interplay Brownie", "$4", false, "url", "description"));
        list.add(new RecommendedDish("Classic Brownie Eggless", "Brownies", "$2", true, "url", "description"));
        list.add(new RecommendedDish("Roasted Nuts Brownie", "Breakfast", "$3", true, "url", "description"));
        RecommendedDishesAdapter adapter = new RecommendedDishesAdapter(list, this);
        recommendedDishesRv.setLayoutManager(new GridLayoutManager(this, 2));
        //recommendedDishesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendedDishesRv.setItemAnimator(new DefaultItemAnimator());
        recommendedDishesRv.setAdapter(adapter);


        final ArrayList<RecommendedDish> accompanimentList = new ArrayList<>();
        accompanimentList.add(new RecommendedDish("Raita", "Complement", "$20", true, "url", "description"));
        accompanimentList.add(new RecommendedDish("Dosa", "Breakfast", "$10", true, "url", "description"));
        accompanimentList.add(new RecommendedDish("Sauce", "Side Dish, Complement", "$25", false, "url", "description"));
        accompanimentList.add(new RecommendedDish("Icecream", "Desert", "$10", true, "url", "description"));
        AccompanimentDishesAdapter adapterAccompaniment = new AccompanimentDishesAdapter(accompanimentList, this);
        accompanimentDishesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        accompanimentDishesRv.setItemAnimator(new DefaultItemAnimator());
        accompanimentDishesRv.setAdapter(adapterAccompaniment);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_hotel_view, menu);
//        return true;
//    }

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
}
