package com.foodie.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.adapter.OrdersAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.ConnectionHelper;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.models.Order;
import com.foodie.app.models.OrderModel;
import com.foodie.app.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.foodie.app.helper.GlobalData.onGoingOrderList;
import static com.foodie.app.helper.GlobalData.pastOrderList;

public class OrdersActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.orders_rv)
    RecyclerView ordersRv;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    private OrdersAdapter adapter;
    Activity activity = OrdersActivity.this;
    private List<OrderModel> modelListReference = new ArrayList<>();

    Context context;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    List<OrderModel> modelList = new ArrayList<>();
    Intent orderIntent;
    ConnectionHelper connectionHelper;
    Handler handler;
    Runnable orderStatusRunnable;
    CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = OrdersActivity.this;
        customDialog = new CustomDialog(context);
        ButterKnife.bind(this);
        connectionHelper = new ConnectionHelper(context);
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
        handler = new Handler();
        orderStatusRunnable = new Runnable() {
            public void run() {
                getOngoingOrders();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(orderStatusRunnable, 5000);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        ordersRv.setLayoutManager(manager);
        modelListReference.clear();
        adapter = new OrdersAdapter(this, activity, modelListReference);
        ordersRv.setAdapter(adapter);
        ordersRv.setHasFixedSize(false);
        onGoingOrderList = new ArrayList<>();
        if (connectionHelper.isConnectingToInternet()) {
            customDialog.show();
            getOngoingOrders();
        } else {
            Utils.displayMessage(activity, context, getString(R.string.oops_connect_your_internet));
        }

    }


    private void getPastOrders() {
        Call<List<Order>> call = apiInterface.getPastOders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                customDialog.dismiss();
                if (response.isSuccessful()) {
                    pastOrderList = new ArrayList<>();
                    pastOrderList = response.body();
                    OrderModel model = new OrderModel();
                    model.setHeader(getString(R.string.past_orders));
                    model.setOrders(pastOrderList);
                    modelList.add(model);
                    modelListReference.clear();
                    modelListReference.addAll(modelList);
//                    LayoutAnimationController controller =
//                            AnimationUtils.loadLayoutAnimation(OrdersActivity.this, R.anim.item_animation_slide_right);
//                    ordersRv.setLayoutAnimation(controller);
//                    ordersRv.scheduleLayoutAnimation();
                    adapter.notifyDataSetChanged();
                    if (onGoingOrderList.size() == 0 && pastOrderList.size() == 0) {
                        errorLayout.setVisibility(View.VISIBLE);
                    } else
                        errorLayout.setVisibility(View.GONE);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Order>> call, @NonNull Throwable t) {
                customDialog.dismiss();
                Toast.makeText(OrdersActivity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getOngoingOrders() {

        Call<List<Order>> call = apiInterface.getOngoingOrders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() == 0) {
                        getPastOrders();
                    } else if (onGoingOrderList.size() != response.body().size()) {
                        onGoingOrderList.clear();
                        onGoingOrderList.addAll(response.body());
                        OrderModel model = new OrderModel();
                        model.setHeader("Current Orders");
                        model.setOrders(onGoingOrderList);
                        modelList.add(model);
                        modelListReference.clear();
                        modelListReference.addAll(modelList);
                        if (onGoingOrderList.size() == 0 && pastOrderList.size() == 0) {
                            errorLayout.setVisibility(View.VISIBLE);
                        } else
                            errorLayout.setVisibility(View.GONE);
                        getPastOrders();
                    }
                } else {
                    getPastOrders();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Order>> call, @NonNull Throwable t) {
                Toast.makeText(OrdersActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                getPastOrders();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(orderStatusRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        //Get Ongoing Order list
        handler.postDelayed(orderStatusRunnable, 5000);

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
        handler.removeCallbacks(orderStatusRunnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(orderStatusRunnable);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
