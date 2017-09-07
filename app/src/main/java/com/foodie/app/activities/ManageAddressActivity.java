package com.foodie.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.foodie.app.R;
import com.foodie.app.adapter.ManageAddressAdapter;
import com.foodie.app.model.Location;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ManageAddressActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.manage_address_rv)
    RecyclerView manageAddressRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        List<Location> locations = new ArrayList<>();
        locations.add(new Location("Home", "Madhavaram, Chennai", 1));
        locations.add(new Location("Work", "Greems Road, Chennai", 2));
        locations.add(new Location("Other", "Reteri, Anna salai, Chennai", 0));
        ManageAddressAdapter adapter = new ManageAddressAdapter(locations, ManageAddressActivity.this);
        manageAddressRv.setLayoutManager(new LinearLayoutManager(this));
        manageAddressRv.setItemAnimator(new DefaultItemAnimator());
        manageAddressRv.setAdapter(adapter);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
