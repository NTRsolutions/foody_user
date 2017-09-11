package com.foodie.app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.foodie.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class CartFragment extends Fragment {
    @BindView(R.id.number_button)
    ElegantNumberButton numberButton;
    Unbinder unbinder;
    @BindView(R.id.animation_line_cart_add)
    ImageView animationLineCartAdd;
    @BindView(R.id.linear_main)
    LinearLayout linearMain;

    private Context context;
    private ViewGroup toolbar;
    private View toolbarLayout;
    AnimatedVectorDrawableCompat avdProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        unbinder = ButterKnife.bind(this, view);
        animationLineCartAdd.setVisibility(View.INVISIBLE);
        numberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = numberButton.getNumber();
                animationLineCartAdd.setVisibility(View.VISIBLE);
                linearMain.setAlpha((float) 0.5);
                view.setClickable(false);
                initializeAvd();
            }
        });

        return view;

    }

    Runnable action = new Runnable() {
        @Override
        public void run() {
            avdProgress.stop();
            if (animationLineCartAdd != null)
                animationLineCartAdd.setVisibility(View.INVISIBLE);
            if(linearMain!=null){
                linearMain.setAlpha((float) 1);
                numberButton.setClickable(true);
            }
        }
    };

    private void initializeAvd() {
        avdProgress = AnimatedVectorDrawableCompat.create(context, R.drawable.add_cart_avd_line);
        animationLineCartAdd.setBackground(avdProgress);
        repeatAnimation();
    }

    private void repeatAnimation() {
        avdProgress.start();
        animationLineCartAdd.postDelayed(action, 3000); // Will repeat animation in every 1 second
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
        unbinder.unbind();
    }


    public void FeedbackDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.feedback);
        EditText commentEdit = (EditText) dialog.findViewById(R.id.comment);

        Button submitBtn = (Button) dialog.findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("CartFragment");
        toolbar = (ViewGroup) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
//        toolbarLayout = LayoutInflater.from(context).inflate(R.layout.toolbar_cart, toolbar, false);
//        toolbar.addView(toolbarLayout);
    }

    @OnClick(R.id.number_button)
    public void onViewClicked() {

    }
}
