package com.foodie.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.activities.FilterActivity;
import com.foodie.app.activities.SetDeliveryLocationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by CSS22 on 22-08-2017.
 */

public class HomeFragment extends Fragment {
    Context context;

    TextView addressLabel;
    TextView address;
    LinearLayout locationLl;
    Button filterBtn;

    /*@BindView(R.id.address_label)
    TextView addressLabel;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.location_ll)
    LinearLayout locationLl;
    @BindView(R.id.filterTxt)
    TextView filterTxt;*/
    @BindView(R.id.impressive_dishes_rv)
    RecyclerView impressiveDishesRv;

    ViewGroup toolbar;
    View toolbarLayout;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (toolbar != null) {
            toolbar.removeView(toolbarLayout);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        System.out.println("HomeFragment");

        toolbar = (ViewGroup) getActivity().findViewById(R.id.toolbar);
        toolbarLayout = LayoutInflater.from(context).inflate(R.layout.toolbar_home, toolbar, false);

        addressLabel = (TextView) toolbarLayout.findViewById(R.id.address_label);
        address = (TextView) toolbarLayout.findViewById(R.id.address);
        locationLl = (LinearLayout) toolbarLayout.findViewById(R.id.location_ll);
        locationLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SetDeliveryLocationActivity.class));
            }
        });
        filterBtn = (Button) toolbarLayout.findViewById(R.id.filter);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FilterActivity.class));
            }
        });
        toolbar.addView(toolbarLayout);
    }


}
