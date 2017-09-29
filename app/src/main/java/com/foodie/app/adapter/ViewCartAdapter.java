package com.foodie.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.model.AddCart;
import com.foodie.app.model.Product;
import com.foodie.app.model.ProductList;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class ViewCartAdapter extends RecyclerView.Adapter<ViewCartAdapter.MyViewHolder> {
    private List<ProductList> list;
    private Context context;
    int priceAmount = 0;
    int itemCount = 0;
    int itemQuantity = 0;
    Product product;
    List<Product> productList;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    AddCart addCart;

    //Animation number
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();

    public ViewCartAdapter(List<ProductList> list, Context con) {
        this.list = list;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_product_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public void add(ProductList item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(ProductList item) {
        int position = list.indexOf(item);
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.cardAddTextLayout.setVisibility(View.GONE);
        holder.cardAddDetailLayout.setVisibility(View.VISIBLE);
//        cardValueTicker.setCharacterList(NUMBER_LIST);
//        cardValueTicker.setText(String.valueOf(1));
//        addCardTextLayout.setVisibility(View.GONE);
//        animationLineCartAdd.setVisibility(View.INVISIBLE);
//        numberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String num = numberButton.getNumber();
//                animationLineCartAdd.setVisibility(View.VISIBLE);
//                linearMain.setAlpha((float) 0.5);
//                view.setClickable(false);
//                initializeAvd();
//            }
//        });
        product = list.get(position).getProduct();
        holder.cardTextValueTicker.setCharacterList(NUMBER_LIST);
        holder.dishNameTxt.setText(product.getName());
        holder.cardTextValueTicker.setText(String.valueOf(1));
        holder.priceTxt.setText(product.getPrices().getCurrency() + " " + product.getPrices().getPrice());
        if (!product.getFoodType().equalsIgnoreCase("veg")) {
            holder.foodImageType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nonveg));
        } else {
            holder.foodImageType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_veg));
        }
        holder.cardAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("access_token2", CommonClass.getInstance().accessToken);
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

            }
        });

        holder.cardMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** Press Add Card Minus button */
                product = list.get(position).getProduct();
                if (holder.cardTextValue.getText().toString().equalsIgnoreCase("1")) {
                    int countMinusValue = Integer.parseInt(holder.cardTextValue.getText().toString()) - 1;
                    holder.cardTextValue.setText("" + countMinusValue);
                    holder.cardTextValueTicker.setText("" + countMinusValue);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("product_id", product.getId().toString());
                    map.put("quantity", "0");
                    Log.e("AddCart_Minus", map.toString());
                    addCart(map);
                } else {
                    int countMinusValue = Integer.parseInt(holder.cardTextValue.getText().toString()) - 1;
                    holder.cardTextValue.setText("" + countMinusValue);
                    holder.cardTextValueTicker.setText("" + countMinusValue);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("product_id", product.getId().toString());
                    map.put("quantity", holder.cardTextValue.getText().toString());
                    Log.e("AddCart_Minus", map.toString());
                    addCart(map);
                }
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
        Call<AddCart> call = apiInterface.postAddCart(map);
        call.enqueue(new Callback<AddCart>() {
            @Override
            public void onResponse(Call<AddCart> call, Response<AddCart> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    addCart = response.body();
                    priceAmount = 0;
                    itemQuantity = 0;
                    itemCount = 0;

                    //get Item Count
                    itemCount = addCart.getProductList().size();
                    for (int i = 0; i < itemCount; i++) {
                        //Get Total item Quantity
                        itemQuantity = itemQuantity + addCart.getProductList().get(i).getQuantity();
                        //Get product price
                        if (addCart.getProductList().get(i).getProduct().getPrices().getPrice() != null)
                            priceAmount = priceAmount + (addCart.getProductList().get(i).getQuantity() * addCart.getProductList().get(i).getProduct().getPrices().getPrice());
                    }
                    if (itemQuantity == 0) {
//                        HotelViewActivity.viewCartLayout.setVisibility(View.GONE);
//                        // Start animation
//                        HotelViewActivity.viewCartLayout.startAnimation(slide_down);
                    } else if (itemQuantity == 1) {
                        String currency = addCart.getProductList().get(0).getProduct().getPrices().getCurrency();
//                        HotelViewActivity.itemText.setText("" + itemQuantity + " Item | " + currency + "" + priceAmount);
//                        if (HotelViewActivity.viewCartLayout.getVisibility() == View.GONE) {
//                            // Start animation
//                            HotelViewActivity.viewCartLayout.setVisibility(View.VISIBLE);
//                            HotelViewActivity.viewCartLayout.startAnimation(slide_up);
//                        }
                    } else {
                        String currency = addCart.getProductList().get(0).getProduct().getPrices().getCurrency();
//                        HotelViewActivity.itemText.setText("" + itemQuantity + " Items | " + currency + "" + priceAmount);
//                        if (HotelViewActivity.viewCartLayout.getVisibility() == View.GONE) {
//                            // Start animation
//                            HotelViewActivity.viewCartLayout.setVisibility(View.VISIBLE);
//                            HotelViewActivity.viewCartLayout.startAnimation(slide_up);
//                        }

                    }


                }
            }

            @Override
            public void onFailure(Call<AddCart> call, Throwable t) {

            }
        });

    }


}
