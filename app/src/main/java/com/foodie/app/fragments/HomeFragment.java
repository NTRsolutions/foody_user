package com.foodie.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amar.library.ui.StickyScrollView;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.foodie.app.LocationUtil.LocationHelper;
import com.foodie.app.LocationUtil.PermissionUtils;
import com.foodie.app.R;
import com.foodie.app.activities.FilterActivity;
import com.foodie.app.activities.SetDeliveryLocationActivity;
import com.foodie.app.adapter.DiscoverAdapter;
import com.foodie.app.adapter.ImpressiveDishesAdapter;
import com.foodie.app.adapter.OfferRestaurantAdapter;
import com.foodie.app.adapter.RestaurantsAdapter;
import com.foodie.app.model.Discover;
import com.foodie.app.model.ImpressiveDish;
import com.foodie.app.model.Restaurant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by santhosh@appoets.com on 22-08-2017.
 */

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback {


    @BindView(R.id.animation_line_image)
    ImageView animationLineImage;
    Context context;
    @BindView(R.id.catagoery_spinner)
    Spinner catagoerySpinner;
    @BindView(R.id.title)
    LinearLayout title;
    @BindView(R.id.scrollView)
    StickyScrollView scrollView;
    private SkeletonScreen skeletonScreen, skeletonScreen2;
    private TextView addressLabel;
    private TextView addressTxt;
    private LinearLayout locationLl;
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


    private Location mLastLocation;

    double latitude;
    double longitude;
    LocationHelper locationHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        locationHelper = new LocationHelper(getActivity());
        locationHelper.checkpermission();

        // check availability of play services
        if (locationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            locationHelper.buildGoogleApiClient();
        }


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 5000ms
                if (locationHelper.isPermissionGranted) {
                    mLastLocation = locationHelper.getLocation();
                    if (mLastLocation != null) {
                        latitude = mLastLocation.getLatitude();
                        longitude = mLastLocation.getLongitude();
                        getAddress();

                    } else {

                        showToast("Couldn't get the location. Make sure location is enabled on the device");
                    }
                }
            }
        }, 5000);


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
        final ArrayList<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant("Madras Coffee House", "Cafe, South Indian", "", "3.8", "51 Mins", "$20", ""));
        restaurantList.add(new Restaurant("Behrouz Biryani", "Biriyani", "", "3.7", "52 Mins", "$50", ""));
        restaurantList.add(new Restaurant("SubWay", "American fast food", "Flat 20% offer on all orders", "4.3", "30 Mins", "$5", "Close soon"));
        restaurantList.add(new Restaurant("Dominoz Pizza", "Pizza shop", "", "4.5", "25 Mins", "$5", ""));
        restaurantList.add(new Restaurant("Pizza hut", "Cafe, Bakery", "", "4.1", "45 Mins", "$5", "Close soon"));
        restaurantList.add(new Restaurant("McDonlad's", "Pizza Food, burger", "Flat 20% offer on all orders", "4.6", "20 Mins", "$5", ""));
        restaurantList.add(new Restaurant("Chai Kings", "Cafe, Bakery", "", "3.3", "36 Mins", "$5", ""));
        restaurantList.add(new Restaurant("sea sell", "Fish, Chicken, mutton", "Flat 30% offer on all orders", "4.3", "20 Mins", "$5", "Close soon"));
        RestaurantsAdapter adapterRestaurant = new RestaurantsAdapter(restaurantList, context, getActivity());
        skeletonScreen = Skeleton.bind(restaurantsRv)
                .adapter(adapterRestaurant)
                .load(R.layout.skeleton_restaurant_list_item)
                .count(2)
                .show();

        //Offer Restaurant Adapter
        restaurantsOfferRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        restaurantsOfferRv.setItemAnimator(new DefaultItemAnimator());
        restaurantsOfferRv.setHasFixedSize(true);
        OfferRestaurantAdapter offerAdapter = new OfferRestaurantAdapter(restaurantList, context);
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
        locationHelper.checkPlayServices();
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


    public void getAddress() {
        Address locationAddress;

        locationAddress = locationHelper.getAddress(latitude, longitude);

        if (locationAddress != null) {

            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();


            String currentLocation;

            if (!TextUtils.isEmpty(address)) {
                currentLocation = address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation += "\n" + address1;

                if (!TextUtils.isEmpty(city)) {
                    currentLocation += "\n" + city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += " - " + postalCode;
                } else {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += "\n" + postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation += "\n" + state;

                if (!TextUtils.isEmpty(country))
                    currentLocation += "\n" + country;

                Log.e("Current_location", currentLocation);

                addressLabel.setText(address);
                addressTxt.setText(currentLocation);
                addressTxt.setVisibility(View.VISIBLE);

            }

        } else
            showToast("Something went wrong");
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

        locationLl = (LinearLayout) toolbarLayout.findViewById(R.id.location_ll);
        locationLl.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationHelper.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Connection failed:", " ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        mLastLocation = locationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        locationHelper.connectApiClient();
    }


    // Permission check functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


}
