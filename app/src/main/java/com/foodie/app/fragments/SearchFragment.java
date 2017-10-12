package com.foodie.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.adapter.ViewPagerAdapter;
import com.foodie.app.helper.CommonClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class SearchFragment extends Fragment {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.view_line1)
    View viewLine1;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    Unbinder unbinder;
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
        unbinder = ButterKnife.bind(this, view);





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
        unbinder.unbind();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        System.out.println("SearchFragment");
        toolbar = (ViewGroup) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbarLayout = LayoutInflater.from(context).inflate(R.layout.toolbar_search, toolbar, false);
        toolbar.addView(toolbarLayout);
        HomeActivity.updateNotificationCount(context, CommonClass.getInstance().notificationCount);

        //ViewPager Adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new RestaurantFragment(), "RESTAURANT");
        adapter.addFragment(new OrderHelpFragment(), "DISHES");
        viewPager.setAdapter(adapter);
        //set ViewPager
        tabLayout.setupWithViewPager(viewPager);


    }

}
