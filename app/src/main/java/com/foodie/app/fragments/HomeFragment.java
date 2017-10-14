package com.foodie.app.fragments;

import android.app.Activity;
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
import com.foodie.app.HomeActivity;
import com.foodie.app.R;
import com.foodie.app.activities.FilterActivity;
import com.foodie.app.activities.SetDeliveryLocationActivity;
import com.foodie.app.adapter.DiscoverAdapter;
import com.foodie.app.adapter.BannerAdapter;
import com.foodie.app.adapter.OfferRestaurantAdapter;
import com.foodie.app.adapter.RestaurantsAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.helper.ConnectionHelper;
import com.foodie.app.model.Address;
import com.foodie.app.model.Banner;
import com.foodie.app.model.Cuisine;
import com.foodie.app.model.Discover;
import com.foodie.app.model.ImpressiveDish;
import com.foodie.app.model.Restaurant;
import com.foodie.app.model.RestaurantsData;
import com.foodie.app.model.Shop;
import com.foodie.app.utils.Utils;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.foodie.app.helper.CommonClass.addressList;
import static com.foodie.app.helper.CommonClass.cuisineIdArrayList;
import static com.foodie.app.helper.CommonClass.cuisineList;
import static com.foodie.app.helper.CommonClass.isOfferApplied;
import static com.foodie.app.helper.CommonClass.isPureVegApplied;
import static com.foodie.app.helper.CommonClass.latitude;
import static com.foodie.app.helper.CommonClass.longitude;
import static com.foodie.app.helper.CommonClass.selectedAddress;


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
    @BindView(R.id.offer_title_header)
    TextView offerTitleHeader;
    private SkeletonScreen skeletonScreen, skeletonScreen2, skeletonText1, skeletonText2, skeletonSpinner;
    private TextView addressLabel;
    private TextView addressTxt;
    private LinearLayout locationAddressLayout;
    private RelativeLayout errorLoadingLayout;

    private Button filterBtn;

    @BindView(R.id.restaurants_offer_rv)
    RecyclerView restaurantsOfferRv;
    @BindView(R.id.impressive_dishes_rv)
    RecyclerView bannerRv;
    @BindView(R.id.restaurants_rv)
    RecyclerView restaurantsRv;
    @BindView(R.id.discover_rv)
    RecyclerView discoverRv;
    int ADDRESS_SELECTION = 1;
    int FILTER_APPLIED_CHECK = 2;
    ImageView filterSelectionImage;

    private ViewGroup toolbar;
    private View toolbarLayout;
    AnimatedVectorDrawableCompat avdProgress;
    public static ArrayList<Integer> cuisineSelectedList = null;

    String[] catagoery = {"Relevance", "Cost for Two", "Delivery Time", "Rating"};

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    List<Shop> restaurantList;
    RestaurantsAdapter adapterRestaurant;
    public static boolean isFilterApplied = false;
    BannerAdapter bannerAdapter;
    List<Banner> bannerList;
    ConnectionHelper connectionHelper;
    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;

    }


    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        System.out.println("HomeFragment");
        connectionHelper= new ConnectionHelper(context);
        toolbar = (ViewGroup) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbarLayout = LayoutInflater.from(context).inflate(R.layout.toolbar_home, toolbar, false);
        addressLabel = (TextView) toolbarLayout.findViewById(R.id.address_label);
        addressTxt = (TextView) toolbarLayout.findViewById(R.id.address);

        locationAddressLayout = (LinearLayout) toolbarLayout.findViewById(R.id.location_ll);
        errorLoadingLayout = (RelativeLayout) toolbarLayout.findViewById(R.id.error_loading_layout);
        locationAddressLayout.setVisibility(View.INVISIBLE);
        errorLoadingLayout.setVisibility(View.VISIBLE);


        bannerList = new ArrayList<>();
        bannerAdapter = new BannerAdapter(bannerList, context, getActivity());
        bannerRv.setHasFixedSize(true);
        bannerRv.setItemViewCacheSize(20);
        bannerRv.setDrawingCacheEnabled(true);
        bannerRv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        bannerRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        bannerRv.setItemAnimator(new DefaultItemAnimator());
        skeletonScreen2 = Skeleton.bind(bannerRv)
                .adapter(bannerAdapter)
                .load(R.layout.skeleton_impressive_list_item)
                .count(3)
                .show();
        skeletonText1 = Skeleton.bind(offerTitleHeader)
                .load(R.layout.skeleton_label)
                .show();
        skeletonText2 = Skeleton.bind(restaurantCountTxt)
                .load(R.layout.skeleton_label)
                .show();
        skeletonSpinner = Skeleton.bind(catagoerySpinner)
                .load(R.layout.skeleton_label)
                .show();
        HomeActivity.updateNotificationCount(context, CommonClass.getInstance().notificationCount);

        //Spinner
        //Creating the ArrayAdapter instance having the country shopList
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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 5000ms
                findRestaurant();
                errorLoadingLayout.setVisibility(View.GONE);
                locationAddressLayout.setVisibility(View.VISIBLE);
                if (addressList != null && addressList.getAddresses().size() != 0) {
                    for (int i = 0; i < addressList.getAddresses().size(); i++) {
                        Address address1 = addressList.getAddresses().get(i);
                        if (getDoubleThreeDigits(latitude) == getDoubleThreeDigits(address1.getLatitude()) && getDoubleThreeDigits(longitude) == getDoubleThreeDigits(address1.getLongitude())) {
                            selectedAddress = address1;
                            addressLabel.setText(CommonClass.getInstance().addressHeader);
                            addressTxt.setText(CommonClass.getInstance().address);
                            addressLabel.setText(CommonClass.getInstance().selectedAddress.getType());
                            addressTxt.setText(CommonClass.getInstance().selectedAddress.getMapAddress());
                            latitude = CommonClass.getInstance().selectedAddress.getLatitude();
                            longitude = CommonClass.getInstance().selectedAddress.getLongitude();
                            break;
                        } else {
                            addressLabel.setText(CommonClass.getInstance().addressHeader);
                            addressTxt.setText(CommonClass.getInstance().address);
                        }
                    }
                } else {
                    addressLabel.setText(CommonClass.getInstance().addressHeader);
                    addressTxt.setText(CommonClass.getInstance().address);
                }

            }
        }, 3000);

        locationAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), SetDeliveryLocationActivity.class).putExtra("get_address", true), ADDRESS_SELECTION);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.anim_nothing);
            }
        });
        filterBtn = (Button) toolbarLayout.findViewById(R.id.filter);
        filterSelectionImage = (ImageView) toolbarLayout.findViewById(R.id.filter_selection_image);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, FilterActivity.class), FILTER_APPLIED_CHECK);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.anim_nothing);
            }
        });
        toolbar.addView(toolbarLayout);
        //intialize image line
        initializeAvd();

