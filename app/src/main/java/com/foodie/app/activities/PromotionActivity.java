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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.adapter.PromotionsAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.model.PromotionResponse;
import com.foodie.app.model.Promotions;
import com.foodie.app.model.WalletHistory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PromotionActivity extends AppCompatActivity implements PromotionsAdapter.PromotionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.promotions_rv)
    RecyclerView promotionsRv;

    ArrayList<Promotions> promotionsModelArrayList;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    Context context = PromotionActivity.this;
    CustomDialog customDialog;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        title.setText(context.getResources().getString(R.string.promocode));

        promotionsModelArrayList = new ArrayList<>();
        customDialog = new CustomDialog(context);

        //Offer Restaurant Adapter
        promotionsRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        promotionsRv.setItemAnimator(new DefaultItemAnimator());
        promotionsRv.setHasFixedSize(true);
        PromotionsAdapter orderItemListAdapter = new PromotionsAdapter(promotionsModelArrayList, this);
        promotionsRv.setAdapter(orderItemListAdapter);

        getPromoDetails();
    }

    private void getPromoDetails() {
        customDialog.show();
        Call<List<Promotions>> call = apiInterface.getWalletPromoCode();
        call.enqueue(new Callback<List<Promotions>>() {
            @Override
            public void onResponse(Call<List<Promotions>> call, Response<List<Promotions>> response) {
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
                        promotionsModelArrayList.addAll(response.body());
                        promotionsRv.getAdapter().notifyDataSetChanged();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Promotions>> call, Throwable t) {
                customDialog.dismiss();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,AddMoneyActivity.class));
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onApplyBtnClick(Promotions promotions) {
        customDialog.show();
        Call<PromotionResponse> call = apiInterface.applyWalletPromoCode(String.valueOf(promotions.getId()));
        call.enqueue(new Callback<PromotionResponse>() {
            @Override
            public void onResponse(Call<PromotionResponse> call, Response<PromotionResponse> response) {
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
                        CommonClass.profileModel.setWalletBalance(response.body().getWalletMoney());
                        gotoFlow();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<PromotionResponse> call, Throwable t) {
                customDialog.dismiss();
            }
        });
    }

    private void gotoFlow() {
        startActivity(new Intent(this,WalletActivity.class));
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
        finish();
    }
}
