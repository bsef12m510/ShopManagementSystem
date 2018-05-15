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

}
