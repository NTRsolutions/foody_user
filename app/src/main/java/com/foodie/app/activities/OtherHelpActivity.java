package com.foodie.app.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.foodie.app.Pubnub.ChatFragment;
import com.foodie.app.R;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.model.Order;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherHelpActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.chat_us)
    Button chatUs;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @BindView(R.id.reason_title)
    TextView reasonTitle;
    @BindView(R.id.reason_description)
    TextView reasonDescription;
    @BindView(R.id.dispute)
    Button dispute;
    @BindView(R.id.order_id_txt)
    TextView orderIdTxt;
    @BindView(R.id.order_item_txt)
    TextView orderItemTxt;

    int priceAmount = 0;
    int itemQuantity = 0;
    String currency = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_help);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        String reason = getIntent().getExtras().getString("type");
        Order order = GlobalData.getInstance().isSelectedOrder;
        itemQuantity = order.getInvoice().getQuantity();
        priceAmount = order.getInvoice().getNet();
        currency = order.getItems().get(0).getProduct().getPrices().getCurrency();
        if (itemQuantity == 1)
            orderItemTxt.setText(String.valueOf(itemQuantity) + " Item, " + currency + String.valueOf(priceAmount));
        else
            orderItemTxt.setText(String.valueOf(itemQuantity) + " Items, " + currency + String.valueOf(priceAmount));
        orderIdTxt.setText("ORDER #000" + order.getId().toString());
        reasonTitle.setText(reason);
        //Toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @OnClick({R.id.chat_us, R.id.dispute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chat_us:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chat_fragment, new ChatFragment(), "Tamil");
                fragmentTransaction.commit();

                break;
            case R.id.dispute:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }
}
