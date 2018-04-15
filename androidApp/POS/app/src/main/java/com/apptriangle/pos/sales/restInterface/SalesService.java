package com.apptriangle.pos.sales.restInterface;

import com.apptriangle.pos.model.Brand;
import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.model.ProductType;
import com.apptriangle.pos.sales.response.GetProductsResponse;
import com.apptriangle.pos.sales.response.SalesResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zeeshan on 4/7/2018.
 */
public interface SalesService {

    @GET("product/GetAllProducts")
    Call<List<GetProductsResponse>> getAllProducts(@Query("apiKey") String apiKey);


    @GET("product/GetAllProductTypes")
    Call<List<ProductType>> getAllProductTypes(@Query("apiKey") String apiKey);

    @GET("product/GetAllBrandsForProductType")
    Call<List<Brand>> getBrands(@Query("apiKey") String apiKey, @Query("productTypeId") int productTypeId);

    @GET("product/GetAllModelsForProductType")
    Call<List<Product>> getModels(@Query("apiKey") String apiKey, @Query("productTypeId") int productTypeId,  @Query("brandId") int brandId);

    @POST("sale/SaleProduct")
    Call<Object> processSale(@Body SalesResponse sale);

}
