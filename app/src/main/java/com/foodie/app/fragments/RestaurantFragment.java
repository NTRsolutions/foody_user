package com.foodie.app.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.foodie.app.R;
import com.foodie.app.adapter.RestaurantsAdapter;
import com.foodie.app.model.Shop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment {

    RestaurantsAdapter restaurantsAdapter;
    @BindView(R.id.related_txt)
    TextView relatedTxt;
    Unbinder unbinder;
    @BindView(R.id.restaurants_rv)
    RecyclerView restaurantsRv;
    Context context;
    List<Shop> restaurantList;
    private SkeletonScreen skeletonScreen;

    public RestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        unbinder = ButterKnife.bind(this, view);
        context=getActivity();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Restaurant Adapter
        restaurantsRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        restaurantsRv.setItemAnimator(new DefaultItemAnimator());
        restaurantsRv.setHasFixedSize(true);
        restaurantList=new ArrayList<>();
        restaurantsAdapter = new RestaurantsAdapter(restaurantList, context, getActivity());
        skeletonScreen = Skeleton.bind(restaurantsRv)
                .adapter(restaurantsAdapter)
                .load(R.layout.skeleton_restaurant_list_item)
                .count(6)
                .show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
