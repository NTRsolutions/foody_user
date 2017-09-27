package com.foodie.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amar.library.ui.StickyScrollView;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.foodie.app.R;
import com.foodie.app.activities.FilterActivity;
import com.foodie.app.activities.SetDeliveryLocationActivity;
import com.foodie.app.adapter.DiscoverAdapter;
import com.foodie.app.adapter.ImpressiveDishesAdapter;
import com.foodie.app.adapter.OfferRestaurantAdapter;
import com.foodie.app.adapter.RestaurantsAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.model.Discover;
import com.foodie.app.model.ImpressiveDish;
import com.foodie.app.model.Restaurant;
import com.foodie.app.model.ShopsModel;
import com.foodie.app.helper.CommonClass;

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

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    @BindView(R.id.animation_line_image)
    ImageView animationLineImage;
    Context context;
    @BindView(R.id.catagoery_spinner)
    Spinner catagoerySpinner;
    @BindView(R.id.title)
    LinearLayout title;
    @BindView(R.id.scrollView)
    StickyScrollView scrollView;
    @BindView(R.id.restaurant_count_txt)
    TextView restaurantCountTxt;
    private SkeletonScreen skeletonScreen, skeletonScreen2;
    private TextView addressLabel;
    private TextView addressTxt;
    private LinearLayout locationAddressLayout;
    private RelativeLayout errorLoadingLayout;

    private Button filterBtn;

    @BindView(R.id.restaurants_offer_rv)
    RecyclerView restaurantsOfferRv;
    @BindView(R.id.impressive_dishes_rv)
    RecyclerView impressiveDishesRv;
    @BindView(R.id.restaurants_rv)
    RecyclerView restaurantsRv;
    @BindView(R.id.discover_rv)
    RecyclerView discoverRv;

    private ViewGroup toolbar;
    private View toolbarLayout;
    AnimatedVectorDrawableCompat avdProgress;

    String[] catagoery = {"Relevance", "Cost for Two", "Delivery Time", "Rating"};

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    List<ShopsModel> restaurantList;
    RestaurantsAdapter adapterRestaurant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        final ArrayList<ImpressiveDish> list = new ArrayList<>();
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


        ImpressiveDishesAdapter adapter = new ImpressiveDishesAdapter(list, context);
        impressiveDishesRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        impressiveDishesRv.setItemAnimator(new DefaultItemAnimator());
        skeletonScreen2 = Skeleton.bind(impressiveDishesRv)
                .adapter(adapter)
                .load(R.layout.skeleton_impressive_list_item)
                .count(3)
                .show();
