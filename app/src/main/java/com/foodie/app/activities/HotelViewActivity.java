package com.foodie.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.foodie.app.R;
import com.foodie.app.adapter.AccompanimentDishesAdapter;
import com.foodie.app.adapter.RecommendedDishesAdapter;
import com.foodie.app.model.RecommendedDish;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hotel_view, menu);
        return true;
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
}
