package com.apptriangle.pos.stock.service;

import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.stock.response.StockResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zeeshan on 4/19/2018.
 */
public interface StockService {

    @GET("search/SearchByProduct")
    Call<List<StockResponse>> searchByProduct(@Query("userId") String userId, @Query("product") String product);

    @GET("search/SearchByBrand")
    Call<List<StockResponse>> searchByBrand(@Query("apiKey") String apiKey, @Query("brandName") String brandName);

    @GET("search/SearchByModel")
    Call<List<StockResponse>> searchByModel(@Query("apiKey") String apiKey, @Query("model") String model);

    @GET("product/GetAllProducts")
    Call<List<StockResponse>> getAllInventoryProducts(@Query("apiKey") String apiKey);
}
