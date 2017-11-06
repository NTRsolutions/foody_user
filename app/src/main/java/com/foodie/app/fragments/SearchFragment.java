package com.foodie.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.adapter.ViewPagerAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.GlobalData;
import com.foodie.app.models.Product;
import com.foodie.app.models.Search;
import com.foodie.app.models.Shop;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



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
    @BindView(R.id.related_txt)
    TextView relatedTxt;
    EditText searchEt;
    ProgressBar progressBar;
    ImageView searchCloseImg;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
    private Context context;
    private ViewGroup toolbar;
    private View toolbarLayout;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    public static List<Shop> shopList;
    public static List<Product> productList;


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
        HomeActivity.updateNotificationCount(context, GlobalData.getInstance().notificationCount);
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
        shopList = new ArrayList<>();
        productList = new ArrayList<>();
        toolbar = (ViewGroup) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        rootLayout.setVisibility(View.GONE);
        toolbarLayout = LayoutInflater.from(context).inflate(R.layout.toolbar_search, toolbar, false);
        searchEt = (EditText) toolbarLayout.findViewById(R.id.search_et);
        progressBar = (ProgressBar) toolbarLayout.findViewById(R.id.progress_bar);
        searchCloseImg = (ImageView) toolbarLayout.findViewById(R.id.search_close_img);
        //ViewPager Adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new RestaurantSearchFragment(), "RESTAURANT");
        adapter.addFragment(new ProductSearchFragment(), "DISHES");
        viewPager.setAdapter(adapter);
        //set ViewPager
        tabLayout.setupWithViewPager(viewPager);
        searchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    getSearch(s);
                    searchCloseImg.setVisibility(View.VISIBLE);
                    rootLayout.setVisibility(View.VISIBLE);
                    relatedTxt.setText("Related to \"" + s.toString() + "\"");
                } else if (s.length() == 0) {
                    relatedTxt.setText("Related to ");
                    searchCloseImg.setVisibility(View.GONE);
                    rootLayout.setVisibility(View.GONE);
                    shopList.clear();
                    productList.clear();
                    relatedTxt.setText(s.toString());
//                    skeletonScreen.hide();
                    RestaurantSearchFragment.restaurantsAdapter.notifyDataSetChanged();
                }

            }
        });

        toolbar.addView(toolbarLayout);
        HomeActivity.updateNotificationCount(context, GlobalData.getInstance().notificationCount);
        searchCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEt.setText("");
                shopList.clear();
                productList.clear();
                ProductSearchFragment.productsAdapter.notifyDataSetChanged();
                RestaurantSearchFragment.restaurantsAdapter.notifyDataSetChanged();
            }
        });


    }

    private void getSearch(final CharSequence s) {
//        skeletonScreen.show();
        progressBar.setVisibility(View.VISIBLE);
        Call<Search> call = apiInterface.getSearch(s.toString());
        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                progressBar.setVisibility(View.GONE);
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    shopList.clear();
                    productList.clear();
                    shopList.addAll(response.body().getShops());
                    productList.addAll(response.body().getProducts());
//                    skeletonScreen.hide();
                    ProductSearchFragment.productsAdapter.notifyDataSetChanged();
                    RestaurantSearchFragment.restaurantsAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
            }
        });


    }


}
