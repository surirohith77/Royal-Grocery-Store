package com.rs.royalgrocerystore.Model;

import com.google.gson.annotations.SerializedName;

public class Cart {

    @SerializedName("productid")
    int productid;

    @SerializedName("image")
    String  image;

    @SerializedName("product")
    String product;

    @SerializedName("qty")
    int qty;

    @SerializedName("price")
    Double price;

    @SerializedName("Total")
    Double Total;


    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double total) {
        Total = total;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
