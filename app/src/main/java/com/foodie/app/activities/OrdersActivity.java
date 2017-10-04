package com.foodie.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.foodie.app.R;
import com.foodie.app.adapter.OrdersAdapter;
import com.foodie.app.helper.SlideUpItemAnimator;
import com.foodie.app.model.Order;
import com.foodie.app.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.orders_rv)
    RecyclerView ordersRv;

    private OrdersAdapter adapter;
    Activity activity = OrdersActivity.this;
    private List<OrderModel> modelListReference = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0, 0);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        ordersRv.setLayoutManager(manager);
        adapter = new OrdersAdapter(this, activity, modelListReference);
        ordersRv.setAdapter(adapter);
        ordersRv.setHasFixedSize(false);
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(OrdersActivity.this, R.anim.item_animation_slide_right);
        ordersRv.setLayoutAnimation(controller);
        ordersRv.scheduleLayoutAnimation();

    }

    @Override
    public void onResume() {
        super.onResume();

        List<OrderModel> modelList = new ArrayList<>();

        List<Order> orders = new ArrayList<>();
        orders.add(new Order("Funkie", "Washington, DC", "$250", "Noodles 1", "29, Aug, 2017"));
        orders.add(new Order("Funkie", "Washington, DC", "$250", "Noodles 1", "29, Aug, 2017"));
        orders.add(new Order("Funkie", "Washington, DC", "$250", "Noodles 1", "29, Aug, 2017"));
        orders.add(new Order("Funkie", "Washington, DC", "$250", "Noodles 1", "29, Aug, 2017"));
        OrderModel model = new OrderModel();
        model.setHeader("Current Orders");
        model.setOrders(orders);
        modelList.add(model);

        orders = new ArrayList<>();
        orders.add(new Order("Funkie", "Washington, DC", "$50", "Dosa 2", "28, Aug, 2017"));
        orders.add(new Order("Funkie", "Washington, DC", "$71", "Rotti 3", "26, Aug, 2017"));
        orders.add(new Order("Funkie", "Washington, DC", "$132", "Biriyani", "26, Aug, 2017"));
        model = new OrderModel();
        model.setHeader("Past Orders");
        model.setOrders(orders);
        modelList.add(model);

        modelListReference.clear();
        modelListReference.addAll(modelList);
        ordersRv.getAdapter().notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

}
