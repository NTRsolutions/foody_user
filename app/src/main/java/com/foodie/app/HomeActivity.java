package com.foodie.app;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Toast;

import com.foodie.app.fragments.CartFragment;
import com.foodie.app.fragments.HomeFragment;
import com.foodie.app.fragments.ProfileFragment;
import com.foodie.app.fragments.SearchFragment;

import com.foodie.app.utils.ConnectionHelper;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity {

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private BottomBar bottomBar;
    private ConnectionHelper connectionHelper;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        connectionHelper = new ConnectionHelper(this);
        //startActivity(new Intent(HomeActivity.this, HotelViewActivity.class));

        fragmentManager = getSupportFragmentManager();
        fragment = new HomeFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment).commit();

        bottomBar = (BottomBar) findViewById(R.id.bottom_navigation);
        bottomBar.setTabTitleTextAppearance(2);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
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
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        connectionHelper.isConnectingToInternet();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
