package com.apptriangle.pos.model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by zeeshan on 5/3/2018.
 */
public class CSale {
    public int shop_id ;
    public int sale_id ;
    public ArrayList<Product> cproduct ;
    public User agent ;
    public int prod_qty ;
    public String saleDate ;
    public String saleTime ;
    public String cust_name ;
    public String cust_phone ;
    public Double amount_paid, total_amount;

    public CSale(Sale s){
        this.shop_id = s.shop_id;
        this.sale_id = s.sale_id;
        this.cproduct = new ArrayList<>();
        this.cproduct.add(s.cproduct);
        this.agent = s.agent;
        this.prod_qty = s.prod_qty;
        this.saleDate = s.saleDate;
        this.saleTime = s.saleTime;
        this.cust_name = s.cust_name;
        this.cust_phone = s.cust_phone;
    }
}
