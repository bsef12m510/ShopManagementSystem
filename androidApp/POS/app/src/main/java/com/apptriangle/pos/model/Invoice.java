package com.apptriangle.pos.model;

import java.util.List;

public class Invoice {
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
