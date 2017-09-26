package com.foodie.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.foodie.app.R;
import com.foodie.app.activities.CurrentOrderDetailActivity;
import com.foodie.app.activities.HotelViewActivity;
import com.foodie.app.activities.PastOrderDetailActivity;
import com.foodie.app.model.Category;
import com.foodie.app.model.CategoryModel;
import com.foodie.app.model.Order;
import com.foodie.app.model.OrderModel;
import com.foodie.app.model.Product;
import com.foodie.app.model.RecommendedDish;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santhosh@appoets.com on 28-08-2017.
 */

public class HotelCatagoeryAdapter extends SectionedRecyclerViewAdapter<HotelCatagoeryAdapter.ViewHolder> {

    private List<Category> list = new ArrayList<>();
    private LayoutInflater inflater;
    Context context1;
    Activity activity;
    int lastPosition=-1;
    int priceAmount = 0;
    int itemCount = 0;
    //Animation number
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();

    public HotelCatagoeryAdapter(Context context, Activity activity, List<Category> list) {
        this.context1 = context;
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
    public void onBindViewHolder(ViewHolder holder, final int section, int relativePosition, int absolutePosition) {
        final Product object = list.get(section).getProducts().get(relativePosition);
        holder.cardTextValueTicker.setCharacterList(NUMBER_LIST);
        holder.dishNameTxt.setText(object.getName());

        holder.cardTextValueTicker.setText(String.valueOf(1));
//        holder.priceTxt.setText("$"+dish.getPrice());

//       /* check Availablity*/
//        if (object.getAvalability().toString().equalsIgnoreCase("available")) {
//            holder.cardAddTextLayout.setVisibility(View.VISIBLE);
//            holder.cardTextValueTicker.setText(String.valueOf(1));
//            holder.cardInfoLayout.setVisibility(View.GONE);
//        } else if (dish.getAvaialable().equalsIgnoreCase("out of stock")) {
//            holder.cardAddTextLayout.setVisibility(View.GONE);
//            holder.cardInfoLayout.setVisibility(View.VISIBLE);
//            holder.cardAddInfoText.setVisibility(View.GONE);
//            holder.cardAddOutOfStock.setVisibility(View.VISIBLE);
//        } else {
//            holder.cardAddTextLayout.setVisibility(View.GONE);
//            holder.cardInfoLayout.setVisibility(View.VISIBLE);
//            holder.cardAddInfoText.setVisibility(View.VISIBLE);
//            holder.cardAddOutOfStock.setVisibility(View.GONE);
//            holder.cardAddInfoText.setText(dish.getAvaialable());
//        }

        if (!object.getVeg().equalsIgnoreCase("veg")) {
            holder.foodImageType.setImageDrawable(context1.getResources().getDrawable(R.drawable.ic_nonveg));
        } else {
            holder.foodImageType.setImageDrawable(context1.getResources().getDrawable(R.drawable.ic_veg));
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView headerTxt;
        private ImageView dishImg, foodImageType, cardAddBtn, cardMinusBtn, animationLineCartAdd;
        private TextView dishNameTxt, priceTxt,cardTextValue , cardAddInfoText, cardAddOutOfStock;
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


            /*  Click Events*/
                cardAddTextLayout.setOnClickListener(this);
                cardAddBtn.setOnClickListener(this);
                cardMinusBtn.setOnClickListener(this);
            }


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_card_text_layout:
                    /** Press Add Card Text Layout */
                    cardAddDetailLayout.setVisibility(View.VISIBLE);
                    HotelViewActivity.viewCartLayout.setVisibility(View.VISIBLE);
                    itemCount = itemCount + 1;
//                    priceAmount = priceAmount + Integer.parseInt(list.get(getAdapterPosition()).getPrice());
                    HotelViewActivity.itemText.setText("" + itemCount + " Item | $" + "" + priceAmount);
                    cardAddTextLayout.setVisibility(View.GONE);
                    break;

                case R.id.card_add_btn:
                    /** Press Add Card Add button */
                    int countValue = Integer.parseInt(cardTextValue.getText().toString()) + 1;
                    itemCount = itemCount + 1;
//                    priceAmount = priceAmount + Integer.parseInt(list.get(getAdapterPosition()).getPrice());
                    HotelViewActivity.itemText.setText("" + itemCount + " Items | $" + "" + priceAmount);
                    cardTextValue.setText("" + countValue);
                    cardTextValueTicker.setText("" + countValue);
                    break;
                case R.id.card_minus_btn:
                    /** Press Add Card Minus button */
                    if (cardTextValue.getText().toString().equalsIgnoreCase("1")) {
                        cardAddDetailLayout.setVisibility(View.GONE);
                        HotelViewActivity.viewCartLayout.setVisibility(View.GONE);
                        cardAddTextLayout.setVisibility(View.VISIBLE);
                    } else {
                        int countMinusValue = Integer.parseInt(cardTextValue.getText().toString()) - 1;
                        itemCount = itemCount - 1;
//                        priceAmount = priceAmount - Integer.parseInt(list.get(getAdapterPosition()).getPrice());
                        HotelViewActivity.itemText.setText("" + itemCount + " Items | $" + "" + priceAmount);
                        cardTextValue.setText("" + countMinusValue);
                        cardTextValueTicker.setText("" + countMinusValue);

                    }
                    break;
            }
        }
    }
}
