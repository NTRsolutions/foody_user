package com.foodie.app.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.foodie.app.R;
import com.foodie.app.activities.CurrentOrderDetailActivity;
import com.foodie.app.activities.SetDeliveryLocationActivity;
import com.foodie.app.adapter.ViewCartAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.helper.CustomDialog;
import com.foodie.app.model.AddCart;
import com.foodie.app.model.Checkout;
import com.foodie.app.model.ProductList;
import com.robinhood.ticker.TickerUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.map_marker_image)
    ImageView mapMarkerImage;
    @BindView(R.id.location_error_title)
    TextView locationErrorTitle;
    @BindView(R.id.location_error_sub_title)
    TextView locationErrorSubTitle;
    @BindView(R.id.common_btn)
    Button commonBtn;
    @BindView(R.id.dummy_image_view)
    ImageView dummyImageView;
    @BindView(R.id.total_amount)
    TextView totalAmount;
    @BindView(R.id.buttonLayout)
    LinearLayout buttonLayout;
    @BindView(R.id.address_header)
    TextView addressHeader;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.address_delivery_time)
    TextView addressDeliveryTime;
    @BindView(R.id.add_address_txt)
    TextView addAddressTxt;
    @BindView(R.id.bottom_layout)
    LinearLayout bottomLayout;
    public static RelativeLayout dataLayout;
    public static RelativeLayout errorLayout;
    @BindView(R.id.location_info_layout)
    LinearLayout locationInfoLayout;
    @BindView(R.id.location_error_layout)
    RelativeLayout locationErrorLayout;
    private Context context;
    private ViewGroup toolbar;
    private View toolbarLayout;
    AnimatedVectorDrawableCompat avdProgress;
    //Animation number
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();

    public static TextView itemTotalAmount, deliveryCharges, promoCodeApply, discountAmount, payAmount;

    Fragment orderFullViewFragment;
    FragmentManager fragmentManager;
    //Orderitem List
    List<ProductList> viewCartItemList;

    int priceAmount = 0;
    int discount = 0;
    int itemCount = 0;
    int itemQuantity = 0;
    int ADDRESS_SELECTION = 1;

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

      /*  Intialize Global Values*/
        itemTotalAmount = (TextView) view.findViewById(R.id.item_total_amount);
        deliveryCharges = (TextView) view.findViewById(R.id.delivery_charges);
        promoCodeApply = (TextView) view.findViewById(R.id.promo_code_apply);
        discountAmount = (TextView) view.findViewById(R.id.discount_amount);
        payAmount = (TextView) view.findViewById(R.id.total_amount);
        dataLayout = (RelativeLayout) view.findViewById(R.id.data_layout);
        errorLayout = (RelativeLayout) view.findViewById(R.id.error_layout);

        customDialog = new CustomDialog(context);
        getViewCart();

        viewCartItemList = new ArrayList<>();
        //Offer Restaurant Adapter
        orderItemRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        orderItemRv.setItemAnimator(new DefaultItemAnimator());
        orderItemRv.setHasFixedSize(false);
        orderItemRv.setNestedScrollingEnabled(false);

        //Intialize address Value
        if (CommonClass.getInstance().selectedAddress != null) {
            commonBtn.setBackgroundResource(R.drawable.button_corner_bg_green);
            commonBtn.setText(getResources().getString(R.string.proceed_to_pay));
            addressHeader.setText(CommonClass.getInstance().selectedAddress.getType());
            addressDetail.setText(CommonClass.getInstance().selectedAddress.getMapAddress());
            addressDeliveryTime.setText("30 Mins");
        } else {
            commonBtn.setBackgroundResource(R.drawable.button_corner_bg_theme);
            commonBtn.setText(getResources().getString(R.string.add_address_to_proceed));
            locationErrorLayout.setVisibility(View.VISIBLE);
            locationInfoLayout.setVisibility(View.GONE);

        }

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
                    errorLayout.setVisibility(View.VISIBLE);
                    dataLayout.setVisibility(View.GONE);
                    try {

                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    customDialog.dismiss();
                    //get Item Count
                    itemCount = response.body().getProductList().size();
                    if (itemCount == 0) {
                        errorLayout.setVisibility(View.VISIBLE);
                        dataLayout.setVisibility(View.GONE);
                    } else {
                        errorLayout.setVisibility(View.GONE);
                        dataLayout.setVisibility(View.VISIBLE);
                        for (int i = 0; i < itemCount; i++) {
                            //Get Total item Quantity
                            itemQuantity = itemQuantity + response.body().getProductList().get(i).getQuantity();
                            //Get product price
                            if (response.body().getProductList().get(i).getProduct().getPrices().getPrice() != null)
                                priceAmount = priceAmount + (response.body().getProductList().get(i).getQuantity() * response.body().getProductList().get(i).getProduct().getPrices().getPrice());
                            discount = discount + (response.body().getProductList().get(i).getQuantity() * response.body().getProductList().get(i).getProduct().getPrices().getDiscount());
                        }

                        //Set Payment details
                        String currency = response.body().getProductList().get(0).getProduct().getPrices().getCurrency();
                        itemTotalAmount.setText(currency + "" + priceAmount);
                        discountAmount.setText("- " + currency + "" + discount);
                        int topPayAmount = priceAmount - discount;
                        payAmount.setText(currency + "" + topPayAmount);

                        deliveryCharges.setText(response.body().getProductList().get(0).getProduct().getPrices().getCurrency() + "" + response.body().getDeliveryCharges().toString());
                        viewCartItemList.addAll(response.body().getProductList());
                        viewCartAdapter = new ViewCartAdapter(viewCartItemList, context);
                        orderItemRv.setAdapter(viewCartAdapter);
                    }


                }
            }

            @Override
            public void onFailure(Call<AddCart> call, Throwable t) {

                errorLayout.setVisibility(View.VISIBLE);
                dataLayout.setVisibility(View.GONE);
            }
        });
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

    }


    @OnClick({R.id.add_address_txt, R.id.common_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_address_txt:
                /** Press Add address button */
                startActivityForResult(new Intent(getActivity(), SetDeliveryLocationActivity.class).putExtra("get_address", true), ADDRESS_SELECTION);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
                break;
            case R.id.common_btn:
                /**  If address is empty */
                if (commonBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.add_address_to_proceed))) {
                    startActivityForResult(new Intent(getActivity(), SetDeliveryLocationActivity.class).putExtra("get_address", true), ADDRESS_SELECTION);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
                }
                /**  If address is filled */
                else if (commonBtn.getText().toString().equalsIgnoreCase(getResources().getString(R.string.proceed_to_pay))) {

                    checkOut(CommonClass.getInstance().selectedAddress.getId());
                }


                break;

        }
    }

    private void checkOut(Integer id) {
        customDialog.show();
        Call<Checkout> call = apiInterface.postCheckout(id);
        call.enqueue(new Callback<Checkout>() {
            @Override
            public void onResponse(Call<Checkout> call, Response<Checkout> response) {
                customDialog.dismiss();
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {

                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    CommonClass.getInstance().checkoutData = response.body();
                    startActivity(new Intent(getActivity(), CurrentOrderDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();

                }


            }

            @Override
            public void onFailure(Call<Checkout> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.print("CartFragment");
        if (requestCode == ADDRESS_SELECTION && resultCode == Activity.RESULT_OK) {
            System.out.print("CartFragment : Success");
            if (CommonClass.getInstance().selectedAddress != null) {
                locationErrorLayout.setVisibility(View.GONE);
                locationInfoLayout.setVisibility(View.VISIBLE);
                commonBtn.setBackgroundResource(R.drawable.button_corner_bg_green);
                commonBtn.setText(getResources().getString(R.string.proceed_to_pay));
                addressHeader.setText(CommonClass.getInstance().selectedAddress.getType());
                addressDetail.setText(CommonClass.getInstance().selectedAddress.getMapAddress());
                addressDeliveryTime.setText("30 Mins");
            } else {
                commonBtn.setBackgroundResource(R.drawable.button_corner_bg_theme);
                commonBtn.setText(getResources().getString(R.string.add_address_to_proceed));
                locationErrorLayout.setVisibility(View.VISIBLE);
                locationInfoLayout.setVisibility(View.GONE);

            }


        } else if (requestCode == ADDRESS_SELECTION && resultCode == Activity.RESULT_CANCELED) {
            System.out.print("CartFragment : Failure");

        }
    }
}
