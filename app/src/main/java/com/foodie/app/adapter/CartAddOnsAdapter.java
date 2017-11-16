package com.foodie.app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.fragments.AddonBottomSheetFragment;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.models.AddCart;
import com.foodie.app.models.Addon;
import com.foodie.app.models.Cart;
import com.foodie.app.models.CartAddon;
import com.foodie.app.models.Product;
import com.foodie.app.models.Shop;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.List;

import static com.foodie.app.helper.GlobalData.isSelectedProduct;

/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class CartAddOnsAdapter extends RecyclerView.Adapter<CartAddOnsAdapter.MyViewHolder> {
    public static List<Addon> list;
    private Context context;
    int priceAmount = 0;
    int discount = 0;
    int itemCount = 0;
    int itemQuantity = 0;
    Addon addon;
    boolean dataResponse = false;
    Cart productList;

    AddCart addCart;
    AnimatedVectorDrawableCompat avdProgress;
    Dialog dialog;
    Runnable action;
    Shop selectedShop = GlobalData.getInstance().selectedShop;

    //Animation number
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();

    public CartAddOnsAdapter(List<Addon> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_ons_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public void add(Addon item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Cart item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.cardAddTextLayout.setVisibility(View.VISIBLE);
        holder.cardAddDetailLayout.setVisibility(View.GONE);
        addon = list.get(position);
        holder.cardTextValueTicker.setCharacterList(NUMBER_LIST);
        holder.addonName.setText(addon.getAddon().getName() + " " + GlobalData.currencySymbol + list.get(position).getPrice());
        addon.setQuantity(1);
        holder.cardTextValue.setText("1");
        holder.cardTextValueTicker.setText("1");
        addon.getAddon().setChecked(false);
        holder.addonName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                addon = list.get(position);
                if (checked) {
                    holder.cardAddDetailLayout.setVisibility(View.VISIBLE);
                    holder.cardAddTextLayout.setVisibility(View.GONE);
                    addon.getAddon().setChecked(true);
                    setAddOnsText();
                } else {
                    holder.cardAddDetailLayout.setVisibility(View.GONE);
                    holder.cardAddTextLayout.setVisibility(View.VISIBLE);
                    addon.getAddon().setChecked(false);
                    setAddOnsText();
                }
            }
        });

        holder.cardAddTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Press Add Card Text Layout */
                addon = list.get(position);
                holder.cardAddDetailLayout.setVisibility(View.VISIBLE);
                holder.cardAddTextLayout.setVisibility(View.GONE);
                holder.cardTextValue.setText("1");
                holder.cardTextValueTicker.setText("1");
                addon.setQuantity(1);
                holder.addonName.setChecked(true);
                addon.getAddon().setChecked(true);
                setAddOnsText();
            }
        });

        holder.cardAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("access_token2", GlobalData.getInstance().accessToken);
                /** Press Add Card Add button */
                addon = list.get(position);
                addon.getAddon().setChecked(true);
                int countValue = Integer.parseInt(holder.cardTextValue.getText().toString()) + 1;
                holder.cardTextValue.setText("" + countValue);
                holder.cardTextValueTicker.setText("" + countValue);
                addon.setQuantity(countValue);
                setAddOnsText();
            }
        });

        holder.cardMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int countMinusValue;
                /** Press Add Card Minus button */
                addon = list.get(position);
                if (holder.cardTextValue.getText().toString().equalsIgnoreCase("1")) {
                    holder.cardAddDetailLayout.setVisibility(View.GONE);
                    holder.cardAddTextLayout.setVisibility(View.VISIBLE);
                    holder.addonName.setChecked(false);
                    addon.getAddon().setChecked(false);

                } else {
                    countMinusValue = Integer.parseInt(holder.cardTextValue.getText().toString()) - 1;
                    holder.cardTextValue.setText("" + countMinusValue);
                    holder.cardTextValueTicker.setText("" + countMinusValue);
                    addon.setQuantity(countMinusValue);

                }
                setAddOnsText();
            }
        });

        if (GlobalData.cartAddons != null) {
            List<CartAddon> cartAddons = GlobalData.cartAddons;
            for (CartAddon cartAddon : cartAddons) {
                System.out.println("addon " + addon.getAddon().getId() + " ==" + cartAddon.getAddonProduct().getAddon().getId());
                if (addon.getAddon().getId().equals(cartAddon.getAddonProduct().getAddon().getId())) {
                    System.out.println(addon.getAddon().getId() + " = " + cartAddon.getAddonProduct().getAddon().getId());
                    holder.addonName.setChecked(true);
                    addon.getAddon().setChecked(true);
                    setAddOnsText();
                }
            }
        }
    }

    private void setAddOnsText() {
        AddonBottomSheetFragment.addons.setText("");
        if (AddonBottomSheetFragment.selectedCart != null) {
            Product product = AddonBottomSheetFragment.selectedCart.getProduct();
            int quantity = AddonBottomSheetFragment.selectedCart.getQuantity();
            priceAmount = AddonBottomSheetFragment.selectedCart.getQuantity() * product.getPrices().getPrice();
            if (AddonBottomSheetFragment.selectedCart.getCartAddons() != null && !AddonBottomSheetFragment.selectedCart.getCartAddons().isEmpty()) {
                for (int j = 0; j < AddonBottomSheetFragment.selectedCart.getCartAddons().size(); j++) {
                    priceAmount = priceAmount + (AddonBottomSheetFragment.selectedCart.getQuantity() * (AddonBottomSheetFragment.selectedCart.getCartAddons().get(j).getQuantity() *
                            AddonBottomSheetFragment.selectedCart.getCartAddons().get(j).getAddonProduct().getPrice()));
                }
            }
            if (quantity == 0)
                AddonBottomSheetFragment.price.setText(String.valueOf(quantity) + " Item | " + GlobalData.currencySymbol + priceAmount);
            else
                AddonBottomSheetFragment.price.setText(String.valueOf(quantity) + " Items | " + GlobalData.currencySymbol + priceAmount);
        } else {
            int totalAmount = isSelectedProduct.getPrices().getPrice();
            for (Addon addon : list) {
                if (addon.getAddon().getChecked()) {
                    totalAmount = totalAmount + (addon.getQuantity() * addon.getPrice());
                    AddonBottomSheetFragment.addons.append(addon.getAddon().getName() + ", ");
                }
            }
            AddonBottomSheetFragment.price.setText("1 Item | " + GlobalData.currencySymbol + totalAmount);
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView dishImg, foodImageType, cardAddBtn, cardMinusBtn, animationLineCartAdd;
        private TextView cardTextValue, cardAddInfoText, cardAddOutOfStock;
        TickerView cardTextValueTicker;
        CheckBox addonName;
        RelativeLayout cardAddDetailLayout, cardAddTextLayout, cardInfoLayout, addButtonRootLayout;

        private MyViewHolder(View view) {
            super(view);
            foodImageType = (ImageView) itemView.findViewById(R.id.food_type_image);
            animationLineCartAdd = (ImageView) itemView.findViewById(R.id.animation_line_cart_add);
            addonName = (CheckBox) itemView.findViewById(R.id.dish_name_text);
         /*    Add card Button Layout*/
            cardAddDetailLayout = (RelativeLayout) itemView.findViewById(R.id.add_card_layout);
            addButtonRootLayout = (RelativeLayout) itemView.findViewById(R.id.add_button_root_layout);
            cardAddTextLayout = (RelativeLayout) itemView.findViewById(R.id.add_card_text_layout);
            cardAddInfoText = (TextView) itemView.findViewById(R.id.avialablity_time);
            cardAddOutOfStock = (TextView) itemView.findViewById(R.id.out_of_stock);
            cardAddBtn = (ImageView) itemView.findViewById(R.id.card_add_btn);
            cardMinusBtn = (ImageView) itemView.findViewById(R.id.card_minus_btn);
            cardTextValue = (TextView) itemView.findViewById(R.id.card_value);
            cardTextValueTicker = (TickerView) itemView.findViewById(R.id.card_value_ticker);
        }


    }

}
