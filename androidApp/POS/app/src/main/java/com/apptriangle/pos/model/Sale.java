package com.apptriangle.pos.model;

import java.sql.Timestamp;

/**
 * Created by zeeshan on 5/3/2018.
 */
public class Sale {
    public int shop_id ;
    public int sale_id ;
    public Product cproduct ;
    public User agent ;
    public int prod_qty ;
    public String saleDate ;
    public String saleTime ;
    public String cust_name ;
    public String cust_phone ;
}
