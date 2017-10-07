package com.foodie.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.foodie.app.R;
import com.foodie.app.adapter.PromotionsAdapter;
import com.foodie.app.model.Promotions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PromotionActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.promotions_rv)
    RecyclerView promotionsRv;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        ButterKnife.bind(this);


        //Toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setContentInsetsAbsolute(toolbar.getContentInsetLeft(), 0);

        final ArrayList<Promotions> promotionsModelArrayList = new ArrayList<>();
        promotionsModelArrayList.add(new Promotions("12-5-2017 5:00AM", "AC1234 Applied", "$40"));
        promotionsModelArrayList.add(new Promotions("26-6-2017 6:30PM", "DJ1234 Applied", "$80"));

        //Offer Restaurant Adapter
        promotionsRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        promotionsRv.setItemAnimator(new DefaultItemAnimator());
        promotionsRv.setHasFixedSize(true);
        PromotionsAdapter orderItemListAdapter = new PromotionsAdapter(promotionsModelArrayList, context);
        promotionsRv.setAdapter(orderItemListAdapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }
}
