package com.foodie.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tamil on 10/11/2017.
 */

public class FavoriteList {

    @SerializedName("avalabilable")
    @Expose
    private List<Avalabilable> avalabilable = null;
    @SerializedName("un_available")
    @Expose
    private List<UnAvailable> unAvailable = null;

    public List<Avalabilable> getAvalabilable() {
        return avalabilable;
    }

    public void setAvalabilable(List<Avalabilable> avalabilable) {
        this.avalabilable = avalabilable;
    }

    public List<UnAvailable> getUnAvailable() {
        return unAvailable;
    }

    public void setUnAvailable(List<UnAvailable> unAvailable) {
        this.unAvailable = unAvailable;
    }

}