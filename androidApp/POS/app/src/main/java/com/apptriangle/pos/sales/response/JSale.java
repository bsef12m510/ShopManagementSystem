package com.apptriangle.pos.sales.response;

import com.apptriangle.pos.model.Product;

import java.util.List;

/**
 * Created by zeeshan on 5/8/2018.
 */
public class JSale {
    public int invoiceId;
    public String str_invoiceId ;
    public int shopID ;
    public String agentID ;
    public String cust_name ;
    public String cust_phone ;
    public String dlr_details ;
    public String apiKey ;
    public List<Product> products ;
    public double amount_paid ;
    public double total_amount ;
}
