package com.foodie.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.foodie.app.R;
import com.foodie.app.adapter.DeliveryLocationAdapter;
import com.foodie.app.model.Location;
import com.foodie.app.model.LocationModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SetDeliveryLocationActivity extends AppCompatActivity {
    public String TAG = "DeliveryLocationActi";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    LinearLayoutManager manager;
    DeliveryLocationAdapter adapter;
    List<LocationModel> modelListReference = new ArrayList<>();
    @BindView(R.id.delivery_location_rv)
    RecyclerView deliveryLocationRv;
    @BindView(R.id.current_location_ll)
    LinearLayout currentLocationLl;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @BindView(R.id.find_place_ll)
    LinearLayout findPlaceLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_delivery_location);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        manager = new LinearLayoutManager(this);
        deliveryLocationRv.setLayoutManager(manager);
        adapter = new DeliveryLocationAdapter(this, modelListReference);
        deliveryLocationRv.setAdapter(adapter);

    }


    @Override
    public void onResume() {
        super.onResume();

        List<LocationModel> modelList = new ArrayList<>();

        List<Location> locations = new ArrayList<>();
        locations.add(new Location("Home", "Madhavaram, Chennai", 1));
        locations.add(new Location("Work", "Greems Road, Chennai", 2));
        locations.add(new Location("Karthik Home", "Reteri, Anna salai, Chennai", 0));
        LocationModel model = new LocationModel();
        model.setHeader("Saved Address");
        model.setLocations(locations);
        modelList.add(model);

        locations = new ArrayList<>();
        locations.add(new Location("Anna Nager", "Koyambedu Busstand, Chennai", 0));
        locations.add(new Location("Thousand Lights", "9629071600", 0));
        model = new LocationModel();
        model.setHeader("Recent Searches");
        model.setLocations(locations);
        modelList.add(model);

        modelListReference.clear();
        modelListReference.addAll(modelList);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Intent intent = new Intent(SetDeliveryLocationActivity.this, SaveDeliveryLocationActivity.class);
                intent.putExtra("place_id", place.getId());
                startActivity(intent);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    /*private void SetPlaceAutocomplete() {

        ((View) findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
        EditText search = ((EditText) findViewById(R.id.place_autocomplete_search_input));
        search.setHintTextColor(getResources().getColor(R.color.colorSecondaryText));
        search.setTextSize(14);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Search for area, street name...");

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("IN").build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Intent intent = new Intent(SetDeliveryLocationActivity.this, SaveDeliveryLocationActivity.class);
                intent.putExtra("place_id", place.getId());
                startActivity(intent);
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }*/

    @OnClick({R.id.find_place_ll, R.id.current_location_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.find_place_ll:
                findPlace();
                break;
            case R.id.current_location_ll:
                startActivity(new Intent(SetDeliveryLocationActivity.this, SaveDeliveryLocationActivity.class));
                break;
        }
    }

    private void findPlace() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {

        } catch (GooglePlayServicesNotAvailableException e) {

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
