package com.foodie.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.foodie.app.R;
import com.foodie.app.adapter.FilterAdapter;
import com.foodie.app.model.FilterModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.filter_rv)
    RecyclerView filterRv;


    private FilterAdapter adapter;
    private List<FilterModel> modelListReference = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        filters.add("Offers");
        filters.add("Non Veg");
        FilterModel model = new FilterModel();
        model.setHeader("Show Restaurants With");
        model.setFilters(filters);
        modelList.add(model);

        filters = new ArrayList<>();
        filters.add("Italian");
        filters.add("Danish");
        filters.add("Indian");
        filters.add("Spanish");
        filters.add("American");
        model = new FilterModel();
        model.setHeader("Cusines");
        model.setFilters(filters);
        modelList.add(model);

        modelListReference.clear();
        modelListReference.addAll(modelList);
        adapter.notifyDataSetChanged();

    }

}