//        impressiveDishesRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new ImpressiveDishesAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                ImpressiveDish dish = list.get(position);
                Log.d("Hello", "onItemClick position: " + dish.getName());
            }
        });

        //Spinner
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(context, R.layout.spinner_layout, catagoery);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        catagoerySpinner.setAdapter(aa);
        catagoerySpinner.setOnItemSelectedListener(this);


        //Restaurant Adapter
        restaurantsRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        restaurantsRv.setItemAnimator(new DefaultItemAnimator());
        restaurantsRv.setHasFixedSize(true);
        restaurantList = new ArrayList<>();
        adapterRestaurant = new RestaurantsAdapter(restaurantList, context, getActivity());
        skeletonScreen = Skeleton.bind(restaurantsRv)
                .adapter(adapterRestaurant)
                .load(R.layout.skeleton_restaurant_list_item)
                .count(2)
                .show();
        getRestaurant();

        final ArrayList<Restaurant> offerRestaurantList = new ArrayList<>();
        offerRestaurantList.add(new Restaurant("Madras Coffee House", "Cafe, South Indian", "", "3.8", "51 Mins", "$20", ""));
        offerRestaurantList.add(new Restaurant("Behrouz Biryani", "Biriyani", "", "3.7", "52 Mins", "$50", ""));
        offerRestaurantList.add(new Restaurant("SubWay", "American fast food", "Flat 20% offer on all orders", "4.3", "30 Mins", "$5", "Close soon"));
        offerRestaurantList.add(new Restaurant("Dominoz Pizza", "Pizza shop", "", "4.5", "25 Mins", "$5", ""));
        offerRestaurantList.add(new Restaurant("Pizza hut", "Cafe, Bakery", "", "4.1", "45 Mins", "$5", "Close soon"));
        offerRestaurantList.add(new Restaurant("McDonlad's", "Pizza Food, burger", "Flat 20% offer on all orders", "4.6", "20 Mins", "$5", ""));
        offerRestaurantList.add(new Restaurant("Chai Kings", "Cafe, Bakery", "", "3.3", "36 Mins", "$5", ""));
        offerRestaurantList.add(new Restaurant("sea sell", "Fish, Chicken, mutton", "Flat 30% offer on all orders", "4.3", "20 Mins", "$5", "Close soon"));
        //Offer Restaurant Adapter
        restaurantsOfferRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        restaurantsOfferRv.setItemAnimator(new DefaultItemAnimator());
        restaurantsOfferRv.setHasFixedSize(true);
        OfferRestaurantAdapter offerAdapter = new OfferRestaurantAdapter(offerRestaurantList, context);
        restaurantsOfferRv.setAdapter(offerAdapter);


        // Discover
        final List<Discover> discoverList = new ArrayList<>();
        discoverList.add(new Discover("Trending now ", "22 options", "1"));
        discoverList.add(new Discover("Offers near you", "51 options", "1"));
        discoverList.add(new Discover("Whats special", "7 options", "1"));
        discoverList.add(new Discover("Pocket Friendly", "44 options", "1"));

        DiscoverAdapter adapterDiscover = new DiscoverAdapter(discoverList, context);
        discoverRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        discoverRv.setItemAnimator(new DefaultItemAnimator());
        discoverRv.setAdapter(adapterDiscover);
        adapterDiscover.setOnItemClickListener(new DiscoverAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Discover obj = discoverList.get(position);
            }
        });

        return view;

    }

    private void getRestaurant() {
        Call<List<ShopsModel>> getres = apiInterface.getshops(CommonClass.getInstance().latitude, CommonClass.getInstance().longitude);
        getres.enqueue(new Callback<List<ShopsModel>>() {
            @Override
            public void onResponse(Call<List<ShopsModel>> call, Response<List<ShopsModel>> response) {
                CommonClass.getInstance().list = response.body();
//                List<Cuisine> listCuisine = list.get(0).getCuisines();
                restaurantList.clear();
                restaurantList.addAll(CommonClass.getInstance().list);
                restaurantCountTxt.setText("" + restaurantList.size() + " Restaurants");
                adapterRestaurant.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ShopsModel>> call, Throwable t) {

            }
        });
    }

    Runnable action = new Runnable() {
        @Override
        public void run() {
            avdProgress.stop();
            if (animationLineImage != null)
                animationLineImage.setVisibility(View.INVISIBLE);
        }
    };

    private void initializeAvd() {
        avdProgress = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_line);
        animationLineImage.setBackground(avdProgress);
        repeatAnimation();
    }

    private void repeatAnimation() {
        avdProgress.start();
        animationLineImage.postDelayed(action, 3000); // Will repeat animation in every 1 second
    }

    @Override
    public void onStart() {
        super.onStart();
        restaurantsRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                skeletonScreen.hide();
                skeletonScreen2.hide();
            }
        }, 3000);
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


    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        System.out.println("HomeFragment");

        toolbar = (ViewGroup) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbarLayout = LayoutInflater.from(context).inflate(R.layout.toolbar_home, toolbar, false);
        addressLabel = (TextView) toolbarLayout.findViewById(R.id.address_label);
        addressTxt = (TextView) toolbarLayout.findViewById(R.id.address);

        locationAddressLayout = (LinearLayout) toolbarLayout.findViewById(R.id.location_ll);
        errorLoadingLayout = (RelativeLayout) toolbarLayout.findViewById(R.id.error_loading_layout);
        locationAddressLayout.setVisibility(View.INVISIBLE);
        errorLoadingLayout.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 5000ms
                errorLoadingLayout.setVisibility(View.GONE);
                locationAddressLayout.setVisibility(View.VISIBLE);
                addressLabel.setText(CommonClass.getInstance().addressHeader);
                addressTxt.setText(CommonClass.getInstance().address);

            }
        }, 3000);


        locationAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SetDeliveryLocationActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
            }
        });
        filterBtn = (Button) toolbarLayout.findViewById(R.id.filter);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FilterActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_nothing);
            }
        });
        toolbar.addView(toolbarLayout);
        //intialize image line
        initializeAvd();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, catagoery[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}
