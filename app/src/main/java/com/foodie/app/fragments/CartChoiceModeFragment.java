package com.foodie.app.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.activities.ProductDetailActivity;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.models.Product;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by santhosh@appoets.com on 13-11-2017.
 */

public class CartChoiceModeFragment extends BottomSheetDialogFragment {

    Context context;
    Activity activity;
    Product product;
    @BindView(R.id.add_ons_items_txt)
    TextView addOnsItemsTxt;
    @BindView(R.id.add_ons_qty)
    TextView addOnsQty;
    @BindView(R.id.i_will_choose_btn)
    Button iWillChooseBtn;
    @BindView(R.id.repeat_btn)
    Button repeatBtn;
    Unbinder unbinder;
    @BindView(R.id.food_type)
    ImageView foodType;
    @BindView(R.id.product_name)
    TextView productName;
    @BindView(R.id.product_price)
    TextView productPrice;
    String addOnsValue="";

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.cart_choice_mode_fragment, null);
        dialog.setContentView(contentView);
        ButterKnife.bind(this, contentView);
        context = getContext();
        activity = getActivity();

        if (GlobalData.isSelectedProduct != null) {
            Product product = GlobalData.isSelectedProduct;
            productName.setText(product.getName());
            productPrice.setText(product.getPrices().getCurrency() + " " + product.getPrices().getPrice());
            for (int i = 0; i <product.getCart().get(0).getCartAddons().size() ; i++) {
                if(i==0)
                    addOnsItemsTxt.setText(product.getCart().get(0).getCartAddons().get(i).getAddonProduct().getAddon().getName());
                else
                    addOnsItemsTxt.append(", "+product.getCart().get(0).getCartAddons().get(i).getAddonProduct().getAddon().getName());
            }

        }




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @OnClick({R.id.i_will_choose_btn, R.id.repeat_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.i_will_choose_btn:
                context.startActivity(new Intent(context, ProductDetailActivity.class));
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
                break;
            case R.id.repeat_btn:

                break;
        }
    }
}