//Get cuisine values
        if (connectionHelper.isConnectingToInternet()) {
            getCuisines();
        } else {
            Utils.displayMessage(activity, context, getString(R.string.oops_connect_your_internet));
        }


    }

    private void findRestaurant() {
        HashMap<String, String> map = new HashMap<>();
        map.put("latitude", String.valueOf(latitude));
        map.put("longitude", String.valueOf(longitude));
        //get User Profile Data
        if (CommonClass.getInstance().profileModel != null) {
            map.put("user_id", String.valueOf(CommonClass.getInstance().profileModel.getId()));
        }
        if (isFilterApplied) {
            filterSelectionImage.setVisibility(View.VISIBLE);
            if (isOfferApplied)
                map.put("offer", "1");
            if (isPureVegApplied)
                map.put("pure_veg", "1");
            if (cuisineIdArrayList != null && cuisineIdArrayList.size() != 0) {
                for (int i = 0; i < cuisineIdArrayList.size(); i++) {
                    map.put("cuisine[" + "" + i + "]", cuisineIdArrayList.get(i).toString());
                }
            }

        } else {
            filterSelectionImage.setVisibility(View.GONE);
        }

        if (connectionHelper.isConnectingToInternet()) {
            getRestaurant(map);
        } else {
            Utils.displayMessage(activity, context, getString(R.string.oops_connect_your_internet));
        }

    }

    private void getRestaurant(HashMap<String, String> map) {
        Call<RestaurantsData> call = apiInterface.getshops(map);
        call.enqueue(new Callback<RestaurantsData>() {
            @Override
            public void onResponse(Call<RestaurantsData> call, Response<RestaurantsData> response) {
                skeletonScreen.hide();
                skeletonScreen2.hide();
                skeletonText1.hide();
                skeletonText2.hide();
                skeletonSpinner.hide();
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {

                    CommonClass.getInstance().shopList = response.body().getShops();
                    restaurantList.clear();
                    restaurantList.addAll(CommonClass.getInstance().shopList);
                    bannerList.addAll(response.body().getBanners());
                    restaurantCountTxt.setText("" + restaurantList.size() + " Restaurants");
                    adapterRestaurant.notifyDataSetChanged();
                    bannerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<RestaurantsData> call, Throwable t) {

            }
        });


    }


    public void getCuisines() {
        Call<List<Cuisine>> call = apiInterface.getcuCuisineCall();
        call.enqueue(new Callback<List<Cuisine>>() {
            @Override
            public void onResponse(Call<List<Cuisine>> call, Response<List<Cuisine>> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(context, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.isSuccessful()) {
                    cuisineList = new ArrayList<Cuisine>();
                    cuisineList.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Cuisine>> call, Throwable t) {

            }
        });


    }

    public double getDoubleThreeDigits(Double value) {
        Double outputValue = new BigDecimal(value.toString()).setScale(3, RoundingMode.HALF_UP).doubleValue();
        return outputValue;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.updateNotificationCount(context, CommonClass.getInstance().notificationCount);

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_SELECTION && resultCode == Activity.RESULT_OK) {
            System.out.print("HomeFragment : Success");
            if (CommonClass.getInstance().selectedAddress != null) {
                addressLabel.setText(CommonClass.getInstance().addressHeader);
                addressTxt.setText(CommonClass.getInstance().address);
                addressLabel.setText(CommonClass.getInstance().selectedAddress.getType());
                addressTxt.setText(CommonClass.getInstance().selectedAddress.getMapAddress());
                latitude = CommonClass.getInstance().selectedAddress.getLatitude();
                longitude = CommonClass.getInstance().selectedAddress.getLongitude();
                findRestaurant();

            } else {

            }
        } else if (requestCode == ADDRESS_SELECTION && resultCode == Activity.RESULT_CANCELED) {
            System.out.print("HomeFragment : Failure");

        }

        if (requestCode == FILTER_APPLIED_CHECK && resultCode == Activity.RESULT_OK) {
            System.out.print("HomeFragment : Filter Success");
            findRestaurant();


        } else if (requestCode == ADDRESS_SELECTION && resultCode == Activity.RESULT_CANCELED) {
            System.out.print("HomeFragment : Filter Failure");

        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(context, catagoery[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}