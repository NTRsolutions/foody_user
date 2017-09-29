package com.foodie.app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.activities.SetDeliveryLocationActivity;
import com.foodie.app.adapter.PromotionsAdapter;
import com.foodie.app.adapter.ViewCartAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.helper.SharedHelper;
import com.foodie.app.model.AddCart;
import com.foodie.app.model.ProductList;
import com.foodie.app.model.PromotionsModel;
import com.robinhood.ticker.TickerUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class CartFragment extends Fragment {

    @BindView(R.id.dish_img)
    ImageView dishImg;
    @BindView(R.id.dish_name)
    TextView dishName;
    @BindView(R.id.dish_category)
    TextView dishCategory;
    @BindView(R.id.re)
    RelativeLayout re;
    @BindView(R.id.order_item_rv)
    RecyclerView orderItemRv;

    @BindView(R.id.linear_main)
    LinearLayout linearMain;
    @BindView(R.id.map_marker_image)
    ImageView mapMarkerImage;
    @BindView(R.id.location_error_title)
    TextView locationErrorTitle;
    @BindView(R.id.location_error_sub_title)
    TextView locationErrorSubTitle;
    @BindView(R.id.location_error_layout)
    RelativeLayout locationErrorLayout;
    @BindView(R.id.add_address_btn)
    Button addAddressBtn;
    @BindView(R.id.dummy_image_view)
    ImageView dummyImageView;
    private Context context;
    private ViewGroup toolbar;
    private View toolbarLayout;
    AnimatedVectorDrawableCompat avdProgress;
    //Animation number
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();

    Fragment orderFullViewFragment;
    FragmentManager fragmentManager;
    //Orderitem List
    List<ProductList> viewCartItemList;

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    ViewCartAdapter viewCartAdapter;
    CustomDialog customDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        ButterKnife.bind(this, view);

        customDialog = new CustomDialog(context);
//        addCardLayout.setVisibility(View.VISIBLE);
//        cardValueTicker.setCharacterList(NUMBER_LIST);
//        cardValueTicker.setText(String.valueOf(1));
//        addCardTextLayout.setVisibility(View.GONE);
//        animationLineCartAdd.setVisibility(View.INVISIBLE);
//        numberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String num = numberButton.getNumber();
//                animationLineCartAdd.setVisibility(View.VISIBLE);
//                linearMain.setAlpha((float) 0.5);
//                view.setClickable(false);
//                initializeAvd();
//            }
//        });

        getViewCart();


        viewCartItemList = new ArrayList<>();
        //Offer Restaurant Adapter
        orderItemRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        orderItemRv.setItemAnimator(new DefaultItemAnimator());
        orderItemRv.setHasFixedSize(true);
        orderItemRv.setNestedScrollingEnabled(false);

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SetDeliveryLocationActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
            }
        });

        return view;

    }

    private void getViewCart() {
        customDialog.show();
        Call<AddCart> call = apiInterface.getViewCart();
        call.enqueue(new Callback<AddCart>() {
            @Override
            public void onResponse(Call<AddCart> call, Response<AddCart> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    customDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    customDialog.dismiss();
                    //Get Profile data
                    viewCartItemList.addAll(response.body().getProductList());
                    viewCartAdapter = new ViewCartAdapter(viewCartItemList, context);
                    orderItemRv.setAdapter(viewCartAdapter);

                }
            }

            @Override
            public void onFailure(Call<AddCart> call, Throwable t) {

            }
        });
    }

//    Runnable action = new Runnable() {
//        @Override
//        public void run() {
//            avdProgress.stop();
//            if (animationLineCartAdd != null)
//                animationLineCartAdd.setVisibility(View.INVISIBLE);
//            if (linearMain != null) {
//                linearMain.setAlpha((float) 1);
//                numberButton.setClickable(true);
//            }
//        }
//    };
//
//    private void initializeAvd() {
//        avdProgress = AnimatedVectorDrawableCompat.create(context, R.drawable.add_cart_avd_line);
//        animationLineCartAdd.setBackground(avdProgress);
//        repeatAnimation();
//    }
//
//    private void repeatAnimation() {
//        avdProgress.start();
//        animationLineCartAdd.postDelayed(action, 3000); // Will repeat animation in every 1 second
//    }

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
        } else {
            dummyImageView.setVisibility(View.GONE);
        }
//        cardValueTicker.setCharacterList(NUMBER_LIST);

    }


//
//    @OnClick(R.id.number_button)
//    public void onViewClicked() {
//
//    }
//
//    @OnClick({R.id.card_minus_btn, R.id.card_value, R.id.card_add_btn})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.card_minus_btn:
//                /** Press Add Card Minus button */
//                if (cardValue.getText().toString().equalsIgnoreCase("1")) {
//                    Toast.makeText(context, "Your order canceled", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    String num = numberButton.getNumber();
//                    animationLineCartAdd.setVisibility(View.VISIBLE);
//                    linearMain.setAlpha((float) 0.5);
//                    view.setClickable(false);
//                    initializeAvd();
//                    int countMinusValue = Integer.parseInt(cardValue.getText().toString()) - 1;
//                    cardValue.setText("" + countMinusValue);
//                    cardValueTicker.setText("" + countMinusValue);
//                }
//                break;
//            case R.id.card_value:
//
//                break;
//            case R.id.card_add_btn:
//                /** Press Add Card Add button */
//                String num = numberButton.getNumber();
//                animationLineCartAdd.setVisibility(View.VISIBLE);
//                linearMain.setAlpha((float) 0.5);
//                initializeAvd();
//                int countValue = Integer.parseInt(cardValue.getText().toString()) + 1;
//                cardValue.setText("" + countValue);
//                cardValueTicker.setText("" + countValue);
//
//                break;
//        }
//    }
}
