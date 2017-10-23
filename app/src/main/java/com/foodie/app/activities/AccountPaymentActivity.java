package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.adapter.AccountPaymentAdapter;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.model.PaymentMethod;

import java.text.NumberFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.foodie.app.helper.GlobalData.currencySymbol;

public class AccountPaymentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.payment_method_lv)
    ListView paymentMethodLv;
    @BindView(R.id.wallet_amount_txt)
    TextView walletAmtTxt;
    @BindView(R.id.wallet_layout)
    RelativeLayout walletLayout;

    NumberFormat numberFormat = GlobalData.getNumberFormat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_payment);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ArrayList<PaymentMethod> list = new ArrayList<>();
        list.add(new PaymentMethod("5431-XXXX-XXXX-4242", 0));
        AccountPaymentAdapter adbPerson = new AccountPaymentAdapter(AccountPaymentActivity.this, list);
        paymentMethodLv.setAdapter(adbPerson);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

    @OnClick(R.id.wallet_layout)
    public void onViewClicked() {
        startActivity(new Intent(this, WalletActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int walletMoney = GlobalData.profileModel.getWalletBalance();
        walletAmtTxt.setText(currencySymbol+" "+String.valueOf(walletMoney));
    }

}
