package com.apptriangle.pos.reports.service;

import com.apptriangle.pos.model.Product;
import com.apptriangle.pos.model.Sale;
import com.apptriangle.pos.model.User;

import java.sql.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zeeshan on 4/30/2018.
 */
public interface ReportService {

    @GET("reports/stockReportByDate")
    Call<List<Product>> getReportData(@Query("apiKey") String apiKey, @Query("dateFromString") String dateFrom,@Query("dateToString") String dateTo,
                                      @Query("productTypeId") int productTypeId,  @Query("brandId") int brandId,@Query("agentId") String agentId);

    @GET("reports/salesReport")
    Call<List<Sale>> getSalesReportData(@Query("apiKey") String apiKey, @Query("dateFromString") String dateFrom,@Query("dateToString") String dateTo,
                                      @Query("productTypeId") int productTypeId,  @Query("brandId") int brandId,@Query("agentId") String agentId);

    @GET("search/getUsersByShop")
    Call<List<User>> getUsers(@Query("apiKey") String apiKey );
}
