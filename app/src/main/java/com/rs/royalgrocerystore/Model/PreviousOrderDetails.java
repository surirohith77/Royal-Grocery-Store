package com.rs.royalgrocerystore.Model;

import com.google.gson.annotations.SerializedName;

public class PreviousOrderDetails {

    @SerializedName("image")
    String image;

    @SerializedName("product")
    String product;

    @SerializedName("qty")
    int qty;

    @SerializedName("price")
    Double price;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
