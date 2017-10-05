package com.foodie.app.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.adapter.OrderDetailAdapter;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.model.Checkout;
import com.foodie.app.model.Item;
import com.foodie.app.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailFragment extends Fragment {


    @BindView(R.id.order_recycler_view)
    RecyclerView orderRecyclerView;
    Unbinder unbinder;

    Context context = getActivity();
    @BindView(R.id.item_total_amount)
    TextView itemTotalAmount;
    @BindView(R.id.service_tax)
    TextView serviceTax;
    @BindView(R.id.delivery_charges)
    TextView deliveryCharges;
    @BindView(R.id.total_amount)
    TextView totalAmount;
    List<Item> itemList;



    int totalAmountValue = 0;
    int discount = 0;
    int itemCount = 0;
    int itemQuantity = 0;
    String currency = "";



    public OrderDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        unbinder = ButterKnife.bind(this, view);


        Checkout checkout=CommonClass.getInstance().checkoutData;
        //set Item List Values
        itemList = new ArrayList<>();
        itemList.addAll(checkout.getItems());

        //Offer Restaurant Adapter
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        orderRecyclerView.setItemAnimator(new DefaultItemAnimator());
        orderRecyclerView.setHasFixedSize(true);
        OrderDetailAdapter orderItemListAdapter = new OrderDetailAdapter(itemList, context);
        orderRecyclerView.setAdapter(orderItemListAdapter);

        currency = checkout.getItems().get(0).getProduct().getPrices().getCurrency();
        itemQuantity=checkout.getInvoice().getQuantity();
        totalAmountValue=checkout.getInvoice().getGross();
        itemTotalAmount.setText(currency+checkout.getInvoice().getGross().toString());
        serviceTax.setText(currency+checkout.getInvoice().getTax().toString());
        totalAmount.setText(currency+String.valueOf(totalAmountValue));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
