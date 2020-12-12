package com.rs.royalgrocerystore.Data;

import com.rs.royalgrocerystore.Model.Cart;
import com.rs.royalgrocerystore.Model.PreviousOrderDetails;
import com.rs.royalgrocerystore.Model.PreviousOrders;
import com.rs.royalgrocerystore.Model.Products;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiDao {

    @POST("Mobileservices.aspx?")
    Call<List<Products>> getBanners(
            @Query("request") String request);

    @POST("Mobileservices.aspx?")
    Call<List<Products>> getGrocery(
            @Query("request") String request,
            @Query("category") String category,
            @Query("customerid") String customerid);

    @POST("Mobileservices.aspx?")
    Call<List<Products>> getVegetables(
            @Query("request") String request,
            @Query("category") String category,
            @Query("customerid") String customerid);

    @POST("Mobileservices.aspx?")
    Call<List<Products>> getNonVeg(
            @Query("request") String request,
            @Query("category") String category,
            @Query("customerid") String customerid);


    @POST("Mobileservices.aspx?")
    Call<List<Cart>> getCartItems(
            @Query("request") String request,
            @Query("customerid") String customerid);


    @POST("Mobileservices.aspx?")
    Call<List<PreviousOrders>> getPreviousOrders(
            @Query("request") String request,
            @Query("customerid") String customerid);

    @POST("Mobileservices.aspx?")
    Call<List<PreviousOrderDetails>> getPreviousOrderDetails(
            @Query("request") String request,
            @Query("orderid") String orderid);

    @POST("Mobileservices.aspx?")
    Call<List<Products>> getSearchItems(
            @Query("request") String request,
            @Query("text") String text);

}
