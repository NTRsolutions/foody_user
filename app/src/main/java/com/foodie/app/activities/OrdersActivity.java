package com.foodie.app.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.adapter.OrdersAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.model.Order;
import com.foodie.app.model.OrderFlow;
import com.foodie.app.model.OrderModel;
import com.foodie.app.service.OrderStatusService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.foodie.app.helper.CommonClass.onGoingOrderList;
import static com.foodie.app.helper.CommonClass.pastOrderList;

public class OrdersActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.orders_rv)
    RecyclerView ordersRv;

    private OrdersAdapter adapter;
    Activity activity = OrdersActivity.this;
    private List<OrderModel> modelListReference = new ArrayList<>();

    Context context;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    List<OrderModel> modelList = new ArrayList<>();
    Handler handler;
    Intent orderIntent;

    int ONGOING_ORDER_LIST_SIZE=0;
    int PAST_ORDER_LIST_SIZE=0;
    private BroadcastReceiver mReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        context=OrdersActivity.this;

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("ONGOING"));
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
        modelListReference.clear();
        adapter = new OrdersAdapter(this, activity, modelListReference);
        ordersRv.setAdapter(adapter);
        ordersRv.setHasFixedSize(false);
        orderIntent = new Intent(context, OrderStatusService.class);
        orderIntent.putExtra("type", "ORDER_LIST");
        startService(orderIntent);
        handler = new Handler();
        getOngoingOrders();



    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            modelList.clear();
            OrderModel onGoingOrderModel = new OrderModel();
            onGoingOrderModel.setHeader("Current Orders");
            onGoingOrderModel.setOrders(onGoingOrderList);
            modelList.add(onGoingOrderModel);
            OrderModel pastOrderModel = new OrderModel();
            pastOrderModel.setHeader("Past Orders");
            pastOrderModel.setOrders(pastOrderList);
            modelList.add(pastOrderModel);
            modelListReference.clear();
            modelListReference.addAll(modelList);
            runOnUiThread(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
            Log.d("receiver", "Success");
        }
    };
//    private  void  runHandler(){
//        Runnable orderStatusRunnable = new Runnable() {
//            public void run() {
//                if (onGoingOrderList != null && pastOrderList != null) {
////                    if( ONGOING_ORDER_LIST_SIZE!=onGoingOrderList.size()){
//                        ONGOING_ORDER_LIST_SIZE=onGoingOrderList.size();
//                        PAST_ORDER_LIST_SIZE=pastOrderList.size();
//                        modelList.clear();
//                        OrderModel onGoingOrderModel = new OrderModel();
//                        onGoingOrderModel.setHeader("Current Orders");
//                        onGoingOrderModel.setOrders(onGoingOrderList);
//                        modelList.add(onGoingOrderModel);
//                        OrderModel pastOrderModel = new OrderModel();
//                        pastOrderModel.setHeader("Past Orders");
//                        pastOrderModel.setOrders(pastOrderList);
//                        modelList.add(pastOrderModel);
//                        modelListReference.clear();
//                        modelListReference.addAll(modelList);
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                adapter.notifyDataSetChanged();
//                            }
//                        });
//
////                    }
//                }
//                handler.postDelayed(this, 2000);
//
//
//            }
//        };
//        handler.postDelayed(orderStatusRunnable, 2000);
//    }
    private void getPastOrders() {
        Call<List<Order>> call = apiInterface.getPastOders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    pastOrderList = new ArrayList<Order>();
                    pastOrderList = response.body();
                    OrderModel model = new OrderModel();
                    model.setHeader("Past Orders");
                    model.setOrders(pastOrderList);
                    modelList.add(model);
                    modelListReference.clear();
                    modelListReference.addAll(modelList);
                    LayoutAnimationController controller =
                            AnimationUtils.loadLayoutAnimation(OrdersActivity.this, R.anim.item_animation_slide_right);
                    ordersRv.setLayoutAnimation(controller);
                    ordersRv.scheduleLayoutAnimation();
                    adapter.notifyDataSetChanged();
//                    runHandler();


                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });

    }

    private void getOngoingOrders() {
        Call<List<Order>> call = apiInterface.getOngoingOrders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    onGoingOrderList = new ArrayList<Order>();
                    onGoingOrderList = response.body();
                    OrderModel model = new OrderModel();
                    model.setHeader("Current Orders");
                    model.setOrders(onGoingOrderList);
                    modelList.add(model);
                    modelListReference.clear();
                    modelListReference.addAll(modelList);
                    getPastOrders();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(orderIntent);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        startService(orderIntent);
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("ONGOING"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(orderIntent);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

}
