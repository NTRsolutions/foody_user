package com.foodie.app;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.WindowManager;

import com.foodie.app.activities.SetDeliveryLocationActivity;
import com.foodie.app.fragments.CartFragment;
import com.foodie.app.fragments.HomeFragment;
import com.foodie.app.fragments.ProfileFragment;
import com.foodie.app.fragments.SearchFragment;

import com.foodie.app.utils.ConnectionHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity {


    private Fragment fragment;
    private FragmentManager fragmentManager;
    ConnectionHelper connectionHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        connectionHelper = new ConnectionHelper(this);
        //startActivity(new Intent(HomeActivity.this, SetDeliveryLocationActivity.class));

        fragmentManager = getSupportFragmentManager();
        fragment = new HomeFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment).commit();

        BottomNavigationViewEx bottomNavigationView = (BottomNavigationViewEx)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setTextVisibility(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.enableItemShiftingMode(false);
        bottomNavigationView.setIconsMarginTop(10);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                fragment = new HomeFragment();
                                break;
                            case R.id.action_search:
                                fragment = new SearchFragment();
                                break;
                            case R.id.action_cart:
                                fragment = new CartFragment();
                                break;
                            case R.id.action_profile:
                                fragment = new ProfileFragment();
                                break;

                        }
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragment).commit();
                        return true;
                    }
                });


        /*final ArrayList<ImpressiveDish> list = new ArrayList<>();
        list.add(new ImpressiveDish("Santhosh1", "Hello"));
        list.add(new ImpressiveDish("Santhosh2", "Hello"));
        list.add(new ImpressiveDish("Santhosh3", "Hello"));
        list.add(new ImpressiveDish("Santhosh4", "Hello"));
        list.add(new ImpressiveDish("Santhosh5", "Hello"));
        list.add(new ImpressiveDish("Santhosh6", "Hello"));
        list.add(new ImpressiveDish("Santhosh7", "Hello"));
        list.add(new ImpressiveDish("Santhosh8", "Hello"));
        list.add(new ImpressiveDish("Santhosh9", "Hello"));
        list.add(new ImpressiveDish("Santhosh10", "Hello"));

        ImpressiveDishesAdapter adapter = new ImpressiveDishesAdapter(list, this);
        impressiveRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        impressiveRv.setItemAnimator(new DefaultItemAnimator());
        impressiveRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImpressiveDishesAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                ImpressiveDish dish =  list.get(position);
                Log.d("Hello", "onItemClick position: " + dish.getName());
            }

        });*/


    }

    @Override
    public void onResume(){
        super.onResume();
        connectionHelper.isConnectingToInternet();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
