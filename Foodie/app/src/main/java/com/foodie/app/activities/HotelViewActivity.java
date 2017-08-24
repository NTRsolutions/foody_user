package com.foodie.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.foodie.app.R;
import com.foodie.app.adapter.AccompanimentDishesAdapter;
import com.foodie.app.adapter.ImpressiveDishesAdapter;
import com.foodie.app.adapter.RecommendedDishesAdapter;
import com.foodie.app.model.ImpressiveDish;
import com.foodie.app.model.RecommendedDish;

import java.util.ArrayList;

public class HotelViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        final ArrayList<RecommendedDish> list = new ArrayList<>();
        list.add(new RecommendedDish("Non", "Starter", "$20", true, "url", "description"));
        list.add(new RecommendedDish("Dosa", "Breakfast", "$10", true, "url", "description"));
        list.add(new RecommendedDish("Biriyani", "Lunch", "$25", false, "url", "description"));
        list.add(new RecommendedDish("Icecream", "Desert", "$10", true, "url", "description"));
        RecyclerView rv = (RecyclerView) findViewById(R.id.recommended_dishes_rv);
        RecommendedDishesAdapter adapter = new RecommendedDishesAdapter(list, this);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);


        final ArrayList<RecommendedDish> accompanimentList = new ArrayList<>();
        accompanimentList.add(new RecommendedDish("Raita", "Complement", "$20", true, "url", "description"));
        accompanimentList.add(new RecommendedDish("Dosa", "Breakfast", "$10", true, "url", "description"));
        accompanimentList.add(new RecommendedDish("Sauce", "Side Dish, Complement", "$25", false, "url", "description"));
        accompanimentList.add(new RecommendedDish("Icecream", "Desert", "$10", true, "url", "description"));
        RecyclerView rvAccompaniment = (RecyclerView) findViewById(R.id.accompaniment_dishes_rv);
        AccompanimentDishesAdapter adapterAccompaniment = new AccompanimentDishesAdapter(accompanimentList, this);
        rvAccompaniment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvAccompaniment.setItemAnimator(new DefaultItemAnimator());
        rvAccompaniment.setAdapter(adapterAccompaniment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hotel_view, menu);
        return true;
    }

}
