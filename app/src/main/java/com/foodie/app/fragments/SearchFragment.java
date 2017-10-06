package com.foodie.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.helper.CommonClass;


/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class SearchFragment extends Fragment {
    private Context context;
    private ViewGroup toolbar;
    private View toolbarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        HomeActivity.updateNotificationCount(context, CommonClass.getInstance().notificationCount);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.updateNotificationCount(context, CommonClass.getInstance().notificationCount);
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
        System.out.println("SearchFragment");
        toolbar = (ViewGroup) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbarLayout = LayoutInflater.from(context).inflate(R.layout.toolbar_search, toolbar, false);
        toolbar.addView(toolbarLayout);
    }

}
