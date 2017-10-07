package com.foodie.app.model;

/**
 * Created by santhosh@appoets.com on 30-08-2017.
 */

public class Promotions {

    public String promotionsDate, promotionCode,promotionAmount;

    public Promotions(String name, String price, String validity) {
        this.promotionsDate = name;
        this.promotionCode = price;
        this.promotionAmount = validity;
    }
}
