package com.foodie.app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.foodie.app.R;
import com.foodie.app.activities.SetDeliveryLocationActivity;

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
    @BindView(R.id.linear_main)
    LinearLayout linearMain;
    @BindView(R.id.card_minus_btn)
    ImageView cardMinusBtn;
    @BindView(R.id.card_value)
    TextView cardValue;
    @BindView(R.id.card_add_btn)
    ImageView cardAddBtn;
    @BindView(R.id.add_card_layout)
    RelativeLayout addCardLayout;
    @BindView(R.id.add_ons_icon)
    ImageView addOnsIcon;
    @BindView(R.id.addText)
    TextView addText;
    @BindView(R.id.animation_line_cart_add)
    ImageView animationLineCartAdd;
    @BindView(R.id.add_card_text_layout)
    RelativeLayout addCardTextLayout;
    @BindView(R.id.add_address_btn)
    Button addAddressBtn;
    @BindView(R.id.dummy_image_view)
    ImageView dummyImageView;


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
        addCardLayout.setVisibility(View.VISIBLE);
        addCardTextLayout.setVisibility(View.GONE);
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
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SetDeliveryLocationActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.anim_nothing);
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
            if (linearMain != null) {
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
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
            dummyImageView.setVisibility(View.VISIBLE);
        }
        else {
            dummyImageView.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.number_button)
    public void onViewClicked() {

    }


    @OnClick({R.id.card_minus_btn, R.id.card_value, R.id.card_add_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.card_minus_btn:
                /** Press Add Card Minus button */
                if (cardValue.getText().toString().equalsIgnoreCase("1")) {
                    Toast.makeText(context, "Your order canceled", Toast.LENGTH_SHORT).show();

                } else {
                    String num = numberButton.getNumber();
                    animationLineCartAdd.setVisibility(View.VISIBLE);
                    linearMain.setAlpha((float) 0.5);
                    view.setClickable(false);
                    initializeAvd();
                    int countMinusValue = Integer.parseInt(cardValue.getText().toString()) - 1;
                    cardValue.setText("" + countMinusValue);
                }
                break;
            case R.id.card_value:

                break;
            case R.id.card_add_btn:
                /** Press Add Card Add button */
                String num = numberButton.getNumber();
                animationLineCartAdd.setVisibility(View.VISIBLE);
                linearMain.setAlpha((float) 0.5);
                initializeAvd();
                int countValue = Integer.parseInt(cardValue.getText().toString()) + 1;
                cardValue.setText("" + countValue);

                break;
        }
    }
}
