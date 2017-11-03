package com.foodie.app.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.fragments.CartFragment;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.models.AddCart;
import com.foodie.app.models.Product;
import com.foodie.app.models.Cart;
import com.foodie.app.models.Shop;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.foodie.app.helper.GlobalData.categoryList;

/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class ViewCartAdapter extends RecyclerView.Adapter<ViewCartAdapter.MyViewHolder> {
    private List<Cart> list;
    private Context context;
    int priceAmount = 0;
    int discount = 0;
    int itemCount = 0;
    int itemQuantity = 0;
    Product product;
    boolean dataResponse = false;
    Cart productList;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    AddCart addCart;
    AnimatedVectorDrawableCompat avdProgress;
    Dialog dialog;
    Runnable action;
    Shop selectedShop = GlobalData.getInstance().selectedShop;


    //Animation number
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();

    public ViewCartAdapter(List<Cart> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_product_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public void add(Cart item, int position) {
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
        holder.cardAddTextLayout.setVisibility(View.GONE);
        holder.cardAddDetailLayout.setVisibility(View.VISIBLE);
        product = list.get(position).getProduct();
        holder.cardTextValueTicker.setCharacterList(NUMBER_LIST);
        holder.dishNameTxt.setText(product.getName());
        holder.cardTextValue.setText(list.get(position).getQuantity().toString());
        holder.cardTextValueTicker.setText(list.get(position).getQuantity().toString());
        holder.priceTxt.setText(product.getPrices().getCurrency() + " " + list.get(position).getQuantity() * product.getPrices().getPrice());
        if (!product.getFoodType().equalsIgnoreCase("veg")) {
            holder.foodImageType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nonveg));
        } else {
            holder.foodImageType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_veg));
        }
        selectedShop=product.getShop();
