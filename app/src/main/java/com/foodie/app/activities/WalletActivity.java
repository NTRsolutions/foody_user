package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.adapter.AccountPaymentAdapter;
import com.foodie.app.adapter.WalletHistoryAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.helper.CustomDialog;
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

public class WalletActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.add_btn)
    Button addBtn;
    @BindView(R.id.wallet_amount_txt)
    TextView walletAmountTxt;
    @BindView(R.id.wallet_history_recycler_view)
    RecyclerView walletHistoryRecyclerView;

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    Context context = WalletActivity.this;
    CustomDialog customDialog;

    List<WalletHistory> walletHistoryHistoryList;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;

    NumberFormat numberFormat = CommonClass.getNumberFormat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        customDialog = new CustomDialog(context);


        title.setText(context.getResources().getString(R.string.walletHistory));
        walletHistoryHistoryList = new ArrayList<>();
        walletHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        walletHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        walletHistoryRecyclerView.setHasFixedSize(true);
        WalletHistoryAdapter walletHistoryAdapter = new WalletHistoryAdapter(walletHistoryHistoryList);
        walletHistoryRecyclerView.setAdapter(walletHistoryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int walletMoney = CommonClass.profileModel.getWalletBalance();
        if (walletMoney > 0) {
            walletAmountTxt.setText(numberFormat.format(walletMoney));
        }
        getWalletHistory();
    }

    private void getWalletHistory() {
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
                        walletHistoryHistoryList.addAll(response.body());
                        walletHistoryRecyclerView.getAdapter().notifyDataSetChanged();
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
        startActivity(new Intent(this,AccountPaymentActivity.class));
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

    @OnClick(R.id.add_btn)
    public void onViewClicked() {
        startActivity(new Intent(this, AddMoneyActivity.class));
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.back)
    public void onBackClicked() {
        onBackPressed();
    }
}
