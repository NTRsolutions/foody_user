package com.foodie.app.model;

/**
 * Created by CSS22 on 22-08-2017.
 */

public class RecommendedDish {
    String name, category, price, imgUrl;
    Boolean isVeg;
    public RecommendedDish(String name, String category, String price, Boolean isVeg, String url) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.isVeg = isVeg;
        this.imgUrl = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }

    public String getPrice(){
        return price;
    }
    public void setPrice(String price){
        this.price = price;
    }

    public Boolean getIsVeg(){
        return isVeg;
    }
    public void setIsVeg(Boolean isVeg){
        this.isVeg = isVeg;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String image) {
        this.imgUrl = image;
    }

}
