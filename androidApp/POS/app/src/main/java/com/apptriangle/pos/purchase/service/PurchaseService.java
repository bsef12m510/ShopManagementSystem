package com.apptriangle.pos.purchase.service;

import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.model.UoM;
import com.apptriangle.pos.purchase.response.JProduct;
import com.apptriangle.pos.purchase.response.PurchaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zeeshan on 4/19/2018.
 */
public interface PurchaseService {

    @GET("search/getMeasurementUnits")
    Call<List<UoM>> getMeasurementUnits(@Query("apiKey") String apiKey);

    @GET("product/GetAllModelsForProductType")
    Call<List<JProduct>> getModels(@Query("apiKey") String apiKey, @Query("productTypeId") int productTypeId,  @Query("brandId") int brandId);


    @POST("purchase/PurchaseProduct")
    Call<Object> processPurchase(@Body PurchaseResponse purchase);

    @GET("values/deleteProductType")
    Call<Object> deleteProductType(@Query("apiKey") String apiKey, @Query("p") Integer id);

    @GET("values/deleteProduct")
    Call<Object> deleteProduct(@Query("apiKey") String apiKey, @Query("p") Integer id);

    @GET("values/deleteBrand")
    Call<Object> deleteBrand(@Query("apiKey") String apiKey, @Query("brand") Integer id);

}
