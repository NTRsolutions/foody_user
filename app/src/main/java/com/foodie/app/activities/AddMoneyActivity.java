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
import com.foodie.app.model.PaymentMethod;
import com.foodie.app.model.WalletHistory;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_amount);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        customDialog = new CustomDialog(context);
        title.setText(context.getResources().getString(R.string.add_money));

        ArrayList<PaymentMethod> list = new ArrayList<>();
        list.add(new PaymentMethod("5431-XXXX-XXXX-4242", 0));
        AccountPaymentAdapter adbPerson = new AccountPaymentAdapter(this, list);
        paymentMethodLv.setAdapter(adbPerson);

        amountTxt.setHint(numberFormat.getCurrency().getSymbol());

        //getCardList();
    }


    //TODO update getCards API and the recyclerview
    private void getCardList() {
        customDialog.show();
        Call<List<WalletHistory>> call = apiInterface.getWalletHistory();
        call.enqueue(new Callback<List<WalletHistory>>() {
            @Override
            public void onResponse(Call<List<WalletHistory>> call, Response<List<WalletHistory>> response) {
                customDialog.dismiss();
                if (response != null) {
                    if (!response.isSuccessful() && response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else if (response.isSuccessful()) {
                        Log.e("onResponse: ", response.toString());
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<WalletHistory>> call, Throwable t) {
                customDialog.dismiss();
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
