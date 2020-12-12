package com.rs.royalgrocerystore.Model;

import com.google.gson.annotations.SerializedName;

public class PreviousOrders {

    @SerializedName("date")
    String date;

    @SerializedName("orderid")
    int orderid;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }
}
