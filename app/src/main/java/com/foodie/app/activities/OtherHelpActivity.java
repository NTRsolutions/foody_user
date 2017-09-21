package com.foodie.app.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.foodie.app.R;
import com.foodie.app.pubnub.callback.fragment.ChatFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OtherHelpActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.chat_us)
    Button chatUs;
    @BindView(R.id.call_us)
    Button callUs;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @BindView(R.id.title)
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_help);
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();
        title.setText("Others");

        //Toolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @OnClick({R.id.chat_us, R.id.call_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chat_us:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.chat_fragment, new ChatFragment(), "Tamil");
                fragmentTransaction.commit();

                break;
            case R.id.call_us:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }
}
