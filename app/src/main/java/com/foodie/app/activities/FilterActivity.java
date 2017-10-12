package com.foodie.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.adapter.FilterAdapter;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.model.Cuisine;
import com.foodie.app.model.FilterModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodie.app.helper.CommonClass.cuisineList;
import static com.foodie.app.helper.CommonClass.shopList;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.filter_rv)
    RecyclerView filterRv;
    public static Button applyFilterBtn;
    public static TextView resetTxt;


    private FilterAdapter adapter;
    private List<FilterModel> modelListReference = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        applyFilterBtn = (Button) findViewById(R.id.apply_filter);
        resetTxt = (TextView) findViewById(R.id.reset_txt);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_nothing, R.anim.slide_down);
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this);
        filterRv.setLayoutManager(manager);
        adapter = new FilterAdapter(this, modelListReference);
        filterRv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<FilterModel> modelList = new ArrayList<>();

        List<String> filters = new ArrayList<>();
        Cuisine cuisine1 = new Cuisine();
        cuisine1.setName("Offers");
        Cuisine cuisine2 = new Cuisine();
        cuisine2.setName("Pure veg");
        List<Cuisine> cuisineList1 = new ArrayList<>();
        cuisineList1.add(cuisine1);
        cuisineList1.add(cuisine2);
        FilterModel model = new FilterModel();
        model.setHeader("Show Restaurants With");
        model.setFilters(cuisineList1);
        modelList.add(model);
        filters = new ArrayList<>();
        List<Cuisine> cuisineList2 = CommonClass.cuisineList;

        for (int i = 0; i < cuisineList2.size(); i++) {
            filters.add(cuisineList2.get(i).getName());
        }
        model = new FilterModel();
        model.setHeader("Cusines");
        model.setFilters(cuisineList2);
        modelList.add(model);

        modelListReference.clear();
        modelListReference.addAll(modelList);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_down);

    }
}
