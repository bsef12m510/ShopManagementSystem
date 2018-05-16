package com.apptriangle.pos.InvoiceSearchFragment.service;

import com.apptriangle.pos.model.Invoice;
import com.apptriangle.pos.sales.response.GetProductsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InvoiceService {

    @GET("invoice/GetSalesInvoice")
    Call<Invoice> getInvoiceById(@Query("apiKey") String apiKey, @Query("invoiceId") String invoiceId);

    @GET("invoice/GetSalesInvoiceByCell")
    Call<List<Invoice>> getInvoiceByCell(@Query("apiKey") String apiKey, @Query("cust_phone") String cust_phone);

    @GET("invoice/clearSaleInvoicePayment")
    Call<Object> updatePayment(@Query("apiKey") String apiKey, @Query("invoiceId") int invoiceId, @Query("amt") double amt);
}


