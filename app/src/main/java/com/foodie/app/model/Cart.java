
package com.foodie.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cart {

    @SerializedName("id")
    @Expose
    private Integer id;
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
    @SerializedName("product")
    @Expose
    private Product product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
