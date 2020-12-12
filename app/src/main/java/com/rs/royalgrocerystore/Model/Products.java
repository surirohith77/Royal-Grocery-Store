package com.rs.royalgrocerystore.Model;

import com.google.gson.annotations.SerializedName;

public class Products {

    @SerializedName("category")
    String category;

    @SerializedName("product")
    String product;

    @SerializedName("MRP")
    String MRP;

    @SerializedName("rgsprice")
    String rgsprice;

    @SerializedName("save")
    Double save;

    @SerializedName("image")
    String image;

    @SerializedName("id")
    int id;

    @SerializedName("cartqy")
    int cartqy;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getRgsprice() {
        return rgsprice;
    }

    public void setRgsprice(String rgsprice) {
        this.rgsprice = rgsprice;
    }

    public Double getSave() {
        return save;
    }

    public void setSave(Double save) {
        this.save = save;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartqy() {
        return cartqy;
    }

    public void setCartqy(int cartqy) {
        this.cartqy = cartqy;
    }
}
