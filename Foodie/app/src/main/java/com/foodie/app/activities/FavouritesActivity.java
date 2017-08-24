package com.foodie.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.adapter.FavouriteDishAdapter;
import com.foodie.app.model.FavouriteDish;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouritesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.favorites_lv)
    ListView favoritesLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayList<FavouriteDish> list = new ArrayList<FavouriteDish>();
        list.add(new FavouriteDish("Pancake", "Cake", ""));
        list.add(new FavouriteDish("Bunny burgs", "Burgers", ""));
        FavouriteDishAdapter adbPerson = new FavouriteDishAdapter(FavouritesActivity.this, list);
        favoritesLv.setAdapter(adbPerson);

    }
}
