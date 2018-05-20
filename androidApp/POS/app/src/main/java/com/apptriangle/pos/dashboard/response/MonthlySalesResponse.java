package com.apptriangle.pos.dashboard.response;

import java.sql.Date;

public class MonthlySalesResponse {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount = saleAmount;
    }

    String date;
    double saleAmount;
}