//        for (int i = 0; i < GlobalData.getInstance().shopList.size(); i++) {
//            if (list.get(0).getProduct().getShopId().equals(GlobalData.getInstance().shopList.get(i).getId()))
//                selectedShop = GlobalData.getInstance().shopList.get(i);
//        }

        holder.cardAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("access_token2", GlobalData.getInstance().accessToken);
                /** Intilaize Animation View Image */
                holder.animationLineCartAdd.setVisibility(View.VISIBLE);
                //Intialize
                avdProgress = AnimatedVectorDrawableCompat.create(context, R.drawable.add_cart_avd_line);
                holder.animationLineCartAdd.setBackground(avdProgress);
                avdProgress.start();
                action = new Runnable() {
                    @Override
                    public void run() {
                        if (!dataResponse) {
                            avdProgress.start();
                            holder.animationLineCartAdd.postDelayed(action, 3000);
                        }

                    }
                };
                holder.animationLineCartAdd.postDelayed(action, 3000);

                /** Press Add Card Add button */
                product = list.get(position).getProduct();
                int countValue = Integer.parseInt(holder.cardTextValue.getText().toString()) + 1;
                holder.cardTextValue.setText("" + countValue);
                holder.cardTextValueTicker.setText("" + countValue);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("product_id", product.getId().toString());
                map.put("quantity", holder.cardTextValue.getText().toString());
                Log.e("AddCart_add", map.toString());
                addCart(map);
                int quantity = Integer.parseInt(holder.cardTextValue.getText().toString());
                priceAmount = quantity * product.getPrices().getPrice();
                holder.priceTxt.setText(product.getPrices().getCurrency() + " " + priceAmount);

                //We don't know categories means do nothing,
                if (categoryList != null) {
                    //we Know category
                    for (int i = 0; i < categoryList.size(); i++) {
                        for (int j = 0; j < categoryList.get(i).getProducts().size(); j++) {
                            if (categoryList.get(i).getProducts().get(j).getId().equals(product.getId())) {
                                categoryList.get(i).getProducts().get(j).getCart().setQuantity(countValue);
                            }
                        }
                    }
                }


            }
        });

        holder.cardMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** Intilaize Animation View Image */
                holder.animationLineCartAdd.setVisibility(View.VISIBLE);
                //Intialize
                avdProgress = AnimatedVectorDrawableCompat.create(context, R.drawable.add_cart_avd_line);
                holder.animationLineCartAdd.setBackground(avdProgress);
                avdProgress.start();
                action = new Runnable() {
                    @Override
                    public void run() {
                        if (!dataResponse) {
                            avdProgress.start();
                            holder.animationLineCartAdd.postDelayed(action, 3000);
                        }
                    }
                };
                holder.animationLineCartAdd.postDelayed(action, 3000);

                int countMinusValue;
                /** Press Add Card Minus button */
                product = list.get(position).getProduct();
                if (holder.cardTextValue.getText().toString().equalsIgnoreCase("1")) {
                    countMinusValue = Integer.parseInt(holder.cardTextValue.getText().toString()) - 1;
                    holder.cardTextValue.setText("" + countMinusValue);
                    holder.cardTextValueTicker.setText("" + countMinusValue);
                    productList = list.get(position);
                    remove(productList);

                    if (categoryList != null) {
                        for (int i = 0; i < categoryList.size(); i++) {
                            for (int j = 0; j < categoryList.get(i).getProducts().size(); j++) {
                                if (categoryList.get(i).getProducts().get(j).getId().equals(product.getId())) {
                                    categoryList.get(i).getProducts().get(j).setCart(null);
                                }
                            }
                        }
                    }

                } else {
                    countMinusValue = Integer.parseInt(holder.cardTextValue.getText().toString()) - 1;
                    holder.cardTextValue.setText("" + countMinusValue);
                    holder.cardTextValueTicker.setText("" + countMinusValue);
                    if (categoryList != null) {
                        for (int i = 0; i < categoryList.size(); i++) {
                            for (int j = 0; j < categoryList.get(i).getProducts().size(); j++) {
                                if (categoryList.get(i).getProducts().get(j).getId().equals(product.getId())) {
                                    categoryList.get(i).getProducts().get(j).getCart().setQuantity(countMinusValue);
                                }
                            }
                        }
                    }


                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("product_id", product.getId().toString());
                map.put("quantity", String.valueOf(countMinusValue));
                Log.e("AddCart_Minus", map.toString());
                addCart(map);

                int quantity = Integer.parseInt(holder.cardTextValue.getText().toString());
                priceAmount = quantity * product.getPrices().getPrice();
                holder.priceTxt.setText(product.getPrices().getCurrency() + " " + priceAmount);


            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView dishImg, foodImageType, cardAddBtn, cardMinusBtn, animationLineCartAdd;
        private TextView dishNameTxt, priceTxt, cardTextValue, cardAddInfoText, cardAddOutOfStock;
        TickerView cardTextValueTicker;
        RelativeLayout cardAddDetailLayout, cardAddTextLayout, cardInfoLayout;

        private MyViewHolder(View view) {
            super(view);
            foodImageType = (ImageView) itemView.findViewById(R.id.food_type_image);
            animationLineCartAdd = (ImageView) itemView.findViewById(R.id.animation_line_cart_add);
            dishNameTxt = (TextView) itemView.findViewById(R.id.dish_name_text);
            priceTxt = (TextView) itemView.findViewById(R.id.price_text);
         /*    Add card Button Layout*/
            cardAddDetailLayout = (RelativeLayout) itemView.findViewById(R.id.add_card_layout);
            cardAddTextLayout = (RelativeLayout) itemView.findViewById(R.id.add_card_text_layout);
            cardAddInfoText = (TextView) itemView.findViewById(R.id.avialablity_time);
            cardAddOutOfStock = (TextView) itemView.findViewById(R.id.out_of_stock);
            cardAddBtn = (ImageView) itemView.findViewById(R.id.card_add_btn);
            cardMinusBtn = (ImageView) itemView.findViewById(R.id.card_minus_btn);
            cardTextValue = (TextView) itemView.findViewById(R.id.card_value);
            cardTextValueTicker = (TickerView) itemView.findViewById(R.id.card_value_ticker);
        }


    }


    private void addCart(HashMap<String, String> map) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.empty_dialog);
        dialog.setCancelable(false);
        dataResponse = false;
        dialog.show();
        Call<AddCart> call = apiInterface.postAddCart(map);
        call.enqueue(new Callback<AddCart>() {
            @Override
            public void onResponse(Call<AddCart> call, Response<AddCart> response) {
                avdProgress.stop();
                dialog.dismiss();
                dataResponse = true;

                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    addCart = response.body();
                    GlobalData.getInstance().addCart = response.body();
                    priceAmount = 0;
                    discount = 0;
                    itemQuantity = 0;
                    itemCount = 0;
                    //get Item Count
                    itemCount = addCart.getProductList().size();
                    if (itemCount != 0) {
                        for (int i = 0; i < itemCount; i++) {
                            //Get Total item Quantity
                            itemQuantity = itemQuantity + addCart.getProductList().get(i).getQuantity();
                            //Get product price
                            if (addCart.getProductList().get(i).getProduct().getPrices().getPrice() != null)
                                priceAmount = priceAmount + (addCart.getProductList().get(i).getQuantity() * addCart.getProductList().get(i).getProduct().getPrices().getPrice());
                        }
                        if(response.body().getProductList().get(0).getProduct().getShop().getOfferMinAmount()<priceAmount){
                            int offerPercentage=response.body().getProductList().get(0).getProduct().getShop().getOfferPercent();
                            discount = (int) (priceAmount*(offerPercentage*0.01));

                        }

                        GlobalData.getInstance().notificationCount = itemQuantity;
                        //Set Payment details
                        String currency = addCart.getProductList().get(0).getProduct().getPrices().getCurrency();
                        CartFragment.itemTotalAmount.setText(currency + "" + priceAmount);
                        CartFragment.discountAmount.setText("- " + currency + "" + discount);
                        int topPayAmount = priceAmount - discount;
                        topPayAmount = topPayAmount+ response.body().getDeliveryCharges()+response.body().getTaxPercentage();
                        CartFragment.payAmount.setText(currency + "" + topPayAmount);

                    } else {
                        GlobalData.getInstance().notificationCount = itemQuantity;
//                        HomeActivity.updateNotificationCount(context, GlobalData.getInstance().notificationCount);
                        CartFragment.errorLayout.setVisibility(View.VISIBLE);
                        CartFragment.dataLayout.setVisibility(View.GONE);
                        Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(Call<AddCart> call, Throwable t) {

            }
        });

    }


}
