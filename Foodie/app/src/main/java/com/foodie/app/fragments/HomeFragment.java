package com.foodie.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.activities.SetDeliveryLocationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by CSS22 on 22-08-2017.
 */

public class HomeFragment extends Fragment {
    Context context;

    @BindView(R.id.address_label)
    TextView addressLabel;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.location_ll)
    LinearLayout locationLl;
    @BindView(R.id.filterTxt)
    TextView filterTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.impressive_dishes_rv)
    RecyclerView impressiveDishesRv;

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

        locationLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SetDeliveryLocationActivity.class));
            }
        });


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

}
