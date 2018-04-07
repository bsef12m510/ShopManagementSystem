package com.apptriangle.pos.sales.restInterface;

import com.apptriangle.pos.model.Brand;
import com.apptriangle.pos.sales.response.GetProductsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zeeshan on 4/7/2018.
 */
public interface SalesService {

    @GET("product/GetAllProducts")
    Call<List<GetProductsResponse>> getAllProducts(@Query("apiKey") String apiKey);

    @GET("product/GetAllBrandsForProductType")
    Call<List<Brand>> getBrands(@Query("apiKey") String apiKey, @Query("productTypeId") int productTypeId);

}
