package com.apptriangle.pos.purchase.service;

import com.apptriangle.pos.model.UoM;
import com.apptriangle.pos.purchase.response.JProduct;
import com.apptriangle.pos.purchase.response.PurchaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zeeshan on 4/19/2018.
 */
public interface PurchaseService {

    @GET("search/getMeasurementUnits")
    Call<List<UoM>> getMeasurementUnits(@Query("userId") String userId);

    @GET("product/GetAllModelsForProductType")
    Call<List<JProduct>> getModels(@Query("apiKey") String apiKey, @Query("productTypeId") int productTypeId,  @Query("brandId") int brandId);


    @POST("purchase/PurchaseProduct")
    Call<Object> processPurchase(@Body PurchaseResponse purchase);
}
