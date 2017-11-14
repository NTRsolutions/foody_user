package com.foodie.app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.adapter.CartAddOnsAdapter;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.models.AddCart;
import com.foodie.app.models.Addon;
import com.foodie.app.models.Product;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.foodie.app.activities.AccountPaymentActivity.apiInterface;
import static com.foodie.app.adapter.CartAddOnsAdapter.list;


/**
 * Created by santhosh@appoets.com on 13-11-2017.
 */

public class AddonBottomSheetFragment extends BottomSheetDialogFragment {

    @BindView(R.id.add_ons_rv)
    RecyclerView addOnsRv;

    Context context;
    List<Addon> addonList;
    @BindView(R.id.food_type)
    ImageView foodType;
    @BindView(R.id.product_name)
    TextView productName;
    @BindView(R.id.product_price)
    TextView productPrice;

    public static TextView addons;
    public static TextView price;

    @BindView(R.id.update)
    TextView update;

    Unbinder unbinder;

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.addon_bottom_sheet_fragment, null);
        dialog.setContentView(contentView);
        ButterKnife.bind(this, contentView);
        context = getContext();
        addons = (TextView) contentView.findViewById(R.id.addons);
        price = (TextView) contentView.findViewById(R.id.price);
        addOnsRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        addOnsRv.setItemAnimator(new DefaultItemAnimator());
        addOnsRv.setHasFixedSize(false);
        addOnsRv.setNestedScrollingEnabled(false);


        addonList = new ArrayList<>();

        CartAddOnsAdapter addOnsAdapter = new CartAddOnsAdapter(addonList, context);
        addOnsRv.setAdapter(addOnsAdapter);

        if (GlobalData.isSelectedProduct != null) {
            Product product = GlobalData.isSelectedProduct;
            productName.setText(product.getName());
            productPrice.setText(product.getPrices().getCurrency() + " " + product.getPrices().getPrice());

            addonList.clear();
            addonList.addAll(product.getAddons());
            addOnsRv.getAdapter().notifyDataSetChanged();

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Product product = GlobalData.isSelectedProduct;
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("product_id", product.getId().toString());
                    map.put("quantity",String.valueOf(GlobalData.isSelctedCart.getQuantity()));
                    map.put("cart_id", String.valueOf(GlobalData.isSelctedCart.getId()));

                    for (int i = 0; i < list.size(); i++) {
                        Addon addon = list.get(i);
                        if (addon.getAddon().getChecked()) {
                            map.put("product_addons[" + "" + i + "]", addon.getId().toString());
                            map.put("addons_qty[" + "" + i + "]", addon.getQuantity().toString());
                        }
                    }
                    Log.e("AddCart_add", map.toString());
                    addItem(map);
                }
            });
        }

    }

    private void addItem(HashMap<String, String> map) {
        Call<AddCart> call = apiInterface.postAddCart(map);
        call.enqueue(new Callback<AddCart>() {
            @Override
            public void onResponse(@Nonnull Call<AddCart> call,@Nonnull Response<AddCart> response) {
                if (response.isSuccessful()) {
                    GlobalData.addCart = response.body();
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull Call<AddCart> call, @Nonnull Throwable t) {
                Toast.makeText(getContext(), "Something wrong - addItem AddonBottomSheetFragment", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
