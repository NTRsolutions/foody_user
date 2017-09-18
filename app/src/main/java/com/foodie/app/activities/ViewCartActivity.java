package com.foodie.app.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.foodie.app.R;
import com.foodie.app.fragments.CartFragment;
import com.foodie.app.fragments.HomeFragment;

public class ViewCartActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        fragmentManager = getSupportFragmentManager();
        fragment = new CartFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.view_cart_container, fragment).commit();

    }
}