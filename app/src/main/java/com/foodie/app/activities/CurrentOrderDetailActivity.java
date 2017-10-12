package com.foodie.app.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.adapter.OrderFlowAdapter;
import com.foodie.app.adapter.OrdersAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.fragments.OrderViewFragment;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.model.Message;
import com.foodie.app.model.Order;
import com.foodie.app.model.OrderFlow;
import com.foodie.app.model.OrderModel;
import com.foodie.app.service.OrderStatusService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    int itemQuantity = 0;
    String currency = "";
    @BindView(R.id.order_flow_rv)
    RecyclerView orderFlowRv;

    Context context;
    Intent orderIntent;
    OrderFlowAdapter adapter;
    boolean isOrderPage = false;
    private BroadcastReceiver mReceiver;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order_detail);
        ButterKnife.bind(this);

        context = CurrentOrderDetailActivity.this;
        orderIntent = new Intent(context, OrderStatusService.class);
        orderIntent.putExtra("type", "SINGLE_ORDER");
        if(isSelectedOrder!=null)
        orderIntent.putExtra("order_id", isSelectedOrder.getId());
        startService(orderIntent);
        isOrderPage = getIntent().getBooleanExtra("is_order_page", false);

        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("SINGLE_ORDER"));

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
        if (isOrderPage) {
            finish();
        } else {
            startActivity(new Intent(CurrentOrderDetailActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
        }
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.notifyDataSetChanged();
            Log.d("receiver", "Success");
        }
    };

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

    private void rateTransporter(HashMap<String, String> map) {
        System.out.println(map.toString());
        Call<Message> call = apiInterface.rate(map);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                if (response.errorBody() != null) {
                    finish();
                } else if (response.isSuccessful()) {
                    Message message = response.body();
                    Toast.makeText(context, message.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Toast.makeText(context, "Something wrong - rateTransporter", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void rate() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final FrameLayout frameView = new FrameLayout(this);
            builder.setView(frameView);

            final AlertDialog alertDialog = builder.create();
            LayoutInflater inflater = alertDialog.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.feedback_popup, frameView);
            alertDialog.show();

            final Integer[] rating = {5};
            final RadioGroup rateRadioGroup = (RadioGroup) dialogView.findViewById(R.id.rate_radiogroup);
            ((RadioButton) rateRadioGroup.getChildAt(4)).setChecked(true);
            rateRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    rating[0] = i;
                }
            });

            final EditText comment = (EditText) dialogView.findViewById(R.id.comment);
            Button feedbackSubmit = (Button) dialogView.findViewById(R.id.feedback_submit);
            feedbackSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonClass.isSelectedOrder != null && CommonClass.isSelectedOrder.getId() != null) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("order_id", String.valueOf(CommonClass.isSelectedOrder.getId()));
                        map.put("rating", String.valueOf(rating[0]));
                        map.put("comment", comment.getText().toString());
                        map.put("type", "transporter");
                        rateTransporter(map);
                        alertDialog.dismiss();
                    }

                }
            });
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(orderIntent);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(orderIntent);
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter("SINGLE_ORDER"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(orderIntent);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }
}
