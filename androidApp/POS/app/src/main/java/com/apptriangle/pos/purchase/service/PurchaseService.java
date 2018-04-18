package com.apptriangle.pos.purchase.service;

import com.apptriangle.pos.model.UoM;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zeeshan on 4/19/2018.
 */
public interface PurchaseService {

    @GET("search/getMeasurementUnits")
    Call<List<UoM>> getMeasurementUnits(@Query("userId") String userId);
}
