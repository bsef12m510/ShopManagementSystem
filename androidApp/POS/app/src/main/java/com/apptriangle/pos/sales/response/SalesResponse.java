package com.apptriangle.pos.sales.response;

import com.apptriangle.pos.model.Product;

import java.util.List;

/**
 * Created by zeeshan on 3/31/2018.
 */
public class SalesResponse {
    public String apiKey, discount,
            cust_name,
            cust_phone;
    public Double total_amount ,amount_paid;

    public List<Product> products;
}
