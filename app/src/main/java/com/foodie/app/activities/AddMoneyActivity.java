package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.adapter.AccountPaymentAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.models.Card;
import com.foodie.app.models.PaymentMethod;
import com.foodie.app.models.WalletHistory;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.foodie.app.helper.GlobalData.cardArrayList;

public class AddMoneyActivity extends AppCompatActivity {

    public static final String TAG = "AddMoneyActivity";

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    Context context = AddMoneyActivity.this;
    CustomDialog customDialog;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.promo_layout)
    RelativeLayout promoLayout;
    @BindView(R.id.payment_method_lv)
    ListView paymentMethodLv;

    NumberFormat numberFormat = GlobalData.getNumberFormat();
    @BindView(R.id.amount_txt)
    EditText amountTxt;
    @BindView(R.id.pay_btn)
    Button payBtn;

    public  static AccountPaymentAdapter accountPaymentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_amount);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        customDialog = new CustomDialog(context);
        title.setText(context.getResources().getString(R.string.add_money));
        cardArrayList = new ArrayList<>();
        accountPaymentAdapter = new AccountPaymentAdapter(context, cardArrayList,false);
        paymentMethodLv.setAdapter(accountPaymentAdapter);
        amountTxt.setHint(numberFormat.getCurrency().getSymbol());

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCardList();
    }

    //TODO update getCards API and the recyclerview
    private void getCardList() {
        customDialog.show();
        Call<List<Card>> call= apiInterface.getCardList();
        call.enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                customDialog.dismiss();
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    cardArrayList.clear();
                    cardArrayList.addAll(response.body());
                    accountPaymentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,WalletActivity.class));
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick({R.id.back, R.id.promo_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.promo_layout:
                startActivity(new Intent(this, PromotionActivity.class).putExtra("tag",TAG ));
                finish();
                break;
        }
    }
}
