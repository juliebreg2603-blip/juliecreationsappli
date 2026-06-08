package com.example.juliecreations.api;

import com.example.juliecreations.model.HydraCollection;
import com.example.juliecreations.model.Product;
import com.example.juliecreations.model.User;
import com.example.juliecreations.model.Order;
import com.example.juliecreations.model.RegisterRequest;
import com.example.juliecreations.model.LoginRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("products")
    Call<HydraCollection<Product>> getProducts();

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int id);

    @GET("users/{id}")
    Call<User> getProfile(@Path("id") int id);

    @GET("users")
    Call<HydraCollection<User>> getUsers();

    @GET("orders")
    Call<HydraCollection<Order>> getOrdersByUser(@Query("user") String userIri);

    @POST("register")
    Call<User> register(@Body RegisterRequest request);

    @POST("login")
    Call<User> login(@Body LoginRequest request);
}