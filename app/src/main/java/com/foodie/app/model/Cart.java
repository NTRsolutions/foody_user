package com.foodie.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamil on 10/5/2017.
 */

public class Cart {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("promocode_id")
    @Expose
    private Object promocodeId;
    @SerializedName("order_id")
    @Expose
    private Object orderId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("savedforlater")
    @Expose
    private Integer savedforlater;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Object getPromocodeId() {
        return promocodeId;
    }

    public void setPromocodeId(Object promocodeId) {
        this.promocodeId = promocodeId;
    }

    public Object getOrderId() {
        return orderId;
    }

    public void setOrderId(Object orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSavedforlater() {
        return savedforlater;
    }

    public void setSavedforlater(Integer savedforlater) {
        this.savedforlater = savedforlater;
    }

}
