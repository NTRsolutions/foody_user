package com.foodie.app.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.adapter.OrderFlowAdapter;
import com.foodie.app.adapter.OrdersAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.fragments.OrderViewFragment;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.model.Order;
import com.foodie.app.model.OrderFlow;
import com.foodie.app.model.OrderModel;
import com.foodie.app.service.OrderStatusService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodie.app.helper.CommonClass.ORDER_STATUS;
import static com.foodie.app.helper.CommonClass.isSelectedOrder;
import static com.foodie.app.helper.CommonClass.onGoingOrderList;
import static com.foodie.app.helper.CommonClass.pastOrderList;

public class CurrentOrderDetailActivity extends AppCompatActivity {

    @BindView(R.id.order_id_txt)
    TextView orderIdTxt;
    @BindView(R.id.order_item_txt)
    TextView orderItemTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.order_status_txt)
    TextView orderStatusTxt;
    @BindView(R.id.order_status_layout)
    RelativeLayout orderStatusLayout;
    @BindView(R.id.order_id_txt_2)
    TextView orderIdTxt2;
    @BindView(R.id.order_placed_time)
    TextView orderPlacedTime;

    Fragment orderFullViewFragment;
    FragmentManager fragmentManager;

    int priceAmount = 0;
    int discount = 0;
    int itemCount = 0;
    int itemQuantity = 0;
    String currency = "";
    @BindView(R.id.order_flow_rv)
    RecyclerView orderFlowRv;

    Context context;
    Handler handler;
    Intent orderIntent;
    OrderFlowAdapter adapter;
    String previousStatus = "";
    Runnable orderStatusRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order_detail);
        ButterKnife.bind(this);

        context = CurrentOrderDetailActivity.this;
        orderIntent = new Intent(context, OrderStatusService.class);
        orderIntent.putExtra("type", "SINGLE_ORDER");
        orderIntent.putExtra("order_id", isSelectedOrder.getId());
        startService(orderIntent);


        //set Toolbar
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

        List<OrderFlow> orderFlowList = new ArrayList<>();
        orderFlowList.add(new OrderFlow(getString(R.string.order_placed), getString(R.string.description_1), R.drawable.ic_order_placed, ORDER_STATUS.get(0)));
        orderFlowList.add(new OrderFlow(getString(R.string.order_confirmed), getString(R.string.description_2), R.drawable.ic_order_confirmed, ORDER_STATUS.get(1) + ORDER_STATUS.get(2)));
        orderFlowList.add(new OrderFlow(getString(R.string.order_processed), getString(R.string.description_3), R.drawable.ic_order_processed, ORDER_STATUS.get(3) + ORDER_STATUS.get(4)));
        orderFlowList.add(new OrderFlow(getString(R.string.order_pickedup), getString(R.string.description_4), R.drawable.ic_order_picked_up, ORDER_STATUS.get(5) + ORDER_STATUS.get(6)));
        orderFlowList.add(new OrderFlow(getString(R.string.order_delivered), getString(R.string.description_5), R.drawable.ic_order_delivered, ORDER_STATUS.get(7)));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        orderFlowRv.setLayoutManager(manager);
        adapter = new OrderFlowAdapter(orderFlowList, this);
        orderFlowRv.setAdapter(adapter);
        orderFlowRv.setHasFixedSize(false);
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.item_animation_slide_right);
        orderFlowRv.setLayoutAnimation(controller);
        orderFlowRv.scheduleLayoutAnimation();

        handler = new Handler();
        orderStatusRunnable = new Runnable() {
            public void run() {
                if (isSelectedOrder != null) {
                    if (!isSelectedOrder.getStatus().equalsIgnoreCase(previousStatus)) {
                        previousStatus = isSelectedOrder.getStatus();
                        adapter.notifyDataSetChanged();
                    }

                    handler.postDelayed(this, 5000);
                }

            }
        };
        handler.postDelayed(orderStatusRunnable, 5000);


        if (CommonClass.getInstance().isSelectedOrder != null) {
            Order order = CommonClass.getInstance().isSelectedOrder;
            orderIdTxt.setText("ORDER #000" + order.getId().toString());
            itemQuantity = order.getInvoice().getQuantity();
            priceAmount = order.getInvoice().getNet();
            currency = order.getItems().get(0).getProduct().getPrices().getCurrency();
            if (itemQuantity == 1)
                orderItemTxt.setText(String.valueOf(itemQuantity) + " Item, " + currency + String.valueOf(priceAmount));
            else
                orderItemTxt.setText(String.valueOf(itemQuantity) + " Items, " + currency + String.valueOf(priceAmount));

            orderIdTxt2.setText("#000" + order.getId().toString());
            orderPlacedTime.setText(getTimeFromString(order.getCreatedAt()));

            //set Fragment
            orderFullViewFragment = new OrderViewFragment();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.order_detail_fargment, orderFullViewFragment).commit();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CurrentOrderDetailActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

    private String getTimeFromString(String time) {
        System.out.println("Time : " + time);
        String value = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

            if (time != null) {
                Date date = df.parse(time);
                value = sdf.format(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();

        }
        return value;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(orderIntent);
        handler.removeCallbacks(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(orderIntent);
        handler.postDelayed(orderStatusRunnable, 5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(orderIntent);
        handler.removeCallbacks(null);
    }
}
