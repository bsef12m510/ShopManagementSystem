package com.apptriangle.pos.dashboard.service;

import com.apptriangle.pos.Login.response.LoginResponse;
import com.apptriangle.pos.dashboard.response.MonthlySalesResponse;
import com.apptriangle.pos.dashboard.response.TodaySaleResponse;
import com.apptriangle.pos.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DashboardService {
    @GET("product/GetProductsSoldToday")
    Call<List<Product>> getTopSellingProducts(@Query("apiKey") String apiKey);

    @GET("product/GetLowStockProducts")
    Call<List<Product>> getLowStockProducts(@Query("apiKey") String apiKey);

    @GET("product/getInventory")
    Call<List<Product>> getInventory(@Query("apiKey") String apiKey);

    @GET("reports/GetSalesAmountByMonth")
    Call<List<MonthlySalesResponse>> getSalesAmountByMonth(@Query("apiKey") String apiKey);

    @GET("reports/GetTodaySale")
    Call<List<TodaySaleResponse>> getTodaySale(@Query("apiKey") String apiKey);
}
