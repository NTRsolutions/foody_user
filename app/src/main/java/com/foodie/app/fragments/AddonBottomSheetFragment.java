package com.foodie.app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.adapter.AddOnsAdapter;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.models.Addon;
import com.foodie.app.models.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.addon_bottom_sheet_fragment, null);
        dialog.setContentView(contentView);
        ButterKnife.bind(this, contentView);
        context = getContext();

        addOnsRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        addOnsRv.setItemAnimator(new DefaultItemAnimator());
        addOnsRv.setHasFixedSize(false);
        addOnsRv.setNestedScrollingEnabled(false);


        addonList = new ArrayList<>();

        AddOnsAdapter addOnsAdapter = new AddOnsAdapter(addonList, context);
        addOnsRv.setAdapter(addOnsAdapter);

        if (GlobalData.isSelectedProduct != null) {
            Product product = GlobalData.isSelectedProduct;
            productName.setText(product.getName());
            productPrice.setText(product.getPrices().getCurrency() + " " + product.getPrices().getPrice());

            addonList.clear();
            addonList.addAll(product.getAddons());
            addOnsRv.getAdapter().notifyDataSetChanged();

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
