package com.foodie.app.model;

/**
 * Created by santhosh@appoets.com on 24-08-2017.
 */

public class FavouriteDish {
    String name, category, imgUrl;

    public FavouriteDish(String name, String category, String url) {
        this.name = name;
        this.category = category;
        this.imgUrl = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String image) {
        this.imgUrl = image;
    }

}
