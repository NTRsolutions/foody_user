package com.foodie.app.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.foodie.app.R;
import com.foodie.app.activities.HotelViewActivity;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.models.AddCart;
import com.foodie.app.models.Cart;
import com.foodie.app.models.Category;
import com.foodie.app.models.ClearCart;
import com.foodie.app.models.Product;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.foodie.app.helper.GlobalData.profileModel;
import static com.foodie.app.helper.GlobalData.selectedShop;

/**
 * Created by Tamil on 28-08-2017.
 */

public class HotelCatagoeryAdapter extends SectionedRecyclerViewAdapter<HotelCatagoeryAdapter.ViewHolder> {

    private List<Category> list = new ArrayList<>();
    private LayoutInflater inflater;
    Context context;
    Activity activity;
    int lastPosition = -1;
    int priceAmount = 0;
    int itemCount = 0;
    int itemQuantity = 0;
    Product product;
    List<Product> productList;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    AddCart addCart;
    Animation slide_down, slide_up;
    boolean isShopIsChanged = false;

    //Animation number
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();

    public HotelCatagoeryAdapter(Context context, Activity activity, List<Category> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                v = inflater.inflate(R.layout.category_header, parent, false);
                return new ViewHolder(v, true);
            case VIEW_TYPE_ITEM:
                v = inflater.inflate(R.layout.accompainment_list_item, parent, false);
                return new ViewHolder(v, false);
            default:
                v = inflater.inflate(R.layout.accompainment_list_item, parent, false);
                return new ViewHolder(v, false);
        }
    }

    @Override
    public int getSectionCount() {
        return list.size();
    }


    @Override
    public int getItemCount(int section) {
        return list.get(section).getProducts().size();
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, final int section) {
        holder.headerTxt.setText(list.get(section).getName());
        holder.headerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(list.get(section).getName());
            }
        });
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int section, final int relativePosition, int absolutePosition) {
        product = list.get(section).getProducts().get(relativePosition);
        productList = list.get(section).getProducts();
        holder.cardTextValueTicker.setCharacterList(NUMBER_LIST);
        holder.dishNameTxt.setText(product.getName());
        addCart = GlobalData.getInstance().addCart;
        if (GlobalData.getInstance().addCart != null && GlobalData.getInstance().addCart.getProductList().size() != 0) {
            setViewcartBottomLayout(GlobalData.getInstance().addCart);
        }
        if (product.getCart() != null) {
            GlobalData.getInstance().selectedShop = HotelViewActivity.shops;
            holder.cardAddTextLayout.setVisibility(View.GONE);
            holder.cardAddDetailLayout.setVisibility(View.VISIBLE);
            holder.cardTextValueTicker.setText(String.valueOf(product.getCart().getQuantity()));
            holder.cardTextValue.setText(String.valueOf(product.getCart().getQuantity()));
        } else {
            holder.cardAddTextLayout.setVisibility(View.VISIBLE);
            holder.cardAddDetailLayout.setVisibility(View.GONE);
            holder.cardTextValueTicker.setText(String.valueOf(1));
            holder.cardTextValue.setText(String.valueOf(1));
        }

        holder.priceTxt.setText(product.getPrices().getCurrency() + " " + product.getPrices().getPrice());

        if (!product.getFoodType().equalsIgnoreCase("veg")) {
            holder.foodImageType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nonveg));
        } else {
            holder.foodImageType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_veg));
        }
        holder.cardAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("access_token2", GlobalData.getInstance().accessToken);
                /** Press Add Card Add button */
                    product = list.get(section).getProducts().get(relativePosition);
                    int countValue = Integer.parseInt(holder.cardTextValue.getText().toString()) + 1;
                    holder.cardTextValue.setText("" + countValue);
                    holder.cardTextValueTicker.setText("" + countValue);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("product_id", product.getId().toString());
                    map.put("quantity", holder.cardTextValue.getText().toString());
                    Log.e("AddCart_add", map.toString());
                    addCart(map);
                    product.getCart().setQuantity(countValue);

            }
        });

        holder.cardMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** Press Add Card Minus button */
                product = list.get(section).getProducts().get(relativePosition);
                if (holder.cardTextValue.getText().toString().equalsIgnoreCase("1")) {
                    int countMinusValue = Integer.parseInt(holder.cardTextValue.getText().toString()) - 1;
                    holder.cardTextValue.setText("" + countMinusValue);
                    holder.cardTextValueTicker.setText("" + countMinusValue);
                    holder.cardAddDetailLayout.setVisibility(View.GONE);
                    if (addCart.getProductList().size() == 0 && addCart != null)
                        HotelViewActivity.viewCartLayout.setVisibility(View.GONE);
                    holder.cardAddTextLayout.setVisibility(View.VISIBLE);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("product_id", product.getId().toString());
                    map.put("quantity", "0");
                    Log.e("AddCart_Minus", map.toString());
                    addCart(map);
                    product.setCart(null);
                } else {
                    int countMinusValue = Integer.parseInt(holder.cardTextValue.getText().toString()) - 1;
                    holder.cardTextValue.setText("" + countMinusValue);
                    holder.cardTextValueTicker.setText("" + countMinusValue);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("product_id", product.getId().toString());
                    map.put("quantity", holder.cardTextValue.getText().toString());
                    Log.e("AddCart_Minus", map.toString());
                    addCart(map);
                    //Add model values
                    product.getCart().setQuantity(countMinusValue);
                }
            }
        });

        holder.cardAddTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Press Add Card Text Layout */
                if(isShopIsChanged&& GlobalData.getInstance().addCart!=null){
                    Log.e("IsShopchanged",""+isShopIsChanged);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getResources().getString(R.string.replace_cart_item))
                            .setMessage(context.getResources().getString(R.string.do_you_want_to_discart_the_selection_and_add_dishes_from_the_restaurant))
                            .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    clearCart();
                                    isShopIsChanged=false;
                                    GlobalData.getInstance().selectedShop=HotelViewActivity.shops;
                                    product = list.get(section).getProducts().get(relativePosition);
                                    holder.cardAddDetailLayout.setVisibility(View.VISIBLE);
                                    holder.cardAddTextLayout.setVisibility(View.GONE);
                                    holder.cardTextValue.setText("1");
                                    holder.cardTextValueTicker.setText("1");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("product_id", product.getId().toString());
                                    map.put("quantity", holder.cardTextValue.getText().toString());
                                    Log.e("AddCart_Text", map.toString());
                                    addCart(map);
                                    if (product.getCart() != null) {
                                        product.getCart().setQuantity(1);
                                    } else {
                                        Cart cart = new Cart();
                                        cart.setQuantity(1);
                                        product.setCart(cart);
                                    }
                                }
                            })
                            .setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    nbutton.setTextColor(context.getResources().getColor(R.color.theme));
                    nbutton.setTypeface(nbutton.getTypeface(), Typeface.BOLD);
                    Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(context.getResources().getColor(R.color.theme));
                    pbutton.setTypeface(pbutton.getTypeface(), Typeface.BOLD);
                }
                else  if (GlobalData.getInstance().profileModel != null) {
            /*     Check Shop*/
                    Log.e("IsShopchanged2 :",""+isShopIsChanged);
                    Log.e("profileModel",""+ profileModel.toString());
                      product = list.get(section).getProducts().get(relativePosition);
                      holder.cardAddDetailLayout.setVisibility(View.VISIBLE);
                      holder.cardAddTextLayout.setVisibility(View.GONE);
                      holder.cardTextValue.setText("1");
                      holder.cardTextValueTicker.setText("1");
                      HashMap<String, String> map = new HashMap<String, String>();
                      map.put("product_id", product.getId().toString());
                      map.put("quantity", holder.cardTextValue.getText().toString());
                      Log.e("AddCart_Text", map.toString());
                      addCart(map);
                      if (product.getCart() != null) {
                          product.getCart().setQuantity(1);
                      } else {
                          Cart cart = new Cart();
                          cart.setQuantity(1);
                          product.setCart(cart);
                      }

                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.please_login_and_order_dishes), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void alertDialog() {

    }

    private void clearCart() {

        Call<ClearCart> call=apiInterface.clearCart();
        call.enqueue(new Callback<ClearCart>() {
            @Override
            public void onResponse(Call<ClearCart> call, Response<ClearCart> response) {

                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
//                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    selectedShop = HotelViewActivity.shops;
                    GlobalData.getInstance().addCart.getProductList().clear();

                }

            }

            @Override
            public void onFailure(Call<ClearCart> call, Throwable t) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                GlobalData.getInstance().addCartShopId = selectedShop.getId();
            }
        });

    }

    private void addCart(HashMap<String, String> map) {
        Call<AddCart> call = apiInterface.postAddCart(map);
        call.enqueue(new Callback<AddCart>() {
            @Override
            public void onResponse(Call<AddCart> call, Response<AddCart> response) {
               selectedShop = HotelViewActivity.shops;
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    GlobalData.getInstance().addCartShopId = selectedShop.getId();
                    addCart = response.body();
                    GlobalData.getInstance().addCart = response.body();
                    setViewcartBottomLayout(addCart);
                }
            }

            @Override
            public void onFailure(Call<AddCart> call, Throwable t) {

            }
        });

    }

    private void setViewcartBottomLayout(AddCart addCart) {
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
        GlobalData.getInstance().notificationCount = itemQuantity;
        if (itemQuantity == 0) {
            HotelViewActivity.viewCartLayout.setVisibility(View.GONE);
            // Start animation
            HotelViewActivity.viewCartLayout.startAnimation(slide_down);
        } else if (itemQuantity == 1) {
            if (HotelViewActivity.shops.getId() != GlobalData.getInstance().addCart.getProductList().get(0).getProduct().getShopId()) {
                isShopIsChanged=true;
                HotelViewActivity.viewCartShopName.setVisibility(View.VISIBLE);
                HotelViewActivity.viewCartShopName.setText("From : "+ GlobalData.getInstance().addCart.getProductList().get(0).getProduct().getShop().getName());
            } else{
                isShopIsChanged=false;
                HotelViewActivity.viewCartShopName.setVisibility(View.GONE);
            }
            String currency = addCart.getProductList().get(0).getProduct().getPrices().getCurrency();
            HotelViewActivity.itemText.setText("" + itemQuantity + " Item | " + currency + "" + priceAmount);
            if (HotelViewActivity.viewCartLayout.getVisibility() == View.GONE) {
                // Start animation
                HotelViewActivity.viewCartLayout.setVisibility(View.VISIBLE);
                HotelViewActivity.viewCartLayout.startAnimation(slide_up);
            }
        } else {
            if (HotelViewActivity.shops.getId() != GlobalData.getInstance().addCart.getProductList().get(0).getProduct().getShopId()) {
                isShopIsChanged=true;
                HotelViewActivity.viewCartShopName.setVisibility(View.VISIBLE);
                HotelViewActivity.viewCartShopName.setText("From : "+ GlobalData.getInstance().addCart.getProductList().get(0).getProduct().getShop().getName());
            } else{
                isShopIsChanged=false;
                HotelViewActivity.viewCartShopName.setVisibility(View.GONE);

            }
            String currency = addCart.getProductList().get(0).getProduct().getPrices().getCurrency();
            HotelViewActivity.itemText.setText("" + itemQuantity + " Items | " + currency + "" + priceAmount);
            if (HotelViewActivity.viewCartLayout.getVisibility() == View.GONE) {
                // Start animation
                HotelViewActivity.viewCartLayout.setVisibility(View.VISIBLE);
                HotelViewActivity.viewCartLayout.startAnimation(slide_up);
            }

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView headerTxt;
        private ImageView dishImg, foodImageType, cardAddBtn, cardMinusBtn, animationLineCartAdd;
        private TextView dishNameTxt, priceTxt, cardTextValue, cardAddInfoText, cardAddOutOfStock;
        TickerView cardTextValueTicker;
        RelativeLayout cardAddDetailLayout, cardAddTextLayout, cardInfoLayout;

        public ViewHolder(View itemView, boolean isHeader) {
            super(itemView);
            if (isHeader) {
                headerTxt = (TextView) itemView.findViewById(R.id.category_header);
            } else {
                dishImg = (ImageView) itemView.findViewById(R.id.dishImg);
                foodImageType = (ImageView) itemView.findViewById(R.id.food_type_image);
                animationLineCartAdd = (ImageView) itemView.findViewById(R.id.animation_line_cart_add);
                dishNameTxt = (TextView) itemView.findViewById(R.id.dish_name_text);
                priceTxt = (TextView) itemView.findViewById(R.id.price_text);

             /*    Add card Button Layout*/
                cardAddDetailLayout = (RelativeLayout) itemView.findViewById(R.id.add_card_layout);
                cardAddTextLayout = (RelativeLayout) itemView.findViewById(R.id.add_card_text_layout);
                cardInfoLayout = (RelativeLayout) itemView.findViewById(R.id.add_card_info_layout);
                cardAddInfoText = (TextView) itemView.findViewById(R.id.avialablity_time);
                cardAddOutOfStock = (TextView) itemView.findViewById(R.id.out_of_stock);
                cardAddBtn = (ImageView) itemView.findViewById(R.id.card_add_btn);
                cardMinusBtn = (ImageView) itemView.findViewById(R.id.card_minus_btn);
                cardTextValue = (TextView) itemView.findViewById(R.id.card_value);
                cardTextValueTicker = (TickerView) itemView.findViewById(R.id.card_value_ticker);
                //itemView.setOnClickListener( this);

                //Load animation
                slide_down = AnimationUtils.loadAnimation(context,
                        R.anim.slide_down);
                slide_up = AnimationUtils.loadAnimation(context,
                        R.anim.slide_up);
            }

        }

    }
}
