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
import com.foodie.app.model.OrderItem;

import java.util.ArrayList;

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

    public OrderDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        unbinder = ButterKnife.bind(this, view);


        final ArrayList<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(new OrderItem("Madras Coffee House", "$20"));
        orderItemList.add(new OrderItem("Biriyani", "$50"));
        orderItemList.add(new OrderItem("American fast food", "$5"));

        //Offer Restaurant Adapter
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        orderRecyclerView.setItemAnimator(new DefaultItemAnimator());
        orderRecyclerView.setHasFixedSize(true);
        OrderDetailAdapter orderItemListAdapter = new OrderDetailAdapter(orderItemList, context);
        orderRecyclerView.setAdapter(orderItemListAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
