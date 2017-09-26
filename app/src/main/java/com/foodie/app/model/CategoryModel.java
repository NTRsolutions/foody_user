package com.foodie.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santhosh@appoets.com on 28-08-2017.
 */

public class CategoryModel {
    String header;
    List<Product> productList = new ArrayList<>();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setOrders(List<Product> productList) {
        this.productList = productList;
    }

}
