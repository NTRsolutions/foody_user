package com.foodie.app.model;

/**
 * Created by santhosh@appoets.com on 30-08-2017.
 */

public class Order {

    public String restaurantName, restaurantAddress, totalAmount, dishName, dateTime;

    public Order(String restaurantName, String restaurantAddress, String totalAmount, String dishName, String dateTime) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.totalAmount = totalAmount;
        this.dishName = dishName;
        this.dateTime = dateTime;
    }
}
