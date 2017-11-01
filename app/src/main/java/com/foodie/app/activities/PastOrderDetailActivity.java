package com.foodie.app.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.fragments.OrderViewFragment;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.models.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.foodie.app.helper.GlobalData.isSelectedOrder;

public class PastOrderDetailActivity extends AppCompatActivity {


    Fragment orderFullViewFragment;
    FragmentManager fragmentManager;


    Double priceAmount = 0.0;
    int discount = 0;
    int itemCount = 0;
    int itemQuantity = 0;
    String currency = "";
    @BindView(R.id.order_id_txt)
    TextView orderIdTxt;
    @BindView(R.id.order_item_txt)
    TextView orderItemTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.source_image)
    ImageView sourceImage;
    @BindView(R.id.restaurant_name)
    TextView restaurantName;
    @BindView(R.id.restaurant_address)
    TextView restaurantAddress;
    @BindView(R.id.source_layout)
    RelativeLayout sourceLayout;
    @BindView(R.id.dot_line)
    View dotLine;
    @BindView(R.id.destination_image)
    ImageView destinationImage;
    @BindView(R.id.user_address_title)
    TextView userAddressTitle;
    @BindView(R.id.user_address)
    TextView userAddress;
    @BindView(R.id.destination_layout)
    RelativeLayout destinationLayout;
    @BindView(R.id.view_line2)
    View viewLine2;
    @BindView(R.id.order_succeess_image)
    ImageView orderSucceessImage;
    @BindView(R.id.order_status_txt)
    TextView orderStatusTxt;
    @BindView(R.id.order_status_layout)
    RelativeLayout orderStatusLayout;
    @BindView(R.id.order_detail_fargment)
    FrameLayout orderDetailFargment;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
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
        if (isSelectedOrder != null) {
            Order order = GlobalData.getInstance().isSelectedOrder;
            orderIdTxt.setText("ORDER #000" + order.getId().toString());
            itemQuantity = order.getInvoice().getQuantity();
            priceAmount = order.getInvoice().getNet();
            if(order.getStatus().equalsIgnoreCase("CANCELLED")){
                orderStatusTxt.setText(getResources().getString(R.string.order_cancelled));
                orderSucceessImage.setVisibility(View.GONE);
                orderStatusTxt.setTextColor(getResources().getColor(R.color.colorRed));
            }else{
                orderStatusTxt.setText(getResources().getString(R.string.order_delivered_successfully_on)+getFormatTime(order.getOrdertiming().get(7).getCreatedAt()));
                orderStatusTxt.setTextColor(getResources().getColor(R.color.colorGreen));
                orderSucceessImage.setVisibility(View.VISIBLE);
            }
            currency = order.getItems().get(0).getProduct().getPrices().getCurrency();
            if (itemQuantity == 1)
                orderItemTxt.setText(String.valueOf(itemQuantity) + " Item, " + currency + String.valueOf(priceAmount));
            else
                orderItemTxt.setText(String.valueOf(itemQuantity) + " Items, " + currency + String.valueOf(priceAmount));

            restaurantName.setText(order.getShop().getName());
            restaurantAddress.setText(order.getShop().getAddress());
            userAddressTitle.setText(order.getAddress().getType());
            userAddress.setText(order.getAddress().getMapAddress());

            //set Fragment
            orderFullViewFragment = new OrderViewFragment();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.order_detail_fargment, orderFullViewFragment).commit();
        }

    }

    private String getFormatTime(String time) {
        System.out.println("Time : " + time);
        String value = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm aa");

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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }
}
