package com.foodie.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.app.R;
import com.foodie.app.adapter.FavouritesAdapter;
import com.foodie.app.build.api.ApiClient;
import com.foodie.app.build.api.ApiInterface;
import com.foodie.app.model.Available;
import com.foodie.app.model.FavListModel;
import com.foodie.app.model.FavoriteList;
import com.foodie.app.model.UnAvailable;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FavouritesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.favorites_Rv)
    RecyclerView favoritesRv;

    ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
    private FavouritesAdapter adapter;
    private List<FavListModel> modelListReference = new ArrayList<>();
    List<FavListModel> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        LinearLayoutManager manager = new LinearLayoutManager(this);
        favoritesRv.setLayoutManager(manager);
        adapter = new FavouritesAdapter(this, modelListReference);
        favoritesRv.setAdapter(adapter);
        getFavorites();


    }


    private void getFavorites() {


        Call<FavoriteList> call=apiInterface.getFavoriteList();
        call.enqueue(new Callback<FavoriteList>() {
            @Override
            public void onResponse(Call<FavoriteList> call, Response<FavoriteList> response) {
                if(response.isSuccessful()){
                    FavListModel model = new FavListModel();
                    model.setHeader("available");
                    model.setFav(response.body().getAvailable());
                    modelList.add(model);

                    model = new FavListModel();
                    model.setHeader("un available");



                    List<Available> list = new ArrayList<>();
                    for (UnAvailable obj : response.body().getUnAvailable()) {
                        Gson gson = new Gson();
                        String json = gson.toJson(obj);
                        Available cust = gson.fromJson(json, Available.class);
                        list.add(cust);
                    }
                    model.setFav(list);
                    modelList.add(model);

                    modelListReference.clear();
                    modelListReference.addAll(modelList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FavoriteList> call, Throwable t) {
                Toast.makeText(FavouritesActivity.this, "Something wrong - getFavorites", Toast.LENGTH_LONG).show();
            }
        });

        /*Call<FavoriteList> call = apiInterface.getFavoriteList();
        call.enqueue(new Callback<FavoriteList>() {
            @Override
            public void onResponse(@NonNull Call<FavoriteList> call, @NonNull Response<FavoriteList> response) {
                if (response.errorBody() != null) {

                } else if(response.isSuccessful()){
                    FavListModel model = new FavListModel();
                    model.setHeader("available");
                    model.setFav(response.body().getAvailable());
                    modelList.add(model);

                    *//*model = new FavListModel();
                    model.setHeader("un available");
                    List<Available> list = new ArrayList<>();
                    for (UnAvailable obj : response.body().getUnAvailable()) {
                        list.add(obj);
                    }
                    model.setFav(list);
                    modelList.add(model);*//*

                    modelListReference.clear();
                    modelListReference.addAll(modelList);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(@NonNull Call<FavoriteList> call, @NonNull Throwable t) {

            }
        });*/


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_nothing, R.anim.slide_out_right);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
