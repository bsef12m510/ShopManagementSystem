package com.apptriangle.pos.purchase.response;

import com.apptriangle.pos.model.Product;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by zeeshan on 4/22/2018.
 */
public class PurchaseResponse {
    public String apiKey,dlr_name,dlr_phone,dlr_info;
    public Integer purch_id;
    public Double total_amount ,amount_paid;
    public List<JProduct> products;
    public Timestamp purch_dtime;


}
