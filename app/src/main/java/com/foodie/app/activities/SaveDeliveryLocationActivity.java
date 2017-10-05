package com.foodie.app.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.helper.CommonClass;
import com.foodie.app.model.Message;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SaveDeliveryLocationActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.address)
    EditText addressEdit;
    @BindView(R.id.flat_no)
    EditText flatNoEdit;
    @BindView(R.id.landmark)
    EditText landmark;
    @BindView(R.id.dummy_image_view)
    ImageView dummyImageView;
    @BindView(R.id.animation_line_cart_add)
    ImageView animationLineCartAdd;
    @BindView(R.id.type_radiogroup)
    RadioGroup typeRadiogroup;
    @BindView(R.id.home_radio)
    RadioButton homeRadio;
    @BindView(R.id.work_radio)
    RadioButton workRadio;
    @BindView(R.id.other_radio)
    RadioButton otherRadio;

    private String TAG = "SaveDelivery";
    private BottomSheetBehavior behavior;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private int value = 0;
    private Double crtLat;
    private Double crtLng;
    private Double srcLat;
    private Double srcLng;
    AnimatedVectorDrawableCompat avdProgress;

    @BindView(R.id.backArrow)
    ImageView backArrow;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    com.foodie.app.model.Address address = null;
    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_delivery_location);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        address = new com.foodie.app.model.Address();
        address.setType("other");
        //Intialize Animation line
        initializeAvd();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
            } else {
                //Request Location Permission
            }
        } else {
            buildGoogleApiClient();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);

        //set state is expanded
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        dummyImageView.setVisibility(View.VISIBLE);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                // to collapse to show
                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    dummyImageView.setVisibility(View.GONE);

                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    dummyImageView.setVisibility(View.GONE);
                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    dummyImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events


            }

        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("place_id");
            String isEdit = extras.getString("edit");
            if (id != null) {
                Places.GeoDataApi.getPlaceById(mGoogleApiClient, id).setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            Place place = places.get(0);
                            addressEdit.setText(place.getAddress());
                            LatLng latLng = place.getLatLng();
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        } else {
                            System.out.println("Place not found");
                        }
                        places.release();
                    }
                });
            }

            if (isEdit != null && isEdit.equals("yes")) {
                if (CommonClass.selectedAddress != null) {
                    address = CommonClass.selectedAddress;
                    addressEdit.setText(address.getMapAddress());
                    flatNoEdit.setText(address.getBuilding());
                    landmark.setText(address.getLandmark());
                    if (address.getType().equals("home")) {
                        homeRadio.setChecked(true);
                    } else if (address.getType().equals("work")) {
                        workRadio.setChecked(true);
                    } else {
                        otherRadio.setChecked(true);
                    }
                }
            }

        }

        typeRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(i);
                address.setType(radioButton.getText().toString().toLowerCase());
            }
        });


    }

    Runnable action = new Runnable() {
        @Override
        public void run() {
            avdProgress.stop();
            if (animationLineCartAdd != null)
                animationLineCartAdd.setVisibility(View.INVISIBLE);
        }
    };

    private void initializeAvd() {
        avdProgress = AnimatedVectorDrawableCompat.create(getApplicationContext(), R.drawable.avd_line);
        animationLineCartAdd.setBackground(avdProgress);
        repeatAnimation();
    }

    private void repeatAnimation() {
        animationLineCartAdd.setVisibility(View.VISIBLE);
        avdProgress.start();
        animationLineCartAdd.postDelayed(action, 1500); // Will repeat animation in every 1 second
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
            if (!success) {
                Log.i("Map:Style", "Style parsing failed.");
            } else {
                Log.i("Map:Style", "Style Applied.");
            }
        } catch (Resources.NotFoundException e) {
            Log.i("Map:Style", "Can't find style. Error: ");
        }

        mMap = googleMap;

        if (mMap != null) {
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.setOnCameraMoveListener(this);
            mMap.setOnCameraIdleListener(this);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);
        }

        /*LatLng sydney = new LatLng(13.080703, 80.200733);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }*/
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (value == 0) {
            value = 1;
            if (address.getId() == null) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(16).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else {
                LatLng loc = new LatLng(address.getLatitude(), address.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(16).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
            //getAddress(location.getLatitude(), location.getLongitude());
        }
        crtLat = location.getLatitude();
        crtLng = location.getLongitude();
    }

    private void getAddress(double latitude, double longitude) {
        System.out.println("GetAddress " + latitude + " | " + longitude);
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                if (returnedAddress.getMaxAddressLineIndex() > 0) {
                    for (int j = 0; j < returnedAddress.getMaxAddressLineIndex(); j++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(j)).append("");
                    }
                } else {
                    strReturnedAddress.append(returnedAddress.getAddressLine(0)).append("");
                }
                addressEdit.setText(strReturnedAddress.toString());
                Address obj = addresses.get(0);

                address.setCity(obj.getLocality());
                address.setState(obj.getAdminArea());
                address.setCountry(obj.getCountryName());
                address.setLatitude(obj.getLatitude());
                address.setLongitude(obj.getLongitude());
                address.setPincode(obj.getPostalCode());
                //SharedHelper.putKey(context, "pickup_address", strReturnedAddress.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraIdle() {
        try {
            CameraPosition cameraPosition = mMap.getCameraPosition();
            srcLat = cameraPosition.target.latitude;
            srcLng = cameraPosition.target.longitude;

            //Intialize animation line
            initializeAvd();
            getAddress(srcLat, srcLng);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraMove() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        dummyImageView.setVisibility(View.GONE);
        addressEdit.setText(this.getResources().getString(R.string.getting_address));
        animationLineCartAdd.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void saveAddress() {
        if (address != null && address.getMapAddress() != null && validate()) {
            Call<com.foodie.app.model.Address> call = apiInterface.saveAddress(address);
            call.enqueue(new Callback<com.foodie.app.model.Address>() {
                @Override
                public void onResponse(Call<com.foodie.app.model.Address> call, Response<com.foodie.app.model.Address> response) {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(SaveDeliveryLocationActivity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(SaveDeliveryLocationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else if (response != null && response.isSuccessful()) {
                        CommonClass.selectedAddress = response.body();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<com.foodie.app.model.Address> call, Throwable t) {
                    Log.e(TAG, t.toString());
                    Toast.makeText(SaveDeliveryLocationActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void updateAddress() {
        if (address != null && address.getId() != null && validate()) {
            Call<com.foodie.app.model.Address> call = apiInterface.updateAddress(address.getId(), address);
            call.enqueue(new Callback<com.foodie.app.model.Address>() {
                @Override
                public void onResponse(Call<com.foodie.app.model.Address> call, Response<com.foodie.app.model.Address> response) {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(SaveDeliveryLocationActivity.this, jObjError.optString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(SaveDeliveryLocationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else if (response != null && response.isSuccessful()) {
                        CommonClass.selectedAddress = response.body();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<com.foodie.app.model.Address> call, Throwable t) {
                    Log.e(TAG, t.toString());
                    Toast.makeText(SaveDeliveryLocationActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean validate(){
        if(address.getMapAddress().isEmpty() && address.getMapAddress().equals(getResources().getString(R.string.getting_address))){
            Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show();
            return false;
        }else if(address.getBuilding().isEmpty()){
            Toast.makeText(this, "Please enter Flat No", Toast.LENGTH_SHORT).show();
            return false;
        }else if(address.getLandmark().isEmpty()){
            Toast.makeText(this, "Please enter landmark", Toast.LENGTH_SHORT).show();
            return false;
        }else if(address.getLatitude() == null || address.getLongitude() == null){
            Toast.makeText(this, "Lat & long cannot be left blank", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

    @OnClick({R.id.backArrow, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                onBackPressed();
                break;
            case R.id.save:
                address.setMapAddress(addressEdit.getText().toString());
                address.setBuilding(flatNoEdit.getText().toString());
                address.setLandmark(landmark.getText().toString());

                if (address.getId() != null) {
                    updateAddress();
                } else {
                    saveAddress();
                }
                break;
        }
    }
}
